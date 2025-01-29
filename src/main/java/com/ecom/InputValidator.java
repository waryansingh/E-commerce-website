package com.ecom;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class InputValidator {
	final static protected boolean is_valid_text(String text) {
		Pattern pattern=Pattern.compile("^[A-Za-z ]+$");
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}
	final static protected boolean is_valid_alphanumeric(String text) {
		Pattern pattern=Pattern.compile("^[A-Za-z0-9, ]+$");
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}
	final static protected boolean is_valid_email(String text) {
		Pattern pattern=Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}
	final static protected boolean is_valid_phonenumber(String text) {
		Pattern pattern=Pattern.compile("^[0-9]{10}$");
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}
	final static protected boolean is_valid_number(String text) {
		Pattern pattern=Pattern.compile("^[0-9]+$");
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}
	final static protected boolean is_valid_number(String text,int length) {
		Pattern pattern=Pattern.compile("^\\d{" + length + "}$");
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}
	final static protected boolean is_valid_address(String text) {
		Pattern pattern=Pattern.compile("^[^<>\\\"'&]*$");
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}
	final static protected boolean contains_html(String text) {
		Pattern pattern=Pattern.compile("^[^<>\"'&]*(\n[^<>\"'&]*)*$");
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}
	final static protected boolean contains_sql(String text) {
		Pattern pattern=Pattern.compile("(?i)(SELECT|INSERT|UPDATE|DELETE|DROP|ALTER|CREATE|EXEC|UNION|--|;|\\bWHERE\\b|\\bHAVING\\b|\\bGROUP\\b|\\bORDER\\b|\\bLIMIT\\b|\\bFROM\\b|\\bIN\\b)");
		Matcher matcher = pattern.matcher(text);
		return matcher.find();
	}
	final static protected boolean is_valid_address_details(String []addresses) {
		int length=addresses.length;
		for(int i=0;i<length-1;i++) {
			if(!is_valid_address(addresses[i])) {return false;}
		}
		return is_valid_number(addresses[length-1]);
	}
	final static protected boolean is_empty(String text) {
		if(text==null || text.isEmpty())return true;
		return false;
	}
	final static protected boolean is_empty(String []texts) {
		for(int i=0;i<texts.length;i++) {
			if(texts[i]==null || texts[i].isEmpty())
				return true;
		}
		return false;
	}
}
