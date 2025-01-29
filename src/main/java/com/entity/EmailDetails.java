package com.entity;

import java.util.ArrayList;
import java.util.HashMap;

import com.mailtext.GetMailText;

public final class EmailDetails {
	private String user_type=null,mail_type=null,receiver_name=null,receiver_mail=null,mail_text=null,mail_subject=null,product_type=null,product_name=null,order_id=null,order_date=null,order_reference_code=null,total_amount=null,personal_address_id=null;
	private StringBuilder security_code=null;
	private int status,product_status;
	private ArrayList<ProductToBuy> products=null;
	private HashMap<String,ClientPersonalDetails> personal_address=null;
	private HashMap<String,ClientFavouriteAddress> selected_fav_address=null;
	public EmailDetails(String user_type,String mail_type,String receiver_name,String receiver_mail,StringBuilder code) {
		this.user_type=user_type;
		this.mail_type=mail_type;
		this.receiver_name=receiver_name;
		this.receiver_mail=receiver_mail;
		this.security_code=code;
		set_text();
		replace_contents_of_security_mail();
	}
	public EmailDetails(String user_type,String mail_type,String receiver_name,String receiver_mail,int status) {
		this.user_type=user_type;
		this.mail_type=mail_type;
		this.receiver_name=receiver_name;
		this.receiver_mail=receiver_mail;
		this.status=status;
		set_text();
		replace_contents_of_status_mail();
	}
	public EmailDetails(String user_type,String mail_type,String receiver_name,String receiver_mail,String product_type,String product_name,int product_status,String date) {
		this.user_type=user_type;
		this.mail_type=mail_type;
		this.receiver_name=receiver_name;
		this.receiver_mail=receiver_mail;
		this.product_type=product_type;
		this.product_name=product_name;
		this.product_status=product_status;
		set_text();
		replace_contents_of_product_status_mail();
	}
	public EmailDetails(String user_type,String mail_type,String receiver_name,String receiver_mail,String product_type,String product_name,int product_status) {
		this.user_type=user_type;
		this.mail_type=mail_type;
		this.receiver_name=receiver_name;
		this.receiver_mail=receiver_mail;
		this.product_type=product_type;
		this.product_name=product_name;
		this.product_status=product_status;
		set_text();
		replace_contents_of_product_status_mail();
	}
	public EmailDetails(String user_type,String mail_type,String receiver_name,String receiver_mail,String []order_details) {
		this.user_type=user_type;
		this.mail_type=mail_type;
		this.receiver_name=receiver_name;
		this.receiver_mail=receiver_mail;
		this.order_id=order_details[0];
		this.order_date=order_details[1];
		this.order_reference_code=order_details[2];
		set_text();
		replace_contents_of_order_cancle_mail();
	}

	public EmailDetails(String user_type,String mail_type,String receiver_name,String receiver_mail,ArrayList<ProductToBuy> products,HashMap<String,ClientPersonalDetails> personal_address, HashMap<String,ClientFavouriteAddress> selected_fav_address,String []order_details) {
		this.user_type=user_type;
		this.mail_type=mail_type;
		this.receiver_name=receiver_name;
		this.receiver_mail=receiver_mail;
		this.products=products;
		this.personal_address=personal_address;
		this.selected_fav_address=selected_fav_address;
		this.order_id=order_details[0];
		this.order_date=order_details[1];
		this.order_reference_code=order_details[2];
		this.total_amount=order_details[3];
		this.personal_address_id=order_details[4];
		set_text();
		replace_contents_of_order_placed_mail();
	}
	
	private void replace_contents_of_order_cancle_mail() {
		this.mail_text=this.mail_text.replace("[Name]",this.receiver_name);
		this.mail_text=this.mail_text.replace("{Reference ID}",this.order_reference_code);
		this.mail_text=this.mail_text.replace("{Order ID}",this.order_id);
		this.mail_text=this.mail_text.replace("{Order Date}",this.order_date);
	}
	
	final private void replace_contents_of_order_placed_mail() {
		this.mail_text=this.mail_text.replace("[Name]",this.receiver_name);
		this.mail_text=this.mail_text.replace("{Reference ID}",this.order_reference_code);
		this.mail_text=this.mail_text.replace("{Order ID}",this.order_id);
		this.mail_text=this.mail_text.replace("{Order Date}",this.order_date);
		String temp_text=this.mail_text.substring(0,this.mail_text.lastIndexOf("Products Ordered"));
		for(ProductToBuy product:this.products) {
			temp_text+=String.format("Product: %s \n Quantity: %s \n Price: %s \n",product.get_data("product_name"), product.get_data("product_quantity"), product.get_data("total_price"));
		}
		temp_text+=String.format("\n Subtotal: %s \n Delivery: 20 \n Total Amount: %s \n",Double.parseDouble(this.total_amount)-20,this.total_amount);
		ClientPersonalDetails address=this.personal_address.get(this.personal_address_id);
		temp_text+=String.format("\n Shipping Address \n Name: %s \n Address Type: %s \n Address: %s \n Landmark: %s \n",this.receiver_name,address.get_data("client_address_type").toUpperCase(),
		address.get_data("client_address")+", "+address.get_data("client_city")+", "+address.get_data("client_state")+", "+address.get_data("client_zip_code"),address.get_data("client_landmark"));
		if(this.selected_fav_address!=null) {
			temp_text+="\n Favorite Address \n";
			for(ClientFavouriteAddress fav_address:this.selected_fav_address.values()) {
				temp_text+=String.format("Address Type: %s \n Address: %s \n Landmark: %s \n",fav_address.get_data("client_fav_address_type"),
				fav_address.get_data("client_fav_address")+", "+fav_address.get_data("client_fav_city")+", "+fav_address.get_data("client_fav_state")+", "+fav_address.get_data("client_fav_zip_code"),fav_address.get_data("client_fav_landmark"));
			}
		}
		temp_text+="\n"+"""
				You can track the status of your order anytime by logging into your account.

				If you have any questions, feel free to reach out to us at teamurbangroove@gmail.com.

				Thank you for choosing UrbanGroove.

			Best Regards,
				The Urban Groove Team
		""";
		this.mail_text=temp_text;
	}
	final private void replace_contents_of_product_status_mail() {
		this.mail_text=this.mail_text.replace("[Name]",this.receiver_name);
		this.mail_text=this.mail_text.replace("[Product Name]",this.product_name);
		this.mail_text=this.mail_text.replace("[Category]",this.product_type);
	}
	final private void replace_contents_of_security_mail() {
		this.mail_text=this.mail_text.replace("[Name]",this.receiver_name);
		this.mail_text=this.mail_text.replace("{OTP_CODE}",this.security_code);
	}
	
	final private void replace_contents_of_status_mail() {
		this.mail_text=this.mail_text.replace("[Name]",this.receiver_name);
	}
	
	final private void set_text() {
		if(this.user_type.equals("client")) {
			switch(this.mail_type) {
			case "client_security_mail_text"->{this.mail_text=GetMailText.ClientMailText.get_text("client_security_mail_text");this.mail_subject="Client Signup OTP for Urban Groove.";}
			case "client_mail_text_deletion"->{this.mail_text=GetMailText.ClientMailText.get_text("client_mail_text_deletion");this.mail_subject="Your Account on Urban Groove Has Been Deleted.";}
			case "client_mail_text_order_placed"->{this.mail_text=GetMailText.ClientMailText.get_text("client_mail_text_order_placed");this.mail_subject="Your Order Details.";}
			case "client_mail_text_forget_password"->{this.mail_text=GetMailText.ClientMailText.get_text("client_mail_text_forget_password");this.mail_subject="🔒 Reset Your Password - Your OTP Code";}
			case "client_mail_text_order_canceled_by_vendor"->{this.mail_text=GetMailText.ClientMailText.get_text("client_mail_text_order_canceled_by_vendor");this.mail_subject="Update on Your Order [ORDER ID: "+this.order_id+"]";}
			case "client_mail_text_order_canceled_by_client"->{this.mail_text=GetMailText.ClientMailText.get_text("client_mail_text_order_canceled_by_client");this.mail_subject="Your Order Has Been Canceled [Order ID: "+this.order_id+"]";}
			case "client_mail_text_order_refund_by_client"->{this.mail_text=GetMailText.ClientMailText.get_text("client_mail_text_order_refund_by_client");this.mail_subject="Refund Request Acknowledgement";}
			case "client_mail_text_order_canceled_by_admin"->{this.mail_text=GetMailText.ClientMailText.get_text("client_mail_text_order_canceled_by_admin");this.mail_subject="Your Order Has Been Canceled [Order ID: "+this.order_id+"]";}
			case "client_mail_text_order_return_refund_request_canceled_by_admin"->{this.mail_text=GetMailText.ClientMailText.get_text("client_mail_text_order_return_refund_request_canceled_by_admin");this.mail_subject="Your Return/Refund Request Has Been Canceled";}
			case "client_mail_text_order_return_refund_request_approved_by_admin"->{this.mail_text=GetMailText.ClientMailText.get_text("client_mail_text_order_return_refund_request_approved_by_admin");this.mail_subject="Refund Process Initiated for Your Returned [Order ID: "+this.order_id+"]";}
			}
		}
		else if(this.user_type.equals("admin")) {
			switch(this.mail_type) {
			case "admin_security_mail_text"->{this.mail_text=GetMailText.AdminMailText.get_text("admin_security_mail_text");this.mail_subject="Admin Registration OTP for Urban Groove.";}
			case "status_mail"->{
				switch(this.status) {
				case 0->{this.mail_text=GetMailText.AdminMailText.get_text("admin_status_mail_text_rejection");this.mail_subject="Update on Your Admin Registration Request.";}
				case 1->{this.mail_text=GetMailText.AdminMailText.get_text("admin_status_mail_text_approval");this.mail_subject="Welcome to the Urban Groove Admin Team! 🎉";}
				case 2->{this.mail_text=GetMailText.AdminMailText.get_text("admin_status_mail_text_deletion");this.mail_subject="Your Admin Account on Urban Groove Has Been Deleted.";}
				case 3->{this.mail_text=GetMailText.AdminMailText.get_text("admin_status_mail_text_blacklisted");this.mail_subject="Account Blacklisting Notification.";}
				}
			}
			case "admin_mail_text_forget_password"->{this.mail_text=GetMailText.AdminMailText.get_text("admin_mail_text_forget_password");this.mail_subject="🔒 Reset Your Password - Your OTP Code";}
			}
		}
		else if(this.user_type.equals("vendor")){
			switch(this.mail_type) {
			case "vendor_security_mail_text"->{this.mail_text=GetMailText.VendorMailText.get_text("vendor_security_mail_text");this.mail_subject="Vendor Registration OTP Urban Groove.";}
			case "status_mail"->{
				switch(this.status) {
				case 0->{this.mail_text=GetMailText.VendorMailText.get_text("vendor_status_mail_text_rejection");this.mail_subject="Update on Your Vendor Registration Request.";}
				case 1->{this.mail_text=GetMailText.VendorMailText.get_text("vendor_status_mail_text_approval");this.mail_subject="Welcome to the Urban Groove Vendor Community! 🎉";}
				case 2->{this.mail_text=GetMailText.VendorMailText.get_text("vendor_status_mail_text_deletion");this.mail_subject="Your Vendor Account on Urban Groove Has Been Deleted.";}
				case 3->{this.mail_text=GetMailText.VendorMailText.get_text("vendor_status_mail_text_blacklisted");this.mail_subject="Account Blacklisting Notification.";}
				}
			}
			case "product_status_mail"->{
				switch(this.product_status) {
				case 0->{this.mail_text=GetMailText.VendorMailText.get_text("vendor_product_status_mail_text_rejection");this.mail_subject="Product Approval Notification.";}
				case 1->{this.mail_text=GetMailText.VendorMailText.get_text("vendor_product_status_mail_text_approval");this.mail_subject="Product Rejection Notification.";}
				case 2->{this.mail_text=GetMailText.VendorMailText.get_text("vendor_product_status_mail_text_deletion");this.mail_subject="Product Deletion Notification.";}
				case 3->{this.mail_text=GetMailText.VendorMailText.get_text("vendor_product_status_mail_text_blacklisted");this.mail_subject="Product Blacklisting Notification.";}
				}
			}
			case "vendor_mail_text_forget_password"->{this.mail_text=GetMailText.VendorMailText.get_text("vendor_mail_text_forget_password");this.mail_subject="🔒 Reset Your Password - Your OTP Code";}
			}
		}
	}
	
	final public Object get_data(String which_data) {
		switch(which_data) {
		case "receiver_mail"->{return this.receiver_mail;}
		case "mail_text"->{return this.mail_text;}
		case "mail_subject"->{return this.mail_subject;}
		}
		return null;
	}
}
