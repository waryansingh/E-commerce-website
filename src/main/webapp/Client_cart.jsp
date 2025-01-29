<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.Cart" %>
<%@ page import="java.util.ArrayList" %>
<%if(request.getSession().getAttribute("Client_username")==null || request.getSession().getAttribute("Client_id")==null || (int)request.getSession().getAttribute("Client_id")==-1){response.sendRedirect("Client_login_signup.jsp");return;} %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Your Cart</title>
<style>
/* General Layout */
body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    margin: 0;
    padding: 0;
    background-color: #f8f9fa;
    color: #333;
}

.cart-container {
    margin: 40px auto;
    max-width: 1200px;
    width: 90%;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    padding: 20px;
}

.cart-container h3 {
    font-size: 2rem;
    text-align: center;
    color: #ff6f00;
    border-bottom: 3px solid #ff6f00;
    padding-bottom: 10px;
    margin-bottom: 20px;
}

.cart-items {
    list-style: none;
    padding: 0;
    margin: 0;
}

.cart-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
    padding: 15px;
    border-radius: 8px;
    background-color: #fdfdfd;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
    transition: box-shadow 0.3s ease, transform 0.3s ease;
}

.cart-item:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.cart-item img {
    width: 100px;
    height: 100px;
    object-fit: cover;
    border-radius: 6px;
}

.item-details {
    flex: 1;
    margin-left: 20px;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.item-name {
    font-size: 1.2rem;
    font-weight: bold;
    margin-bottom: 8px;
    color: #333;
}

.item-price {
    font-size: 1rem;
    color: #555;
    margin-bottom: 12px;
}

.quantity-wrapper {
    display: flex;
    align-items: center;
    gap: 10px;
}

.quantity-button {
    padding: 10px 15px;
    font-size: 1rem;
    font-weight: bold;
    color: #fff;
    background-color: #ff6f00;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease, transform 0.2s ease;
}

.quantity-button:hover {
    background-color: #e65c00;
    transform: scale(1.1);
}

.quantity-display {
    font-size: 1.1rem;
    font-weight: bold;
    color: #333;
    text-align: center;
    width: 40px;
}

.remove-button {
    padding: 10px 15px;
    background-color: #dc3545;
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    font-size: 0.9rem;
    transition: background-color 0.3s ease, transform 0.2s ease;
}

.remove-button:hover {
    background-color: #b52a3b;
    transform: scale(1.02);
}

.checkout-container {
    margin-top: 20px;
    text-align: center;
}

.checkout-button {
    padding: 15px 30px;
    font-size: 1.2rem;
    font-weight: bold;
    color: white;
    background-color: #28a745;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    transition: background-color 0.3s ease, transform 0.2s ease;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.checkout-button:hover {
    background-color: #218838;
    transform: translateY(-2px);
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

.cart-links a {
    margin-left: 20px;
    color: #fff;
    text-decoration: none;
    font-weight: bold;
}
.cart-section a {
    margin-left: 20px;
    color: #fff;
    text-decoration: none;
    font-weight: bold;
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
  	<a href="ClientFrontEndLoader?operation=blog-posts-all" target="_blank">Blogs</a>
  	<a href="ClientFrontEndLoader?operation=load-wishlist">Wishlist</a>
  </div>
</header>

<div class="cart-container">
    <h3>Your Cart</h3>
    <ul class="cart-items">
    <%
    double total=0.0,price=0.0;
    @SuppressWarnings("unchecked")
    ArrayList<Cart> items=(ArrayList<Cart>)request.getSession().getAttribute("client-cart-data");
    if(items==null || items.isEmpty()) { %>
    <div>
        <h2>Empty Cart</h2>
    </div>
    <% } else {
        for(Cart item : items) { %>
        <%price=(Double.parseDouble(item.get_data("product_price")) * Integer.parseInt(item.get_data("product_quantity")));total+=price;%>
        <li class="cart-item">
            <% if(item.get_data("product_type").equals("clothing")) { %>
                    <img src="Clothing product images/<%=item.get_data("product_img1") %>" alt="Product Image">
                <% } else if(item.get_data("product_type").equals("electronics")) { %>
                    <img src="Electronics product images/<%=item.get_data("product_img1") %>" alt="Product Image">
                <% } else { %>
                    <img src="Food product images/<%=item.get_data("product_img1") %>" alt="Product Image">
                <% } %>
            <div class="item-details">
                <p class="item-name"><%=item.get_data("product_name") %></p>
                <%if(item.get_data("product_type").equals("clothing")) { %>
                <h4>Size: <%=item.get_data("product_size") %></h4>
                <%}%>
                <p class="item-price">₹<%=price%></p>
                <div class="quantity-wrapper">
                	<form action="ClientOperationsHandler?operation=decrease-quantity" method="post">
                	<input type="hidden" value="<%=item.get_data("cart_id")%>" name="cart_id">
                    <input type="hidden" value="<%=item.get_data("product_quantity") %>" name="current_quantity">
                    <button class="quantity-button" type="submit">-</button>
                    </form>
                    
                    <span class="quantity-display"><%=item.get_data("product_quantity") %></span>
                    
                    <form action="ClientOperationsHandler?operation=increase-quantity" method="post">
                    <input type="hidden" value="<%=item.get_data("product_id")%>" name="product_id">
                    <input type="hidden" value="<%=item.get_data("product_type")%>" name="product_type">
                    <input type="hidden" value="<%=item.get_data("cart_id")%>" name="cart_id">
                    <input type="hidden" value="<%=item.get_data("product_quantity") %>" name="current_quantity">
                    <button class="quantity-button" type="submit">+</button>
                	</form>
                </div>
            </div>
            <form action="ClientOperationsHandler?operation=remove-from-cart" method="post">
            <input type="hidden" value="<%=item.get_data("cart_id")%>" name="cart_id">
            <button class="remove-button" type="submit">Remove</button>
        	</form>
        </li>
        <%}items.clear();items=null;request.getSession().removeAttribute("client-cart-data");} %>
    </ul>
    <%if(total!=0.0){ %>
    <h3>Total ₹<%=total%></h3>
    <div class="checkout-container">
    <form action="ClientOperationsHandler?operation=buy-cart" method="post">
        <button class="checkout-button" type="submit">Proceed to Checkout</button>
    </form>
    </div>
    <%}%>
</div>

</body>
</html>
