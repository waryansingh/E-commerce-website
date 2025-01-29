<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.entity.Orders" %>
<%if(request.getSession().getAttribute("Admin-username")==null){response.sendRedirect("error.jsp");return;}String msg=(String)request.getSession().getAttribute("msg");request.getSession().removeAttribute("msg");%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Orders</title>
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

        .orders-navbar {
            background-color: #000;
            padding: 10px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .orders-navbar h1 {
            color: #ff8c00;
            margin: 0;
        }

        .orders-navbar ul {
            list-style: none;
            margin: 0;
            padding: 0;
            display: flex;
        }

        .orders-navbar ul li {
            margin-left: 20px;
        }

        .orders-navbar ul li a {
            color: #ff8c00;
            font-weight: bold;
            text-decoration: none;
            transition: color 0.3s;
        }

        .orders-navbar ul li a:hover {
            color: #ff5719;
        }

        .orders-search-container {
            display: flex;
            align-items: center;
            margin: auto;
        }

        .orders-search-container input[type="text"] {
            padding: 10px;
            border: none;
            border-radius: 5px;
            width: 400px;
            margin-right: 10px;
        }

        .orders-search-container button {
            padding: 10px;
            background-color: #ff8c00;
            color: #000;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .orders-search-container button:hover {
            background-color: #ff5719;
        }

        .orders-container {
            width: 80%;
            margin: 20px auto;
            flex: 1;
        }

        .orders-card {
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

        .orders-info {
            flex: 3;
            display: flex;
            flex-direction: column;
        }

        .orders-info h2 {
            margin: 0;
            font-size: 20px;
            color: #ff8c00;
        }

        .orders-info p {
            margin: 5px 0;
            color: white;
        }

        .orders-actions {
            flex: 1;
            text-align: right;
            display: flex;
            flex-direction: column;
            align-items: flex-end;
            gap: 10px;
        }

        .orders-actions select {
            padding: 10px;
            background-color: #ff8c00;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            color: #000;
        }

        .orders-actions button {
            background-color: #ff8c00;
            color: #000;
            border: none;
            padding: 10px 15px;
            cursor: pointer;
            border-radius: 5px;
            font-size: 14px;
        }

        .orders-actions button:hover {
            background-color: #ff5719;
        }

        .orders-actions a {
            color: #ff8c00;
            font-weight: bold;
            text-decoration: none;
            padding: 10px;
        }

        .orders-actions a:hover {
            color: #ff5719;
        }

        .orders-footer {
            background-color: #000;
            color: #ff8c00;
            text-align: center;
            padding: 20px 0;
        }

        .orders-footer p {
            margin: 0;
        }

        .orders-notice {
            background-color: #e74c3c;
            color: white;
            padding: 15px;
            margin: auto;
            border-radius: 4px;
            text-align: center;
            font-weight: bold;
            width: 20%;
        }

        .orders-image {
            flex: 1;
            max-width: 200px;
            margin-right: 20px;
        }

        .orders-image img {
            width: 100%;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);
        }

        @media (max-width: 768px) {
            .orders-card {
                flex-direction: column;
            }

            .orders-actions {
                text-align: center;
                margin-top: 10px;
                flex-direction: column;
            }
        }

        #orders-NA {
            color: black;
            margin: auto;
        }
    </style>
</head>
<body>

<div class="orders-navbar">
    <h1>Order Items</h1>
    <div class="orders-search-container">
        <form action="AdminOperationsHandler" method="get">
        	<input type="hidden" name="request-type" value="search-order-items">
            <input type="text" placeholder="Search By Order Id or Reference code..." name="order-id">
            <button type="submit">Search</button>
        </form>
    </div>
</div>

<div class="orders-container">
    <%
    @SuppressWarnings("unchecked")
    ArrayList<Orders> items=(ArrayList<Orders>)request.getSession().getAttribute("orders-items");
    if(items!=null){
    	for(Orders item:items){%>
    <div class="orders-card">
        <div class="orders-image">
        <%if(item.get_data("product_type").equals("clothing")){%>
        <img src="Clothing product images/<%=item.get_data("product_img1") %>" alt="Product Image">
        <%}else if(item.get_data("product_type").equals("electronics")){%>
        <img src="Electronics product images/<%=item.get_data("product_img1") %>" alt="Product Image">
        <%}else if(item.get_data("product_type").equals("food")){%>
        <img src="Food product images/<%=item.get_data("product_img1") %>" alt="Product Image">
        <%}%>
        </div>
        <div class="orders-info">
            <p>Order ID: <strong><%=item.get_data("order_id") %></strong></p>
            <p>Product Price: <strong>₹<%=item.get_data("product_selling_price")%></strong></p>
            <p>Product Quantity: <strong><%=item.get_data("product_quantity")%></strong></p>
            <p>Total Price: <strong>₹<%=item.get_data("total_item_price")%></strong></p>
            <%if(item.get_data("product_status").equals("0")){%>
                <p>Status: <strong>Ordered</strong></p>
            <%}else if(item.get_data("product_status").equals("1")){%>
                <p>Status: <strong>Delivered</strong></p>
            <%}else if(item.get_data("product_status").equals("2")){%>
                <p>Status: <strong>Canceled</strong></p>
            <%}else if(item.get_data("product_status").equals("3")){%>
                <p>Status: <strong>Refunded</strong></p>
            <%}else if(item.get_data("product_status").equals("-1")){%>
                <p>Status: <strong>To be Refunded</strong></p>
            <%}%>
        </div>
        <div class="orders-actions">
            <form action="ClientFrontEndLoader" method="get">
            <input type="hidden" name="product-id" value="<%=item.get_data("product_id")%>">
    		<input type="hidden" name="product-type" value="<%=item.get_data("product_type")%>">
            <button type="submit">View</button>
        </form>
        </div>
    </div>
	<%}items.clear();items=null;request.getSession().removeAttribute("orders-items");}%>
</div>
</body>
</html>
