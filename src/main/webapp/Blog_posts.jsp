<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.BlogPosts" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Blog Posts</title>
<style>
/* General Layout */
body {
    font-family: 'Roboto', Arial, sans-serif;
    margin: 0;
    padding: 0;
    background: linear-gradient(120deg, #ffecd2 0%, #fcb69f 100%);
    background-repeat: no-repeat; /* Ensure background doesn't repeat */
    background-attachment: fixed; /* Optional: Makes the background fixed during scroll */
    color: #333;
    line-height: 1.6;
}

/* Blog Section */
.blog-section {
    margin: 40px auto;
    padding: 30px;
    background-color: #ffffff;
    border-radius: 12px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
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

/* Search Section */
.search-section {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 30px;
}

.search-box {
    display: flex;
    align-items: center;
    background-color: #fff;
    border: 2px solid #ddd;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1);
    transition: box-shadow 0.3s ease, border-color 0.3s ease;
    width: 100%;
    max-width: 600px;
}

.search-box:hover,
.search-box:focus-within {
    border-color: #ff5722;
    box-shadow: 0 0 8px rgba(255, 87, 34, 0.5);
}

.search-input {
    flex: 1;
    padding: 12px 15px;
    font-size: 1rem;
    border: none;
    outline: none;
    color: #333;
}

.search-button {
    padding: 12px 20px;
    background: linear-gradient(45deg, #ff5722, #ff7043);
    color: #ffffff;
    border: none;
    cursor: pointer;
    font-size: 1rem;
    font-weight: bold;
    transition: all 0.3s ease;
}

.search-button:hover {
    background: linear-gradient(45deg, #ff7043, #ff5722);
}

/* Filter Section */
.custom-dropdown {
	margin-right: 20px;
    position: relative;
    display: inline-block;
}

.dropdown-button {
	margin-top: 52px;
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
    background-color: #ffffff;
    box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2);
    border-radius: 8px;
    padding: 10px;
    z-index: 1;
    min-width: 200px;
}

.dropdown-content a {
    color: #333;
    padding: 8px 12px;
    text-decoration: none;
    display: block;
    border-radius: 4px;
    transition: background-color 0.2s ease;
}

.dropdown-content a:hover {
    background-color: #f5f5f5;
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
	width: 100%;
    background-color: #fff;
    border-radius: 16px;
    box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    position: relative;
}

.blog-card:hover {
	cursor:pointer;
    transform: translateY(-8px);
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.2);
}

.blog-image {
    width: 100%;
    height: 230px;
    object-fit: cover;
    transition: transform 0.3s ease;
}

.blog-card:hover .blog-image {
    transform: scale(1.05);
}

.blog-content {
    padding: 20px;
}

.blog-title {
    font-size: 1.8rem;
    font-weight: bold;
    color: #333;
    margin-bottom: 10px;
    transition: color 0.3s ease;
}

.blog-card:hover .blog-title {
    color: #ff5722;
}

.blog-excerpt {
    font-size: 1rem;
    color: #666;
}

/* Add New Post Button */
.add-post {
    position: fixed;
    bottom: 30px;
    right: 30px;
    z-index: 100;
}

.add-post-btn {
    width: 70px;
    height: 70px;
    background: linear-gradient(45deg, #ff5722, #ff7043);
    color: #ffffff;
    font-size: 2.5rem;
    font-weight: bold;
    border: none;
    border-radius: 50%;
    cursor: pointer;
    transition: all 0.3s ease;
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.add-post-btn:hover {
    transform: scale(1.2);
    background: linear-gradient(45deg, #ff7043, #ff5722);
}

/* Responsive Design */
@media (max-width: 768px) {
    .blog-grid {
        grid-template-columns: 1fr;
    }

    .filter-section {
        flex-direction: column;
        gap: 15px;
    }
}
/* Likes and Dislikes Section */
.likes-dislikes {
	margin-left:10px;
	margin-bottom:10px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    gap: 10px; /* Space between likes and dislikes */
    margin-top: 10px; /* Adjust as needed for spacing from other elements */
}
.likes-dislikes:hover{
	cursor:pointer;
}

.likes-dislikes .likes-count,
.likes-dislikes .dislikes-count {
    display: flex;
    align-items: center;
    font-size: 1rem;
    font-weight: bold;
    color: #333;
    background-color: #fff9f3; /* Light background for emphasis */
    padding: 5px 15px;
    border-radius: 20px; /* Circular edges */
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Subtle shadow for depth */
    gap: 8px; /* Space between the emoji and the count */
    transition: transform 0.2s ease-in-out;
}

.likes-dislikes .likes-count {
    background-color: #e6f7e6; /* Greenish background for likes */
    color: #28a745; /* Green text color */
}

.likes-dislikes .dislikes-count {
    background-color: #fdecea; /* Reddish background for dislikes */
    color: #dc3545; /* Red text color */
}
.search-type {
	width: 605px;
	margin-bottom: 10px;
	text-align: center;
    border: none;
    border-radius: 8px;
    padding: 12px 15px;
    background-color: white;
    color: black;
    font-size: 1rem;
    cursor: pointer;
    box-shadow: 0 3px 6px rgba(0, 0, 0, 0.5);
    transition: box-shadow 0.3s ease, border-color 0.3s ease;
}

.search-type:hover {
    box-shadow: 0 0 8px rgba(230, 74, 25, 0.5);
}

.search-type option {
    background-color: white;
    color: black;
}

</style>
</head>
<body>
<div class="blog-section">
    <h3>Blog Posts</h3>

    <div class="search-section">
    <div class="custom-dropdown">
    <button class="dropdown-button">Filter Options</button>
    <div class="dropdown-content">
        <a href="ClientFrontEndLoader?operation=filter-blogs&filter-option=new">New Posts</a>
        <a href="ClientFrontEndLoader?operation=filter-blogs&filter-option=old">Old Posts</a>
        <a href="ClientFrontEndLoader?operation=filter-blogs&filter-option=most-liked">Most Liked</a>
        <a href="ClientFrontEndLoader?operation=filter-blogs&filter-option=most-disliked">Most Disliked</a>
    </div>
	</div>
		<form action="ClientFrontEndLoader" method="get">
		<select class="search-type" name="search-type">
            <option value="normal-search">Search by Title, Author, or Content</option>
        	<option value="product-link-search">Enter Product Link to Find Related Blogs</option>
        </select>
        <div class="search-box">
            <input class="search-input" type="text" placeholder="Search for a blog post..." name="search-text">
            <input type="hidden" value="search-blogs" name="operation">
            <button class="search-button" type="submit">Search</button>
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
        <%}else{int i=0;
        	for(BlogPosts post:posts){%>
        <form id="load-post<%=i%>" action="ClientFrontEndLoader" method="get">
        <div class="blog-card" onclick="submitForm('load-post<%=i%>')">
            <img class="blog-image" src="Blog post images/<%=post.get_data("blog_img") %>" alt="Blog Image">
            <div class="blog-content">
            	<p>Author: <%=post.get_data("client_name") %></p>
                <p class="blog-title"><%=post.get_data("blog_title") %></p>
                <p class="blog-excerpt"><%=post.get_data("blog_content") %></p>
            </div>
            <div class="likes-dislikes">
    <span class="likes-count">
        👍 <%=post.get_data("like_count") %>
    </span>
    <span class="dislikes-count">
        👎 <%=post.get_data("dislike_count") %>
    </span>
</div>
        </div>
        <input type="hidden" name="operation" value="load-post">
        <input type="hidden" name="blog-id" value="<%=post.get_data("blog_id")%>">
        </form>
		<%i++;}posts.clear();posts=null;request.getSession().removeAttribute("all-blog-data");}%>
    </div>
</div>

<div class="add-post">
    <button class="add-post-btn" onclick="location.href='Client_blog_upload.jsp'">+</button>
</div>
</body>
<script>
function submitForm(form) {
    document.getElementById(form).submit();
}
</script>
</html>