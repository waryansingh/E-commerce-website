package com.ecom;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.entity.ClientFavouriteAddress;
import com.entity.ClientPersonalDetails;
import com.entity.EmailDetails;
import com.entity.ProductToBuy;

@WebServlet(
        name = "ClientOperationsHandler",
        urlPatterns = {"/ClientOperationsHandler"}
)
public class ClientOperationsHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connect=null;
	private SecurityCodeHandler securitycode=null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("Client_username")==null)set_session(request);
		if(request.getSession().getAttribute("Client_id")==null || (int)request.getSession().getAttribute("Client_id")==-1)set_client_id((String)request.getSession().getAttribute("Client_username"),request);
		
		switch(request.getParameter("operation")) {
		case "cart"->{
			if(request.getParameter("product_type").equals("clothing")) {
				String []product_details= {
						request.getParameter("product_id"),
						request.getParameter("product_type"),
						request.getParameter("product_size").trim()
				};
				if(product_details[2]==null || product_details[2].isBlank() || product_details[2].isEmpty() || product_details[2].equals("N/A") || (!product_details[2].equals("S") && !product_details[2].equals("M") && !product_details[2].equals("L") && !product_details[2].equals("XL") && !product_details[2].equals("XXL"))) {
					request.getSession().setAttribute("product_msg","Please choose a size option.");
					response.sendRedirect("ClientFrontEndLoader?product-id="+product_details[0]+"&product-type="+product_details[1]);return;
				}
				for(String detail:product_details) {
					if(InputValidator.contains_sql(detail)) {response.sendRedirect("index");return;}
				}
				if((int)request.getSession().getAttribute("Client_id")==-1 || (String)request.getSession().getAttribute("Client_username")==null) {response.sendRedirect("Client_login_signup.jsp");return;}
				add_to_cart(product_details,(int)request.getSession().getAttribute("Client_id"),(String)request.getSession().getAttribute("Client_username"));
				response.sendRedirect("ClientFrontEndLoader?product-id="+product_details[0]+"&product-type="+product_details[1]);return;
			}else {
				String []product_details= {
						request.getParameter("product_id"),
						request.getParameter("product_type")
				};
				for(String detail:product_details) {
					if(InputValidator.contains_sql(detail)) {response.sendRedirect("index");return;}
				}
				if((int)request.getSession().getAttribute("Client_id")==-1 || (String)request.getSession().getAttribute("Client_username")==null) {response.sendRedirect("Client_login_signup.jsp");return;}
				add_to_cart(product_details,(int)request.getSession().getAttribute("Client_id"),(String)request.getSession().getAttribute("Client_username"));
				response.sendRedirect("ClientFrontEndLoader?product-id="+product_details[0]+"&product-type="+product_details[1]);return;
			}
		}
		case "remove-from-cart"->{
			remove_from_cart(Integer.parseInt(request.getParameter("cart_id")));
			response.sendRedirect("ClientFrontEndLoader?operation=load-cart");return;
		}
		case "increase-quantity"->{
			increase_quantity_in_cart(Integer.parseInt(request.getParameter("cart_id")),Integer.parseInt(request.getParameter("current_quantity")),Integer.parseInt(request.getParameter("product_id")),request.getParameter("product_type"));
			response.sendRedirect("ClientFrontEndLoader?operation=load-cart");return;
		}
		case "decrease-quantity"->{
			decrease_quantity_in_cart(Integer.parseInt(request.getParameter("cart_id")),Integer.parseInt(request.getParameter("current_quantity")));
			response.sendRedirect("ClientFrontEndLoader?operation=load-cart");return;
		}
		case "buy"->{
			if(request.getParameter("product_type").equals("clothing")) {
				String []product_details= {
						request.getParameter("product_id"),
						request.getParameter("product_type"),
						request.getParameter("product_size").trim()
				};
				if(product_details[2]==null || product_details[2].isBlank() || product_details[2].isEmpty() || product_details[2].equals("N/A") || (!product_details[2].equals("S") && !product_details[2].equals("M") && !product_details[2].equals("L") && !product_details[2].equals("XL") && !product_details[2].equals("XXL"))) {
					request.getSession().setAttribute("product_msg","Please choose a size option.");
					response.sendRedirect("ClientFrontEndLoader?product-id="+product_details[0]+"&product-type="+product_details[1]);return;
				}
				for(String detail:product_details)if(InputValidator.contains_sql(detail)) {response.sendRedirect("index.jsp");return;}
				if((int)request.getSession().getAttribute("Client_id")==-1 || (String)request.getSession().getAttribute("Client_username")==null) {response.sendRedirect("Client_login_signup.jsp");return;}
				request.getSession().setAttribute("checkout-products",load_product_for_checkout(product_details));
				request.getSession().removeAttribute("random-food-product");
				request.getSession().setAttribute("client-address",load_client_address((String)request.getSession().getAttribute("Client_username")));
				request.getSession().setAttribute("client-fav-address",get_favorite_address((String)request.getSession().getAttribute("Client_username")));
				request.getRequestDispatcher("Client_checkout_page.jsp").forward(request, response);return;
			}
			else {
				String []product_details= {
						request.getParameter("product_id"),
						request.getParameter("product_type")
				};
				for(String detail:product_details)if(InputValidator.contains_sql(detail)) {response.sendRedirect("index.jsp");return;}
				if((int)request.getSession().getAttribute("Client_id")==-1 || (String)request.getSession().getAttribute("Client_username")==null) {response.sendRedirect("Client_login_signup.jsp");return;}
				request.getSession().setAttribute("checkout-products",load_product_for_checkout(product_details));
				request.getSession().removeAttribute("random-food-product");
				request.getSession().setAttribute("client-address",load_client_address((String)request.getSession().getAttribute("Client_username")));
				request.getSession().setAttribute("client-fav-address",get_favorite_address((String)request.getSession().getAttribute("Client_username")));
				request.getRequestDispatcher("Client_checkout_page.jsp").forward(request, response);return;
			}
		}
		case "wishlist"->{
			String []product_details= {
					request.getParameter("product_id"),
					request.getParameter("product_type")
			};
			for(String detail:product_details) {
				if(InputValidator.contains_sql(detail)) {response.sendRedirect("index.jsp");return;}
			}
			if((int)request.getSession().getAttribute("Client_id")==-1 ||request.getSession().getAttribute("Client_username")==null) {response.sendRedirect("Client_login_signup.jsp");return;}
			add_to_wishlist(product_details,(int)request.getSession().getAttribute("Client_id"),(String)request.getSession().getAttribute("Client_username"));
			response.sendRedirect("ClientFrontEndLoader?product-id="+product_details[0]+"&product-type="+product_details[1]);return;
		}
		case "remove-from-wishlist"->{
			remove_from_wishlist(Integer.parseInt(request.getParameter("wishlist_id")));
			response.sendRedirect("ClientFrontEndLoader?operation=load-wishlist");return;
		}
		case "log-out"->{
			remove_existing_cookie(request.getCookies(),response);
			request.getSession().removeAttribute("Client_id");request.getSession().removeAttribute("Client_username");
			response.sendRedirect("index");return;
		}
		case "delete-client-account"->{
			remove_existing_cookie(request.getCookies(),response);
			delete_client_account(Integer.parseInt(request.getParameter("client-id")));
			request.getSession().removeAttribute("Client_id");request.getSession().removeAttribute("Client_username");
			response.sendRedirect("index");return;
		}
		case "buy-cart"->{
			if((int)request.getSession().getAttribute("Client_id")==-1 ||(String)request.getSession().getAttribute("Client_username")==null) {response.sendRedirect("Client_login_signup.jsp");return;}
			request.getSession().setAttribute("checkout-products",load_cart_for_checkout((int)request.getSession().getAttribute("Client_id"),request));
			if(request.getSession().getAttribute("client-address")==null)request.getSession().setAttribute("client-address",load_client_address((String)request.getSession().getAttribute("Client_username")));
			if(request.getSession().getAttribute("client-fav-address")==null)request.getSession().setAttribute("client-fav-address",get_favorite_address((String)request.getSession().getAttribute("Client_username")));
			request.getRequestDispatcher("Client_checkout_page.jsp").forward(request, response);return;
		}
		case "add-random-item"->{
			if((int)request.getSession().getAttribute("Client_id")==-1 ||(String)request.getSession().getAttribute("Client_username")==null) {response.sendRedirect("Client_login_signup.jsp");return;}
			@SuppressWarnings("unchecked")
			ArrayList<ProductToBuy> products=(ArrayList<ProductToBuy>)request.getSession().getAttribute("checkout-products");
			request.getSession().setAttribute("checkout-products",add_random_food_product(products));
			request.getSession().setAttribute("random-food-product",true);
			request.getRequestDispatcher("Client_checkout_page.jsp").forward(request, response);return;
		}
		case "remove-random-item"->{
			if(request.getSession().getAttribute("checkout-products")!=null) {
				@SuppressWarnings("unchecked")
				ArrayList<ProductToBuy> products=(ArrayList<ProductToBuy>)request.getSession().getAttribute("checkout-products");
				request.getSession().setAttribute("checkout-products",remove_random_food_product(products));
			}
			request.getRequestDispatcher("Client_checkout_page.jsp").forward(request, response);return;
		}
		case "add-new-address-from-checkout-page"->{
			String[] client_address= {
					request.getParameter("address-type"),
					request.getParameter("address"),
					request.getParameter("city"),
					request.getParameter("state"),
					request.getParameter("land-mark"),
					request.getParameter("zip-code")
			};
			if(InputValidator.is_empty(client_address)) {request.getRequestDispatcher("index.jsp").forward(request, response);return;}
			for(String address:client_address) {if(InputValidator.contains_sql(address)) {request.getRequestDispatcher("index.jsp").forward(request, response);return;}}
			add_new_address((String)request.getSession().getAttribute("Client_username"),client_address);
			request.getSession().setAttribute("client-address",load_client_address((String)request.getSession().getAttribute("Client_username")));
			request.getRequestDispatcher("Client_checkout_page.jsp").forward(request, response);return;
		}
		case "add-address"->{
			String[] client_address= {
					request.getParameter("address-type"),
					request.getParameter("address"),
					request.getParameter("city"),
					request.getParameter("state"),
					request.getParameter("land-mark"),
					request.getParameter("zip-code")
			};
			if(InputValidator.is_empty(client_address)) {
				for(String address:client_address) {if(InputValidator.contains_sql(address)) {request.getSession().setAttribute("error_msg","An unexpected error occurred while processing your request.");request.getRequestDispatcher("index").forward(request, response);return;}}
				if(!InputValidator.is_valid_number(client_address[5])) {request.getSession().setAttribute("error_msg","Incorrect Value.");request.getRequestDispatcher("index").forward(request, response);return;}
			}
			if(!add_new_address((String)request.getSession().getAttribute("Client_username"),client_address))request.getSession().setAttribute("error_msg","Limit reached for 3 Addresses.");
			response.sendRedirect("index");return;
		}
		case "add-fav-address"->{
			String[] client_fav_address= {
					request.getParameter("address-type"),
					request.getParameter("address"),
					request.getParameter("city"),
					request.getParameter("state"),
					request.getParameter("land-mark"),
					request.getParameter("zip-code"),
					request.getParameter("reciever-name"),
					request.getParameter("phone-number")
			};
			if(InputValidator.is_empty(client_fav_address)) {
				for(String address:client_fav_address) {if(InputValidator.contains_sql(address)) {request.getSession().setAttribute("error_msg","An unexpected error occurred while processing your request.");request.getRequestDispatcher("index").forward(request, response);return;}}
				if(!InputValidator.is_valid_number(client_fav_address[5]) && !InputValidator.is_valid_phonenumber(client_fav_address[7])) {request.getSession().setAttribute("error_msg","Incorrect Value.");request.getRequestDispatcher("index").forward(request, response);return;}
			}
			if(!add_new_fav_address((String)request.getSession().getAttribute("Client_username"),client_fav_address))request.getSession().setAttribute("error_msg","Limit reached for 3 Favourite Addresses.");
			response.sendRedirect("index");return;
		}
		case "manage-favourite-address"->{
			request.getSession().setAttribute("fav-address-selected",manage_fav_address(request.getParameter("fav-address-id"),request));
			request.getRequestDispatcher("Client_checkout_page.jsp").forward(request, response);return;
		}
		case "delete-favourite-address"->{
			String fav_address_id=request.getParameter("fav-address-id");
			if(!InputValidator.is_valid_number(fav_address_id)) {request.getSession().setAttribute("error_msg","An unexpected error occurred while processing your request.");request.getRequestDispatcher("index").forward(request, response);return;}
			delete_favourite_address(fav_address_id);
			response.sendRedirect("index");return;
		}
		case "delete-address"->{
			String address_id=request.getParameter("address-id");
			if(!InputValidator.is_valid_number(address_id)) {request.getSession().setAttribute("error_msg","An unexpected error occurred while processing your request.");request.getRequestDispatcher("index").forward(request, response);return;}
			delete_address(address_id);
			response.sendRedirect("index");return;
		}
		case "manage-address"->{
			request.getSession().setAttribute("selected_address",manage_address(request.getParameter("address"),request));
			request.getRequestDispatcher("Client_checkout_page.jsp").forward(request, response);return;
		}
		case "manage-rating"->{
			String[] product_details={
					request.getParameter("rating"),
					request.getParameter("product-type"),
					request.getParameter("product-id")
					
			};
			if(InputValidator.is_empty(product_details)) {response.sendRedirect("ClientFrontEndLoader?operation=load-orders");return;}
			else if(!InputValidator.is_valid_number(product_details[0]) || !InputValidator.is_valid_number(product_details[2]) || (Integer.parseInt(product_details[0])<1 || Integer.parseInt(product_details[0])>5)) {response.sendRedirect("ClientFrontEndLoader?operation=load-orders");return;}
			manage_product_rating((int)request.getSession().getAttribute("Client_id"),product_details);
			response.sendRedirect("ClientFrontEndLoader?operation=load-orders&msg=Thanks for your valuable rating.");return;
		}
		case "manage-review"->{
			String[] product_details={
					request.getParameter("review"),
					request.getParameter("product-type"),
					request.getParameter("product-id")
			};
			if(InputValidator.is_empty(product_details)) {response.sendRedirect("ClientFrontEndLoader?operation=load-orders");return;}
			else if(InputValidator.contains_sql(product_details[0]) || !InputValidator.is_valid_number(product_details[2]) || product_details.length>200) {response.sendRedirect("ClientFrontEndLoader?operation=load-orders");return;}
			manage_product_review((int)request.getSession().getAttribute("Client_id"),product_details);
			response.sendRedirect("ClientFrontEndLoader?operation=load-orders&msg=Thanks for your valuable review.");return;
		}
		case "validate-forget-pass-otp"->{
			String code=request.getParameter("forgot-pass-otp");
			if(!InputValidator.is_empty(code) && InputValidator.is_valid_number(code,6)) {
				if(this.securitycode.validate_security_code(new StringBuilder(code))) {
					request.getSession().setAttribute("change_forgotten_password","client");
					response.sendRedirect("Reset_password.jsp");return;
				}else {
					if(this.securitycode.code_expired()) {
						request.getSession().removeAttribute("user_type_before_otp_verify");
						request.getSession().removeAttribute("change_forgotten_password");
						request.getSession().removeAttribute("client_id_forget_pass");
						response.sendRedirect("Client_login_signup.jsp?msg=OTP expired.");
						return;
					}else {response.sendRedirect("Reset_password?msg=Incorrect OTP.");return;}
				}
			}else {response.sendRedirect("Reset_password?msg=Enter valid OTP.");return;}
		}
		case "update-fogotten-password"->{
			String []new_passwords= {
					request.getParameter("new-password"),
					request.getParameter("confirm-new-password")
			};
			if(!InputValidator.is_empty(new_passwords) && new_passwords[0].equals(new_passwords[1])) {
				if(!InputValidator.contains_sql(new_passwords[0]) && !InputValidator.contains_sql(new_passwords[0])) {
					update_forgotten_password(new_passwords,Integer.parseInt((String)request.getSession().getAttribute("client_id_forget_pass")));
					request.getSession().removeAttribute("user_type_before_otp_verify");
					request.getSession().removeAttribute("change_forgotten_password");
					request.getSession().removeAttribute("client_id_forget_pass");
					response.sendRedirect("Client_login_signup.jsp?msg=Password changed successfully.");return;
				}else {response.sendRedirect("Reset_password.jsp?msg=Password must contain only alphanumeric characters.");return;}
			}else {response.sendRedirect("Reset_password.jsp?msg=Passwords cannot be empty and both passwords must be same.");return;}
		}
		case "client-password-otp"->{
			String client_details=request.getParameter("client-email-phone");
			if(!InputValidator.is_empty(client_details)) {
				if(InputValidator.is_valid_phonenumber(client_details)) {
					String []client_details_from_db=get_client_details("phone",client_details);
					if(client_details_from_db!=null) {
						this.securitycode=new SecurityCodeHandler();
						send_password_change_mail(client_details_from_db,this.securitycode);
						request.getSession().setAttribute("user_type_before_otp_verify","client");
						request.getSession().setAttribute("client_id_forget_pass",client_details_from_db[1]);
						response.sendRedirect("Reset_password.jsp");return;
					}
					else {response.sendRedirect("Client_login_signup.jsp?msg=This phone number is not registered.");return;}
				}else if(InputValidator.is_valid_email(client_details)) {
					String []client_details_from_db=get_client_details("email",client_details);
					if(client_details_from_db!=null) {
						this.securitycode=new SecurityCodeHandler();
						send_password_change_mail(client_details_from_db,this.securitycode);
						request.getSession().setAttribute("user_type_before_otp_verify","client");
						request.getSession().setAttribute("client_id_forget_pass",client_details_from_db[1]);
						response.sendRedirect("Reset_password.jsp");return;
					}else {response.sendRedirect("Client_login_signup.jsp?msg=This email is not registered.");return;}
				}else {response.sendRedirect("Client_login_signup.jsp?msg=Invalid email or number.");return;}
			}else {response.sendRedirect("Client_login_signup.jsp?msg=Provide a valid email or phone number.");return;}
		}
		case "cancel-order"->{
			String order_id=request.getParameter("order-id");
			if(!InputValidator.is_empty(order_id) && InputValidator.is_valid_number(order_id)) cancel_order(Integer.parseInt(order_id));
			response.sendRedirect("ClientFrontEndLoader?operation=load-orders");return;
		}
		case "refund-return"->{
			String order_id=request.getParameter("order-id");
			if(!InputValidator.is_empty(order_id) && InputValidator.is_valid_number(order_id)) refund_return_order(Integer.parseInt(order_id));
			response.sendRedirect("ClientFrontEndLoader?operation=load-orders");return;
		}
		}
	}
	
	final private void refund_return_order(int order_id) {
		set_connection();
		String []query= {String.format("UPDATE order_items SET product_status=-1 WHERE order_id=%d",order_id),
				 		 String.format("UPDATE orders SET order_status=-1 WHERE order_id=%d",order_id)};
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT o.order_id, o.order_date, o.order_reference_code, o.payment_id, o.total_amount, c.client_fname, c.client_lname, c.client_email FROM orders o INNER JOIN client_personal_details c ON o.client_id=c.client_id WHERE o.order_id=%d",order_id))){
			if(result.next()) {
				connect.createStatement().execute(query[0]);
				connect.createStatement().execute(query[1]);
				connect.commit();
				String []order_details= {Integer.toString(order_id),String.valueOf(result.getDate("order_date")),result.getString("order_reference_code")};
				new MailHandler(new EmailDetails("client","client_mail_text_order_refund_by_client",result.getString("client_fname")+" "+result.getString("client_lname"),result.getString("client_email"),order_details)).send_mail();
			}
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}

	final private void cancel_order(int order_id) {
		set_connection();
		String []query= {String.format("UPDATE order_items SET product_status=2 WHERE order_id=%d",order_id),
						 String.format("UPDATE orders SET order_status=2 WHERE order_id=%d",order_id)};
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT o.order_id, o.order_date, o.order_reference_code, o.payment_id, o.total_amount, c.client_fname, c.client_lname, c.client_email FROM orders o INNER JOIN client_personal_details c ON o.client_id=c.client_id WHERE o.order_id=%d",order_id))){
			if(result.next()) {
				if(!result.getString("payment_id").equals("CASH ON DELIVERY"))new RazorPayService().initiate_refund(result.getDouble("total_amount"),result.getString("payment_id"));
				connect.createStatement().execute(query[0]);
				connect.createStatement().execute(query[1]);
				ResultSet products=connect.createStatement().executeQuery(String.format("SELECT product_id,product_type,product_quantity FROM order_items WHERE order_id=%d",result.getInt("order_id")));
				while(products.next()) connect.createStatement().execute(String.format("UPDATE %s_products SET product_quantity = product_quantity + %d WHERE product_id=%d",products.getString("product_type"),products.getInt("product_quantity"),products.getInt("product_id")));
				products.close();
				connect.commit();
				String []order_details= {Integer.toString(order_id),String.valueOf(result.getDate("order_date")),result.getString("order_reference_code")};
				new MailHandler(new EmailDetails("client","client_mail_text_order_canceled_by_client",result.getString("client_fname")+" "+result.getString("client_lname"),result.getString("client_email"),order_details)).send_mail();
			}
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}

	final private void update_forgotten_password(String[] new_passwords, int client_id) {
		set_connection();
		String query="UPDATE client_personal_details SET client_password=? WHERE client_id=?";
		try(PreparedStatement statement=connect.prepareStatement(query)){
			statement.setString(1,new_passwords[0]);
			statement.setInt(2,client_id);
			statement.execute();
			connect.commit();
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private void send_password_change_mail(String []client_personal_details,SecurityCodeHandler securitycode) {
		new MailHandler(new EmailDetails("client","client_mail_text_forget_password",client_personal_details[0],client_personal_details[2],securitycode.get_security_code())).send_mail();
	}

	final private String[] get_client_details(String type, String client_details) {
		set_connection();
		String query=null;
		if(type.equals("phone")) query=String.format("SELECT client_fname, client_lname, client_id, client_email FROM client_personal_details WHERE client_phone='%s'",client_details);
		else if(type.equals("email")) query=String.format("SELECT client_fname, client_lname, client_id, client_email FROM client_personal_details WHERE client_email='%s'",client_details);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				String []details_from_db= {result.getString("client_fname")+" "+result.getString("client_lname"),Integer.toString(result.getInt("client_id")), result.getString("client_email")};
				close_connection();
				return details_from_db;
			}
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private void delete_address(String address_id) {
		set_connection();
		try{
			connect.createStatement().execute(String.format("DELETE FROM client_address WHERE client_address_id=%d",Integer.parseInt(address_id)));
			connect.commit();
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private void delete_favourite_address(String fav_address_id) {
		set_connection();
		try{
			connect.createStatement().execute(String.format("DELETE FROM client_favourite_address WHERE client_fav_address_id=%d",Integer.parseInt(fav_address_id)));
			connect.commit();
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private void manage_product_review(int client_id,String[] product_details) {
		set_connection();
		try(PreparedStatement statement=connect.prepareStatement("UPDATE product_rating_review SET product_review=? WHERE product_id=? AND product_type=? AND client_id=?")){
			statement.setString(1,product_details[0]);
			statement.setInt(2,Integer.parseInt(product_details[2]));
			statement.setString(3,product_details[1]);
			statement.setInt(4, client_id);
			statement.execute();
			connect.commit();
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}

	final private void manage_product_rating(int client_id,String[] product_details) {
		set_connection();
		String query=String.format("SELECT product_client_rating FROM product_rating_review WHERE client_id=%d AND product_id=%d AND product_type='%s'",client_id,Integer.parseInt(product_details[2]),product_details[1]);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				if(result.getFloat("product_client_rating")>0) {
					System.out.println("Executed1");
					ResultSet product_table1=connect.createStatement().executeQuery(String.format("SELECT pro.product_id, pro.product_rating, pro.product_rating_count, rev.product_client_rating FROM "+product_details[1]+"_products pro INNER JOIN product_rating_review rev ON pro.product_id=rev.product_id AND pro.product_type=rev.product_type WHERE pro.product_id=%d",Integer.parseInt(product_details[2])));
					if(product_table1.next()) {
						PreparedStatement statement1=connect.prepareStatement("UPDATE "+product_details[1]+"_products SET product_rating=? WHERE product_id=?");
						float new_rating=((product_table1.getFloat("product_rating")*product_table1.getInt("product_rating_count"))-product_table1.getFloat("product_client_rating"))+Float.parseFloat(product_details[0]);
						statement1.setFloat(1,(float)new_rating/product_table1.getInt("product_rating_count"));
						statement1.setInt(2,product_table1.getInt("product_id"));
						PreparedStatement statement2=connect.prepareStatement("UPDATE product_rating_review SET product_client_rating=? WHERE product_id=? AND product_type=? AND client_id=?");
						statement2.setFloat(1,Float.parseFloat(product_details[0]));
						statement2.setInt(2,Integer.parseInt(product_details[2]));
						statement2.setString(3,product_details[1]);
						statement2.setInt(4,client_id);
						statement1.execute();
						statement2.execute();
						product_table1.close();statement1.close();statement2.close();
						connect.commit();
				}
				}else {
					ResultSet product_table2=connect.createStatement().executeQuery(String.format("SELECT product_id, product_type, product_rating, product_rating_count FROM "+product_details[1]+"_products WHERE product_id=%d",Integer.parseInt(product_details[2])));
					System.out.println("Executed2");
					if(product_table2.next()) {
						PreparedStatement statement1=connect.prepareStatement("UPDATE "+product_details[1]+"_products SET product_rating=?, product_rating_count=? WHERE product_id=?");
						statement1.setFloat(1,(float)(product_table2.getFloat("product_rating")+Float.parseFloat(product_details[0]))/(product_table2.getInt("product_rating_count")+1));
						statement1.setInt(2,(product_table2.getInt("product_rating_count")+1));
						statement1.setInt(3,Integer.parseInt(product_details[2]));
						PreparedStatement statement2=connect.prepareStatement("UPDATE product_rating_review SET product_client_rating=? WHERE product_id=? AND product_type=? AND client_id=?");
						statement2.setFloat(1,Float.parseFloat(product_details[0]));
						statement2.setInt(2,Integer.parseInt(product_details[2]));
						statement2.setString(3,product_details[1]);
						statement2.setInt(4,client_id);
						statement1.execute();
						statement2.execute();
						product_table2.close();statement1.close();statement2.close();
						connect.commit();
					}
				}
			}
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private HashMap<String,ClientPersonalDetails> manage_address(String address_id,HttpServletRequest request) {
		set_connection();
		String query=String.format("SELECT client_address_id, client_address_type, client_address, client_city, client_state, client_landmark, client_zip_code FROM client_address WHERE client_address_id=%d",Integer.parseInt(address_id));
		@SuppressWarnings("unchecked")
		HashMap<String,ClientPersonalDetails> selected_address=(HashMap<String,ClientPersonalDetails>)request.getSession().getAttribute("selected-address")!=null?(HashMap<String,ClientPersonalDetails>)request.getSession().getAttribute("selected-address"):new HashMap<String,ClientPersonalDetails>();
		if(!selected_address.containsKey(address_id)) {
			selected_address.clear();
			try(ResultSet result=connect.createStatement().executeQuery(query)){
				if(result.next()) {
					ClientPersonalDetails address=new ClientPersonalDetails();
					address.set_data("client_address_id",Integer.toString(result.getInt("client_address_id")));
					address.set_data("client_address_type", result.getString("client_address_type"));
					address.set_data("client_address", result.getString("client_address"));
					address.set_data("client_city", result.getString("client_city"));
					address.set_data("client_state", result.getString("client_state"));
					address.set_data("client_landmark", result.getString("client_landmark"));
					address.set_data("client_zip_code", Integer.toString(result.getInt("client_zip_code")));
					selected_address.put(Integer.toString(result.getInt("client_address_id")),address);
					close_connection();
					return selected_address;
				}
			}catch(Exception e) {e.printStackTrace();}
		}else {close_connection();return selected_address;}
		return null;
	}
	
	final private HashMap<String,ClientFavouriteAddress> manage_fav_address(String address_id,HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		HashMap<String,ClientFavouriteAddress> fav_addresses=(HashMap<String,ClientFavouriteAddress>)request.getSession().getAttribute("fav-address-selected")==null?new HashMap<String,ClientFavouriteAddress>():(HashMap<String,ClientFavouriteAddress>)request.getSession().getAttribute("fav-address-selected");
		@SuppressWarnings("unchecked")
		ArrayList<ProductToBuy> current_products=(ArrayList<ProductToBuy>)request.getSession().getAttribute("checkout-products");

		if(fav_addresses!=null && fav_addresses.containsKey(address_id)) {
			for(int i=0;i<current_products.size();i++) {
				current_products.get(i).set_data("product_quantity",Integer.toString(Integer.parseInt(current_products.get(i).get_data("product_quantity"))-1));
				current_products.get(i).calculate_total_price();
			}
			fav_addresses.remove(address_id);
			request.getSession().setAttribute("checkout-products",current_products);
		}
		else {
			for(int i=0;i<current_products.size();i++) {
				current_products.get(i).set_data("product_quantity",Integer.toString(1+Integer.parseInt(current_products.get(i).get_data("product_quantity"))));
				current_products.get(i).calculate_total_price();
			}
			fav_addresses.put(address_id,add_fav_address(Integer.parseInt(address_id)));
			request.getSession().setAttribute("checkout-products",current_products);
		}
		return fav_addresses;
	}
	
	final private ClientFavouriteAddress add_fav_address(int client_fav_address_id) {
		set_connection();
		ClientFavouriteAddress address=new ClientFavouriteAddress();
		String query=String.format("SELECT client_fav_address_id,client_fav_address_type,client_fav_address,client_fav_city,client_fav_state,client_fav_landmark,client_fav_zip_code FROM client_favourite_address WHERE client_fav_address_id=%d", client_fav_address_id);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				address.set_data("client_fav_address_id",Integer.toString(result.getInt("client_fav_address_id")));
				address.set_data("client_fav_address_type",result.getString("client_fav_address_type"));
				address.set_data("client_fav_address",result.getString("client_fav_address"));
				address.set_data("client_fav_city",result.getString("client_fav_city"));
				address.set_data("client_fav_state",result.getString("client_fav_state"));
				address.set_data("client_fav_landmark",result.getString("client_fav_landmark"));
				address.set_data("client_fav_zip_code",Integer.toString(result.getInt("client_fav_zip_code")));
			}
			close_connection();
			return address;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private ArrayList<ClientFavouriteAddress> get_favorite_address(String client_username) {
		set_connection();
		ArrayList<ClientFavouriteAddress> items=new ArrayList<ClientFavouriteAddress>();
		String query=String.format("SELECT client_fav_address_id,client_fav_address_type,client_fav_address,client_fav_city,client_fav_state,client_fav_landmark,client_fav_zip_code FROM client_favourite_address WHERE client_username='%s'",client_username);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				ClientFavouriteAddress address=new ClientFavouriteAddress();
				address.set_data("client_fav_address_id",Integer.toString(result.getInt("client_fav_address_id")));
				address.set_data("client_fav_address_type",result.getString("client_fav_address_type"));
				address.set_data("client_fav_address",result.getString("client_fav_address"));
				address.set_data("client_fav_city",result.getString("client_fav_city"));
				address.set_data("client_fav_state",result.getString("client_fav_state"));
				address.set_data("client_fav_landmark",result.getString("client_fav_landmark"));
				address.set_data("client_fav_zip_code",Integer.toString(result.getInt("client_fav_zip_code")));
				items.add(address);
			}
			close_connection();
			return items;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private boolean add_new_fav_address(String client_username, String[] client_fav_address) {
		set_connection();
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT COUNT(client_fav_address_id) AS address_count FROM client_favourite_address WHERE client_username='%s'",client_username))){
			if(result.next() && result.getInt("address_count")==3) {close_connection();return false;}
			else {
				PreparedStatement statement=connect.prepareStatement("INSERT INTO client_favourite_address (client_username, client_fav_address_type, client_fav_address, client_fav_city, client_fav_state, client_fav_landmark, client_fav_zip_code, receiver_name, receiver_phone) VALUES (?,?,?,?,?,?,?,?,?)");
				statement.setString(1, client_username);
				statement.setString(2, client_fav_address[0]);
				statement.setString(3, client_fav_address[1]);
				statement.setString(4, client_fav_address[2]);
				statement.setString(5, client_fav_address[3]);
				statement.setString(6, client_fav_address[4]);
				statement.setInt(7, Integer.parseInt(client_fav_address[5]));
				statement.setString(8, client_fav_address[6]);
				statement.setString(9, client_fav_address[7]);
				statement.execute();
				connect.commit();
				statement.close();
			}
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
		return true;
	}
	
	final private boolean add_new_address(String client_username,String[] client_address) {
		set_connection();
		String query="INSERT INTO client_address(client_username, client_address_type, client_address, client_city, client_state, client_landmark, client_zip_code) VALUES(?,?,?,?,?,?,?)";
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT COUNT(client_address_id) AS address_count FROM client_address WHERE client_username='%s'",client_username))){
			if(result.next() && result.getInt("address_count")==3){close_connection();return false;}
			else {
				PreparedStatement statement=connect.prepareStatement(query);
				statement.setString(1,client_username);
				statement.setString(2,client_address[0]);
				statement.setString(3,client_address[1]);
				statement.setString(4,client_address[2]);
				statement.setString(5,client_address[3]);
				statement.setString(6,client_address[4]);
				statement.setInt(7,Integer.parseInt(client_address[5]));
				statement.execute();
				connect.commit();
			}
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
		return true;
	}
	
	final private ArrayList<ClientPersonalDetails> load_client_address(String client_username){
		set_connection();
		ArrayList<ClientPersonalDetails> list=new ArrayList<ClientPersonalDetails>();
		String query=String.format("SELECT client_address_id, client_address_type, client_address, client_city, client_state, client_landmark, client_zip_code FROM client_address WHERE client_username='%s'", client_username);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				ClientPersonalDetails address=new ClientPersonalDetails();
				address.set_data("client_address_id",Integer.toString(result.getInt("client_address_id")));
				address.set_data("client_address_type", result.getString("client_address_type"));
				address.set_data("client_address", result.getString("client_address"));
				address.set_data("client_city", result.getString("client_city"));
				address.set_data("client_state", result.getString("client_state"));
				address.set_data("client_landmark", result.getString("client_landmark"));
				address.set_data("client_zip_code", Integer.toString(result.getInt("client_zip_code")));
				list.add(address);
			}
			close_connection();
			return list;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private ArrayList<ProductToBuy> remove_random_food_product(ArrayList<ProductToBuy> products) {
		products.remove(products.size()-1);
		return products;
	}
	
	final private ArrayList<ProductToBuy> add_random_food_product(ArrayList<ProductToBuy> products){
		set_connection();
		try(ResultSet result=connect.createStatement().executeQuery("SELECT product_id, product_name, product_selling_price, product_img1, product_quantity FROM food_products WHERE product_selling_price <= 50 ORDER BY RAND() LIMIT 1")){
			if(result.next()) {
				ProductToBuy item=new ProductToBuy();
				item.set_data("product_id",Integer.toString(result.getInt("product_id")));
				item.set_data("product_name",result.getString("product_name"));
				item.set_data("product_type","food");
				item.set_data("product_img",result.getString("product_img1"));
				item.set_data("product_quantity","1");
				item.set_data("product_price",Double.toString(result.getDouble("product_selling_price")));
				item.set_data("total_price",String.valueOf(result.getDouble("product_selling_price")*1));
				products.add(item);
				close_connection();
				return products;
			}
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private ArrayList<ProductToBuy> load_product_for_checkout(String []product_details){
		set_connection();
		ArrayList<ProductToBuy> items=new ArrayList<ProductToBuy>();
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT product_name,product_selling_price,product_img1,product_quantity FROM "+product_details[1]+"_products WHERE product_id=%d",Integer.parseInt(product_details[0])))){
		switch(product_details[1]) {
		case "clothing"->{
			if(result.next()) {
				ProductToBuy product=new ProductToBuy();
				product.set_data("product_id",product_details[0]);
				product.set_data("product_name",result.getString("product_name"));
				product.set_data("product_type",product_details[1]);
				product.set_data("product_img",result.getString("product_img1"));
				product.set_data("product_size",product_details[2]);
				product.set_data("product_quantity","1");
				product.set_data("product_price",Double.toString(result.getDouble("product_selling_price")));
				product.calculate_total_price();
				items.add(product);
				close_connection();
				return items;
			}
		}
		default->{
			if(result.next()) {
				ProductToBuy product=new ProductToBuy();
				product.set_data("product_id",product_details[0]);
				product.set_data("product_name",result.getString("product_name"));
				product.set_data("product_type",product_details[1]);
				product.set_data("product_img",result.getString("product_img1"));
				product.set_data("product_quantity","1");
				product.set_data("product_price",Double.toString(result.getDouble("product_selling_price")));
				product.calculate_total_price();
				items.add(product);
				close_connection();
				return items;
			}
		}
		}
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private ArrayList<ProductToBuy> load_cart_for_checkout(int client_id,HttpServletRequest request) {
		set_connection();
		if(request.getSession().getAttribute("checkout-cart-products")!=null) {
			@SuppressWarnings("unchecked")
			ArrayList<ProductToBuy> items=(ArrayList<ProductToBuy>)request.getSession().getAttribute("checkout-cart-products");
			items.clear();items=null;
			request.getSession().removeAttribute("checkout-cart-products");
		}
		ArrayList<ProductToBuy> items=new ArrayList<ProductToBuy>();
		String query=String.format("SELECT product_id,product_type,product_name,product_price,product_size,product_img1,product_quantity FROM cart WHERE client_id=%d ORDER BY cart_id DESC",client_id);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				ProductToBuy list=new ProductToBuy();
				list.set_data("product_id",Integer.toString(result.getInt("product_id")));
				list.set_data("product_type",result.getString("product_type"));
				list.set_data("product_name",result.getString("product_name"));
				list.set_data("product_price",Double.toString(result.getDouble("product_price")));
				list.set_data("product_size",result.getString("product_size"));
				list.set_data("product_img",result.getString("product_img1"));
				list.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
				list.set_data("total_price",String.valueOf(result.getDouble("product_price")*result.getInt("product_quantity")));
				items.add(list);
			}
			close_connection();
			return items;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private void delete_client_account(int client_id) {
		set_connection();
		String delete_query=String.format("DELETE FROM client_personal_details WHERE client_id=%d",client_id);
		String get_blog_images=String.format("SELECT blog_img FROM blog_posts WHERE client_id=%d",client_id);
		try(ResultSet result=connect.createStatement().executeQuery(get_blog_images)){
			while(result.next()) {
				new File("E:/Dynamic Web Project/src/main/webapp/Blog post images/"+result.getString("blog_img")).delete();
			}
			connect.createStatement().execute(delete_query);
			connect.commit();
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}

	final private void remove_existing_cookie(Cookie []cookies, HttpServletResponse response) {
		for(Cookie cookie:cookies) {
			if(cookie.getName().equals("Client_username_cookie")) {
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
	}
	
	final private void increase_quantity_in_cart(int cart_id,int quantity,int product_id,String product_type) {
		set_connection();
		String query=String.format("SELECT product_quantity FROM "+product_type+"_products WHERE product_stock_status=1 AND product_status=1 AND product_id=%d",product_id);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
			if((quantity+1)>result.getInt("product_quantity"))return;
			PreparedStatement statement=connect.prepareStatement("UPDATE cart SET product_quantity=? WHERE cart_id=?");
			statement.setInt(1,++quantity);
			statement.setInt(2,cart_id);
			statement.execute();
			connect.commit();
			statement.close();
			}
			else {
				connect.createStatement().execute(String.format("DELETE FROM cart WHERE cart_id=%d",cart_id));
				connect.commit();
			}
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private void decrease_quantity_in_cart(int cart_id,int quantity) {
		set_connection();
		if((quantity-1)<1) return;
		else {
			try(PreparedStatement statement=connect.prepareStatement("UPDATE cart SET product_quantity=? WHERE cart_id=?")){
				statement.setInt(1,--quantity);
				statement.setInt(2,cart_id);
				statement.execute();
				connect.commit();
			}catch(Exception e) {e.printStackTrace();
				try {connect.rollback();} 
				catch (SQLException e1) {e1.printStackTrace();}
			}
		}
		close_connection();
	}
	
	final private void add_to_cart(String []product_details,int client_id,String client_username) {
		set_connection();
		String query1=String.format("SELECT product_id, product_type FROM cart WHERE client_id=%d OR client_username='%s'",client_id,client_username);
		try(ResultSet result=connect.createStatement().executeQuery(query1)){
			while(result.next()) {
				if(result.getInt("product_id")==Integer.parseInt(product_details[0]) && result.getString("product_type").equals(product_details[1])) {
					close_connection();return;
				}
			}
		}catch(Exception e) {e.printStackTrace();}
		if(product_details[1].equals("clothing")) {
			String query="INSERT INTO cart (client_id,client_username,product_id,product_type,product_name,product_price,product_img1,product_size) VALUES(?,?,?,?,?,?,?,?)";
			try(PreparedStatement statement=connect.prepareStatement(query)){
				ResultSet result=connect.createStatement().executeQuery(String.format("SELECT product_name, product_selling_price, product_img1 FROM "+product_details[1]+"_products WHERE product_id=%d", Integer.parseInt(product_details[0])));
				if(result.next()) {
					statement.setInt(1, client_id);
					statement.setString(2, client_username);
					statement.setInt(3, Integer.parseInt(product_details[0]));
					statement.setString(4, product_details[1]);
					statement.setString(5, result.getString("product_name"));
					statement.setDouble(6, result.getDouble("product_selling_price"));
					statement.setString(7, result.getString("product_img1"));
					statement.setString(8, product_details[2]);
					statement.execute();
					connect.commit();
				}
				result.close();
			}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
			}
		}
		else {
			String query="INSERT INTO cart (client_id,client_username,product_id,product_type,product_name,product_price,product_img1) VALUES(?,?,?,?,?,?,?)";
			try (PreparedStatement statement=connect.prepareStatement(query)){
				ResultSet result=connect.createStatement().executeQuery(String.format("SELECT product_name, product_selling_price, product_img1 FROM "+product_details[1]+"_products WHERE product_id=%d", Integer.parseInt(product_details[0])));
				if(result.next()) {
					statement.setInt(1, client_id);
					statement.setString(2, client_username);
					statement.setInt(3, Integer.parseInt(product_details[0]));
					statement.setString(4, product_details[1]);
					statement.setString(5, result.getString("product_name"));
					statement.setDouble(6, result.getDouble("product_selling_price"));
					statement.setString(7, result.getString("product_img1"));
					statement.execute();
					connect.commit();
				}
				result.close();
			}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
			}
		}
		close_connection();
	}
	
	final private void add_to_wishlist(String []product_details,int client_id,String client_username) {
		set_connection();
		String query1=String.format("SELECT product_id,product_type FROM wishlist WHERE client_id=%d OR client_username='%s'",client_id,client_username);
		try(ResultSet result=connect.createStatement().executeQuery(query1)){
			while(result.next()) {
				if(result.getInt("product_id")==Integer.parseInt(product_details[0]) && result.getString("product_type").equals(product_details[1])) {
					close_connection();return;
				}
			}
		}catch(Exception e) {e.printStackTrace();}
		String query="INSERT INTO wishlist (client_id,client_username,product_id,product_type,product_name,product_price,product_img1) VALUES(?,?,?,?,?,?,?)";
		try (PreparedStatement statement=connect.prepareStatement(query)){
			ResultSet result=connect.createStatement().executeQuery(String.format("SELECT product_name, product_selling_price, product_img1 FROM "+product_details[1]+"_products WHERE product_id=%d",Integer.parseInt(product_details[0])));
			if(result.next()) {
				statement.setInt(1, client_id);
				statement.setString(2, client_username);
				statement.setInt(3, Integer.parseInt(product_details[0]));
				statement.setString(4, product_details[1]);
				statement.setString(5, result.getString("product_name"));
				statement.setDouble(6, result.getDouble("product_selling_price"));
				statement.setString(7, result.getString("product_img1"));
				statement.execute();
				connect.commit();
			}
			result.close();
		}catch(Exception e) {e.printStackTrace();
		try {connect.rollback();} 
		catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private void set_client_id(String username,HttpServletRequest request) {
		set_connection();
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT client_id FROM client_personal_details WHERE client_username='%s'",username))){
			if(result.next()) {
				request.getSession().setAttribute("Client_id",result.getInt("client_id"));
				close_connection();
			}
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		request.getSession().setAttribute("Client_id",-1);
	}
	
	final private void remove_from_cart(int wishlist_id) {
		set_connection();
		String query=String.format("DELETE FROM cart WHERE cart_id=%d",wishlist_id);
		try{
			connect.createStatement().execute(query);
			connect.commit();
		}catch(Exception e) {e.printStackTrace();
		try {connect.rollback();} 
		catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private void remove_from_wishlist(int wishlist_id) {
		set_connection();
		String query=String.format("DELETE FROM wishlist WHERE wishlist_id=%d",wishlist_id);
		try{
			connect.createStatement().execute(query);
			connect.commit();
		}catch(Exception e) {e.printStackTrace();
		try {connect.rollback();} 
		catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private static void set_session(HttpServletRequest request) {
		Cookie []cookies=request.getCookies();
		for(Cookie cookie:cookies) {
			if(cookie.getName().equals("Client_username_cookie")) {
				request.getSession().setAttribute("Client_username",cookie.getValue());
				return;
			}
		}
		request.getSession().setAttribute("Client_username",null);
		return;
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
