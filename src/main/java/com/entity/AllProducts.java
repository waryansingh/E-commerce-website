package com.entity;


public final class AllProducts {
	public final static class Clothing{
		private String product_brand_name,product_id,product_type,product_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_img1,product_img2,product_img3,product_img4,product_img5,product_size1,product_size2,product_size3,product_size4,product_size5,product_refund_replace_option,product_rating,product_rating_count,product_status,product_status_updated_by,wishlisted,carted;
		final public void set_data(String which_data,String value) {
			switch(which_data) {
			case "product_brand_name"->{this.product_brand_name=value;}
			case "product_id"->{this.product_id=value;}
			case "product_type"->{this.product_type=value;}
			case "product_name"->{this.product_name=value;}
			case "product_original_price"->{this.product_original_price=value;}
			case "product_selling_price"->{this.product_selling_price=value;}
			case "product_quantity"->{this.product_quantity=value;}
			case "product_stock_status"->{this.product_stock_status=value;}
			case "product_keywords"->{this.product_keywords=value;}
			case "product_description"->{this.product_description=value;}
			case "product_img1"->{this.product_img1=value;}
			case "product_img2"->{this.product_img2=value;}
			case "product_img3"->{this.product_img3=value;}
			case "product_img4"->{this.product_img4=value;}
			case "product_img5"->{this.product_img5=value;}
			case "product_size1"->{this.product_size1=value;}
			case "product_size2"->{this.product_size2=value;}
			case "product_size3"->{this.product_size3=value;}
			case "product_size4"->{this.product_size4=value;}
			case "product_size5"->{this.product_size5=value;}
			case "product_refund_replace_option"->{this.product_refund_replace_option=value;}
			case "product_rating"->{this.product_rating=value;}
			case "product_status"->{this.product_status=value;}
			case "product_status_updated_by"->{this.product_status_updated_by=value;}
			case "product_rating_count"->{this.product_rating_count=value;}
			case "wishlisted"->{this.wishlisted=value;}
			case "carted"->{this.carted=value;}
			}
		}
		final public String get_data(String which_data) {
			switch(which_data) {
			case "product_brand_name"->{return this.product_brand_name;}
			case "product_id"->{return this.product_id;}
			case "product_type"->{return this.product_type;}
			case "product_name"->{return this.product_name;}
			case "product_original_price"->{return this.product_original_price;}
			case "product_selling_price"->{return this.product_selling_price;}
			case "product_quantity"->{return this.product_quantity;}
			case "product_stock_status"->{return this.product_stock_status;}
			case "product_keywords"->{return this.product_keywords;}
			case "product_description"->{return this.product_description;}
			case "product_img1"->{return this.product_img1;}
			case "product_img2"->{return this.product_img2;}
			case "product_img3"->{return this.product_img3;}
			case "product_img4"->{return this.product_img4;}
			case "product_img5"->{return this.product_img5;}
			case "product_size1"->{return this.product_size1;}
			case "product_size2"->{return this.product_size2;}
			case "product_size3"->{return this.product_size3;}
			case "product_size4"->{return this.product_size4;}
			case "product_size5"->{return this.product_size5;}
			case "product_refund_replace_option"->{return this.product_refund_replace_option;}
			case "product_rating"->{return this.product_rating;}
			case "product_status"->{return this.product_status;}
			case "product_status_updated_by"->{return this.product_status_updated_by;}
			case "product_rating_count"->{return this.product_rating_count;}
			case "wishlisted"->{return this.wishlisted;}
			case "carted"->{return this.carted;}
			}
			return null;
		}
		
	}
	public final static class Electronics{
		private String product_brand_name,product_id,product_type,product_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_technical_description,product_img1,product_img2,product_img3,product_img4,product_img5,product_refund_replace_option,product_rating,product_rating_count,product_status,product_status_updated_by,wishlisted,carted;
		final public void set_data(String which_data,String value) {
			switch(which_data) {
			case "product_brand_name"->{this.product_brand_name=value;}
			case "product_id"->{this.product_id=value;}
			case "product_type"->{this.product_type=value;}
			case "product_name"->{this.product_name=value;}
			case "product_original_price"->{this.product_original_price=value;}
			case "product_selling_price"->{this.product_selling_price=value;}
			case "product_quantity"->{this.product_quantity=value;}
			case "product_stock_status"->{this.product_stock_status=value;}
			case "product_keywords"->{this.product_keywords=value;}
			case "product_description"->{this.product_description=value;}
			case "product_technical_description"->{this.product_technical_description=value;}
			case "product_img1"->{this.product_img1=value;}
			case "product_img2"->{this.product_img2=value;}
			case "product_img3"->{this.product_img3=value;}
			case "product_img4"->{this.product_img4=value;}
			case "product_img5"->{this.product_img5=value;}
			case "product_refund_replace_option"->{this.product_refund_replace_option=value;}
			case "product_rating"->{this.product_rating=value;}
			case "product_status"->{this.product_status=value;}
			case "product_status_updated_by"->{this.product_status_updated_by=value;}
			case "product_rating_count"->{this.product_rating_count=value;}
			case "wishlisted"->{this.wishlisted=value;}
			case "carted"->{this.carted=value;}
			}
		}
		final public String get_data(String which_data) {
			switch(which_data) {
			case "product_brand_name"->{return this.product_brand_name;}
			case "product_id"->{return this.product_id;}
			case "product_type"->{return this.product_type;}
			case "product_name"->{return this.product_name;}
			case "product_original_price"->{return this.product_original_price;}
			case "product_selling_price"->{return this.product_selling_price;}
			case "product_quantity"->{return this.product_quantity;}
			case "product_stock_status"->{return this.product_stock_status;}
			case "product_keywords"->{return this.product_keywords;}
			case "product_description"->{return this.product_description;}
			case "product_technical_description"->{return this.product_technical_description;}
			case "product_img1"->{return this.product_img1;}
			case "product_img2"->{return this.product_img2;}
			case "product_img3"->{return this.product_img3;}
			case "product_img4"->{return this.product_img4;}
			case "product_img5"->{return this.product_img5;}
			case "product_refund_replace_option"->{return this.product_refund_replace_option;}
			case "product_rating"->{return this.product_rating;}
			case "product_status"->{return this.product_status;}
			case "product_status_updated_by"->{return this.product_status_updated_by;}
			case "product_rating_count"->{return this.product_rating_count;}
			case "wishlisted"->{return this.wishlisted;}
			case "carted"->{return this.carted;}
			}
			return null;
		}
		
	}
	public final static class Food{
		private String product_brand_name,product_id,product_type,product_name,product_original_price,product_selling_price,product_quantity,product_stock_status,product_keywords,product_description,product_shelf_life,product_perishable,product_vegetarian,product_meat_type,product_img1,product_img2,product_img3,product_img4,product_img5,product_refund_replace_option,product_rating,product_rating_count,product_status,product_status_updated_by,wishlisted,carted;
		final public void set_data(String which_data,String value) {
			switch(which_data) {
			case "product_brand_name"->{this.product_brand_name=value;}
			case "product_id"->{this.product_id=value;}
			case "product_type"->{this.product_type=value;}
			case "product_name"->{this.product_name=value;}
			case "product_original_price"->{this.product_original_price=value;}
			case "product_selling_price"->{this.product_selling_price=value;}
			case "product_quantity"->{this.product_quantity=value;}
			case "product_stock_status"->{this.product_stock_status=value;}
			case "product_keywords"->{this.product_keywords=value;}
			case "product_description"->{this.product_description=value;}
			case "product_shelf_life"->{this.product_shelf_life=value;}
			case "product_perishable"->{this.product_perishable=value;}
			case "product_vegetarian"->{this.product_vegetarian=value;}
			case "product_meat_type"->{this.product_meat_type=value;}
			case "product_img1"->{this.product_img1=value;}
			case "product_img2"->{this.product_img2=value;}
			case "product_img3"->{this.product_img3=value;}
			case "product_img4"->{this.product_img4=value;}
			case "product_img5"->{this.product_img5=value;}
			case "product_refund_replace_option"->{this.product_refund_replace_option=value;}
			case "product_rating"->{this.product_rating=value;}
			case "product_status"->{this.product_status=value;}
			case "product_status_updated_by"->{this.product_status_updated_by=value;}
			case "product_rating_count"->{this.product_rating_count=value;}
			case "wishlisted"->{this.wishlisted=value;}
			case "carted"->{this.carted=value;}
			}
		}
		final public String get_data(String which_data) {
			switch(which_data) {
			case "product_brand_name"->{return this.product_brand_name;}
			case "product_id"->{return this.product_id;}
			case "product_type"->{return this.product_type;}
			case "product_name"->{return this.product_name;}
			case "product_original_price"->{return this.product_original_price;}
			case "product_selling_price"->{return this.product_selling_price;}
			case "product_quantity"->{return this.product_quantity;}
			case "product_stock_status"->{return this.product_stock_status;}
			case "product_keywords"->{return this.product_keywords;}
			case "product_description"->{return this.product_description;}
			case "product_shelf_life"->{return this.product_shelf_life;}
			case "product_perishable"->{return this.product_perishable;}
			case "product_vegetarian"->{return this.product_vegetarian;}
			case "product_meat_type"->{return this.product_meat_type;}
			case "product_img1"->{return this.product_img1;}
			case "product_img2"->{return this.product_img2;}
			case "product_img3"->{return this.product_img3;}
			case "product_img4"->{return this.product_img4;}
			case "product_img5"->{return this.product_img5;}
			case "product_refund_replace_option"->{return this.product_refund_replace_option;}
			case "product_rating"->{return this.product_rating;}
			case "product_status"->{return this.product_status;}
			case "product_status_updated_by"->{return this.product_status_updated_by;}
			case "product_rating_count"->{return this.product_rating_count;}
			case "wishlisted"->{return this.wishlisted;}
			case "carted"->{return this.carted;}
			}
			return null;
		}
		
	}
	final public static int get_discount(String val1,String val2) {
		return (int)(100-Double.parseDouble(val1)/Double.parseDouble(val2)*100);
	}
}
