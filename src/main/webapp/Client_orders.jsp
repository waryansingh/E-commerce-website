<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.entity.Orders" %>
<%if(request.getSession().getAttribute("Client_username")==null || request.getSession().getAttribute("Client_id")==null || (int)request.getSession().getAttribute("Client_id")==-1){response.sendRedirect("Client_login_signup.jsp");return;} %>
<%String msg=request.getParameter("msg"); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Your Orders</title>
  <style>
    /* General Layout */
    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        margin: 0;
        padding: 0;
        background-color: #f8f9fa;
        color: #333;
    }

    .order-container {
        margin: 40px auto;
        width: 1500px;
        width: 90%;
        background: #fff;
        border-radius: 8px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        padding: 20px;
    }

    .order-container h3 {
        font-size: 2rem;
        text-align: center;
        color: #ff6f00;
        border-bottom: 3px solid #ff6f00;
        padding-bottom: 10px;
        margin-bottom: 20px;
    }

    .order-item {
        margin-bottom: 20px;
        padding: 15px;
        border-radius: 8px;
        background-color: #fdfdfd;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        transition: box-shadow 0.3s ease, transform 0.3s ease;
    }

    .order-item:hover {
        transform: translateY(-5px);
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    }

    .order-header {
        font-weight: bold;
        color: #ff6f00;
        margin-bottom: 10px;
        font-size: 1.2rem;
    }

    .product-details p {
        margin: 5px 0;
        color: #555;
        font-size: 1rem;
    }

    .order-footer {
        margin-top: 10px;
        font-size: 1rem;
    }

    .action-buttons {
        margin-top: 10px;
    }

    .action-buttons button {
        padding: 10px 15px;
        font-size: 1rem;
        font-weight: bold;
        color: #fff;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        margin-right: 10px;
        transition: background-color 0.3s ease, transform 0.2s ease;
    }

    .refund-button {
        background-color: #dc3545;
    }

    .refund-button:hover {
        background-color: #b52a3b;
        transform: scale(1.05);
    }

    .replace-button {
        background-color: #007bff;
    }

    .replace-button:hover {
        background-color: #0056b3;
        transform: scale(1.05);
    }

    /* Header */
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
        color: black;
        text-decoration: none;
    }

    .search-bar {
        flex: 1;
        display: flex;
        align-items: center;
    }

    .search-bar input {
        width: 650px;
        margin-right: 1px;
        margin-left: 50px;
        padding: 0.8rem 1rem;
        font-size: 1rem;
        border-radius: 4px 0 0 4px;
        border: 1px solid #ccc;
        outline: none;
        transition: border-color 0.3s ease;
    }

    .search-bar input:focus {
        border-color: #ff6f00;
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
    }

    .cart-section a {
        margin-left: 20px;
        color: #fff;
        text-decoration: none;
        font-weight: bold;
    }
    .product-image {
    width: 100px;
    height: 100px;
    object-fit: cover;
    border-radius: 8px;
    margin-bottom: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.product-image:hover {
    transform: scale(1.1);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}
.copy-button {
    padding: 5px 10px;
    font-size: 0.9rem;
    font-weight: bold;
    color: #fff;
    background-color: #28a745;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    margin-left: 10px;
    transition: background-color 0.3s ease, transform 0.2s ease;
}

.copy-button:hover {
    background-color: #218838;
    transform: scale(1.05);
}
.rating-section {
    margin-top: 15px;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    color: #333;
}

.rating-section label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
    font-size: 1rem;
}

.rating-dropdown {
    width: 100%;
    max-width: 300px;
    padding: 10px;
    font-size: 1rem;
    color: #555;
    border: 1px solid #ddd;
    border-radius: 6px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    outline: none;
    appearance: none;
    background: #fff url('data:image/svg+xml;charset=US-ASCII,%3Csvg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 4 5"%3E%3Cpath fill="%23333" d="M2 0L0 2h4z"%3E%3C/path%3E%3C/svg%3E') no-repeat right 10px center;
    background-size: 12px;
    transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.rating-dropdown:focus {
    border-color: #ff6f00;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}


.review-section {
    margin-top: 15px;
}

.review-section textarea {
    width: 100%;
    max-width: 500px;
    padding: 12px;
    border: 1px solid #ddd;
    border-radius: 6px;
    font-size: 1rem;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    outline: none;
    transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.review-section textarea:focus {
    border-color: #ff6f00;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.submit-review-button {
    margin-top: 10px;
    padding: 10px 20px;
    font-size: 1rem;
    font-weight: bold;
    color: #fff;
    background-color: #28a745;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    transition: background-color 0.3s ease, transform 0.2s ease;
}

.submit-review-button:hover {
    background-color: #218838;
    transform: scale(1.05);
}
.notification-bar {
    background-color: #fffbf2;
    border: 1px solid #ffd700;
    color: #333;
    padding: 15px;
    font-size: 1rem;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    margin: 20px auto;
    max-width: 800px;
    text-align: center;
    transition: background-color 0.3s ease, box-shadow 0.3s ease;
}

.notification-bar:hover {
    background-color: #fff9e6;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}


  </style>
</head>
<body>
<header>
  <h2 id="name"><a href="ClientFrontEndLoader">Urban Groove</a></h2>
  <form action="ClientFrontEndLoader" method="get">
  <div class="search-bar">
    <input type="text" placeholder="Search for Products..." name="product-type">
    <input type="hidden" value="product-srearch-result" name="operation">
    <button type="submit">Search</button>
  </div> 
  </form>
  <div class="cart-section">
    <a href="#" target="_blank">Blogs</a>
  </div>
</header>
<%
    Object[] order_details=(Object[])request.getSession().getAttribute("client-orders");
    @SuppressWarnings("unchecked")
    ArrayList<Orders> list=(ArrayList<Orders>)order_details[0];
    @SuppressWarnings("unchecked")
   	ArrayList<Orders> ordered_products=(ArrayList<Orders>)order_details[1];
    @SuppressWarnings("unchecked")
    ArrayList<Orders> ordered_products_review=(ArrayList<Orders>)order_details[2];
	%>
<div class="order-container">
	<%if(msg!=null){ %>
	<div class="notification-bar">
  		<strong><%=msg%></strong>
	</div>
	<%} %>
    <h3>Your Orders</h3>
    <div class="order-item">
    <%if(list!=null && !list.isEmpty() && ordered_products!=null && !ordered_products.isEmpty()){
    		for(Orders order:list){%>
    	<div class="order-header">
    		Order ID: <%=order.get_data("order_id")%> <br>
    		Order Reference Code: <span id="order-code-<%=order.get_data("order_id")%>"><%=order.get_data("order_reference_code")%></span>
    		<button class="copy-button" onclick="copyToClipboard('<%=order.get_data("order_reference_code")%>')">Copy</button> <br>
    		Date: <%=order.get_data("order_date") %><br>
    		Delivery Address: <%=order.get_data("client_personal_address")%><br>
    		<%if(order.get_data("client_fav_address1")!=null && !order.get_data("client_fav_address1").equals("N/A")){%>
    		Your 1st Favourite Address: <%=order.get_data("client_fav_address1")%><br>
    		<%}if(order.get_data("client_fav_address2")!=null && !order.get_data("client_fav_address2").equals("N/A")){%>
    		Your 2nd Favourite Address: <%=order.get_data("client_fav_address2")%><br>
    		<%}if(order.get_data("client_fav_address3")!=null && !order.get_data("client_fav_address3").equals("N/A")){%>
    		Your 3rd Favourite Address: <%=order.get_data("client_fav_address3")%><br>
    		<%}%>
    		
    		<%if(order.get_data("order_status").equals("0")){ %>
    		<strong>Status: Ordered</strong>
    		<%}else if(order.get_data("order_status").equals("1")){ %>
    		<strong>Status: Delivered</strong>
    		<%}else if(order.get_data("order_status").equals("2")){%>
    		<strong>Status: Canceled</strong>
    		<%}else if(order.get_data("order_status").equals("-1")){%>
    		<strong>Status: Refund Verification Under Process</strong>
    		<%}else if(order.get_data("order_status").equals("3")){%>
    		<strong>Status: Returned & Refunded</strong>
    		<%}%>
		</div>
    			<%for(int i=0;i<ordered_products.size();i++){
    				if(order.get_data("order_id").equals(ordered_products.get(i).get_data("order_id")) && ordered_products.get(i).get_data("order_id").equals(ordered_products_review.get(i).get_data("order_id"))){%>
        <div class="product-details">
        <%if(ordered_products.get(i).get_data("product_type").equals("clothing")){ %>
        	<img src="Clothing product images/<%=ordered_products.get(i).get_data("product_img1")%>" class="product-image">
       	<%}else if(ordered_products.get(i).get_data("product_type").equals("electronics")){ %>
       		<img src="Electronics product images/<%=ordered_products.get(i).get_data("product_img1")%>" class="product-image">
       	<%}else{ %>
       		<img src="Food product images/<%=ordered_products.get(i).get_data("product_img1")%>" class="product-image">
       	<%} %>
            <p>Product Name: <%=ordered_products.get(i).get_data("product_name")%></p>
            <p>Quantity: <%=ordered_products.get(i).get_data("product_quantity") %></p>
            <p>Price: ₹<%=ordered_products.get(i).get_data("total_item_price") %></p>
        <%if(order.get_data("order_status").equals("1")){ %>
        <form action="ClientOperationsHandler?operation=manage-rating" method="post" id="rating-product<%=i%>">
        <input type="hidden" name="product-type" value="<%=ordered_products.get(i).get_data("product_type")%>">
    	<input type="hidden" name="product-id" value="<%=ordered_products.get(i).get_data("product_id")%>"> 
        <div class="rating-section">
    		<label for="rating-<%=ordered_products.get(i).get_data("product_id")%>">Rate this product:</label>
    		<select id="rating-<%=ordered_products.get(i).get_data("product_id")%>" name="rating" class="rating-dropdown">
    			<%if(ordered_products_review.get(i).get_data("product_client_rating").equals("5.0")){%>
    			<option value="" disabled>Choose your rating</option>
        		<option value="1">★☆☆☆☆ - Poor</option>
        		<option value="2">★★☆☆☆ - Fair</option>
        		<option value="3">★★★☆☆ - Good</option>
        		<option value="4">★★★★☆ - Very Good</option>
        		<option value="5" selected>★★★★★ - Excellent</option>
    			<%}else if(ordered_products_review.get(i).get_data("product_client_rating").equals("4.0")){ %>
    			<option value="" disabled>Choose your rating</option>
        		<option value="1">★☆☆☆☆ - Poor</option>
        		<option value="2">★★☆☆☆ - Fair</option>
        		<option value="3">★★★☆☆ - Good</option>
        		<option value="4" selected>★★★★☆ - Very Good</option>
        		<option value="5">★★★★★ - Excellent</option>
    			<%}else if(ordered_products_review.get(i).get_data("product_client_rating").equals("3.0")){ %>
    			<option value="" disabled>Choose your rating</option>
        		<option value="1">★☆☆☆☆ - Poor</option>
        		<option value="2">★★☆☆☆ - Fair</option>
        		<option value="3" selected>★★★☆☆ - Good</option>
        		<option value="4">★★★★☆ - Very Good</option>
        		<option value="5">★★★★★ - Excellent</option>
    			<%}else if(ordered_products_review.get(i).get_data("product_client_rating").equals("2.0")){ %>
    			<option value="" disabled>Choose your rating</option>
        		<option value="1">★☆☆☆☆ - Poor</option>
        		<option value="2" selected>★★☆☆☆ - Fair</option>
        		<option value="3">★★★☆☆ - Good</option>
        		<option value="4">★★★★☆ - Very Good</option>
        		<option value="5">★★★★★ - Excellent</option>
    			<%}else if(ordered_products_review.get(i).get_data("product_client_rating").equals("1.0")){ %>
    			<option value="" disabled>Choose your rating</option>
        		<option value="1" selected>★☆☆☆☆ - Poor</option>
        		<option value="2">★★☆☆☆ - Fair</option>
        		<option value="3">★★★☆☆ - Good</option>
        		<option value="4">★★★★☆ - Very Good</option>
        		<option value="5">★★★★★ - Excellent</option>
    			<%}else{%>
    			<option value="" disabled selected>Choose your rating</option>
        		<option value="1">★☆☆☆☆ - Poor</option>
        		<option value="2">★★☆☆☆ - Fair</option>
        		<option value="3">★★★☆☆ - Good</option>
        		<option value="4">★★★★☆ - Very Good</option>
        		<option value="5">★★★★★ - Excellent</option>
    			<%}%>
    		</select>
		</div>
		<button class="submit-review-button" type="submit">Submit Rating</button>
		</form>
		
		<form action="ClientOperationsHandler?operation=manage-review" method="post">
		<div class="review-section">
    		<label for="review-<%=ordered_products.get(i).get_data("product_id")%>">Write a Review:</label><br>
    		<input type="hidden" name="product-type" value="<%=ordered_products.get(i).get_data("product_type")%>">
    		<input type="hidden" name="product-id" value="<%=ordered_products.get(i).get_data("product_id")%>"> 
    		<textarea id="review-<%=ordered_products.get(i).get_data("product_id")%>" name="review" rows="4" placeholder="Share your thoughts about this product..."><%=ordered_products_review.get(i).get_data("product_review") %></textarea><br>
    		<button class="submit-review-button" type="submit">Submit Review</button>
		</div>
		</form>
		<%} %>
        <hr>
        </div>
        <%}}%>
        <p><strong>Total Price: ₹<%=order.get_data("total_amount") %></strong></p>
        <%if(order.get_data("order_status").equals("1")){%>
        <div class="order-footer">
        <form action="ClientOperationsHandler?operation=refund-return" method="post">
        <div class="action-buttons">
        <input type="hidden" value="<%=order.get_data("order_id")%>" name="order-id">
                <button class="refund-button">Request Refund</button>
            </div>
        </form>
        </div>
        <%}else if(order.get_data("order_status").equals("0")){%>
        <div class="order-footer">
        <form action="ClientOperationsHandler?operation=cancel-order" method="post">
        <input type="hidden" value="<%=order.get_data("order_id")%>" name="order-id">
            <div class="action-buttons">
                <button class="refund-button">Cancel Order</button>
            </div>
        </form>
        </div>
        <%} %>
    <%}ordered_products.clear();list.clear();ordered_products_review.clear();ordered_products=null;list=null;ordered_products_review=null;order_details=null;request.getSession().removeAttribute("client-orders");}else{%>
    <h2>No Orders Found.</h2>
    <%}%>
    </div>
</div>
<script>
function submitForm(form) {
    document.getElementById(form).submit();
	}
function copyToClipboard(referenceCode) {
    const tempInput = document.createElement("input");
    tempInput.value = referenceCode;
    document.body.appendChild(tempInput);
    tempInput.select();
    document.execCommand("copy");
    document.body.removeChild(tempInput);
    alert("Order Reference Code copied: " + referenceCode);
}
</script>
</body>
</html>
