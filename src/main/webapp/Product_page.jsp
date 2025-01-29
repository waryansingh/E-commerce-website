<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.AllProducts" %>
<%@ page import="com.entity.ClientPersonalDetails" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.entity.ProductReview" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Page</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Arial', sans-serif;
            background-color: #ffffff;
            color: #333333;
            line-height: 1.6;
        }

        .container {
            display: flex;
            flex-wrap: wrap;
            width: 85%;
            margin: 20px auto;
            background-color: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        .left-section, .right-section {
            padding: 20px;
        }

        .left-section {
            flex: 1 1 30%;
            border-right: 2px solid #ff8c00;
        }

        .right-section {
            flex: 1 1 70%;
            padding-left: 20px;
        }

        .product-title {
            font-size: 1.8rem;
            font-weight: 700;
            color: #ff8c00;
            margin-bottom: 15px;
        }

        .price-section {
            font-size: 1.6rem;
            margin-bottom: 15px;
        }

        .price-section .original-price {
            text-decoration: line-through;
            color: #777;
            margin-left: 10px;
        }

        .offers ul {
            margin-top: 10px;
            list-style: inside;
        }

        .availability, .rating {
            margin-bottom: 10px;
        }

        .action-buttons {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }
        .btn {
            flex: 1;
            padding: 15px;
            background-color: #ff8c00;
            color: #ffffff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-align: center;
            font-size: 16px;
            transition: background-color 0.3s ease;
        }

        .btn:hover {
            background-color: #ff7400;
        }

        .specifications, .description {
            margin-bottom: 20px;
        }

        .product-images {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .main-image img {
            width: 400px;
            height: 500px;
            object-fit: contain;
            border-radius: 8px;
        }

        .thumbnail-list {
            display: flex;
            gap: 10px;
        }

        .thumbnail-list img {
            width: 70px;
            height: 70px;
            border-radius: 8px;
            cursor: pointer;
            border: 2px solid transparent;
            transition: border-color 0.3s ease;
        }

        .thumbnail-list img:hover {
            border-color: #ff8c00;
        }

        /* Media Queries */
        @media screen and (max-width: 768px) {
            .container {
                flex-direction: column;
            }

            .left-section, .right-section {
                flex: 1 1 100%;
                border: none;
            }

            .right-section {
                padding-left: 0;
            }
        }
        header {
  background-color: #ff6f00;
  color: #fff;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 10;
}
header h2 a {
    color: #fff;
    text-decoration: none;
}
/* Search Bar */
.search-bar {
  flex: 1;
  display: flex;
  align-items: center;
}

.search-bar input {
  width: 65%;
  margin-right: 1px;
  margin-left: 200px;
  padding: 0.8rem 1rem;
  font-size: 1rem;
  border-radius: 4px 0 0 4px;
  border: 1px solid #ccc;
  outline: none;
  transition: border-color 0.3s ease;
}

.search-bar input:focus {
  border-color: #ff6f00;
  /* Orange */
}

.search-bar button {
  height: 45px;
  padding: 0.8rem 1rem;
  background-color: #ff8f00;
  color: #fff;
  border: none;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.search-bar button:hover {
  background-color: #e65c00;
  /* Darker Orange */
}

.cart-section a {
    margin-left: 20px;
    color: #fff;
    text-decoration: none;
    font-weight: bold;
}
.product-sizes {
    margin: 20px 0;
}

.product-sizes h3 {
    font-size: 1.2rem;
    font-weight: bold;
    color: #ff8c00;
    margin-bottom: 10px;
}

.size-options {
    display: flex;
    gap: 15px;
    align-items: center;
}

.size-options label {
    font-size: 1rem;
    font-weight: bold;
    color: #333;
    display: flex;
    align-items: center;
    cursor: pointer;
}

.size-options input[type="radio"] {
    margin-right: 5px;
    accent-color: #ff8c00; /* Orange theme color */
    transform: scale(1.2); /* Slightly larger radio button */
    cursor: pointer;
    transition: transform 0.2s ease;
}

.size-options input[type="radio"]:hover {
    transform: scale(1.3);
}
.review-section {
    margin-top: 2rem;
    padding: 1.5rem;
    border-top: 1px solid #ddd;
    background-color: #f9f9f9;
}

.review-section h2 {
    font-size: 1.8rem;
    color: #333;
    margin-bottom: 1rem;
    text-align: center;
}

.review {
    margin-bottom: 1.5rem;
    padding: 1rem;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    background-color: #fff;
}

.review-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 0.5rem;
}

.client-name {
    font-weight: bold;
    font-size: 1.2rem;
    color: #555;
}

.rating {
    color: #f39c12;
    font-size: 1.2rem;
}

.review-text {
    font-size: 1rem;
    color: #666;
    line-height: 1.5;
    margin: 0;
}

    </style>
    <script>
        function changeImage(src) {
            document.getElementById("mainProductImage").src = src;
        }
    </script>
</head>
<body>
<header>
  <h2 id="name"><a href="ClientFrontEndLoader" style="text-decoration: none; color:black;">Urban Groove</a></h2>
  <div class="search-bar">
    <input type="text" placeholder="Search for Products...">
    <button>Search</button>
  </div>
  <div class="cart-section">
    <a href="ClientFrontEndLoader?operation=load-cart">Cart</a>
    <a href="Vendor_login_signup.jsp">Become a Seller</a>
  </div>
</header>
<% 
Object product=(Object)request.getSession().getAttribute("product-data");
if(product!=null && product instanceof AllProducts.Clothing){AllProducts.Clothing data=(AllProducts.Clothing)product;%>
<div class="container">
    <div class="left-section">
        <div class="product-images">
            <div class="main-image">
                <img id="mainProductImage" src="Clothing product images/<%= data.get_data("product_img1") %>" alt="Main Product Image">
            </div>
            <div class="thumbnail-list">
                <img src="Clothing product images/<%= data.get_data("product_img1") %>" alt="Thumbnail 1" onclick="changeImage('Clothing product images/<%= data.get_data("product_img1") %>')">
                <img src="Clothing product images/<%= data.get_data("product_img2") %>" alt="Thumbnail 2" onclick="changeImage('Clothing product images/<%= data.get_data("product_img2") %>')">
                <img src="Clothing product images/<%= data.get_data("product_img3") %>" alt="Thumbnail 3" onclick="changeImage('Clothing product images/<%= data.get_data("product_img3") %>')">
                <img src="Clothing product images/<%= data.get_data("product_img4") %>" alt="Thumbnail 4" onclick="changeImage('Clothing product images/<%= data.get_data("product_img4") %>')">
                <img src="Clothing product images/<%= data.get_data("product_img5") %>" alt="Thumbnail 5" onclick="changeImage('Clothing product images/<%= data.get_data("product_img5") %>')">
            </div>
        </div>
    </div>

    <!-- Right section: Product Info -->
    <div class="right-section">
    	<%if(request.getSession().getAttribute("product_msg")!=null){%>
    	<h2><%=(String)request.getSession().getAttribute("product_msg")%></h2>
    	<%request.getSession().removeAttribute("product_msg");}%>
    	
        <h1 class="product-title"><%=data.get_data("product_name") %></h1>
        <p>Brand: <%=data.get_data("product_brand_name") %></p>
        <p class="price-section">
             ₹ <%=data.get_data("product_selling_price") %>
            <span class="original-price">₹ <%=data.get_data("product_original_price") %></span> 
            <span>(<%=AllProducts.get_discount(data.get_data("product_selling_price"),data.get_data("product_original_price"))%>% off)</span>
        </p>
        <div class="rating">
            Rating: <span><%=data.get_data("product_rating").substring(0,data.get_data("product_rating").lastIndexOf(".")+2)%>/5</span> (<%=data.get_data("product_rating_count") %> ratings)
        </div>
        <div class="availability">
            Stock:<span><%=data.get_data("product_quantity")%></span>     
        </div>
        <h4>Refundable/Replaceable: <%=data.get_data("product_refund_replace_option") %></h4>
    <div class="product-sizes">
    <h3>Select Size:</h3>
    <div class="size-options">
        <%if(data.get_data("product_size1")!=null && data.get_data("product_size1").equals("S")){%>
        <label>
            <input type="radio" name="size" value="S" onclick="set_size('S')"> S
        </label>
        <%}if(data.get_data("product_size2")!=null && data.get_data("product_size2").equals("M")){ %>
        <label>
            <input type="radio" name="size" value="M" onclick="set_size('M')"> M
        </label>
        <%}if(data.get_data("product_size3")!=null && data.get_data("product_size3").equals("L")){%>
        <label>
            <input type="radio" name="size" value="L" onclick="set_size('L')"> L
        </label>
        <%}if(data.get_data("product_size4")!=null && data.get_data("product_size4").equals("XL")){%>
        <label>
            <input type="radio" name="size" value="XL" onclick="set_size('XL')"> XL
        </label>
        <%}if(data.get_data("product_size5")!=null && data.get_data("product_size5").equals("XXL")){ %>
        <label>
            <input type="radio" name="size" value="XXL" onclick="set_size('XXL')"> XXL
        </label>
    	<%} %>
    </div>
	</div>
        <div class="action-buttons">        
        <%if(Integer.parseInt(data.get_data("product_quantity"))>0){ %>
        <form action="ClientOperationsHandler?operation=cart" method="post">
        	<input type="hidden" value="N/A" class="size" name="product_size">
        	<input type="hidden" value="<%=data.get_data("product_type")%>" name="product_type">
        	<input type="hidden" value="<%=data.get_data("product_id")%>" name="product_id">
        	<%if(data.get_data("carted").equals("true")){%>
        	<button class="btn" type="button" onclick="location.href='ClientFrontEndLoader?operation=load-cart'">View in Cart</button>
        	<%}else{%>
        	<button class="btn" type="submit">Add to Cart</button>
        	<%} %>
        </form>
        <form action="ClientOperationsHandler?operation=buy" method="post">
        	<input type="hidden" value="N/A" class="size" name="product_size">
        	<input type="hidden" value="<%=data.get_data("product_type")%>" name="product_type">
        	<input type="hidden" value="<%=data.get_data("product_id")%>" name="product_id">
            <button class="btn" type="submit">Buy Now</button>
        </form>
        <%}else{%>
        	<h2>This product is sold out!</h2>
        <%}%>
        <form action="ClientOperationsHandler?operation=wishlist" method="post">
        	<input type="hidden" value="<%=data.get_data("product_type")%>" name="product_type">
        	<input type="hidden" value="<%=data.get_data("product_id")%>" name="product_id">
        	<%if(data.get_data("wishlisted").equals("true")){%>
        	<button class="btn" type="button" onclick="location.href='ClientFrontEndLoader?operation=load-wishlist'">View in Wishlist</button>
        	<%}else{%>
        	<button class="btn" type="submit">Add to Wishlist</button>
        	<%}%>
        </form>
        </div>

        <div class="description">
            <h2>Description</h2>
            <p><%=data.get_data("product_description") %></p>
        </div>
    </div>
</div>

<div class="review-section">
    <h2>Customer Reviews</h2><hr>
    <%
    @SuppressWarnings("unchecked")
    HashMap<Integer,ProductReview> comments=(HashMap<Integer,ProductReview>)request.getSession().getAttribute("product_comments");
    if(comments!=null && !comments.isEmpty()){
    	for(ProductReview comment:comments.values()){%>
    <div class="review">
        <div class="review-header">
            <span class="client-name"><%=comment.get_data("client_name") %></span>
            <%if(comment.get_data("product_client_rating").equals("5.0")){ %>
            <span class="rating">⭐⭐⭐⭐⭐</span>
            <%}else if(comment.get_data("product_client_rating").equals("4.0")){%>
            <span class="rating">⭐⭐⭐⭐</span>
            <%}else if(comment.get_data("product_client_rating").equals("3.0")){%>
            <span class="rating">⭐⭐⭐</span>
            <%}else if(comment.get_data("product_client_rating").equals("2.0")){%>
            <span class="rating">⭐⭐</span>
            <%}else if(comment.get_data("product_client_rating").equals("1.0")){%>
            <span class="rating">⭐</span>
            <%}%>
        </div>
        <p class="review-text">
            <%=comment.get_data("product_review") %>
        </p>
    </div>
    <%}comments.clear();comments=null;request.getSession().removeAttribute("product_comments");}else{%>
    <h2>No Reviews.</h2>
    <%}%>
</div>


<%}else if(product!=null && product instanceof AllProducts.Electronics){AllProducts.Electronics data=(AllProducts.Electronics)product;%>
<div class="container">
    <div class="left-section">
        <div class="product-images">
            <div class="main-image">
                <img id="mainProductImage" src="Electronics product images/<%= data.get_data("product_img1") %>" alt="Main Product Image">
            </div>
            <div class="thumbnail-list">
                <img src="Electronics product images/<%= data.get_data("product_img1") %>" alt="Thumbnail 1" onclick="changeImage('Electronics product images/<%= data.get_data("product_img1") %>')">
                <img src="Electronics product images/<%= data.get_data("product_img2") %>" alt="Thumbnail 2" onclick="changeImage('Electronics product images/<%= data.get_data("product_img2") %>')">
                <img src="Electronics product images/<%= data.get_data("product_img3") %>" alt="Thumbnail 3" onclick="changeImage('Electronics product images/<%= data.get_data("product_img3") %>')">
                <img src="Electronics product images/<%= data.get_data("product_img4") %>" alt="Thumbnail 4" onclick="changeImage('Electronics product images/<%= data.get_data("product_img4") %>')">
                <img src="Electronics product images/<%= data.get_data("product_img5") %>" alt="Thumbnail 5" onclick="changeImage('Electronics product images/<%= data.get_data("product_img5") %>')">
            </div>
        </div>
    </div>

    <!-- Right section: Product Info -->
    <div class="right-section">
        <h1 class="product-title"><%=data.get_data("product_name") %></h1>
        <p>Brand: <%=data.get_data("product_brand_name") %></p>
        <p class="price-section">
             ₹ <%=data.get_data("product_selling_price") %>
            <span class="original-price">₹ <%=data.get_data("product_original_price") %></span> 
            <span>(<%=AllProducts.get_discount(data.get_data("product_selling_price"),data.get_data("product_original_price"))%>% off)</span>
        </p>
        <div class="rating">
            Rating: <span><%=data.get_data("product_rating").substring(0,data.get_data("product_rating").lastIndexOf(".")+2)%>/5</span> (<%=data.get_data("product_rating_count") %> ratings)
        </div>
        <div class="availability">
            Stock:<span><%=data.get_data("product_quantity")%></span>     
        </div>
		<h4>Refundable/Replaceable: <%=data.get_data("product_refund_replace_option") %></h4>
        <div class="action-buttons">        
        <%if(Integer.parseInt(data.get_data("product_quantity"))>0){ %>
        <form action="ClientOperationsHandler?operation=cart" method="post">
        	<input type="hidden" value="<%=data.get_data("product_type")%>" name="product_type">
        	<input type="hidden" value="<%=data.get_data("product_id")%>" name="product_id">
            <%if(data.get_data("carted").equals("true")){%>
        	<button class="btn" type="button" onclick="location.href='ClientFrontEndLoader?operation=load-cart'">View in Cart</button>
        	<%}else{%>
        	<button class="btn" type="submit">Add to Cart</button>
        	<%} %>
        </form>
        <form action="ClientOperationsHandler?operation=buy" method="post">
        	<input type="hidden" value="<%=data.get_data("product_type")%>" name="product_type">
        	<input type="hidden" value="<%=data.get_data("product_id")%>" name="product_id">
            <button class="btn" type="submit">Buy Now</button>
        </form>
        <%}else{%>
        	<h2>This product is sold out!</h2>
        <%}%>
        <form action="ClientOperationsHandler?operation=wishlist" method="post">
        	<input type="hidden" value="<%=data.get_data("product_type")%>" name="product_type">
        	<input type="hidden" value="<%=data.get_data("product_id")%>" name="product_id">
          	<%if(data.get_data("wishlisted").equals("true")){%>
        	<button class="btn" type="button" onclick="location.href='ClientFrontEndLoader?operation=load-wishlist'">View in Wishlist</button>
        	<%}else{%>
        	<button class="btn" type="submit">Add to Wishlist</button>
        	<%}%>
        </form>
        </div>

        <div class="description">
            <h2>Description</h2>
            <p><%=data.get_data("product_description") %></p>
        </div>

        <div class="specifications">
            <h2>Technical Description</h2>
			<p><%=data.get_data("product_technical_description") %></p>
        </div>
    </div>
</div>

<div class="review-section">
    <h2>Customer Reviews</h2><hr>
    <%
    @SuppressWarnings("unchecked")
    HashMap<Integer,ProductReview> comments=(HashMap<Integer,ProductReview>)request.getSession().getAttribute("product_comments");
    if(comments!=null && !comments.isEmpty()){
    	for(ProductReview comment:comments.values()){%>
    <div class="review">
        <div class="review-header">
            <span class="client-name"><%=comment.get_data("client_name") %></span>
            <%if(comment.get_data("product_client_rating").equals("5.0")){ %>
            <span class="rating">⭐⭐⭐⭐⭐</span>
            <%}else if(comment.get_data("product_client_rating").equals("4.0")){%>
            <span class="rating">⭐⭐⭐⭐</span>
            <%}else if(comment.get_data("product_client_rating").equals("3.0")){%>
            <span class="rating">⭐⭐⭐</span>
            <%}else if(comment.get_data("product_client_rating").equals("2.0")){%>
            <span class="rating">⭐⭐</span>
            <%}else if(comment.get_data("product_client_rating").equals("1.0")){%>
            <span class="rating">⭐</span>
            <%}%>
        </div>
        <p class="review-text">
            <%=comment.get_data("product_review") %>
        </p>
    </div>
    <%}comments.clear();comments=null;request.getSession().removeAttribute("product_comments");}else{%>
    <h2>No Reviews.</h2>
    <%}%>
</div>

<%}else if(product!=null && product instanceof AllProducts.Food){AllProducts.Food data=(AllProducts.Food)product; %>
<div class="container">
    <div class="left-section">
        <div class="product-images">
            <div class="main-image">
                <img id="mainProductImage" src="Food product images/<%= data.get_data("product_img1") %>" alt="Main Product Image">
            </div>
            <div class="thumbnail-list">
                <img src="Food product images/<%= data.get_data("product_img1") %>" alt="Thumbnail 1" onclick="changeImage('Food product images/<%= data.get_data("product_img1") %>')">
                <img src="Food product images/<%= data.get_data("product_img2") %>" alt="Thumbnail 2" onclick="changeImage('Food product images/<%= data.get_data("product_img2") %>')">
                <img src="Food product images/<%= data.get_data("product_img3") %>" alt="Thumbnail 3" onclick="changeImage('Food product images/<%= data.get_data("product_img3") %>')">
                <img src="Food product images/<%= data.get_data("product_img4") %>" alt="Thumbnail 4" onclick="changeImage('Food product images/<%= data.get_data("product_img4") %>')">
                <img src="Food product images/<%= data.get_data("product_img5") %>" alt="Thumbnail 5" onclick="changeImage('Food product images/<%= data.get_data("product_img5") %>')">
            </div>
        </div>
    </div>

    <!-- Right section: Product Info -->
    <div class="right-section">
        <h1 class="product-title"><%=data.get_data("product_name") %></h1>
        <p>Brand: <%=data.get_data("product_brand_name") %></p>
        <p class="price-section">
             ₹ <%=data.get_data("product_selling_price") %>
            <span class="original-price">₹ <%=data.get_data("product_original_price") %></span> 
            <span>(<%=AllProducts.get_discount(data.get_data("product_selling_price"),data.get_data("product_original_price"))%>% off)</span>
        </p>
        <div class="rating">
            Rating: <span><%=data.get_data("product_rating").substring(0,data.get_data("product_rating").lastIndexOf(".")+2)%>/5</span> (<%=data.get_data("product_rating_count") %> ratings)
        </div>
        <div class="availability">
            Stock:<span><%=data.get_data("product_quantity")%></span>     
        </div>
		<h4>Refundable/Replaceable: <%=data.get_data("product_refund_replace_option") %></h4>
        <div class="action-buttons">        
        <%if(Integer.parseInt(data.get_data("product_quantity"))>0){ %>
        <form action="ClientOperationsHandler?operation=cart" method="post">
        	<input type="hidden" value="<%=data.get_data("product_type")%>" name="product_type">
        	<input type="hidden" value="<%=data.get_data("product_id")%>" name="product_id">
            <%if(data.get_data("carted").equals("true")){%>
        	<button class="btn" type="button" onclick="location.href='ClientFrontEndLoader?operation=load-cart'">View in Cart</button>
        	<%}else{%>
        	<button class="btn" type="submit">Add to Cart</button>
        	<%} %>
        </form>
        <form action="ClientOperationsHandler?operation=buy" method="post">
        	<input type="hidden" value="<%=data.get_data("product_type")%>" name="product_type">
        	<input type="hidden" value="<%=data.get_data("product_id")%>" name="product_id">
            <button class="btn" type="submit">Buy Now</button>
        </form>
        <%}else{%>
        	<h2>This product is sold out!</h2>
        <%}%>
        <form action="ClientOperationsHandler?operation=wishlist" method="post">
        	<input type="hidden" value="<%=data.get_data("product_type")%>" name="product_type">
        	<input type="hidden" value="<%=data.get_data("product_id")%>" name="product_id">
          	<%if(data.get_data("wishlisted").equals("true")){%>
        		<button class="btn" type="button" onclick="location.href='ClientFrontEndLoader?operation=load-wishlist'">View in Wishlist</button>
        	<%}else{%>
        	<button class="btn" type="submit">Add to Wishlist</button>
        	<%}%>
        </form>
        </div>

        <div class="description">
            <h2>Description</h2>
            <p><%=data.get_data("product_description") %></p>
        </div>

        <div class="specifications">
            <h3>Shelf Life</h3>
			<p><%=data.get_data("product_shelf_life")%></p>
			<h3>Product Perishable</h3>
			<p><%=data.get_data("product_perishable").equals("1")?"Yes":"No"%></p>
			<h3>Product Vegetarian</h3>
			<p><%=data.get_data("product_vegetarian").equals("1")?"Yes":"No"%></p>
			<h3>Meat Type</h3>
			<p><%=data.get_data("product_meat_type").isEmpty()?"N/A":data.get_data("product_meat_type")%></p>
        </div>
    </div>
</div>

<div class="review-section">
    <h2>Customer Reviews</h2><hr>
    <%
    @SuppressWarnings("unchecked")
    HashMap<Integer,ProductReview> comments=(HashMap<Integer,ProductReview>)request.getSession().getAttribute("product_comments");
    if(comments!=null && !comments.isEmpty()){
    	for(ProductReview comment:comments.values()){%>
    <div class="review">
        <div class="review-header">
            <span class="client-name"><%=comment.get_data("client_name") %></span>
            <%if(comment.get_data("product_client_rating").equals("5.0")){ %>
            <span class="rating">⭐⭐⭐⭐⭐</span>
            <%}else if(comment.get_data("product_client_rating").equals("4.0")){%>
            <span class="rating">⭐⭐⭐⭐</span>
            <%}else if(comment.get_data("product_client_rating").equals("3.0")){%>
            <span class="rating">⭐⭐⭐</span>
            <%}else if(comment.get_data("product_client_rating").equals("2.0")){%>
            <span class="rating">⭐⭐</span>
            <%}else if(comment.get_data("product_client_rating").equals("1.0")){%>
            <span class="rating">⭐</span>
            <%}%>
        </div>
        <p class="review-text">
            <%=comment.get_data("product_review") %>
        </p>
    </div>
    <%}comments.clear();comments=null;request.getSession().removeAttribute("product_comments");}else{%>
    <h2>No Reviews.</h2>
    <%}%>
</div>

<%}%>
<script>
function set_size(size){
	let elements=document.getElementsByClassName("size");
	for(let i=0;i<elements.length;i++){
		elements[i].value=size
	}
}
function changeImage(src) {
    document.getElementById("mainProductImage").src = src;
}
</script>
</body>
</html>
