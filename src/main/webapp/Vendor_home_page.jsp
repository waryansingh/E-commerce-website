<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%if(request.getSession().getAttribute("Vendor-username")==null){response.sendRedirect("Vendor_login_signup.jsp");return;}%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vendor Dashboard</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background-color: #333; /* Gray background */
            color: #fff;
            display: flex;
            flex-direction: column;
        }

        a {
            text-decoration: none;
            color: inherit;
            font-weight: bold;
        }

        a:hover {
            color: #ff5719; 
        }

        .navbar {
            background-color: #1e1e1e;
            color: #ff8c00;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
        }

        .navbar h1 {
            margin: 0;
            font-size: 1.8em;
        }

        .navbar ul {
            list-style: none;
            margin: 0;
            padding: 0;
            display: flex;
        }

        .navbar ul li {
            margin-left: 25px;
        }

        .hero {
            background-color: #1e1e1e;
            color: #fff;
            padding: 60px 20px;
            text-align: center;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.5);
        }

        .hero h2 {
            font-size: 2.2em;
            margin-bottom: 15px;
        }

        .hero p {
            font-size: 1.2em;
            margin-bottom: 30px;
            color: #e0e0e0;
        }

        .hero a {
            background-color: #ff8c00;
            padding: 15px 40px;
            color: #000;
            font-weight: bold;
            border-radius: 5px;
            transition: background-color 0.3s;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
        }

        .hero a:hover {
            background-color: #ff5719;
        }

        .company-types {
            padding: 50px 20px;
            background-color: #333; /* Gray section */
            text-align: center;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.5);
        }

        .company-types h3 {
            font-size: 2em;
            margin-bottom: 30px;
            color: #ff8c00;
        }

        .company-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 30px;
        }

        .company-item {
            background-color: #1e1e1e;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            transition: transform 0.3s, box-shadow 0.3s;
        }

        .company-item:hover {
            transform: translateY(-8px);
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
        }

        .company-item h3 {
            margin-bottom: 15px;
            color: #ff8c00;
        }

        .company-item p {
            margin-bottom: 15px;
            color: #d3d3d3;
        }

        .company-item a {
            background-color: #ff8c00;
            padding: 12px 50px;
            color: #000;
            font-weight: bold;
            border-radius: 5px;
            transition: background-color 0.3s;
        }

        .company-item a:hover {
            background-color: #ff5719;
        }

        .top-selling {
            padding: 50px 20px;
            background-color: #333; /* Gray section */
            text-align: center;
        }

        .top-selling h3 {
            font-size: 2em;
            margin-bottom: 30px;
            color: #ff8c00;
        }

        .carousel {
            height: 350px;
            position: relative;
            width: 800px;
            margin: auto;
            overflow: hidden;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }

        .carousel-images img {
            width: 100%;
            height: auto;
            display: none;
        }

        .carousel-images img:first-child {
            display: block;
        }

        .footer {
            background-color: #1e1e1e;
            color: #ff8c00;
            text-align: center;
            padding: 25px 0;
            margin-top: 40px;
        }

        .footer p {
            margin: 0;
            font-size: 1.1em;
        }
        
        .notification-badge {
    		background-color: red; /* Red background */
    		color: white; /* White text color */
    		border-radius: 50%; /* Circular shape */
    		padding: 5px 10px; /* Padding around the text */
    		font-size: 0.8em; /* Font size */
    		position: absolute; /* Positioning */
    		top: 0; /* Aligns at the top */
    		right: 10px; /* Adjusts position relative to the link */
	}
    </style>
</head>
<body>

<div class="navbar">
    <h1>Vendor Dashboard</h1>
    <ul>
        <li><a href="VendorOperationsHandler?request-type=all_products">Your Products</a></li>
        <li><a href="VendorOperationsHandler?request-type=to_dispatch">Orders to Dispatch</a></li>
        <li><a href="VendorOperationsHandler?request-type=vendor-logout">Logout</a></li>
    </ul>
</div>

<div class="hero">
    <h2>Welcome to the Vendor Dashboard</h2>
    <p>Manage your products and track performance effortlessly</p>
</div>

<div class="company-types">
    <h3>Select Your Product Type</h3>
    <div class="company-grid">
        <div class="company-item">
            <h3>Clothing</h3>
            <p>Upload clothing products</p>
            <a href="Clothing_page.jsp">Upload</a>
        </div>
        <div class="company-item">
            <h3>Electronics & Accessories</h3>
            <p>Upload electronics and accessories</p>
            <a href="Electronics_page.jsp">Upload</a>
        </div>
        <div class="company-item">
            <h3>Food</h3>
            <p>Upload food products</p>
            <a href="Food_page.jsp">Upload</a>
        </div>
    </div>
</div>

<div class="top-selling">
</div>

<script>
    const images = document.querySelectorAll('.carousel-images img');
    let currentIndex = 0;

    function showNextImage() {
        images[currentIndex].style.display = 'none'; 
        currentIndex = (currentIndex + 1) % images.length; 
        images[currentIndex].style.display = 'block';
    }

    setInterval(showNextImage, 4000);
</script>
</body>
</html>
