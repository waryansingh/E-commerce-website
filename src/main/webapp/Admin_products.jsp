<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.entity.AllProducts" %>
<%if(request.getSession().getAttribute("Admin-username")==null){response.sendRedirect("error.jsp");return;}String error=(String)request.getSession().getAttribute("error");request.getSession().removeAttribute("error");%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Products</title>
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
            flex-direction: column;
            min-height: 100vh;
            background: linear-gradient(135deg, #ff8c00, #000);
            background-repeat: no-repeat; 
    		background-attachment: fixed; 
    		overflow: auto;
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

        /* New Search Bar Styles */
        .search-container {
            display: flex;
            align-items: center;
            margin:auto;
        }

        .search-container input[type="text"] {
            padding: 10px;
            border: none;
            border-radius: 5px;
            width: 250px;
            margin-right:10px;
        }

        .search-container button {
            padding: 10px;
            background-color: #ff8c00;
            color: #000;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration:none;
        }

        .search-container a:hover {
            background-color: #ff5719;
        }

        .container {
            width: 80%;
            margin: 20px auto;
            flex: 1;
        }

        .product-card {
            background-color: #2c2c2c;
            border-radius: 5px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            color: #fff;
        }

        .product-img {
            flex: 1;
            max-width: 100px;
            margin-right: 20px;
        }

        .product-img img {
            width: 100px;
            height: 100px;
            object-fit: cover;
            border-radius: 5px;
        }

        .product-info {
            flex: 3;
            display: flex;
            flex-direction: column;
        }

        .product-info h2 {
            margin: 0;
            font-size: 20px;
            color: #ff8c00;
        }

        .product-info p {
            margin: 5px 0;
            color: white;
        }

        .product-actions {
            flex: 1;
            text-align: right;
            display: flex;
            flex-direction: column;
            align-items: flex-end;
            gap: 10px;
        }

        .product-actions select {
            padding: 10px;
            background-color: #ff8c00;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            color: #000;
        }

        .product-actions button {
            background-color: #ff8c00;
            color: #000;
            border: none;
            padding: 10px 15px;
            cursor: pointer;
            border-radius: 5px;
            font-size: 14px;
        }

        .product-actions button:hover {
            background-color: #ff5719;
        }

        .product-actions a {
            color: #ff8c00;
            font-weight: bold;
            text-decoration: none;
            padding: 10px;
        }

        .product-actions a:hover {
            color: #ff5719;
        }

        .status-approved {
            color: #4caf50; /* Green for Approved */
            font-weight: bold;
        }

        .status-pending {
            color: #ff9800; /* Orange for Pending */
            font-weight: bold;
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

        @media (max-width: 768px) {
            .product-card {
                flex-direction: column;
            }

            .product-actions {
                text-align: center;
                margin-top: 10px;
                flex-direction: column;
            }
            .notice {
            background-color: #e74c3c;
            color: white;
            padding: 15px;
            margin:auto;
            border-radius: 4px;
            text-align: center;
            font-weight: bold;
            width: 20%;
        }
        }
        #NA{
        	color:black;
        	margin:auto;
        }
    </style>
</head>
<body>
<%
@SuppressWarnings("unchecked")
ArrayList<Object> products=(ArrayList<Object>)request.getSession().getAttribute("productlist");
%>
<div class="navbar">
    <h1>All Products</h1>
    <%if(error!=null){%>
                <div class="notice">
                    <%=error%>
                </div>
        <%}%>
    <div class="search-container">
    <form action="AdminOperationsHandler" method="get">
        <input type="text" placeholder="Search products..." name="product-type">
        <input type="hidden" value="product-srearch-result" name="request-type">
        <button type="submit">Search</button>
    </form>
    </div>
</div>
<%if(products==null || products.isEmpty()){ %>
<div id="NA"><h1>No Products Found</h1></div>
<% return;} %>
<%String page_type=null;
for(Object product:products){ 
	    if(product instanceof AllProducts.Clothing){
	    	if(((AllProducts.Clothing)product).get_data("product_status").equals("0"))
	    		page_type="product_requests";
	    	else page_type="registered_products";
	    %>
<div class="container">
    <div class="product-card">
        <div class="product-img">
            <img src="Clothing product images/<%=((AllProducts.Clothing)product).get_data("product_img1")%>" alt="Product Image">
        </div>
        <div class="product-info">
            <h2><%=((AllProducts.Clothing)product).get_data("product_type").toUpperCase() %> Product</h2>
            <p>Brand: <%=((AllProducts.Clothing)product).get_data("product_brand_name") %></p>
            <p>Name: <%=((AllProducts.Clothing)product).get_data("product_name") %></p>
            <p>Original Price: ₹<%=((AllProducts.Clothing)product).get_data("product_original_price") %></p>
            <p>Selling Price: ₹<%=((AllProducts.Clothing)product).get_data("product_selling_price") %></p>
            <p>Quantity: <%=((AllProducts.Clothing)product).get_data("product_quantity")%></p>
            <p>Keywords: <%=((AllProducts.Clothing)product).get_data("product_keywords")%></p>
            <p>Description: <%=((AllProducts.Clothing)product).get_data("product_description")%></p>
            <p>Available sizes: <%= ((AllProducts.Clothing)product).get_data("product_size1")+"    "+((AllProducts.Clothing)product).get_data("product_size2")+"    "+((AllProducts.Clothing)product).get_data("product_size3")+"    "+((AllProducts.Clothing)product).get_data("product_size4")+"    "+((AllProducts.Clothing)product).get_data("product_size5")%></p>
            <p>Stock status: <%=((AllProducts.Clothing)product).get_data("product_stock_status")%></p>
            <p>Refund or Replace: <%=((AllProducts.Clothing)product).get_data("product_refund_replace_option")%></p>
            <p>Rating: <%=((AllProducts.Clothing)product).get_data("product_rating")%></p>
            <p>Status updated by: <%=((AllProducts.Clothing)product).get_data("product_status_updated_by")%></p>
            <%String status=null;
            if(((AllProducts.Clothing)product).get_data("product_status").equals("0")){status="Pending";%>
            	<p>Status: <span class="status-pending"><%=status %></span></p>
            <%}else if(((AllProducts.Clothing)product).get_data("product_status").equals("3")){status="BlackListed";%>
            	<p>Status: <span class="status-pending"><%=status %></span></p>
            <%}else{status="Approved";%>
            	<p>Status: <span class="status-approved"><%=status %></span></p>
            <%}%>
        </div>
        <div class="product-actions">
        <form action="AdminOperationsHandler" method="post">
            <input type="hidden" value=<%=((AllProducts.Clothing)product).get_data("product_id")%> name="clothing-product-id">
            <input type="hidden" value="alter-product-status" name="form-request-type">
            <input type="hidden" value=<%=page_type%> name="product-return-page">
            <input type="hidden" value=<%=((AllProducts.Clothing)product).get_data("product_type")%> name="product-type">
            <select name="clothing_status_option">
                <option value="1">Approve</option>
                <option value="0">Reject</option>
                <option value="2">Delete</option>
                <option value="3">Blacklist</option>
            </select>
            <button type="submit">Save</button>
            </form>
        </div>
    </div>
</div>	    	
	    <%}
	    else if(product instanceof AllProducts.Electronics){
	    	if(((AllProducts.Electronics)product).get_data("product_status").equals("0"))
	    		page_type="product_requests";
	    	else page_type="registered_products";
	    %>
<div class="container">
    <div class="product-card">
        <div class="product-img">
            <img src="Electronics product images/<%=((AllProducts.Electronics)product).get_data("product_img1")%>" alt="Product Image">
        </div>
        <div class="product-info">
            <h2><%=((AllProducts.Electronics)product).get_data("product_type").toUpperCase() %> Product</h2>
            <p>Brand: <%=((AllProducts.Electronics)product).get_data("product_brand_name") %></p>
            <p>Name: <%=((AllProducts.Electronics)product).get_data("product_name") %></p>
            <p>Original Price: ₹<%=((AllProducts.Electronics)product).get_data("product_original_price") %></p>
            <p>Selling Price: ₹<%=((AllProducts.Electronics)product).get_data("product_selling_price") %></p>
            <p>Quantity: <%=((AllProducts.Electronics)product).get_data("product_quantity")%></p>
            <p>Keywords: <%=((AllProducts.Electronics)product).get_data("product_keywords")%></p>
            <p>Description: <%=((AllProducts.Electronics)product).get_data("product_description")%></p>
            <p>Technical Description: <%=((AllProducts.Electronics)product).get_data("product_technical_description") %></p>
            <p>Stock status: <%=((AllProducts.Electronics)product).get_data("product_stock_status")%></p>
            <p>Refund or Replace: <%=((AllProducts.Electronics)product).get_data("product_refund_replace_option")%></p>
            <p>Rating: <%=((AllProducts.Electronics)product).get_data("product_rating")%></p>
            <p>Status updated by: <%=((AllProducts.Electronics)product).get_data("product_status_updated_by")%></p>
            <%String status=null;
            if(((AllProducts.Electronics)product).get_data("product_status").equals("0")){status="Pending";%>
            	<p>Status: <span class="status-pending"><%=status %></span></p>
            <%}else if(((AllProducts.Electronics)product).get_data("product_status").equals("3")){status="BlackListed";%>
        		<p>Status: <span class="status-pending"><%=status %></span></p>
        	<%}else{status="Approved";%>
            	<p>Status: <span class="status-approved"><%=status %></span></p>
            <%}%>
        </div>
        <div class="product-actions">
        <form action="AdminOperationsHandler" method="post">
            <input type="hidden" value=<%=((AllProducts.Electronics)product).get_data("product_id")%> name="electronics-product-id">
            <input type="hidden" value="alter-product-status" name="form-request-type">
            <input type="hidden" value=<%=page_type%> name="product-return-page">
            <input type="hidden" value=<%=((AllProducts.Electronics)product).get_data("product_type")%> name="product-type">
            <select name="electronics_status_option">
                <option value="1">Approve</option>
                <option value="0">Reject</option>
                <option value="2">Delete</option>
                <option value="3">Blacklist</option>
            </select>
            <button type="submit">Save</button>
            </form>
        </div>
    </div>
</div>
<%}else{
	if(((AllProducts.Food)product).get_data("product_status").equals("0"))
		page_type="product_requests";
	else page_type="registered_products";
%> 
<div class="container">
    <div class="product-card">
        <div class="product-img">
            <img src="Food product images/<%=((AllProducts.Food)product).get_data("product_img1") %>" alt="Product Image 1">
        </div>
        <div class="product-info">
            <h2><%=((AllProducts.Food)product).get_data("product_type").toUpperCase() %> Product</h2>
            <p>Brand: <%=((AllProducts.Food)product).get_data("product_brand_name") %></p>
            <p>Name: <%=((AllProducts.Food)product).get_data("product_name") %></p>
            <p>Original Price: ₹<%=((AllProducts.Food)product).get_data("product_original_price") %></p>
            <p>Selling Price: ₹<%=((AllProducts.Food)product).get_data("product_selling_price") %></p>
            <p>Quantity: <%=((AllProducts.Food)product).get_data("product_quantity")%></p>
            <p>Keywords: <%=((AllProducts.Food)product).get_data("product_keywords")%></p>
            <p>Description: <%=((AllProducts.Food)product).get_data("product_description")%></p>
            <p>Shelf life: <%=((AllProducts.Food)product).get_data("product_shelf_life") %></p>
            <p>Perishable: <%=((AllProducts.Food)product).get_data("product_perishable").equals("1")?"Yes":"No" %></p>
            <p>Vegetarian: <%=((AllProducts.Food)product).get_data("product_vegetarian").equals("1")?"Yes":"No" %></p>
            <p>Meat used: <%=((AllProducts.Food)product).get_data("product_meat_type")==null?" ":((AllProducts.Food)product).get_data("product_meat_type") %></p>
            <p>Stock status: <%=((AllProducts.Food)product).get_data("product_stock_status")%></p>
            <p>Refund or Replace: <%=((AllProducts.Food)product).get_data("product_refund_replace_option")%></p>
            <p>Rating: <%=((AllProducts.Food)product).get_data("product_rating")%></p>
            <p>Status updated by: <%=((AllProducts.Food)product).get_data("product_status_updated_by")%></p>
            <%String status=null;
            if(((AllProducts.Food)product).get_data("product_status").equals("0")){status="Pending";%>
            	<p>Status: <span class="status-pending"><%=status %></span></p>
            <%}else if(((AllProducts.Food)product).get_data("product_status").equals("3")){status="BlackListed";%>
        		<p>Status: <span class="status-pending"><%=status %></span></p>
        	<%}else{status="Approved";%>
            	<p>Status: <span class="status-approved"><%=status %></span></p>
            <%}%>
        </div>
        <div class="product-actions">
        <form action="AdminOperationsHandler" method="post">
            <input type="hidden" value=<%=((AllProducts.Food)product).get_data("product_id")%> name="food-product-id">
            <input type="hidden" value="alter-product-status" name="form-request-type">
            <input type="hidden" value=<%=page_type%> name="product-return-page">
            <input type="hidden" value=<%=((AllProducts.Food)product).get_data("product_type")%> name="product-type">
            <select name="food_status_option">
                <option value="1">Approve</option>
                <option value="0">Reject</option>
                <option value="2">Delete</option>
                <option value="3">Blacklist</option>
            </select>
            <button type="submit">Save</button>
            </form>
        </div>
    </div>
</div>
<%}%>
<%}%>
<%
products.clear();
products=null;
request.getSession().removeAttribute("productlist"); 
%>
</body>
</html>
