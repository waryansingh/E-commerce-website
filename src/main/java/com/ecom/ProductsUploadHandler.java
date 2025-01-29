package com.ecom;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.http.Part;

@MultipartConfig
@WebServlet(
        name = "ProductsUploadHandler",
        urlPatterns = { "/ProductsUploadHandler"}
)
public class ProductsUploadHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connect=null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch(request.getParameter("type")) {
		//clothing section
		case "clothing"->{
			request.getSession().removeAttribute("type");
			String []clothing_product={
					request.getParameter("clothing-product-name"),
					request.getParameter("clothing-product-brand-name"),
					request.getParameter("clothing-original-price"),
					request.getParameter("clothing-selling-price"),
					request.getParameter("clothing-product-quantity"),
					request.getParameter("clothing-product-keywords"),
					request.getParameter("clothing-product-description"),
					request.getParameter("clothing-refund-replace")
			};
			String []sizes= {
					request.getParameter("clothing-size1"),
					request.getParameter("clothing-size2"),
					request.getParameter("clothing-size3"),
					request.getParameter("clothing-size4"),
					request.getParameter("clothing-size5")	
			};
			Part []images={
					request.getPart("clothing-product-image1"),
					request.getPart("clothing-product-image2"),
					request.getPart("clothing-product-image3"),
					request.getPart("clothing-product-image4"),
					request.getPart("clothing-product-image5")
			};
			for(Part image:images) {if(image==null || image.getSize()==0) {response.sendRedirect("Clothing_page.jsp?msg=Upload all 5 images.");return;}}
			String []image_names={
					images[0].getSubmittedFileName(),
					images[1].getSubmittedFileName(),
					images[2].getSubmittedFileName(),
					images[3].getSubmittedFileName(),
					images[4].getSubmittedFileName()
			};
			if(!InputValidator.is_empty(clothing_product) && size_check(sizes)) {
				if(is_valid_product(clothing_product)) {
					if(ImageValidator.is_valid_image_size(images) && ImageValidator.is_valid_image_type(image_names)) {
						if(register_clothing_product(clothing_product,sizes,images,image_names,request)) {
							response.sendRedirect("Clothing_page.jsp?msg=Clothing product registered.");return;
						}else {response.sendRedirect("Clothing_page.jsp?msg=Faild to register the product.");return;}
					}else {response.sendRedirect("Clothing_page.jsp?msg=Image must be (.jpg, .jpeg, .png, .jfif) type and size must be under 2mb.");return;}
				}else {response.sendRedirect("Clothing_page.jsp?msg=Some special characters are not allowed.");return;}
			}else {response.sendRedirect("Clothing_page.jsp?msg=Fill the empty fields. Any one size option must be chosen.");return;}
		}
		//electronics section
		case "electronics"->{
			request.getSession().removeAttribute("type");
			String []electronic_product= {
					request.getParameter("electronic-product-name"),
					request.getParameter("electronic-product-brand-name"),
					request.getParameter("electronic-original-price"),
					request.getParameter("electronic-selling-price"),
					request.getParameter("electronic-product-quantity"),
					request.getParameter("electronic-product-keywords"),
					request.getParameter("electronic-product-description"),
					request.getParameter("electronic-technical-description"),
					request.getParameter("electronic-refund-replace")
			};
			Part []images= {
					request.getPart("electronic-product-image1"),
					request.getPart("electronic-product-image2"),
					request.getPart("electronic-product-image3"),
					request.getPart("electronic-product-image4"),
					request.getPart("electronic-product-image5")
			};
			for(Part image:images) {if(image==null || image.getSize()==0) {response.sendRedirect("Electronics_page.jsp?msg=Upload all 5 images.");return;}}
			String []image_names={
					images[0].getSubmittedFileName(),
					images[1].getSubmittedFileName(),
					images[2].getSubmittedFileName(),
					images[3].getSubmittedFileName(),
					images[4].getSubmittedFileName()
			};
			if(!InputValidator.is_empty(electronic_product)) {
				if(is_valid_product(electronic_product)) {
					if(ImageValidator.is_valid_image_size(images) && ImageValidator.is_valid_image_type(image_names)) {
						if(register_electronics_product(electronic_product,images,image_names,request)) {
							response.sendRedirect("Electronics_page.jsp?msg=Electronics product registered.");return;
						}else {response.sendRedirect("Electronics_page.jsp?msg=Faild to register the product.");return;}
					}else {response.sendRedirect("Electronics_page.jsp?msg=Image must be (.jpg, .jpeg, .png, .jfif) type and size must be under 2mb.");return;}
				}else {response.sendRedirect("Electronics_page.jsp?msg=Some special characters are not allowed.");return;}
			}else {response.sendRedirect("Electronics_page.jsp?msg=Fill the empty fields.");return;}
		}
		//Food section
		case "food"->{
			String []food_product= {
				request.getParameter("food-product-name"),
				request.getParameter("food-product-brand-name"),
				request.getParameter("food-original-price"),
				request.getParameter("food-selling-price"),
				request.getParameter("food-product-quantity"),
				request.getParameter("food-product-keywords"),
				request.getParameter("food-product-description"),
				request.getParameter("food-shelf-life"),
				request.getParameter("food-product-perishable"),
				request.getParameter("food-product-veg"),
				request.getParameter("food-refund-replace")
			};
			String food_meat_type=request.getParameter("food-meat-type");
			
			if(food_meat_type!=null) {if(!InputValidator.contains_html(food_meat_type)) {response.sendRedirect("Food_page.jsp?msg=Some special characters are not allowed.");return;}}
			Part []images= {
					request.getPart("food-product-image1"),
					request.getPart("food-product-image2"),
					request.getPart("food-product-image3"),
					request.getPart("food-product-image4"),
					request.getPart("food-product-image5")
			};
			for(Part image:images) {if(image==null || image.getSize()==0) {response.sendRedirect("Food_page.jsp?msg=Upload all 5 images.");return;}}
			String []image_names={
					images[0].getSubmittedFileName(),
					images[1].getSubmittedFileName(),
					images[2].getSubmittedFileName(),
					images[3].getSubmittedFileName(),
					images[4].getSubmittedFileName()
			};
			if(!InputValidator.is_empty(food_product)) {
				if(is_valid_product(food_product)) {
					if(ImageValidator.is_valid_image_size(images) && ImageValidator.is_valid_image_type(image_names)) {
						if(register_food_product(food_product,food_meat_type,images,image_names,request)) {
							response.sendRedirect("Food_page.jsp?msg=Food product registered.");return;
						}else {response.sendRedirect("Food_page.jsp?msg=Faild to register the product.");return;}
					}else {response.sendRedirect("Food_page.jsp?msg=Image must be (.jpg, .jpeg, .png, .jfif) type and size must be under 2mb.");return;}
				}else {response.sendRedirect("Food_page.jsp?msg=Some special characters are not allowed.");return;}
			}else {response.sendRedirect("Food_page.jsp?msg=Fill the empty fields.");return;}
		}
		}
	}
	final private boolean is_valid_product(String []products) {
		for(int i=0;i<products.length;i++) {
			if(InputValidator.contains_sql(products[i])){return false;}
		}
		return true;
	}
	
	final private boolean register_food_product(String []food_product,String food_meat_type,Part []images,String []image_names,HttpServletRequest request) {
		set_connection();
		String query="INSERT INTO food_products(vendor_username,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_keywords,product_description,product_shelf_life,product_perishable,product_vegetarian,product_meat_type,product_img1,product_img2,product_img3,product_img4,product_img5,product_refund_replace_option) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		image_names=ImageValidator.get_new_image_name(image_names,(String)request.getSession().getAttribute("Vendor-username"));
		String upload_path="E:/Dynamic Web Project/src/main/webapp/Food product images/";
		File upload=new File(upload_path);
		try {
			if(!upload.exists()) {upload.mkdir();}
			PreparedStatement statement=connect.prepareStatement(query);
			statement.setString(1,(String)request.getSession().getAttribute("Vendor-username"));
			statement.setString(2,food_product[0]);
			statement.setString(3,food_product[1]);
			statement.setDouble(4,Double.parseDouble(food_product[2]));
			statement.setDouble(5,Double.parseDouble(food_product[3]));
			statement.setInt(6,Integer.parseInt(food_product[4]));
			statement.setString(7,food_product[5]);
			statement.setString(8,food_product[6]);
			statement.setString(9,food_product[7]);
			statement.setBoolean(10,Boolean.parseBoolean(food_product[8]));
			statement.setBoolean(11,Boolean.parseBoolean(food_product[9]));
			statement.setString(12,food_meat_type);
			statement.setString(13,image_names[0]);
			statement.setString(14,image_names[1]);
			statement.setString(15,image_names[2]);
			statement.setString(16,image_names[3]);
			statement.setString(17,image_names[4]);
			statement.setString(18,food_product[10]);
			statement.execute();
			connect.commit();
			for(int i=0;i<images.length;i++) {
				upload_path="E:/Dynamic Web Project/src/main/webapp/Food product images/"+image_names[i];
				images[i].write(upload_path);
			}
			statement.close();
			close_connection();
		}
		catch(Exception e) {
			try {connect.rollback();} catch (SQLException e1) {e1.printStackTrace();}
			for(int i=0;i<images.length;i++) {
				new File("E:/Dynamic Web Project/src/main/webapp/Food product images/"+image_names[i]).delete();
			}
			e.printStackTrace();
			close_connection();
			return false;
		}
		return true;
	}
	
	final private boolean register_electronics_product(String []electronic_product,Part []images,String []image_names,HttpServletRequest request) {
		set_connection();
		String query="INSERT INTO electronics_products(vendor_username,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_keywords,product_description,product_technical_description,product_img1,product_img2,product_img3,product_img4,product_img5,product_refund_replace_option) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		image_names=ImageValidator.get_new_image_name(image_names,(String)request.getSession().getAttribute("Vendor-username"));
		String upload_path="E:/Dynamic Web Project/src/main/webapp/Electronics product images/";
		File upload=new File(upload_path);
		try {
			if(!upload.exists()) {upload.mkdir();}
			PreparedStatement statement=connect.prepareStatement(query);
			statement.setString(1,(String)request.getSession().getAttribute("Vendor-username"));
			statement.setString(2,electronic_product[0]);
			statement.setString(3,electronic_product[1]);
			statement.setDouble(4,Double.parseDouble(electronic_product[2]));
			statement.setDouble(5,Double.parseDouble(electronic_product[3]));
			statement.setInt(6,Integer.parseInt(electronic_product[4]));
			statement.setString(7, electronic_product[5]);
			statement.setString(8, electronic_product[6]);
			statement.setString(9, electronic_product[7]);
			statement.setString(10,image_names[0]);
			statement.setString(11,image_names[1]);
			statement.setString(12,image_names[2]);
			statement.setString(13,image_names[3]);
			statement.setString(14,image_names[4]);
			statement.setString(15,electronic_product[8]);
			statement.execute();
			connect.commit();
			for(int i=0;i<images.length;i++) {
				upload_path="E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+image_names[i];
				images[i].write(upload_path);
			}
			statement.close();
			close_connection();
		}
		catch(Exception e) {
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
			for(int i=0;i<images.length;i++) {
				new File("E:/Dynamic Web Project/src/main/webapp/Electronics product images/"+image_names[i]).delete();
			}
			e.printStackTrace();
			close_connection();
			return false;
		}
		return true;
	}
	
	final private boolean register_clothing_product(String []clothing_product,String []sizes,Part []images,String []image_names,HttpServletRequest request) {
		set_connection();
		String query="INSERT INTO clothing_products(vendor_username,product_name,product_brand_name,product_original_price,product_selling_price,product_quantity,product_keywords,product_description,product_img1,product_img2,product_img3,product_img4,product_img5,product_size1,product_size2,product_size3,product_size4,product_size5,product_refund_replace_option) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		image_names=ImageValidator.get_new_image_name(image_names,(String)request.getSession().getAttribute("Vendor-username"));
		String upload_path="E:/Dynamic Web Project/src/main/webapp/Clothing product images/";
		File upload=new File(upload_path);
		try {
			if(!upload.exists()) {upload.mkdir();}
			PreparedStatement statement=connect.prepareStatement(query);
			statement.setString(1,(String)request.getSession().getAttribute("Vendor-username"));
			statement.setString(2,clothing_product[0]);
			statement.setString(3,clothing_product[1]);
			statement.setDouble(4,Double.parseDouble(clothing_product[2]));
			statement.setDouble(5,Double.parseDouble(clothing_product[3]));
			statement.setInt(6,Integer.parseInt(clothing_product[4]));
			statement.setString(7,clothing_product[5]);
			statement.setString(8,clothing_product[6]);
			statement.setString(9,image_names[0]);
			statement.setString(10,image_names[1]);
			statement.setString(11,image_names[2]);
			statement.setString(12,image_names[3]);
			statement.setString(13,image_names[4]);
			statement.setString(14,sizes[0]);
			statement.setString(15,sizes[1]);
			statement.setString(16,sizes[2]);
			statement.setString(17,sizes[3]);
			statement.setString(18,sizes[4]);
			statement.setString(19,clothing_product[7]);
			statement.execute();
			connect.commit();
			for(int i=0;i<images.length;i++) {
				upload_path="E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+image_names[i];
				images[i].write(upload_path);
			}
			statement.close();
			close_connection();
		}
		catch(Exception e) {
			try {connect.rollback();} catch (SQLException e1) {e1.printStackTrace();}
			for(int i=0;i<images.length;i++) {
				new File("E:/Dynamic Web Project/src/main/webapp/Clothing product images/"+image_names[i]).delete();
			}
			e.printStackTrace();
			close_connection();
			return false;
		}
		return true;
	}
	final private boolean size_check(String []sizes) {
		for(String size:sizes) {
			if(size!=null) {
				return true;
			}
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
