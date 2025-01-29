<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.entity.VendorPersonalDetails" %>
<%if(request.getSession().getAttribute("Admin-username")==null){response.sendRedirect("error.jsp");return;}String msg=(String)request.getSession().getAttribute("msg");request.getSession().removeAttribute("msg");%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vendor - Management</title>
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
            justify-content: flex-start;
            align-items: center;
            height: 100vh;
            background: linear-gradient(135deg, #ff8c00, #000);
            overflow: hidden; /* Prevent background from scrolling */
        }

        header {
            background-color: black;
            color: #ff8c00;
            padding: 15px;
            width: 100%;
            display: flex;
            align-items: center;
            position: relative;
        }

        h1 {
            margin: 0;
            font-size: 24px;
            white-space: nowrap;
            flex-shrink: 0;
        }

        .search-container {
            position: absolute;
            left: 50%;
            transform: translateX(-50%);
            display: flex;
            align-items: center;
        }

        .search-container input[type="text"] {
            padding: 10px;
            border: none;
            border-radius: 5px;
            width: 350px;
            margin-right: 10px;
        }

        .search-container button {
            padding: 10px;
            background-color: #ff8c00;
            color: #000;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .search-container button:hover {
            background-color: #ff5719;
        }

        .container {
            width: 100%;
            max-width: 1200px;
            flex-grow: 1;
            padding: 20px;
            overflow-y: auto;
        }

        .card {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background-color: #2c2c2c;
            margin-bottom: 5px;
            padding: 20px;
            border-radius: 8px;
            transition: box-shadow 0.3s ease;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
        }

        .card:hover {
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
        }

        .card img {
            max-width: 80px;
            max-height: 80px;
            border-radius: 8px;
            border: 1px solid #ff8c00;
            cursor: pointer;
            transition: transform 0.3s ease;
        }

        .card img:hover {
            transform: scale(1.1);
        }

        .card-content {
            flex-grow: 1;
            margin-left: 20px;
        }

        .card-content h2 {
            font-size: 20px;
            margin: 0;
            color: #ff8c00;
        }

        .card-content p {
            margin: 5px 0;
            color: #fff;
        }

        .card-content .highlight {
            color: #ff8c00;
            font-weight: bold;
        }

        .card-actions {
            display: flex;
            flex-direction: column;
            align-items: flex-end;
        }

        select {
            padding: 10px;
            background-color: #333;
            color: white;
            border: 1px solid #555;
            border-radius: 5px;
            margin-bottom: 10px;
            width: 150px;
        }

        .save-button {
            background-color: #ff8c00;
            color: black;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s ease;
        }

        .save-button:hover {
            background-color: #ff5719;
        }

        .overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.8);
            justify-content: center;
            align-items: center;
            z-index: 9999;
        }

        .overlay img {
            max-width: 90%;
            max-height: 90%;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(255, 140, 0, 0.6);
        }

        .overlay .close-btn {
            position: absolute;
            top: 10px;
            right: 10px;
            background-color: #ff8c00;
            color: black;
            border: none;
            padding: 10px;
            border-radius: 5px;
            cursor: pointer;
        }

        .overlay:target {
            display: flex;
        }

        .notice {
            background-color: #e74c3c;
            color: white;
            padding: 15px;
            margin: auto;
            border-radius: 4px;
            text-align: center;
            font-weight: bold;
            width: 20%;
        }

        a {
            text-decoration: none;
        }
        
    </style>
    <script>
    	document.addEventListener("DOMContentLoaded", () => {
        const closeButtons = document.querySelectorAll(".close-btn");
        closeButtons.forEach((btn) => {
            btn.addEventListener("click", (event) => {
                    window.history.back();
            });
        });
    });
</script>

</head>
<body>
<%
	@SuppressWarnings("unchecked")
	ArrayList<VendorPersonalDetails> data_list = (ArrayList<VendorPersonalDetails>)request.getSession().getAttribute("datalist");
	if (data_list != null) {
	    int counter = 1; // Initialize a counter for unique IDs
%>
    <header>
        <h1>Vendor - Members</h1>
        <%if(msg!=null){%>
                <div class="notice">
                    <%=msg%>
                </div>
        <%}%>
        <div class="search-container">
        <form action="AdminOperationsHandler" method="get">
            <input type="text" placeholder="Enter name or username or email..." name="search-val">
            <input type="hidden" value="search-vendors" name="request-type">
            <button type="submit">Search</button>
        </form>
    </div>
    </header>
    <div class="container">
<%
String page_type=null;
String vendor_status = null;
for(VendorPersonalDetails data : data_list){
    String tradeImageId = "tradeImage" + counter;
    String govIdImageId = "govIdImage" + counter;
    if(data.get_data("vendor_status").equals("1")){
    	vendor_status = "Approved";	
    	page_type="registered_vendors";
    }
    else if(data.get_data("vendor_status").equals("0")){
    	vendor_status = "Requested";
    	page_type="vendor_requests";
    }
    else{
    	page_type="blacklisted_vendors";
    	vendor_status = "Blacklisted";	
    }
%>
        <div class="card">
            
            <div class="card-content">
                <h2><%=data.get_data("vendor_full_name") %></h2>
                <p>Email: <%= data.get_data("vendor_email") %></p>
                <p>Phone: <%= data.get_data("vendor_phone") %></p>
                <p>User name: <%= data.get_data("vendor_username") %></p>
                <p>Address: <%= data.get_data("vendor_address") %></p>
                <p>City: <%= data.get_data("vendor_city") %></p>
                <p>State: <%= data.get_data("vendor_state") %></p>
                <p>Zip Code: <%= data.get_data("vendor_zip_code") %></p>
                <h2>Company/Shop Address</h2>
                <p>Shop Name/Number: <%= data.get_data("vendor_shop_name_number") %></p>
                <p>Address: <%= data.get_data("vendor_shop_address") %></p>
                <p>City: <%= data.get_data("vendor_shop_city") %></p>
                <p>State: <%= data.get_data("vendor_shop_state") %></p>
                <p>Zip Code: <%= data.get_data("vendor_shop_zip_code") %></p>
                <p>Status updated by: <%= data.get_data("vendor_status_updated") %></p>
                <p>GST Number: <span class="highlight"> <%= data.get_data("vendor_gst_number") %></span></p>
                <p>Status: <span class="highlight"> <%=vendor_status%></span></p><br>
                <a href="Vendor Trade License/<%=data.get_data("vendor_trade_license") %>" download="Trade-License-<%=data.get_data("vendor_full_name")%>.jpg" class="save-button" >Download Trade License</a>
                <a href="Vendor GovID/<%=data.get_data("vendor_govId_picture") %>" download="Gov-ID-<%=data.get_data("vendor_full_name")%>.jpg" class="save-button" >Download Gov ID</a>
            </div>
            <a href="#<%= tradeImageId %>">
                <img src="Vendor Trade License/<%=data.get_data("vendor_trade_license") %>" alt="Trade-license">
            </a><br>
            <div class="card-content">
                <a href="#<%= govIdImageId %>">
                    <img src="Vendor GovID/<%=data.get_data("vendor_govId_picture") %>" alt="Gov-ID">
                </a>
            </div>
            <form action="AdminOperationsHandler" method="POST">
            <div class="card-actions">
                <select name="status_option">
                    <option value="1">Approve</option>
                    <option value="0">Reject</option>
                    <option value="2">Delete</option>
                    <option value="3">Blacklist</option>
                </select>
                <input type="hidden" value=<%=data.get_data("vendor_id")%> name="vendor_id">
                <input type="hidden" value="alter-vendor-status" name="form-request-type">
                <input type="hidden" value=<%=page_type%> name="vendor-return-page">
                <button type="submit" class="save-button">Save</button>
            </div>
            </form>
        </div>

        <!-- Profile Image Overlay -->
        <div id="<%= tradeImageId %>" class="overlay">
            <img src="Vendor Trade License/<%=data.get_data("vendor_trade_license") %>" alt="Trade License Full">
            <button class="close-btn">Close</button>
        </div>

        <!-- Gov ID Image Overlay -->
        <div id="<%= govIdImageId %>" class="overlay">
            <img src="Vendor GovID/<%=data.get_data("vendor_govId_picture") %>" alt="Gov ID Full">
            <button class="close-btn">Close</button>
        </div>

<% counter++;
}
data_list.clear();
data_list=null;
request.getSession().removeAttribute("datalist");
}
%>
    </div>
</body>
</html>
