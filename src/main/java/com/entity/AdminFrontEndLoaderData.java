package com.entity;
public class AdminFrontEndLoaderData{
	private int registered_admins,admin_requests,registered_vendors,vendor_requests,product_request,registered_products,clothing_product,electronics_product,food_product,blacklisted_vendors,blacklisted_admins,registered_clients,blacklisted_clients,blog_posts,blacklisted_products,delivered_orders,order_canceled,order_refund_request,order_refunded,pre_delivery_orders;
	private String admin_full_name,admin_email,admin_phone,admin_address,admin_profile_picture,admin_govId_picture;
	
	final public void set_count(String which_data,int value) {
		switch(which_data) {
		case "registered_admins"->{this.registered_admins=value;}
		case "admin_requests"->{this.admin_requests=value;}
		case "registered_vendors"->{this.registered_vendors=value;}
		case "vendor_requests"->{this.vendor_requests=value;}
		case "product_request"->{this.product_request=value;}
		case "registered_products"->{this.registered_products=value;}
		case "clothing_product"->{this.clothing_product=value;}
		case "electronics_product"->{this.electronics_product=value;}
		case "food_product"->{this.food_product=value;}
		case "blacklisted_vendors"->{this.blacklisted_vendors=value;}
		case "blacklisted_admins"->{this.blacklisted_admins=value;}
		case "registered_clients"->{this.registered_clients=value;}
		case "blacklisted_clients"->{this.blacklisted_clients=value;}
		case "blog_posts"->{this.blog_posts=value;}
		case "blacklisted_products"->{this.blacklisted_products=value;}
		case "delivered_orders"->{this.delivered_orders=value;}
		case "order_canceled"->{this.order_canceled=value;}
		case "order_refund_request"->{this.order_refund_request=value;}
		case "order_refunded"->{this.order_refunded=value;}
		case "pre_delivery_orders"->{this.pre_delivery_orders=value;}
		}
	}

	final public int get_count(String which_data) {
		switch(which_data) {
		case "registered_admins"->{return this.registered_admins;}
		case "admin_requests"->{return this.admin_requests;}
		case "registered_vendors"->{return this.registered_vendors;}
		case "vendor_requests"->{return this.vendor_requests;}
		case "product_request"->{return this.product_request;}
		case "registered_products"->{return this.registered_products;}
		case "clothing_product"->{return this.clothing_product;}
		case "electronics_product"->{return this.electronics_product;}
		case "food_product"->{return this.food_product;}
		case "blacklisted_vendors"->{return this.blacklisted_vendors;}
		case "blacklisted_admins"->{return this.blacklisted_admins;}
		case "registered_clients"->{return this.registered_clients;}
		case "blacklisted_clients"->{return this.blacklisted_clients;}
		case "blog_posts"->{return this.blog_posts;}
		case "blacklisted_products"->{return this.blacklisted_products;}
		case "delivered_orders"->{return this.delivered_orders;}
		case "order_canceled"->{return this.order_canceled;}
		case "order_refund_request"->{return this.order_refund_request;}
		case "order_refunded"->{return this.order_refunded;}
		case "pre_delivery_orders"->{return this.pre_delivery_orders;}
		}
		return -1;
	}
	
	final public void set_profile(String which_data,String value) {
		switch(which_data) {
		case "admin_full_name"->{this.admin_full_name=value;}
		case "admin_email"->{this.admin_email=value;}
		case "admin_phone"->{this.admin_phone=value;}
		case "admin_address"->{this.admin_address=value;}
		case "admin_profile_picture"->{this.admin_profile_picture=value;}
		case "admin_govId_picture"->{this.admin_govId_picture=value;}
		}
	}
	
	final public String get_profile(String which_data) {
		switch(which_data) {
		case "admin_full_name"->{return this.admin_full_name;}
		case "admin_email"->{return this.admin_email;}
		case "admin_phone"->{return this.admin_phone;}
		case "admin_address"->{return this.admin_address;}
		case "admin_profile_picture"->{return this.admin_profile_picture;}
		case "admin_govId_picture"->{return this.admin_govId_picture;}
		}
		return null;
	}
}