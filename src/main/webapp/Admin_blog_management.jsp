<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.BlogPosts" %>
<%@ page import="java.util.ArrayList" %>
<%if(request.getSession().getAttribute("Admin-username")==null){response.sendRedirect("Admin_login_signup.jsp");return;} %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin Blog Management</title>
<style>
/* General Layout */
body {
    font-family: 'Roboto', Arial, sans-serif;
    margin: 0;
    padding: 0;
    background: linear-gradient(120deg, #000000 0%, #ff5722 100%);
    background-repeat: no-repeat; /* Ensure background doesn't repeat */
    background-attachment: fixed; /* Optional: Makes the background fixed during scroll */
    color: #ffffff;
    line-height: 1.6;
}

/* Blog Section */
.blog-section {
    margin: 40px auto;
    padding: 30px;
    background-color: #1c1c1c;
    border-radius: 12px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.5);
    max-width: 90%;
    box-sizing: border-box;
    overflow: hidden;
    animation: fadeIn 1s ease-in-out;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.blog-section h3 {
    font-size: 2.5rem;
    font-weight: 700;
    color: #ff5722;
    margin-bottom: 25px;
    border-bottom: 3px solid #ff5722;
    padding-bottom: 10px;
    text-align: center;
}

/* Search and Filter Section */
.search-section {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;
}

.search-box {
    display: flex;
    align-items: center;
    background-color: #1c1c1c;
    border: 2px solid #ff5722;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 3px 6px rgba(0, 0, 0, 0.5);
    width: 100%;
    max-width: 600px;
    transition: box-shadow 0.3s ease, border-color 0.3s ease;
}

.search-box:hover,
.search-box:focus-within {
    border-color: #e64a19;
    box-shadow: 0 0 8px rgba(230, 74, 25, 0.5);
}

.search-input {
    flex: 1;
    width: 400px;
    padding: 12px 15px;
    font-size: 1rem;
    border: none;
    outline: none;
    color: #ffffff;
    background-color: transparent;
}

.search-button {
    padding: 12px 20px;
    background: linear-gradient(45deg, #ff5722, #e64a19);
    color: #ffffff;
    border: none;
    cursor: pointer;
    font-size: 1rem;
    font-weight: bold;
    transition: all 0.3s ease;
}

.search-button:hover {
    background: linear-gradient(45deg, #e64a19, #ff5722);
}

/* Filter Section */
.custom-dropdown {
    position: relative;
    display: inline-block;
}

.dropdown-button {
    background-color: #ff5722;
    color: white;
    padding: 10px 20px;
    font-size: 1rem;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.dropdown-button:hover {
    background-color: #e64a19;
}

.dropdown-content {
    display: none;
    position: absolute;
    background-color: #333333;
    box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.8);
    border-radius: 8px;
    padding: 10px;
    z-index: 1;
    min-width: 200px;
}

.dropdown-content a {
    color: #ffffff;
    padding: 8px 12px;
    text-decoration: none;
    display: block;
    border-radius: 4px;
    transition: background-color 0.2s ease;
}

.dropdown-content a:hover {
    background-color: #444444;
}

.custom-dropdown:hover .dropdown-content {
    display: block;
}

/* Blog Grid */
.blog-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
    gap: 30px;
}

.blog-card {
    background-color: #333333;
    border-radius: 16px;
    box-shadow: 0 6px 15px rgba(0, 0, 0, 0.8);
    overflow: hidden;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    position: relative;
    padding-bottom: 20px;
}

.blog-card:hover {
    transform: translateY(-8px);
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.8);
}

.blog-image {
    width: 100%;
    max-height: 230px;
    object-fit: cover;
}

.blog-content {
    padding: 20px;
}

.blog-title {
    font-size: 1.8rem;
    font-weight: bold;
    color: #ff5722;
    margin-bottom: 10px;
}

.blog-excerpt {
    font-size: 1rem;
    color: #cccccc;
}

/* Likes and Dislikes Section */
.likes-dislikes {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    gap: 10px;
    margin-top: 10px;
    margin-left: 10px;
}

.likes-dislikes span {
    display: flex;
    align-items: center;
    font-size: 1rem;
    font-weight: bold;
    padding: 5px 15px;
    border-radius: 20px;
    transition: transform 0.2s ease-in-out;
    gap: 8px;
}

.likes-dislikes .likes-count {
    background-color: #e6f7e6; /* Greenish background for likes */
    color: #28a745; /* Green text color */
}

.likes-dislikes .dislikes-count {
    background-color: #fdecea; /* Reddish background for dislikes */
    color: #dc3545; /* Red text color */
}

/* Delete Button */
.delete-button {
    display: flex;
    margin: 20px;
    background-color: #dc3545;
    color: #ffffff;
    padding: 10px 20px;
    border: none;
    border-radius: 8px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s ease;
    text-align: center;
    max-width: 150px;
}

.delete-button:hover {
    background-color: #ff0000;
}
/* Dropdown for Search Type */
.search-type {
	width: 605px;
	margin-bottom: 10px;
	text-align: center;
    border: none;
    border-radius: 8px;
    padding: 12px 15px;
    background-color: #1c1c1c;
    color: #ffffff;
    font-size: 1rem;
    cursor: pointer;
    box-shadow: 0 3px 6px rgba(0, 0, 0, 0.5);
    transition: box-shadow 0.3s ease, border-color 0.3s ease;
}

.search-type:hover {
    box-shadow: 0 0 8px rgba(230, 74, 25, 0.5);
}

.search-type option {
    background-color: #333333;
    color: #ffffff;
}
/* Action Buttons Section */
.actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;
    gap: 10px;
    padding: 0 20px;
}

.action-form {
    margin: 0;
}

.view-product-button {
	margin-top: 10px;
	margin-left: 12px;
    display: inline-block;
    background-color: #0288d1;
    color: #ffffff;
    text-decoration: none;
    padding: 10px 20px;
    font-size: 1rem;
    border-radius: 8px;
    text-align: center;
    font-weight: bold;
    transition: background-color 0.3s ease, transform 0.2s ease-in-out;
}

.view-product-button:hover {
    background-color: #0277bd;
    transform: translateY(-2px);
}

/* Responsive Design */
@media (max-width: 768px) {
    .blog-grid {
        grid-template-columns: 1fr;
    }
}
</style>
</head>
<body>
<div class="blog-section">
    <h3>Manage Blog Posts</h3>
	
	<!-- Search and Filter Section -->
    <div class="search-section">
        <!-- Dropdown -->
        <div class="custom-dropdown">
            <button class="dropdown-button">Filter Options</button>
            <div class="dropdown-content">
                <a href="AdminOperationsHandler?request-type=filter-blogs&filter-option=new">New Posts</a>
                <a href="AdminOperationsHandler?request-type=filter-blogs&filter-option=old">Old Posts</a>
                <a href="AdminOperationsHandler?request-type=filter-blogs&filter-option=most-liked">Most Liked</a>
                <a href="AdminOperationsHandler?request-type=filter-blogs&filter-option=most-disliked">Most Disliked</a>
            </div>
        </div>
        <!-- Search Box -->
        <form action="AdminOperationsHandler" method="get">
            <div class="search-box-container">
                <select class="search-type" name="search-type">
                    <option value="normal-search">Search by Title, Author, or Content</option>
                    <option value="product-link-search">Enter Product Link to Find Related Blogs</option>
                </select>
                <div class="search-box">
                    <input class="search-input" type="text" placeholder="Search for a blog post..." name="search-text">
                    <input type="hidden" value="search-blogs" name="request-type">
                    <button class="search-button" type="submit">Search</button>
                </div>
            </div>
        </form>
    </div>
	
    <div class="blog-grid">
        <%
        @SuppressWarnings("unchecked")
        ArrayList<BlogPosts> posts=(ArrayList<BlogPosts>)request.getSession().getAttribute("all-blog-data");
        if(posts==null || posts.isEmpty()){%>
        <div>
            <h2>No Blog Posts found.</h2>
        </div>
        <%}else{for(BlogPosts post:posts){%>
        <div class="blog-card">
            <img class="blog-image" src="Blog post images/<%=post.get_data("blog_img") %>" alt="Blog Image">
            <div class="blog-content">
                <p>Author: <%=post.get_data("client_name") %></p>
                <p class="blog-title"><%=post.get_data("blog_title") %></p>
                <p class="blog-excerpt"><%=post.get_data("blog_content") %></p>
            </div>
            <div class="likes-dislikes">
                <span class="likes-count">👍 <%=post.get_data("like_count") %></span>
                <span class="dislikes-count">👎 <%=post.get_data("dislike_count") %></span>
            </div>
            <% if (!post.get_data("link").equals("")) { %>
        		<a href="<%= post.get_data("link") %>" class="view-product-button" target="_blank">View Product</a>
    		<%}%>
            <form action="AdminOperationsHandler?form-request-type=delete-blog" method="post">
                <input type="hidden" name="blog-id" value="<%=post.get_data("blog_id")%>">
                <button type="submit" class="delete-button">Delete Post</button>
            </form>
            
        </div>
        <%}posts.clear();posts=null;request.getSession().removeAttribute("all-blog-data");}%>
    </div>
</div>
</body>
</html>
