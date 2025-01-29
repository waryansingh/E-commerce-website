package com.entity;
public class ProductReview {
	private String client_name,product_review,product_client_rating;
	public void set_data(String which_data,String value) {
		switch(which_data) {
		case "client_name"->{this.client_name=value;}
		case "product_review"->{this.product_review=value;}
		case "product_client_rating"->{this.product_client_rating=value;}
		}
	}
	public String get_data(String which_data) {
		switch(which_data) {
		case "client_name"->{return this.client_name;}
		case "product_review"->{return this.product_review;}
		case "product_client_rating"->{return this.product_client_rating;}
		}
		return null;
	}
}
