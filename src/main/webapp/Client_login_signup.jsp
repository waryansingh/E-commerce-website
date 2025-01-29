<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%String msg=request.getParameter("msg"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Client Login & Signup</title>
    <style>
        /* Global reset and base styling */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Arial', sans-serif;
        }

        body {
            background-color: #f4f4f9;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            color: #333;
            background: linear-gradient(135deg, #ff6f00 30%, #ffffff);
            overflow: hidden;
        }

        /* Container styling */
        .container {
            width: 800px; /* Increased width for more space */
            max-width: 90%;
            background-color: #ffffff;
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
            padding: 3rem 2.5rem;
            text-align: center;
            transition: transform 0.3s ease;
            overflow-y: auto; /* Make the container scrollable */
            height: 80vh; /* Allow container to be scrollable if the content exceeds height */
        }

        /* Header and toggle styling */
        .header {
            font-size: 1.8rem;
            font-weight: bold;
            margin-bottom: 1rem;
            color: #ff6f00;
        }

        .toggle-btn {
            display: flex;
            justify-content: space-around;
            background-color: #f0f0f0;
            border-radius: 10px;
            overflow: hidden;
            margin-bottom: 2rem;
            cursor: pointer;
        }

        .toggle-btn span {
            flex: 1;
            padding: 1rem 1.5rem; /* Increased padding for more clickable area */
            font-weight: bold;
            color: #666;
            transition: background-color 0.4s ease, color 0.3s ease;
        }

        .toggle-btn span.active {
            background-color: #ff6f00;
            color: #ffffff;
        }

        /* Form styling */
        form {
            display: none;
            opacity: 0;
            transition: opacity 0.4s ease;
            width: 100%;
            margin-top: 1.5rem;
        }

        form.active {
            display: block;
            opacity: 1;
        }

        .form-group {
            margin-bottom: 1.2rem;
        }

        .form-group input,
        .form-group select {
            width: 100%;
            padding: 1rem;
            margin: 0.9rem 0;
            border: 1px solid #ddd;
            border-radius: 12px;
            font-size: 1rem;
            transition: all 0.3s ease;
        }

        .form-group input[type="text"]:focus,
        .form-group input[type="email"]:focus,
        .form-group input[type="password"]:focus,
        .form-group input[type="number"]:focus,
        .form-group select:focus {
            border-color: #ff6f00;
            outline: none;
            box-shadow: 0 0 8px rgba(255, 111, 0, 0.3);
        }

        /* Grouped inputs styling for Signup */
        .signup-fields {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            gap: 1.2rem;
        }

        .signup-fields .form-group {
            width: calc(50% - 0.6rem); /* 2 columns layout */
        }

        .signup-fields .full-width {
            width: 100%;
        }

        .address-section {
            margin-top: 2rem;
            text-align: left;
            font-size: 1.2rem;
            font-weight: bold;
            margin-bottom: 1rem;
        }

        /* Submit button */
        .submit-btn {
            width: 100%;
            padding: 1.2rem;
            background-color: #ff6f00;
            color: #fff;
            border: none;
            border-radius: 12px;
            font-size: 1.1rem;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s ease;
            margin-top: 1.5rem;
        }

        .submit-btn:hover {
            background-color: #e65c00;
            transform: translateY(-3px);
        }

        /* Extra link styling */
        .extra-link {
            margin-top: 1.5rem;
            font-size: 0.95rem;
            color: #777;
        }

        .extra-link a {
            color: #ff6f00;
            text-decoration: none;
            font-weight: bold;
            transition: color 0.3s ease;
        }

        .extra-link a:hover {
            color: #e65c00;
        }

        /* Remember Me checkbox */
        .remember-me {
            display: flex;
            align-items: center;
            margin-top: 1rem;
        }

        .remember-me input {
            margin-right: 0.5rem;
        }
        
        .popup-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 1000;
            opacity: 0;
            pointer-events: none;
            transition: opacity 0.3s ease;
        }

        .popup-overlay.active {
            opacity: 1;
            pointer-events: all;
        }

        .popup-content {
            background: #fff;
            padding: 20px;
            width: 90%;
            max-width: 400px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
            text-align: center;
            position: relative;
        }

        .popup-content h3 {
            font-size: 1.8rem;
            color: #ff6f00;
            margin-bottom: 10px;
        }

        .popup-content p {
            font-size: 1rem;
            color: #333;
            margin-bottom: 20px;
        }

        .close-icon {
            position: absolute;
            top: 10px;
            right: 10px;
            font-size: 1.5rem;
            background: none;
            border: none;
            color: #ff6f00;
            cursor: pointer;
            transition: color 0.3s ease;
        }

        .close-icon:hover {
            color: #e65c00;
        }

        /* Security Code Input */
        .security-code-input {
            margin: 15px 0;
        }

        .security-code-input input {
            width: 100%;
            max-width: 300px;
            padding: 10px;
            font-size: 1.1rem;
            text-align: center;
            border: 1px solid #ccc;
            border-radius: 5px;
            outline: none;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        .security-code-input input:focus {
            border-color: #ff6f00;
            box-shadow: 0 0 8px rgba(255, 111, 0, 0.5);
        }

        .verify-button {
            padding: 10px 20px;
            font-size: 1rem;
            background-color: #ff8f00;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .verify-button:hover {
            background-color: #e65c00;
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
        /* Forgot Password Link */
		.extra-link {
    		margin-top: 1.5rem;
    		font-size: 0.95rem;
    		color: #777;
		}

		.extra-link a {
    		color: #ff6f00;
    		text-decoration: none;
    		font-weight: bold;
    		transition: color 0.3s ease;
			}

		.extra-link a:hover {
    		color: #e65c00;
		}
		/* Popup Overlay */
.popup-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
    opacity: 0;
    pointer-events: none;
    transition: opacity 0.3s ease;
}

.popup-overlay.active {
    opacity: 1;
    pointer-events: all;
}

/* Popup Content */
.popup-content {
    background: #fff;
    padding: 20px;
    width: 90%;
    max-width: 400px;
    border-radius: 8px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
    text-align: center;
    position: relative;
}

.popup-content h3 {
    font-size: 1.8rem;
    color: #ff6f00;
    margin-bottom: 10px;
}

.popup-content p {
    font-size: 1rem;
    color: #333;
    margin-bottom: 20px;
}

.popup-content .security-code-input input {
    width: 100%;
    max-width: 300px;
    padding: 10px;
    font-size: 1.1rem;
    text-align: center;
    border: 1px solid #ccc;
    border-radius: 5px;
    outline: none;
    transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.popup-content .security-code-input input:focus {
    border-color: #ff6f00;
    box-shadow: 0 0 8px rgba(255, 111, 0, 0.5);
}

.popup-content .verify-button {
    padding: 10px 20px;
    font-size: 1rem;
    background-color: #ff8f00;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.popup-content .verify-button:hover {
    background-color: #e65c00;
}

.popup-content .close-icon {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 1.5rem;
    background: none;
    border: none;
    color: #ff6f00;
    cursor: pointer;
    transition: color 0.3s ease;
}

.popup-content .close-icon:hover {
    color: #e65c00;
}
		
        
    </style>
</head>
<body>

<div class="container">
    <div class="header">Welcome</div>

    <!-- Toggle buttons for Login and Signup -->
    <div class="toggle-btn">
        <span id="loginToggle" onclick="showForm('login')" class="active">Login</span>
        <span id="signupToggle" onclick="showForm('signup')">Sign Up</span>
    </div>
	
	<%if(msg!=null){%>
    	<div class="notice">
        	<%=msg%>
        </div>
    <%}%>
	
    <!-- Login Form -->
    <form action="ClientLoginHandler" id="loginForm" class="active" method="post">
        <div class="form-group">
            <input type="text" placeholder="Username" name="client-login-username" required>
        </div>
        <div class="form-group">
            <input type="password" placeholder="Password" name="client-login-password" required>
        </div>
        <div class="remember-me">
            <input type="checkbox" id="rememberMe" name="client-remember-me">
            <label for="rememberMe">Remember Me</label>
        </div>
        <button type="submit" class="submit-btn">Login</button>
    	<div class="extra-link">
        <a href="#" onclick="openForgetPasswordPopup()">Forgot Password?</a>
    	</div>
    </form>
    
    <div id="forgetPasswordPopup" class="popup-overlay">
    <div class="popup-content">
        <button id="closeForgetPasswordPopup" class="close-icon">&times;</button>
        <h3>Forgot Password</h3>
        <p>Please enter your registered email or phone number to receive OTP and change the password.</p>
        <form action="ClientOperationsHandler?operation=client-password-otp" method="post" id="forget-password-form" class="active">
            <div class="security-code-input">
                <input type="text" name="client-email-phone" placeholder="Enter Email or Phone Number" required>
            </div>
            <button type="submit" class="verify-button">Submit</button>
        </form>
    </div>
	</div>

    <!-- Signup Form -->
    <form action="ClientSignupHandler" id="signupForm" method="post">
        <div class="form-group">
            <!-- First and Last Name Fields (2 columns) -->
            <div class="signup-fields">
                <div class="form-group">
                    <input type="text" placeholder="First Name" name="client-first-name" required>
                </div>
                <div class="form-group">
                    <input type="text" placeholder="Last Name" name="client-last-name" required>
                </div>
            </div>
            <input type="email" placeholder="Email" name="client-email" required>
            <input type="text" placeholder="Phone number" name="client-phone-number" required>
            <input type="text" placeholder="Username" name="client-username" required>
            <input type="password" placeholder="Password" name="client-password" required>
            <input type="password" placeholder="Confirm Password" name="client-confirm-password" required>
        </div>

        <!-- Address Section Heading -->
        <div class="address-section">Address</div>
        
        <div class="form-group">
            <select name="client-address-type" required>
                <option disabled selected>Select Address Type</option>
                <option value="Home">Home</option>
                <option value="Work">Office</option>
            </select>
        </div>
        
        <!-- Grouped Address Fields (2 columns) -->
        <div class="signup-fields">
            <div class="form-group">
                <input type="text" placeholder="Street Address" name="client-address" required>
            </div>
            <div class="form-group">
                <input type="text" placeholder="City" name="client-city" required>
            </div>
            <div class="form-group">
                <input type="text" placeholder="State" name="client-state" required>
            	<input type="text" placeholder="Land Mark" name="client-landmark" required>
            </div>
            <div class="form-group">
                <input type="text" placeholder="Zip Code" name="client-zip-code" required>
            </div>
        </div>

        <button type="submit" class="submit-btn">Sign Up</button>
    </form>
</div>
<form action="ClientSignupHandler?type=otp" method="post" id="code-submit">
	<input type="hidden" maxlength="6" name="security-code" id="code-to-send">
</form>
<%if(request.getSession().getAttribute("Enter_otp")!=null){ request.getSession().removeAttribute("Enter_otp");%>
<div id="popup" class="popup-overlay active">
    <div class="popup-content">
        <button id="closePopup" class="close-icon">&times;</button>
        <h3>Enter OTP</h3>
        <p>Please enter the 6-digit security code to proceed.</p>
        <div class="security-code-input">
        	<input type="text" id="securityCode" maxlength="6" placeholder="Enter code">
        </div>
            <button class="verify-button" id="assign-code">Verify</button>
    </div>
</div>

<%}%>
<script>
function openForgetPasswordPopup() {
    document.getElementById('forgetPasswordPopup').classList.add('active');
}

function closeForgetPasswordPopup() {
    document.getElementById('forgetPasswordPopup').classList.remove('active');
}

document.addEventListener("DOMContentLoaded", function () {
    const closePopup = document.getElementById('closeForgetPasswordPopup');
    closePopup.addEventListener("click", closeForgetPasswordPopup);
});

    function showForm(formType) {
        // Toggle active class on forms and buttons based on the form type
        document.getElementById('loginForm').classList.toggle('active', formType === 'login');
        document.getElementById('signupForm').classList.toggle('active', formType === 'signup');
        document.getElementById('loginToggle').classList.toggle('active', formType === 'login');
        document.getElementById('signupToggle').classList.toggle('active', formType === 'signup');
    }
    
    function submitForm(form) {
	    document.getElementById(form).submit();
  	}
    
    document.getElementById("assign-code").addEventListener("click",()=>{
    	document.getElementById("code-to-send").value=document.getElementById("securityCode").value;
    	submitForm('code-submit');
    });
    
    document.addEventListener("DOMContentLoaded", function () {
        const closePopup = document.getElementById("closePopup");
        // Hide popup on close button click
        closePopup.addEventListener("click", function () {
            popup.classList.remove("active");
        });
    });
</script>

</body>
</html>
