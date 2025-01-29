<%@ page import="com.ecom.AdminFrontEndLoader"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.AdminFrontEndLoaderData" %>
<% if(request.getSession().getAttribute("Admin-username")==null){response.sendRedirect("Admin_login_signup.jsp");return;}
AdminFrontEndLoaderData data=(AdminFrontEndLoaderData)request.getSession().getAttribute("admin-front-end-data");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background-color: #1e1e1e;
            color: #fff;
            display: flex;
            height: 100vh;
            background: linear-gradient(135deg, #ff8c00, #000);
            background-repeat: no-repeat; 
    		background-attachment: fixed; 
    		overflow: auto;
        }

        .sidebar {
            width: 250px;
            background-color: #333;
            height: 100%;
            padding: 20px 15px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.5);
            position: fixed;
        }

        .sidebar h2 {
            color: #ff8c00;
            text-align: center;
            margin-bottom: 30px;
        }

        .sidebar ul {
            list-style-type: none;
        }

        .sidebar ul li {
            margin: 20px 0;
        }

        .sidebar ul li a {
            text-decoration: none;
            width: 100%;
            color: #fff;
            font-size: 18px;
            display: block;
            padding: 10px 20px;
            background-color: #444;
            border-radius: 5px;
            transition: background-color 0.3s ease;
            cursor: pointer;
        }

        .sidebar ul li a:hover {
            background-color: #ff8c00;
            color: #000;
        }

        .main-content {
            margin-left: 270px;
            padding: 30px;
            width: calc(100% - 270px);
        }

        .header {
            background-color: #333;
            color: #ff8c00;
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
        }

        .header h1 {
            margin: 0;
            font-size: 24px;
        }

        .header button {
            padding: 10px 15px;
            background-color: #ff8c00;
            border: none;
            color: #000;
            font-weight: bold;
            border-radius: 5px;
            cursor: pointer;
        }

        .dashboard-section, .manage-users, .product-management, .orders, .reports, .settings, .profile {
            display: none;
        }

        .dashboard-section.active, .manage-users.active, .product-management.active, .orders.active, .reports.active, .settings.active, .profile.active {
            display: block;
        }

        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 20px;
        }

        .dashboard-card {
            background-color: #2c2c2c;
            padding: 30px;
            border-radius: 10px;
            text-align: center;
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
        }

        .dashboard-card h3, h1{
            color: #ff8c00;
            margin-bottom: 20px;
        }

        .dashboard-card p {
            font-size: 18px;
            color: #ccc;
        }

        .dashboard-card a {
            padding: 10px 20px;
            background-color: #ff8c00;
            border: none;
            color: #000;
            font-weight: bold;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 20px;
        }

        .dashboard-card button:hover {
            background-color: #ff5719;
        }
        
        #logout {
            background-color: #444;
            color: #fff;
            font-size: 18px;
            display: block;
            padding: 5px 5px;
            border-radius: 5px;
            width: 80px;
            transition: background-color 0.3s ease;
            margin-top: 350px;
        }

        #logout:hover {
            background-color: #cc0000;
            color: white;
            cursor: pointer;
        }

        .profile-details {
            display: flex;
            flex-direction: column;
            gap: 20px;
            margin-top: 20px;
        }

        .profile-details .dashboard-card {
            background-color: #2c2c2c;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .profile-details h3 {
            color: #ff8c00;
        }

        .profile-details p {
            color: #ccc;
            line-height: 1.8; /* Increased line spacing */
        }

        .profile-details img {
            width: 20%;
            height:5%;
            border-radius: 10px;
            transition: transform 0.2s;
        }

        .profile-details img:hover {
            transform: scale(1.05);
        }
.sales-container {
    max-width: 100%;
    margin: 20px 0;
    background-color: #2c2c2c;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
    color: #fff;
    max-height: 400px; /* Set a maximum height */
    overflow-y: auto;  /* Enable vertical scrolling */
}

.sales-container h2 {
    font-size: 1.8rem;
    color: #ff8c00;
    text-align: center;
    font-weight: bold;
    margin-bottom: 20px;
}

.sales-card {
    background-color: #333;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
    text-align: center;
}

.sales-card h3 {
    font-size: 1.4rem;
    color: #ff8c00;
    font-weight: bold;
    margin-bottom: 10px;
}

.sales-amount {
    font-size: 1.6rem;
    color: #fff;
    font-weight: bold;
}

.sales-card:hover {
    background-color: #444;
    transform: translateY(-2px);
    transition: background-color 0.3s ease, transform 0.2s ease;
}


.btn-primary {
    background-color: #007bff;
    border: none;
    padding: 10px 15px;
    text-decoration: none;
    color: white;
    border-radius: 5px;
}

.btn-primary:hover {
    background-color: #0056b3;
}

      
        
        #dash-admin-details{
        	margin-bottom: 20px;
        }
        #dash-admin-details p{
        	font: 25px solid white;
        }
        #images-top-products{
        	margin-top: 20px;
        }
	#but{
            padding: 10px 20px;
            background-color: red;
            border: none;
            color: white;
            font-weight: bold;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 20px;
        }
    </style>
    <script>
    function submitForm(form) {
	    document.getElementById(form).submit();
  	}
    window.addEventListener('pageshow',(event)=>{
        if (event.persisted) {
            window.location.href='AdminFrontEndLoader';
        }
    });
        function showSection(sectionClass) {
            document.querySelectorAll('.main-content > div').forEach((section) => {
                section.classList.remove('active');
            });
            document.querySelector('.' + sectionClass).classList.add('active');
        }
    </script>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Admin Panel</h2>
        <ul>
            <li><a id="admin-dashboard" onclick="showSection('dashboard-section')">Dashboard</a></li>
            <li><a id="admin-manage-users" onclick="showSection('manage-users')">Manage Users</a></li>
            <li><a id="admin-product-management" onclick="showSection('product-management')">Product Management</a></li>
            <li><a id="admin-orders" onclick="showSection('orders')">Orders</a></li>
            <li><a id="admin-settings" onclick="showSection('settings')">Settings</a></li>
            <li><a id="admin-profile" onclick="showSection('profile')">Profile</a></li>
            <li><a href="AdminOperationsHandler?request-type=log-out" id="logout">Log Out</a></li>
        </ul>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Dashboard Section -->
        <div class="dashboard-section active">
            <div class="header">
                <h1>Dashboard</h1>
            </div>
        
            <div class="dashboard-card" id="dash-admin-details">
                    <h1>Hi, <%=data.get_profile("admin_full_name")%></h1>
                    <p>Welcome back, You have full access to manage products, vendors, orders, and customer inquiries. Let's ensure everything is running smoothly.</p>
                </div>
            
            <div class="dashboard-grid">
                <div class="dashboard-card">
                    <h3>Clothing Products</h3>
                    <b><%=data.get_count("clothing_product") %></b>
                </div>
                <div class="dashboard-card">
                    <h3>Electronics and Accessories Products</h3>
                    <b><%=data.get_count("electronics_product") %></b>
                </div>
                <div class="dashboard-card">
                    <h3>Food Products</h3>
                    <b><%=data.get_count("food_product") %></b>
                </div>
            </div>
        
        </div>
		<%if(data!=null){ %>
        <!-- Manage Users Section -->
        <div class="manage-users">
            <div class="header">
                <h1>Manage Users</h1>
            </div>
            <div class="dashboard-grid">
                <div class="dashboard-card">
                    <h3>Registered Admins</h3>
                    <p><%=data.get_count("registered_admins")%></p><br>
                    <a href="AdminOperationsHandler?request-type=registered_admins">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Registered Vendors</h3>
                    <p><%=data.get_count("registered_vendors")%></p><br>
                    <a href="AdminOperationsHandler?request-type=registered_vendors">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Admin Requests</h3>
                    <p><%=data.get_count("admin_requests")%></p><br>
                    <a href="AdminOperationsHandler?request-type=admin_requests">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Vendors Requests</h3>
                    <p><%=data.get_count("vendor_requests")%></p><br>
                    <a href="AdminOperationsHandler?request-type=vendor_requests">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Blacklisted Vendors</h3>
                    <p><%=data.get_count("blacklisted_vendors") %></p><br>
                    <a href="AdminOperationsHandler?request-type=blacklisted_vendors">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Blacklisted Admins</h3>
                    <p><%=data.get_count("blacklisted_admins") %></p><br>
                    <a href="AdminOperationsHandler?request-type=blacklisted_admins">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Customers</h3>
                    <p><%=data.get_count("registered_clients") %></p><br>
                    <a href="AdminOperationsHandler?request-type=all_clients">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Blog Posts</h3>
                    <p><%=data.get_count("blog_posts")%></p><br>
                    <a href="AdminOperationsHandler?request-type=all_blogs">View</a>
                </div>
            </div>
        </div>
        <!-- Product Management Section -->
        <div class="product-management">
            <div class="header">
                <h1>Product Management</h1>
            </div>
            <div class="dashboard-grid">
                <div class="dashboard-card">
                    <h3>Total Approved Products</h3>
                    <p><%=data.get_count("registered_products") %></p><br>
                    <a href="AdminOperationsHandler?request-type=registered_products">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Total Requested Products</h3>
                    <p><%=data.get_count("product_request") %></p><br>
                    <a href="AdminOperationsHandler?request-type=product_requests">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Total Blacklisted Products</h3>
                    <p><%=data.get_count("blacklisted_products") %></p><br>
                    <a href="AdminOperationsHandler?request-type=products_blacklisted">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Clothing Products</h3>
                    <p><%=data.get_count("clothing_product") %></p><br>
                    <a href="AdminOperationsHandler?request-type=clothing_products">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Electronic Products</h3>
                    <p><%=data.get_count("electronics_product") %></p><br>
                    <a href="AdminOperationsHandler?request-type=electronics_products">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Food Products</h3>
                    <p><%=data.get_count("food_product") %></p><br>
                    <a href="AdminOperationsHandler?request-type=food_products">View</a>
                </div>
            </div>
        </div>

        <div class="orders">
            <div class="header">
                <h1>Orders</h1>
            </div>
            <div class="dashboard-grid">
                <div class="dashboard-card">
                    <h3>Delivered Orders</h3>
                    <p><%=data.get_count("delivered_orders") %></p><br>
                    <a href="AdminOperationsHandler?request-type=delivered-orders">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Pre-Delivery Orders</h3>
                    <p><%=data.get_count("pre_delivery_orders") %></p><br>
                    <a href="AdminOperationsHandler?request-type=pre-delivery-orders">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Refund request Orders</h3>
                    <p><%=data.get_count("order_refund_request") %></p><br>
                    <a href="AdminOperationsHandler?request-type=refund-request-orders">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Refunded Orders</h3>
                    <p><%=data.get_count("order_refunded") %></p><br>
                    <a href="AdminOperationsHandler?request-type=refunded-orders">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Canceled Orders</h3>
                    <p><%=data.get_count("order_canceled") %></p><br>
                    <a href="AdminOperationsHandler?request-type=canceled-orders">View</a>
                </div>
                <div class="dashboard-card">
                    <h3>Order Items</h3><br>
                    <a href="Admin_order_items.jsp">Search</a>
                </div>
            </div>
        </div>
        <!-- Settings Section -->
        <div class="settings">
            <div class="header">
                <h1>Settings</h1>
            </div>
            <div class="dashboard-grid">
                <div class="dashboard-card">
                    <form action="AdminOpearationsHandler?form-request-type=delete-account" method="post">
                    	<button id="but">Delete Account</button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Profile Section -->
        <div class="profile">
            <div class="header">
                <h1>Profile</h1>
            </div>
            <div class="profile-details">
                <div class="dashboard-card">
                    <div>
                        <h3>Profile Details</h3>
                        <p><strong>Name:</strong> <%=data.get_profile("admin_full_name")%></p>
                        <p><strong>Email:</strong> <%=data.get_profile("admin_email")%></p>
                        <p><strong>Phone Number:</strong> (+91) <%=data.get_profile("admin_phone") %></p>
                        <p><strong>Username:</strong> <%=(String)request.getSession().getAttribute("Admin-username")%></p>
                        <p><strong>Address:</strong> <%=data.get_profile("admin_address")%></p>
                    </div>
                    <div>
                        <h3>Documents</h3>
                        <div style="margin-top: 10px;">
                            <strong>Profile Picture:</strong><br>
                            <img src="Admin Profile Pictures/<%=data.get_profile("admin_profile_picture")%>" alt="Profile Picture">                      
                        </div>
                        <div style="margin-top: 10px;">
                            <strong>Government ID:</strong><br>
                            <img src="Admin GovID/<%=data.get_profile("admin_govId_picture")%>" alt="Government ID">
                        </div>
                        <%} data=null;%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
