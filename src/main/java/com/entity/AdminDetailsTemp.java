package com.entity;

import java.io.InputStream;

public class AdminDetailsTemp {
	private String[] admin_personal_details=null;
	private String[] admin_address=null;
	private InputStream[] images_stream=null;
	private String[] images_name=null;
	public AdminDetailsTemp(String[] admin_personal_details,String[] admin_address,InputStream[] images_stream,String[] images_name) {
		this.admin_personal_details=admin_personal_details;
		this.admin_address=admin_address;
		this.images_stream=images_stream;
		this.images_name=images_name;
	}
	final public Object get_data(String which_array) {
		switch(which_array) {
		case "admin_personal_details"->{return this.admin_personal_details;}
		case "admin_address"->{return this.admin_address;}
		case "images_stream"->{return this.images_stream;}
		case "images_name"->{return this.images_name;}
		}
		return null;
	}
}
