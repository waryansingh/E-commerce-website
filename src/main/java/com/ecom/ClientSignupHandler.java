package com.ecom;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.entity.ClientDetailsTemp;
import com.entity.EmailDetails;

@WebServlet(
        name = "ClientSignupHandler",
        urlPatterns = { "/ClientSignupHandler"}
)
public class ClientSignupHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connect=null;
	private SecurityCodeHandler securitycode=null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("type")!=null && request.getParameter("type").equals("otp")) {
			if(request.getParameter("security-code")==null || request.getParameter("security-code").isEmpty()) {response.sendRedirect("Client_login_signup.jsp?msg=Enter the OTP.");return;}
			StringBuilder code=new StringBuilder(request.getParameter("security-code"));
			if(this.securitycode.validate_security_code(code)) {
				ClientDetailsTemp temp_details=(ClientDetailsTemp)request.getSession().getAttribute("client_temp_signup_details");
				if(register_new_client(temp_details.get_data("client_personal_details"),temp_details.get_data("client_personal_address"))) {
					temp_details=null;request.getSession().removeAttribute("client_temp_signup_details");this.securitycode=null;response.sendRedirect("index");return;
				}else {response.sendRedirect("Client_login_signup.jsp?msg=Client Cannnot be Registered.");return;}
			}else {this.securitycode=null;response.sendRedirect("Client_login_signup.jsp?msg=Invalid OTP.");return;}
		}
		String []client_personal_details= {
				request.getParameter("client-first-name"),
				request.getParameter("client-last-name"),
				request.getParameter("client-email"),
				request.getParameter("client-phone-number"),
				request.getParameter("client-username"),
				request.getParameter("client-password")
		};
		String []client_personal_address= {
				request.getParameter("client-address-type"),
				request.getParameter("client-address"),
				request.getParameter("client-city"),
				request.getParameter("client-state"),
				request.getParameter("client-landmark"),
				request.getParameter("client-zip-code")
		};
		if(!InputValidator.is_empty(client_personal_details) && !InputValidator.is_empty(client_personal_address)){
			if(InputValidator.is_valid_address_details(client_personal_address)) {
				if(request.getParameter("client-confirm-password").equals(client_personal_details[5])) {
					if(InputValidator.is_valid_text(client_personal_details[0]) && InputValidator.is_valid_text(client_personal_details[1])) {
						if(InputValidator.is_valid_email(client_personal_details[2]) && InputValidator.is_valid_phonenumber(client_personal_details[3]) && !InputValidator.contains_sql(client_personal_details[4]) && !InputValidator.contains_sql(client_personal_details[5])) {
							if(is_new_client(client_personal_details)) {
								request.getSession().setAttribute("client_temp_signup_details",new ClientDetailsTemp(client_personal_details,client_personal_address));
								this.securitycode=new SecurityCodeHandler();
								send_security_code_mail(client_personal_details,this.securitycode);
								request.getSession().setAttribute("Enter_otp",true);
								response.sendRedirect("Client_login_signup.jsp?msg=Enter Security Code.");return;
							}else {response.sendRedirect("Client_login_signup.jsp?msg=Client Already Registerd.");return;}
						}else {response.sendRedirect("Client_login_signup.jsp?msg=Re-check Email or PhoneNumber or Username. Username and Password must be alphanumeric.");return;}
					}else {response.sendRedirect("Client_login_signup.jsp?msg=Invalid name.");return;}
				}else {response.sendRedirect("Client_login_signup.jsp?msg=Passwords are not matching.");return;}
			}else {response.sendRedirect("Client_login_signup.jsp?msg=Invalid address details.");return;}
		}else {response.sendRedirect("Client_login_signup.jsp?msg=Fill the empty fields.");return;}
	}
	
	final private void send_security_code_mail(String []client_personal_details,SecurityCodeHandler securitycode) {
		new MailHandler(new EmailDetails("client","client_security_mail_text",client_personal_details[0]+" "+client_personal_details[1],client_personal_details[2],securitycode.get_security_code())).send_mail();
	}
	
	final private boolean is_new_client(String []client_personal_details) {
		set_connection();
		String query=String.format("SELECT client_username,client_phone,client_email FROM client_personal_details WHERE client_username='%s'",client_personal_details[4]);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				if(result.getString("client_username").equals(client_personal_details[4]) || result.getString("client_phone").equals(client_personal_details[3]) || result.getString("client_email").equals(client_personal_details[2])) {
					close_connection();
					return false;
				}
			}
		}catch(Exception e){e.printStackTrace();close_connection();return false;}
		close_connection();
		return true;
	}
	
	final private boolean register_new_client(String []client_personal_details,String []client_personal_address) {
		set_connection();
		String query1="INSERT INTO client_personal_details (client_fname,client_lname,client_email,client_phone,client_username,client_password) VALUES(?,?,?,?,?,?)";
		String query2="INSERT INTO client_address (client_username,client_address_type,client_address,client_city,client_state,client_landmark,client_zip_code) VALUES(?,?,?,?,?,?,?)";
		try {
			PreparedStatement statement1=connect.prepareStatement(query1);
			for(int i=0;i<client_personal_details.length;i++) {
				statement1.setString((i+1),client_personal_details[i]);
			}
			PreparedStatement statement2=connect.prepareStatement(query2);
			statement2.setString(1,client_personal_details[4]);
			for(int i=0;i<client_personal_address.length;i++) {
				if(i==5) {statement2.setInt((i+2),Integer.parseInt(client_personal_address[i]));break;}
				statement2.setString((i+2),client_personal_address[i]);
			}
			statement1.execute();
			statement2.execute();
			connect.commit();
			statement1.close();
			statement2.close();
			close_connection();
			System.out.println("Client registered");
			return true;
		}catch(Exception e) {e.printStackTrace();
		try {connect.rollback();} 
		catch (SQLException e1) {e1.printStackTrace();}
		close_connection();
		}
		return false;
	}
	
	final private void set_connection() {
		try {if(connect==null || connect.isClosed()) {connect=ConnectToDB.create_connection();}} 
		catch (SQLException e) {e.printStackTrace();}
	}
	final private void close_connection() {
		try {if(!connect.isClosed() || connect!=null) connect.close();} 
		catch (SQLException e) {e.printStackTrace();}
	}
}
