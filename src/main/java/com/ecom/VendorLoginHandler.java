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
        name = "VendorLoginHandler",
        urlPatterns = { "/VendorLoginHandler"}
)
public class VendorLoginHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection connect=null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().setAttribute("Vendor-username",null);
		String []vendor_login_details= {
			request.getParameter("vendor-login-username"),
			request.getParameter("vendor-login-password")
		};
		if(!InputValidator.is_empty(vendor_login_details)) {
			if(!InputValidator.contains_sql(vendor_login_details[0]) && !InputValidator.contains_sql(vendor_login_details[0])) {
				switch(is_valid_vendor(vendor_login_details)) {
				case 0->{response.sendRedirect("Vendor_login_signup.jsp?msg=Vendor Status : Pending. You cannot login into system. Try again later."); return;}
				case 1->{request.getSession().setAttribute("Vendor-username",vendor_login_details[0]);response.sendRedirect("Vendor_home_page.jsp");}
				case 3->{response.sendRedirect("Vendor_login_signup.jsp?msg=Vendor Banned."); return;}
				default->{response.sendRedirect("Vendor_login_signup.jsp?msg=Wrong Vendor Details."); return;}
				}
			}
			else {response.sendRedirect("Vendor_login_signup.jsp?msg=Special characters are not allowed."); return;}
		}
		else {response.sendRedirect("Vendor_login_signup.jsp?msg=Fill the empty fields."); return;}
	}
	
	final private int is_valid_vendor(String []vendor_login_details){
		String []vendor_from_db=vendor_exists(vendor_login_details);
		if(vendor_from_db!=null) {
			if(vendor_from_db[0].equals(vendor_login_details[0]) && vendor_from_db[1].equals(vendor_login_details[1])) {
				return Integer.parseInt(vendor_from_db[2]);
			}
		}
		return -1;
	}
	
	final private String []vendor_exists(String []vendor_login_details) {
		set_connection();
		String query="SELECT vendor_username, vendor_password, vendor_status FROM vendor_personal_details WHERE vendor_username=? and vendor_password=?";
		try (PreparedStatement statement=connect.prepareStatement(query)){
			statement.setString(1,vendor_login_details[0]);
			statement.setString(2,vendor_login_details[1]);
			ResultSet result=statement.executeQuery();
			if(result.next()) {
				String []vendor_from_db= {
					result.getString("vendor_username"),
					result.getString("vendor_password"),
					Integer.toString(result.getInt("vendor_status"))
				};
				result.close();
				statement.close();
				close_connection();
				return vendor_from_db;
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
		try {if(!connect.isClosed() || connect!=null) connect.close();} 
		catch (SQLException e) {e.printStackTrace();}
	}
}
