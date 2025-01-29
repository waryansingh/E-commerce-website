package com.entity;

public final class ProductToBuy {
	private String product_name,product_img,product_type,product_size,product_price,product_quantity,product_id,total_price;
	final public void set_data(String which_data,String value) {
		switch(which_data) {
		case "product_id"->{this.product_id=value;}
		case "product_name"->{this.product_name=value;}
		case "product_type"->{this.product_type=value;}
		case "product_img"->{this.product_img=value;}
		case "product_size"->{this.product_size=value;}
		case "product_quantity"->{this.product_quantity=value;}
		case "product_price"->{this.product_price=value;}
		case "total_price"->{this.total_price=value;}
		}
	}
	final public String get_data(String which_data) {
		switch(which_data) {
		case "product_id"->{return this.product_id;}
		case "product_name"->{return this.product_name;}
		case "product_type"->{return this.product_type;}
		case "product_img"->{return this.product_img;}
		case "product_size"->{return this.product_size;}
		case "product_quantity"->{return this.product_quantity;}
		case "product_price"->{return this.product_price;}
		case "total_price"->{return this.total_price;}
		}
		return null;
	}
	final public void calculate_total_price() {
		set_data("total_price",Double.toString(Double.parseDouble(this.product_price)*Integer.parseInt(this.product_quantity)));
	}
}
