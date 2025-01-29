<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.Wishlist" %>
<%@ page import="java.util.ArrayList" %>
<%if(request.getSession().getAttribute("Client_username")==null){response.sendRedirect("Client_login_signup.jsp");return;} %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Your Wishlist</title>
<style>
/* General Layout */
body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    background-color: #f4f4f4;
}

.wishlist-section {
    margin: 20px auto;
    width: 90%;
    max-width: 1000px;
    padding: 20px;
    background-color: #ffffff;
    border-radius: 8px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.wishlist-section h3 {
    font-size: 1.8rem;
    font-weight: 700;
    color: #ff8c00;
    margin-bottom: 20px;
    border-bottom: 2px solid #ff8c00;
    padding-bottom: 8px;
    text-align: center;
}

.wishlist-items {
    list-style: none;
    padding: 0;
    margin: 0;
}

.wishlist-items li {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
    padding: 15px;
    background-color: #f9f9f9;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.wishlist-items li:hover {
    transform: translateY(-3px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.wishlist-items img {
    width: 80px;
    height: 80px;
    border-radius: 8px;
    object-fit: cover;
    margin-right: 15px;
}

.wishlist-details {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.wishlist-product-name {
    font-size: 1.2rem;
    font-weight: bold;
    color: #333;
    margin-bottom: 8px;
}

.wishlist-product-price {
    font-size: 1rem;
    color: #777;
    margin-bottom: 10px;
}

.btn-remove {
    padding: 10px 15px;
    background-color: #ff8c00;
    color: #ffffff;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    font-size: 0.9rem;
    transition: background-color 0.3s ease, transform 0.2s ease;
    white-space: nowrap;
}

.btn-remove:hover {
    background-color: #e65c00;
    transform: scale(1.02);
}

/* Responsive Design */
@media (max-width: 768px) {
    .wishlist-items li {
        flex-direction: column;
        align-items: flex-start;
    }

    .wishlist-items img {
        margin-bottom: 10px;
    }

    .btn-remove {
        align-self: flex-start;
    }
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
  /* Orange */
}

.search-bar button {
  height: 45px;
  padding: 0.8rem 1rem;
  background-color: #ff8f00;
  /* Lighter Orange for contrast */
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
    <a href="ClientFrontEndLoader?operation=load-cart">Cart</a>
    <a href="ClientFrontEndLoader?operation=blog-posts-all" target="_blank">Blogs</a>
    <a href="Vendor_login_signup.jsp">Become a Seller</a>
  </div>
</header>
<div class="wishlist-section">
    <h3>Your Wishlist</h3>
    <% 
    @SuppressWarnings("unchecked")
    ArrayList<Wishlist> items=(ArrayList<Wishlist>)request.getSession().getAttribute("client-wishlist-data");
    if(items==null || items.isEmpty()) { %>
        <div class="not-found">
            <h2>Empty Wish List</h2>
        </div>
    <% } else {int i=0;
        for(Wishlist item : items) { %>
        <ul class="wishlist-items">
            <li>
                <% if(item.get_data("product_type").equals("clothing")) { %>
                    <img src="Clothing product images/<%=item.get_data("product_img1") %>" alt="Product Image">
                <% } else if(item.get_data("product_type").equals("electronics")) { %>
                    <img src="Electronics product images/<%=item.get_data("product_img1") %>" alt="Product Image">
                <% } else { %>
                    <img src="Food product images/<%=item.get_data("product_img1") %>" alt="Product Image">
                <% } %>
                <div class="wishlist-details" onclick="submitForm('load-clothing-product<%=i%>')">
                <form id="load-clothing-product<%=i%>" action="ClientFrontEndLoader" method="get">
                <input type="hidden" name="product-id" value="<%=item.get_data("product_id")%>">
    			<input type="hidden" name="product-type" value="<%=item.get_data("product_type")%>">
                    <p class="wishlist-product-name"><%=item.get_data("product_name") %></p>
                    <p class="wishlist-product-price">₹ <%=item.get_data("product_price") %></p>
                </form>
                    <form action="ClientOperationsHandler?operation=remove-from-wishlist" method="post">
                        <input type="hidden" value="<%=item.get_data("wishlist_id") %>" name="wishlist_id">
                        <button class="btn btn-remove" type="submit">Remove</button>
                    </form>
                </div>
            </li>
        </ul>
    <% i++;}items.clear();items=null; request.getSession().removeAttribute("client-wishlist-data");} %>
</div>
<script>
function submitForm(form) {
    document.getElementById(form).submit();
}
</script>
</body>
</html>
