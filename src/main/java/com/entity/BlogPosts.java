package com.entity;
public class BlogPosts {
	private String blog_id,blog_title,blog_content,blog_img,client_id,client_name,link,like_count,dislike_count,upload_date;
	public void set_data(String which_data, String value) {
		switch(which_data) {
		case "blog_id"->{this.blog_id=value;}
		case "blog_title"->{this.blog_title=value;}
		case "blog_content"->{this.blog_content=value;}
		case "blog_img"->{this.blog_img=value;}
		case "client_id"->{this.client_id=value;}
		case "client_name"->{this.client_name=value;}
		case "link"->{this.link=value;}
		case "like_count"->{this.like_count=value;}
		case "dislike_count"->{this.dislike_count=value;}
		case "upload_date"->{this.upload_date=value;}
		}
	}
	public String get_data(String which_data) {
		switch(which_data) {
		case "blog_id"->{return this.blog_id;}
		case "blog_title"->{return this.blog_title;}
		case "blog_content"->{return this.blog_content;}
		case "blog_img"->{return this.blog_img;}
		case "client_id"->{return this.client_id;}
		case "client_name"->{return this.client_name;}
		case "link"->{return this.link;}
		case "like_count"->{return this.like_count;}
		case "dislike_count"->{return this.dislike_count;}
		case "upload_date"->{return this.upload_date;}
		}
		return null;
	}
}
