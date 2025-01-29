<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%if(request.getSession().getAttribute("user_type_before_otp_verify")==null){response.sendRedirect("Client_login_signup.jsp");return;}%>
<%String msg=request.getParameter("msg");%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Arial', sans-serif;
        }

        body {
            background: linear-gradient(135deg, #000 30%, #fff);
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            color: #fff;
        }

        .container {
            background: #1c1c1c;
            padding: 3rem;
            border-radius: 16px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.5);
            text-align: center;
            width: 600px;
        }

        .header {
            font-size: 2rem;
            font-weight: bold;
            color: #ff6f00;
            margin-bottom: 1.5rem;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-group input {
            width: 100%;
            padding: 1.2rem;
            font-size: 1.1rem;
            border: 1px solid #444;
            border-radius: 10px;
            background: #2a2a2a;
            color: #fff;
            outline: none;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        .form-group input:focus {
            border-color: #ff6f00;
            box-shadow: 0 0 10px rgba(255, 111, 0, 0.7);
        }

        .submit-btn {
            background-color: #ff6f00;
            color: #fff;
            padding: 1.2rem;
            border: none;
            border-radius: 10px;
            font-size: 1.1rem;
            cursor: pointer;
            width: 100%;
            margin-top: 1.5rem;
            transition: background-color 0.3s ease;
        }

        .submit-btn:hover {
            background-color: #e65c00;
        }

        .notice {
            background-color: #e74c3c;
            color: white;
            padding: 1rem;
            margin-bottom: 1rem;
            border-radius: 8px;
            font-weight: bold;
        }

        @media (max-width: 768px) {
            .container {
                width: 90%;
                padding: 2rem;
            }

            .header {
                font-size: 1.8rem;
            }

            .form-group input {
                font-size: 1rem;
            }

            .submit-btn {
                font-size: 1rem;
            }
        }
    </style>
</head>
<body>
<div class="container">
	<%if(msg!=null){%>
    	<div class="notice">
        	<%=msg%>
        </div>
    <%}%>
    <div class="header">Reset Password</div>
	<%if(request.getSession().getAttribute("user_type_before_otp_verify").equals("client") && request.getSession().getAttribute("change_forgotten_password")==null){%>
    <form id="otpForm" action="ClientOperationsHandler?operation=validate-forget-pass-otp" method="post">
        <div class="form-group">
            <input type="text" name="forgot-pass-otp" id="otp" placeholder="Enter OTP" maxlength="6" required>
        </div>
        <button type="submit" class="submit-btn">Verify OTP</button>
    </form>
	<%}else if(request.getSession().getAttribute("user_type_before_otp_verify").equals("admin") && request.getSession().getAttribute("change_forgotten_password")==null){ %>
    <form id="otpForm" action="AdminOperationsHandler?form-request-type=validate-forget-pass-otp" method="post">
        <div class="form-group">
            <input type="text" name="forgot-pass-otp" id="otp" placeholder="Enter OTP" maxlength="6" required>
        </div>
        <button type="submit" class="submit-btn">Verify OTP</button>
    </form>
	<%}else if(request.getSession().getAttribute("user_type_before_otp_verify").equals("vendor") && request.getSession().getAttribute("change_forgotten_password")==null){ %>
    <form id="otpForm" action="VendorOperationsHandler?operation=validate-forget-pass-otp" method="post">
        <div class="form-group">
            <input type="text" name="forgot-pass-otp" id="otp" placeholder="Enter OTP" maxlength="6" required>
        </div>
        <button type="submit" class="submit-btn">Verify OTP</button>
    </form>
	<%}%>
	
	<%if(request.getSession().getAttribute("user_type_before_otp_verify").equals("client") && (request.getSession().getAttribute("change_forgotten_password")!=null && request.getSession().getAttribute("change_forgotten_password").equals("client"))){ %>
    <form id="resetPasswordForm" action="ClientOperationsHandler?operation=update-fogotten-password" method="post">
        <div class="form-group">
            <input type="password" name="new-password" id="newPassword" placeholder="New Password" required>
        </div>
        <div class="form-group">
            <input type="password" name="confirm-new-password" id="confirmPassword" placeholder="Confirm Password" required>
        </div>
        <button type="submit" class="submit-btn">Reset Password</button>
    </form>
	<%}else if(request.getSession().getAttribute("user_type_before_otp_verify").equals("admin") && (request.getSession().getAttribute("change_forgotten_password")!=null && request.getSession().getAttribute("change_forgotten_password").equals("admin"))){ %>
    <form id="resetPasswordForm" action="AdminOperationsHandler?form-request-type=update-fogotten-password" method="post">
        <div class="form-group">
            <input type="password" name="new-password" id="newPassword" placeholder="New Password" required>
        </div>
        <div class="form-group">
            <input type="password" name="confirm-new-password" id="confirmPassword" placeholder="Confirm Password" required>
        </div>
        <button type="submit" class="submit-btn">Reset Password</button>
    </form>
	<%}else if(request.getSession().getAttribute("user_type_before_otp_verify").equals("vendor") && (request.getSession().getAttribute("change_forgotten_password")!=null && request.getSession().getAttribute("change_forgotten_password").equals("vendor"))){ %>
    <form id="resetPasswordForm" action="VendorOperationsHandler?operation=update-fogotten-password" method="post">
        <div class="form-group">
            <input type="password" name="new-password" id="newPassword" placeholder="New Password" required>
        </div>
        <div class="form-group">
            <input type="password" name="confirm-new-password" id="confirmPassword" placeholder="Confirm Password" required>
        </div>
        <button type="submit" class="submit-btn">Reset Password</button>
    </form>
	<%} %>
</div>
</body>
</html>
