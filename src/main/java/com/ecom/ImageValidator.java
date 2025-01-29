package com.ecom;
import java.util.UUID;

import jakarta.servlet.http.Part;

final class ImageValidator {
	final protected static boolean is_valid_image_size(Part []images) {
		final long MaxFileSize = 2 * 1024 * 1024; // 2 MB
		for(Part image:images) {
			if(image.getSize()>MaxFileSize) {
				return false;
			}
		}
        return true;
    }
	
	final protected static boolean is_valid_image_size(Part image) {
		final long MaxFileSize = 2 * 1024 * 1024; // 2 MB
		if(image.getSize()>MaxFileSize) {
			return false;
		}
        return true;
    }

	final protected static boolean is_valid_image_type(String []images_name) {
		String []valid_image_types= {".jpg",".jpeg",".jfif",".png"};
		int is_valid = 0;
		for(int i=0;i<images_name.length;i++) {
			for(String types:valid_image_types) {
				if(images_name[i].toLowerCase().endsWith(types)) {
					is_valid++;
	                break;
				}
			}
		}
		if (is_valid==images_name.length) return true;
	    return false;
	}
	
	final protected static boolean is_valid_image_type(String image_name) {
		String []valid_image_types= {".jpg",".jpeg",".jfif",".png"};
		for(String types:valid_image_types) {
			if(image_name.toLowerCase().endsWith(types)) {
				return true;
			}
		}
		return false;
	}
	
	final protected static String[] get_new_image_name(String []images_name,String username) {
		for(int i=0;i<images_name.length;i++) {
			images_name[i] = username+UUID.randomUUID().toString() + images_name[i].substring(images_name[i].lastIndexOf("."));
		}
		return images_name;
	}
	
	final protected static String get_new_image_name(String image_name,String username) {
		return username+UUID.randomUUID().toString() + image_name.substring(image_name.lastIndexOf("."));
	}
	
}
