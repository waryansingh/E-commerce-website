package com.ecom;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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
import com.entity.AdminPersonalDetails;
import com.entity.VendorPersonalDetails;
import com.entity.AllProducts;
import com.entity.BlogPosts;
import com.entity.ClientPersonalDetails;
import com.entity.EmailDetails;
import com.entity.Orders;

@WebServlet(
        name = "AdminOperationsHandler",
        urlPatterns = { "/AdminOperationsHandler"}
)
public class AdminOperationsHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connect=null;
	private SecurityCodeHandler securitycode=null;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String request_type=request.getParameter("request-type");
		switch(request_type) {
		case "registered_admins"->{
			request.getSession().setAttribute("datalist", get_details_of_admins(1));
			request.getRequestDispatcher("Admin_operations.jsp").forward(request, response);
		}
		case "admin_requests"->{
			request.getSession().setAttribute("datalist",get_details_of_admins(0));
			request.getRequestDispatcher("Admin_operations.jsp").forward(request, response);
		}
		case "registered_vendors"->{
			request.getSession().setAttribute("datalist",get_details_of_vendors(1));
			request.getRequestDispatcher("Vendor_operations.jsp").forward(request, response);
		}	
		case "vendor_requests"->{
			request.getSession().setAttribute("datalist",get_details_of_vendors(0));
			request.getRequestDispatcher("Vendor_operations.jsp").forward(request, response);
		}
		case "product_requests"->{
			request.getSession().setAttribute("productlist",get_all_products(0));
			request.getRequestDispatcher("Admin_products.jsp").forward(request, response);
		}
		case "registered_products"->{
			request.getSession().setAttribute("productlist",get_all_products(1));
			request.getRequestDispatcher("Admin_products.jsp").forward(request, response);
		}
		case "clothing_products","electronics_products","food_products"->{
			request.getSession().setAttribute("productlist",get_product(request_type));
			request.getRequestDispatcher("Admin_products.jsp").forward(request, response);
		}
		case "product-srearch-result"->{
			request.getSession().setAttribute("productlist",get_searched_products((String)request.getParameter("product-type")));
			request.getRequestDispatcher("Admin_products.jsp").forward(request, response);
		}
		case "blacklisted_admins"->{
			request.getSession().setAttribute("datalist", get_details_of_admins(3));
			request.getRequestDispatcher("Admin_operations.jsp").forward(request, response);
		}
		case "blacklisted_vendors"->{
			request.getSession().setAttribute("datalist",get_details_of_vendors(3));
			request.getRequestDispatcher("Vendor_operations.jsp").forward(request, response);
		}
		case "all_clients"->{
			request.getSession().setAttribute("datalist",get_details_of_clients());
			request.getRequestDispatcher("Admin_clients.jsp").forward(request, response);
		}
		case "products_blacklisted"->{
			request.getSession().setAttribute("productlist",get_all_products(3));
			request.getRequestDispatcher("Admin_products.jsp").forward(request, response);
		}
		case "all_blogs"->{
			request.getSession().setAttribute("all-blog-data",load_all_blogs());
			request.getRequestDispatcher("Admin_blog_management.jsp").forward(request, response);return;
		}
		case "filter-blogs"->{
			request.getSession().setAttribute("all-blog-data",filter_blog_posts(request.getParameter("filter-option")));
			request.getRequestDispatcher("Admin_blog_management.jsp").forward(request, response);return;
		}
		case "search-blogs"->{
			request.getSession().setAttribute("all-blog-data",request.getParameter("search-type").equals("product-link-search")?get_blog_posts_link(request.getParameter("search-text")):get_searched_blog_posts(request.getParameter("search-text")));
			request.getRequestDispatcher("Admin_blog_management.jsp").forward(request, response);return;
		}
		case "log-out"->{
			request.getSession().removeAttribute("Admin-username");
			request.getRequestDispatcher("Admin_login_signup.jsp").forward(request, response);return;
		}
		case "pre-delivery-orders"->{
			request.getSession().setAttribute("orders-data",load_orders(0));
			request.getRequestDispatcher("Admin_orders.jsp").forward(request, response);return;
		}
		case "delivered-orders"->{
			request.getSession().setAttribute("orders-data",load_orders(1));
			request.getRequestDispatcher("Admin_orders.jsp").forward(request, response);return;
		}
		case "refund-request-orders"->{
			request.getSession().setAttribute("orders-data",load_orders(-1));
			request.getRequestDispatcher("Admin_orders.jsp").forward(request, response);return;
		}
		case "refunded-orders"->{
			request.getSession().setAttribute("orders-data",load_orders(3));
			request.getRequestDispatcher("Admin_orders.jsp").forward(request, response);return;
		}
		case "canceled-orders"->{
			request.getSession().setAttribute("orders-data",load_orders(2));
			request.getRequestDispatcher("Admin_orders.jsp").forward(request, response);return;
		}
		case "search-order"->{
			request.getSession().setAttribute("orders-data",searched_orders(request.getParameter("order-id-code")));
			request.getRequestDispatcher("Admin_orders.jsp").forward(request, response);return;
		}
		case "search-order-items"->{
			request.getSession().setAttribute("orders-items",search_order_items(request.getParameter("order-id")));
			request.getRequestDispatcher("Admin_order_items.jsp").forward(request, response);return;
		}
		case "search-admins"->{
			request.getSession().setAttribute("datalist",get_searched_admins(request.getParameter("search-val")));
			request.getRequestDispatcher("Admin_operations.jsp").forward(request, response);return;
		}
		case "search-vendors"->{
			request.getSession().setAttribute("datalist",get_searched_vendors(request.getParameter("search-val")));
			request.getRequestDispatcher("Vendor_operations.jsp").forward(request, response);
		}
		case "search-clients"->{
			request.getSession().setAttribute("datalist",get_searched_clients(request.getParameter("search-val")));
			request.getRequestDispatcher("Admin_clients.jsp").forward(request, response);
		}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		switch(request.getParameter("form-request-type")!=null?request.getParameter("form-request-type"):"") {
		case "alter-admin-status"->{
			String []admin_info= {
				request.getParameter("admin_id"),
				request.getParameter("status_option")
			};
			if(!InputValidator.is_valid_number(admin_info[0]) || !InputValidator.is_valid_number(admin_info[1],1)) {close_connection();request.getSession().setAttribute("msg","Values cannot be altered.");response.sendRedirect("AdminOperationsHandler?request-type="+request.getParameter("admin-return-page"));return;}
			update_admin_status(Integer.parseInt(admin_info[0]), Integer.parseInt(admin_info[1]), request);
			response.sendRedirect("AdminOperationsHandler?request-type="+request.getParameter("admin-return-page"));return;
		}
		case "alter-vendor-status"->{
			String []vendor_info= {
				request.getParameter("vendor_id"),
				request.getParameter("status_option")
				
			};
			if(!InputValidator.is_valid_number(vendor_info[0]) || !InputValidator.is_valid_number(vendor_info[1],1)) {request.getSession().setAttribute("msg","Values cannot be altered.");response.sendRedirect("AdminOperationsHandler?request-type="+request.getParameter("vendor-return-page"));return;}
			update_vendor_status(Integer.parseInt(vendor_info[0]), Integer.parseInt(vendor_info[1]),request);
			response.sendRedirect("AdminOperationsHandler?request-type="+request.getParameter("vendor-return-page"));return;
		}
		case "alter-product-status"->{
			if(request.getParameter("product-type").equals("clothing")) {
				String []product_info= {
					request.getParameter("clothing-product-id"),
					request.getParameter("clothing_status_option")
				};
				if(!InputValidator.is_valid_number(product_info[0]) || !InputValidator.is_valid_number(product_info[1],1)) {close_connection();request.getSession().setAttribute("msg","Values cannot be altered.");response.sendRedirect("AdminOperationsHandler?request-type="+request.getParameter("product-return-page"));return;}
				update_product_status(Integer.parseInt(product_info[0]), Integer.parseInt(product_info[1]), "clothing",request);
				
			}else if(request.getParameter("product-type").equals("electronics")) {
				String []product_info= {
					request.getParameter("electronics-product-id"),
					request.getParameter("electronics_status_option")
				};
				if(!InputValidator.is_valid_number(product_info[0]) || !InputValidator.is_valid_number(product_info[1],1)) {close_connection();request.getSession().setAttribute("msg","Values cannot be altered.");response.sendRedirect("AdminOperationsHandler?request-type="+request.getParameter("product-return-page"));return;}
				update_product_status(Integer.parseInt(product_info[0]), Integer.parseInt(product_info[1]), "electronics",request);
				
			}else if(request.getParameter("product-type").equals("food")){
				String []product_info= {
					request.getParameter("food-product-id"),
					request.getParameter("food_status_option")
				};
				if(!InputValidator.is_valid_number(product_info[0]) || !InputValidator.is_valid_number(product_info[1],1)) {close_connection();request.getSession().setAttribute("msg","Values cannot be altered.");response.sendRedirect("AdminOperationsHandler?request-type="+request.getParameter("product-return-page"));return;}
				update_product_status(Integer.parseInt(product_info[0]), Integer.parseInt(product_info[1]),"food",request);
				
			}
			response.sendRedirect("AdminOperationsHandler?request-type="+request.getParameter("product-return-page"));return;
		}
		case "delete_client"->{
			String client_id=request.getParameter("client_id");
			if(!InputValidator.is_valid_number(client_id)) {response.sendRedirect("AdminOperationsHandler?request-type=all_clients");return;}
			delete_client(Integer.parseInt(client_id));
			response.sendRedirect("AdminOperationsHandler?request-type=all_clients");return;
		}
		case "delete-blog"->{
			String blod_id=request.getParameter("blog-id");
			if(!InputValidator.is_valid_number(blod_id)) {response.sendRedirect("AdminOperationsHandler?request-type=all_blogs");return;}
			delete_blog_post(Long.parseLong(blod_id));
			response.sendRedirect("AdminOperationsHandler?request-type=all_blogs");return;
		}
		case "validate-forget-pass-otp"->{
			String code=request.getParameter("forgot-pass-otp");
			if(!InputValidator.is_empty(code) && InputValidator.is_valid_number(code,6)) {
				if(this.securitycode.validate_security_code(new StringBuilder(code))) {
					request.getSession().setAttribute("change_forgotten_password","admin");
					response.sendRedirect("Reset_password.jsp");return;
				}else {
					if(this.securitycode.code_expired()) {
						request.getSession().removeAttribute("user_type_before_otp_verify");
						request.getSession().removeAttribute("change_forgotten_password");
						request.getSession().removeAttribute("admin_id_forget_pass");
						response.sendRedirect("Admin_login_signup.jsp?msg=OTP expired.");return;
					}else {response.sendRedirect("Reset_password.jsp?msg=Incorrect OTP.");return;}
				}
			}else {response.sendRedirect("Reset_password.jsp?msg=Enter valid OTP.");return;}
		}
		case "update-fogotten-password"->{
			String []new_passwords={
					request.getParameter("new-password"),
					request.getParameter("confirm-new-password")
			};
			if(!InputValidator.is_empty(new_passwords) && new_passwords[0].equals(new_passwords[1])) {
				if(!InputValidator.contains_sql(new_passwords[0]) && !InputValidator.contains_sql(new_passwords[0])) {
					update_forgotten_password(new_passwords,Integer.parseInt((String)request.getSession().getAttribute("admin_id_forget_pass")));
					request.getSession().removeAttribute("user_type_before_otp_verify");
					request.getSession().removeAttribute("change_forgotten_password");
					request.getSession().removeAttribute("admin_id_forget_pass");
					response.sendRedirect("Admin_login_signup.jsp?msg=Password changed successfully.");return;
				}else {response.sendRedirect("Reset_password.jsp?msg=Password must contain only alphanumeric characters.");return;}
			}else {response.sendRedirect("Reset_password.jsp?msg=Passwords cannot be empty and both passwords must be same.");return;}
		}
		case "admin-password-otp"->{
			String admin_details=request.getParameter("admin-email-phone");
			if(!InputValidator.is_empty(admin_details)) {
				if(InputValidator.is_valid_phonenumber(admin_details)) {
					String []admin_details_from_db=get_admin_details("phone",admin_details);
					if(admin_details_from_db!=null) {
						this.securitycode=new SecurityCodeHandler();
						send_password_change_mail(admin_details_from_db,this.securitycode);
						request.getSession().setAttribute("user_type_before_otp_verify","admin");
						request.getSession().setAttribute("admin_id_forget_pass",admin_details_from_db[1]);
						response.sendRedirect("Reset_password.jsp");return;
					}else {response.sendRedirect("Admin_login_signup.jsp?msg=This phone number is not registered.");return;}
				}else if(InputValidator.is_valid_email(admin_details)) {
					String []admin_details_from_db=get_admin_details("email",admin_details);
					if(admin_details_from_db!=null) {
						this.securitycode=new SecurityCodeHandler();
						send_password_change_mail(admin_details_from_db,this.securitycode);
						request.getSession().setAttribute("user_type_before_otp_verify","admin");
						request.getSession().setAttribute("admin_id_forget_pass",admin_details_from_db[1]);
						response.sendRedirect("Reset_password.jsp");return;
					}else {response.sendRedirect("Admin_login_signup.jsp?msg=This email is not registered.");return;}
				}else {response.sendRedirect("Admin_login_signup.jsp?msg=Invalid email or number.");return;}
			}else {response.sendRedirect("Admin_login_signup.jsp?msg=Provide a valid email or phone number.");return;}
		}
		case "update-orders"->{
			String []details= {request.getParameter("order-id"),request.getParameter("order_status_option")};
			if(!InputValidator.is_empty(details) && InputValidator.is_valid_number(details[0]) && InputValidator.is_valid_number(details[1],1))update_order_status(Integer.parseInt(details[0]),Integer.parseInt(details[1]));
			request.getRequestDispatcher("Admin_orders.jsp").forward(request, response);return;
		}
		case "delete-account"->{
			delete_admin_account((String)request.getSession().getAttribute("Admin-username"));
			request.getSession().removeAttribute("Admin-username");
			response.sendRedirect("Admin_login_signup.jsp");return;
		}
		}
	}
	
	final private ArrayList<ClientPersonalDetails> get_searched_clients(String search_val) {
		set_connection();
		ArrayList<ClientPersonalDetails> data_list=new ArrayList<ClientPersonalDetails>();
		String query=String.format("SELECT cpd.client_id, cpd.client_fname, cpd.client_lname, cpd.client_email, cpd.client_phone, cpd.client_username, cpd.client_profile_picture, ca.client_address_type, ca.client_address, ca.client_city, ca.client_state, ca.client_landmark, ca.client_zip_code FROM client_personal_details cpd INNER JOIN client_address ca ON cpd.client_username=ca.client_username WHERE cpd.client_username = '%s' OR cpd.client_email = '%s' OR cpd.client_phone = '%s'", search_val,search_val,search_val);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				ClientPersonalDetails data=new ClientPersonalDetails();
				data.set_data("client_id",Integer.toString(result.getInt("client_id")));
				data.set_data("client_fname",result.getString("client_fname"));
				data.set_data("client_lname",result.getString("client_lname"));
				data.set_data("client_email",result.getString("client_email"));
				data.set_data("client_phone",result.getString("client_phone"));
				data.set_data("client_profile_picture",result.getString("client_profile_picture"));
				data.set_data("client_username",result.getString("client_username"));
				data.set_data("client_address_type",result.getString("client_address_type"));
				data.set_data("client_address",result.getString("client_address"));
				data.set_data("client_city",result.getString("client_city"));
				data.set_data("client_state",result.getString("client_state"));
				data.set_data("client_landmark",result.getString("client_landmark"));
				data.set_data("client_zip_code",Integer.toString(result.getInt("client_zip_code")));
				data_list.add(data);
			}
			close_connection();
			return data_list;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private ArrayList<VendorPersonalDetails> get_searched_vendors(String search_val) {
		set_connection();
		ArrayList<VendorPersonalDetails> data_list=new ArrayList<VendorPersonalDetails>();
		String query=String.format("SELECT vpd.vendor_id, vpd.vendor_fname, vpd.vendor_lname, vpd.vendor_email, vpd.vendor_phone, vpd.vendor_status, " +
			    "vpd.vendor_username, vpd.vendor_gst_number, vpd.vendor_trade_license, vpd.vendor_govId_picture, vpd.vendor_status_updated, " +
			    "vpa.vendor_address, vpa.vendor_city, vpa.vendor_state, vpa.vendor_zip_code, " +
			    "vsa.vendor_shop_name_number, vsa.vendor_shop_address, vsa.vendor_shop_city, vsa.vendor_shop_state, vsa.vendor_shop_zip_code " +
			    "FROM vendor_personal_details vpd " +
			    "INNER JOIN vendor_personal_address vpa ON vpd.vendor_username = vpa.vendor_username " +
			    "INNER JOIN vendor_shop_address vsa ON vpd.vendor_username = vsa.vendor_username " +
			    "WHERE vpd.vendor_email = '%s' OR vpd.vendor_phone = '%s' OR vpd.vendor_username = '%s'",search_val,search_val,search_val);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				VendorPersonalDetails data=new VendorPersonalDetails();
				data.set_data("vendor_id", Integer.toString(result.getInt("vendor_id")));
				data.set_data("vendor_full_name", result.getString("vendor_fname")+" "+result.getString("vendor_lname"));
				data.set_data("vendor_email", result.getString("vendor_email"));
				data.set_data("vendor_phone", result.getString("vendor_phone"));
				data.set_data("vendor_username", result.getString("vendor_username"));
				data.set_data("vendor_gst_number", result.getString("vendor_gst_number"));
				data.set_data("vendor_trade_license", result.getString("vendor_trade_license"));
				data.set_data("vendor_govId_picture", result.getString("vendor_govId_picture"));
				data.set_data("vendor_status_updated", result.getString("vendor_status_updated"));
				data.set_data("vendor_status", Integer.toString(result.getInt("vendor_status")));
				data.set_data("vendor_address", result.getString("vendor_address"));
				data.set_data("vendor_city", result.getString("vendor_city"));
				data.set_data("vendor_state", result.getString("vendor_state"));
				data.set_data("vendor_zip_code", result.getString("vendor_zip_code"));
				data.set_data("vendor_shop_name_number",result.getString("vendor_shop_name_number"));
				data.set_data("vendor_shop_address", result.getString("vendor_shop_address"));
				data.set_data("vendor_shop_city", result.getString("vendor_shop_city"));
				data.set_data("vendor_shop_state", result.getString("vendor_shop_state"));
				data.set_data("vendor_shop_zip_code", result.getString("vendor_shop_zip_code"));
				data_list.add(data);
			}
			close_connection();
			return data_list;
		}
		catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private ArrayList<AdminPersonalDetails> get_searched_admins(String search_val) {
		set_connection();
		ArrayList<AdminPersonalDetails> data_list=new ArrayList<AdminPersonalDetails>();
		String query=String.format("SELECT apd.admin_id, apd.admin_fname, apd.admin_lname, apd.admin_email, apd.admin_phone, apd.admin_username, apd.admin_profile_picture, apd.admin_govId_picture, apd.admin_status_updated, apd.admin_status, aa.admin_address, aa.admin_city, aa.admin_state, aa.admin_zip_code FROM admin_personal_details apd INNER JOIN admin_address aa ON apd.admin_username = aa.admin_username WHERE apd.admin_email = '%s' OR apd.admin_phone = '%s' OR apd.admin_username = '%s'", search_val,search_val,search_val);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				AdminPersonalDetails data=new AdminPersonalDetails();
				data.set_data("admin_id", Integer.toString(result.getInt("admin_id")));
				data.set_data("admin_full_name", result.getString("admin_fname")+" "+result.getString("admin_lname"));
				data.set_data("admin_email", result.getString("admin_email"));
				data.set_data("admin_phone", result.getString("admin_phone"));
				data.set_data("admin_username", result.getString("admin_username"));
				data.set_data("admin_profile_picture", result.getString("admin_profile_picture"));
				data.set_data("admin_govId_picture", result.getString("admin_govId_picture"));
				data.set_data("admin_status_updated", result.getString("admin_status_updated"));
				data.set_data("admin_address", result.getString("admin_address"));
				data.set_data("admin_city", result.getString("admin_city"));
				data.set_data("admin_state", result.getString("admin_state"));
				data.set_data("admin_zip_code", result.getString("admin_zip_code"));
				data.set_data("admin_status",Integer.toString(result.getInt("admin_status")));
				data_list.add(data);
			}
			close_connection();
			return data_list;
		}
		catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private void delete_admin_account(String admin_username) {
		set_connection();
		String query=String.format("SELECT admin_profile_picture, admin_govId_picture FROM admin_personal_details WHERE admin_username='%s'",admin_username);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				new File("E:/Dynamic Web Project/src/main/webapp/Admin Profile Picture/"+result.getString("admin_profile_picture")).delete();
				new File("E:/Dynamic Web Project/src/main/webapp/Admin GovID/"+result.getString("admin_govId_picture")).delete();
				connect.createStatement().execute(String.format("DELETE FROM admin_personal_details WHERE admin_username='%s'",admin_username));
				connect.createStatement().execute(String.format("DELETE FROM admin_address WHERE admin_username='%s'",admin_username));
				connect.commit();
			}
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}

	final private void update_order_status(int order_id,int status) {
		set_connection();
		String []query={String.format("UPDATE order_items SET product_status=%d WHERE order_id=%d",status,order_id),
						String.format("UPDATE orders SET order_status=%d WHERE order_id=%d",status,order_id)};
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT o.order_id, o.order_date, o.order_reference_code, o.payment_id, o.total_amount, c.client_fname, c.client_lname, c.client_email FROM orders o INNER JOIN client_personal_details c ON o.client_id=c.client_id WHERE o.order_id=%d",order_id))){
			if(result.next()) {
				String []order_details= {Integer.toString(order_id),String.valueOf(result.getDate("order_date")),result.getString("order_reference_code")};
				connect.createStatement().execute(query[0]);
				connect.createStatement().execute(query[1]);
				if(status==2 || status==3) {
					ResultSet products=connect.createStatement().executeQuery(String.format("SELECT product_id,product_type,product_quantity FROM order_items WHERE order_id=%d",result.getInt("order_id")));
					while(products.next()) connect.createStatement().execute(String.format("UPDATE %s_products SET product_quantity = product_quantity + %d WHERE product_id=%d",products.getString("product_type"),products.getInt("product_quantity"),products.getInt("product_id")));
					products.close();
					if(!result.getString("payment_id").equals("CASH ON DELIVERY"))new RazorPayService().initiate_refund(result.getDouble("total_amount"),result.getString("payment_id"));
					connect.commit();
					if(status==3)new MailHandler(new EmailDetails("client","client_mail_text_order_return_refund_request_approved_by_admin",result.getString("client_fname")+" "+result.getString("client_lname"),result.getString("client_email"),order_details)).send_mail();
					else new MailHandler(new EmailDetails("client","client_mail_text_order_canceled_by_admin",result.getString("client_fname")+" "+result.getString("client_lname"),result.getString("client_email"),order_details)).send_mail();
					return;
				}
				connect.commit();
				new MailHandler(new EmailDetails("client","client_mail_text_order_return_refund_request_canceled_by_admin",result.getString("client_fname")+" "+result.getString("client_lname"),result.getString("client_email"),order_details)).send_mail();
			}
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private ArrayList<Orders> search_order_items(String search_val) {
		set_connection();
		ArrayList<Orders> list=new ArrayList<Orders>();
		String query=String.format("SELECT order_item_id, order_id, product_name, product_type, product_status, product_size, product_id, product_img1, product_quantity, product_selling_price, total_item_price FROM order_items WHERE order_id=%d",Integer.parseInt(search_val));
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				Orders product=new Orders();
				product.set_data("order_item_id",Integer.toString(result.getInt("order_item_id")));
				product.set_data("order_id",Integer.toString(result.getInt("order_id")));
				product.set_data("product_name",result.getString("product_name"));
				product.set_data("product_type",result.getString("product_type"));
				product.set_data("product_size",result.getString("product_size"));
				product.set_data("product_id",Integer.toString(result.getInt("product_id")));
				product.set_data("product_img1",result.getString("product_img1"));
				product.set_data("product_status",result.getString("product_status"));
				product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
				product.set_data("product_selling_price",Double.toString(result.getDouble("product_selling_price")));
				product.set_data("total_item_price",Double.toString(result.getDouble("total_item_price")));
				list.add(product);
			}
			close_connection();
			return list;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}

	final private ArrayList<Orders> searched_orders(String search_val) {
		set_connection();
		ArrayList<Orders> list=new ArrayList<Orders>();
		String query=String.format("SELECT order_id, payment_id, payment_order_id, client_id, order_date, total_amount, client_personal_address, client_fav_address1, client_fav_address2, client_fav_address3, order_reference_code, order_status FROM orders WHERE order_id=%d OR order_reference_code='%s'", InputValidator.is_valid_number(search_val)==true?Integer.parseInt(search_val):0, search_val);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				Orders order=new Orders();
				order.set_data("order_id",Integer.toString(result.getInt("order_id")));
				order.set_data("payment_id",result.getString("payment_id"));
				order.set_data("payment_order_id",result.getString("payment_order_id"));
				order.set_data("client_id",Integer.toString(result.getInt("client_id")));
				order.set_data("order_date",String.valueOf(result.getDate("order_date")));
				order.set_data("total_amount",Double.toString(result.getDouble("total_amount")));
				order.set_data("client_personal_address",result.getString("client_personal_address"));
				order.set_data("client_fav_address1",result.getString("client_fav_address1"));
				order.set_data("client_fav_address2",result.getString("client_fav_address2"));
				order.set_data("client_fav_address3",result.getString("client_fav_address3"));
				order.set_data("order_reference_code",result.getString("order_reference_code"));
				order.set_data("order_status",Integer.toString(result.getInt("order_status")));
				list.add(order);
			}
			close_connection();
			return list;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}

	final private ArrayList<Orders> load_orders(int status) {
		set_connection();
		ArrayList<Orders> list=new ArrayList<Orders>();
		String query=String.format("SELECT order_id, payment_id, payment_order_id, client_id, order_date, total_amount, client_personal_address, client_fav_address1, client_fav_address2, client_fav_address3, order_reference_code, order_status FROM orders WHERE order_status=%d",status);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				Orders order=new Orders();
				order.set_data("order_id",Integer.toString(result.getInt("order_id")));
				order.set_data("payment_id",result.getString("payment_id"));
				order.set_data("payment_order_id",result.getString("payment_order_id"));
				order.set_data("client_id",Integer.toString(result.getInt("client_id")));
				order.set_data("order_date",String.valueOf(result.getDate("order_date")));
				order.set_data("total_amount",Double.toString(result.getDouble("total_amount")));
				order.set_data("client_personal_address",result.getString("client_personal_address"));
				order.set_data("client_fav_address1",result.getString("client_fav_address1"));
				order.set_data("client_fav_address2",result.getString("client_fav_address2"));
				order.set_data("client_fav_address3",result.getString("client_fav_address3"));
				order.set_data("order_reference_code",result.getString("order_reference_code"));
				order.set_data("order_status",Integer.toString(result.getInt("order_status")));
				list.add(order);
			}
			close_connection();
			return list;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private void update_forgotten_password(String[] new_passwords, int admin_id) {
		set_connection();
		String query="UPDATE admin_personal_details SET admin_password=? WHERE admin_id=?";
		try(PreparedStatement statement=connect.prepareStatement(query)){
			statement.setString(1,new_passwords[0]);
			statement.setInt(2,admin_id);
			statement.execute();
			connect.commit();
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private void send_password_change_mail(String []admin_personal_details,SecurityCodeHandler securitycode) {
		new MailHandler(new EmailDetails("admin","admin_mail_text_forget_password",admin_personal_details[0],admin_personal_details[2],securitycode.get_security_code())).send_mail();
	}
	
	final private String[] get_admin_details(String type, String admin_details) {
		set_connection();
		String query=null;
		if(type.equals("phone")) query=String.format("SELECT admin_fname, admin_lname, admin_id, admin_email FROM admin_personal_details WHERE admin_phone='%s'",admin_details);
		else if(type.equals("email")) query=String.format("SELECT admin_fname, admin_lname, admin_id, admin_email FROM admin_personal_details WHERE admin_email='%s'",admin_details);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				String []details_from_db= {result.getString("admin_fname")+" "+result.getString("admin_lname"),Integer.toString(result.getInt("admin_id")), result.getString("admin_email")};
				close_connection();
				return details_from_db;
			}
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private ArrayList<BlogPosts> get_blog_posts_link(String search_text) {
		set_connection();
		ArrayList<BlogPosts> posts=new ArrayList<BlogPosts>();
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT * FROM blog_posts WHERE link='%s'",search_text))){
			while(result.next()) {
				BlogPosts post=new BlogPosts();
				post.set_data("blog_id",Integer.toString(result.getInt("blog_id")));
				post.set_data("blog_title",result.getString("blog_title"));
				post.set_data("blog_content",result.getString("blog_content"));
				post.set_data("blog_img",result.getString("blog_img"));
				post.set_data("client_id",Integer.toString(result.getInt("client_id")));
				post.set_data("client_name",result.getString("client_name"));
				post.set_data("link",result.getString("link"));
				post.set_data("like_count",Long.toString(result.getLong("like_count")));
				post.set_data("dislike_count",Long.toString(result.getLong("dislike_count")));
				post.set_data("upload_date",String.valueOf(result.getDate("upload_date")));
				posts.add(post);
			}
			close_connection();
			return posts;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private ArrayList<BlogPosts> get_searched_blog_posts(String search_text){
		set_connection();
		ArrayList<BlogPosts> posts=new ArrayList<BlogPosts>();
		try(PreparedStatement statement=connect.prepareStatement("SELECT * FROM blog_posts WHERE blog_title LIKE ? OR blog_content LIKE ? OR client_name LIKE ? ORDER BY upload_date DESC")){
			statement.setString(1, "%"+search_text+"%");
			statement.setString(2, "%"+search_text+"%");
			statement.setString(3, "%"+search_text+"%");
			ResultSet result=statement.executeQuery();
			while(result.next()) {
				BlogPosts post=new BlogPosts();
				post.set_data("blog_id",Integer.toString(result.getInt("blog_id")));
				post.set_data("blog_title",result.getString("blog_title"));
				post.set_data("blog_content",result.getString("blog_content"));
				post.set_data("blog_img",result.getString("blog_img"));
				post.set_data("client_id",Integer.toString(result.getInt("client_id")));
				post.set_data("client_name",result.getString("client_name"));
				post.set_data("link",result.getString("link"));
				post.set_data("like_count",Long.toString(result.getLong("like_count")));
				post.set_data("dislike_count",Long.toString(result.getLong("dislike_count")));
				post.set_data("upload_date",String.valueOf(result.getDate("upload_date")));
				posts.add(post);
			}
			result.close();
			close_connection();
			return posts;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private void delete_blog_post(long blog_id) {
		set_connection();
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT blog_img FROM blog_posts WHERE blog_id=%d",blog_id))){
			connect.createStatement().execute(String.format("DELETE FROM blog_posts WHERE blog_id=%d",blog_id));
			connect.commit();
			if(result.next())new File("E:/Dynamic Web Project/src/main/webapp/Blog post images/"+result.getString("blog_img")).delete();
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private ArrayList<BlogPosts> filter_blog_posts(String filter_option){
		set_connection();
		String query=null;
		ArrayList<BlogPosts> posts=new ArrayList<BlogPosts>();
		switch(filter_option) {
		case "new"->{query="SELECT * FROM blog_posts ORDER BY upload_date DESC";}
		case "old"->{query="SELECT * FROM blog_posts ORDER BY upload_date ASC";}
		case "most-liked"->{query="SELECT * FROM blog_posts ORDER BY like_count DESC";}
		case "most-disliked"->{query="SELECT * FROM blog_posts ORDER BY dislike_count DESC";}
		}
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				BlogPosts post=new BlogPosts();
				post.set_data("blog_id",Integer.toString(result.getInt("blog_id")));
				post.set_data("blog_title",result.getString("blog_title"));
				post.set_data("blog_content",result.getString("blog_content"));
				post.set_data("blog_img",result.getString("blog_img"));
				post.set_data("client_id",Integer.toString(result.getInt("client_id")));
				post.set_data("client_name",result.getString("client_name"));
				post.set_data("link",result.getString("link"));
				post.set_data("like_count",Long.toString(result.getLong("like_count")));
				post.set_data("dislike_count",Long.toString(result.getLong("dislike_count")));
				post.set_data("upload_date",String.valueOf(result.getDate("upload_date")));
				posts.add(post);
			}
			close_connection();
			return posts;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private ArrayList<BlogPosts> load_all_blogs(){
		set_connection();
		ArrayList<BlogPosts> posts=new ArrayList<BlogPosts>();
		try(ResultSet result=connect.createStatement().executeQuery("SELECT * FROM blog_posts ORDER BY upload_date DESC")){
			while(result.next()) {
				BlogPosts post=new BlogPosts();
				post.set_data("blog_id",Integer.toString(result.getInt("blog_id")));
				post.set_data("blog_title",result.getString("blog_title"));
				post.set_data("blog_content",result.getString("blog_content"));
				post.set_data("blog_img",result.getString("blog_img"));
				post.set_data("client_id",Integer.toString(result.getInt("client_id")));
				post.set_data("client_name",result.getString("client_name"));
				post.set_data("link",result.getString("link"));
				post.set_data("like_count",Long.toString(result.getLong("like_count")));
				post.set_data("dislike_count",Long.toString(result.getLong("dislike_count")));
				post.set_data("upload_date",String.valueOf(result.getDate("upload_date")));
				posts.add(post);
			}
			close_connection();
			return posts;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private void delete_client(int client_id) {
		set_connection();
		String query=String.format("SELECT blog_img FROM blog_posts WHERE client_id=%d", client_id);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				new File("E:/Dynamic Web Project/src/main/webapp/Blog post images/"+result.getString("blog_img")).delete();
			}
			connect.createStatement().execute(String.format("DELETE FROM client_personal_details WHERE client_id=%d",client_id));
			connect.commit();
		}catch(Exception e) {e.printStackTrace();
		try {connect.rollback();} 
		catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	final private ArrayList<ClientPersonalDetails> get_details_of_clients(){
		set_connection();
		ArrayList<ClientPersonalDetails> data_list=new ArrayList<ClientPersonalDetails>();
		String query="SELECT cpd.client_id, cpd.client_fname, cpd.client_lname, cpd.client_email, cpd.client_phone, cpd.client_username, cpd.client_profile_picture,"+
					 "ca.client_address_type, ca.client_address, ca.client_city, ca.client_state, ca.client_landmark, ca.client_zip_code FROM client_personal_details cpd INNER JOIN client_address ca ON cpd.client_username=ca.client_username";
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				ClientPersonalDetails data=new ClientPersonalDetails();
				data.set_data("client_id",Integer.toString(result.getInt("client_id")));
				data.set_data("client_fname",result.getString("client_fname"));
				data.set_data("client_lname",result.getString("client_lname"));
				data.set_data("client_email",result.getString("client_email"));
				data.set_data("client_phone",result.getString("client_phone"));
				data.set_data("client_profile_picture",result.getString("client_profile_picture"));
				data.set_data("client_username",result.getString("client_username"));
				data.set_data("client_address_type",result.getString("client_address_type"));
				data.set_data("client_address",result.getString("client_address"));
				data.set_data("client_city",result.getString("client_city"));
				data.set_data("client_state",result.getString("client_state"));
				data.set_data("client_landmark",result.getString("client_landmark"));
				data.set_data("client_zip_code",Integer.toString(result.getInt("client_zip_code")));
				data_list.add(data);
			}
			close_connection();
			return data_list;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private ArrayList<Object> get_searched_products(String product_type) {
		set_connection();
		ArrayList<Object> data=new ArrayList<Object>();
		String clothing_query="SELECT vendor_username,product_id,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_img1,product_size1,product_size2,product_size3,product_size4,product_size5,product_refund_replace_option,product_rating,product_status,product_status_updated_by FROM clothing_products WHERE (product_name LIKE ? OR FIND_IN_SET(? , REPLACE(product_keywords, ' ', '')))";
		String electronics_query="SELECT vendor_username,product_id,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_technical_description,product_img1,product_refund_replace_option,product_rating,product_status,product_status_updated_by FROM electronics_products WHERE (product_name LIKE ? OR FIND_IN_SET(? , REPLACE(product_keywords, ' ', '')))";
		String food_query="SELECT vendor_username,product_id,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_shelf_life,product_perishable,product_vegetarian,product_meat_type,product_img1,product_refund_replace_option,product_rating,product_status,product_status_updated_by FROM food_products WHERE (product_name LIKE ? OR FIND_IN_SET(? , REPLACE(product_keywords, ' ', '')))";
		try{
			PreparedStatement statement1=connect.prepareStatement(clothing_query);
			statement1.setString(1, product_type);statement1.setString(2, product_type.trim().replaceAll(" ",""));
			ResultSet result1=statement1.executeQuery();
			while(result1.next()) {
				AllProducts.Clothing product=new AllProducts.Clothing();
				product.set_data("product_id",Integer.toString(result1.getInt("product_id")));
				product.set_data("product_type",result1.getString("product_type"));
				product.set_data("product_name",result1.getString("product_name"));
				product.set_data("product_brand_name",result1.getString("product_brand_name"));
				product.set_data("product_original_price",Double.toString(result1.getDouble("product_original_price")));
				product.set_data("product_selling_price",Double.toString(result1.getDouble("product_selling_price")));
				product.set_data("product_quantity",Integer.toString(result1.getInt("product_quantity")));
				product.set_data("product_stock_status",Integer.toString(result1.getInt("product_stock_status")));
				product.set_data("product_keywords",result1.getString("product_keywords"));
				product.set_data("product_description",result1.getString("product_description"));
				product.set_data("product_img1",result1.getString("product_img1"));
				product.set_data("product_size1",result1.getString("product_size1"));
				product.set_data("product_size2",result1.getString("product_size2"));
				product.set_data("product_size3",result1.getString("product_size3"));
				product.set_data("product_size4",result1.getString("product_size4"));
				product.set_data("product_size5",result1.getString("product_size5"));
				product.set_data("product_refund_replace_option",result1.getString("product_refund_replace_option"));
				product.set_data("product_rating",Float.toString(result1.getFloat("product_rating")));
				product.set_data("product_status",Integer.toString(result1.getInt("product_status")));
				product.set_data("product_status_updated_by",result1.getString("product_status_updated_by"));
				data.add(product);
			}
			statement1.close();
			result1.close();
			
			PreparedStatement statement2=connect.prepareStatement(electronics_query);
			statement2.setString(1, product_type);statement2.setString(2, product_type.trim().replaceAll(" ",""));
			ResultSet result2=statement2.executeQuery();
			while(result2.next()) {
				AllProducts.Electronics product=new AllProducts.Electronics();
				product.set_data("product_id",Integer.toString(result2.getInt("product_id")));
				product.set_data("product_type",result2.getString("product_type"));
				product.set_data("product_name",result2.getString("product_name"));
				product.set_data("product_brand_name",result2.getString("product_brand_name"));
				product.set_data("product_original_price",Double.toString(result2.getDouble("product_original_price")));
				product.set_data("product_selling_price",Double.toString(result2.getDouble("product_selling_price")));
				product.set_data("product_quantity",Integer.toString(result2.getInt("product_quantity")));
				product.set_data("product_stock_status",Integer.toString(result2.getInt("product_stock_status")));
				product.set_data("product_keywords",result2.getString("product_keywords"));
				product.set_data("product_description",result2.getString("product_description"));
				product.set_data("product_technical_description",result2.getString("product_technical_description"));
				product.set_data("product_img1",result2.getString("product_img1"));
				product.set_data("product_refund_replace_option",result2.getString("product_refund_replace_option"));
				product.set_data("product_rating",Float.toString(result2.getFloat("product_rating")));
				product.set_data("product_status",Integer.toString(result2.getInt("product_status")));
				product.set_data("product_status_updated_by",result2.getString("product_status_updated_by"));
				data.add(product);
			}
			statement2.close();
			result2.close();
			
			PreparedStatement statement3=connect.prepareStatement(food_query);
			statement3.setString(1, product_type);statement3.setString(2, product_type.trim().replaceAll(" ",""));
			ResultSet result3=statement3.executeQuery();
			while(result3.next()) {
				AllProducts.Food product=new AllProducts.Food();
				product.set_data("product_id",Integer.toString(result3.getInt("product_id")));
				product.set_data("product_type",result3.getString("product_type"));
				product.set_data("product_name",result3.getString("product_name"));
				product.set_data("product_brand_name",result3.getString("product_brand_name"));
				product.set_data("product_original_price",Double.toString(result3.getDouble("product_original_price")));
				product.set_data("product_selling_price",Double.toString(result3.getDouble("product_selling_price")));
				product.set_data("product_quantity",Integer.toString(result3.getInt("product_quantity")));
				product.set_data("product_stock_status",Integer.toString(result3.getInt("product_stock_status")));
				product.set_data("product_keywords",result3.getString("product_keywords"));
				product.set_data("product_description",result3.getString("product_description"));
				product.set_data("product_shelf_life",result3.getString("product_shelf_life"));
				product.set_data("product_perishable",result3.getString("product_perishable"));
				product.set_data("product_vegetarian",result3.getString("product_vegetarian"));
				product.set_data("product_meat_type",result3.getString("product_meat_type"));
				product.set_data("product_img1",result3.getString("product_img1"));
				product.set_data("product_refund_replace_option",result3.getString("product_refund_replace_option"));
				product.set_data("product_rating",Float.toString(result3.getFloat("product_rating")));
				product.set_data("product_status",Integer.toString(result3.getInt("product_status")));
				product.set_data("product_status_updated_by",result3.getString("product_status_updated_by"));
				data.add(product);
			}
			statement3.close();
			result3.close();
			close_connection();
			return data;
		}
		catch(Exception e) {e.printStackTrace();close_connection();}
		return null;
	}
	
	final private ArrayList<Object> get_product(String product_type) {
		set_connection();
		ArrayList<Object> data=new ArrayList<Object>();
		switch(product_type) {
		case "clothing_products"->{
			try {
				ResultSet result1=connect.createStatement().executeQuery("SELECT product_id,vendor_username,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_img1,product_size1,product_size2,product_size3,product_size4,product_size5,product_refund_replace_option,product_rating,product_status,product_status_updated_by FROM clothing_products");
				while(result1.next()) {
					AllProducts.Clothing product=new AllProducts.Clothing();
					product.set_data("product_id",Integer.toString(result1.getInt("product_id")));
					product.set_data("product_brand_name",result1.getString("product_brand_name"));
					product.set_data("product_type",result1.getString("product_type"));
					product.set_data("product_name",result1.getString("product_name"));
					product.set_data("product_original_price",Double.toString(result1.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result1.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result1.getInt("product_quantity")));
					product.set_data("product_stock_status",result1.getString("product_stock_status"));
					product.set_data("product_keywords",result1.getString("product_keywords"));
					product.set_data("product_description",result1.getString("product_description"));
					product.set_data("product_img1",result1.getString("product_img1"));
					product.set_data("product_size1",result1.getString("product_size1"));
					product.set_data("product_size2",result1.getString("product_size2"));
					product.set_data("product_size3",result1.getString("product_size3"));
					product.set_data("product_size4",result1.getString("product_size4"));
					product.set_data("product_size5",result1.getString("product_size5"));
					product.set_data("product_refund_replace_option",result1.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result1.getFloat("product_rating")));
					product.set_data("product_status",Integer.toString(result1.getInt("product_status")));
					product.set_data("product_status_updated_by", result1.getString("product_status_updated_by"));
					data.add(product);
				}
				result1.close();
				close_connection();
			}
			catch(Exception e) {e.printStackTrace();close_connection();}
			return data;
		}
		case "electronics_products"->{
			try {
				ResultSet result2=connect.createStatement().executeQuery("SELECT product_id,vendor_username,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_technical_description,product_img1,product_refund_replace_option,product_rating,product_status,product_status_updated_by FROM electronics_products");
				while(result2.next()) {
					AllProducts.Electronics product=new AllProducts.Electronics();
					product.set_data("product_id",Integer.toString(result2.getInt("product_id")));
					product.set_data("product_brand_name",result2.getString("product_brand_name"));
					product.set_data("product_type",result2.getString("product_type"));
					product.set_data("product_name",result2.getString("product_name"));
					product.set_data("product_original_price",Double.toString(result2.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result2.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result2.getInt("product_quantity")));
					product.set_data("product_stock_status",result2.getString("product_stock_status"));
					product.set_data("product_keywords",result2.getString("product_keywords"));
					product.set_data("product_description",result2.getString("product_description"));
					product.set_data("product_technical_description",result2.getString("product_technical_description"));
					product.set_data("product_img1",result2.getString("product_img1"));
					product.set_data("product_refund_replace_option",result2.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result2.getFloat("product_rating")));
					product.set_data("product_status",Integer.toString(result2.getInt("product_status")));
					product.set_data("product_status_updated_by", result2.getString("product_status_updated_by"));
					data.add(product);
				}
				result2.close();
				close_connection();
			}
			catch(Exception e) {e.printStackTrace();close_connection();}
			return data;
		}
		case "food_products"->{
			try {
				ResultSet result3=connect.createStatement().executeQuery("SELECT product_id,vendor_username,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_shelf_life,product_perishable,product_vegetarian,product_meat_type,product_img1,product_refund_replace_option,product_rating,product_status,product_status_updated_by FROM food_products");
				while(result3.next()) {
					AllProducts.Food product=new AllProducts.Food();
					product.set_data("product_id",Integer.toString(result3.getInt("product_id")));
					product.set_data("product_brand_name",result3.getString("product_brand_name"));
					product.set_data("product_type",result3.getString("product_type"));
					product.set_data("product_name",result3.getString("product_name"));
					product.set_data("product_original_price",Double.toString(result3.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result3.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result3.getInt("product_quantity")));
					product.set_data("product_stock_status",result3.getString("product_stock_status"));
					product.set_data("product_keywords",result3.getString("product_keywords"));
					product.set_data("product_description",result3.getString("product_description"));
					product.set_data("product_shelf_life",result3.getString("product_shelf_life"));
					product.set_data("product_perishable",result3.getString("product_perishable"));
					product.set_data("product_vegetarian",result3.getString("product_vegetarian"));
					product.set_data("product_meat_type",result3.getString("product_meat_type"));
					product.set_data("product_img1",result3.getString("product_img1"));
					product.set_data("product_refund_replace_option",result3.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result3.getFloat("product_rating")));
					product.set_data("product_status",Integer.toString(result3.getInt("product_status")));
					product.set_data("product_status_updated_by", result3.getString("product_status_updated_by"));
					data.add(product);
				}
				result3.close();
				close_connection();
			}
			catch(Exception e) {e.printStackTrace();close_connection();}
			return data;
		}
		}
		return null;
	}
	
	final private void send_product_status_mail(int product_id,int status,String product_type) {
	String query=String.format("SELECT p.product_name, p.product_updated, vpd.vendor_fname, vpd.vendor_lname, vpd.vendor_email FROM "+product_type+"_products p INNER JOIN vendor_personal_details vpd ON p.vendor_username=vpd.vendor_username WHERE product_id=%d",product_id);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				new MailHandler(new EmailDetails("vendor","product_status_mail",result.getString("vendor_fname")+" "+result.getString("vendor_lname"),result.getString("vendor_email"),product_type,result.getString("product_name"),status,String.valueOf(result.getDate("product_updated")))).send_mail();
			}
		}catch(Exception e) {e.printStackTrace();}
	}
	
	final private void send_product_status_deletion_mail(int product_id,int status,String product_type) {
		String query=String.format("SELECT p.product_name, vpd.vendor_fname, vpd.vendor_lname, vpd.vendor_email FROM "+product_type+"_products p INNER JOIN vendor_personal_details vpd ON p.vendor_username=vpd.vendor_username WHERE product_id=%d",product_id);
			try(ResultSet result=connect.createStatement().executeQuery(query)){
				if(result.next()) {
					new MailHandler(new EmailDetails("vendor","product_status_mail",result.getString("vendor_fname")+" "+result.getString("vendor_lname"),result.getString("vendor_email"),product_type,result.getString("product_name"),status)).send_mail();
				}
			}catch(Exception e) {e.printStackTrace();}
		}
	
	final private void update_product_status(int product_id,int status,String product_type,HttpServletRequest request) {
		set_connection();
		switch(status) {
		case 0,1,3->{
			try(PreparedStatement statement1=connect.prepareStatement("UPDATE "+product_type+"_products SET product_status=?, product_status_updated_by=? WHERE product_id=?")){
				statement1.setInt(1,status);
				statement1.setString(2,(String)request.getSession().getAttribute("Admin-username"));
				statement1.setInt(3,product_id);
				statement1.execute();
				connect.commit();
			}
			catch(Exception e) {
				try {connect.rollback();} 
				catch (SQLException e1) {e1.printStackTrace();} 
				e.printStackTrace();
			}
			send_product_status_mail(product_id,status,product_type);
		}
		case 2->{
			send_product_status_deletion_mail(product_id,status,product_type);
			try{
				if(product_type.equals("clothing")) {
					PreparedStatement photos=connect.prepareStatement("SELECT product_img1,product_img2,product_img3,product_img4,product_img5 FROM clothing_products WHERE product_id=?");
					photos.setInt(1,product_id);
					ResultSet result=photos.executeQuery();
					if(result.next()) {
						File []paths= {
							new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img1")),
							new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img2")),
							new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img3")),
							new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img4")),
							new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img5"))
						};
						PreparedStatement statement2=connect.prepareStatement("DELETE FROM "+product_type+"_products WHERE product_id=?");
						statement2.setString(1,"clothing");statement2.setInt(2,product_id);
						statement2.execute();
						statement2.close();
						connect.commit();
						for(File path:paths) {
							path.delete();
						}
					}
					photos.close();
					result.close();
				}
				else if(product_type.equals("electronics")) {
					PreparedStatement photos=connect.prepareStatement("SELECT product_img1,product_img2,product_img3,product_img4,product_img5 FROM electronics_products WHERE product_id=?");
					photos.setInt(1,product_id);
					ResultSet result=photos.executeQuery();
					if(result.next()) {
						File []paths= {
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img1")),
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img2")),
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img3")),
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img4")),
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img5"))
						};
						PreparedStatement statement2=connect.prepareStatement("DELETE FROM "+product_type+"_products WHERE product_id=?");
						statement2.setString(1,"electronics");statement2.setInt(2,product_id);
						statement2.execute();
						statement2.close();
						connect.commit();
						for(File path:paths) {
							path.delete();
						}
					}
					photos.close();
					result.close();
				}
				else if(product_type.equals("food")) {
					PreparedStatement photos=connect.prepareStatement("SELECT product_img1,product_img2,product_img3,product_img4,product_img5 FROM food_products WHERE product_id=?");
					photos.setInt(1,product_id);
					ResultSet result=photos.executeQuery();
					if(result.next()) {
						File []paths= {
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img1")),
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img2")),
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img3")),
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img4")),
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img5"))
						};
						PreparedStatement statement2=connect.prepareStatement("DELETE FROM "+product_type+"_products WHERE product_id=?");
						statement2.setString(1,"food");statement2.setInt(2,product_id);
						statement2.execute();
						statement2.close();
						connect.commit();
						for(File path:paths) {
							path.delete();
						}
					}
					photos.close();
					result.close();
				}
			}
			catch(Exception e) {
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();} 
			e.printStackTrace();
			}
		}
		}
		close_connection();
	}
	
	final private ArrayList<Object> get_all_products(int status){
		set_connection();
		ArrayList<Object> data=new ArrayList<Object>();
		String clothing_query=String.format("SELECT product_id,vendor_username,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_img1,product_size1,product_size2,product_size3,product_size4,product_size5,product_refund_replace_option,product_rating,product_status,product_status_updated_by FROM clothing_products WHERE product_status=%d",status);
		String electronics_query=String.format("SELECT product_id,vendor_username,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_technical_description,product_img1,product_refund_replace_option,product_rating,product_status,product_status_updated_by FROM electronics_products WHERE product_status=%d",status);
		String food_query=String.format("SELECT product_id,vendor_username,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_shelf_life,product_perishable,product_vegetarian,product_meat_type,product_img1,product_refund_replace_option,product_rating,product_status,product_status_updated_by FROM food_products WHERE product_status=%d",status);
		try {
			ResultSet result1=connect.createStatement().executeQuery(clothing_query);
			while(result1.next()) {
				AllProducts.Clothing product=new AllProducts.Clothing();
				product.set_data("product_id",Integer.toString(result1.getInt("product_id")));
				product.set_data("product_brand_name",result1.getString("product_brand_name"));
				product.set_data("product_type",result1.getString("product_type"));
				product.set_data("product_name",result1.getString("product_name"));
				product.set_data("product_original_price",Double.toString(result1.getDouble("product_original_price")));
				product.set_data("product_selling_price",Double.toString(result1.getDouble("product_selling_price")));
				product.set_data("product_quantity",Integer.toString(result1.getInt("product_quantity")));
				product.set_data("product_stock_status",result1.getString("product_stock_status"));
				product.set_data("product_keywords",result1.getString("product_keywords"));
				product.set_data("product_description",result1.getString("product_description"));
				product.set_data("product_img1",result1.getString("product_img1"));
				product.set_data("product_size1",result1.getString("product_size1"));
				product.set_data("product_size2",result1.getString("product_size2"));
				product.set_data("product_size3",result1.getString("product_size3"));
				product.set_data("product_size4",result1.getString("product_size4"));
				product.set_data("product_size5",result1.getString("product_size5"));
				product.set_data("product_refund_replace_option",result1.getString("product_refund_replace_option"));
				product.set_data("product_rating",Float.toString(result1.getFloat("product_rating")));
				product.set_data("product_status",Integer.toString(result1.getInt("product_status")));
				product.set_data("product_status_updated_by", result1.getString("product_status_updated_by"));
				data.add(product);
			}
			result1.close();
			ResultSet result2=connect.createStatement().executeQuery(electronics_query);
			while(result2.next()) {
				AllProducts.Electronics product=new AllProducts.Electronics();
				product.set_data("product_id",Integer.toString(result2.getInt("product_id")));
				product.set_data("product_brand_name",result2.getString("product_brand_name"));
				product.set_data("product_type",result2.getString("product_type"));
				product.set_data("product_name",result2.getString("product_name"));
				product.set_data("product_original_price",Double.toString(result2.getDouble("product_original_price")));
				product.set_data("product_selling_price",Double.toString(result2.getDouble("product_selling_price")));
				product.set_data("product_quantity",Integer.toString(result2.getInt("product_quantity")));
				product.set_data("product_stock_status",result2.getString("product_stock_status"));
				product.set_data("product_keywords",result2.getString("product_keywords"));
				product.set_data("product_description",result2.getString("product_description"));
				product.set_data("product_technical_description",result2.getString("product_technical_description"));
				product.set_data("product_img1",result2.getString("product_img1"));
				product.set_data("product_refund_replace_option",result2.getString("product_refund_replace_option"));
				product.set_data("product_rating",Float.toString(result2.getFloat("product_rating")));
				product.set_data("product_status",Integer.toString(result2.getInt("product_status")));
				product.set_data("product_status_updated_by", result2.getString("product_status_updated_by"));
				data.add(product);
			}
			result2.close();
			ResultSet result3=connect.createStatement().executeQuery(food_query);
			while(result3.next()) {
				AllProducts.Food product=new AllProducts.Food();
				product.set_data("product_id",Integer.toString(result3.getInt("product_id")));
				product.set_data("product_brand_name",result3.getString("product_brand_name"));
				product.set_data("product_type",result3.getString("product_type"));
				product.set_data("product_name",result3.getString("product_name"));
				product.set_data("product_original_price",Double.toString(result3.getDouble("product_original_price")));
				product.set_data("product_selling_price",Double.toString(result3.getDouble("product_selling_price")));
				product.set_data("product_quantity",Integer.toString(result3.getInt("product_quantity")));
				product.set_data("product_stock_status",result3.getString("product_stock_status"));
				product.set_data("product_keywords",result3.getString("product_keywords"));
				product.set_data("product_description",result3.getString("product_description"));
				product.set_data("product_shelf_life",result3.getString("product_shelf_life"));
				product.set_data("product_perishable",result3.getString("product_perishable"));
				product.set_data("product_vegetarian",result3.getString("product_vegetarian"));
				product.set_data("product_meat_type",result3.getString("product_meat_type"));
				product.set_data("product_img1",result3.getString("product_img1"));
				product.set_data("product_refund_replace_option",result3.getString("product_refund_replace_option"));
				product.set_data("product_rating",Float.toString(result3.getFloat("product_rating")));
				product.set_data("product_status",Integer.toString(result3.getInt("product_status")));
				product.set_data("product_status_updated_by", result3.getString("product_status_updated_by"));
				data.add(product);
			}
			result3.close();
			close_connection();
			return data;
			
		}catch(Exception e) {e.printStackTrace();close_connection();}
		return null;
	}
	
	final private ArrayList<AdminPersonalDetails> get_details_of_admins(int status) {
		set_connection();
		ArrayList<AdminPersonalDetails> data_list=new ArrayList<AdminPersonalDetails>();
		String query=String.format("SELECT apd.admin_id, apd.admin_fname, apd.admin_lname, apd.admin_email, apd.admin_phone, apd.admin_username, apd.admin_profile_picture, apd.admin_govId_picture, apd.admin_status_updated, aa.admin_address, aa.admin_city, aa.admin_state, aa.admin_zip_code FROM admin_personal_details apd INNER JOIN admin_address aa ON apd.admin_username = aa.admin_username WHERE apd.admin_status = %d", status);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				AdminPersonalDetails data=new AdminPersonalDetails();
				data.set_data("admin_id", Integer.toString(result.getInt("admin_id")));
				data.set_data("admin_full_name", result.getString("admin_fname")+" "+result.getString("admin_lname"));
				data.set_data("admin_email", result.getString("admin_email"));
				data.set_data("admin_phone", result.getString("admin_phone"));
				data.set_data("admin_username", result.getString("admin_username"));
				data.set_data("admin_profile_picture", result.getString("admin_profile_picture"));
				data.set_data("admin_govId_picture", result.getString("admin_govId_picture"));
				data.set_data("admin_status_updated", result.getString("admin_status_updated"));
				data.set_data("admin_address", result.getString("admin_address"));
				data.set_data("admin_city", result.getString("admin_city"));
				data.set_data("admin_state", result.getString("admin_state"));
				data.set_data("admin_zip_code", result.getString("admin_zip_code"));
				data.set_data("admin_status",Integer.toString(status));
				data_list.add(data);
			}
			close_connection();
			return data_list;
		}
		catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}

	final private ArrayList<VendorPersonalDetails> get_details_of_vendors(int status){
		set_connection();
		ArrayList<VendorPersonalDetails> data_list=new ArrayList<VendorPersonalDetails>();
		String query=String.format("SELECT vpd.vendor_id, vpd.vendor_fname, vpd.vendor_lname, vpd.vendor_email, vpd.vendor_phone, " +
			    "vpd.vendor_username, vpd.vendor_gst_number, vpd.vendor_trade_license, vpd.vendor_govId_picture, vpd.vendor_status_updated, " +
			    "vpa.vendor_address, vpa.vendor_city, vpa.vendor_state, vpa.vendor_zip_code, " +
			    "vsa.vendor_shop_name_number, vsa.vendor_shop_address, vsa.vendor_shop_city, vsa.vendor_shop_state, vsa.vendor_shop_zip_code " +
			    "FROM vendor_personal_details vpd " +
			    "INNER JOIN vendor_personal_address vpa ON vpd.vendor_username = vpa.vendor_username " +
			    "INNER JOIN vendor_shop_address vsa ON vpd.vendor_username = vsa.vendor_username " +
			    "WHERE vpd.vendor_status = %d", status);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				VendorPersonalDetails data=new VendorPersonalDetails();
				data.set_data("vendor_id", Integer.toString(result.getInt("vendor_id")));
				data.set_data("vendor_full_name", result.getString("vendor_fname")+" "+result.getString("vendor_lname"));
				data.set_data("vendor_email", result.getString("vendor_email"));
				data.set_data("vendor_phone", result.getString("vendor_phone"));
				data.set_data("vendor_username", result.getString("vendor_username"));
				data.set_data("vendor_gst_number", result.getString("vendor_gst_number"));
				data.set_data("vendor_trade_license", result.getString("vendor_trade_license"));
				data.set_data("vendor_govId_picture", result.getString("vendor_govId_picture"));
				data.set_data("vendor_status_updated", result.getString("vendor_status_updated"));
				data.set_data("vendor_status", Integer.toString(status));
				data.set_data("vendor_address", result.getString("vendor_address"));
				data.set_data("vendor_city", result.getString("vendor_city"));
				data.set_data("vendor_state", result.getString("vendor_state"));
				data.set_data("vendor_zip_code", result.getString("vendor_zip_code"));
				data.set_data("vendor_shop_name_number",result.getString("vendor_shop_name_number"));
				data.set_data("vendor_shop_address", result.getString("vendor_shop_address"));
				data.set_data("vendor_shop_city", result.getString("vendor_shop_city"));
				data.set_data("vendor_shop_state", result.getString("vendor_shop_state"));
				data.set_data("vendor_shop_zip_code", result.getString("vendor_shop_zip_code"));
				data_list.add(data);
			}
			close_connection();
			return data_list;
		}
		catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private void send_admin_status_mail(int admin_id,int status,String user_type) {
		String query=String.format("SELECT admin_fname, admin_lname, admin_email FROM admin_personal_details WHERE admin_id=%d",admin_id);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				new MailHandler(new EmailDetails(user_type,"status_mail",result.getString("admin_fname")+" "+result.getString("admin_lname"),result.getString("admin_email"),status)).send_mail();
			}
		}catch(Exception e) {e.printStackTrace();}
	}
	
	final private void send_vendor_status_mail(int vendor_id,int status,String user_type) {
		String query=String.format("SELECT vendor_fname, vendor_lname, vendor_email FROM vendor_personal_details WHERE vendor_id=%d",vendor_id);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				new MailHandler(new EmailDetails(user_type,"status_mail",result.getString("vendor_fname")+" "+result.getString("vendor_lname"),result.getString("vendor_email"),status)).send_mail();
			}
		}catch(Exception e) {e.printStackTrace();}
	}
	
	final private void update_admin_status(int admin_id,int status,HttpServletRequest request) {
		set_connection();
		switch(status) {
			case 0,1,3->{
				send_admin_status_mail(admin_id, status,"admin");
				String query="UPDATE admin_personal_details SET admin_status=?, admin_status_updated=? WHERE admin_id=?";
				try(PreparedStatement statement=connect.prepareStatement(query)){
					statement.setInt(1, status);
					statement.setString(2,(String)request.getSession().getAttribute("Admin-username"));
					statement.setInt(3, admin_id);
					statement.executeUpdate();
					connect.commit();
				}
				catch(Exception e) {e.printStackTrace();
				try {
					connect.rollback();
				} catch (SQLException e1) {e1.printStackTrace();}
				}
			}
			case 2->{
			send_admin_status_mail(admin_id, status,"admin");	
			String query1=String.format("SELECT admin_profile_picture,admin_govId_picture FROM admin_personal_details WHERE admin_id=%d",admin_id);
			String path1=null,path2=null;
			try {
				ResultSet result=connect.createStatement().executeQuery(query1);
				if(result.next()) {
					path1="E:/Dynamic Web Project/src/main/webapp/Admin Profile Pictures/"+result.getString("admin_profile_picture");
					path2="E:/Dynamic Web Project/src/main/webapp/Admin GovID/"+result.getString("admin_govId_picture");
				}
				result.close();
			}
			catch(Exception e){System.out.println(e.getMessage());}
			String query2="DELETE FROM admin_personal_details WHERE admin_id=?";
			try(PreparedStatement statement=connect.prepareStatement(query2)){
				File del_path1=new File(path1);
				File del_path2=new File(path2);
				statement.setInt(1,admin_id);
				statement.executeUpdate();
				connect.commit();
				del_path1.delete();
				del_path2.delete();
			}
			catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
			}
		}
		}
		close_connection();
	}
	
	final private void update_vendor_status(int vendor_id,int status,HttpServletRequest request) {
		set_connection();
		switch(status) {
		case 0,1,3->{
			send_vendor_status_mail(vendor_id, status,"vendor");
			String query="UPDATE vendor_personal_details SET vendor_status=?, vendor_status_updated=? WHERE vendor_id=?";
			try(PreparedStatement statement=connect.prepareStatement(query)){
				statement.setInt(1, status);
				statement.setString(2,(String)request.getSession().getAttribute("Admin-username"));
				statement.setInt(3, vendor_id);
				statement.executeUpdate();
				connect.commit();
			}
			catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
			}
			
		}
		case 2->{
			send_vendor_status_mail(vendor_id, status,"vendor");
			delete_products_of_vendor(vendor_id);
			String query1=String.format("SELECT vendor_trade_license,vendor_govId_picture FROM vendor_personal_details WHERE vendor_id=%d",vendor_id);
			String path1=null,path2=null;
			try {
				ResultSet result=connect.createStatement().executeQuery(query1);
				if(result.next()) {
					path1="E:/Dynamic Web Project/src/main/webapp/Vendor Trade License/"+result.getString("vendor_trade_license");
					path2="E:/Dynamic Web Project/src/main/webapp/Vendor GovID/"+result.getString("vendor_govId_picture");
				}
				result.close();
			}
			catch(Exception e){System.out.println(e.getMessage());}
			String query2="DELETE FROM vendor_personal_details WHERE vendor_id=?";
			try(PreparedStatement statement=connect.prepareStatement(query2)){
				File del_path1=new File(path1);
				File del_path2=new File(path2);
				statement.setInt(1,vendor_id);
				statement.executeUpdate();
				connect.commit();
				del_path1.delete();
				del_path2.delete();
			}
			catch(Exception e) {e.printStackTrace();
			try {
				connect.rollback();
			} catch (SQLException e1) {e1.printStackTrace();}
			}
		}
		}
		alter_product_status_of_the_vendor(vendor_id,status);
		close_connection();
	}
	
	final private void alter_product_status_of_the_vendor(int vendor_id,int status) {
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT vendor_username FROM vendor_personal_details WHERE vendor_id=%d",vendor_id))){
			if(result.next()) {
				connect.createStatement().executeUpdate(String.format("UPDATE clothing_products SET product_status=%d WHERE vendor_username='%s'",status,result.getString("vendor_username")));
				connect.createStatement().executeUpdate(String.format("UPDATE electronics_products SET product_status=%d WHERE vendor_username='%s'",status,result.getString("vendor_username")));
				connect.createStatement().executeUpdate(String.format("UPDATE food_products SET product_status=%d WHERE vendor_username='%s'",status,result.getString("vendor_username")));
				connect.commit();
			}
		}catch(Exception e) {
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
			e.printStackTrace();
		}
	}
	
	final private void delete_products_of_vendor(int vendor_id) {
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT vendor_username FROM vendor_personal_details WHERE vendor_id=%d",vendor_id))){
			if(result.next()) {
				ResultSet result1=connect.createStatement().executeQuery(String.format("SELECT product_img1,product_img2,product_img3,product_img4,product_img5 FROM clothing_products WHERE vendor_username='%s'",result.getString("vendor_username")));
				ResultSet result2=connect.createStatement().executeQuery(String.format("SELECT product_img1,product_img2,product_img3,product_img4,product_img5 FROM electronics_products WHERE vendor_username='%s'",result.getString("vendor_username")));
				ResultSet result3=connect.createStatement().executeQuery(String.format("SELECT product_img1,product_img2,product_img3,product_img4,product_img5 FROM food_products WHERE vendor_username='%s'",result.getString("vendor_username")));
				while(result1.next()) {
					new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result1.getString("product_img1")).delete();
					new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result1.getString("product_img2")).delete();
					new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result1.getString("product_img3")).delete();
					new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result1.getString("product_img4")).delete();
					new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result1.getString("product_img5")).delete();
				}
				result1.close();
				while(result2.next()) {
					new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result2.getString("product_img1")).delete();
					new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result2.getString("product_img2")).delete();
					new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result2.getString("product_img3")).delete();
					new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result2.getString("product_img4")).delete();
					new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result2.getString("product_img5")).delete();
				}
				result2.close();
				while(result3.next()) {
					new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result3.getString("product_img1")).delete();
					new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result3.getString("product_img2")).delete();
					new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result3.getString("product_img3")).delete();
					new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result3.getString("product_img4")).delete();
					new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result3.getString("product_img5")).delete();
				}
				result3.close();
			}
		}catch(Exception e) {e.printStackTrace();}
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
