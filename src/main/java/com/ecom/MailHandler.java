package com.ecom;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.entity.EmailDetails;

public final class MailHandler {
	private EmailDetails maildetails=null;
	private static final String host="smtp.gmail.com",from_email="teamurbangroove@gmail.com",from_email_password="zenm ctpr raws gvqe",port="587";
	private MimeMessage message=null;
	private Session session;
	private Properties properties=null;
	MailHandler(EmailDetails maildetails){
		this.maildetails=maildetails;
		map_properties();
		set_mail_session();
	}
	
	final public void send_mail(){
		try {Transport.send(compose_mail());} catch (MessagingException e) {e.printStackTrace();}
	}
	
	final private MimeMessage compose_mail() {
		this.message=new MimeMessage(session);
		try {
			this.message.setFrom(new InternetAddress(from_email));
			this.message.setRecipient(Message.RecipientType.TO,new InternetAddress((String)this.maildetails.get_data("receiver_mail")));
			this.message.setSubject((String)this.maildetails.get_data("mail_subject"));
			this.message.setText((String)this.maildetails.get_data("mail_text"));
		}catch(Exception e) {e.printStackTrace();}
		return this.message;
	}
	
	final private void set_mail_session() {
		session=Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from_email,from_email_password);
			}
		});
		session.setDebug(true);
	}
	
	final private void map_properties() {
		properties=new Properties();
		properties.put("mail.smtp.host",host);
		properties.put("mail.smtp.port",port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable","true");
		properties.put("mail.smtp.ssl.protocols","TLSv1.2");
	}
	
}
