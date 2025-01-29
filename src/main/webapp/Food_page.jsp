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
    <title>Food - Vendor Dashboard</title>
    <style>
        /* Theme Styles */
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

        .food-section {
            padding: 40px 20px;
            flex: 1;
        }

        .food-section h4 {
            font-size: 2em;
            margin-bottom: 20px;
            text-align: center;
            color: #ff8c00;
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
        .upload-form input[type="date"],
        .upload-form input[type="file"],
        .upload-form textarea,
        .upload-form select {
            width: 100%;
            padding: 10px;
            background-color: #333;
            border: 1px solid #555;
            color: #fff;
            border-radius: 5px;
            transition: border-color 0.3s ease-in-out;
        }

        .upload-form input[type="text"]:focus,
        .upload-form input[type="number"]:focus,
        .upload-form input[type="date"]:focus,
        .upload-form input[type="file"]:focus,
        .upload-form textarea:focus,
        .upload-form select:focus {
            border-color: #ff8c00;
            outline: none;
        }

        .upload-form textarea {
            resize: vertical;
            grid-column: span 2;
        }

        .form-row {
            display: flex;
            justify-content: space-between;
            flex-wrap: wrap;
            grid-column: span 2;
        }

        .form-group {
            flex: 1;
            margin-right: 20px;
        }

        .form-group:last-child {
            margin-right: 0;
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

        .radio {
            margin-bottom: 20px;
        }

        .meat-type {
            grid-column: span 2;
        }

        .footer {
            background-color: #000;
            color: #ff8c00;
            text-align: center;
            padding: 20px 0;
            margin-top: auto;
        }

        .footer p {
            margin: 0;
        }
        .upload-form .full-width {
            grid-column: span 2;
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
<div class="navbar">
    <h1>Upload Food Product</h1>
    <ul>
        <li><a href="Vendor_home_page.jsp">Home</a></li>
    </ul>
</div>

<div class="container">
    <div class="food-section">

        <form enctype="multipart/form-data" class="upload-form" action="ProductsUploadHandler" method="post">
        <%if(msg!=null){ %>
        <div role="alert" id="notice" class="full-width"><%=msg%></div>
        <%} %>
        <input type="hidden" name="type" value="food">
            <div class="form-row">
                <div class="form-group">
                    <label for="product-name">Product Name</label>
                    <input type="text" id="product-name" name="food-product-name" placeholder="Enter product name" required>
                </div>
                
            <div class="form-group">
                <label for="brand-name">Brand Name</label>
                <input type="text" id="brand-name" name="food-product-brand-name" placeholder="Enter product name" required>
            </div>
                
                <div class="form-group">
                    <label for="original-price">Original Price</label>
                    <input type="number" id="original-price" name="food-original-price" placeholder="Enter original price" required>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="selling-price">Selling Price</label>
                    <input type="number" id="selling-price" name="food-selling-price" placeholder="Enter selling price" required>
                </div>
                <div class="form-group">
                    <label for="quantity">Quantity</label>
                    <input type="number" id="quantity" name="food-product-quantity" placeholder="Enter quantity" required>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="shelf-life">Shelf Life (Number of Months/ Days/ Weeks)</label>
                    <input type="text" placeholder="12 Months, 1 Year, 30 Days. etc" id="shelf-life" name="food-shelf-life" required>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="keywords">Keywords (Comma separated)</label>
                    <input type="text" id="keywords" name="food-product-keywords" placeholder="Enter keywords" required>
                </div>
            </div>

			<div class="full-width">
            <label for="product-description">Product Description</label>
            <textarea id="product-description" name="food-product-description" placeholder="Enter product description" rows="3" required></textarea>
			</div>
			
            <div>
                <label for="perishable">Is the product perishable?</label>
                <select id="perishable" name="food-product-perishable" required>
                    <option value="">Select</option>
                    <option value="true">Yes</option>
                    <option value="false">No</option>
                </select>
            </div>

            <div>
                <label for="veg">Is the product vegetarian?</label>
                <select id="veg" name="food-product-veg" onchange="toggleMeatType()" required>
                    <option value="">Select</option>
                    <option value="true">Yes</option>
                    <option value="false">No</option>
                </select>
            </div>

            <div class="meat-type form-group" style="display:none;">
                <label for="meat-type">Type of Meat Used</label>
                <input type="text" id="meat-type" name="food-meat-type" placeholder="Enter type of meat (e.g., chicken, beef, etc.)">
            </div>
				
			<div class="full-width">
            <label for="product-images">Upload Images (5 maximum)</label>
                <input class="file" type="file" id="product-images" name="food-product-image1" accept=".jpg, .jpeg, .png, .jfif" required>
                <input class="file" type="file" id="product-images" name="food-product-image2" accept=".jpg, .jpeg, .png, .jfif" required>
                <input class="file" type="file" id="product-images" name="food-product-image3" accept=".jpg, .jpeg, .png, .jfif" required>
                <input class="file" type="file" id="product-images" name="food-product-image4" accept=".jpg, .jpeg, .png, .jfif" required>
                <input type="file" id="product-images" name="food-product-image5" accept=".jpg, .jpeg, .png, .jfif" required>
            </div>

            <div class="refund-replace-section">
                <label>Refund/Replace Option:</label><br>
                <label><input type="radio" name="food-refund-replace" value="refundable" class="radio" required> Refundable</label>
                <label><input type="radio" name="food-refund-replace" value="replaceable" class="radio" required> Replaceable</label>
                <label><input type="radio" name="food-refund-replace" value="both" required> Both</label>
            </div>

            <button type="submit" id="upload">Upload Product</button>
        </form>
    </div>
</div>

<script>
    function toggleMeatType() {
        const meatTypeDiv = document.querySelector('.meat-type');
        if (document.getElementById('veg').value === 'false') {
            meatTypeDiv.style.display = 'block';
        } else {
            meatTypeDiv.style.display = 'none';
        }
    }
</script>
</body>
</html>
