package com.entity;

public class Wishlist {
	private String wishlist_id,client_id,client_username,product_id,product_type,product_name,product_price,product_img1;
	public void set_data(String which_data, String value){
		switch(which_data) {
		case "wishlist_id"->{this.wishlist_id=value;}
		case "client_id"->{this.client_id=value;}
		case "client_username"->{this.client_username=value;}
		case "product_id"->{this.product_id=value;}
		case "product_type"->{this.product_type=value;}
		case "product_name"->{this.product_name=value;}
		case "product_price"->{this.product_price=value;}
		case "product_img1"->{this.product_img1=value;}
		}
	}
	public String get_data(String which_data){
		switch(which_data) {
		case "wishlist_id"->{return this.wishlist_id;}
		case "client_id"->{return this.client_id;}
		case "client_username"->{return this.client_username;}
		case "product_id"->{return this.product_id;}
		case "product_type"->{return this.product_type;}
		case "product_name"->{return this.product_name;}
		case "product_price"->{return this.product_price;}
		case "product_img1"->{return this.product_img1;}
		}
		return null;
	}
}
