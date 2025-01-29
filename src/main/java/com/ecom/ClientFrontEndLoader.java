package com.ecom;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.entity.AllProducts;
import com.entity.BlogPosts;
import com.entity.Cart;
import com.entity.ClientFavouriteAddress;
import com.entity.ClientPersonalDetails;
import com.entity.Orders;
import com.entity.ProductReview;
import com.entity.Wishlist;

@WebServlet(
        name = "ClientFrontEndLoader",
        urlPatterns = {"/ClientFrontEndLoader","/index"}
)
public class ClientFrontEndLoader extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connect=null;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("Client_username")==null)set_session(request);
		if(request.getSession().getAttribute("Client_id")==null || (int)request.getSession().getAttribute("Client_id")==-1) set_client_id(request,(String)request.getSession().getAttribute("Client_username"));
		
		switch(request.getParameter("products")==null?" ":request.getParameter("products")){
		case "clothing"->{
			request.getSession().setAttribute("product-list",get_product("clothing"));
			request.getSession().setAttribute("product-type","clothing");
			request.getRequestDispatcher("index.jsp").forward(request, response);return;
		}
		case "electronics"->{
			request.getSession().setAttribute("product-list",get_product("electronics"));
			request.getSession().setAttribute("product-type","electronics");
			request.getRequestDispatcher("index.jsp").forward(request, response);return;
		}
		case "food"->{
			request.getSession().setAttribute("product-list",get_product("food"));
			request.getSession().setAttribute("product-type","food");
			request.getRequestDispatcher("index.jsp").forward(request, response);return;
		}
		}
		switch(request.getParameter("operation")==null?"":request.getParameter("operation")) {
		case "load-wishlist"->{
			if((int)request.getSession().getAttribute("Client_id")==-1 ||(String)request.getSession().getAttribute("Client_username")==null) {response.sendRedirect("Client_login_signup.jsp");return;}
			request.getSession().setAttribute("client-wishlist-data",load_wishlist(request,(int)request.getSession().getAttribute("Client_id"),(String)request.getSession().getAttribute("Client_username")));
			request.getRequestDispatcher("Client_wishlist.jsp").forward(request, response);return;
		}
		case "load-cart"->{
			if((int)request.getSession().getAttribute("Client_id")==-1 ||(String)request.getSession().getAttribute("Client_username")==null) {response.sendRedirect("Client_login_signup.jsp");return;}
			request.getSession().setAttribute("client-cart-data",load_cart(request,(int)request.getSession().getAttribute("Client_id"),(String)request.getSession().getAttribute("Client_username")));
			request.getRequestDispatcher("Client_cart.jsp").forward(request, response);return;
		}
		case "blog-posts-all"->{
			if((int)request.getSession().getAttribute("Client_id")==-1 ||(String)request.getSession().getAttribute("Client_username")==null) {response.sendRedirect("Client_login_signup.jsp");return;}
			request.getSession().setAttribute("all-blog-data",load_all_blogs());
			request.getRequestDispatcher("Blog_posts.jsp").forward(request, response);return;
		}
		case "filter-blogs"->{
			if((int)request.getSession().getAttribute("Client_id")==-1 ||(String)request.getSession().getAttribute("Client_username")==null) {response.sendRedirect("Client_login_signup.jsp");return;}
			request.getSession().setAttribute("all-blog-data",filter_blog_posts(request.getParameter("filter-option")));
			request.getRequestDispatcher("Blog_posts.jsp").forward(request, response);return;
		}
		case "search-blogs"->{
			if((int)request.getSession().getAttribute("Client_id")==-1 ||(String)request.getSession().getAttribute("Client_username")==null) {response.sendRedirect("Client_login_signup.jsp");return;}
			String search_type=request.getParameter("search-text");
			if(!InputValidator.is_empty(search_type) && !InputValidator.contains_sql(search_type))request.getSession().setAttribute("all-blog-data",get_searched_blog_posts(request.getParameter("search-type"),search_type));
			request.getRequestDispatcher("Blog_posts.jsp").forward(request, response);return;
		}
		case "personal-blog-posts"->{
			if((int)request.getSession().getAttribute("Client_id")==-1 ||(String)request.getSession().getAttribute("Client_username")==null) {response.sendRedirect("Client_login_signup.jsp");return;}
			request.getSession().setAttribute("client-blog-data",load_client_blogs((int)request.getSession().getAttribute("Client_id")));
			request.getRequestDispatcher("Client_blogs.jsp").forward(request, response);return;
		}
		case "load-post"->{
			if((int)request.getSession().getAttribute("Client_id")==-1 ||(String)request.getSession().getAttribute("Client_username")==null) {response.sendRedirect("Client_login_signup.jsp");return;}
			request.getSession().setAttribute("post-data",load_post(Long.parseLong(request.getParameter("blog-id"))));
			request.getRequestDispatcher("Blog_post_page.jsp").forward(request, response);return;
		}
		case "product-srearch-result"->{
			request.getSession().setAttribute("product-list",get_searched_products((String)request.getParameter("product-type")));
			request.getRequestDispatcher("index.jsp").forward(request, response);return;
		}
		case "load-orders"->{
			request.getSession().setAttribute("client-orders",load_orders((int)request.getSession().getAttribute("Client_id")));
			request.getRequestDispatcher("Client_orders.jsp").forward(request, response);return;
		}
		}
		if(request.getParameter("product-type")!=null) {
			request.getSession().setAttribute("product-data",load_product(request.getParameter("product-id"),request.getParameter("product-type"),request));
			request.getRequestDispatcher("Product_page.jsp").forward(request, response);return;
		}
		
		request.getSession().setAttribute("product-list",get_all_products());
		request.getSession().setAttribute("client-profile-data",get_client_profile_details((String)request.getSession().getAttribute("Client_username")));
		request.getSession().setAttribute("product-type","all");
		request.getRequestDispatcher("index.jsp").forward(request, response);return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch(request.getParameter("sort-type")){
		case "form-1"->{
			String type=request.getParameter("product-type-form-1");
			if(type.equals("all")) {request.getRequestDispatcher("index.jsp?msg=All products cannot be filtered. Choose other options.").forward(request, response);return;}
			request.getSession().setAttribute("product-list",get_sorted_product(request.getParameter("sort"), type));
			request.getSession().setAttribute("form-1",request.getParameter("sort"));
			request.getRequestDispatcher("index.jsp").forward(request, response);return;
		}
		case "form-2"->{
			String type=request.getParameter("product-type-form-2");
			if(type.equals("all")) {request.getRequestDispatcher("index.jsp?msg=All products cannot be filtered. Choose other options.").forward(request, response);return;}
			request.getSession().setAttribute("product-list",get_sorted_products_based_on_price(Double.parseDouble(request.getParameter("price-min")),Double.parseDouble(request.getParameter("price-max")),type));
			request.getRequestDispatcher("index.jsp").forward(request, response);return;
		}
		case "form-3"->{
			String type=request.getParameter("product-type-form-3");
			if(type.equals("all")) {request.getRequestDispatcher("index.jsp?msg=All products cannot be filtered. Choose other options.").forward(request, response);return;}
			request.getSession().setAttribute("product-list",get_sorted_products_based_on_rating(Float.parseFloat(request.getParameter("rating-stars")), type));
			request.getSession().setAttribute("form-3",request.getParameter("rating-stars"));
			request.getRequestDispatcher("index.jsp").forward(request, response);return;
		}
		case "form-4"->{
			request.getSession().setAttribute("product-list",get_sorted_product_based_on_brand_name(request.getParameter("brand-name-form-4")));
			request.getRequestDispatcher("index.jsp").forward(request, response);return;
		}
		}
	}
	
	final private Object[] load_orders(int client_id) {
		set_connection();
		ArrayList<Orders> list=new ArrayList<Orders>();
		ArrayList<Orders> ordered_products=new ArrayList<Orders>(); 
		ArrayList<Orders> ordered_products_review=new ArrayList<Orders>(); 
		String query=String.format("SELECT order_id, payment_id, payment_order_id, client_id, order_date, total_amount, client_personal_address, client_fav_address1, client_fav_address2, client_fav_address3, order_reference_code, order_status FROM orders WHERE client_id=%d ORDER BY order_date DESC",client_id);
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
				ResultSet products=connect.createStatement().executeQuery(String.format("SELECT order_item_id, order_id, product_name, product_type, product_size, product_id, product_img1, product_quantity, product_selling_price, total_item_price FROM order_items WHERE order_id=%d ", result.getInt("order_id")));
				while(products.next()) {
					ResultSet product_refund_replace=connect.createStatement().executeQuery(String.format("SELECT product_refund_replace_option FROM %s_products WHERE product_id=%d", products.getString("product_type"),products.getInt("product_id")));
					Orders product=new Orders();
					product.set_data("order_item_id",Integer.toString(products.getInt("order_item_id")));
					product.set_data("order_id",Integer.toString(products.getInt("order_id")));
					product.set_data("product_name",products.getString("product_name"));
					product.set_data("product_type",products.getString("product_type"));
					product.set_data("product_size",products.getString("product_size"));
					product.set_data("product_id",Integer.toString(products.getInt("product_id")));
					product.set_data("product_img1",products.getString("product_img1"));
					product.set_data("product_quantity",Integer.toString(products.getInt("product_quantity")));
					product.set_data("product_selling_price",Double.toString(products.getDouble("product_selling_price")));
					product.set_data("total_item_price",Double.toString(products.getDouble("total_item_price")));
					if(product_refund_replace.next()) {product.set_data("product_refund_replace_option",product_refund_replace.getString("product_refund_replace_option"));}
					ordered_products.add(product);
					product_refund_replace.close();
				}
				products.close();
				ResultSet products_review=connect.createStatement().executeQuery(String.format("SELECT order_id,product_client_rating, product_review FROM product_rating_review WHERE order_id=%d",result.getInt("order_id")));
				while(products_review.next()) {
					Orders product_rev=new Orders();
					product_rev.set_data("order_id",Integer.toString(products_review.getInt("order_id")));
					product_rev.set_data("product_review",products_review.getString("product_review"));
					product_rev.set_data("product_client_rating",Float.toString(products_review.getFloat("product_client_rating")));
					ordered_products_review.add(product_rev);
				}
				products_review.close();
			}
			close_connection();
			Object[] order_details= {list,ordered_products,ordered_products_review};
			return order_details;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private Object get_sorted_product_based_on_brand_name(String brand_name) {
		set_connection();
		ArrayList<Object> data=new ArrayList<Object>();
		String clothing=String.format("SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM clothing_products WHERE product_stock_status=1 AND product_status=1 AND product_brand_name='%s'",brand_name);
		String electronics=String.format("SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM electronics_products WHERE product_stock_status=1 AND product_status=1 AND product_brand_name='%s'",brand_name);
		String food=String.format("SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM food_products WHERE product_stock_status=1 AND product_status=1 AND product_brand_name='%s'",brand_name);
		try {
			ResultSet result1=connect.createStatement().executeQuery(clothing);
			while(result1.next()) {
				AllProducts.Clothing product=new AllProducts.Clothing();
				product.set_data("product_id",Integer.toString(result1.getInt("product_id")));
				product.set_data("vendor_username",result1.getString("vendor_username"));
				product.set_data("product_type",result1.getString("product_type"));
				product.set_data("product_name",result1.getString("product_name"));
				product.set_data("product_quantity",Integer.toString(result1.getInt("product_quantity")));
				product.set_data("product_original_price",Double.toString(result1.getDouble("product_original_price")));
				product.set_data("product_selling_price",Double.toString(result1.getDouble("product_selling_price")));
				product.set_data("product_quantity",Integer.toString(result1.getInt("product_quantity")));
				product.set_data("product_img1",result1.getString("product_img1"));
				product.set_data("product_refund_replace_option",result1.getString("product_refund_replace_option"));
				product.set_data("product_rating",Float.toString(result1.getFloat("product_rating")));
				data.add(product);
			}
			result1.close();
			ResultSet result2=connect.createStatement().executeQuery(electronics);
			while(result2.next()) {
				AllProducts.Electronics product=new AllProducts.Electronics();
				product.set_data("product_id",Integer.toString(result2.getInt("product_id")));
				product.set_data("vendor_username",result2.getString("vendor_username"));
				product.set_data("product_type",result2.getString("product_type"));
				product.set_data("product_name",result2.getString("product_name"));
				product.set_data("product_quantity",Integer.toString(result2.getInt("product_quantity")));
				product.set_data("product_original_price",Double.toString(result2.getDouble("product_original_price")));
				product.set_data("product_selling_price",Double.toString(result2.getDouble("product_selling_price")));
				product.set_data("product_quantity",Integer.toString(result2.getInt("product_quantity")));
				product.set_data("product_img1",result2.getString("product_img1"));
				product.set_data("product_refund_replace_option",result2.getString("product_refund_replace_option"));
				product.set_data("product_rating",Float.toString(result2.getFloat("product_rating")));
				data.add(product);
			}
			result2.close();
			ResultSet result3=connect.createStatement().executeQuery(food);
			while(result3.next()) {
				AllProducts.Food product=new AllProducts.Food();
				product.set_data("product_id",Integer.toString(result3.getInt("product_id")));
				product.set_data("vendor_username",result3.getString("vendor_username"));
				product.set_data("product_type",result3.getString("product_type"));
				product.set_data("product_name",result3.getString("product_name"));
				product.set_data("product_quantity",Integer.toString(result3.getInt("product_quantity")));
				product.set_data("product_original_price",Double.toString(result3.getDouble("product_original_price")));
				product.set_data("product_selling_price",Double.toString(result3.getDouble("product_selling_price")));
				product.set_data("product_quantity",Integer.toString(result3.getInt("product_quantity")));
				product.set_data("product_img1",result3.getString("product_img1"));
				product.set_data("product_refund_replace_option",result3.getString("product_refund_replace_option"));
				product.set_data("product_rating",Float.toString(result3.getFloat("product_rating")));
				data.add(product);
			}
			result3.close();
			close_connection();
			return data;
		}
		catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}

	final private ArrayList<Object> get_searched_products(String product_type) {
		set_connection();
		ArrayList<Object> data=new ArrayList<Object>();
		String clothing_query="SELECT vendor_username,product_id,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_img1,product_size1,product_size2,product_size3,product_size4,product_size5,product_refund_replace_option,product_rating,product_status,product_status_updated_by FROM clothing_products WHERE product_stock_status=1 AND product_status=1 AND (product_name LIKE ? OR FIND_IN_SET(? , REPLACE(product_keywords, ' ', '')))";
		String electronics_query="SELECT vendor_username,product_id,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_technical_description,product_img1,product_refund_replace_option,product_rating,product_status,product_status_updated_by FROM electronics_products WHERE product_stock_status=1 AND product_status=1 AND (product_name LIKE ? OR FIND_IN_SET(? , REPLACE(product_keywords, ' ', '')))";
		String food_query="SELECT vendor_username,product_id,product_type,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_shelf_life,product_perishable,product_vegetarian,product_meat_type,product_img1,product_refund_replace_option,product_rating,product_status,product_status_updated_by FROM food_products WHERE product_stock_status=1 AND product_status=1 AND (product_name LIKE ? OR FIND_IN_SET(? , REPLACE(product_keywords, ' ', '')))";
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
	
	final private ArrayList<BlogPosts> get_searched_blog_posts(String search_type,String search_text){
		set_connection();
		ArrayList<BlogPosts> posts=new ArrayList<BlogPosts>();
		ResultSet result=null;
		try{
			if(search_type.equals("normal-search")) {
				PreparedStatement statement=connect.prepareStatement("SELECT * FROM blog_posts WHERE blog_title LIKE ? OR blog_content LIKE ? OR client_name LIKE ? ORDER BY upload_date DESC");
				statement.setString(1, "%"+search_text+"%");
				statement.setString(2, "%"+search_text+"%");
				statement.setString(3, "%"+search_text+"%");
				result=statement.executeQuery();
			}
			else if(search_type.equals("product-link-search")){
				PreparedStatement statement=connect.prepareStatement("SELECT * FROM blog_posts WHERE link=?");
				statement.setString(1, search_text);
				result=statement.executeQuery();
			}
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
	
	final private BlogPosts load_post(long blog_id) {
		set_connection();
		BlogPosts post=null;
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT * FROM blog_posts WHERE blog_id=%d",blog_id))){
			if(result.next()) {
				post=new BlogPosts();
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
			}
			close_connection();
			return post;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private ArrayList<BlogPosts> load_client_blogs(int client_id){
		set_connection();
		ArrayList<BlogPosts> posts=new ArrayList<BlogPosts>();
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT * FROM blog_posts WHERE client_id=%d",client_id))){
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
	
	final private Object load_product(String product_id,String product_type,HttpServletRequest request) {
		set_connection();
		String query=null;
		boolean wishlisted=false,carted=false;
		try{
			ResultSet inwish=connect.createStatement().executeQuery(String.format("SELECT product_id, product_type FROM wishlist WHERE (client_id=%d OR client_username='%s') AND product_id=%d AND product_type='%s'",(int)request.getSession().getAttribute("Client_id"),(String)request.getSession().getAttribute("Client_username"),Integer.parseInt(product_id),product_type));
			if(inwish.next() && inwish.getInt("product_id")==Integer.parseInt(product_id) && inwish.getString("product_type").equals(product_type))
				wishlisted=true;
			
			ResultSet incart=connect.createStatement().executeQuery(String.format("SELECT product_id, product_type FROM cart WHERE (client_id=%d OR client_username='%s') AND product_id=%d AND product_type='%s'",(int)request.getSession().getAttribute("Client_id"),(String)request.getSession().getAttribute("Client_username"),Integer.parseInt(product_id),product_type));
			if(incart.next() && incart.getInt("product_id")==Integer.parseInt(product_id) && incart.getString("product_type").equals(product_type))
				carted=true;
			
			inwish.close();
			incart.close();
		}catch(Exception e1) {e1.printStackTrace();}
		switch(product_type){
		case "clothing"->{
			AllProducts.Clothing product=null;
			query=String.format("SELECT product_id,product_type,product_brand_name,product_name,product_quantity,product_original_price,product_selling_price,product_stock_status,product_description,product_img1,product_img2,product_img3,product_img4,product_img5,product_size1,product_size2,product_size3,product_size4,product_size5,product_refund_replace_option,product_rating,product_rating_count FROM %s_products WHERE product_stock_status=1 AND product_id=%d",product_type,Integer.parseInt(product_id));
			try(ResultSet result=connect.createStatement().executeQuery(query)){
				if(result.next()) {
					product=new AllProducts.Clothing();
					product.set_data("product_id",Integer.toString(result.getInt("product_id")));
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_brand_name",result.getString("product_brand_name"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_description",result.getString("product_description"));
					product.set_data("product_img1",result.getString("product_img1"));
					product.set_data("product_img2",result.getString("product_img2"));
					product.set_data("product_img3",result.getString("product_img3"));
					product.set_data("product_img4",result.getString("product_img4"));
					product.set_data("product_img5",result.getString("product_img5"));
					product.set_data("product_size1",result.getString("product_size1"));
					product.set_data("product_size2",result.getString("product_size2"));
					product.set_data("product_size3",result.getString("product_size3"));
					product.set_data("product_size4",result.getString("product_size4"));
					product.set_data("product_size5",result.getString("product_size5"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result.getFloat("product_rating")));
					product.set_data("product_rating_count",Integer.toString(result.getInt("product_rating_count")));
					product.set_data("wishlisted",Boolean.toString(wishlisted));
					product.set_data("carted",Boolean.toString(carted));
				}
				ResultSet product_review=connect.createStatement().executeQuery(String.format("SELECT c.client_fname, c.client_lname, rev.client_id, rev.product_review, rev.product_client_rating FROM client_personal_details c INNER JOIN product_rating_review rev ON c.client_id=rev.client_id WHERE rev.product_type='%s' AND rev.product_id=%d ORDER BY product_rating_review_id DESC", product_type,Integer.parseInt(product_id)));
				HashMap<Integer,ProductReview> comments=new HashMap<Integer,ProductReview>();
				while(product_review.next()) {
					ProductReview review=new ProductReview();
					if(product_review.getString("product_review").equals("") && product_review.getFloat("product_client_rating")==0.0) {continue;}
					review.set_data("client_name",product_review.getString("client_fname")+" "+product_review.getString("client_lname"));
					review.set_data("product_review",product_review.getString("product_review"));
					review.set_data("product_client_rating",Float.toString(product_review.getFloat("product_client_rating")));
					comments.put(product_review.getInt("client_id"),review);
				}
				request.getSession().setAttribute("product_comments",comments);
			}catch(Exception e) {e.printStackTrace();}
			close_connection();
			return product;
		}
		case "electronics"->{
			AllProducts.Electronics product=null;
			query=String.format("SELECT product_id,product_type,product_brand_name,product_name,product_quantity,product_original_price,product_selling_price,product_stock_status,product_description,product_technical_description,product_img1,product_img2,product_img3,product_img4,product_img5,product_refund_replace_option,product_rating,product_rating_count FROM %s_products WHERE product_stock_status=1 AND product_id=%d",product_type,Integer.parseInt(product_id));
			try(ResultSet result=connect.createStatement().executeQuery(query)){
				if(result.next()) {
					product=new AllProducts.Electronics();
					product.set_data("product_id",Integer.toString(result.getInt("product_id")));
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_brand_name",result.getString("product_brand_name"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_description",result.getString("product_description"));
					product.set_data("product_technical_description",result.getString("product_technical_description"));
					product.set_data("product_img1",result.getString("product_img1"));
					product.set_data("product_img2",result.getString("product_img2"));
					product.set_data("product_img3",result.getString("product_img3"));
					product.set_data("product_img4",result.getString("product_img4"));
					product.set_data("product_img5",result.getString("product_img5"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result.getFloat("product_rating")));
					product.set_data("product_rating_count",Integer.toString(result.getInt("product_rating_count")));
					product.set_data("wishlisted",Boolean.toString(wishlisted));
					product.set_data("carted",Boolean.toString(carted));
				}
				ResultSet product_review=connect.createStatement().executeQuery(String.format("SELECT c.client_fname, c.client_lname, rev.client_id, rev.product_review, rev.product_client_rating FROM client_personal_details c INNER JOIN product_rating_review rev ON c.client_id=rev.client_id WHERE rev.product_type='%s' AND rev.product_id=%d ORDER BY product_rating_review_id DESC", product_type,Integer.parseInt(product_id)));
				HashMap<Integer,ProductReview> comments=new HashMap<Integer,ProductReview>();
				while(product_review.next()) {
					ProductReview review=new ProductReview();
					if(product_review.getString("product_review").equals("") && product_review.getFloat("product_client_rating")==0.0) {continue;}
					review.set_data("client_name",product_review.getString("client_fname")+" "+product_review.getString("client_lname"));
					review.set_data("product_review",product_review.getString("product_review"));
					review.set_data("product_client_rating",Float.toString(product_review.getFloat("product_client_rating")));
					comments.put(product_review.getInt("client_id"),review);
				}
				request.getSession().setAttribute("product_comments",comments);
			}catch(Exception e) {e.printStackTrace();}
			close_connection();
			return product;
		}
		case "food"->{
			AllProducts.Food product=null;
			query=String.format("SELECT product_id,product_type,product_brand_name,product_quantity,product_name,product_original_price,product_selling_price,product_stock_status,product_description,product_shelf_life,product_perishable,product_vegetarian,product_meat_type,product_img1,product_img2,product_img3,product_img4,product_img5,product_refund_replace_option,product_rating,product_rating_count FROM %s_products WHERE product_stock_status=1 AND product_id=%d",product_type,Integer.parseInt(product_id));
			try(ResultSet result=connect.createStatement().executeQuery(query)){
				if(result.next()) {
					product=new AllProducts.Food();
					product.set_data("product_id",Integer.toString(result.getInt("product_id")));
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_brand_name",result.getString("product_brand_name"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_description",result.getString("product_description"));
					product.set_data("product_shelf_life",result.getString("product_shelf_life"));
					product.set_data("product_perishable",result.getString("product_perishable"));
					product.set_data("product_vegetarian",result.getString("product_vegetarian"));
					product.set_data("product_meat_type",result.getString("product_meat_type"));
					product.set_data("product_img1",result.getString("product_img1"));
					product.set_data("product_img2",result.getString("product_img2"));
					product.set_data("product_img3",result.getString("product_img3"));
					product.set_data("product_img4",result.getString("product_img4"));
					product.set_data("product_img5",result.getString("product_img5"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result.getFloat("product_rating")));
					product.set_data("product_rating_count",Integer.toString(result.getInt("product_rating_count")));
					product.set_data("wishlisted",Boolean.toString(wishlisted));
					product.set_data("carted",Boolean.toString(carted));
				}
				ResultSet product_review=connect.createStatement().executeQuery(String.format("SELECT c.client_fname, c.client_lname, rev.client_id, rev.product_review, rev.product_client_rating FROM client_personal_details c INNER JOIN product_rating_review rev ON c.client_id=rev.client_id WHERE rev.product_type='%s' AND rev.product_id=%d ORDER BY product_rating_review_id DESC", product_type,Integer.parseInt(product_id)));
				HashMap<Integer,ProductReview> comments=new HashMap<Integer,ProductReview>();
				while(product_review.next()) {
					ProductReview review=new ProductReview();
					if(product_review.getString("product_review").equals("") && product_review.getFloat("product_client_rating")==0.0) {continue;}
					review.set_data("client_name",product_review.getString("client_fname")+" "+product_review.getString("client_lname"));
					review.set_data("product_review",product_review.getString("product_review"));
					review.set_data("product_client_rating",Float.toString(product_review.getFloat("product_client_rating")));
					comments.put(product_review.getInt("client_id"),review);
				}
				request.getSession().setAttribute("product_comments",comments);
			}catch(Exception e) {e.printStackTrace();}
			close_connection();
			return product;
		}
		}
		close_connection();
		return null;
	}
	
	final private ArrayList<Cart> load_cart(HttpServletRequest request,int client_id,String client_username){
		set_connection();
		if(request.getSession().getAttribute("client-cart-data")!=null) {
			@SuppressWarnings("unchecked")
			ArrayList<Cart> items=(ArrayList<Cart>)request.getSession().getAttribute("client-cart-data");
			items.clear();
			items=null;
			request.getSession().removeAttribute("client-cart-data");
		}
		ArrayList<Cart> items=new ArrayList<Cart>();
		String query=String.format("SELECT cart_id,client_id,product_id,product_type,product_name,product_price,product_img1,product_size,product_quantity FROM cart WHERE client_id=%d OR client_username='%s' ORDER BY cart_id DESC",client_id,client_username);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				Cart list=new Cart();
				list.set_data("cart_id",Integer.toString(result.getInt("cart_id")));
				list.set_data("client_id",Integer.toString(result.getInt("client_id")));
				list.set_data("product_id",Integer.toString(result.getInt("product_id")));
				list.set_data("product_type",result.getString("product_type"));
				list.set_data("product_name",result.getString("product_name"));
				list.set_data("product_price",Double.toString(result.getDouble("product_price")));
				list.set_data("product_img1",result.getString("product_img1"));
				list.set_data("product_size",result.getString("product_size"));
				list.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
				items.add(list);
			}
			close_connection();
			return items;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private ArrayList<Wishlist> load_wishlist(HttpServletRequest request,int client_id,String client_username){
		set_connection();
		if(request.getSession().getAttribute("client-wishlist-data")!=null) {
			@SuppressWarnings("unchecked")
			ArrayList<Wishlist> items=(ArrayList<Wishlist>)request.getSession().getAttribute("client-wishlist-data");
			items.clear();
			items=null;
			request.getSession().removeAttribute("client-wishlist-data");
		}
		ArrayList<Wishlist> items=new ArrayList<Wishlist>();
		String query=String.format("SELECT wishlist_id,client_id,product_id,product_type,product_name,product_price,product_img1 FROM wishlist WHERE client_id=%d OR client_username='%s' ORDER BY wishlist_id DESC",client_id,client_username);
		try(ResultSet result=connect.createStatement().executeQuery(query)){
			while(result.next()) {
				Wishlist list=new Wishlist();
				list.set_data("wishlist_id",Integer.toString(result.getInt("wishlist_id")));
				list.set_data("client_id",Integer.toString(result.getInt("client_id")));
				list.set_data("product_id",Integer.toString(result.getInt("product_id")));
				list.set_data("product_type",result.getString("product_type"));
				list.set_data("product_name",result.getString("product_name"));
				list.set_data("product_price",Double.toString(result.getDouble("product_price")));
				list.set_data("product_img1",result.getString("product_img1"));
				items.add(list);
			}
			close_connection();
			return items;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private void set_client_id(HttpServletRequest request,String username) {
		set_connection();
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT client_id FROM client_personal_details WHERE client_username='%s'",username))){
			if(result.next()) {
				request.getSession().setAttribute("Client_id",result.getInt("client_id"));
				close_connection();
				return;
			}
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		request.getSession().setAttribute("Client_id",-1);
	}
	
	final private void set_session(HttpServletRequest request) {
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
	
	final private ArrayList<Object> get_sorted_products_based_on_rating(float rating,String type){
		set_connection();
		ArrayList<Object> data=new ArrayList<Object>();
		String query=String.format("SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM "+type+"_products WHERE product_status=1 AND product_rating >= %f",rating);
		switch(type) {
		case "clothing"->{
			try(ResultSet result=connect.createStatement().executeQuery(query)){
				while(result.next()) {
					AllProducts.Clothing product=new AllProducts.Clothing();
					product.set_data("product_id",Integer.toString(result.getInt("product_id")));
					product.set_data("vendor_username",result.getString("vendor_username"));
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_img1",result.getString("product_img1"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result.getFloat("product_rating")));
					data.add(product);
				}
				close_connection();
				return data;
			}
			catch(Exception e) {e.printStackTrace();}
		}
		case "electronics"->{
			try(ResultSet result=connect.createStatement().executeQuery(query)){
				while(result.next()) {
					AllProducts.Electronics product=new AllProducts.Electronics();
					product.set_data("product_id",Integer.toString(result.getInt("product_id")));
					product.set_data("vendor_username",result.getString("vendor_username"));
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_img1",result.getString("product_img1"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result.getFloat("product_rating")));
					data.add(product);
				}
				close_connection();
				return data;
			}
			catch(Exception e) {e.printStackTrace();}
		}
		case "food"->{
			try(ResultSet result=connect.createStatement().executeQuery(query)){
				while(result.next()) {
					AllProducts.Food product=new AllProducts.Food();
					product.set_data("product_id",Integer.toString(result.getInt("product_id")));
					product.set_data("vendor_username",result.getString("vendor_username"));
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_img1",result.getString("product_img1"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result.getFloat("product_rating")));
					data.add(product);
				}
				close_connection();
				return data;
			}
			catch(Exception e) {e.printStackTrace();}
		}
		}
		close_connection();
		return null;
	}
	
	final private ArrayList<Object> get_sorted_products_based_on_price(Double min,Double max,String type){
		set_connection();
		ArrayList<Object> data=new ArrayList<Object>();
		String query=String.format("SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM "+type+"_products WHERE product_status=1 AND product_selling_price >= %f AND product_selling_price <= %f",min,max);
		switch(type) {
		case "clothing"->{
			try(ResultSet result=connect.createStatement().executeQuery(query)){
				while(result.next()) {
					AllProducts.Clothing product=new AllProducts.Clothing();
					product.set_data("product_id",Integer.toString(result.getInt("product_id")));
					product.set_data("vendor_username",result.getString("vendor_username"));
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_img1",result.getString("product_img1"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result.getFloat("product_rating")));
					data.add(product);
				}
				close_connection();
				return data;
			}
			catch(Exception e) {e.printStackTrace();close_connection();}
		}
		case "electronics"->{
			try(ResultSet result=connect.createStatement().executeQuery(query)){
				while(result.next()) {
					AllProducts.Electronics product=new AllProducts.Electronics();
					product.set_data("product_id",Integer.toString(result.getInt("product_id")));
					product.set_data("vendor_username",result.getString("vendor_username"));
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_img1",result.getString("product_img1"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result.getFloat("product_rating")));
					data.add(product);
				}
				close_connection();
				return data;
			}
			catch(Exception e) {e.printStackTrace();close_connection();}
		}
		case "food"->{
			try(ResultSet result=connect.createStatement().executeQuery(query)){
				while(result.next()) {
					AllProducts.Food product=new AllProducts.Food();
					product.set_data("product_id",Integer.toString(result.getInt("product_id")));
					product.set_data("vendor_username",result.getString("vendor_username"));
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_img1",result.getString("product_img1"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result.getFloat("product_rating")));
					data.add(product);
				}
				close_connection();
				return data;
			}
			catch(Exception e) {e.printStackTrace();}
		}
		}
		close_connection();
		return null;
	}
	
	final private ArrayList<Object> get_sorted_product(String sorting,String type){
		set_connection();
		ArrayList<Object> data=new ArrayList<Object>();
		String query1="",query2="",query3="";
		//Creating query string 
		switch(sorting) {
		case "p-l-to-h"->{
			query1="SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM clothing_products WHERE product_status=1 ORDER BY product_selling_price ASC";
			query2="SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM electronics_products WHERE product_status=1 ORDER BY product_selling_price ASC";
			query3="SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM food_products WHERE product_status=1 ORDER BY product_selling_price ASC";
		}
		case "p-h-to-l"->{
			query1="SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM clothing_products WHERE product_status=1 ORDER BY product_selling_price DESC";
			query2="SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM electronics_products WHERE product_status=1 ORDER BY product_selling_price DESC";
			query3="SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM food_products WHERE product_status=1 ORDER BY product_selling_price DESC";
		}
		case "r-h-to-l"->{
			query1="SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM clothing_products WHERE product_status=1 ORDER BY product_rating DESC";
			query2="SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM electronics_products WHERE product_status=1 ORDER BY product_rating DESC";
			query3="SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM food_products WHERE product_status=1 ORDER BY product_rating DESC";
		}
		}
		//Getting data from the database
		switch(type) {
		case "clothing"->{
			try(ResultSet result1=connect.createStatement().executeQuery(query1)){
				while(result1.next()) {
					AllProducts.Clothing product=new AllProducts.Clothing();
					product.set_data("product_id",Integer.toString(result1.getInt("product_id")));
					product.set_data("vendor_username",result1.getString("vendor_username"));
					product.set_data("product_type",result1.getString("product_type"));
					product.set_data("product_name",result1.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result1.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result1.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result1.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result1.getInt("product_quantity")));
					product.set_data("product_img1",result1.getString("product_img1"));
					product.set_data("product_refund_replace_option",result1.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result1.getFloat("product_rating")));
					data.add(product);
				}
				close_connection();
				return data;
			}
			catch(Exception e) {e.printStackTrace();close_connection();}
		}
		case "electronics"->{
			try(ResultSet result2=connect.createStatement().executeQuery(query2)){
				while(result2.next()) {
					AllProducts.Electronics product=new AllProducts.Electronics();
					product.set_data("product_id",Integer.toString(result2.getInt("product_id")));
					product.set_data("vendor_username",result2.getString("vendor_username"));
					product.set_data("product_type",result2.getString("product_type"));
					product.set_data("product_name",result2.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result2.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result2.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result2.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result2.getInt("product_quantity")));
					product.set_data("product_img1",result2.getString("product_img1"));
					product.set_data("product_refund_replace_option",result2.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result2.getFloat("product_rating")));
					data.add(product);
				}
				close_connection();
				return data;
			}
			catch(Exception e) {e.printStackTrace();close_connection();}
		}
		case "food"->{
			try(ResultSet result3=connect.createStatement().executeQuery(query3)){
				while(result3.next()) {
					AllProducts.Food product=new AllProducts.Food();
					product.set_data("product_id",Integer.toString(result3.getInt("product_id")));
					product.set_data("vendor_username",result3.getString("vendor_username"));
					product.set_data("product_type",result3.getString("product_type"));
					product.set_data("product_name",result3.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result3.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result3.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result3.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result3.getInt("product_quantity")));
					product.set_data("product_img1",result3.getString("product_img1"));
					product.set_data("product_refund_replace_option",result3.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result3.getFloat("product_rating")));
					data.add(product);
				}
				close_connection();
				return data;
			}
			catch(Exception e) {e.printStackTrace();}
		}
		}
		close_connection();
		return null;
	}
	
	final private ArrayList<Object> get_product(String type){
		set_connection();
		ArrayList<Object> data=new ArrayList<Object>();
		String query="SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM "+type+"_products WHERE product_stock_status=1 AND product_status=1";
		switch(type){
		case "clothing"->{
			try(ResultSet result=connect.createStatement().executeQuery(query)){
				while(result.next()) {
					AllProducts.Clothing product=new AllProducts.Clothing();
					product.set_data("product_id",Integer.toString(result.getInt("product_id")));
					product.set_data("vendor_username",result.getString("vendor_username"));
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_img1",result.getString("product_img1"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result.getFloat("product_rating")));
					data.add(product);
				}
				close_connection();
				return data;
			}
			catch(Exception e) {e.printStackTrace();close_connection();}
		}
		case "electronics"->{
			try(ResultSet result=connect.createStatement().executeQuery(query)){
				while(result.next()) {
					AllProducts.Electronics product=new AllProducts.Electronics();
					product.set_data("product_id",Integer.toString(result.getInt("product_id")));
					product.set_data("vendor_username",result.getString("vendor_username"));
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_img1",result.getString("product_img1"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result.getFloat("product_rating")));
					data.add(product);
				}
				close_connection();
				return data;
			}
			catch(Exception e) {e.printStackTrace();close_connection();}
		}
		case "food"->{
			try(ResultSet result=connect.createStatement().executeQuery(query)){
				while(result.next()) {
					AllProducts.Food product=new AllProducts.Food();
					product.set_data("product_id",Integer.toString(result.getInt("product_id")));
					product.set_data("vendor_username",result.getString("vendor_username"));
					product.set_data("product_type",result.getString("product_type"));
					product.set_data("product_name",result.getString("product_name"));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_original_price",Double.toString(result.getDouble("product_original_price")));
					product.set_data("product_selling_price",Double.toString(result.getDouble("product_selling_price")));
					product.set_data("product_quantity",Integer.toString(result.getInt("product_quantity")));
					product.set_data("product_img1",result.getString("product_img1"));
					product.set_data("product_refund_replace_option",result.getString("product_refund_replace_option"));
					product.set_data("product_rating",Float.toString(result.getFloat("product_rating")));
					data.add(product);
				}
				close_connection();
				return data;
			}
			catch(Exception e) {e.printStackTrace();}
		}
		}
		close_connection();
		return null;
	}
	
	final private Object[] get_client_profile_details(String client_username) {
		set_connection();
		String query1=String.format("SELECT client_id,client_fname,client_lname,client_email,client_phone,client_profile_picture FROM client_personal_details WHERE client_username='%s'", client_username);
		String query2=String.format("SELECT client_address_id,client_address_type,client_address,client_city,client_state,client_landmark,client_zip_code FROM client_address WHERE client_username='%s'", client_username);
		String query3=String.format("SELECT client_fav_address_id,client_fav_address_type,client_fav_address,client_fav_city,client_fav_state,client_fav_landmark,client_fav_zip_code,receiver_name,receiver_phone FROM client_favourite_address WHERE client_username='%s'", client_username);
		try(ResultSet result1=connect.createStatement().executeQuery(query1)){
			ResultSet result2=connect.createStatement().executeQuery(query2);
			ResultSet result3=connect.createStatement().executeQuery(query3);
			ClientPersonalDetails details=new ClientPersonalDetails();
			if(result1.next()) {
				details.set_data("client_id",Integer.toString(result1.getInt("client_id")));
				details.set_data("client_fname",result1.getString("client_fname"));
				details.set_data("client_lname",result1.getString("client_lname"));
				details.set_data("client_email",result1.getString("client_email"));
				details.set_data("client_phone",result1.getString("client_phone"));
				details.set_data("client_profile_picture",result1.getString("client_profile_picture"));
				details.set_data("client_username",client_username);
			}
			ArrayList<ClientPersonalDetails> address=new ArrayList<ClientPersonalDetails>();
			while(result2.next()) {
				ClientPersonalDetails address_details=new ClientPersonalDetails();
				address_details.set_data("client_address_id",Integer.toString(result2.getInt("client_address_id")));
				address_details.set_data("client_address_type",result2.getString("client_address_type"));
				address_details.set_data("client_address",result2.getString("client_address"));
				address_details.set_data("client_city",result2.getString("client_city"));
				address_details.set_data("client_state",result2.getString("client_state"));
				address_details.set_data("client_landmark",result2.getString("client_landmark"));
				address_details.set_data("client_zip_code",Integer.toString(result2.getInt("client_zip_code")));
				address.add(address_details);
			}
			ArrayList<ClientFavouriteAddress> fav_address=new ArrayList<ClientFavouriteAddress>();
			while(result3.next()) {
				ClientFavouriteAddress fav_address_details=new ClientFavouriteAddress();
				fav_address_details.set_data("client_fav_address_id",Integer.toString(result3.getInt("client_fav_address_id")));
				fav_address_details.set_data("client_fav_address_type",result3.getString("client_fav_address_type"));
				fav_address_details.set_data("client_fav_address",result3.getString("client_fav_address"));
				fav_address_details.set_data("client_fav_city",result3.getString("client_fav_city"));
				fav_address_details.set_data("client_fav_state",result3.getString("client_fav_state"));
				fav_address_details.set_data("client_fav_landmark",result3.getString("client_fav_landmark"));
				fav_address_details.set_data("client_fav_zip_code",Integer.toString(result3.getInt("client_fav_zip_code")));
				fav_address_details.set_data("receiver_name",result3.getString("receiver_name"));
				fav_address_details.set_data("receiver_phone",result3.getString("receiver_phone"));
				fav_address.add(fav_address_details);
			}
			result1.close();
			result2.close();
			result3.close();
			close_connection();
			Object[] profile_details= {details,address,fav_address};
			return profile_details;
		}catch(Exception e){e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private ArrayList<Object> get_all_products(){
		set_connection();
		ArrayList<Object> data=new ArrayList<Object>();
		String clothing="SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM clothing_products WHERE product_stock_status=1 AND product_status=1 ORDER BY product_id DESC";
		String electronics="SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM electronics_products WHERE product_stock_status=1 AND product_status=1 ORDER BY product_id DESC";
		String food="SELECT product_id,vendor_username,product_type,product_name,product_quantity,product_original_price,product_selling_price,product_quantity,product_img1,product_refund_replace_option,product_rating FROM food_products WHERE product_stock_status=1 AND product_status=1 ORDER BY product_id DESC";
		try {
			ResultSet result1=connect.createStatement().executeQuery(clothing);
			while(result1.next()) {
				AllProducts.Clothing product=new AllProducts.Clothing();
				product.set_data("product_id",Integer.toString(result1.getInt("product_id")));
				product.set_data("vendor_username",result1.getString("vendor_username"));
				product.set_data("product_type",result1.getString("product_type"));
				product.set_data("product_name",result1.getString("product_name"));
				product.set_data("product_quantity",Integer.toString(result1.getInt("product_quantity")));
				product.set_data("product_original_price",Double.toString(result1.getDouble("product_original_price")));
				product.set_data("product_selling_price",Double.toString(result1.getDouble("product_selling_price")));
				product.set_data("product_quantity",Integer.toString(result1.getInt("product_quantity")));
				product.set_data("product_img1",result1.getString("product_img1"));
				product.set_data("product_refund_replace_option",result1.getString("product_refund_replace_option"));
				product.set_data("product_rating",Float.toString(result1.getFloat("product_rating")));
				data.add(product);
			}
			result1.close();
			ResultSet result2=connect.createStatement().executeQuery(electronics);
			while(result2.next()) {
				AllProducts.Electronics product=new AllProducts.Electronics();
				product.set_data("product_id",Integer.toString(result2.getInt("product_id")));
				product.set_data("vendor_username",result2.getString("vendor_username"));
				product.set_data("product_type",result2.getString("product_type"));
				product.set_data("product_name",result2.getString("product_name"));
				product.set_data("product_quantity",Integer.toString(result2.getInt("product_quantity")));
				product.set_data("product_original_price",Double.toString(result2.getDouble("product_original_price")));
				product.set_data("product_selling_price",Double.toString(result2.getDouble("product_selling_price")));
				product.set_data("product_quantity",Integer.toString(result2.getInt("product_quantity")));
				product.set_data("product_img1",result2.getString("product_img1"));
				product.set_data("product_refund_replace_option",result2.getString("product_refund_replace_option"));
				product.set_data("product_rating",Float.toString(result2.getFloat("product_rating")));
				data.add(product);
			}
			result2.close();
			ResultSet result3=connect.createStatement().executeQuery(food);
			while(result3.next()) {
				AllProducts.Food product=new AllProducts.Food();
				product.set_data("product_id",Integer.toString(result3.getInt("product_id")));
				product.set_data("vendor_username",result3.getString("vendor_username"));
				product.set_data("product_type",result3.getString("product_type"));
				product.set_data("product_name",result3.getString("product_name"));
				product.set_data("product_quantity",Integer.toString(result3.getInt("product_quantity")));
				product.set_data("product_original_price",Double.toString(result3.getDouble("product_original_price")));
				product.set_data("product_selling_price",Double.toString(result3.getDouble("product_selling_price")));
				product.set_data("product_quantity",Integer.toString(result3.getInt("product_quantity")));
				product.set_data("product_img1",result3.getString("product_img1"));
				product.set_data("product_refund_replace_option",result3.getString("product_refund_replace_option"));
				product.set_data("product_rating",Float.toString(result3.getFloat("product_rating")));
				data.add(product);
			}
			result3.close();
			close_connection();
			return data;
		}
		catch(Exception e) {e.printStackTrace();}
		close_connection();
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
}
