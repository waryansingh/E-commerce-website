package com.entity;

public class ClientDetailsTemp {
	private String []client_personal_details=null;
	private String []client_personal_address=null;
	public ClientDetailsTemp(String []client_personal_details,String []client_personal_address){
		this.client_personal_details=client_personal_details;
		this.client_personal_address=client_personal_address;
	}
	
	final public String[] get_data(String which_array) {
		switch(which_array) {
		case "client_personal_details"->{return this.client_personal_details;}
		case "client_personal_address"->{return this.client_personal_address;}
		}
		return null;
	}
}
