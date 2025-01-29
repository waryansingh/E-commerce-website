<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.BlogPosts" %>
<%if(request.getSession().getAttribute("Client_username")==null){response.sendRedirect("Client_login_signup.jsp");return;} %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>View Blog Post</title>
<style>
/* General Reset */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

/* Scrollable Body with Gradient */
body {
    font-family: 'Poppins', sans-serif;
    background: linear-gradient(120deg, #ffecd2 0%, #fcb69f 100%);
    color: #333;
    line-height: 1.6;
    overflow-y: auto;
    margin: 0;
    padding: 0;
}

/* Blog Container */
.blog-container {
    background-color: #fff;
    padding: 40px;
    margin: 20px auto;
    border-radius: 10px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
    width: 95%;
    max-width: 1300px;
    border: 3px solid #ff6f00;
}

/* Blog Header */
.blog-header {
    text-align: center;
    margin-bottom: 20px;
}

.blog-header h3 {
    font-size: 3rem;
    color: #ff6f00;
    font-weight: bold;
    margin: 0;
    text-transform: uppercase;
}

.blog-header h3::after {
    content: '';
    display: block;
    width: 80px;
    height: 5px;
    background-color: #ff6f00;
    margin: 10px auto;
    border-radius: 3px;
}

/* Blog Details */
.blog-details {
    text-align: center;
    margin-bottom: 30px;
    font-size: 1.1rem;
    color: #555;
}

.blog-details p span {
    font-weight: bold;
    color: #ff6f00;
}

/* Blog Content */
.blog-content {
    margin-bottom: 30px;
}

.blog-content img {
    width: 100%;
    max-height: 600px;
    object-fit: cover;
    border-radius: 10px;
    margin-bottom: 20px;
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

.blog-content p {
    font-size: 1.3rem;
    line-height: 1.8;
    color: #444;
    text-align: justify;
}

/* Product Link */
.product-link {
    display: inline-block;
    margin: 20px 0;
    padding: 12px 25px;
    font-size: 1.2rem;
    color: #fff;
    background: linear-gradient(45deg, #ff6f00, #ff9000);
    border-radius: 6px;
    text-decoration: none;
    font-weight: bold;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
    transition: all 0.3s ease;
}

.product-link:hover {
    transform: translateY(-3px);
    box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
}

/* Like/Dislike Section */
.like-dislike {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin: 30px 0;
}

.like-dislike form {
    display: inline-block;
}

.like-dislike button {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 12px 30px;
    font-size: 1.2rem;
    color: #fff;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    font-weight: bold;
    transition: all 0.3s ease;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.like-dislike button:hover {
    transform: translateY(-3px);
    box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
}

.like-dislike .like {
    background: #ff6f00;
}

.like-dislike .dislike {
    background: #e53935;
}

/* Back Button */
.back-btn {
    display: inline-block;
    margin: 20px auto;
    font-size: 1.2rem;
    font-weight: bold;
    color: #ff6f00;
    text-decoration: none;
    transition: color 0.3s ease;
}

.back-btn:hover {
    color: #d35400;
}

/* Responsive Adjustments */
@media (max-width: 768px) {
    .blog-container {
        padding: 20px;
        width: 90%;
    }

    .blog-header h3 {
        font-size: 2rem;
    }

    .blog-content img {
        max-height: 300px;
    }

    .like-dislike {
        flex-direction: column;
        gap: 15px;
    }

    .like-dislike button {
        width: 100%;
    }
}
</style>
</head>
<body>
<%
BlogPosts post = (BlogPosts) request.getSession().getAttribute("post-data");
%>
<div class="blog-container">
    <!-- Blog Header -->
    <div class="blog-header">
        <h3><%= post.get_data("blog_title") %></h3>
    </div>

    <!-- Blog Details -->
    <div class="blog-details">
        <p><span>Author:</span> <%= post.get_data("client_name") %></p>
        <p><span>Posted on:</span> <%= post.get_data("upload_date") %></p>
    </div>

    <!-- Blog Content -->
    <div class="blog-content">
        <img src="Blog post images/<%= post.get_data("blog_img") %>" alt="Blog Image">
        <p><%= post.get_data("blog_content") %></p>
    </div>

    <!-- Product Link -->
    <% if (!post.get_data("link").equals("")) { %>
        <a href="<%= post.get_data("link") %>" class="product-link" target="_blank">View Product</a>
    <% } %>

    <!-- Like/Dislike Section -->
    <div class="like-dislike">
        <form action="BlogPostHandler?operation=do-like" method="post">
            <input type="hidden" name="client_id" value="<%= post.get_data("client_id") %>">
            <input type="hidden" name="blog_id" value="<%= post.get_data("blog_id") %>">
            <button class="like" type="submit">👍 <span id="like-count"><%= post.get_data("like_count") %></span></button>
        </form>
        <form action="BlogPostHandler?operation=do-dislike" method="post">
            <input type="hidden" name="client_id" value="<%= post.get_data("client_id") %>">
            <input type="hidden" name="blog_id" value="<%= post.get_data("blog_id") %>">
            <button class="dislike" type="submit">👎 <span id="dislike-count"><%= post.get_data("dislike_count") %></span></button>
        </form>
    </div>

    <!-- Back Button -->
    <a href="ClientFrontEndLoader?operation=blog-posts-all" class="back-btn">← Back to Blogs</a>
</div>

</body>
</html>
