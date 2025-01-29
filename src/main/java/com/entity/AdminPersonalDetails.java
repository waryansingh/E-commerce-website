package com.entity;
public final class AdminPersonalDetails {
	private String admin_id,admin_full_name,admin_email,admin_phone,admin_username,admin_address,admin_city,admin_state,admin_zip_code,admin_profile_picture,admin_govId_picture,admin_status_updated,admin_status;
	final public void set_data(String which_data,String value) {
		switch(which_data) {
		case "admin_id"->{this.admin_id=value;}
		case "admin_full_name"->{this.admin_full_name=value;}
		case "admin_email"->{this.admin_email=value;}
		case "admin_phone"->{this.admin_phone=value;}
		case "admin_username"->{this.admin_username=value;}
		case "admin_address"->{this.admin_address=value;}
		case "admin_city"->{this.admin_city=value;}
		case "admin_state"->{this.admin_state=value;}
		case "admin_zip_code"->{this.admin_zip_code=value;}
		case "admin_profile_picture"->{this.admin_profile_picture=value;}
		case "admin_govId_picture"->{this.admin_govId_picture=value;}
		case "admin_status"->{this.admin_status=value;}
		case "admin_status_updated"->{this.admin_status_updated=value;}
		}
	}
	final public String get_data(String which_data) {
		switch(which_data) {
		case "admin_id"->{return this.admin_id;}
		case "admin_full_name"->{return this.admin_full_name;}
		case "admin_email"->{return this.admin_email;}
		case "admin_phone"->{return this.admin_phone;}
		case "admin_username"->{return this.admin_username;}
		case "admin_address"->{return this.admin_address;}
		case "admin_city"->{return this.admin_city;}
		case "admin_state"->{return this.admin_state;}
		case "admin_zip_code"->{return this.admin_zip_code;}
		case "admin_profile_picture"->{return this.admin_profile_picture;}
		case "admin_govId_picture"->{return this.admin_govId_picture;}
		case "admin_status"->{return this.admin_status;}
		case "admin_status_updated"->{return this.admin_status_updated;}
		}
		return null;
	}
}
