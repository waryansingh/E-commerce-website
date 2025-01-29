<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.entity.ClientPersonalDetails" %>
<% if(request.getSession().getAttribute("Admin-username")==null){response.sendRedirect("Admin_login_signup.jsp");return;}%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Client Management</title>
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
        }

        header {
            background-color: black;
            color: #ff8c00;
            padding: 15px;
            width: 100%;
            display: flex;
            justify-content: space-between;
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
            height: 40px;
            margin-right: 10px;
            font-size: 16px;
        }

        .search-container button {
            padding: 10px 20px;
            background-color: #ff8c00;
            color: #000;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            height: 40px;
        }

        .search-container button:hover {
            background-color: #ff5719;
        }

        .container {
            width: 100%;
            max-width: 1200px;
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
            border-radius: 50%;
            border: 1px solid #ff8c00;
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
        
        #NA{
        	color:black;
        	margin-top:250px;
        	margin-left:500px;
        }
    </style>
</head>
<body>
    <header>
        <h1>Client Management</h1>
        <div class="search-container">
            <input type="text" placeholder="Search clients by name or username...">
            <button type="button">Search</button>
        </div>
    </header>
    <div class="container">
<% 
@SuppressWarnings("unchecked")
ArrayList<ClientPersonalDetails> data_list=(ArrayList<ClientPersonalDetails>)request.getSession().getAttribute("datalist");
if(data_list!=null){
for(ClientPersonalDetails data:data_list){
%>
        <div class="card">
            <img src="placeholder-profile.jpg" alt="Profile Picture">
            <div class="card-content">
                <h2><%=data.get_data("client_fname")+" "+data.get_data("client_lname") %></h2>
                <p>Email: <%=data.get_data("client_email") %></p>
                <p>Phone: +91 <%=data.get_data("client_phone") %></p>
                <p>Username: <%=data.get_data("client_username") %></p>
                <p>Address Type: <%=data.get_data("client_email") %></p>
                <p>Address: <%=data.get_data("client_address_type") %></p>
                <p>City: <%=data.get_data("client_city") %></p>
                <p>State: <%=data.get_data("client_state") %></p>
                <p>Landmark: <%=data.get_data("client_landmark") %></p>
                <p>Zip Code: <%=data.get_data("client_zip_code") %></p>
            </div>
            <form action="AdminOperationsHandler?form-request-type=delete_client" class="card-actions" method="post">
            	<input type="hidden" name="client_id" value="<%=data.get_data("client_id")%>">
                <button type="submit" class="save-button">Delete</button>
            </form>
        </div>
<%}
data_list.clear();
data_list=null;
request.getSession().removeAttribute("datalist");
}else{%>
<div id="NA"><h1>No Clients Found</h1></div>
<%} %>
    </div>
</body>
</html>
