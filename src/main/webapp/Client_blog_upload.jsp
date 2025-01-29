<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.File" %>
<%@ page import="com.entity.BlogPosts" %>
<%@ page import="jakarta.servlet.annotation.MultipartConfig" %>
<%@ page import="jakarta.servlet.http.Part" %>
<%if(request.getSession().getAttribute("Client_username")==null){response.sendRedirect("Client_login_signup.jsp");return;} %>
<%String msg=request.getParameter("msg"); %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Blog</title>
<style>
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

/* Blog Form Section */
.blog-form-section {
    background-color: #fff;
    padding: 60px;
    border-radius: 15px;
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.2);
    width: 90%; /* Maintains responsiveness */
    max-width: 1000px; /* Increased maximum width */
    box-sizing: border-box;
    animation: fadeIn 0.8s ease-in-out;
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

.blog-form-section h3 {
    font-size: 2.2rem;
    font-weight: bold;
    color: #ff8c00;
    margin-bottom: 25px;
    text-align: center;
    position: relative;
}

.blog-form-section h3:after {
    content: '';
    display: block;
    width: 70px;
    height: 4px;
    background-color: #ff8c00;
    margin: 12px auto 0;
    border-radius: 2px;
}

/* Form Styles */
.blog-form {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.blog-form label {
    font-size: 1.1rem;
    color: #555;
    font-weight: bold;
    margin-bottom: 8px;
}

.blog-form input,
.blog-form textarea {
    width: 100%;
    padding: 12px 16px;
    font-size: 1rem;
    border-radius: 10px;
    border: 1px solid #ddd;
    outline: none;
    transition: border-color 0.3s ease, box-shadow 0.3s ease;
    background-color: #fdfdfd;
}

.blog-form input:focus,
.blog-form textarea:focus {
    border-color: #ff8c00;
    box-shadow: 0 0 8px rgba(255, 140, 0, 0.5);
}

.blog-form textarea {
    height: 180px;
    resize: vertical;
}

/* Image Upload Section */
.image-upload {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
    background-color: #fff8e5;
    padding: 20px;
    border-radius: 10px;
    border: 1px dashed #ff8c00;
    text-align: center;
    position: relative;
}

.image-upload label {
    font-size: 1.1rem;
    font-weight: bold;
    color: #ff8c00;
    cursor: pointer;
    background-color: #fff;
    border: 1px solid #ff8c00;
    padding: 10px 20px;
    border-radius: 8px;
    transition: background-color 0.3s ease, color 0.3s ease, transform 0.2s ease;
}

.image-upload label:hover {
    background-color: #ff8c00;
    color: #fff;
    transform: scale(1.05);
}

.image-upload input {
    display: none;
}

.image-upload .preview {
    width: 100%;
    max-width: 200px;
    border-radius: 10px;
    margin-top: 10px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

/* Submit Button */
.blog-form button {
    padding: 16px;
    font-size: 1.3rem;
    font-weight: bold;
    color: #fff;
    background: linear-gradient(to right, #ff8c00, #ffb84d);
    border: none;
    border-radius: 10px;
    cursor: pointer;
    transition: background-color 0.3s ease, transform 0.2s ease;
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

.blog-form button:hover {
    background: linear-gradient(to right, #e65c00, #ff8c00);
    transform: scale(1.05);
}

/* Back Button */
.back-btn {
    display: block;
    margin-top: 25px;
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
.notice {
            background-color: #e74c3c;
            color: white;
            padding: 15px;
            margin-bottom: 10px;
            border-radius: 4px;
            text-align: center;
            font-weight: bold;
            width: 100%;
        }
</style>
<script>
function previewImage(event) {
    const imageInput = event.target;
    const preview = document.getElementById('image-preview');
    const file = imageInput.files[0];

    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
        };
        reader.readAsDataURL(file);
    }
}
</script>
</head>
<body>
<% 
BlogPosts post=(BlogPosts)request.getSession().getAttribute("edit-post-data");
if(post==null){
%>
<div class="blog-form-section">
    <h3>Post a Blog</h3>
    <%if(msg!=null){%>
    <div class="notice">
    	<%=msg%>
    </div>
    <%}%>
    <form class="blog-form" action="BlogPostHandler?operation=upload-post" method="post" enctype="multipart/form-data">

        <label for="title">Blog Title:</label>
        <input type="text" id="title" name="blog-title" placeholder="Enter your blog title" required>
        
        <label for="link">Product Link (Optional):</label>
        <input type="text" id="link" name="product-link" placeholder="You can give the link of the product you are writing about">

        <label>Upload Image:</label>
        <div class="image-upload">
            <label for="image">Choose Image</label>
            <input type="file" id="image" name="blog-image" accept=".jpg, .jpeg, .png, .jfif" onchange="previewImage(event)" required>
            <img id="image-preview" class="preview" style="display: none;" alt="Preview">
        </div>

        <label for="content">Content:</label>
        <textarea id="content" name="blog-content" placeholder="Write your blog content here..." required></textarea>

        <button type="submit">Post Blog</button>
    </form>
    <a href="ClientFrontEndLoader?operation=blog-posts-all" class="back-btn">← Back to Blogs</a>
    </div>
    <%}else{%>
<div class="blog-form-section">
    <h3>Update Your Blog</h3>
    <%if(msg!=null){%>
    <div class="notice">
    	<%=msg%>
    </div>
    <%}%>
    <form class="blog-form" action="BlogPostHandler?operation=update-post" method="post">
        <label for="title">Blog Title:</label>
        <input type="text" id="title" name="blog-title" value="<%=post.get_data("blog_title") %>" required>
        <%if(post.get_data("link").equals("")){ %>
        <label for="link">Product Link (Optional):</label>
        <input type="text" id="link" name="product-link" placeholder="You can give the link of the product you are writing about">
		<%}else{ %>
		<label for="link">Product Link (Optional):</label>
        <input type="text" id="link" name="product-link" value="<%=post.get_data("link")%>">
		<%} %>
        <label for="content">Content:</label>
        <textarea id="content" name="blog-content" required><%=post.get_data("blog_content") %></textarea>
        <input type="hidden" name="blog-id" value="<%=post.get_data("blog_id")%>">
        <button type="submit">Update Blog</button>
    </form>
</div>
    <%}post=null;request.getSession().removeAttribute("edit-post-data");%>
</body>
</html>
