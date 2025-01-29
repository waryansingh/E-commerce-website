<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.entity.Orders" %>
<%
if(request.getSession().getAttribute("Vendor-username")==null){response.sendRedirect("error.jsp");return;}
String msg=(String)request.getSession().getAttribute("msg");request.getSession().removeAttribute("msg");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vendor Products</title>
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

        .container {
            width: 80%;
            margin: 20px auto;
            flex: 1;
        }

        .product-card {
            background-color: #2c2c2c;
            border-radius: 5px;
            padding: 20px;
            margin-bottom: 1px;
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
    <script>
    window.addEventListener('pageshow',(event)=>{
        if (event.persisted) {
            window.location.reload();
        }
    });
    </script>
</head>
<body>
<%
@SuppressWarnings("unchecked")
ArrayList<Orders> orders=(ArrayList<Orders>)request.getSession().getAttribute("new_orders");
%>
<div class="navbar">
    <h1>Products to Dispatch</h1>
    <ul>
        <li><a href="Vendor_home_page.jsp">Home</a></li>
    </ul>
</div>
<%if(orders==null || orders.isEmpty()){ %>
<div id="NA"><h1>No Products Found</h1></div>
<% return;} %>
<%for(Orders order:orders){ %>
<div class="container">
    <div class="product-card">
        <div class="product-img">
        <%if(order.get_data("product_type").equals("clothing")){ %>
            <img src="Clothing product images/<%=order.get_data("product_img1")%>" alt="Product Image">
        <%}else if(order.get_data("product_type").equals("electronics")){ %>
        	<img src="Electronics product images/<%=order.get_data("product_img1")%>" alt="Product Image">
        <%}else if(order.get_data("product_type").equals("food")){ %>
        	<img src="Food product images/<%=order.get_data("product_img1")%>" alt="Product Image">
        <%} %>
        </div>
        <div class="product-info">
            <h2><%=order.get_data("product_type").toUpperCase() %> Product</h2>
            <p>Brand: <%=order.get_data("product_brand_name") %></p>
            <p>Name: <%=order.get_data("product_name") %></p>
            <p>Selling Price: ₹<%=order.get_data("product_selling_price") %></p>
            <p>Quantity: <%=order.get_data("product_quantity")%></p>
           	<p>Status: <span class="status-pending"><%=order.get_data("product_status").equals("0")?"Ordered":""%></span></p>
        </div>
        <div class="product-actions">
        <form action="VendorOperationsHandler?operation=update_new_product_orders_status" method="post">
        	<input type="hidden" value="alter_product_stock_status" name="request-type">
            <input type="hidden" value=<%=order.get_data("product_id")%> name="product-id">
            <input type="hidden" value=<%=order.get_data("product_type")%> name="product-type">
            <input type="hidden" value=<%=order.get_data("order_id") %> name="order-id">
            <select name="product-dispatch-status">
                <option value="1">Prepared</option>
                <option value="2">Cancel</option>
            </select>
            <button type="submit">Done</button>
            </form>
        </div>
    </div>
</div>	    	
<%}%>
<%

%>
</body>
</html>
