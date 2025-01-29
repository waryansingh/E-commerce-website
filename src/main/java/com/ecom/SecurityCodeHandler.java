package com.ecom;
import java.util.Random;

public class SecurityCodeHandler {
	private static final int digits=6;
	private long expiry_time=-1;
	private StringBuilder security_code=null;
	SecurityCodeHandler(){
		generate_security_code();
	}
	
	final private void generate_security_code() {
		StringBuilder code= new StringBuilder(digits);
		Random random=new Random();
		for(int i=1;i<=digits;i++){
			code.append(Integer.toString(random.nextInt(10)));
		}
		this.security_code=code;
		this.expiry_time = System.currentTimeMillis() + 40 * 1000;
	}
	
	final private void invalidate() {
		this.expiry_time=-1;
		this.security_code=null;
	}
	
	final public boolean validate_security_code(StringBuilder code) {
		if(this.expiry_time!=-1 &&  System.currentTimeMillis()<=this.expiry_time && this.security_code.compareTo(code)==0) {
			invalidate();
			return true;
		}
		invalidate();
		return false;
	}
	
	final public boolean code_expired() {
		if(this.expiry_time==-1 ||  System.currentTimeMillis()>=this.expiry_time)return true;
		return false;
	}
	
	final public StringBuilder get_security_code() {return this.security_code;}
}
