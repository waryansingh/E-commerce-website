package com.ecom;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.entity.AdminDetailsTemp;
import com.entity.EmailDetails;

@MultipartConfig
@WebServlet(
        name = "AdminSignupHandler",
        urlPatterns = { "/AdminSignupHandler"}
)
public class AdminSignupHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connect=null;
	private SecurityCodeHandler securitycode=null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("type")!=null && request.getParameter("type").equals("otp")) {
			if(request.getParameter("security-code")==null || request.getParameter("security-code").isEmpty()) {response.sendRedirect("Admin_login_signup.jsp?msg=Enter the OTP.");return;}
			StringBuilder code=new StringBuilder(request.getParameter("security-code"));
			if(securitycode.validate_security_code(code)) {
				AdminDetailsTemp temp_details=(AdminDetailsTemp)request.getSession().getAttribute("admin_temp_signup_details");
				if(register_new_admin((String[])temp_details.get_data("admin_personal_details"),(String[])temp_details.get_data("admin_address"),(InputStream[])temp_details.get_data("images_stream"),(String[])temp_details.get_data("images_name"))) {
					temp_details=null;request.getSession().removeAttribute("admin_temp_signup_details");securitycode=null;response.sendRedirect("Admin_login_signup.jsp?msg=Admin Registered.");return;
				}else {response.sendRedirect("Admin_login_signup.jsp?msg=Admin Cannnot be Registered.");return;}
			}else {response.sendRedirect("Admin_login_signup.jsp?msg=Invalid OTP.");return;}
		}
		String []admin_personal_details= {
				request.getParameter("firstname"),
				request.getParameter("lastname"),
				request.getParameter("email"),
				request.getParameter("phone"),
				request.getParameter("username"),
				request.getParameter("password")
		};
		String []admin_address= {
				request.getParameter("address"),
				request.getParameter("city"),
				request.getParameter("state"),
				request.getParameter("zip")
		};
		Part []images= {
			request.getPart("profile-pic"),
			request.getPart("gov-id")
		};
		if(images[0]==null || images[1]==null) {response.sendRedirect("Admin_login_signup.jsp?msg=Upload all Images."); return;}
		
		String []images_name= {
				images[0].getSubmittedFileName(),
				images[1].getSubmittedFileName()
		};
		if(!InputValidator.is_empty(admin_personal_details) && !InputValidator.is_empty(admin_address)){
			if(InputValidator.is_valid_address_details(admin_address)) {
				if(request.getParameter("confirmpassword").equals(admin_personal_details[5])) {
					if(InputValidator.is_valid_text(admin_personal_details[0]) && InputValidator.is_valid_text(admin_personal_details[1])) {
						if(InputValidator.is_valid_email(admin_personal_details[2]) && InputValidator.is_valid_phonenumber(admin_personal_details[3]) && !InputValidator.contains_sql(admin_personal_details[4]) && !InputValidator.contains_sql(admin_personal_details[5])) {
							if(ImageValidator.is_valid_image_type(images_name) && ImageValidator.is_valid_image_size(images)) {
								if(is_new_admin(admin_personal_details)) {
									InputStream []images_stream={
											images[0].getInputStream(),
											images[1].getInputStream()
									};
									request.getSession().setAttribute("admin_temp_signup_details",new AdminDetailsTemp(admin_personal_details,admin_address,images_stream,images_name));
									this.securitycode=new SecurityCodeHandler();
									send_security_code_mail(admin_personal_details,this.securitycode);
									request.getSession().setAttribute("Enter_otp",true);
									response.sendRedirect("Admin_login_signup.jsp?msg=Enter Security Code.");return;
								}else {response.sendRedirect("Admin_login_signup.jsp?msg=Admin is already registered with this username or email or phone number.");return;}
							}else {response.sendRedirect("Admin_login_signup.jsp?msg=Image must be (.jpg, .jpeg, .png, .jfif) type and size must be under 2mb.");return;}
						}else {response.sendRedirect("Admin_login_signup.jsp?msg=Re-check Email or PhoneNumber or Username. Username and Password must be alphanumeric.");return;}
					}else {response.sendRedirect("Admin_login_signup.jsp?msg=Invalid Name.");return;}
				}else {response.sendRedirect("Admin_login_signup.jsp?msg=Passwords are not matching.");return;}
			}else {response.sendRedirect("Admin_login_signup.jsp?msg=Invalid Address Details.");return;}
		}else {response.sendRedirect("Admin_login_signup.jsp?msg=Fill the empty fields.");return;}
	}
	
	final private void send_security_code_mail(String []admin_personal_details,SecurityCodeHandler securitycode) {
		new MailHandler(new EmailDetails("admin","admin_security_mail_text",admin_personal_details[0]+" "+admin_personal_details[1],admin_personal_details[2],securitycode.get_security_code())).send_mail();
	}
	
	final private boolean is_new_admin(String []admin_personal_details) {
		set_connection();
		String query=String.format("SELECT admin_email, admin_phone, admin_username FROM admin_personal_details WHERE admin_username='%s'",admin_personal_details[4]);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				if(result.getString("admin_email").equals(admin_personal_details[2]) || result.getString("admin_phone").equals(admin_personal_details[3]) || result.getString("admin_username").equals(admin_personal_details[4]))
					return false;
			}
			else {
				close_connection();
				return true;
			}
		} 
		catch (SQLException e) {e.printStackTrace();}
		close_connection();
		return false;
	}
	
	
	final private boolean register_new_admin(String []admin_personal_details, String []admin_address,InputStream []images,String []images_name) {
		set_connection();
		String query1="INSERT INTO admin_personal_details(admin_fname,admin_lname,admin_email,admin_phone,admin_username,admin_password,admin_profile_picture,admin_govId_picture) VALUES(?,?,?,?,?,?,?,?)";
		String query2="INSERT INTO admin_address(admin_username, admin_address, admin_city, admin_state, admin_zip_code) VALUES (?,?,?,?,?)";
		images_name=ImageValidator.get_new_image_name(images_name,admin_personal_details[4]);
		String upload_path1="E:/Dynamic Web Project/src/main/webapp/Admin Profile Pictures/";//+images_name[0]
		String upload_path2="E:/Dynamic Web Project/src/main/webapp/Admin GovID/";//+images_name[1]
		File upload1=new File(upload_path1);
		File upload2=new File(upload_path2);
		try {	
			if(!upload1.exists()) {upload1.mkdir();}
			if(!upload2.exists()) {upload2.mkdir();}
			
			upload_path1="E:/Dynamic Web Project/src/main/webapp/Admin Profile Pictures/"+images_name[0];
			upload_path2="E:/Dynamic Web Project/src/main/webapp/Admin GovID/"+images_name[1];
			
			PreparedStatement statement1=connect.prepareStatement(query1);
			PreparedStatement statement2=connect.prepareStatement(query2);
			
			for(int i=0;i<admin_personal_details.length;i++) {
				statement1.setString((i+1), admin_personal_details[i]);
			}
			statement1.setString(7, images_name[0]);
			statement1.setString(8, images_name[1]);
			
			statement2.setString(1, admin_personal_details[4]);
			for(int i=0;i<admin_address.length;i++) {
				if(i==3) {
					statement2.setInt((i+2), Integer.parseInt(admin_address[i]));
					break;
				}
				statement2.setString((i+2), admin_address[i]);
			}
			
			statement1.execute();
			statement2.execute();

			byte[] buffer = new byte[1024];
			FileOutputStream outputStream1 = new FileOutputStream(new File(upload_path1));
            int bytesRead1;
            while ((bytesRead1 = images[0].read(buffer)) != -1) {
                outputStream1.write(buffer, 0, bytesRead1);
            }
            
            FileOutputStream outputStream2 = new FileOutputStream(new File(upload_path2));
			int bytesRead2;
            while ((bytesRead2 = images[1].read(buffer)) != -1) {
                outputStream2.write(buffer, 0, bytesRead2);
            }
			buffer=null;
			connect.commit();
			statement1.close();
			statement2.close();
			outputStream1.close();
			outputStream2.close();
			connect.close();
			close_connection();
			return true;
		}
		catch(Exception e) {
			new File(upload_path1).delete();
			new File(upload_path2).delete();
			try {connect.rollback();} catch (SQLException e1) {e1.printStackTrace();}
			System.out.print(e.getMessage());
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
