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

@WebServlet(
        name = "AdminLoginHandler",
        urlPatterns = { "/AdminLoginHandler"}
)
public class AdminLoginHandler extends HttpServlet {
	private static final long serialVersionUID = 1L; 
    private Connection connect=null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().setAttribute("Admin-username",null);
		String []admin_login_details={
			request.getParameter("login-username"),
			request.getParameter("login-password")
		};
		if(!InputValidator.is_empty(admin_login_details)) {
			if(!InputValidator.contains_sql(admin_login_details[0]) && !InputValidator.contains_sql(admin_login_details[1])) {
				switch(is_valid_admin(admin_login_details)) {
				case 0->{response.sendRedirect("Admin_login_signup.jsp?msg=Admin Status : Pending. You cannot login into system. Try again later.");return;}
				case 1->{request.getSession().setAttribute("Admin-username",admin_login_details[0]); response.sendRedirect("AdminFrontEndLoader");return;}
				case 3->{response.sendRedirect("Admin_login_signup.jsp?msg=Admin Banned.");return;}
				default->{response.sendRedirect("Admin_login_signup.jsp?msg=Wrong Admin details.");return;}
				}
			}else {response.sendRedirect("Admin_login_signup.jsp?msg=Special characters are not allowed.");return;}
		}else {response.sendRedirect("Admin_login_signup.jsp?msg=Fields cannot be empty.");return;}
	}
	
	final private int is_valid_admin(String []admin_login_details){
		String []admin_from_db=admin_exists(admin_login_details);
		if(admin_from_db!=null) {
			if(admin_from_db[0].equals(admin_login_details[0]) && admin_from_db[1].equals(admin_login_details[1])) {
				return Integer.parseInt(admin_from_db[2]);
			}
		}
		return -1;
	}
	
	final private String []admin_exists(String []admin_login_details) {
		set_connection();
		String query="SELECT admin_username, admin_password, admin_status FROM admin_personal_details WHERE admin_username=? and admin_password=?";
		try (PreparedStatement statement=connect.prepareStatement(query)){
			statement.setString(1,admin_login_details[0]);
			statement.setString(2,admin_login_details[1]);
			ResultSet result=statement.executeQuery();
			if(result.next()) {
				String []admin_from_db= {
					result.getString("admin_username"),
					result.getString("admin_password"),
					Integer.toString(result.getInt("admin_status"))
				};
				result.close();
				statement.close();
				close_connection();
				return admin_from_db;
			}
		}
		catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	final private void set_connection() {
		try {if(connect==null || connect.isClosed()) {connect=ConnectToDB.create_connection();}
	} catch (SQLException e) {e.printStackTrace();}
	}
	final private void close_connection() {
		try {if(!connect.isClosed()|| connect!=null) connect.close();} 
		catch (SQLException e) {e.printStackTrace();}
	}
}
