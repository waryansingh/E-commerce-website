package com.ecom;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.entity.EmailDetails;
import com.entity.VendorDetailsTemp;
@MultipartConfig
@WebServlet(
        name = "VendorSignupHandler",
        urlPatterns = { "/VendorSignupHandler"}
)
public class VendorSignupHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connect=null;
	private SecurityCodeHandler securitycode=null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("type")!=null && request.getParameter("type").equals("otp")) {
			if(request.getParameter("security-code")==null || request.getParameter("security-code").isEmpty()) {response.sendRedirect("Vendor_login_signup.jsp?msg=Enter the OTP.");return;}
			StringBuilder code=new StringBuilder(request.getParameter("security-code"));
			if(securitycode.validate_security_code(code)) {
				VendorDetailsTemp temp_details=(VendorDetailsTemp)request.getSession().getAttribute("vendor_temp_signup_details");
				if(register_new_vendor((String[])temp_details.get_data("vendor_personal_details"),(String[])temp_details.get_data("vendor_personal_address"),(String[])temp_details.get_data("vendor_shop_address"),(InputStream[])temp_details.get_data("images_stream"),(String[])temp_details.get_data("image_names"))) {
					temp_details=null;request.getSession().removeAttribute("vendor_temp_signup_details");securitycode=null;response.sendRedirect("Vendor_login_signup.jsp?msg=Vendor Registered.");return;
				}else {response.sendRedirect("Vendor_login_signup.jsp?msg=Vendor Cannnot be Registered.");return;}
			}else {response.sendRedirect("Vendor_login_signup.jsp?msg=Invalid OTP.");return;}
		}
		String []vendor_personal_details= {
				request.getParameter("vendor-firstname"),
				request.getParameter("vendor-lastname"),
				request.getParameter("vendor-email"),
				request.getParameter("vendor-phone"),
				request.getParameter("vendor-username"),
				request.getParameter("vendor-password"),
				request.getParameter("vendor-gstn")
		};
		String []vendor_personal_address= {
				request.getParameter("vendor-address"),
				request.getParameter("vendor-city"),
				request.getParameter("vendor-state"),
				request.getParameter("vendor-zip")	
		};
		String []vendor_shop_address= {
				request.getParameter("vendor-shop-name"),
				request.getParameter("shop-address"),
				request.getParameter("shop-city"),
				request.getParameter("shop-state"),
				request.getParameter("shop-zip")	
		};
		Part []images= {
				request.getPart("vendor-trade-license"),
				request.getPart("vendor-gov-id")
		};
		if(images[0]==null || images[1]==null) {response.sendRedirect("Vendor_login_signup.jsp?msg=Upload Images."); return;}
		String []image_names= {
				images[0].getSubmittedFileName(),
				images[1].getSubmittedFileName()
		};
		if(!InputValidator.is_empty(vendor_personal_details) && !InputValidator.is_empty(vendor_personal_address) && !InputValidator.is_empty(vendor_shop_address)) {
			if(InputValidator.is_valid_address_details(vendor_personal_address) && InputValidator.is_valid_address_details(vendor_shop_address)) {
				if(request.getParameter("vendor-confirmpassword").equals(vendor_personal_details[5])) {
					if(InputValidator.is_valid_text(vendor_personal_details[0]) && InputValidator.is_valid_text(vendor_personal_details[1])) {
						if(InputValidator.is_valid_email(vendor_personal_details[2]) && InputValidator.is_valid_phonenumber(vendor_personal_details[3]) && !InputValidator.contains_sql(vendor_personal_details[4]) && !InputValidator.contains_sql(vendor_personal_details[5]) && InputValidator.is_valid_alphanumeric(vendor_personal_details[6])) {
							if(ImageValidator.is_valid_image_type(image_names) && ImageValidator.is_valid_image_size(images)) {
								if(is_new_vendor(vendor_personal_details)) {
									InputStream []images_stream={
											images[0].getInputStream(),
											images[1].getInputStream()
									};
									request.getSession().setAttribute("vendor_temp_signup_details",new VendorDetailsTemp(vendor_personal_details,vendor_personal_address,vendor_shop_address,images_stream,image_names));
									this.securitycode=new SecurityCodeHandler();
									send_security_code_mail(vendor_personal_details,this.securitycode);
									request.getSession().setAttribute("Enter_otp",true);
									response.sendRedirect("Vendor_login_signup.jsp?msg=Enter Security Code.");return;
								}else {response.sendRedirect("Vendor_login_signup.jsp?msg=Vendor is already registered with this username or email or phone number.");return;}
							}else {response.sendRedirect("Vendor_login_signup.jsp?msg=Image must be (.jpg, .jpeg, .png, .jfif) type and size must be under 2mb.");return;}
						}else {response.sendRedirect("Vendor_login_signup.jsp?msg=Re-check Email or PhoneNumber or Username. Username and Password must be alphanumeric. GSTN is and 15-digit alphanumeric number.");return;}
					}else {response.sendRedirect("Vendor_login_signup.jsp?msg=Invalid Names.");return;}
				}else {response.sendRedirect("Vendor_login_signup.jsp?msg=Passwords are not matching.");return;}
			}else {response.sendRedirect("Vendor_login_signup.jsp?msg=Invalid Address Details."); return;}
		}else {response.sendRedirect("Vendor_login_signup.jsp?msg=Fill the empty fields."); return;}
	}

	final private void send_security_code_mail(String []vendor_personal_details,SecurityCodeHandler securitycode) {
		new MailHandler(new EmailDetails("vendor","vendor_security_mail_text",vendor_personal_details[0]+" "+vendor_personal_details[1],vendor_personal_details[2],securitycode.get_security_code())).send_mail();
	}
	
	final private boolean is_new_vendor(String []vendor_personal_details) {
		set_connection();
		String query=String.format("SELECT vendor_email, vendor_phone, vendor_username FROM vendor_personal_details WHERE vendor_username='%s'",vendor_personal_details[4]);
		try {
			ResultSet result=connect.createStatement().executeQuery(query);
			if(result.next()) {
				if(result.getString("vendor_email").equals(vendor_personal_details[2]) || result.getString("vendor_phone").equals(vendor_personal_details[3]) || result.getString("vendor_username").equals(vendor_personal_details[4])) {
					result.close();
					close_connection();
					return false;
				}
			}
		}
		catch(Exception e) {e.printStackTrace();}
		close_connection();
		return true;
	}
	
	
	final private boolean register_new_vendor(String []vendor_personal_details,String []vendor_personal_address,String []vendor_shop_address,InputStream []images,String []image_names) {
		set_connection();
		String query1="INSERT INTO vendor_personal_details (vendor_fname, vendor_lname, vendor_email, vendor_phone, vendor_username, vendor_password, vendor_gst_number,vendor_trade_license, vendor_govId_picture) VALUES (?,?,?,?,?,?,?,?,?)";
		String query2="INSERT INTO vendor_personal_address (vendor_username, vendor_address, vendor_city, vendor_state, vendor_zip_code) VALUES (?,?,?,?,?)";
		String query3="INSERT INTO vendor_shop_address (vendor_username, vendor_shop_name_number, vendor_shop_address, vendor_shop_city, vendor_shop_state, vendor_shop_zip_code) VALUES (?,?,?,?,?,?)";
		image_names=ImageValidator.get_new_image_name(image_names, vendor_personal_details[4]);
		String upload_path1="E:/Dynamic Web Project/src/main/webapp/Vendor Trade License/";
		String upload_path2="E:/Dynamic Web Project/src/main/webapp/Vendor GovID/";
		File upload1=new File(upload_path1);
		File upload2=new File(upload_path2);
		try {
			if(!upload1.exists()) {upload1.mkdir();}
			if(!upload2.exists()) {upload2.mkdir();}
			
			upload_path1="E:/Dynamic Web Project/src/main/webapp/Vendor Trade License/"+image_names[0];
			upload_path2="E:/Dynamic Web Project/src/main/webapp/Vendor GovID/"+image_names[1];
			
			PreparedStatement statement1=connect.prepareStatement(query1);
			PreparedStatement statement2=connect.prepareStatement(query2);
			PreparedStatement statement3=connect.prepareStatement(query3);
			
			for(int i=0;i<vendor_personal_details.length;i++) {
				statement1.setString((i+1), vendor_personal_details[i]);
			}
			statement1.setString(8, image_names[0]);
			statement1.setString(9, image_names[1]);
			
			statement2.setString(1, vendor_personal_details[4]);
			statement3.setString(1, vendor_personal_details[4]);
			for(int i=0;i<vendor_personal_address.length;i++) {
				if(i==3) {
					statement2.setInt((i+2), Integer.parseInt(vendor_personal_address[i]));
					break;
				}
				statement2.setString((i+2), vendor_personal_address[i]);
			}
			statement3.setString(2, vendor_shop_address[0]);
			statement3.setString(3, vendor_shop_address[1]);
			statement3.setString(4, vendor_shop_address[2]);
			statement3.setString(5, vendor_shop_address[3]);
			statement3.setInt(6, Integer.parseInt(vendor_shop_address[4]));
			
			statement1.execute();
			statement2.execute();
			statement3.execute();
			
			byte[] buffer = new byte[1024];
			FileOutputStream outputStream1 = new FileOutputStream(new File(upload_path1));
            int bytesRead1;
            while ((bytesRead1 = images[0].read(buffer)) != -1) {
                outputStream1.write(buffer, 0, bytesRead1);
            }
            
            FileOutputStream outputStream2 = new FileOutputStream(new File(upload_path2));
			int bytesRead2;
            while ((bytesRead2 = images[1].read(buffer)) != -1) {
                outputStream2.write(buffer, 0, bytesRead2);
            }
			buffer=null;
			connect.commit();
			statement1.close();
			statement2.close();
			statement3.close();
			outputStream1.close();
			outputStream2.close();
			connect.close();
			close_connection();
			return true;
		}
		catch(Exception e) {
			new File(upload_path1).delete();
			new File(upload_path2).delete();
			try {connect.rollback();} catch (SQLException e1) {e1.printStackTrace();}
			e.printStackTrace();
		}
		close_connection();
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