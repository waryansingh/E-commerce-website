<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.BlogPosts" %>
<%@ page import="java.util.ArrayList" %>
<%if(request.getSession().getAttribute("Client_username")==null){response.sendRedirect("Client_login_signup.jsp");return;} %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>My Blogs</title>
<style>
/* General Layout */
/* General Layout */
body {
    font-family: 'Arial', sans-serif;
    margin: 0;
    padding: 0;
    background: linear-gradient(to bottom right, #ff8c00, #ffcc66);
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
}

/* Container */
.blog-container {
    background-color: #fff;
    padding: 30px;
    border-radius: 15px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
    width: 90%;
    max-height: 90vh;
    max-width: 1400px;
    box-sizing: border-box;
    overflow-y: auto; /* Allow scrolling for long content */
}

/* Header */
.blog-header {
    text-align: center;
    margin-bottom: 20px;
}

.blog-header h3 {
    font-size: 2.2rem;
    color: #ff8c00;
    margin: 0;
}

.blog-header h3:after {
    content: '';
    display: block;
    width: 70px;
    height: 4px;
    background-color: #ff8c00;
    margin: 12px auto 0;
    border-radius: 2px;
}

/* Blog List */
.blog-list {
    list-style: none;
    padding: 0;
    margin: 0;
    display: flex;
    flex-direction: column;
    gap: 20px;
    overflow-y: auto; /* Ensure the list itself scrolls if too long */
}

/* Blog Card */
.blog-card {
    background: #fff8e5;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;
    gap: 10px;
    position: relative;
}

.blog-card h4 {
    font-size: 1.5rem;
    color: #ff8c00;
    margin: 0;
}

.blog-card p {
    font-size: 1rem;
    color: #555;
    margin: 0;
    line-height: 1.6;
}

/* Image */
.blog-card img {
    width: 220px;
    height: 220px;
    object-fit: cover;
    border-radius: 8px;
    margin-top: 10px;
}

/* Buttons */
.blog-card .actions {
    margin-top: 10px;
    display: flex;
    gap: 10px;
}

.blog-card button {
    padding: 10px 15px;
    font-size: 0.9rem;
    color: #fff;
    background: linear-gradient(to right, #ff8c00, #ffb84d);
    border: none;
    border-radius: 8px;
    cursor: pointer;
    transition: background-color 0.3s ease, transform 0.2s ease;
}

.blog-card button:hover {
    background: linear-gradient(to right, #e65c00, #ff8c00);
    transform: scale(1.05);
}

/* Back Button */
.back-btn {
    display: block;
    margin-top: 20px;
    font-size: 1rem;
    color: #ff8c00;
    text-align: center;
    font-weight: bold;
    text-decoration: none;
    transition: color 0.3s ease;
}

.back-btn:hover {
    color: #e65c00;
}

</style>
</head>
<body>
<div class="blog-container">
    <div class="blog-header">
        <h3>Your Blogs</h3>
        <a href="ClientFrontEndLoader?operation=blog-posts-all" class="back-btn">← Back to Home</a>
    </div>
    <ul class="blog-list">
        <%
        @SuppressWarnings("unchecked")
        ArrayList<BlogPosts> posts=(ArrayList<BlogPosts>)request.getSession().getAttribute("client-blog-data");
        if(posts==null || posts.isEmpty()){%>
        <div>
        	<h2>No Blog Posts found.</h2>
    	</div>
        <%}else{
        for(BlogPosts post:posts){%>
        <li class="blog-card">
            <h4><%=post.get_data("blog_title") %></h4>
            <p>Author: <%=post.get_data("client_name") %></p>
            <img src="Blog post images/<%=post.get_data("blog_img") %>" alt="Blog Image">
            <p><%=post.get_data("blog_content")%></p>
            <%if(!post.get_data("link").equals("")){%>
            <h3><a href="<%=post.get_data("link")%>" target="_blank">Product Link</a></h3>
            <%}%>
            <div class="actions">
            <form action="BlogPostHandler?operation=load-product-for-edit" method="post">
            	<input type="hidden" name="blog_id" value="<%=post.get_data("blog_id")%>">
                <button type="submit">Edit</button>
            </form>
            <form action="BlogPostHandler?operation=delete-post" method="post">
            	<input type="hidden" name="blog_id" value="<%=post.get_data("blog_id")%>">
            	<input type="hidden" name="blog_img" value="<%=post.get_data("blog_img")%>">
                <button>Delete</button>
            </form>
            </div>
        </li>
        <%}} %>
    </ul>
</div>
</body>
</html>
