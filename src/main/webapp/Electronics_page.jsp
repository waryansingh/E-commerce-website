<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.File" %>
<%@ page import="jakarta.servlet.annotation.MultipartConfig" %>
<%@ page import="jakarta.servlet.http.Part" %>
<%if(request.getSession().getAttribute("Vendor-username")==null){response.sendRedirect("Vendor_login_signup.jsp");return;}
String msg=request.getParameter("msg");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Electronics & Accessories - Vendor Dashboard</title>
    <style>
        /* Global Styles */
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
            background-color: #1e1e1e;
            color: #fff;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            background: linear-gradient(135deg, #ff8c00, #000);
        }

        .container {
            width: 80%;
            margin: 20px auto;
            flex: 1;
        }

        .navbar {
            background-color: #000;
            padding: 10px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .navbar h1 {
            color: #ff8c00;
            margin: 0;
        }

        .navbar ul {
            list-style: none;
            margin: 0;
            padding: 0;
            display: flex;
        }

        .navbar ul li {
            margin-left: 20px;
        }

        .navbar ul li a {
            color: #ff8c00;
            font-weight: bold;
            text-decoration: none;
            transition: color 0.3s;
        }

        .navbar ul li a:hover {
            color: #ff5719;
        }

        .electronics-section {
            padding: 40px 20px;
        }

        .electronics-section h4 {
            font-size: 2em;
            margin-bottom: 20px;
            text-align: center;
            color: #000; /* Changed to black */
        }

        .upload-form {
            background-color: #2c2c2c;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
            max-width: 800px;
            margin: auto;
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .upload-form label {
            color: #ff8c00;
            font-weight: bold;
            display: block;
            margin-bottom: 10px;
        }

        .upload-form input[type="text"],
        .upload-form input[type="number"],
        .upload-form input[type="file"],
        .upload-form textarea {
            width: 100%;
            padding: 10px;
            background-color: #333;
            border: 1px solid #555;
            color: #fff;
            border-radius: 5px;
        }

        .upload-form textarea {
            resize: vertical;
            grid-column: span 2;
        }

        .upload-form .full-width {
            grid-column: span 2;
        }

        .refund-replace-section {
            grid-column: span 2;
            margin-bottom: 20px;
        }

        .upload-form button {
            background-color: #ff8c00;
            border: none;
            padding: 15px;
            color: #000;
            font-size: 1em;
            cursor: pointer;
            border-radius: 5px;
            width: 100%;
            transition: background-color 0.3s;
            grid-column: span 2;
        }

        .upload-form button:hover {
            background-color: #ff5719;
        }
        .footer {
            background-color: #000;
            color: #ff8c00;
            text-align: center;
            padding: 20px 0;
        }

        .footer p {
            margin: 0;
        }
        .file{
        	margin-bottom:10px;
        }
        #notice {
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
</head>
<body>

<!-- Navigation Bar -->
<div class="navbar">
    <h1>Electronics & Accessories</h1>
    <ul>
        <li><a href="Vendor_home_page.jsp">Home</a></li>
    </ul>
</div>

<!-- Electronics Section -->
<div class="container">
    <div class="electronics-section">
        <form enctype="multipart/form-data" class="upload-form" action="ProductsUploadHandler" method="post">
        <%if(msg!=null){ %>
        <div role="alert" id="notice" class="full-width"><%=msg%></div>
        <%} %>
        <input type="hidden" name="type" value="electronics">
            <div>
                <label for="product-name">Product Name</label>
                <input type="text" id="product-name" name="electronic-product-name" placeholder="Enter product name" required>
            </div>
            
            <div >
                <label for="brand-name">Brand Name</label>
                <input type="text" id="brand-name" name="electronic-product-brand-name" placeholder="Enter product name" required>
            </div>

            <div>
                <label for="original-price">Original Price</label>
                <input type="number" id="original-price" name="electronic-original-price" placeholder="Enter original price" required>
            </div>

            <div>
                <label for="selling-price">Selling Price</label>
                <input type="number" id="selling-price" name="electronic-selling-price" placeholder="Enter selling price" required>
            </div>

            <div>
                <label for="quantity">Quantity Available</label>
                <input type="number" id="quantity" name="electronic-product-quantity" placeholder="Enter product quantity" required>
            </div>

            <div class="full-width">
                <label for="keywords">Keywords (Comma separated)</label>
                <textarea type="text" id="keywords" name="electronic-product-keywords" placeholder="Enter product keywords (comma separated)" required></textarea>
            </div>

            <div class="full-width">
                <label for="product-description">Product Description</label>
                <textarea id="product-description" name="electronic-product-description" placeholder="Enter product description" rows="4" required></textarea>
            </div>

            <div class="full-width">
                <label for="technical-description">Technical Description</label>
                <textarea id="technical-description" name="electronic-technical-description" placeholder="Enter technical details (e.g., specifications, features)" rows="4" required></textarea>
            </div>

            <div class="full-width">
                <label for="product-images">Upload Images (5)</label>
                <input class="file" type="file" id="product-images" name="electronic-product-image1" accept=".jpg, .jpeg, .png, .jfif" required>
                <input class="file" type="file" id="product-images" name="electronic-product-image2" accept=".jpg, .jpeg, .png, .jfif" required>
                <input class="file" type="file" id="product-images" name="electronic-product-image3" accept=".jpg, .jpeg, .png, .jfif" required>
                <input class="file" type="file" id="product-images" name="electronic-product-image4" accept=".jpg, .jpeg, .png, .jfif" required>
                <input type="file" id="product-images" name="electronic-product-image5" accept=".jpg, .jpeg, .png, .jfif" required>
            </div>

            <div class="refund-replace-section">
                <label>Refund/Replace Option:</label><br>
                <label><input type="radio" name="electronic-refund-replace" value="refundable" required> Refundable</label>
                <label><input type="radio" name="electronic-refund-replace" value="replaceable" required> Replaceable</label>
                <label><input type="radio" name="electronic-refund-replace" value="both" required> Both</label>
            </div>

            <button type="submit" id="upload">Upload Product</button>
        </form>
    </div>
</div>

</body>
</html>
