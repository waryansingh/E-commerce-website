package com.ecom;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.entity.BlogPosts;

@MultipartConfig
@WebServlet(
        name = "BlogPostHandler",
        urlPatterns = {"/BlogPostHandler"}
)
public class BlogPostHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connect=null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch(request.getParameter("operation")==null?"":request.getParameter("operation")) {
		case "upload-post"->{
			String []blog_post_details= {
					request.getParameter("blog-title"),
					request.getParameter("blog-content")
				};
				String product_link=request.getParameter("product-link");
				Part image=request.getPart("blog-image");
				if(image==null || image.getSize()==0) {response.sendRedirect("Client_blog_upload.jsp?msg=Image must be added.");return;}
				String image_name=image.getSubmittedFileName();
				if(!InputValidator.is_empty(blog_post_details)) {
					if(is_valid_post(blog_post_details)) {
						if(ImageValidator.is_valid_image_size(image) && ImageValidator.is_valid_image_type(image_name)) {
							if(upload_blog_post(blog_post_details,image,image_name,(int)request.getSession().getAttribute("Client_id"),product_link,request)) {
								response.sendRedirect("Client_blog_upload.jsp?msg=Blog posted.");return;
							}else {response.sendRedirect("Client_blog_upload.jsp?msg=Failed to upload your blog post.");return;}
						}else {response.sendRedirect("Client_blog_upload.jsp?msg=Image must be (.jpg, .jpeg, .png, .jfif) type and size must be under 2mb.");return;}
					}else {response.sendRedirect("Client_blog_upload.jsp?msg=Some special characters are not allowed.");return;}
				}else {response.sendRedirect("Client_blog_upload.jsp?msg=Your blog cannot be empty.");return;}
		}
		case "load-product-for-edit"->{
			request.getSession().setAttribute("edit-post-data",load_post(Long.parseLong(request.getParameter("blog_id"))));
			request.getRequestDispatcher("Client_blog_upload.jsp").forward(request, response);return;
		}
		case "update-post"->{
			String []blog_details= {
				request.getParameter("blog-id"),
				request.getParameter("blog-title"),
				request.getParameter("blog-content")
			};
			String product_link=request.getParameter("product-link");
			if(!InputValidator.is_empty(blog_details)) {
				if(is_valid_post(blog_details)) {
					if(update_blog_post(Long.parseLong(blog_details[0]),blog_details,product_link)) {
						response.sendRedirect("Client_blog_upload.jsp?msg=Blog updated.");return;
					}else {response.sendRedirect("Client_blog_upload.jsp?msg=Failed to update your blog post.");return;}
				}else {response.sendRedirect("Client_blog_upload.jsp?msg=Some special characters are not allowed.");return;}
			}else {response.sendRedirect("Client_blog_upload.jsp?msg=Your blog cannot be empty.");return;}
		}
		case "delete-post"->{
			delete_blog_post(Long.parseLong(request.getParameter("blog_id")),request.getParameter("blog_img"));
			response.sendRedirect("ClientFrontEndLoader?operation=personal-blog-posts");return;
		}
		case "do-like"->{
			do_like(Long.parseLong(request.getParameter("blog_id")),Integer.parseInt(request.getParameter("client_id")));
			response.sendRedirect("ClientFrontEndLoader?operation=load-post&blog-id="+request.getParameter("blog_id"));return;
		}
		case "do-dislike"->{
			do_dislike(Long.parseLong(request.getParameter("blog_id")),Integer.parseInt(request.getParameter("client_id")));
			response.sendRedirect("ClientFrontEndLoader?operation=load-post&blog-id="+request.getParameter("blog_id"));return;
		}
		}
	}
	
	final private void delete_blog_post(long blog_id,String blog_img) {
		set_connection();
		try{
			connect.createStatement().execute(String.format("DELETE FROM blog_posts WHERE blog_id=%d",blog_id));
			connect.commit();
			new File("E:/Dynamic Web Project/src/main/webapp/Blog post images/"+blog_img).delete();
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private boolean update_blog_post(long blog_id,String []blog_details,String product_link) {
		set_connection();
		String query="UPDATE blog_posts SET blog_title=?,blog_content=?,link=? WHERE blog_id=?";
		try(PreparedStatement statement=connect.prepareStatement(query)){
			statement.setString(1,blog_details[1]);
			statement.setString(2,blog_details[2]);
			statement.setString(3,product_link);
			statement.setLong(4,blog_id);
			statement.execute();
			statement.close();
			connect.commit();
			return true;
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
		return false;
	}
	
	final private BlogPosts load_post(long blog_id) {
		set_connection();
		BlogPosts post=null;
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT * FROM blog_posts WHERE blog_id=%d",blog_id))){
			if(result.next()) {
				post=new BlogPosts();
				post.set_data("blog_id",Integer.toString(result.getInt("blog_id")));
				post.set_data("blog_title",result.getString("blog_title"));
				post.set_data("blog_content",result.getString("blog_content"));
				post.set_data("blog_img",result.getString("blog_img"));
				post.set_data("client_id",Integer.toString(result.getInt("client_id")));
				post.set_data("client_name",result.getString("client_name"));
				post.set_data("link",result.getString("link"));
				post.set_data("like_count",Long.toString(result.getLong("like_count")));
				post.set_data("dislike_count",Long.toString(result.getLong("dislike_count")));
				post.set_data("upload_date",String.valueOf(result.getDate("upload_date")));
			}
			close_connection();
			return post;
		}catch(Exception e) {e.printStackTrace();}
		close_connection();
		return null;
	}
	
	final private void do_dislike(long blog_id,int client_id) {
		set_connection();
		String get_log=String.format("SELECT log_id,log FROM blog_like_dislike_log WHERE blog_id=%d AND client_id=%d",blog_id,client_id);
		try {
			ResultSet result1=connect.createStatement().executeQuery(get_log);
			if(result1.next()) {//removed dislike from already disliked post
				if(result1.getInt("log")==0) {
				ResultSet get_disliked_post=connect.createStatement().executeQuery(String.format("SELECT dislike_count FROM blog_posts WHERE blog_id=%d",blog_id));
				if(get_disliked_post.next()) {
					PreparedStatement statement=connect.prepareStatement("UPDATE blog_posts SET dislike_count=? WHERE blog_id=?");
					statement.setLong(1,(get_disliked_post.getLong("dislike_count")-1));
					statement.setLong(2,blog_id);
					statement.execute();
					statement.close();
				}
				get_disliked_post.close();
				connect.createStatement().execute(String.format("DELETE FROM blog_like_dislike_log WHERE log_id=%d",result1.getLong("log_id")));
				}
				if(result1.getInt("log")==1) {//removed like from already liked post and given a dislike 
				ResultSet get_liked_post=connect.createStatement().executeQuery(String.format("SELECT like_count,dislike_count FROM blog_posts WHERE blog_id=%d",blog_id));
				if(get_liked_post.next()) {
					PreparedStatement statement=connect.prepareStatement("UPDATE blog_posts SET like_count=?,dislike_count=? WHERE blog_id=?");
					statement.setLong(1, (get_liked_post.getLong("like_count")-1));
					statement.setLong(2, (get_liked_post.getLong("dislike_count")+1));
					statement.setLong(3, blog_id);
					statement.execute();
					statement.close();
				}
				get_liked_post.close();
				PreparedStatement statement=connect.prepareStatement("UPDATE blog_like_dislike_log SET log=? WHERE log_id=?");
				statement.setInt(1,0);
				statement.setLong(2,result1.getLong("log_id"));
				statement.execute();
				statement.close();
				}
			}
			else {//disliked a post
				ResultSet result2=connect.createStatement().executeQuery(String.format("SELECT dislike_count FROM blog_posts WHERE blog_id=%d",blog_id));
				if(result2.next()) {
					PreparedStatement statement=connect.prepareStatement("UPDATE blog_posts SET dislike_count=? WHERE blog_id=?");
					statement.setLong(1,(result2.getLong("dislike_count")+1));
					statement.setLong(2,blog_id);
					statement.execute();
					statement.close();
				}
				result2.close();
				PreparedStatement statement=connect.prepareStatement("INSERT INTO blog_like_dislike_log(blog_id,client_id,log) VALUES(?,?,?)");
				statement.setLong(1,blog_id);
				statement.setInt(2,client_id);
				statement.setInt(3,0);
				statement.execute();
				statement.close();
			}
			result1.close();
			connect.commit();
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private void do_like(long blog_id,int client_id) {
		set_connection();
		String get_log=String.format("SELECT log_id,log FROM blog_like_dislike_log WHERE blog_id=%d AND client_id=%d",blog_id,client_id);
		try {
			ResultSet result1=connect.createStatement().executeQuery(get_log);
			if(result1.next()) {//removed like from already liked post
				if(result1.getInt("log")==1) {
				ResultSet get_liked_post=connect.createStatement().executeQuery(String.format("SELECT like_count FROM blog_posts WHERE blog_id=%d",blog_id));
				if(get_liked_post.next()) {
					PreparedStatement statement=connect.prepareStatement("UPDATE blog_posts SET like_count=? WHERE blog_id=?");
					statement.setLong(1,(get_liked_post.getLong("like_count")-1));
					statement.setLong(2,blog_id);
					statement.execute();
					statement.close();
				}
				get_liked_post.close();
				connect.createStatement().execute(String.format("DELETE FROM blog_like_dislike_log WHERE log_id=%d",result1.getLong("log_id")));
				} 
				if(result1.getInt("log")==0){//removed dislike from already disliked post and given a like
				ResultSet get_disliked_post=connect.createStatement().executeQuery(String.format("SELECT like_count,dislike_count FROM blog_posts WHERE blog_id=%d",blog_id));
				if(get_disliked_post.next()) {
					PreparedStatement statement=connect.prepareStatement("UPDATE blog_posts SET like_count=?,dislike_count=? WHERE blog_id=?");
					statement.setLong(1,(get_disliked_post.getLong("like_count")+1));
					statement.setLong(2,(get_disliked_post.getLong("dislike_count")-1));
					statement.setLong(3,blog_id);
					statement.execute();
					statement.close();
				}
				get_disliked_post.close();
				PreparedStatement statement=connect.prepareStatement("UPDATE blog_like_dislike_log SET log=? WHERE log_id=?");
				statement.setInt(1, 1);
				statement.setLong(2,result1.getLong("log_id"));
				statement.execute();
				statement.close();
				}
			}
			else{//liked a post
				ResultSet result2=connect.createStatement().executeQuery(String.format("SELECT like_count FROM blog_posts WHERE blog_id=%d",blog_id));
				if(result2.next()) {
					PreparedStatement statement=connect.prepareStatement("UPDATE blog_posts SET like_count=? WHERE blog_id=?");
					statement.setLong(1,(result2.getLong("like_count")+1));
					statement.setLong(2,blog_id);
					statement.execute();
					statement.close();
				}
				result2.close();
				PreparedStatement statement=connect.prepareStatement("INSERT INTO blog_like_dislike_log(blog_id,client_id,log) VALUES(?,?,?)");
				statement.setLong(1,blog_id);
				statement.setInt(2,client_id);
				statement.setInt(3,1);
				statement.execute();
				statement.close();
			}
			result1.close();
			connect.commit();
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
		}
		close_connection();
	}
	
	final private boolean upload_blog_post(String []blog_post_details,Part image,String image_name,int client_id,String product_link,HttpServletRequest request) {
		set_connection();
		String client_name="N/A";
		try(ResultSet result=connect.createStatement().executeQuery(String.format("SELECT client_fname,client_lname FROM client_personal_details WHERE client_id=%d",client_id))){
			if(result.next()) client_name=result.getString("client_fname")+" "+result.getString("client_lname");
			else return false;
		}catch(Exception e) {e.printStackTrace();}
		String query="INSERT INTO blog_posts(blog_title,blog_content,blog_img,client_id,client_name,link) VALUES(?,?,?,?,?,?)";
		image_name=ImageValidator.get_new_image_name(image_name,(String)request.getSession().getAttribute("Client_username"));
		String upload_path="E:/Dynamic Web Project/src/main/webapp/Blog post images/";
		File upload=new File(upload_path);
		try(PreparedStatement statement=connect.prepareStatement(query)){
			statement.setString(1,blog_post_details[0]);
			statement.setString(2,blog_post_details[1]);
			statement.setString(3,image_name);
			statement.setInt(4,client_id);
			statement.setString(5,client_name);
			statement.setString(6,product_link);
			statement.execute();
			if(!upload.exists())upload.mkdir();
			upload_path="E:/Dynamic Web Project/src/main/webapp/Blog post images/"+image_name;
			image.write(upload_path);
			connect.commit();
			return true;
		}catch(Exception e) {e.printStackTrace();
			try {connect.rollback();} 
			catch (SQLException e1) {e1.printStackTrace();}
			new File("E:/Dynamic Web Project/src/main/webapp/Blog post images/"+image_name).delete();
		}
		close_connection();
		return false;
	}
	
	final private static boolean is_valid_post(String []blog_post_details) {
		for(int i=0;i<blog_post_details.length;i++) {
			if(InputValidator.contains_sql(blog_post_details[i])){return false;}
		}
		return true;
	}
	
	final private void set_connection() {
		try {if(connect==null || connect.isClosed()) {connect=ConnectToDB.create_connection();}
	} catch (SQLException e) {e.printStackTrace();}
	}
	final private void close_connection() {
		try {if(!connect.isClosed()|| connect!=null) connect.close();} 
		catch (SQLException e) {e.printStackTrace();}
	}
}
