<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.annotation.MultipartConfig" %>
<%@ page import="jakarta.servlet.http.Part" %>
<%@ page import="com.entity.AllProducts" %>
<%
if(request.getSession().getAttribute("Vendor-username")==null){response.sendRedirect("error.jsp");return;}
String msg=(String)request.getParameter("msg");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Product</title>
    <style>
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
            justify-content: center;
            align-items: center;
            height: 100vh;
            background: linear-gradient(135deg, #ff8c00, #000);
        }

        .container {
            width: 1000px; /* Increased width */
            height: 90%; /* Fixed height to make it scrollable */
            background-color: #2c2c2c;
            border-radius: 15px;
            overflow-y: auto; /* Scrollable container */
            position: relative;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
            padding: 40px;
        }

        h2 {
            color: #ff8c00;
            margin-bottom: 30px;
            text-align: center;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            color: #ff8c00;
            margin-bottom: 5px;
            display: block;
        }

        input[type="text"],
        input[type="number"],
        input[type="file"],
        select,
        textarea {
            width: 100%;
            padding: 15px;
            margin: 10px 0;
            background-color: #333;
            border: 1px solid #555;
            color: #fff;
            border-radius: 5px;
        }

        textarea {
            resize: vertical;
        }

        .btn {
            width: 100%;
            padding: 15px;
            background-color: #ff8c00;
            color: #000;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            margin-top: 20px;
        }

        .btn:hover {
            background-color: #ff5719;
        }

        .checkbox-group {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }

        .checkbox-group input {
            margin-right: 10px;
        }

        .form-row {
            display: flex;
            justify-content: space-between;
        }

        .form-row .form-group {
            width: 48%;
        }

        .image-upload-section {
            margin-top: 20px;
            display: none; /* Hidden by default */
        }

        .checkbox-section {
            margin-top: 30px;
        }

        /* Show the form when checkbox is checked */
        input[type="checkbox"]:checked ~ .image-upload-section {
            display: block;
        }

        input[type="checkbox"]:checked ~ .edit-section {
            display: block;
        }

        .edit-section {
            display: block;
        }
        .notice {
            background-color: #e74c3c;
            color: white;
            padding: 15px;
            margin:auto;
            border-radius: 4px;
            text-align: center;
            font-weight: bold;
            width: 100%;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Edit Product Details</h2>
        <%if(msg!=null){%>
                <div class="notice">
                    <%=msg%>
                </div>
        	<%}%>
        <%if(request.getSession().getAttribute("product") instanceof AllProducts.Clothing){AllProducts.Clothing product=(AllProducts.Clothing)request.getSession().getAttribute("product");%>
        	<form action="VendorOperationsHandler?product-type=<%=product.get_data("product_type") %>" method="POST" enctype="multipart/form-data">
            <div class="edit-section">
                <div class="form-group">
                    <label for="productName">Name:</label>
                    <input type="text" id="productName" value="<%=product.get_data("product_name") %>" name="clothing-product-name">
                </div>
                
                <div class="form-group">
                <label for="brand-name">Brand Name</label>
                <input type="text" id="brand-name" value="<%=product.get_data("product_brand_name") %>" name="clothing-product-brand-name">
            	</div>
                
				<input type="hidden" value="<%=product.get_data("product_id") %>" name="clothing-product-id">
				
                <div class="form-group">
                    <label for="originalPrice">Original Price:</label>
                    <input type="number" id="originalPrice" value="<%=product.get_data("product_original_price") %>" name="clothing-product-original-price" step="0.01">
                </div>

                <div class="form-group">
                    <label for="sellingPrice">Selling Price:</label>
                    <input type="number" id="sellingPrice" value="<%=product.get_data("product_selling_price") %>" name="clothing-product-selling-price" step="0.01">
                </div>
                
                <div class="form-group">
                    <label for="productQuantity">Quantity:</label>
                    <input type="number" id="productQuantity" value="<%=product.get_data("product_quantity") %>" name="clothing-product-quantity" min="1">
                </div>

                <div class="form-group">
                    <label for="keywords">Keywords (comma-separated):</label>
                    <textarea id="keywords" name="clothing-product-keywords"><%=product.get_data("product_keywords") %></textarea>
                </div>

                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea id="description" name="clothing-product-description" rows="4"><%=product.get_data("product_description") %></textarea>
                </div>
                
                <div class="form-group">
                <label>Refund/Replace Option:</label><br>
                <%if(product.get_data("product_refund_replace_option").equals("refundable")){%>
                <label><input type="radio" name="clothing-product-refund-replace" value="refundable" class="radio" required checked> Refundable</label>
                <%}else{%>
                <label><input type="radio" name="clothing-product-refund-replace" value="refundable" class="radio" required> Refundable</label>
                <%}%>
                
                <%if(product.get_data("product_refund_replace_option").equals("replaceable")){%>
                <label><input type="radio" name="clothing-product-refund-replace" value="replaceable" class="radio" required checked> Replaceable</label>
                <%}else{%>
                <label><input type="radio" name="clothing-product-refund-replace" value="replaceable" class="radio" required> Replaceable</label>
                <%}%>
                
                <%if(product.get_data("product_refund_replace_option").equals("both")){ %>
                <label><input type="radio" name="clothing-product-refund-replace" value="both" required checked> Both</label>
                <%}else{%>
                <label><input type="radio" name="clothing-product-refund-replace" value="both" required> Both</label>
                <%} %>
            	</div>
            	
            <label>Available Sizes:</label>
            <div class="edit-section">
            <%if(product.get_data("product_size1")!=null && product.get_data("product_size1").equals("S")){ %>
                <label><input type="checkbox" name="clothing-size1" value="S" checked> S</label>
            <%}else{%>
            	<label><input type="checkbox" name="clothing-size1" value="S"> S</label>
            <%}if(product.get_data("product_size2")!=null && product.get_data("product_size2").equals("M")){%>
            	<label><input type="checkbox" name="clothing-size2" value="M" checked> M</label>
            <%}else{%>
            	<label><input type="checkbox" name="clothing-size2" value="M"> M</label>
            <%}if(product.get_data("product_size3")!=null && product.get_data("product_size3").equals("L")){%>
            	<label><input type="checkbox" name="clothing-size3" value="L" checked> L</label>
            <%}else{%>
            	<label><input type="checkbox" name="clothing-size3" value="L"> L</label>
            <%}if(product.get_data("product_size4")!=null && product.get_data("product_size4").equals("XL")){%>
            	<label><input type="checkbox" name="clothing-size4" value="XL" checked> XL</label>
            <%}else{%>
            	<label><input type="checkbox" name="clothing-size4" value="XL"> XL</label>
            <%}if(product.get_data("product_size5")!=null && product.get_data("product_size5").equals("XXL")){ %>
                <label><input type="checkbox" name="clothing-size5" value="XXL" checked> XXL</label>
            <%}else{%>
            	<label><input type="checkbox" name="clothing-size5" value="XXL" > XXL</label>
            <%}%>
            </div>
            </div>
			<div class="checkbox-section">
                <div class="checkbox-group">
                    <input type="checkbox" id="editProduct" name="product-edit-withImage">
                    <label for="editProduct">Apply Changes Along With Images</label>
                </div>
            </div>
            
                <h3>Upload Images:</h3>
                <div>
                <input type="file" id="product-images" name="clothing-product-image1" accept=".jpg, .jpeg, .png, .jfif">
                <input type="file" id="product-images" name="clothing-product-image2" accept=".jpg, .jpeg, .png, .jfif">
                <input type="file" id="product-images" name="clothing-product-image3" accept=".jpg, .jpeg, .png, .jfif">
                <input type="file" id="product-images" name="clothing-product-image4" accept=".jpg, .jpeg, .png, .jfif">
                <input type="file" id="product-images" name="clothing-product-image5" accept=".jpg, .jpeg, .png, .jfif">    
                </div>

            <div class="form-group">
                <button type="submit" class="btn">Save Changes</button>
            </div>
        </form>
        	<%}else if(request.getSession().getAttribute("product") instanceof AllProducts.Electronics){AllProducts.Electronics product=(AllProducts.Electronics)request.getSession().getAttribute("product");%>
        	<form action="VendorOperationsHandler?product-type=<%=product.get_data("product_type") %>" method="post" enctype="multipart/form-data">
            <div class="edit-section">
                <div class="form-group">
                    <label for="productName">Name:</label>
                    <input type="text" id="productName" value="<%=product.get_data("product_name") %>" name="electronics-product-name">
                </div>
                
                <div class="form-group">
                <label for="brand-name">Brand Name</label>
                <input type="text" id="brand-name" value="<%=product.get_data("product_brand_name") %>" name="electronics-product-brand-name">
            	</div>

				<input type="hidden" value="<%=product.get_data("product_id") %>" name="electronics-product-id">
				<input type="hidden" value="<%=product.get_data("product_type") %>" name="product-type">

                <div class="form-group">
                    <label for="originalPrice">Original Price:</label>
                    <input type="number" id="originalPrice" value="<%=product.get_data("product_original_price") %>" name="electronics-product-original-price" step="0.1">
                </div>

                <div class="form-group">
                    <label for="sellingPrice">Selling Price:</label>
                    <input type="number" id="sellingPrice" value="<%=product.get_data("product_selling_price") %>" name="electronics-product-selling-price" step="0.1">
                </div>
                
                <div class="form-group">
                    <label for="productQuantity">Quantity:</label>
                    <input type="number" id="productQuantity" value="<%=product.get_data("product_quantity") %>" name="electronics-product-quantity" min="1">
                </div>

                <div class="form-group">
                    <label for="keywords">Keywords (comma-separated):</label>
                    <textarea id="keywords" name="electronics-product-keywords"><%=product.get_data("product_keywords") %></textarea>
                </div>

                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea id="description" name="electronics-product-description" rows="4"><%=product.get_data("product_description") %></textarea>
                </div>
                
                <div class="form-group">
                    <label for="technical-description">Technical Description:</label>
                    <textarea id="description" name="electronics-product-technical-description" rows="4"><%=product.get_data("product_technical_description") %></textarea>
                </div>
                
            <div class="form-group">
                <label>Refund/Replace Option:</label><br>
                <%if(product.get_data("product_refund_replace_option").equals("refundable")){%>
                <label><input type="radio" name="electronics-product-refund-replace" value="refundable" class="radio" required checked> Refundable</label>
                <%}else{%>
                <label><input type="radio" name="electronics-product-refund-replace" value="refundable" class="radio" required> Refundable</label>
                <%}%>
                
                <%if(product.get_data("product_refund_replace_option").equals("replaceable")){%>
                <label><input type="radio" name="electronics-product-refund-replace" value="replaceable" class="radio" required checked> Replaceable</label>
                <%}else{%>
                <label><input type="radio" name="electronics-product-refund-replace" value="replaceable" class="radio" required> Replaceable</label>
                <%}%>
                
                <%if(product.get_data("product_refund_replace_option").equals("both")){ %>
                <label><input type="radio" name="electronics-product-refund-replace" value="both" required checked> Both</label>
                <%}else{%>
                <label><input type="radio" name="electronics-product-refund-replace" value="both" required> Both</label>
                <%} %>
            </div>
            
            </div>
			<div class="checkbox-section">
                <div class="checkbox-group">
                    <input type="checkbox" id="editProduct" name="product-edit-withImage">
                    <label for="editProduct">Apply Changes Along With Images</label>
                </div>
            </div>
            
                <h3>Upload Images:</h3>
                <div>
                <input type="file" id="product-images" name="electronics-product-image1" accept=".jpg, .jpeg, .png, .jfif">
                <input type="file" id="product-images" name="electronics-product-image2" accept=".jpg, .jpeg, .png, .jfif">
                <input type="file" id="product-images" name="electronics-product-image3" accept=".jpg, .jpeg, .png, .jfif">
                <input type="file" id="product-images" name="electronics-product-image4" accept=".jpg, .jpeg, .png, .jfif">
                <input type="file" id="product-images" name="electronics-product-image5" accept=".jpg, .jpeg, .png, .jfif">    
                </div>
            

            <div class="form-group">
                <button type="submit" class="btn">Save Changes</button>
            </div>
        </form>
        	<%}else if(request.getSession().getAttribute("product") instanceof AllProducts.Food){AllProducts.Food product=(AllProducts.Food)request.getSession().getAttribute("product");%>
        <form action="VendorOperationsHandler?product-type=<%=product.get_data("product_type") %>" method="post" enctype="multipart/form-data">
            <div class="edit-section">
                <div class="form-group">
                    <label for="productName">Name:</label>
                    <input type="text" id="productName" value="<%=product.get_data("product_name") %>" name="food-product-name">
                </div>
                
                <div class="form-group">
                <label for="brand-name">Brand Name</label>
                <input type="text" id="brand-name" value="<%=product.get_data("product_brand_name") %>" name="food-product-brand-name">
            	</div>
                
                <input type="hidden" value="<%=product.get_data("product_id") %>" name="food-product-id">
				<input type="hidden" value="<%=product.get_data("product_type") %>" name="product-type">

                <div class="form-group">
                    <label for="originalPrice">Original Price:</label>
                    <input type="number" id="originalPrice" value="<%=product.get_data("product_original_price") %>" name="food-product-original-price" step="0.01">
                </div>

                <div class="form-group">
                    <label for="sellingPrice">Selling Price:</label>
                    <input type="number" id="sellingPrice" value="<%=product.get_data("product_selling_price") %>" name="food-product-selling-price" step="0.01">
                </div>
                
                <div class="form-group">
                    <label for="productQuantity">Quantity:</label>
                    <input type="number" id="productQuantity" value="<%=product.get_data("product_quantity") %>" name="food-product-quantity" min="1">
                </div>

                <div class="form-group">
                    <label for="keywords">Keywords (comma-separated):</label>
                    <textarea id="keywords" name="food-product-keywords"><%=product.get_data("product_keywords") %></textarea>
                </div>

                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea id="description" name="food-product-description" rows="4"><%=product.get_data("product_description") %></textarea>
                </div>
                
                <div class="form-group">
                    <label for="shelfLife">Shelf Life:</label>
                    <input type="text" id="shelfLife" value="<%=product.get_data("product_shelf_life") %>" name="food-product-shelf-life">
                </div>
                
                <div class="form-group">
                <label for="perishable">Is the product perishable?</label>
                <select id="perishable" name="food-product-perishable" required>
                    <option value="">Select</option>
                    <option value="true" <%=product.get_data("product_perishable").equals("1")?"selected":" " %>>Yes</option>
                    <option value="false" <%=product.get_data("product_perishable").equals("0")?"selected":" " %>>No</option>
                </select>
            </div>

            <div class="form-group">
                <label for="veg">Is the product vegetarian?</label>
                <select id="veg" name="food-product-veg">
                    <option value="">Select</option>
                    <option value="true" <%=product.get_data("product_vegetarian").equals("1")?"selected":" " %>>Yes</option>
                    <option value="false" <%=product.get_data("product_vegetarian").equals("0")?"selected":" " %>>No</option>
                </select>
            </div>

            <div class="form-group">
                <label for="meat-type">Type of Meat Used</label>
                <input type="text" id="meat-type" value="<%=product.get_data("product_meat_type")!=null?product.get_data("product_meat_type"):"" %>" name="food-meat-type" placeholder="Enter type of meat (e.g., chicken, beef, etc.)">
            </div>
                
            <div class="form-group">
                <label>Refund/Replace Option:</label><br>
                <%if(product.get_data("product_refund_replace_option").equals("refundable")){%>
                <label><input type="radio" name="food-product-refund-replace" value="refundable" class="radio" required checked> Refundable</label>
                <%}else{%>
                <label><input type="radio" name="food-product-refund-replace" value="refundable" class="radio" required> Refundable</label>
                <%}%>
                
                <%if(product.get_data("product_refund_replace_option").equals("replaceable")){%>
                <label><input type="radio" name="food-product-refund-replace" value="replaceable" class="radio" required checked> Replaceable</label>
                <%}else{%>
                <label><input type="radio" name="food-product-refund-replace" value="replaceable" class="radio" required> Replaceable</label>
                <%}%>
                
                <%if(product.get_data("product_refund_replace_option").equals("both")){ %>
                <label><input type="radio" name="food-product-refund-replace" value="both" required checked> Both</label>
                <%}else{%>
                <label><input type="radio" name="food-product-refund-replace" value="both" required> Both</label>
                <%} %>
            </div>
            
            </div>
			<div class="checkbox-section">
                <div class="checkbox-group">
                    <input type="checkbox" id="editProduct" name="product-edit-withImage">
                    <label for="editProduct">Apply Changes Along With Images</label>
                </div>
            </div>
            
                <h3>Upload Images:</h3>
                <div>
                <input type="file" id="product-images" name="food-product-image1" accept=".jpg, .jpeg, .png, .jfif">
                <input type="file" id="product-images" name="food-product-image2" accept=".jpg, .jpeg, .png, .jfif">
                <input type="file" id="product-images" name="food-product-image3" accept=".jpg, .jpeg, .png, .jfif">
                <input type="file" id="product-images" name="food-product-image4" accept=".jpg, .jpeg, .png, .jfif">
                <input type="file" id="product-images" name="food-product-image5" accept=".jpg, .jpeg, .png, .jfif">    
                </div>
            <div class="form-group">
                <button type="submit" class="btn">Save Changes</button>
            </div>
        </form>
        <%} %>
    </div>
</body>
</html>
