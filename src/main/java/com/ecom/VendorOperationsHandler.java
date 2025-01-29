package com.ecom;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.entity.AllProducts;
import com.entity.EmailDetails;
import com.entity.Orders;

@MultipartConfig
@WebServlet(
        name = "VendorOperationsHandler",
        urlPatterns = { "/VendorOperationsHandler"}
)
public class VendorOperationsHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connect=null;
	private SecurityCodeHandler securitycode=null;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch(request.getParameter("request-type")) {
		case "all_products"->{
			request.getSession().setAttribute("productlist",get_all_products(request));
			request.getRequestDispatcher("Vendor_products.jsp").forward(request, response);
		}
		case "srearch-result"->{
			request.getSession().setAttribute("productlist",get_searched_products(request,(String)request.getParameter("product-type")));
			request.getRequestDispatcher("Vendor_products.jsp").forward(request, response);
		}
		case "alter_product_stock_status"->{
			String []product_info={
					request.getParameter("product-id"),
					request.getParameter("product-stock-status"),
					request.getParameter("product-type")
			};
			if(!InputValidator.is_valid_number(product_info[0]) && !InputValidator.is_valid_number(product_info[1],1) && !InputValidator.is_valid_text(product_info[2])) {request.getSession().setAttribute("msg","Values cannot be changed.");response.sendRedirect("VendorOperationsHandler?request-type=all_products");return;}
			update_product_stock(product_info);response.sendRedirect("VendorOperationsHandler?request-type=all_products");return;
		}
		case "edit_product"->{
			String []product_info={
				request.getParameter("product-id"),
				request.getParameter("product-type")
			};
			if(!InputValidator.is_valid_number(product_info[0]) && !InputValidator.is_valid_text(product_info[1])) {request.getSession().setAttribute("msg","Values cannot be changed.");response.sendRedirect("VendorOperationsHandler?request-type=all_products");return;}
			request.getSession().setAttribute("product",get_product_to_edit(request,product_info));
			request.getRequestDispatcher("Vendor_product_edit.jsp").forward(request, response);
		}
		case "to_dispatch"->{
			request.getSession().setAttribute("new_orders",load_new_product_orders((String)request.getSession().getAttribute("Vendor-username")));
			request.getRequestDispatcher("Vendor_orders.jsp").forward(request, response);
		}
		case "vendor-logout"->{
			request.getSession().removeAttribute("Vendor-username");
			response.sendRedirect("Vendor_login_signup.jsp");return;
		}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch(request.getParameter("operation")==null?"":request.getParameter("operation")) {
		case "validate-forget-pass-otp"->{
			String code=request.getParameter("forgot-pass-otp");
			if(!InputValidator.is_empty(code) && InputValidator.is_valid_number(code,6)) {
				if(this.securitycode.validate_security_code(new StringBuilder(code))) {
					request.getSession().setAttribute("change_forgotten_password","vendor");
					response.sendRedirect("Reset_password.jsp");return;
				}else {
					if(this.securitycode.code_expired()) {
						request.getSession().removeAttribute("user_type_before_otp_verify");
						request.getSession().removeAttribute("change_forgotten_password");
						request.getSession().removeAttribute("vendor_id_forget_pass");
						response.sendRedirect("Vendor_login_signup.jsp?msg=OTP expired.");return;
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
					update_forgotten_password(new_passwords,Integer.parseInt((String)request.getSession().getAttribute("vendor_id_forget_pass")));
					request.getSession().removeAttribute("user_type_before_otp_verify");
					request.getSession().removeAttribute("change_forgotten_password");
					request.getSession().removeAttribute("vendor_id_forget_pass");
					response.sendRedirect("Vendor_login_signup.jsp?msg=Password changed successfully.");return;
				}else {response.sendRedirect("Reset_password.jsp?msg=Password must contain only alphanumeric characters.");return;}
			}else {response.sendRedirect("Reset_password.jsp?msg=Passwords cannot be empty and both passwords must be same.");return;}
		}
		case "vendor-password-otp"->{
			String vendor_details=request.getParameter("vendor-email-phone");
			if(!InputValidator.is_empty(vendor_details)) {
				if(InputValidator.is_valid_phonenumber(vendor_details)) {
					String []vendor_details_from_db=get_vendor_details("phone",vendor_details);
					if(vendor_details_from_db!=null) {
						this.securitycode=new SecurityCodeHandler();
						send_password_change_mail(vendor_details_from_db,this.securitycode);
						request.getSession().setAttribute("user_type_before_otp_verify","vendor");
						request.getSession().setAttribute("vendor_id_forget_pass",vendor_details_from_db[1]);
						response.sendRedirect("Reset_password.jsp");return;
					}else {response.sendRedirect("Vendor_login_signup.jsp?msg=This phone number is not registered.");return;}
				}else if(InputValidator.is_valid_email(vendor_details)) {
					String []vendor_details_from_db=get_vendor_details("email",vendor_details);
					if(vendor_details_from_db!=null) {
						this.securitycode=new SecurityCodeHandler();
						send_password_change_mail(vendor_details_from_db,this.securitycode);
						request.getSession().setAttribute("user_type_before_otp_verify","vendor");
						request.getSession().setAttribute("vendor_id_forget_pass",vendor_details_from_db[1]);
						response.sendRedirect("Reset_password.jsp");return;
					}else {response.sendRedirect("Vendor_login_signup.jsp?msg=This email is not registered.");return;}
				}else {response.sendRedirect("Vendor_login_signup.jsp?msg=Invalid email or number.");return;}
			}else {response.sendRedirect("Vendor_login_signup.jsp?msg=Provide a valid email or phone number.");return;}
		}
		case "update_new_product_orders_status"->{
			String dispatch_status=request.getParameter("product-dispatch-status");
			String []product_details= {
					request.getParameter("product-type"),
					request.getParameter("product-id"),
					request.getParameter("order-id")
			};
			if(request.getParameter("product-dispatch-status").equals("1")) {
				if(!InputValidator.is_empty(product_details) && InputValidator.is_valid_number(dispatch_status) && InputValidator.is_valid_number(product_details[1]) && InputValidator.is_valid_number(product_details[2]))update_new_product_orders_status(product_details,Integer.parseInt(dispatch_status));
				response.sendRedirect("VendorOperationsHandler?request-type=to_dispatch");return;
			}else if(request.getParameter("product-dispatch-status").equals("2")) {
				if(!InputValidator.is_empty(product_details) && InputValidator.is_valid_number(dispatch_status) && InputValidator.is_valid_number(product_details[1]) && InputValidator.is_valid_number(product_details[2])) {
					update_new_product_orders_status(product_details,Integer.parseInt(dispatch_status));
					update_order_status_reject(Integer.parseInt(product_details[2]));
				}
				response.sendRedirect("VendorOperationsHandler?request-type=to_dispatch");return;
			}
		}
		}
		switch(request.getParameter("product-type")==null?"":request.getParameter("product-type")) {
		case "clothing"->{
			boolean update_images=false;
			String []product_info= {
				request.getParameter("clothing-product-id"),
				request.getParameter("clothing-product-name"),
				request.getParameter("clothing-product-brand-name"),
				request.getParameter("clothing-product-original-price"),
				request.getParameter("clothing-product-selling-price"),
				request.getParameter("clothing-product-quantity"),
				request.getParameter("clothing-product-keywords"),
				request.getParameter("clothing-product-description"),
				request.getParameter("clothing-product-refund-replace")
			};
			String []sizes= {
					request.getParameter("clothing-size1"),
					request.getParameter("clothing-size2"),
					request.getParameter("clothing-size3"),
					request.getParameter("clothing-size4"),
					request.getParameter("clothing-size5")	
			};
			Part []images=null;String []image_names=null;
			if(request.getParameter("product-edit-withImage")!=null) {
				images = new Part[]{
						request.getPart("clothing-product-image1"),
						request.getPart("clothing-product-image2"),
						request.getPart("clothing-product-image3"),
						request.getPart("clothing-product-image4"),
						request.getPart("clothing-product-image5")	
				};
				for(Part image:images) {if(image==null || image.getSize()==0) {response.sendRedirect("Vendor_product_edit.jsp?msg=Upload all 5 images.");return;}}
				image_names=new String[] {
					images[0].getSubmittedFileName(),
					images[1].getSubmittedFileName(),
					images[2].getSubmittedFileName(),
					images[3].getSubmittedFileName(),
					images[4].getSubmittedFileName()
				};
				if(!ImageValidator.is_valid_image_size(images) && !ImageValidator.is_valid_image_type(image_names)) {response.sendRedirect("Vendor_product_edit.jsp?msg=Image must be (.jpg, .jpeg, .png, .jfif) type and size must be under 2mb.");return;}
				update_images=true;
			}
			if(!InputValidator.is_empty(product_info) && size_check(sizes)) {
					if(update_clothing_product(product_info,sizes,images,image_names,update_images,request.getParameter("product-type"),request)) {
						response.sendRedirect("Vendor_product_edit.jsp?msg=Clothing product updated.");return;
					}else {response.sendRedirect("Vendor_product_edit.jsp?msg=Faild to update the product.");return;}
			}else {response.sendRedirect("Vendor_product_edit.jsp?msg=Fill the empty fields.");return;}
		}
		case "electronics"->{
			boolean update_images=false;
			String []product_info= {
					request.getParameter("electronics-product-id"),
					request.getParameter("electronics-product-name"),
					request.getParameter("electronics-product-brand-name"),
					request.getParameter("electronics-product-original-price"),
					request.getParameter("electronics-product-selling-price"),
					request.getParameter("electronics-product-quantity"),
					request.getParameter("electronics-product-keywords"),
					request.getParameter("electronics-product-description"),
					request.getParameter("electronics-product-technical-description"),
					request.getParameter("electronics-product-refund-replace")
				};
			Part []images=null;String []image_names=null;
			if(request.getParameter("product-edit-withImage")!=null) {
				images = new Part[]{
						request.getPart("electronics-product-image1"),
						request.getPart("electronics-product-image2"),
						request.getPart("electronics-product-image3"),
						request.getPart("electronics-product-image4"),
						request.getPart("electronics-product-image5")	
				};
				for(Part image:images) {if(image==null || image.getSize()==0) {response.sendRedirect("Vendor_product_edit.jsp?msg=Upload all 5 images.");return;}}
				image_names=new String[] {
					images[0].getSubmittedFileName(),
					images[1].getSubmittedFileName(),
					images[2].getSubmittedFileName(),
					images[3].getSubmittedFileName(),
					images[4].getSubmittedFileName()
				};
				if(!ImageValidator.is_valid_image_size(images) && !ImageValidator.is_valid_image_type(image_names)) {response.sendRedirect("Vendor_product_edit.jsp?msg=Image must be (.jpg, .jpeg, .png, .jfif) type and size must be under 2mb.");return;}
				update_images=true;
			}
			if(!InputValidator.is_empty(product_info)) {
					if(update_electronics_product(product_info,images,image_names,update_images,request.getParameter("product-type"),request)) {
						response.sendRedirect("Vendor_product_edit.jsp?msg=Electronic product updated.");return;
					}else {response.sendRedirect("Vendor_product_edit.jsp?msg=Faild to update the product.");return;}
			}else {response.sendRedirect("Vendor_product_edit.jsp?msg=Fill the empty fields.");return;}
		}
		case "food"->{
			boolean update_images=false;
			String []product_info={
					request.getParameter("food-product-id"),
					request.getParameter("food-product-name"),
					request.getParameter("food-product-brand-name"),
					request.getParameter("food-product-original-price"),
					request.getParameter("food-product-selling-price"),
					request.getParameter("food-product-quantity"),
					request.getParameter("food-product-keywords"),
					request.getParameter("food-product-description"),
					request.getParameter("food-product-shelf-life"),
					request.getParameter("food-product-perishable"),
					request.getParameter("food-product-veg"),
					request.getParameter("food-product-refund-replace")
				};
			String food_meat_type=request.getParameter("food-meat-type");
			if(food_meat_type!=null) {if(!InputValidator.contains_html(food_meat_type)) {response.sendRedirect("Vendor_product_edit.jsp?msg=Some special characters are not allowed.");return;}}
			Part []images=null;String []image_names=null;
			if(request.getParameter("product-edit-withImage")!=null) {
				images = new Part[]{
						request.getPart("food-product-image1"),
						request.getPart("food-product-image2"),
						request.getPart("food-product-image3"),
						request.getPart("food-product-image4"),
						request.getPart("food-product-image5")	
				};
				for(Part image:images) {if(image==null || image.getSize()==0) {response.sendRedirect("Vendor_product_edit.jsp?msg=Upload all 5 images.");return;}}
				image_names=new String[] {
					images[0].getSubmittedFileName(),
					images[1].getSubmittedFileName(),
					images[2].getSubmittedFileName(),
					images[3].getSubmittedFileName(),
					images[4].getSubmittedFileName()
				};
				if(!ImageValidator.is_valid_image_size(images) && !ImageValidator.is_valid_image_type(image_names)) {response.sendRedirect("Vendor_product_edit.jsp?msg=Image must be (.jpg, .jpeg, .png, .jfif) type and size must be under 2mb.");return;}
				update_images=true;
			}
			if(!InputValidator.is_empty(product_info)){
					if(update_food_product(product_info,images,image_names,update_images,request.getParameter("product-type"),request,food_meat_type)){
						response.sendRedirect("Vendor_product_edit.jsp?msg=Food product updated.");return;
					}else {response.sendRedirect("Vendor_product_edit.jsp?msg=Faild to update the product.");return;}
			}else {response.sendRedirect("Vendor_product_edit.jsp?msg=Fill the empty fields.");return;}
		}
		}
	}
	
	private void update_order_status_reject(int order_id) {
		set_connection();
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT o.order_id, o.order_date, o.order_reference_code, o.total_amount, o.payment_id, c.client_fname, c.client_lname, c.client_email FROM orders o INNER JOIN client_personal_details c ON o.client_id=c.client_id WHERE order_id=%d",order_id))){
			if(result.next()) {
				if(!result.getString("payment_id").equals("CASH ON DELIVERY"))new RazorPayService().initiate_refund(result.getDouble("total_amount"),result.getString("payment_id"));
				connect.createStatement().execute(String.format("UPDATE orders SET order_status=%d WHERE order_id=%d",2,order_id));
				connect.commit();
				String [] order_details= {Integer.toString(order_id),String.valueOf(result.getDate("order_date")),result.getString("order_reference_code")};
				new MailHandler(new EmailDetails("client","client_mail_text_order_canceled_by_vendor",result.getString("client_fname")+" "+result.getString("client_lname"),result.getString("client_email"),order_details)).send_mail();;
			}
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}

	final private void update_new_product_orders_status(String []product_details,int dispatch_status) {
		set_connection();
		try(PreparedStatement statement=connect.prepareStatement("UPDATE order_items SET product_status=? WHERE product_type=? AND product_id=? AND order_id=?")){
			statement.setInt(1,dispatch_status);
			statement.setString(2,product_details[0]);
			statement.setInt(3,Integer.parseInt(product_details[1]));
			statement.setInt(4,Integer.parseInt(product_details[2]));
			statement.execute();
			connect.commit();
			if(dispatch_status==1)update_order_status(Integer.parseInt(product_details[2]));
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private void update_order_status(int order_id) {
		set_connection();
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT SUM(CASE WHEN product_status = 1 THEN 1 ELSE 0 END) AS count_ones, SUM(CASE WHEN product_status = 0 THEN 1 ELSE 0 END) AS count_zeros FROM order_items WHERE order_id=%d", order_id))){
			if(result.next() && (result.getInt("count_ones")-result.getInt("count_zeros")==result.getInt("count_ones"))) {
				connect.createStatement().execute(String.format("UPDATE orders SET order_status=1 WHERE order_id=%d",order_id));
				connect.commit();
			}
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private ArrayList<Orders> load_new_product_orders(String vendor_username) {
		set_connection();
		ArrayList<Orders> product_list=new ArrayList<Orders>();
		try(ResultSet result=connect.createStatement().executeQuery("SELECT order_id, product_type, product_id, product_size, product_img1, product_status, product_quantity, product_selling_price FROM order_items WHERE product_status=0")){
			while(result.next()) {
				ResultSet result1=connect.createStatement().executeQuery(String.format("SELECT product_name, product_brand_name FROM %s_products WHERE vendor_username='%s' AND product_id=%d", result.getString("product_type"),vendor_username,result.getInt("product_id")));
				while(result1.next()) {
					Orders order=new Orders();
					order.set_data("order_id",Integer.toString(result.getInt("order_id")));
					order.set_data("product_type",result.getString("product_type"));
					order.set_data("product_id",Integer.toString(result.getInt("product_id")));
					order.set_data("product_size",result.getString("product_size"));
					order.set_data("product_img1",result.getString("product_img1"));
					order.set_data("product_quantity",result.getString("product_quantity"));
					order.set_data("product_selling_price",Double.toString(result.getDouble("product_selling_price")));
					order.set_data("product_status",Integer.toString(result.getInt("product_status")));
					order.set_data("product_name",result1.getString("product_name"));
					order.set_data("product_brand_name",result1.getString("product_brand_name"));
					product_list.add(order);
				}
			}
			close_connection();
			return product_list;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private void update_forgotten_password(String[] new_passwords, int vendor_id) {
		set_connection();
		String query="UPDATE vendor_personal_details SET vendor_password=? WHERE vendor_id=?";
		try(PreparedStatement statement=connect.prepareStatement(query)){
			statement.setString(1,new_passwords[0]);
			statement.setInt(2,vendor_id);
			statement.execute();
			connect.commit();
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private void send_password_change_mail(String []vendor_personal_details,SecurityCodeHandler securitycode) {
		new MailHandler(new EmailDetails("vendor","vendor_mail_text_forget_password",vendor_personal_details[0],vendor_personal_details[2],securitycode.get_security_code())).send_mail();
	}
	
	final private String[] get_vendor_details(String type, String vendor_details) {
		set_connection();
		String query=null;
		if(type.equals("phone")) query=String.format("SELECT vendor_fname, vendor_lname, vendor_id, vendor_email FROM vendor_personal_details WHERE vendor_phone='%s'",vendor_details);
		else if(type.equals("email")) query=String.format("SELECT vendor_fname, vendor_lname, vendor_id, vendor_email FROM vendor_personal_details WHERE vendor_email='%s'",vendor_details);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				String []details_from_db= {result.getString("vendor_fname")+" "+result.getString("vendor_lname"),Integer.toString(result.getInt("vendor_id")), result.getString("vendor_email")};
				close_connection();
				return details_from_db;
			}
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private boolean update_images(int product_id,Part []images,String []image_names,String product_type,HttpServletRequest request) {
		String photo_query=String.format("SELECT product_img1,product_img2,product_img3,product_img4,product_img5 FROM "+product_type+"_products WHERE product_id=%d",product_id);
		switch(product_type) {
		case "clothing"->{
			try {
				File []paths=null;
				ResultSet result=connect.createStatement().executeQuery(photo_query);
				if(result.next()) {
				paths=new File[]{
						new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img1")),
						new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img2")),
						new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img3")),
						new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img4")),
						new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img5"))
				};
				}
				result.close();
				image_names=ImageValidator.get_new_image_name(image_names,(String)request.getSession().getAttribute("Vendor-username"));
				String upload_path="E:/Dynamic Web Project/src/main/webapp/Clothing product images/";
				File upload=new File(upload_path);
				if(!upload.exists()) {upload.mkdir();}
				PreparedStatement statement=connect.prepareStatement("UPDATE clothing_products SET product_img1=?,product_img2=?,product_img3=?,product_img4=?,product_img5=? WHERE product_id=?");
				statement.setString(1,image_names[0]);
				statement.setString(2,image_names[1]);
				statement.setString(3,image_names[2]);
				statement.setString(4,image_names[3]);
				statement.setString(5,image_names[4]);
				statement.setInt(6,product_id);
				statement.execute();
				for(File path:paths) {
					path.delete();
				}
				for(int i=0;i<images.length;i++) {
					upload_path="E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+image_names[i];
					images[i].write(upload_path);
				}
				statement.close();
				return true;
			}
			catch(Exception e) {
				try {connect.rollback();} 
				catch (SQLException e1) {e1.printStackTrace();}
				for(int i=0;i<images.length;i++) {
					new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+image_names[i]).delete();
				}
				e.printStackTrace();
			}
		}
		case "electronics"->{
			try {
				File []paths=null;
				ResultSet result=connect.createStatement().executeQuery(photo_query);
				if(result.next()) {
					paths=new File[] {
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img1")),
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img2")),
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img3")),
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img4")),
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img5"))
					};
				}
				result.close();
				image_names=ImageValidator.get_new_image_name(image_names,(String)request.getSession().getAttribute("Vendor-username"));
				String upload_path="E:/Dynamic Web Project/src/main/webapp/Electronics product images/";
				File upload=new File(upload_path);
				if(!upload.exists()) {upload.mkdir();}
				PreparedStatement statement=connect.prepareStatement("UPDATE electronics_products SET product_img1=?,product_img2=?,product_img3=?,product_img4=?,product_img5=? WHERE product_id=?");
				statement.setString(1,image_names[0]);
				statement.setString(2,image_names[1]);
				statement.setString(3,image_names[2]);
				statement.setString(4,image_names[3]);
				statement.setString(5,image_names[4]);
				statement.setInt(6,product_id);
				statement.execute();
				for(File path:paths) {
					path.delete();
				}
				for(int i=0;i<images.length;i++) {
					upload_path="E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+image_names[i];
					images[i].write(upload_path);
				}
				statement.close();
				return true;
			}
			catch(Exception e) {
				try {connect.rollback();} 
				catch (SQLException e1) {e1.printStackTrace();}
				for(int i=0;i<images.length;i++) {
					new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+image_names[i]).delete();
				}
				e.printStackTrace();
			}
		}
		case "food"->{
			try {
				File []paths=null;
				ResultSet result=connect.createStatement().executeQuery(photo_query);
				if(result.next()) {
					paths=new File[] {
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img1")),
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img2")),
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img3")),
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img4")),
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img5"))
					};
				}
				result.close();
				image_names=ImageValidator.get_new_image_name(image_names,(String)request.getSession().getAttribute("Vendor-username"));
				String upload_path="E:/Dynamic Web Project/src/main/webapp/Food product images/";
				File upload=new File(upload_path);
				if(!upload.exists()) {upload.mkdir();}
				PreparedStatement statement=connect.prepareStatement("UPDATE food_products SET product_img1=?,product_img2=?,product_img3=?,product_img4=?,product_img5=? WHERE product_id=?");
				statement.setString(1,image_names[0]);
				statement.setString(2,image_names[1]);
				statement.setString(3,image_names[2]);
				statement.setString(4,image_names[3]);
				statement.setString(5,image_names[4]);
				statement.setInt(6,product_id);
				statement.execute();
				for(File path:paths) {
					path.delete();
				}
				for(int i=0;i<images.length;i++) {
					upload_path="E:/Dynamic Web Project/src/main/webapp/Food product images/"+image_names[i];
					images[i].write(upload_path);
				}
				statement.close();
				return true;
			}
			catch(Exception e) {
				try {connect.rollback();} 
				catch (SQLException e1) {e1.printStackTrace();}
				for(int i=0;i<images.length;i++) {
					new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+image_names[i]).delete();
				}
				e.printStackTrace();
			}
		}
		}
		return false;
	}
		
	final private void update_product_stock(String []product_info) {
		set_connection();
		switch(Integer.parseInt(product_info[1])) {
		case 0,1->{
			try(PreparedStatement statement=connect.prepareStatement("UPDATE "+product_info[2]+"_products SET product_stock_status=? WHERE product_id=?")){
				statement.setInt(1,Integer.parseInt(product_info[1]));
				statement.setInt(2,Integer.parseInt(product_info[0]));
				statement.execute();
				connect.commit();
				close_connection();
			}
			catch(Exception e) {
				try {connect.rollback();} 
				catch (SQLException e1) {e1.printStackTrace();close_connection();}
				e.printStackTrace();
			}
		}
		case 2->{
			try(PreparedStatement statement=connect.prepareStatement("DELETE FROM "+product_info[2]+"_products WHERE product_id=?")){
				statement.setInt(1,Integer.parseInt(product_info[0]));
				statement.execute();
				connect.commit();
				File []paths=get_path(product_info);
				for(File path:paths) {path.delete();}
				close_connection();
			}
			catch(Exception e) {
				try {connect.rollback();} 
				catch (SQLException e1) {e1.printStackTrace();close_connection();}
				e.printStackTrace();
			}
		}
		}
	}
	
	final private File[] get_path(String []product_info) {
		File []paths=null;
		try {
			switch(product_info[2]) {
			case "clothing"->{
				ResultSet result=connect.createStatement().executeQuery(String.format("SELECT product_img1,product_img2,product_img3,product_img4,product_img5 FROM "+product_info[2]+"_products WHERE product_id=%d",Integer.parseInt(product_info[0])));
				if(result.next()) {
					paths=new File[] {
							new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img1")),
							new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img2")),
							new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img3")),
							new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img4")),
							new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+result.getString("product_img5")),
					};
				}
				result.close();
			}
			case "electronics"->{
				ResultSet result=connect.createStatement().executeQuery(String.format("SELECT product_img1,product_img2,product_img3,product_img4,product_img5 FROM "+product_info[2]+"_products WHERE product_id=%d",Integer.parseInt(product_info[0])));
				if(result.next()) {
					paths=new File[] {
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img1")),
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img2")),
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img3")),
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img4")),
							new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+result.getString("product_img5")),
					};
				}
				result.close();
			}
			case "food"->{
				ResultSet result=connect.createStatement().executeQuery(String.format("SELECT product_img1,product_img2,product_img3,product_img4,product_img5 FROM "+product_info[2]+"_products WHERE product_id=%d",Integer.parseInt(product_info[0])));
				if(result.next()) {
					paths=new File[] {
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img1")),
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img2")),
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img3")),
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img4")),
							new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+result.getString("product_img5")),
					};
				}
				result.close();
			}
			}
		}
		catch(Exception e) {e.printStackTrace();}
		return paths;
	}
	
	final private boolean update_food_product(String []product_info,Part []images,String []image_names,boolean update_images,String product_type,HttpServletRequest request,String food_meat_type) {
		set_connection();
		try(PreparedStatement statement=connect.prepareStatement("UPDATE food_products SET product_name=?,product_brand_name=?,product_original_price=?,product_selling_price=?,product_quantity=?,product_keywords=?,product_description=?,product_shelf_life=?,product_perishable=?,product_vegetarian=?,product_meat_type=?,product_refund_replace_option=? WHERE product_id=?")){
			statement.setString(1,product_info[1]);
			statement.setString(2,product_info[2]);
			statement.setDouble(3,Double.parseDouble(product_info[3]));
			statement.setDouble(4,Double.parseDouble(product_info[4]));
			statement.setInt(5,Integer.parseInt(product_info[5]));
			statement.setString(6,product_info[6]);
			statement.setString(7,product_info[7]);
			statement.setString(8,product_info[8]);
			statement.setBoolean(9,Boolean.parseBoolean(product_info[9]));
			statement.setBoolean(10,Boolean.parseBoolean(product_info[10]));
			statement.setString(11,food_meat_type);
			statement.setString(12,product_info[11]);
			statement.setInt(13,Integer.parseInt(product_info[0]));
			statement.execute();
			if(update_images) {
				if(!update_images(Integer.parseInt(product_info[0]),images,image_names,product_type,request)) {connect.rollback();return false;}
			}
			connect.commit();
			close_connection();
			return true;
		}
		catch(Exception e) {
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
			close_connection();
			e.printStackTrace();
		}
		return false;
	}
	
	final private boolean update_electronics_product(String []product_info,Part []images,String []image_names,boolean update_images,String product_type,HttpServletRequest request) {
		set_connection();
		try (PreparedStatement statement=connect.prepareStatement("UPDATE electronics_products SET product_name=?,product_brand_name=?,product_original_price=?,product_selling_price=?,product_quantity=?,product_keywords=?,product_description=?,product_technical_description=?,product_refund_replace_option=? WHERE product_id=?")){
			statement.setString(1,product_info[1]);
			statement.setString(2,product_info[2]);
			statement.setDouble(3,Double.parseDouble(product_info[3]));
			statement.setDouble(4,Double.parseDouble(product_info[4]));
			statement.setInt(5,Integer.parseInt(product_info[5].trim()));
			statement.setString(6,product_info[6]);
			statement.setString(7,product_info[7]);
			statement.setString(8,product_info[8]);
			statement.setString(9,product_info[9]);
			statement.setInt(10,Integer.parseInt(product_info[0]));
			statement.execute();
			if(update_images) {
				if(!update_images(Integer.parseInt(product_info[0]),images,image_names,product_type,request)) {connect.rollback();return false;}
			}
			connect.commit();
			close_connection();
			return true;
		}
		catch(Exception e) {
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
			close_connection();
			e.printStackTrace();
		}
		return false;
	}
	
	final private boolean update_clothing_product(String []product_info,String []sizes,Part []images,String []image_names,boolean update_images,String product_type,HttpServletRequest request) {
		set_connection();
		try (PreparedStatement statement=connect.prepareStatement("UPDATE clothing_products SET product_name=?,product_brand_name=?,product_original_price=?,product_selling_price=?,product_quantity=?,product_keywords=?,product_description=?,product_size1=?,product_size2=?,product_size3=?,product_size4=?,product_size5=?,product_refund_replace_option=? WHERE product_id=?")){
			statement.setString(1,product_info[1]);
			statement.setString(2,product_info[2]);
			statement.setDouble(3,Double.parseDouble(product_info[3]));
			statement.setDouble(4,Double.parseDouble(product_info[4]));
			statement.setInt(5,Integer.parseInt(product_info[5]));
			statement.setString(6,product_info[6]);
			statement.setString(7,product_info[7]);
			statement.setString(8,sizes[0]);
			statement.setString(9,sizes[1]);
			statement.setString(10,sizes[2]);
			statement.setString(11,sizes[3]);
			statement.setString(12,sizes[4]);
			statement.setString(13,product_info[8]);
			statement.setInt(14,Integer.parseInt(product_info[0]));
			statement.execute();
			if(update_images) {
				if(!update_images(Integer.parseInt(product_info[0]),images,image_names,product_type,request)) {connect.rollback();return false;}
			}
			connect.commit();
			close_connection();
			return true;
		}
		catch(Exception e) {
		try {connect.rollback();} 
		catch (SQLException e1) {e1.printStackTrace();}
		close_connection();
		e.printStackTrace();
		}
		return false;
	}
	
	final private boolean size_check(String []sizes) {
		for(String size:sizes) {
			if(size!=null) {
				return true;
			}
		}
		return false;
	}
	
	final private Object get_product_to_edit(HttpServletRequest request,String []product_info){
		set_connection();
		switch(product_info[1]) {
		case "clothing"->{
			AllProducts.Clothing product=new AllProducts.Clothing();
			try (ResultSet result=connect.createStatement().executeQuery(String.format("SELECT product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_keywords,product_description,product_size1,product_size2,product_size3,product_size4,product_size5,product_refund_replace_option FROM %s_products WHERE product_id=%d",product_info[1],Integer.parseInt(product_info[0])))){
				while(result.next()) {
					product.set_data("product_brand_name",result.getString("product_brand_name"));
					product.set_data("product_id",product_info[0]);
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price", Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_keywords",result.getString("product_keywords"));
					product.set_data("product_description",result.getString("product_description"));
					product.set_data("product_size1",result.getString("product_size1"));
					product.set_data("product_size2",result.getString("product_size2"));
					product.set_data("product_size3",result.getString("product_size3"));
					product.set_data("product_size4",result.getString("product_size4"));
					product.set_data("product_size5",result.getString("product_size5"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
				}
			}
			catch(Exception e) {e.printStackTrace();close_connection();}
			close_connection();
			return product;
		}
		case "electronics"->{
			AllProducts.Electronics product=new AllProducts.Electronics();
			try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_keywords,product_description,product_technical_description,product_refund_replace_option FROM %s_products WHERE product_id=%d",product_info[1],Integer.parseInt(product_info[0])))){
				while(result.next()) {
					product.set_data("product_brand_name",result.getString("product_brand_name"));
					product.set_data("product_id",product_info[0]);
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price", Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_keywords",result.getString("product_keywords"));
					product.set_data("product_description",result.getString("product_description"));
					product.set_data("product_technical_description",result.getString("product_technical_description"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
				}
			}
			catch(Exception e) {e.printStackTrace();close_connection();}
			close_connection();
			return product;
		}
		case "food"->{
			AllProducts.Food product=new AllProducts.Food();
			try (ResultSet result=connect.createStatement().executeQuery(String.format("SELECT product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_keywords,product_description,product_shelf_life,product_perishable,product_vegetarian,product_meat_type,product_refund_replace_option FROM %s_products WHERE product_id=%d",product_info[1],Integer.parseInt(product_info[0])))){
				while(result.next()) {
					product.set_data("product_brand_name",result.getString("product_brand_name"));
					product.set_data("product_id",product_info[0]);
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price", Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_keywords",result.getString("product_keywords"));
					product.set_data("product_description",result.getString("product_description"));
					product.set_data("product_shelf_life",result.getString("product_shelf_life"));
					product.set_data("product_perishable",result.getString("product_perishable"));
					product.set_data("product_vegetarian",result.getString("product_vegetarian"));
					product.set_data("product_meat_type",result.getString("product_meat_type"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
				}
			}
			catch(Exception e) {e.printStackTrace();close_connection();}
			close_connection();
			return product;
		}
		}
		return null;
	}
	
	final private ArrayList<Object> get_searched_products(HttpServletRequest request,String product_type) {
		set_connection();
		ArrayList<Object> data=create_products_list();
		String clothing_query="SELECT product_id,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_img1,product_size1,product_size2,product_size3,product_size4,product_size5,product_refund_replace_option,product_rating,product_status FROM clothing_products WHERE (product_name LIKE ? OR FIND_IN_SET(? , REPLACE(product_keywords, ' ', ''))) AND vendor_username=?";
		String electronics_query="SELECT product_id,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_technical_description,product_img1,product_refund_replace_option,product_rating,product_status FROM electronics_products WHERE (product_name LIKE ? OR FIND_IN_SET(? , REPLACE(product_keywords, ' ', ''))) AND vendor_username=?";
		String food_query="SELECT product_id,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_shelf_life,product_perishable,product_vegetarian,product_meat_type,product_img1,product_refund_replace_option,product_rating,product_status FROM food_products WHERE (product_name LIKE ? OR FIND_IN_SET(? , REPLACE(product_keywords, ' ', ''))) AND vendor_username=?";
		try{
			PreparedStatement statement1=connect.prepareStatement(clothing_query);
			statement1.setString(1, product_type);statement1.setString(2, product_type.trim().replaceAll(" ",""));
			statement1.setString(3,(String)request.getSession().getAttribute("Vendor-username"));
			ResultSet result1=statement1.executeQuery();
			while(result1.next()) {
				AllProducts.Clothing product=new AllProducts.Clothing();
				product.set_data("product_brand_name",result1.getString("product_brand_name"));
				product.set_data("product_id",Integer.toString(result1.getInt("product_id")));
				product.set_data("product_type",result1.getString("product_type"));
				product.set_data("product_name",result1.getString("product_name"));
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
				data.add(product);
			}
			statement1.close();
			result1.close();
			
			PreparedStatement statement2=connect.prepareStatement(electronics_query);
			statement2.setString(1, product_type);statement2.setString(2, product_type.trim().replaceAll(" ",""));
			statement2.setString(3,(String)request.getSession().getAttribute("Vendor-username"));
			ResultSet result2=statement2.executeQuery();
			while(result2.next()) {
				AllProducts.Electronics product=new AllProducts.Electronics();
				product.set_data("product_brand_name",result2.getString("product_brand_name"));
				product.set_data("product_id",Integer.toString(result2.getInt("product_id")));
				product.set_data("product_type",result2.getString("product_type"));
				product.set_data("product_name",result2.getString("product_name"));
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
				data.add(product);
			}
			statement2.close();
			result2.close();
			
			PreparedStatement statement3=connect.prepareStatement(food_query);
			statement3.setString(1, product_type);statement3.setString(2, product_type.trim().replaceAll(" ",""));
			statement3.setString(3,(String)request.getSession().getAttribute("Vendor-username"));
			ResultSet result3=statement3.executeQuery();
			while(result3.next()) {
				AllProducts.Food product=new AllProducts.Food();
				product.set_data("product_brand_name",result3.getString("product_brand_name"));
				product.set_data("product_id",Integer.toString(result3.getInt("product_id")));
				product.set_data("product_type",result3.getString("product_type"));
				product.set_data("product_name",result3.getString("product_name"));
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
	
	final private ArrayList<Object> get_all_products(HttpServletRequest request) {
		set_connection();
		ArrayList<Object> data=create_products_list();
		String clothing_query=String.format("SELECT product_id,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_img1,product_size1,product_size2,product_size3,product_size4,product_size5,product_refund_replace_option,product_rating,product_status FROM clothing_products WHERE vendor_username='%s'",(String)request.getSession().getAttribute("Vendor-username"));
		String electronics_query=String.format("SELECT product_id,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_technical_description,product_img1,product_refund_replace_option,product_rating,product_status FROM electronics_products WHERE vendor_username='%s'",(String)request.getSession().getAttribute("Vendor-username"));
		String food_query=String.format("SELECT product_id,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_shelf_life,product_perishable,product_vegetarian,product_meat_type,product_img1,product_refund_replace_option,product_rating,product_status FROM food_products WHERE vendor_username='%s'",(String)request.getSession().getAttribute("Vendor-username"));
		try {
			ResultSet result1=connect.createStatement().executeQuery(clothing_query);
			while(result1.next()) {
				AllProducts.Clothing product=new AllProducts.Clothing();
				product.set_data("product_brand_name",result1.getString("product_brand_name"));
				product.set_data("product_id",Integer.toString(result1.getInt("product_id")));
				product.set_data("product_type",result1.getString("product_type"));
				product.set_data("product_name",result1.getString("product_name"));
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
				data.add(product);
			}
			result1.close();
			ResultSet result2=connect.createStatement().executeQuery(electronics_query);
			while(result2.next()) {
				AllProducts.Electronics product=new AllProducts.Electronics();
				product.set_data("product_brand_name",result2.getString("product_brand_name"));
				product.set_data("product_id",Integer.toString(result2.getInt("product_id")));
				product.set_data("product_type",result2.getString("product_type"));
				product.set_data("product_name",result2.getString("product_name"));
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
				data.add(product);
			}
			result2.close();
			ResultSet result3=connect.createStatement().executeQuery(food_query);
			while(result3.next()) {
				AllProducts.Food product=new AllProducts.Food();
				product.set_data("product_brand_name",result3.getString("product_brand_name"));
				product.set_data("product_id",Integer.toString(result3.getInt("product_id")));
				product.set_data("product_type",result3.getString("product_type"));
				product.set_data("product_name",result3.getString("product_name"));
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
				data.add(product);
			}
			result3.close();
			close_connection();
			return data;
		}
		catch(Exception e) {e.printStackTrace();close_connection();}
		return null;
	}
	final private void set_connection() {
		try {if(connect==null || connect.isClosed()) {connect=ConnectToDB.create_connection();}} 
		catch (SQLException e) {e.printStackTrace();}
	}
	final private void close_connection() {
		try {if(!connect.isClosed() || connect!=null) connect.close();} 
		catch (SQLException e) {e.printStackTrace();}
	}
	final private static ArrayList<Object> create_products_list() {return new ArrayList<Object>();}
}
