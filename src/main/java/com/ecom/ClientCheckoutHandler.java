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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import com.entity.ClientFavouriteAddress;
import com.entity.ClientPersonalDetails;
import com.entity.EmailDetails;
import com.entity.ProductToBuy;

@WebServlet(
        name = "ClientCheckoutHandler",
        urlPatterns = {"/ClientCheckoutHandler"}
)
public class ClientCheckoutHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connect=null;
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	switch(request.getParameter("operation")!=null?request.getParameter("operation"):"") {
	case "create-order-payment"->{
		if(request.getSession().getAttribute("selected_address")==null) {request.getRequestDispatcher("Client_checkout_page.jsp?msg=Please choose a delivery address.").forward(request, response);return;}
		if(request.getParameter("payment-method").equals("cash-on-delivery")) {
				request.getSession().setAttribute("payment_option","cod");
				request.getRequestDispatcher("Client_checkout_page.jsp").forward(request, response);return;
		}
		else{
			double total_amount=20.0;
			ArrayList<ProductToBuy> products=(ArrayList<ProductToBuy>)request.getSession().getAttribute("checkout-products");
			for(ProductToBuy product:products) {total_amount+=Double.parseDouble(product.get_data("total_price"));}
			try {
				RazorPayService razorpayService = new RazorPayService();
		        String orderId = razorpayService.create_order(total_amount);
		        request.getSession().setAttribute("client_personal_details",load_client_personal_details((int)request.getSession().getAttribute("Client_id")));
		        request.getSession().setAttribute("orderId", orderId);
		        request.getSession().setAttribute("payment_option","pay-now");
		        request.getRequestDispatcher("Client_checkout_page.jsp").forward(request, response);return;
			} catch (Exception e) {e.printStackTrace();}
		}
	}
	case "process-order"->{
		if(process_order(request)) {
			send_order_placed_mail((int)request.getSession().getAttribute("Client_id"),(ArrayList<ProductToBuy>)request.getSession().getAttribute("checkout-products"),(HashMap<String,ClientPersonalDetails>)request.getSession().getAttribute("selected_address"),(HashMap<String,ClientFavouriteAddress>)request.getSession().getAttribute("fav-address-selected"),(String)request.getSession().getAttribute("order_reference_code"));
			response.sendRedirect("ClientFrontEndLoader?operation=load-orders");return;
		}else {request.getRequestDispatcher("Client_checkout_page.jsp?msg=Order faild to proceed.").forward(request, response);return;}
	}
	}
	}
	
	final private void send_order_placed_mail(int client_id, ArrayList<ProductToBuy> products, HashMap<String,ClientPersonalDetails> personal_address, HashMap<String,ClientFavouriteAddress> selected_fav_address, String order_reference_code) {
		set_connection();
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT c.client_fname, c.client_lname, c.client_email, o.order_id, ca.client_address_id, o.order_date, o.order_reference_code, o.total_amount FROM client_personal_details c JOIN client_address ca ON c.client_username=ca.client_username JOIN orders o ON c.client_id=o.client_id WHERE c.client_id=%d AND o.order_reference_code='%s'",client_id,order_reference_code))){
			if(result.next()) {
				String []order_details= {Integer.toString(result.getInt("order_id")),String.valueOf(result.getDate("order_date")),result.getString("order_reference_code"),Double.toString(result.getDouble("total_amount")),Integer.toString(result.getInt("client_address_id"))};
				new MailHandler(new EmailDetails("client","client_mail_text_order_placed",result.getString("client_fname")+""+result.getString("client_lname"),result.getString("client_email"),products,personal_address,selected_fav_address,order_details)).send_mail();
			}
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
	}

	final private boolean process_order_items(HttpServletRequest request,String order_reference_code) {
		set_connection();
		String []payment_details=(String[])request.getSession().getAttribute("payment_details");
		double total_amount=20.0;
		@SuppressWarnings("unchecked")
		ArrayList<ProductToBuy> products=(ArrayList<ProductToBuy>)request.getSession().getAttribute("checkout-products");
		String query=String.format("SELECT order_id FROM orders WHERE order_reference_code='%s'",order_reference_code);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				PreparedStatement statement1=connect.prepareStatement("INSERT INTO order_items (order_id, client_id, product_name, product_type, product_size, product_id, product_img1, product_quantity, product_selling_price, total_item_price) VALUES (?,?,?,?,?,?,?,?,?,?)");
				PreparedStatement statement2=connect.prepareStatement("INSERT INTO product_rating_review (order_id,client_id,product_id,product_type) VALUES (?,?,?,?)");
				for(ProductToBuy product:products) {
					ResultSet listed_product=connect.createStatement().executeQuery(String.format("SELECT product_quantity FROM "+product.get_data("product_type")+"_products WHERE product_id=%d",Integer.parseInt(product.get_data("product_id"))));
					statement1.setInt(1,result.getInt("order_id"));
					statement2.setInt(1,result.getInt("order_id"));
					statement1.setInt(2,(int)request.getSession().getAttribute("Client_id"));
					statement2.setInt(2,(int)request.getSession().getAttribute("Client_id"));
					statement1.setString(3,product.get_data("product_name"));
					statement1.setString(4,product.get_data("product_type"));
					statement1.setString(5,product.get_data("product_size"));
					statement1.setInt(6,Integer.parseInt(product.get_data("product_id")));
					statement2.setInt(3,Integer.parseInt(product.get_data("product_id")));
					statement2.setString(4,product.get_data("product_type"));
					statement1.setString(7,product.get_data("product_img"));
					statement1.setString(8,product.get_data("product_quantity"));
					statement1.setString(9,product.get_data("product_price"));
					statement1.setString(10,product.get_data("total_price"));
					total_amount+=Double.parseDouble(product.get_data("total_price"));
					statement1.addBatch();statement2.addBatch();
					if(listed_product.next())connect.createStatement().execute(String.format("UPDATE "+product.get_data("product_type")+"_products SET product_quantity = product_quantity - %d WHERE product_id=%d",Integer.parseInt(product.get_data("product_quantity"),10),Integer.parseInt(product.get_data("product_id"),10)));
					listed_product.close();
				}
				statement1.executeBatch();
				statement2.executeBatch();
				statement1.close();statement2.close();
			}
			connect.commit();
			return true;
		}catch(Exception e) {e.printStackTrace();
		try {connect.rollback();if(payment_details[0]!=null)new RazorPayService().initiate_refund(total_amount, payment_details[0]);} 
		catch (Exception e1) {e1.printStackTrace();}
		}
		close_connection();
		return false;
	}

	final private boolean process_order(HttpServletRequest request) {
		set_connection();
		LocalDateTime datetime=LocalDateTime.now();
		String query="INSERT INTO orders (payment_id, payment_order_id, client_id, total_amount, client_personal_address, client_fav_address1, client_fav_address2, client_fav_address3, order_reference_code) VALUES (?,?,?,?,?,?,?,?,?)";
		String[] payment_details= {
			request.getParameter("payment_id")!=null?request.getParameter("payment_id"):null,
			request.getParameter("order_id")!=null?request.getParameter("order_id"):null
		};
		request.getSession().setAttribute("payment_details", payment_details);
		double total_amount=20.0;
		@SuppressWarnings("unchecked")
		ArrayList<ProductToBuy> products=(ArrayList<ProductToBuy>)request.getSession().getAttribute("checkout-products");
		for(ProductToBuy product:products) {total_amount+=Double.parseDouble(product.get_data("total_price"));}
		
		@SuppressWarnings("unchecked")
	    HashMap<String,ClientPersonalDetails> selected_address=(HashMap<String,ClientPersonalDetails>)request.getSession().getAttribute("selected_address");
		String client_personal_address="";
		for(ClientPersonalDetails address:selected_address.values()) {
			client_personal_address+=address.get_data("client_address_type").toUpperCase()+", ";
			client_personal_address+=address.get_data("client_address")+", ";
			client_personal_address+=address.get_data("client_city")+", ";
			client_personal_address+=address.get_data("client_state")+", ";
			client_personal_address+=address.get_data("client_landmark")+", ";
			client_personal_address+=address.get_data("client_zip_code");
		}
		
		@SuppressWarnings("unchecked")
		HashMap<String,ClientFavouriteAddress> selected_fav_address=(HashMap<String,ClientFavouriteAddress>)request.getSession().getAttribute("fav-address-selected");
		String []client_fav_addresses=new String[3];
		if(selected_fav_address!=null && !selected_fav_address.isEmpty() && selected_fav_address.size()!=0) {
			Arrays.fill(client_fav_addresses, "N/A");int i=0;
			for(ClientFavouriteAddress fav_address:selected_fav_address.values()) {
				client_fav_addresses[i]=fav_address.get_data("client_fav_address_type").toUpperCase()+", ";
				client_fav_addresses[i]+=fav_address.get_data("client_fav_address")+", ";
				client_fav_addresses[i]+=fav_address.get_data("client_fav_city")+", ";
				client_fav_addresses[i]+=fav_address.get_data("client_fav_state")+", ";
				client_fav_addresses[i]+=fav_address.get_data("client_fav_landmark")+", ";
				client_fav_addresses[i]+=fav_address.get_data("client_fav_zip_code");i++;
			}
		}
		try(PreparedStatement statement=connect.prepareStatement(query)){
			statement.setString(1,payment_details[0]==null?"CASH ON DELIVERY":payment_details[0]);
			statement.setString(2,payment_details[1]==null?"CASH ON DELIVERY":payment_details[1]);
			statement.setInt(3,(int)request.getSession().getAttribute("Client_id"));
			statement.setDouble(4, total_amount);
			statement.setString(5,client_personal_address);
			statement.setString(6,client_fav_addresses[0]);
			statement.setString(7,client_fav_addresses[1]);
			statement.setString(8,client_fav_addresses[2]);
			String order_reference_code=String.valueOf("ORD-"+UUID.randomUUID()+"-D-"+datetime.getYear()+datetime.getMonthValue()+datetime.getDayOfMonth()+datetime.getDayOfWeek()+"-T-"+System.currentTimeMillis());
			statement.setString(9,order_reference_code);
			statement.execute();
			request.getSession().setAttribute("order_reference_code",order_reference_code);
			return process_order_items(request,order_reference_code);
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();if(payment_details[0]!=null)new RazorPayService().initiate_refund(total_amount, payment_details[0]);} 
			catch (Exception e1) {e1.printStackTrace();}
		}
		close_connection();
		return false;
	}
	
	final private ClientPersonalDetails load_client_personal_details(int client_id) {
		set_connection();
		String query=String.format("SELECT client_username,client_fname,client_lname,client_email,client_phone FROM client_personal_details WHERE client_id=%d", client_id);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			if(result.next()) {
				ClientPersonalDetails details=new ClientPersonalDetails();
				details.set_data("client_id",Integer.toString(client_id));
				details.set_data("client_fname",result.getString("client_fname"));
				details.set_data("client_lname",result.getString("client_lname"));
				details.set_data("client_email",result.getString("client_email"));
				details.set_data("client_phone",result.getString("client_phone"));
				details.set_data("client_username",result.getString("client_username"));
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
