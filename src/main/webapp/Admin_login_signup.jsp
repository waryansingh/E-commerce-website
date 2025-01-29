<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.File" %>
<%@ page import="jakarta.servlet.annotation.MultipartConfig" %>
<%@ page import="jakarta.servlet.http.Part" %>
<%String msg=request.getParameter("msg");%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login & Signup</title>
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
            justify-content: center;
            align-items: center;
            height: 100vh;
            background: linear-gradient(135deg, #ff8c00, #000);
        }

        .container {
            width: 800px;
            height: 750px;
            background-color: #2c2c2c;
            border-radius: 15px;
            overflow: hidden;
            position: relative;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
        }

        .form-wrapper {
            display: flex;
            transition: transform 0.5s ease-in-out;
            width: 200%;
            height: 100%;
        }

        .form-container {
            width: 50%;
            padding: 50px 80px;
            box-sizing: border-box;
        }

        h2 {
            color: #ff8c00;
            margin-bottom: 30px;
            text-align: center;
        }

        input[type="text"],
        input[type="email"],
        input[type="password"],
        input[type="file"] {
            width: 100%;
            padding: 15px;
            margin: 10px 0;
            background-color: #333;
            border: 1px solid #555;
            color: #fff;
            border-radius: 5px;
        }

        label {
            color: #ff8c00;
            margin-bottom: 5px;
            display: block;
        }

        .btn {
            width: 100%;
            padding: 15px;
            background-color: #ff8c00;
            color: #000;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            margin-top: 20px;
        }

        .btn:hover {
            background-color: #ff5719;
        }

        .switch {
            text-align: center;
            margin-top: 20px;
        }

        .switch a {
            color: #ff8c00;
            text-decoration: none;
            font-weight: bold;
        }

        .switch a:hover {
            text-decoration: underline;
        }
        .signup-active {
            transform: translateX(-50%);
        }

        .login-active {
            transform: translateX(0%);
        }
        .signup-form {
            width: 55%;
            height: 100%;
            overflow-y: auto;
        }

        .form-row {
            display: flex;
            justify-content: space-between;
        }

        .form-row .form-group {
            width: 48%;
        }

        .upload-section {
            margin-top: 20px;
        }

        .upload-section label {
            font-weight: bold;
        }
        .checkbox-container {
            margin: 10px 0;
            display: flex;
            align-items: center;
        }

        .checkbox-container input[type="checkbox"] {
            margin-right: 10px;
        }

        .checkbox-container label {
            color: #ff8c00;
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
        
.popup-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.7); /* Darker overlay */
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
    background: #333; /* Dark background */
    padding: 20px;
    width: 90%;
    max-width: 400px;
    border-radius: 8px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5); /* Slightly darker shadow */
    text-align: center;
    position: relative;
}

.popup-content h3 {
    font-size: 1.8rem;
    color: #ff6f00; /* Keep the same accent color */
    margin-bottom: 10px;
}

.popup-content p {
    font-size: 1rem;
    color: #ddd; /* Lighter text for readability */
    margin-bottom: 20px;
}

.close-icon {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 1.5rem;
    background: none;
    border: none;
    color: #ff6f00; /* Keep the same accent color */
    cursor: pointer;
    transition: color 0.3s ease;
}

.close-icon:hover {
    color: #e65c00; /* Slightly darker hover effect */
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
    border: 1px solid #555; /* Darker border for dark theme */
    border-radius: 5px;
    outline: none;
    background-color: #444; /* Darker background for input */
    color: #fff; /* White text inside input */
    transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.security-code-input input:focus {
    border-color: #ff6f00; /* Keep the same accent color on focus */
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
    background-color: #e65c00; /* Slightly darker hover effect */
}
		.extra-link {
    		margin-top: 1.5rem;
    		font-size: 0.95rem;
    		color: #777;
		}

		.extra-link a {
    		color: #ff6f00;
    		margin-left: 240px;
    		text-decoration: none;
    		font-weight: bold;
    		transition: color 0.3s ease;
			}

		.extra-link a:hover {
    		color: #e65c00;
		}
        .forget-pass-popup-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    margin-left: -400px;
    background: rgba(0, 0, 0, 0.8); /* Black transparent background */
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
    transition: opacity 0.3s ease, transform 0.3s ease; /* Smooth appearance */
    opacity: 0;
    pointer-events: none;
}

.forget-pass-popup-overlay.active {
    opacity: 1;
    pointer-events: all;
    transform: scale(1); /* Ensure it's scaled to full size */
}

.forget-pass-popup-content {
    background-color: #1a1a1a; /* Dark background */
    border: 2px solid #ff8c00; /* Bright orange border */
    border-radius: 10px;
    padding: 20px;
    width: 500px;
    max-width: 90%; /* Adjust for smaller screens */
    text-align: center;
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.5);
    position: relative;
}

.close-icon {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 24px;
    background: none;
    border: none;
    color: #ff8c00; /* Orange close button */
    cursor: pointer;
    transition: color 0.3s;
}

.close-icon:hover {
    color: #ff6600; /* Slightly darker orange on hover */
}

.forget-pass-popup-content h3 {
    font-size: 24px;
    color: #ff8c00; /* Orange heading */
    margin-bottom: 15px;
}

.forget-pass-popup-content p {
    font-size: 16px;
    color: #d3d3d3; /* Light gray text */
    margin-bottom: 20px;
}

.security-code-input {
    margin: 20px 0;
}

.security-code-input input {
    width: 100%;
    padding: 12px;
    font-size: 16px;
    border: 2px solid #ff8c00;
    border-radius: 5px;
    background: #2a2a2a; /* Darker background */
    color: #ffffff;
    outline: none;
    transition: border-color 0.3s;
}

.security-code-input input:focus {
    border-color: #ff6600; /* Slightly darker orange */
    box-shadow: 0 0 10px rgba(255, 111, 0, 0.5);
}

.verify-button {
    padding: 10px 20px;
    font-size: 16px;
    color: #1a1a1a;
    background-color: #ff8c00;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
    font-weight: bold;
}

.verify-button:hover {
    background-color: #ff6600; /* Slightly darker orange */
}

/* Responsive Design for Popup */
@media (max-width: 500px) {
    .forget-pass-popup-content {
        width: 95%;
        padding: 15px;
    }

    .forget-pass-popup-content h3 {
        font-size: 20px;
    }

    .forget-pass-popup-content p {
        font-size: 14px;
    }
}
        
    </style>
</head>
<body>

    <div class="container">
        <div class="form-wrapper login-active" id="formWrapper">
            <div class="form-container login-form">
                <h2>Admin Login</h2>
                <%if(msg!=null){%>
                <div class="notice">
                    <%=msg%>
                </div>
                <%}%>
                <form action="AdminLoginHandler" method="POST">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="login-username" placeholder="Enter your username" required>
                    
                    <label for="password">Password</label>
                    <input type="password" id="password" name="login-password" placeholder="Enter your password" required>
                    
                    <button type="submit" class="btn" id="log-in">Login</button>
                    
                    <div class="switch">
                        <p>Don't have an account? <a href="javascript:void(0);" id="showSignup">Sign up here</a></p>
                    </div>
                </form>
            <div class="extra-link">
        			<a href="#" onclick="openForgetPasswordPopup()">Forgot Password?</a>
    		</div>
            </div>

    <div id="forgetPasswordPopup" class="forget-pass-popup-overlay">
    <div class="forget-pass-popup-content">
        <button id="closeForgetPasswordPopup" class="close-icon">&times;</button>
        <h3>Forgot Password</h3>
        <p>Please enter your registered email or phone number to receive OTP and change the password.</p>
        <form action="AdminOperationsHandler?form-request-type=admin-password-otp" method="post" id="forget-password-form" class="active">
            <div class="security-code-input">
                <input type="text" name="admin-email-phone" placeholder="Enter Email or Phone Number" required>
            </div>
            <button type="submit" class="verify-button">Submit</button>
        </form>
    </div>
</div>


            <div class="form-container signup-form">
                <h2>Admin Signup</h2>
                <%if(msg!=null){%>
                <div class="notice">
                    <%out.println(msg);%>
                </div>
                <%}%>
                <form action="AdminSignupHandler" method="POST" enctype="multipart/form-data">
                    <label for="firstname">First Name</label>
                    <input type="text" id="firstname" name="firstname" placeholder="Enter your first name" required>
                    
                    <label for="lastname">Last Name</label>
                    <input type="text" id="lastname" name="lastname" placeholder="Enter your last name" required>

                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" placeholder="Enter your email" required>
                    
                    <label for="phone">Phone</label>
                    <input type="text" id="phone" name="phone" placeholder="Enter your number" required>
                    
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" placeholder="Enter your username" required>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="password-signup">Password</label>
                            <input type="password" id="password-signup" name="password" placeholder="Create a password" required>
                        </div>
                        <div class="form-group">
                            <label for="confirmpassword">Confirm Password</label>
                            <input type="password" id="confirmpassword" name="confirmpassword" placeholder="Confirm your password" required>
                        </div>
                    </div>

                    <label for="address">Address</label>
                    <input type="text" id="address" name="address" placeholder="Enter your address" required>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="city">City</label>
                            <input type="text" id="city" name="city" placeholder="Enter your city" required>
                        </div>
                        <div class="form-group">
                            <label for="state">State</label>
                            <input type="text" id="state" name="state" placeholder="Enter your state" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="zip">Zip Code</label>
                            <input type="text" id="zip" name="zip" placeholder="Enter your zip code" required>
                        </div>
                    </div>

                    <div class="upload-section">
                        <label for="profile-pic">Profile Picture</label>
                        <input type="file" id="profile-pic" name="profile-pic" required>

                        <label for="govt-id">Government ID</label>
                        <input type="file" id="govt-id" name="gov-id" required>
                    </div>

                    <button type="submit" class="btn" id="sign-up">Sign Up</button>

                    <div class="switch">
                        <p>Already have an account? <a href="javascript:void(0);" id="showLogin">Login here</a></p>
                    </div>
                </form>
            </div>
        </div>
    </div>
<form action="AdminSignupHandler?type=otp" method="post" id="code-submit">
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

    document.getElementById('closeForgetPasswordPopup').addEventListener('click', closeForgetPasswordPopup);

        const formWrapper = document.getElementById('formWrapper');
        const showSignup = document.getElementById('showSignup');
        const showLogin = document.getElementById('showLogin');

        showSignup.addEventListener('click', function() {
            formWrapper.classList.add('signup-active');
            formWrapper.classList.remove('login-active');
        });

        showLogin.addEventListener('click', function() {
            formWrapper.classList.add('login-active');
            formWrapper.classList.remove('signup-active');
        });
        
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
