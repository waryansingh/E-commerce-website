<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.entity.AllProducts" %>
<%@ page import="com.entity.ClientPersonalDetails" %>
<%@ page import="com.entity.ClientFavouriteAddress" %>
<%String msg=request.getParameter("msg");%>
<%Object[] profile_details=(Object[])request.getSession().getAttribute("client-profile-data"); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Online Shopping Site for Mobiles, Electronics, Furniture, Grocery, Lifestyle, Books & More. Best Offers!</title>
  <link rel="stylesheet" href="index.css">
</head>
<body>

<header>
  <span class="menu-icon" onclick="toggleSidebar()">☰</span>
  <h2 id="name"><a href="ClientFrontEndLoader" style="text-decoration: none; color:black;">Urban Groove</a></h2>
  <form action="ClientFrontEndLoader" method="get">
  <div class="search-bar">
    <input type="text" placeholder="Search for Products..." name="product-type">
    <input type="hidden" value="product-srearch-result" name="operation">
    <button type="submit">Search</button>
  </div> 
  </form>
  <div class="cart-section">
  	<a href="ClientFrontEndLoader?operation=blog-posts-all" target="_blank">Blogs</a>
    <a href="ClientFrontEndLoader?operation=load-cart">Cart</a>
    <a href="Vendor_login_signup.jsp">Become a Seller</a>
  </div>
</header>

<div id="products" class="content-section active">
<div class="banner">
      <img src="Banner images/Urban Groove.png" alt="Banner">
</div>
</div>

<div class="filter-bar">
  <a href="ClientFrontEndLoader" onclick="change_product_type('all')">All</a>
  <a href="ClientFrontEndLoader?products=clothing" onclick="change_product_type('clothing')">Clothing</a>
  <a href="ClientFrontEndLoader?products=electronics" onclick="change_product_type('electronics')">Electronics & Accessories</a>
  <a href="ClientFrontEndLoader?products=food" onclick="change_product_type('food')">Food</a>
</div>
<div id="products" class="content-section active">
  <div id="sort">
    <h3>SORT</h3>
    <%if(msg!=null){%>
    	<div class="notice">
        	<%=msg%>
        </div>
    <%}%>
    <form id="from-check-1" action="ClientFrontEndLoader?sort-type=form-1" method="post">
    <input type="hidden" value="<%=(String)request.getSession().getAttribute("product-type")%>" name="product-type-form-1">
    <div class="filter-option">
        <label for="price--Low-to-High">Price: Low to High</label>
        <%if(request.getSession().getAttribute("form-1")!=null && request.getSession().getAttribute("form-1").equals("p-l-to-h")){request.getSession().removeAttribute("form-1");%>
        <input type="radio" id="price--Low-to-High" name="sort" value="p-l-to-h" onclick="submitForm('from-check-1')" checked>
        <%}else{ %>
        <input type="radio" id="price--Low-to-High" name="sort" value="p-l-to-h" onclick="submitForm('from-check-1')">
        <%} %>
    </div>
    
    <div class="filter-option">
        <label for="price--High-to-Low">Price: High to Low</label>
        <%if(request.getSession().getAttribute("form-1")!=null && request.getSession().getAttribute("form-1").equals("p-h-to-l")){request.getSession().removeAttribute("form-1");%>
        <input type="radio" id="price--High-to-Low" name="sort" value="p-h-to-l" onclick="submitForm('from-check-1')" checked>
        <%}else{ %>
        <input type="radio" id="price--High-to-Low" name="sort" value="p-h-to-l" onclick="submitForm('from-check-1')">
        <%} %>
    </div>
    
    <div class="filter-option">
        <label for="rating">Rating: High to Low</label>
        <%if(request.getSession().getAttribute("form-1")!=null && request.getSession().getAttribute("form-1").equals("r-h-to-l")){request.getSession().removeAttribute("form-1");%>
        <input type="radio" id="rating" name="sort" value="r-h-to-l" onclick="submitForm('from-check-1')" checked>
        <%}else{ %>
        <input type="radio" id="rating" name="sort" value="r-h-to-l" onclick="submitForm('from-check-1')">
        <%} %>
    </div>
    </form>
    
    <div class="section-divider"></div>
    
    <form id="from-check-2" action="ClientFrontEndLoader?sort-type=form-2" method="post">
    <input type="hidden" value="<%=(String)request.getSession().getAttribute("product-type")%>" name="product-type-form-2">
    <div class="filter-section">
        <label class="section-title">Price Range</label>
        <div class="price-range">
            <input type="number" class="price-box" placeholder="Min" id="min" name="price-min">
            <span>to</span>
            <input type="number" class="price-box" placeholder="Max" id="max" name="price-max">
        </div>
        <button type="submit">Apply Filter</button>
    </div>
    </form>

    <div class="section-divider"></div>
    
    <form id="from-check-3" action="ClientFrontEndLoader?sort-type=form-3" method="post">
    <input type="hidden" value="<%=(String)request.getSession().getAttribute("product-type")%>" name="product-type-form-3">
    <div class="filter-section">
        <label class="section-title">Customer Ratings</label>
        <div class="rating-option">
        <%if(request.getSession().getAttribute("form-3")!=null && request.getSession().getAttribute("form-3").equals("4")){request.getSession().removeAttribute("form-3");%>
            <input type="radio" id="4" name="rating-stars" value="4" onclick="submitForm('from-check-3')" checked>
        <%}else{ %>
        	<input type="radio" id="4" name="rating-stars" value="4" onclick="submitForm('from-check-3')">
        <%} %>
            <label for="4">4★ & above</label>
        </div>
        <div class="rating-option">
        <%if(request.getSession().getAttribute("form-3")!=null && request.getSession().getAttribute("form-3").equals("3")){request.getSession().removeAttribute("form-3");%>
            <input type="radio" id="3" name="rating-stars" value="3" onclick="submitForm('from-check-3')" checked>
        <%}else{ %>
        	<input type="radio" id="3" name="rating-stars" value="3" onclick="submitForm('from-check-3')">
        <%} %>
            <label for="3">3★ & above</label>
        </div>
        <div class="rating-option">
        <%if(request.getSession().getAttribute("form-3")!=null && request.getSession().getAttribute("form-3").equals("2")){request.getSession().removeAttribute("form-3");%>
            <input type="radio" id="2" name="rating-stars" value="2" onclick="submitForm('from-check-3')" checked>
        <%}else{ %>
        	<input type="radio" id="2" name="rating-stars" value="2" onclick="submitForm('from-check-3')">
        <%} %>
            <label for="2">2★ & above</label>
        </div>
        <div class="rating-option">
        <%if(request.getSession().getAttribute("form-3")!=null && request.getSession().getAttribute("form-3").equals("1")){request.getSession().removeAttribute("form-3");%>
            <input type="radio" id="1" name="rating-stars" value="1" onclick="submitForm('from-check-3')" checked>
        <%}else{ %>
        	<input type="radio" id="1" name="rating-stars" value="1" onclick="submitForm('from-check-3')">
        <%} %>
            <label for="1">1★ & above</label>
        </div>
    </div>
    </form>
    
    <div class="section-divider"></div>
    
    <form action="ClientFrontEndLoader?sort-type=form-4" method="post">
    <div class="filter-section">
    <label for="brand"><b>Brand Name</b></label>
    <input type="text" id="brand" name="brand-name-form-4">
    <button type="submit">SEARCH</button>
    </div>
    </form>
    
</div>
  <div class="product-grid" id="products">
<%
@SuppressWarnings("unchecked")
ArrayList<Object> products=(ArrayList<Object>)request.getSession().getAttribute("product-list");
if(products==null || products.isEmpty()){%>
<div class="not-found"><h2>No products Found</h2></div>
<%}else{
int i=0;
for(Object product:products){
if(product instanceof AllProducts.Clothing){
if(Integer.parseInt(((AllProducts.Clothing)product).get_data("product_quantity"))<1){%>
    <div class="product-item sold-out" onclick="submitForm('load-clothing-product<%=i%>')">
    <form id="load-clothing-product<%=i%>" action="ClientFrontEndLoader" method="get">
    <input type="hidden" name="product-id" value="<%=((AllProducts.Clothing)product).get_data("product_id")%>">
    <input type="hidden" name="product-type" value="<%=((AllProducts.Clothing)product).get_data("product_type")%>">
      <img src="Clothing product images/<%=((AllProducts.Clothing)product).get_data("product_img1")%>">
      <div class="product-title"><%=((AllProducts.Clothing)product).get_data("product_name")%><p class="product-rating">★<%=((AllProducts.Clothing)product).get_data("product_rating")%></p></div>
      <div class="product-price">₹ <%=((AllProducts.Clothing)product).get_data("product_selling_price")%> <del id="original">₹<%=((AllProducts.Clothing)product).get_data("product_original_price")%></del><p id="discount"><%=AllProducts.get_discount(((AllProducts.Clothing)product).get_data("product_selling_price"),((AllProducts.Clothing)product).get_data("product_original_price"))%>% off</p></div>
    </form>
    </div>
<%}else{%>
	<div class="product-item" onclick="submitForm('load-clothing-product<%=i%>')">
	<form id="load-clothing-product<%=i%>" action="ClientFrontEndLoader" method="get">
    <input type="hidden" name="product-id" value="<%=((AllProducts.Clothing)product).get_data("product_id")%>">
    <input type="hidden" name="product-type" value="<%=((AllProducts.Clothing)product).get_data("product_type")%>">
      <img src="Clothing product images/<%=((AllProducts.Clothing)product).get_data("product_img1")%>">
      <div class="product-title"><%=((AllProducts.Clothing)product).get_data("product_name")%><p class="product-rating">★<%=((AllProducts.Clothing)product).get_data("product_rating")%></p></div>
      <div class="product-price">₹ <%=((AllProducts.Clothing)product).get_data("product_selling_price")%> <del id="original">₹<%=((AllProducts.Clothing)product).get_data("product_original_price")%></del><p id="discount"><%=AllProducts.get_discount(((AllProducts.Clothing)product).get_data("product_selling_price"),((AllProducts.Clothing)product).get_data("product_original_price"))%>% off</p></div>
    </form>
    </div>
<%}%>
<%i++;} else if(product instanceof AllProducts.Electronics){
	if(Integer.parseInt(((AllProducts.Electronics)product).get_data("product_quantity"))<1){%>
	<div class="product-item sold-out" onclick="submitForm('load-electronics-product<%=i%>')">
	<form id="load-electronics-product<%=i%>" action="ClientFrontEndLoader" method="get">
	<input type="hidden" name="product-id" value="<%=((AllProducts.Electronics)product).get_data("product_id")%>">
	<input type="hidden" name="product-type" value="<%=((AllProducts.Electronics)product).get_data("product_type")%>">
      <img src="Electronics product images/<%=((AllProducts.Electronics)product).get_data("product_img1")%>">
      <div class="product-title"><%=((AllProducts.Electronics)product).get_data("product_name")%><p class="product-rating">★<%=((AllProducts.Electronics)product).get_data("product_rating")%></p></div>
      <div class="product-price">₹ <%=((AllProducts.Electronics)product).get_data("product_selling_price")%> <del id="original">₹<%=((AllProducts.Electronics)product).get_data("product_original_price")%></del><p id="discount"><%=AllProducts.get_discount(((AllProducts.Electronics)product).get_data("product_selling_price"),((AllProducts.Electronics)product).get_data("product_original_price"))%>% off</p></div>
    </form>
    </div>
<%}else{%>
	<div class="product-item" onclick="submitForm('load-electronics-product<%=i%>')">
	<form id="load-electronics-product<%=i%>" action="ClientFrontEndLoader" method="get">
	<input type="hidden" name="product-id" value="<%=((AllProducts.Electronics)product).get_data("product_id")%>">
	<input type="hidden" name="product-type" value="<%=((AllProducts.Electronics)product).get_data("product_type")%>">
      <img src="Electronics product images/<%=((AllProducts.Electronics)product).get_data("product_img1")%>">
      <div class="product-title"><%=((AllProducts.Electronics)product).get_data("product_name")%><p class="product-rating">★<%=((AllProducts.Electronics)product).get_data("product_rating")%></p></div>
      <div class="product-price">₹ <%=((AllProducts.Electronics)product).get_data("product_selling_price")%> <del id="original">₹<%=((AllProducts.Electronics)product).get_data("product_original_price")%></del><p id="discount"><%=AllProducts.get_discount(((AllProducts.Electronics)product).get_data("product_selling_price"),((AllProducts.Electronics)product).get_data("product_original_price"))%>% off</p></div>
    </form>
    </div>
<%}%>
<%i++;}else{ 
	if(Integer.parseInt(((AllProducts.Food)product).get_data("product_quantity"))<1){%>
	<div class="product-item sold-out" onclick="submitForm('load-food-product<%=i%>')">
	<form id="load-food-product<%=i%>" action="ClientFrontEndLoader" method="get">
	<input type="hidden" name="product-id" value="<%=((AllProducts.Food)product).get_data("product_id")%>">
	<input type="hidden" name="product-type" value="<%=((AllProducts.Food)product).get_data("product_type")%>">
      <img src="Food product images/<%=((AllProducts.Food)product).get_data("product_img1") %>">
      <div class="product-title"><%=((AllProducts.Food)product).get_data("product_name")%><p class="product-rating">★<%=((AllProducts.Food)product).get_data("product_rating")%></p></div>
      <div class="product-price">₹ <%=((AllProducts.Food)product).get_data("product_selling_price")%> <del id="original">₹<%=((AllProducts.Food)product).get_data("product_original_price")%></del><p id="discount"><%=AllProducts.get_discount(((AllProducts.Food)product).get_data("product_selling_price"),((AllProducts.Food)product).get_data("product_original_price"))%>% off</p></div>
    </form>
    </div>
<%}else{%>
	<div class="product-item" onclick="submitForm('load-food-product<%=i%>')">
	<form id="load-food-product<%=i%>" action="ClientFrontEndLoader" method="get">
	<input type="hidden" name="product-id" value="<%=((AllProducts.Food)product).get_data("product_id")%>">
	<input type="hidden" name="product-type" value="<%=((AllProducts.Food)product).get_data("product_type")%>">
      <img src="Food product images/<%=((AllProducts.Food)product).get_data("product_img1") %>">
      <div class="product-title"><%=((AllProducts.Food)product).get_data("product_name")%><p class="product-rating">★<%=((AllProducts.Food)product).get_data("product_rating")%></p></div>
      <div class="product-price">₹ <%=((AllProducts.Food)product).get_data("product_selling_price")%> <del id="original">₹<%=((AllProducts.Food)product).get_data("product_original_price")%></del><p id="discount"><%=AllProducts.get_discount(((AllProducts.Food)product).get_data("product_selling_price"),((AllProducts.Food)product).get_data("product_original_price"))%>% off</p></div>
    </form>
    </div>
<%}%>
<%i++;}%>
<%}products.clear();products=null;request.getSession().removeAttribute("product-list");
}
%>
  </div>
  </div>
  
<div id="profile" class="content-section">
<%
ClientPersonalDetails details=(ClientPersonalDetails)profile_details[0];
if(details!=null && details.get_data("client_fname")!=null){
@SuppressWarnings("unchecked")
ArrayList<ClientPersonalDetails> addresses=(ArrayList<ClientPersonalDetails>)profile_details[1];%>
    <h2>Profile</h2>
    <div class="profile-details">
        <div class="profile-field">
            <label for="firstName">First Name:</label>
            <p id="firstName"><%=details.get_data("client_fname")%></p>
        </div>
        <div class="profile-field">
            <label for="lastName">Last Name:</label>
            <p id="lastName"><%=details.get_data("client_lname")%></p>
        </div>
        <div class="profile-field">
            <label for="email">Email:</label>
            <p id="email"><%=details.get_data("client_email")%></p>
        </div>
        <div class="profile-field">
            <label for="phone">Phone Number:</label>
            <p id="phone">+91 <%=details.get_data("client_phone")%></p>
        </div>
        <div class="profile-field">
            <label for="username">Username:</label>
            <p id="username"><%=details.get_data("client_username")%></p>
        </div>
		<%int i=1;for(ClientPersonalDetails address:addresses){%>
        <h3>Address Details <%=i++%></h3>
        <div class="profile-field">
            <label for="address">Address Type:</label>
            <p id="address"><%=address.get_data("client_address_type")%></p>
        </div>
        <div class="profile-field">
            <label for="address">Address:</label>
            <p id="address"><%=address.get_data("client_address")%></p>
        </div>
        <div class="profile-field">
            <label for="city">City:</label>
            <p id="city"><%=address.get_data("client_city")%></p>
        </div>
        <div class="profile-field">
            <label for="state">State:</label>
            <p id="state"><%=address.get_data("client_state")%></p>
        </div>
        <div class="profile-field">
            <label for="zip">Landmark:</label>
            <p id="zip"><%=address.get_data("client_landmark")%></p>
        </div>
        <div class="profile-field">
            <label for="zip">Zip Code:</label>
            <p id="zip"><%=address.get_data("client_zip_code")%></p>
        </div>
        <%}if(profile_details[2]!=null){ @SuppressWarnings("unchecked")
        	ArrayList<ClientFavouriteAddress> fav_addresses=(ArrayList<ClientFavouriteAddress>)profile_details[2];int j=1;
        	for(ClientFavouriteAddress fav_address:fav_addresses){%>
        <h3>Favorite Address Details <%=j++ %></h3>
        <div class="profile-field">
            <label for="address">Address Type:</label>
            <p id="address"><%=fav_address.get_data("client_fav_address_type")%></p>
        </div>
        <div class="profile-field">
            <label for="address">Receiver Name:</label>
            <p id="address"><%=fav_address.get_data("receiver_name")%></p>
        </div>
        <div class="profile-field">
            <label for="address">Receiver Phone:</label>
            <p id="address"><%=fav_address.get_data("receiver_phone")%></p>
        </div>
        <div class="profile-field">
            <label for="address">Address:</label>
            <p id="address"><%=fav_address.get_data("client_fav_address")%></p>
        </div>
        <div class="profile-field">
            <label for="city">City:</label>
            <p id="city"><%=fav_address.get_data("client_fav_city")%></p>
        </div>
        <div class="profile-field">
            <label for="state">State:</label>
            <p id="state"><%=fav_address.get_data("client_fav_state")%></p>
        </div>
        <div class="profile-field">
            <label for="zip">Landmark:</label>
            <p id="zip"><%=fav_address.get_data("client_fav_landmark")%></p>
        </div>
        <div class="profile-field">
            <label for="zip">Zip Code:</label>
            <p id="zip"><%=fav_address.get_data("client_fav_zip_code")%></p>
        </div>
        <%}}%>
        
        <div>
        <div id="popup" class="popup-overlay">
        <div class="popup-content">
            <button id="closePopup" class="close-icon">&times;</button>
            <h4>Delete Account?</h4>
            <p>This action is permanent and cannot be undone. 
				All your data, including your profile information, posts, will be permanently deleted.
				If you wish to proceed, click <strong>Confirm.</strong></p>
            <button class="Confirm-button" onclick="submitForm('delete-account')">Confirm</button>
        </div>
    	</div>
        <form action="ClientOperationsHandler?operation=delete-client-account" method="post" id="delete-account"> 
        	<input type="hidden" name="client-id" value="<%=request.getSession().getAttribute("Client_id")%>">
        </form>
        <button id="openPopup" class="popup-trigger">Delete Account</button>
        </div>
    </div>
    
<%}else{%>
<h1 id="not-found"><a href="Client_login_signup.jsp">login</a></h1>
<%}%>
</div>

  <div id="fav-address" class="content-section">
  <div class="address">
  <h2>Favorite Addresses</h2>
  <%if(request.getSession().getAttribute("error_msg")!=null){%>
    	<div class="notice">
        	<%=request.getSession().getAttribute("error_msg")%>
        </div>
    <%request.getSession().removeAttribute("error_msg");}%>
  <div class="address-list">
  <%
  @SuppressWarnings("unchecked")
  ArrayList<ClientFavouriteAddress> items=(ArrayList<ClientFavouriteAddress>)profile_details[2];
  if(items!=null && !items.isEmpty()){
  for(ClientFavouriteAddress address:items){%>
    <div class="address-item">
      <p class="address-title"><%=address.get_data("client_fav_address_type").toUpperCase()%></p>
      <p>Receiver Name: <%=address.get_data("receiver_name") %> Phone: <%=address.get_data("receiver_phone") %></p>
      <p><%=address.get_data("client_fav_address")+", "+address.get_data("client_fav_city")+", "+address.get_data("client_fav_state")+", "+address.get_data("client_fav_landmark")+", "+address.get_data("client_fav_zip_code")  %></p>
    <form action="ClientOperationsHandler?operation=delete-favourite-address" method="post" style="margin-top: 10px;">
    	<input type="hidden" name="fav-address-id" value="<%= address.get_data("client_fav_address_id") %>">
        <button type="submit" class="delete-btn">Delete</button>
    </form>
    </div>
    <%}
  	}else{ %>
    <h1>No Favourite Address Found</h1>
    <%}%>
  </div>
  <button id="add-address-btn" class="add-address-btn" onclick="showAddAddressFields('add-fav-address-form')">Add New Favorite Address</button>

  <div id="add-fav-address-form" class="add-address-form hidden">
    <h3>Add New Address</h3>
    <form action="ClientOperationsHandler?operation=add-fav-address" method="post">
		<select name="address-type">
                <option value="" disabled selected>Select Address Type</option>
                <option value="home">Home</option>
                <option value="work">Work</option>
        </select>
        <textarea placeholder="Address" rows="2" name="address" required></textarea>
        <input type="text" placeholder="City" name="city" required/>
        <input type="text" placeholder="State" name="state" required/>
        <input type="text" placeholder="Zip Code" name="zip-code" required/>
        <input type="text" placeholder="Landmark" name="land-mark" required/>
        <input type="text" placeholder="Reciever Name" name="reciever-name" required/>
        <input type="text" placeholder="Reciever Phone number" name="phone-number" required/>
      <button type="submit" class="submit-address-btn">Save Address</button>
    </form>
  </div>
  </div>
</div>

<div id="add-address" class="content-section">
<div class="address">
<h2>Your Addresses</h2>
	<%if(request.getSession().getAttribute("error_msg")!=null){%>
    	<div class="notice">
        	<%=request.getSession().getAttribute("error_msg")%>
        </div>
    <%request.getSession().removeAttribute("error_msg");}%>
	<div class="address-list">
  <%
  @SuppressWarnings("unchecked")
  ArrayList<ClientPersonalDetails> personal_addresses=(ArrayList<ClientPersonalDetails>)profile_details[1];
  if(personal_addresses!=null && !personal_addresses.isEmpty()){
  for(ClientPersonalDetails address:personal_addresses){%>
    <div class="address-item">
      <p class="address-title"><%=address.get_data("client_address_type").toUpperCase()%></p>
      <p><%=address.get_data("client_address")+", "+address.get_data("client_city")+", "+address.get_data("client_state")+", "+address.get_data("client_landmark")+", "+address.get_data("client_zip_code")  %></p>
    <form action="ClientOperationsHandler?operation=delete-address" method="post" style="margin-top: 10px;">
    	<input type="hidden" name="address-id" value="<%= address.get_data("client_address_id") %>">
        <button type="submit" class="delete-btn">Delete</button>
    </form>
    </div>
    <%}
  	}else{ %>
    <h1>No Address Found</h1>
    <%}%>
  </div>
  <button id="add-address-btn" class="add-address-btn" onclick="showAddAddressFields('add-address-form')">Add New Address</button>
	<div id="add-address-form" class="add-address-form hidden">
    <h3>Add New Address</h3>
    <form action="ClientOperationsHandler?operation=add-address" method="post">
		<select name="address-type">
                <option value="" disabled selected>Select Address Type</option>
                <option value="home">Home</option>
                <option value="work">Work</option>
        </select>
        <textarea placeholder="Address" rows="2" name="address" required></textarea>
        <input type="text" placeholder="City" name="city" required/>
        <input type="text" placeholder="State" name="state" required/>
        <input type="text" placeholder="Zip Code" name="zip-code" required/>
        <input type="text" placeholder="Landmark" name="land-mark" required/>
      <button type="submit" class="submit-address-btn">Save Address</button>
    </form>
  </div>
  </div>
</div>

<div class="sidebar" id="sidebar">
  <span class="sidebar-close" onclick="toggleSidebar()">X</span>
    <span class="greeting">Hello</span>
  <ul>
    <li><a href="ClientFrontEndLoader" onclick="changeContent('products')">All Products</a></li>
    <li><a href="#" onclick="changeContent('profile')">My Profile</a></li>
    <li><a href="ClientFrontEndLoader?operation=personal-blog-posts" target="_blank">Your Blogs</a></li>
    <li><a href="Client_blog_upload.jsp" target="_blank">Post Blog</a></li>
    <li><a href="ClientFrontEndLoader?operation=load-orders">Your Orders</a></li>
    <li><a href="ClientFrontEndLoader?operation=load-wishlist">Wishlist</a></li>
    <li><a href="#" onclick="changeContent('add-address')">Your Address</a></li>
    <li><a href="#" onclick="changeContent('fav-address')">Favorite Addresses</a></li>
    <%if(request.getSession().getAttribute("Client_username")!=null && request.getSession().getAttribute("Client_id")!=null){%>
    <li><form action="ClientOperationsHandler?operation=log-out" method="post" id="log-out">
    <input type="hidden" name="operation" value="log-out">
    <a onclick="submitForm('log-out')">Log Out</a>
	</form></li>
    <%}else{%>
    <li><a href="Client_login_signup.jsp">Log In</a></li>
    <%}%>
  </ul>
</div>

<script>
	function showAddAddressFields(id) {
	  const form = document.getElementById(id);
	  form.classList.toggle('hidden'); // Toggles the visibility of the add address form
	};
  function changeContent(section) {
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(function(sec) {
      sec.classList.remove('active');
    });
    const activeSection = document.getElementById(section);
    if (activeSection) {
      activeSection.classList.add('active');
    }
    toggleSidebar();
  }
  function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('active');
  }
  function submitForm(form) {
	    document.getElementById(form).submit();
  }
  document.addEventListener('DOMContentLoaded', () => {
      const popup = document.getElementById('popup');
      const openPopupButton = document.getElementById('openPopup');
      const closePopupButton = document.getElementById('closePopup');

      // Open the popup
      openPopupButton.addEventListener('click', () => {
          popup.classList.add('active');
          popup.classList.remove('hidden');
      });

      // Close the popup
      closePopupButton.addEventListener('click', () => {
          popup.classList.remove('active');
          popup.classList.add('hidden');
      });

      // Close popup when clicking outside of it
      popup.addEventListener('click', (e) => {
          if (e.target === popup) {
              popup.classList.remove('active');
              popup.classList.add('hidden');
          }
      });
  });
</script>

</body>
</html>
