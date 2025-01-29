package com.entity;

public final class ClientFavouriteAddress {
	private String client_fav_address_id,client_fav_address_type,client_fav_address,client_fav_city,client_fav_state,client_fav_landmark,client_fav_zip_code,receiver_name,receiver_phone;
	final public void set_data(String which_data, String value) {
		 switch(which_data) {
		 case "client_fav_address_id"->{this.client_fav_address_id=value;}
		 case "client_fav_address_type"->{this.client_fav_address_type=value;}
		 case "client_fav_address"->{this.client_fav_address=value;}
		 case "client_fav_city"->{this.client_fav_city=value;}
		 case "client_fav_state"->{this.client_fav_state=value;}
		 case "client_fav_landmark"->{this.client_fav_landmark=value;}
		 case "client_fav_zip_code"->{this.client_fav_zip_code=value;}
		 case "receiver_name"->{this.receiver_name=value;}
		 case "receiver_phone"->{this.receiver_phone=value;}
		 }
	 }
	 final public String get_data(String which_data) {
		 switch(which_data) {
		 case "client_fav_address_id"->{return this.client_fav_address_id;}
		 case "client_fav_address_type"->{return this.client_fav_address_type;}
		 case "client_fav_address"->{return this.client_fav_address;}
		 case "client_fav_city"->{return this.client_fav_city;}
		 case "client_fav_state"->{return this.client_fav_state;}
		 case "client_fav_landmark"->{return this.client_fav_landmark;}
		 case "client_fav_zip_code"->{return this.client_fav_zip_code;}
		 case "receiver_name"->{return this.receiver_name;}
		 case "receiver_phone"->{return this.receiver_phone;}
		 }
		 return null; 
	 }
}
