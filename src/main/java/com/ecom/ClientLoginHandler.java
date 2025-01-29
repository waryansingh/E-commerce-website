package com.ecom;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(
        name = "ClientLoginHandler",
        urlPatterns = { "/ClientLoginHandler"}
)
public class ClientLoginHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connect=null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String []client_login_details= {
				request.getParameter("client-login-username"),
				request.getParameter("client-login-password")
		};
		boolean set_cookie=request.getParameter("client-remember-me")==null?false:true;
		if(!InputValidator.is_empty(client_login_details)) {
			if(!InputValidator.contains_sql(client_login_details[0]) && !InputValidator.contains_sql(client_login_details[1])) {
				if(is_valid_client(client_login_details)){
					if(set_cookie){
						remove_existing_cookie(request.getCookies(),response);
						Cookie client_cookie=new Cookie("Client_username_cookie",client_login_details[0]);
						client_cookie.setMaxAge(60*60*24*365);
						response.addCookie(client_cookie);
					}
					request.getSession().setAttribute("Client_username",client_login_details[0]);
					response.sendRedirect("index");return;
				}else {response.sendRedirect("Client_login_signup.jsp?msg=Invalid Client details.");return;}
			}else {response.sendRedirect("Client_login_signup.jsp?msg=Special characters are not allowed.");return;}
		}else {response.sendRedirect("Client_login_signup.jsp?msg=Fields cannot be empty.");return;}
	}
	
	final private void remove_existing_cookie(Cookie []cookies, HttpServletResponse response) {
		for(Cookie cookie:cookies) {
			if(cookie.getName().equals("Client_username_cookie")) {
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
	}

	final private boolean is_valid_client(String []client_login_details) {
		String []client_from_db=client_exists(client_login_details);
		if(client_from_db!=null && client_from_db[0].equals(client_login_details[0]) && client_from_db[1].equals(client_login_details[1])) {
			return true;
		}
		return false;
	}
	
	final private String[] client_exists(String []client_login_details) {
		set_connection();
		String query=String.format("SELECT client_username, client_password FROM client_personal_details WHERE client_username='%s' AND client_password='%s'", client_login_details[0],client_login_details[1]);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				String []details={
						result.getString("client_username"),
						result.getString("client_password")
				};
				close_connection();
				return details;
			}
		}catch(Exception e) {e.printStackTrace();}
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
