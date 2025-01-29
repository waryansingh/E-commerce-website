package com.ecom;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.entity.AdminFrontEndLoaderData;

@WebServlet(
        name = "AdminFrontEndLoader",
        urlPatterns = { "/AdminFrontEndLoader"}
)
public class AdminFrontEndLoader extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AdminFrontEndLoaderData data=null;
	private Connection connect=null;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().setAttribute("admin-front-end-data",map_data((String)request.getSession().getAttribute("Admin-username")));
		request.getRequestDispatcher("Admin_home_page.jsp").forward(request, response);
	}
	
	final private void get_admin_profile_details(String admin_username) {
		String query1=String.format("SELECT admin_fname,admin_lname,admin_email,admin_phone,admin_profile_picture,admin_govId_picture FROM admin_personal_details WHERE admin_username='%s'", admin_username);
		String query2=String.format("SELECT admin_address,admin_city,admin_state,admin_zip_code FROM admin_address WHERE admin_username='%s'", admin_username);
		try {
			ResultSet result1=connect.createStatement().executeQuery(query1);
			if(result1.next()) {
				data.set_profile("admin_full_name", result1.getString("admin_fname")+" "+result1.getString("admin_lname"));
				data.set_profile("admin_email", result1.getString("admin_email"));
				data.set_profile("admin_phone", result1.getString("admin_phone"));
				data.set_profile("admin_profile_picture", result1.getString("admin_profile_picture"));
				data.set_profile("admin_govId_picture", result1.getString("admin_govId_picture"));
			}
			result1.close();
			ResultSet result2=connect.createStatement().executeQuery(query2);				
			if(result2.next()) {
				data.set_profile("admin_address", result2.getString("admin_address")+", "+result2.getString("admin_city")+", "+result2.getString("admin_state")+", "+Integer.toString(result2.getInt("admin_zip_code")));
			}
			result2.close();
		}
		catch(Exception e) {e.printStackTrace();close_connection();}
	}
	
	final private void get_users_counts() {
		String query1="SELECT COUNT(CASE WHEN admin_status = 1 THEN 1 END) AS registered_admins, COUNT(CASE WHEN admin_status = 0 THEN 1 END) AS admin_requests, COUNT(CASE WHEN admin_status = 3 THEN 1 END) AS blacklisted_admins FROM admin_personal_details";
		String query2="SELECT COUNT(CASE WHEN vendor_status = 1 THEN 1 END) AS registered_vendors, COUNT(CASE WHEN vendor_status = 0 THEN 1 END) AS vendor_requests, COUNT(CASE WHEN vendor_status = 3 THEN 1 END) AS blacklisted_vendors FROM vendor_personal_details";
		String query3="SELECT COUNT(CASE WHEN client_status = 1 THEN 1 END) AS registered_clients, COUNT(CASE WHEN client_status = 3 THEN 1 END) AS blacklisted_clients FROM client_personal_details";
		try {
			ResultSet result1=connect.createStatement().executeQuery(query1);
			if(result1.next()) {
				data.set_count("registered_admins", result1.getInt("registered_admins"));
				data.set_count("admin_requests", result1.getInt("admin_requests"));
				data.set_count("blacklisted_admins", result1.getInt("blacklisted_admins"));
			}
			result1.close();
			ResultSet result2=connect.createStatement().executeQuery(query2);
			if(result2.next()) {
				data.set_count("registered_vendors", result2.getInt("registered_vendors"));
				data.set_count("vendor_requests", result2.getInt("vendor_requests"));
				data.set_count("blacklisted_vendors", result2.getInt("blacklisted_vendors"));
			}
			result2.close();
			ResultSet result3=connect.createStatement().executeQuery(query3);
			if(result3.next()) {
				data.set_count("registered_clients", result3.getInt("registered_clients"));
				data.set_count("blacklisted_clients", result3.getInt("blacklisted_clients"));
			}
			result3.close();
		}
		catch(Exception e) {e.printStackTrace();close_connection();}
	}
	
	final private void get_orders_count() {
		String query="SELECT COUNT(CASE WHEN order_status = 1 THEN 1 END) AS delivered_orders , COUNT(CASE WHEN order_status = 0 THEN 1 END) AS pre_delivery_orders , COUNT(CASE WHEN order_status = 3 THEN 1 END) AS order_refunded, COUNT(CASE WHEN order_status = 2 THEN 1 END) AS order_canceled, COUNT(CASE WHEN order_status = -1 THEN 1 END) AS order_refund_request FROM orders";
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()){
				data.set_count("delivered_orders",result.getInt("delivered_orders"));
				data.set_count("pre_delivery_orders",result.getInt("pre_delivery_orders"));
				data.set_count("order_refunded",result.getInt("order_refunded"));
				data.set_count("order_canceled",result.getInt("order_canceled"));
				data.set_count("order_refund_request",result.getInt("order_refund_request"));
			}
		}catch(Exception e) {e.printStackTrace();}
	}
	
	final private void get_products_count() {
		String clothing_query="SELECT COUNT(product_id) AS clothing_product_count, COUNT(CASE WHEN product_status = 1 THEN 1 END) AS registered_clothing_product, COUNT(CASE WHEN product_status = 0 THEN 1 END) AS clothing_product_requests, COUNT(CASE WHEN product_status = 3 THEN 1 END) AS clothing_product_blacklisted FROM clothing_products";
		String electronics_query="SELECT COUNT(product_id) AS electronics_product_count, COUNT(CASE WHEN product_status = 1 THEN 1 END) AS registered_electronics_products, COUNT(CASE WHEN product_status = 0 THEN 1 END) AS electronic_product_request, COUNT(CASE WHEN product_status = 3 THEN 1 END) AS electronic_product_blacklisted FROM electronics_products";
		String food_query="SELECT COUNT(product_id) AS food_product_count, COUNT(CASE WHEN product_status = 1 THEN 1 END) AS registered_food_products, COUNT(CASE WHEN product_status = 0 THEN 1 END) AS food_product_request, COUNT(CASE WHEN product_status = 3 THEN 1 END) AS food_product_blacklisted FROM food_products";
		try {
			ResultSet result1=connect.createStatement().executeQuery(clothing_query);
			ResultSet result2=connect.createStatement().executeQuery(electronics_query);
			ResultSet result3=connect.createStatement().executeQuery(food_query);
			if(result1.next() && result2.next() && result3.next()) {
				data.set_count("product_request", (result1.getInt("clothing_product_requests") + result2.getInt("electronic_product_request") + result3.getInt("food_product_request")));
				data.set_count("registered_products", (result1.getInt("registered_clothing_product") + result2.getInt("registered_electronics_products")+ result3.getInt("registered_food_products")));
				data.set_count("blacklisted_products", (result1.getInt("clothing_product_blacklisted") + result2.getInt("electronic_product_blacklisted")+ result3.getInt("food_product_blacklisted")));
				data.set_count("clothing_product", result1.getInt("clothing_product_count"));
				data.set_count("electronics_product", result2.getInt("electronics_product_count"));
				data.set_count("food_product", result3.getInt("food_product_count"));
			}
			result1.close();result2.close();result3.close();
		}
		catch(Exception e) {e.printStackTrace();close_connection();}
	}
	final private void get_blog_posts_count() {
		String query="SELECT count(blog_id) AS posts_count FROM blog_posts";
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				data.set_count("blog_posts",result.getInt("posts_count"));
			}
		}catch(Exception e) {e.printStackTrace();}
	}
	
	final private AdminFrontEndLoaderData map_data(String admin_username) {
		set_connection();
		create_object_of_admin_entity();
		get_admin_profile_details(admin_username);
		get_users_counts();
		get_products_count();
		get_blog_posts_count();
		get_orders_count();
		close_connection();
		return data;
	}
	final private void create_object_of_admin_entity() {if(data==null) {data=new AdminFrontEndLoaderData();}}
	final private void set_connection() {
		try {if(connect==null || connect.isClosed()) {connect=ConnectToDB.create_connection();}} 
		catch (SQLException e) {e.printStackTrace();}
	}
	final private void close_connection() {
		try {if(!connect.isClosed() || connect!=null) connect.close();} 
		catch (SQLException e) {e.printStackTrace();}
	}
}
