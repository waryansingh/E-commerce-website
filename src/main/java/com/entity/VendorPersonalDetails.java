package com.entity;

public final class VendorPersonalDetails {
	private String vendor_id,vendor_full_name,vendor_email,vendor_phone,vendor_username,vendor_address,vendor_city,vendor_state,vendor_zip_code,vendor_gst_number,vendor_trade_license,vendor_govId_picture,vendor_status_updated,vendor_status,vendor_shop_name_number,vendor_shop_address,vendor_shop_city,vendor_shop_state,vendor_shop_zip_code;
	
	final public void set_data(String which_data,String value) {
		switch(which_data) {
		case "vendor_id"->{this.vendor_id=value;}
		case "vendor_full_name"->{this.vendor_full_name=value;}
		case "vendor_email"->{this.vendor_email=value;}
		case "vendor_phone"->{this.vendor_phone=value;}
		case "vendor_username"->{this.vendor_username=value;}
		case "vendor_address"->{this.vendor_address=value;}
		case "vendor_city"->{this.vendor_city=value;}
		case "vendor_state"->{this.vendor_state=value;}
		case "vendor_zip_code"->{this.vendor_zip_code=value;}
		case "vendor_trade_license"->{this.vendor_trade_license=value;}
		case "vendor_govId_picture"->{this.vendor_govId_picture=value;}
		case "vendor_status"->{this.vendor_status=value;}
		case "vendor_shop_name_number"->{this.vendor_shop_name_number=value;}
		case "vendor_shop_address"->{this.vendor_shop_address=value;}
		case "vendor_shop_city"->{this.vendor_shop_city=value;}
		case "vendor_shop_state"->{this.vendor_shop_state=value;}
		case "vendor_shop_zip_code"->{this.vendor_shop_zip_code=value;}
		case "vendor_gst_number"->{this.vendor_gst_number=value;}
		case "vendor_status_updated"->{this.vendor_status_updated=value;}
		}
	}
	
	final public String get_data(String which_data) {
		switch(which_data) {
		case "vendor_id"->{return this.vendor_id;}
		case "vendor_full_name"->{return this.vendor_full_name;}
		case "vendor_email"->{return this.vendor_email;}
		case "vendor_phone"->{return this.vendor_phone;}
		case "vendor_username"->{return this.vendor_username;}
		case "vendor_address"->{return this.vendor_address;}
		case "vendor_city"->{return this.vendor_city;}
		case "vendor_state"->{return this.vendor_state;}
		case "vendor_zip_code"->{return this.vendor_zip_code;}
		case "vendor_trade_license"->{return this.vendor_trade_license;}
		case "vendor_govId_picture"->{return this.vendor_govId_picture;}
		case "vendor_status"->{return this.vendor_status;}
		case "vendor_shop_name_number"->{return this.vendor_shop_name_number;}
		case "vendor_shop_address"->{return this.vendor_shop_address;}
		case "vendor_shop_city"->{return this.vendor_shop_city;}
		case "vendor_shop_state"->{return this.vendor_shop_state;}
		case "vendor_shop_zip_code"->{return this.vendor_shop_zip_code;}
		case "vendor_gst_number"->{return this.vendor_gst_number;}
		case "vendor_status_updated"->{return this.vendor_status_updated;}
		}
		return null;
	}
}
