package com.entity;
public class Orders {
	private String product_client_rating,product_review,product_name,order_item_id,order_date,order_id,payment_id,payment_order_id,client_id,total_amount,client_personal_address,client_fav_address1,client_fav_address2,client_fav_address3,order_reference_code,order_status,product_type,product_size,product_id,product_img1,product_quantity,product_selling_price,total_item_price,product_brand_name,product_status,product_refund_replace_option;
	final public void set_data(String which_data,String value) {
		switch(which_data) {
		case "order_id"->{this.order_id=value;}
		case "payment_id"->{this.payment_id=value;}
		case "payment_order_id"->{this.payment_order_id=value;}
		case "client_id"->{this.client_id=value;}
		case "order_date"->{this.order_date=value;}
		case "total_amount"->{this.total_amount=value;}
		case "client_personal_address"->{this.client_personal_address=value;}
		case "client_fav_address1"->{this.client_fav_address1=value;}
		case "client_fav_address2"->{this.client_fav_address2=value;}
		case "client_fav_address3"->{this.client_fav_address3=value;}
		case "order_reference_code"->{this.order_reference_code=value;}
		case "order_status"->{this.order_status=value;}
		case "order_item_id"->{this.order_item_id=value;}
		case "product_name"->{this.product_name=value;}
		case "product_type"->{this.product_type=value;}
		case "product_size"->{this.product_size=value;}
		case "product_id"->{this.product_id=value;}
		case "product_img1"->{this.product_img1=value;}
		case "product_quantity"->{this.product_quantity=value;}
		case "product_selling_price"->{this.product_selling_price=value;}
		case "total_item_price"->{this.total_item_price=value;}
		case "product_client_rating"->{this.product_client_rating=value;}
		case "product_review"->{this.product_review=value;}
		case "product_brand_name"->{this.product_brand_name=value;}
		case "product_status"->{this.product_status=value;}
		case "product_refund_replace_option"->{this.product_refund_replace_option=value;}
		}
	}
	final public String get_data(String which_data) {
		switch(which_data) {
		case "order_id"->{return this.order_id;}
		case "payment_id"->{return this.payment_id;}
		case "payment_order_id"->{return this.payment_order_id;}
		case "client_id"->{return this.client_id;}
		case "order_date"->{return this.order_date;}
		case "total_amount"->{return this.total_amount;}
		case "client_personal_address"->{return this.client_personal_address;}
		case "client_fav_address1"->{return this.client_fav_address1;}
		case "client_fav_address2"->{return this.client_fav_address2;}
		case "client_fav_address3"->{return this.client_fav_address3;}
		case "order_reference_code"->{return this.order_reference_code;}
		case "order_status"->{return this.order_status;}
		case "order_item_id"->{return this.order_item_id;}
		case "product_name"->{return this.product_name;}
		case "product_type"->{return this.product_type;}
		case "product_size"->{return this.product_size;}
		case "product_id"->{return this.product_id;}
		case "product_img1"->{return this.product_img1;}
		case "product_quantity"->{return this.product_quantity;}
		case "product_selling_price"->{return this.product_selling_price;}
		case "total_item_price"->{return this.total_item_price;}
		case "product_client_rating"->{return this.product_client_rating;}
		case "product_review"->{return this.product_review;}
		case "product_brand_name"->{return this.product_brand_name;}
		case "product_status"->{return this.product_status;}
		case "product_refund_replace_option"->{return this.product_refund_replace_option;}
		}
		return null;
	}
}
