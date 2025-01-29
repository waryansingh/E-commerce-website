<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.entity.AdminPersonalDetails" %>
<%if(request.getSession().getAttribute("Admin-username")==null){response.sendRedirect("error.jsp");return;}String msg=(String)request.getSession().getAttribute("error");request.getSession().removeAttribute("msg");%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Member Management</title>
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
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: black;
    color: #ff8c00;
    padding: 0 20px;
    height: 70px; /* Consistent header height */
    width: 100%;
    position: relative;
}

header h1 {
    font-size: 24px;
    margin: 0;
    white-space: nowrap;
    flex-shrink: 0; /* Prevent shrinking */
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
    transition: background-color 0.3s ease;
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
    margin-bottom: 10px;
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

        #NA{
        	color:black;
        	margin-top:250px;
        	margin-left:500px;
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
	ArrayList<AdminPersonalDetails> data_list = (ArrayList<AdminPersonalDetails>)request.getSession().getAttribute("datalist");
	if (data_list != null) {
	    int counter = 1; // Initialize a counter for unique IDs
%>
    <header>
        <h1>Admin - Members</h1>
        <%if(msg!=null){%>
                <div class="notice">
                    <%=msg%>
                </div>
        <%}%>
        <div class="search-container">
        <form action="AdminOperationsHandler" method="GET">
        <input type="hidden" name="request-type" value="search-admins">
            <input type="text" name="search-val" placeholder="Search by name or email or username">
            <button type="submit">Search</button>
        </form>
    </div>
    </header>
    <div class="container">
    <%if(data_list==null || data_list.isEmpty()){ %>
<div id="NA"><h1>No Details Found</h1></div>
<% return;} %>
<%
String page_type=null;
String admin_status = null;
for(AdminPersonalDetails data : data_list){
	if(data.get_data("admin_username").equals((String)request.getSession().getAttribute("Admin-username")))continue;
    String profileImageId = "profileImage" + counter;
    String govIdImageId = "govIdImage" + counter;
    if(data.get_data("admin_status").equals("1")){
    	admin_status = "Approved";
    	page_type="registered_admins";
    }
    else if(data.get_data("admin_status").equals("0")){
    	admin_status = "Requested";
    	page_type="admin_requests";
    }
    else{
    	admin_status = "Blacklisted";
    	page_type="blacklisted_admins";
    }
%>
        <div class="card">
            <a href="#<%= profileImageId %>">
                <img src="Admin Profile Pictures/<%=data.get_data("admin_profile_picture") %>" alt="Profile-picture">
            </a>
            <div class="card-content">
                <h2><%=data.get_data("admin_full_name") %></h2>
                <p>Email: <%= data.get_data("admin_email") %></p>
                <p>Phone: <%= data.get_data("admin_phone") %></p>
                <p>User name: <%= data.get_data("admin_username") %></p>
                <p>Address: <%= data.get_data("admin_address") %></p>
                <p>City: <%= data.get_data("admin_city") %></p>
                <p>State: <%= data.get_data("admin_state") %></p>
                <p>Zip Code: <%= data.get_data("admin_zip_code") %></p>
                <p>Status updated by: <%= data.get_data("admin_status_updated") %></p>
                <p>Status: <span class="highlight"> <%=admin_status%></span></p><br>
                <a href="Admin Profile Pictures/<%=data.get_data("admin_profile_picture") %>" download="Profile_Picture-<%=data.get_data("admin_full_name")%>.jpg" class="save-button" >Download Trade License</a>
                <a href="Admin GovID/<%=data.get_data("admin_govId_picture") %>" download="Gov-ID-<%=data.get_data("admin_full_name")%>.jpg" class="save-button" >Download Gov ID</a>
            </div>
            <div class="card-content">
                <a href="#<%= govIdImageId %>">
                    <img src="Admin GovID/<%=data.get_data("admin_govId_picture") %>" alt="Gov-ID">
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
                <input type="hidden" value=<%=data.get_data("admin_id")%> name="admin_id">
                <input type="hidden" value="alter-admin-status" name="form-request-type">
                <input type="hidden" value=<%=page_type%> name="admin-return-page">
                <button type="submit" class="save-button">Save</button>
            </div>
            </form>
        </div>

        <!-- Profile Image Overlay -->
        <div id="<%= profileImageId %>" class="overlay">
            <img src="Admin Profile Pictures/<%=data.get_data("admin_profile_picture") %>" alt="Profile Full">
            <button class="close-btn">Close</button>
        </div>

        <!-- Gov ID Image Overlay -->
        <div id="<%= govIdImageId %>" class="overlay">
            <img src="Admin GovID/<%=data.get_data("admin_govId_picture") %>" alt="Gov ID Full">
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
