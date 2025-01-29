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
<%
@SuppressWarnings("unchecked")
ArrayList<Orders> orders=(ArrayList<Orders>)request.getSession().getAttribute("orders-data");
%>
<div class="orders-navbar">
    <h1>Orders Management</h1>
    <%if(msg!=null){%>
                <div class="orders-notice">
                    <%=msg%>
                </div>
        <%}%>
    <div class="orders-search-container">
    <form action="AdminOperationsHandler" method="get">
       	<input type="text" placeholder="Search By Order Id or Reference code..." name="order-id-code">
        <input type="hidden" value="search-order" name="request-type">
        <button type="submit">Search</button>
    </form>
    </div>
</div>
<%if(orders==null || orders.isEmpty()){ %>
<div id="orders-NA"><h1>No Orders Found</h1></div>
<% return;} %>
<%String page_type=null;
for(Orders order:orders){%>
<div class="orders-container">
    <div class="orders-card">
        <div class="orders-info">
        	<p>Order Reference Code: <%=order.get_data("order_reference_code")%></p>
            <p>Order id: <%=order.get_data("order_id") %></p>
            <p>Order Date: <%=order.get_data("order_date")%></p>
            <p>Total Price: ₹<%=order.get_data("total_amount") %></p>
            <p>Payment ID/Mode: <%=order.get_data("payment_id") %></p>
            <p>Client Address: <%=order.get_data("client_personal_address")%></p>
            <%if(order.get_data("client_fav_address1")!=null){ %>
            <p>Favorite Address 1: <%=order.get_data("client_fav_address1")%></p>
            <%}if(order.get_data("client_fav_address2")!=null){%>
            <p>Favorite Address 2: <%=order.get_data("client_fav_address2")%></p>
            <%}if(order.get_data("client_fav_address3")!=null){%>
            <p>Favorite Address 3: <%=order.get_data("client_fav_address3")%></p>
            <%}%>
            <%if(order.get_data("order_status").equals("0")){%>
                <p>Status: <span class="orders-status-pending">Ordered</span></p>
                <div class="orders-actions">
        		<form action="AdminOperationsHandler?form-request-type=update-orders" method="post">
        		<input type="hidden" value="<%=order.get_data("order_id")%>" name="order-id">
            	<select name="order_status_option">
                	<option value="2">Cancel</option>
            	</select>
            	<button type="submit">Save</button>
        		</form>
        		</div>
            <%}else if(order.get_data("order_status").equals("1")){%>
                <p>Status: <span class="orders-status-pending">Delivered</span></p>
            <%}else if(order.get_data("order_status").equals("2")){%>
                <p>Status: <span class="orders-status-approved">Canceled</span></p>
            <%}else if(order.get_data("order_status").equals("3")){%>
                <p>Status: <span class="orders-status-approved">Refunded</span></p>
            <%}else if(order.get_data("order_status").equals("-1")){%>
                <p>Status: <span class="orders-status-approved">To be Refunded</span></p>
                <div class="orders-actions">
        		<form action="AdminOperationsHandler?form-request-type=update-orders" method="post">
        		<input type="hidden" value="<%=order.get_data("order_id")%>" name="order-id">
            		<select name="order_status_option">
                		<option value="1">Cancel Request</option>
                		<option value="3">Refund</option>
            		</select>
            	<button type="submit">Save</button>
        		</form>
        		</div>
            <%}%>
        </div>
        
    </div>
</div>
<%} %>
<%
orders.clear();
orders=null;
request.getSession().removeAttribute("productlist"); 
%>
</body>
</html>
