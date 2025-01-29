package com.entity;

import java.io.InputStream;

public class VendorDetailsTemp {
	private String[] vendor_personal_details=null;
	private String[] vendor_personal_address=null;
	private String[] vendor_shop_address=null;
	private InputStream []images_stream=null;
	private String[] image_names=null;
	public VendorDetailsTemp(String[] vendor_personal_details,String[] vendor_personal_address,String[] vendor_shop_address,InputStream []images_stream,String []image_names) {
		this.vendor_personal_details=vendor_personal_details;
		this.vendor_personal_address=vendor_personal_address;
		this.vendor_shop_address=vendor_shop_address;
		this.images_stream=images_stream;
		this.image_names=image_names;
	}
	final public Object get_data(String which_array) {
		switch(which_array) {
		case "vendor_personal_details"->{return this.vendor_personal_details;}
		case "vendor_personal_address"->{return this.vendor_personal_address;}
		case "vendor_shop_address"->{return this.vendor_shop_address;}
		case "images_stream"->{return this.images_stream;}
		case "image_names"->{return this.image_names;}
		}
	return null;
	}
}
