<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.Cart" %>
<%@ page import="com.entity.AllProducts" %>
<%@ page import="com.entity.ClientPersonalDetails" %>
<%@ page import="com.entity.ProductToBuy" %>
<%@ page import="com.entity.ClientFavouriteAddress" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%String msg=request.getParameter("msg");%>
<%if(request.getSession().getAttribute("Client_username")==null || request.getSession().getAttribute("Client_id")==null || (int)request.getSession().getAttribute("Client_id")==-1){response.sendRedirect("Client_login_signup.jsp");return;} %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout Page</title>
    <style>
        body {
            font-family: 'Poppins', Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f6f9;
            color: #333;
        }

        .top-bar {
            background: linear-gradient(to right, #ff7f3f, #ff5a1e);
            color: #fff;
            padding: 10px 20px;
            text-align: center;
            font-size: 22px;
            font-weight: bold;
            letter-spacing: 1px;
        }

        .checkout-container {
            width: 1300px;
            margin: 20px auto 50px;
            background: #fff;
            border-radius: 15px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            border: 1px solid #ddd;
        }

        .header {
            background: linear-gradient(to right, #ff7f3f, #ff5a1e);
            color: #fff;
            padding: 20px;
            text-align: center;
            font-size: 26px;
            font-weight: 600;
            letter-spacing: 1px;
        }

        .form-container {
            padding: 30px 25px;
        }

        .product-container, .address-section, .payment-section {
            margin-bottom: 20px;
            border: 1px solid #ddd;
            border-radius: 10px;
            padding: 20px;
            background-color: #f9f9f9;
        }

        .product {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }

        .product:last-child {
            border-bottom: none;
        }

        .product img {
            width: 100px;
            height: 100px;
            border-radius: 8px;
            margin-right: 15px;
        }

        .product-info {
            flex-grow: 1;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .product-name {
        	width: 200px;
            font-size: 16px;
            font-weight: 500;
        }

        .product-price {
        	width: 100px;
        	text-align: center;
            font-size: 15px;
            color: #555;
        }
		
		.product-quantity {
			margin-left: -100px;
            font-size: 15px;
            color: #555;
        }

        .summary-container {
            background-color: #fff7f0;
            padding: 50px 25px;
            border: 1px solid #ffd1a1;
            border-radius: 10px;
            margin-top: 20px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }

        .summary-container h3 {
            margin: 0 0 10px;
            font-size: 20px;
            font-weight: bold;
            color: #ff7f3f;
        }

        .summary-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin: 10px 0;
            font-size: 15px;
        }

        .summary-item strong {
            font-weight: 600;
        }

        .mystery-item {
            margin: 10px 0;
            display: flex;
            align-items: center;
        }

        .mystery-item input {
            margin-right: 10px;
        }

        .btn-checkout, .btn-save-address  {
            display: block;
            width: 100%;
            padding: 15px;
            font-size: 18px;
            font-weight: 600;
            color: #fff;
            background: linear-gradient(to right, #ff7f3f, #ff5a1e);
            border: none;
            border-radius: 8px;
            cursor: pointer;
            text-align: center;
            text-transform: uppercase;
            transition: background 0.3s ease, transform 0.2s ease;
            box-shadow: 0 4px 12px rgba(255, 90, 30, 0.3);
            margin-top: 10px;
        }
		.remove-random-item, .add-random-item{
			display: block;
            width: auto;
            margin-top: 5px;
            padding: 5px;
            font-size: 18px;
            color: #fff;
            background: linear-gradient(to right, #ff7f3f, #ff5a1e);
            border: none;
            border-radius: 8px;
            cursor: pointer;
            text-align: center;
            text-transform: uppercase;
            transition: background 0.3s ease, transform 0.2s ease;
            box-shadow: 0 4px 12px rgba(255, 90, 30, 0.3);
		}
        .remove-random-item:hover, .btn-checkout:hover, .btn-save-address:hover {
            background: linear-gradient(to right, #ff9f5f, #ff7f3f);
            transform: translateY(-2px);
        }

        .footer {
            text-align: center;
            padding: 20px;
            font-size: 13px;
            color: #777;
            margin-top: 20px;
            background-color: #f8f9fa;
            border-top: 1px solid #ddd;
        }

        .footer span {
            color: #ff7f3f;
            font-weight: 600;
        }
        .new-address-container select {
            width: 100%;
            padding: 10px;
            margin-bottom: 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 8px;
            background-color: #fff;
        }

        .address-title, .payment-title {
            font-size: 18px;
            font-weight: bold;
            color: #333;
            margin-bottom: 10px;
        }

        .address-option, .payment-option {
            margin-bottom: 15px;
        }

        .address-option label, .payment-option label {
            font-size: 14px;
            margin-left: 8px;
            color: #555;
        }

        .new-address-container {
            display: none;
            margin-top: 10px;
            border: 1px dashed #ff7f3f;
            padding: 15px;
            border-radius: 8px;
            background: #fff7f0;
        }

        .new-address-container input, .new-address-container textarea {
            width: 98%;
            padding: 10px;
            margin-bottom: 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 8px;
        }
        
        .new-address-container textarea {
            resize: vertical;
        }
         .product-container, .address-section, .payment-section {
            margin-bottom: 20px;
            border: 1px solid #ddd;
            border-radius: 10px;
            padding: 20px;
            background-color: #f9f9f9;
        }
        .note {
    background-color: #ff4d4d;
    color: #fff;
    padding: 15px;
    border-radius: 8px;
    margin: 20px 0;
    font-size: 1rem;
    font-weight: bold;
    line-height: 1.5;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}
.note p {
    margin: 0;
}
.payment-section {
    margin-bottom: 20px;
    border: 1px solid #ddd;
    border-radius: 10px;
    padding: 20px;
    background-color: #f9f9f9;
}

.payment-title {
    font-size: 18px;
    font-weight: bold;
    color: #333;
    margin-bottom: 10px;
}

.payment-option {
    margin-bottom: 15px;
}

.payment-option label {
    font-size: 14px;
    margin-left: 8px;
    color: #555;
}
.notice {
            background-color: #e74c3c;
            color: white;
            padding: 15px;
            margin-bottom: 10px;
            border-radius: 4px;
            text-align: center;
            font-weight: bold;
            width: 100%;
        }
        
    </style>
</head>
<body>
    <div class="top-bar">Urban Groove</div>
    <div class="checkout-container">
        <div class="header">Checkout</div>
        <%if(msg!=null){%>
            <div class="notice">
            	<%=msg%>
        	</div>
        <%}%>
        <div class="form-container">
            <div class="product-container">
                <h3>Product items</h3>
<%double amount=0.0;
@SuppressWarnings("unchecked")
ArrayList<ProductToBuy> products=(ArrayList<ProductToBuy>)request.getSession().getAttribute("checkout-products");
	if(products!=null){
	for(ProductToBuy product1:products){amount+=Double.parseDouble(product1.get_data("total_price"));%>
	<%if(product1.get_data("product_type").equals("clothing")){%>
			<div class="product">
                    <img src="Clothing product images/<%=product1.get_data("product_img")%>" alt="Product 1">
                    <div class="product-info">
                        <span class="product-name"><%=product1.get_data("product_name")%><br><br>Size: <%=product1.get_data("product_size")%></span>
                        <h5 class="product-quantity">Quantity: <%=product1.get_data("product_quantity")%></h5>
                        <span class="product-price"><strong>₹<%=product1.get_data("total_price")%></strong></span>
                    </div>
            </div>
	<%} else if(product1.get_data("product_type").equals("electronics")){%>
			<div class="product">
                    <img src="Electronics product images/<%=product1.get_data("product_img")%>" alt="Product 1">
                    <div class="product-info">
                        <span class="product-name"><%=product1.get_data("product_name")%></span>
                        <h5 class="product-quantity">Quantity: <%=product1.get_data("product_quantity")%></h5>
                        <span class="product-price"><strong>₹<%=product1.get_data("total_price")%></strong></span>
                    </div>
            </div>
	<%} else if(product1.get_data("product_type").equals("food")){%>
			<div class="product">
                    <img src="Food product images/<%=product1.get_data("product_img")%>" alt="Product 1">
                    <div class="product-info">
                        <span class="product-name"><%=product1.get_data("product_name")%></span>                        
                        <h5 class="product-quantity">Quantity: <%=product1.get_data("product_quantity")%></h5>
                        <span class="product-price"><strong>₹<%=product1.get_data("total_price")%></strong></span>
                    </div>
            </div>
	<%} %>
	<%}}%>
	</div>
            <div class="address-section">
            <h3 class="address-title">Shipping Address</h3>
    <%
    @SuppressWarnings("unchecked")
    HashMap<String,ClientPersonalDetails> selected_address=(HashMap<String,ClientPersonalDetails>)request.getSession().getAttribute("selected_address");
    @SuppressWarnings("unchecked")
    ArrayList<ClientPersonalDetails> all_address=(ArrayList<ClientPersonalDetails>)request.getSession().getAttribute("client-address");
    if(all_address!=null){int i=0;
    for(ClientPersonalDetails address:all_address){%>
                <div class="address-option">
                    <h5>Address Type: <%=address.get_data("client_address_type").toUpperCase()%></h5>
                    <form action="ClientOperationsHandler?operation=manage-address" method="post" id="manage-address<%=i%>">
                    <%if(selected_address!=null && selected_address.containsKey(address.get_data("client_address_id"))){ %>
                    <input type="radio" name="address" id="address<%=i %>" onclick="toggleNewAddress(false); submitForm('manage-address<%=i%>');" value="<%=address.get_data("client_address_id")%>" checked><label for="address<%=i%>"><%=address.get_data("client_address")+", "+address.get_data("client_landmark")+", "+address.get_data("client_zip_code")+", "+address.get_data("client_city")+", "+address.get_data("client_state")%></label>
                	<%}else{%>
                	<input type="radio" name="address" id="address<%=i %>" onclick="toggleNewAddress(false); submitForm('manage-address<%=i%>');" value="<%=address.get_data("client_address_id")%>"><label for="address<%=i%>"><%=address.get_data("client_address")+", "+address.get_data("client_landmark")+", "+address.get_data("client_zip_code")+", "+address.get_data("client_city")+", "+address.get_data("client_state")%></label>
                	<%} %>
                	</form>
                </div>
	<%i++;}}%>
				<div class="address-option">
                    <input type="radio" id="newAddress" name="address" onclick="toggleNewAddress(true)">
                    <label for="newAddress">Add New Address</label>
                </div>
                
                <div class="new-address-container">
                <form action="ClientOperationsHandler?operation=add-new-address-from-checkout-page" method="post">
                    <select name="address-type">
                        <option value="" disabled selected>Select Address Type</option>
                        <option value="home">Home</option>
                        <option value="work">Work</option>
                    </select>
                    <textarea placeholder="Address" rows="2" name="address"></textarea>
                    <input type="text" placeholder="City" name="city"/>
                    <input type="text" placeholder="State" name="state"/>
                    <input type="text" placeholder="Zip Code" name="zip-code"/>
                    <input type="text" placeholder="Landmark" name="land-mark"/>
                    <button class="btn-save-address">Save Address</button>
                </form>
                </div>
	
	<%
	@SuppressWarnings("unchecked")
	HashMap<String,ClientFavouriteAddress> selected_fav_address=(HashMap<String,ClientFavouriteAddress>)request.getSession().getAttribute("fav-address-selected");
	@SuppressWarnings("unchecked")
	ArrayList<ClientFavouriteAddress> items=(ArrayList<ClientFavouriteAddress>)request.getSession().getAttribute("client-fav-address");
    if(items!=null && !items.isEmpty()){int i=0;%>
    <h3 class="address-title">Shipping to Your Favourite Address</h3>
	<div class="note">
    <p>
        <strong>Note:</strong> 
        <ul>
        <li>If you choose to deliver orders to your favorite addresses, the same quantity of each product will be sent to each selected favorite address, in addition to being delivered to you. 
        For example, if you select one favorite address, the existing quantities of all items will be delivered both to you and to that address. 
        Please verify your selected addresses and quantities before proceeding to checkout.</li><br>
    	<li>If you choose to add a mystery item to your order, it will only be delivered to you and will not be sent to any of the selected favorite addresses, even if you have chosen them for the rest of your order.</li>
    	</ul>
    </p>
	</div>
   	<%for(ClientFavouriteAddress address:items){%>
   				<div class="address-option">
   				<form action="ClientOperationsHandler?operation=manage-favourite-address" method="post" id="manage-fav-address<%=i%>">
                    <h5>Address Type: <%=address.get_data("client_fav_address_type").toUpperCase()%></h5>
                    <%if(selected_fav_address!=null && selected_fav_address.containsKey(address.get_data("client_fav_address_id"))){%>
                    <input type="checkbox" name="fav-address" id="fav-address<%=i %>" onclick="submitForm('manage-fav-address<%=i%>')" checked><label for="fav-address<%=i%>"><%=address.get_data("client_fav_address")+", "+address.get_data("client_fav_landmark")+", "+address.get_data("client_fav_zip_code")+", "+address.get_data("client_fav_city")+", "+address.get_data("client_fav_state")%></label>
                	<%}else{ %>
                	<input type="checkbox" name="fav-address" id="fav-address<%=i %>" onclick="submitForm('manage-fav-address<%=i%>')"><label for="fav-address<%=i%>"><%=address.get_data("client_fav_address")+", "+address.get_data("client_fav_landmark")+", "+address.get_data("client_fav_zip_code")+", "+address.get_data("client_fav_city")+", "+address.get_data("client_fav_state")%></label>
                	<%} %>
                	<input type="hidden" value="<%=address.get_data("client_fav_address_id")%>" name="fav-address-id">
                </form>
                </div>
   	<%i++;}}%>
            </div>
            
            <form action="ClientCheckoutHandler?operation=create-order-payment" method="post" id="payment-option">
            <div class="payment-section">
    			<h3 class="payment-title">Payment Method</h3>
    		<div class="payment-option">
    		<%if(request.getSession().getAttribute("payment_option")!=null && request.getSession().getAttribute("payment_option").equals("cod")){ %>
        		<input type="radio" id="cod" name="payment-method" value="cash-on-delivery" onclick="submitForm('payment-option');" checked>
        		<label for="cod">Cash on Delivery (COD)</label>
        	<%}else{%>
        	<input type="radio" id="cod" name="payment-method" value="cash-on-delivery" onclick="submitForm('payment-option');">
        	<label for="cod">Cash on Delivery (COD)</label>
        	<%} %>
    		</div>
    		<div class="payment-option">
    		<%if(request.getSession().getAttribute("payment_option")!=null && request.getSession().getAttribute("payment_option").equals("pay-now")){ %>
        		<input type="radio" id="pay-now" name="payment-method" value="pay-now" onclick="submitForm('payment-option');" checked>
        		<label for="pay-now">Pay Now</label>
        	<%}else{ %>
        	<input type="radio" id="pay-now" name="payment-method" value="pay-now" onclick="submitForm('payment-option');">
        	<label for="pay-now">Pay Now</label>
        	<%} %>
    		</div>
			</div>
			</form>
          
            <div class="summary-container">
                <h3>Order Summary</h3>
                <div class="summary-item">
                    <span>Subtotal</span>
                    <span><strong>₹<%=amount%></strong></span>
                </div>
                <div class="summary-item">
                    <span>Shipping</span>
                    <strong>₹20.00</strong>
                </div>
                <div class="mystery-item">
                <form action="ClientOperationsHandler?operation=add-random-item" method="post" id="random-product">
                <%if(request.getSession().getAttribute("random-food-product")==null){request.getSession().removeAttribute("random-food-product");%>
                	<label for="mysteryItem">Add a mystery food item within ₹50</label>
                	<br><button class="add-random-item" type="submit">Add</button>
                <%}%>
                </form>
                <form action="ClientOperationsHandler?operation=remove-random-item" method="post">
                <%if(request.getSession().getAttribute("random-food-product")!=null){request.getSession().removeAttribute("random-food-product");%>
                	<label for="mysteryItem">Add a mystery food item within ₹50</label>
                	<br><button class="remove-random-item" type="submit">Remove</button>
                <%}%>
                </form>
                </div>
                <div class="summary-item">
                    <span>Total</span>
                    <span class="total-price"><strong>₹<%=(amount+20)%></strong></span>
                </div>
            </div>
            <%if(request.getSession().getAttribute("payment_option")!=null && request.getSession().getAttribute("payment_option").equals("pay-now")){request.getSession().removeAttribute("payment_option");%>
            <button class="btn-checkout" id="rzp-button1">Place Order</button>
            <%}else{ %>
            <form action="ClientCheckoutHandler?operation=process-order" method="post" id="process-order">
            <button class="btn-checkout" id="checkout-but" type="submit">Place Order</button>
            </form>
            <%}%>
        </div>
    </div>
    <div class="footer">
        <p>Thank you for shopping with <span>Urban Groove</span>!</p>
    </div>
</body>
<script src="https://checkout.razorpay.com/v1/checkout.js"></script>
<script>
		function addAddress(addressId){
			document.getElementById("personal-address-id").value=addressId;
		}
        function toggleNewAddress(show) {
            document.querySelector('.new-address-container').style.display = show ? 'block' : 'none';
        }
        function submitForm(form) {
    	    document.getElementById(form).submit();
      	}
        <%ClientPersonalDetails details=(ClientPersonalDetails)request.getSession().getAttribute("client_personal_details");%>
        <%if(details!=null){%>
        var options = {
        		 "key": "rzp_test_Xq2JJkU6biRFF2",
        		 "amount": <%=(amount+20)*100%>, 
        		 "currency": "INR",
        		 "name": "Urban Groove",
        		 "description": "Transaction",
        		 "image": "Logo/logo",
        		 "order_id": "<%=(String)request.getSession().getAttribute("orderId")%>",
        		 "handler": function (response){
        		 //alert(response.razorpay_payment_id);
        		 //alert(response.razorpay_order_id);
        		 //alert(response.razorpay_signature);
        		 
        		 var xhr = new XMLHttpRequest();
        		    xhr.open('POST', 'ClientCheckoutHandler?operation=process-order', true);
        		    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        		    xhr.send('payment_id=' + response.razorpay_payment_id + '&order_id=' + response.razorpay_order_id);
        		    xhr.onload = function() {
        		    	if (xhr.status == 200) {
        		      		window.location.href = 'ClientFrontEndLoader?operation=load-orders';
        		    	}
        		    }
        		 },
        		 "prefill": {
        		 "name": "<%=details.get_data("client_fname")+" "+details.get_data("client_lname")%>",
        		 "email": "<%=details.get_data("client_email")%>",
        		 "contact": "<%=details.get_data("client_phone")%>"
        		 },
        		 "notes": {
        		 "address": "Urban Groove Office"
        		 },
        		 "theme": {
        		 "color": "#3399cc"
        		 }
        		};
        		var rzp1 = new Razorpay(options);
        		rzp1.on('payment.failed', function (response){
        		 alert(response.error.code);
        		 alert(response.error.description);
        		 alert(response.error.source);
        		 alert(response.error.step);
        		 alert(response.error.reason);
        		 alert(response.error.metadata.order_id);
        		 alert(response.error.metadata.payment_id);
        		});
        		document.getElementById('rzp-button1').onclick = function(e){
        		 rzp1.open();
        		 e.preventDefault();
        		}
			<%}%>
    </script>
</html>
