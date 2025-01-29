package com.entity;

public class ClientPersonalDetails {
private String client_id,client_fname,client_lname,client_email,client_phone,client_username,client_profile_picture,client_address_id,client_address_type,client_address,client_city,client_state,client_landmark,client_zip_code;
public void set_data(String which_data,String value) {
	switch(which_data) {
	case "client_id"->{this.client_id=value;}
	case "client_fname"->{this.client_fname=value;}
	case "client_lname"->{this.client_lname=value;}
	case "client_email"->{this.client_email=value;}
	case "client_phone"->{this.client_phone=value;}
	case "client_username"->{this.client_username=value;}
	case "client_profile_picture"->{this.client_profile_picture=value;}
	case "client_address_id"->{this.client_address_id=value;}
	case "client_address_type"->{this.client_address_type=value;}
	case "client_address"->{this.client_address=value;}
	case "client_city"->{this.client_city=value;}
	case "client_state"->{this.client_state=value;}
	case "client_landmark"->{this.client_landmark=value;}
	case "client_zip_code"->{this.client_zip_code=value;}
	}
}

public String get_data(String which_data) {
	switch(which_data) {
	case "client_id"->{return this.client_id;}
	case "client_fname"->{return this.client_fname;}
	case "client_lname"->{return this.client_lname;}
	case "client_email"->{return this.client_email;}
	case "client_phone"->{return this.client_phone;}
	case "client_username"->{return this.client_username;}
	case "client_profile_picture"->{return this.client_profile_picture;}
	case "client_address_id"->{return this.client_address_id;}
	case "client_address_type"->{return this.client_address_type;}
	case "client_address"->{return this.client_address;}
	case "client_city"->{return this.client_city;}
	case "client_state"->{return this.client_state;}
	case "client_landmark"->{return this.client_landmark;}
	case "client_zip_code"->{return this.client_zip_code;}
	}
	return null;
}
}
