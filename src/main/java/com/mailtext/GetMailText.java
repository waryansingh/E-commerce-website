package com.mailtext;
final public class GetMailText {
final public static class ClientMailText{
	private static String client_security_mail_text="""
			Dear [Name],

				We hope this message finds you well. For security purposes, we’ve generated a One-Time Password (OTP) to verify your recent request.

				Your OTP: {OTP_CODE}

				This OTP is valid for the next 30 seconds. Please use it promptly to complete your action.

				If you did not request this OTP, please ignore this email or contact our support team immediately at teamurbangroove@gmail.com.

				Thank you for choosing Urban Groove. We value your trust and are committed to ensuring your data remains secure.

			Warm regards,
				The Urban Groove Team
			""";
	private static String client_mail_text_deletion="""
			Dear [Name],

				We regret to inform you that your client account on Urban Groove has been permanently deleted. As a result, you will no longer have access to the platform, and all associated data, including your blog posts, has been removed.

				If you believe this action was taken in error or would like to appeal the decision, please reach out to us at teamurbangroove@gmail.com. We are open to reconsidering the deletion based on your feedback.

				Thank you for your understanding.

			Warm regards,
				The Urban Groove Team
			""";
	private static String client_mail_text_order_placed="""
			Dear [Name],

				Thank you for placing your order with UrbanGroove Your order has been successfully placed and is now being processed. Below are the details of your order:

				Order Details
				
				Order Reference ID: {Reference ID}
				
				Order ID: {Order ID}
				
				Date: {Order Date}

				Products Ordered
				
				
			""";
	private static String client_mail_text_forget_password="""
			Dear [Name],

				We received a request to reset your password. To proceed, please use the following One-Time Password (OTP):

				Your OTP: {OTP_CODE}

				This code is valid for the next 30 seconds. If you didn’t request a password reset, please ignore this email or contact our support team immediately.

				For security reasons, please do not share this code with anyone.

				If you need further assistance, feel free to reach out to our support team at teamurbangroove@gmail.com.

			Warm regards,
				The Urban Groove Team
			""";
	private static String client_mail_text_order_canceled_by_vendor="""
			Dear [Name],

				We regret to inform you that the vendor has canceled one or more items in your recent order. As per our policy, any cancellation of individual items within an order results in the cancellation of the entire order.

				Order Details:
				Order Reference ID: {Reference ID}
				Order ID: {Order ID}
				Date: {Order Date}
				
				Refund Information (if applicable):
				If you made a payment online, we have initiated a full refund for the total amount paid. The refund will be processed to your original payment method and should reflect in your account within [5–7 business days].

				We sincerely apologize for the inconvenience this may have caused. Our team is committed to ensuring a smooth experience for you and is actively working to address the issue with the vendor.

				If you have any questions or need further assistance, please feel free to contact us at teamurbangroove@gmail.com.

				Thank you for choosing Urban Groove. We look forward to serving you again.

			Warm regards,
				The Urban Groove Team
			""";
	private static String client_mail_text_order_canceled_by_client="""
			Dear [Name],

				We have processed your request to cancel the order, and the order has now been canceled successfully.

				Order Details:
				Order Reference ID: {Reference ID}
				Order ID: {Order ID}
				Date: {Order Date}
				
				Refund Information (if applicable):
				If you made an online payment for this order, the refund process has been initiated. The amount will be credited back to your original payment method within [5–7 business days].

				We hope this action has met your expectations. Should you have any questions or need further assistance, feel free to reach out to us at teamurbangroove@gmail.com.

				Thank you for choosing Urban Groove. We hope to serve you again in the future.

			Best regards,
				The Urban Groove Team
			""";
	private static String client_mail_text_order_refund_by_client="""
			Dear [Name],

				We have received your refund request for your order.
				
				Order Details:
				Order Reference ID: {Reference ID}
				Order ID: {Order ID}
				Date: {Order Date}
				
				Refund Process:
				The refund will be processed after all the products are collected and verified by our admin team.

				Online Payment:
				If the payment was made online, the refund will be credited to your original payment method within 5-7 business days after verification and approval.

				Cash Payment:
				If the payment was made in cash, the refund will be issued in cash once the verification process is complete.

				Our team will notify you once the verification and refund process is completed.

				Thank you for your patience and understanding. Should you have any questions, please feel free to contact our support team at teamurbangroove@gmail.com.

			Best regards,
				The Urban Groove Team
			""";
	private static String client_mail_text_order_canceled_by_admin="""
			Dear [Name],

				We regret to inform you that your order has been canceled by our admin team.

				Cancellation Details:
				Order Details:
				Order Reference ID: {Reference ID}
				Order ID: {Order ID}
				Date: {Order Date}
				
				Reason for Cancellation:
				After careful review, we had to cancel your order due to one or more of the following reasons:

				Product Out of Stock: Unfortunately, the product(s) you ordered are no longer available, and restocking will take longer than anticipated.
				Payment Issue: We encountered an issue processing your payment.
				Incorrect Order Details: There was a discrepancy in the information provided for this order that could not be resolved.
				Policy Violation: The order did not comply with our internal policies or terms of service.
				Refund Information:
				If you have already made a payment, a refund has been initiated and should reflect in your account within 5-7 business days.

				Inquiry Assistance:
				We understand that this may be an inconvenience, and we are here to assist you. If you have any questions or would like to inquire further about the cancellation, please feel free to contact us:

				Email: teamurbangroove@gmail.com. (Please include your Order ID and Order Reference ID for quicker assistance.)
				Our team is committed to addressing your concerns promptly and providing clarity regarding your order status.

				We sincerely apologize for the inconvenience caused and hope to serve you better in the future. Thank you for your understanding and patience.

			Warm regards,
				The Urban Groove Team
			""";
	private static String client_mail_text_order_return_refund_request_canceled_by_admin="""
			Dear [Name],

				We hope this message finds you well. We are writing to inform you that your request for the return and refund of the following order has been reviewed but, unfortunately, could not be approved at this time:

				Order Details:
				Order Details:
				Order Reference ID: {Reference ID}
				Order ID: {Order ID}
				Date: {Order Date}
				
				Reason for Cancellation:
				After careful consideration, we are unable to process your return/refund request due to one or more of the following reasons:

				Return Policy Violation: The request does not meet the criteria outlined in our return/refund policy (e.g., the return window has expired or the product condition is not eligible).
				Incorrect or Incomplete Information: The details provided for the request are either incomplete or inconsistent.
				Non-Returnable Product: The item(s) in question are marked as non-returnable at the time of purchase.
				Other: [Specify other reason, if applicable, or remove this point.]
				We apologize for any inconvenience this may cause and assure you that we value your trust in us.

				Need Further Assistance?
				If you believe this decision was made in error or have additional information to support your request, we encourage you to reach out to us:

				Email: teamurbangroove@gmail.com.
				Please mention your Order ID and Order Reference ID so we can review your concerns promptly.

				Thank you for your understanding and cooperation. We remain committed to serving you and hope to provide you with a better experience in the future.

			Warm regards,
				The Urban Groove Team
			""";
	private static String client_mail_text_order_return_refund_request_approved_by_admin="""
			Dear [Customer Name],

				We are writing to inform you that we have successfully collected and verified your ordered items.

				Order Details:
				Order Reference ID: {Reference ID}
				Order ID: {Order ID}
				Date: {Order Date}
				
				After thorough verification, we are pleased to confirm that your return meets our policy guidelines, and we have initiated the refund process.
				
				Refund Details: Estimated Refund Timeline: 5-7 business days.
				
				Please note that the refund will be processed within the specified timeline.
				If you have any further queries or require assistance, please do not hesitate to reach out to us:

				Email: teamurbangroove@gmail.com.

				We appreciate your patience and understanding throughout the process and look forward to serving you better in the future.

				Thank you for choosing Urban Groove!

			Warm regards,
				The Urban Groove Team
			""";
	final public static String get_text(String which_data) {
		switch(which_data) {
		case "client_security_mail_text"->{return client_security_mail_text;}
		case "client_mail_text_deletion"->{return client_mail_text_deletion;}
		case "client_mail_text_order_placed"->{return client_mail_text_order_placed;}
		case "client_mail_text_forget_password"->{return client_mail_text_forget_password;}
		case "client_mail_text_order_canceled_by_vendor"->{return client_mail_text_order_canceled_by_vendor;}
		case "client_mail_text_order_canceled_by_client"->{return client_mail_text_order_canceled_by_client;}
		case "client_mail_text_order_refund_by_client"->{return client_mail_text_order_refund_by_client;}
		case "client_mail_text_order_canceled_by_admin"->{return client_mail_text_order_canceled_by_admin;}
		case "client_mail_text_order_return_refund_request_canceled_by_admin"->{return client_mail_text_order_return_refund_request_canceled_by_admin;}
		case "client_mail_text_order_return_refund_request_approved_by_admin"->{return client_mail_text_order_return_refund_request_approved_by_admin;}
		}
		return null;
	}
}

final public static class AdminMailText{
	private static String admin_security_mail_text="""
			Dear [Name],

				We hope this message finds you well. For security purposes, we’ve generated a One-Time Password (OTP) to verify your request for admin registration on Urban Groove.

				Your OTP: {OTP_CODE}

				This OTP is valid for the next 30 seconds. Please use it promptly to complete your verification process.

				Once you have successfully verified your OTP, your registration will be subject to approval by another admin. You will receive a confirmation email once your registration has been reviewed and approved.

				If you did not request this OTP, please disregard this email or contact our support team immediately at teamurbangroove@gmail.com.

				We look forward to having you on the admin team!

			Warm regards,
				The Urban Groove Team
			""";
	private static String admin_status_mail_text_approval="""
			Dear [Name],

				We are excited to inform you that your registration as an admin on Urban Groove has been successfully approved! 🎉

				You can now access your admin account to begin managing the platform and ensuring a seamless experience for our users and vendors.

				Please log in with your credentials and navigate to the Admin Dashboard to start performing your tasks, such as approving vendor registrations, managing products, and overseeing platform operations.

				If you have any questions or encounter any issues, our support team is here to assist you. Feel free to reach out at teamurbangroove@gmail.com.

				We’re thrilled to have you on the Urban Groove admin team and are confident that your contributions will play a key role in our platform’s success!

			Warm regards,
				The Urban Groove Team
			""";
	private static String admin_status_mail_text_rejection="""
			Dear [Name],

				Thank you for your interest in becoming an admin at Urban Groove. After careful review, we regret to inform you that your request has been rejected. As a result, you will no longer have access to the admin system.
				
				Reason for Rejection:
				[State the reason, e.g., "Incomplete documentation," or "Inaccurate information provided."]
				
				We understand this may be disappointing, but there is still a chance for reconsideration. If you'd like us to review your request again, please contact us at teamurbangroove@gmail.com.

				We appreciate your understanding and look forward to hearing from you if you'd like to pursue this further.

			Warm regards,
				The Urban Groove Team
			""";
	private static String admin_status_mail_text_deletion="""
			Dear [Name],

				We regret to inform you that your admin account on Urban Groove has been permanently deleted due to the following reason(s):

				[State the reason for deletion, e.g., "Violation of platform policies" or "Unauthorized activities detected."]

				As a result of this action:

				You will no longer have access to the Admin Dashboard or any administrative features of the platform.
				All your associated data, including administrative actions or contributions, has been removed.
				If you believe this decision was made in error or require further clarification, you may contact us at teamurbangroove@gmail.com. However, please note that to regain access, you will need to re-register as an admin and await approval from the existing administrators.

				Thank you for your understanding.

			Warm regards,
				The Urban Groove Team
			""";
	private static String admin_status_mail_text_blacklisted="""
			Dear [Name],

				We regret to inform you that your admin account on Urban Groove has been blacklisted. This action may have been taken either during the review process of your registration or after your registration had already been approved.

				As a result, you no longer have access to the Admin Dashboard or any associated administrative privileges.
				
				Reason for Blacklisting:
				[State the reason, e.g., "Violation of platform policies," or "Suspicious activities detected."]
				
				If you believe this action was taken in error or would like us to reconsider, you can send an appeal to teamurbangroove@gmail.com. Please include all relevant details to help us assess your request thoroughly.

				We appreciate your understanding and will review your case promptly upon receiving your appeal.

			Warm regards,
				The Urban Groove Team
			""";
	private static String admin_mail_text_forget_password="""
			Dear Admin [Name],

				We received a request to reset your password. To proceed, please use the following One-Time Password (OTP):

				Your OTP: {OTP_CODE}

				This code is valid for the next 30 seconds. If you didn’t request a password reset, please ignore this email or contact our support team immediately.

				For security reasons, please do not share this code with anyone.

				If you need further assistance, feel free to reach out to our support team at teamurbangroove@gmail.com.

			Warm regards,
				The Urban Groove Team
			""";
	final public static String get_text(String which_data) {
		switch(which_data) {
		case "admin_security_mail_text"->{return admin_security_mail_text;}
		case "admin_status_mail_text_approval"->{return admin_status_mail_text_approval;}
		case "admin_status_mail_text_rejection"->{return admin_status_mail_text_rejection;}
		case "admin_status_mail_text_deletion"->{return admin_status_mail_text_deletion;}
		case "admin_status_mail_text_blacklisted"->{return admin_status_mail_text_blacklisted;}
		case "admin_mail_text_forget_password"->{return admin_mail_text_forget_password;}
		}
		return null;
	}
}

final public static class VendorMailText{
private static String vendor_security_mail_text="""
		Dear [Name],

			We hope this message finds you well. For security purposes, we’ve generated a One-Time Password (OTP) to verify your request for vendor registration on Urban Groove.

			Your OTP: {OTP_CODE}

			This OTP is valid for the next 30 seconds. Please use it promptly to complete your verification process.

			Once you have successfully verified your OTP, your registration will be reviewed and approved by the admin. You will receive a confirmation email once your registration has been approved.

			If you did not request this OTP, please disregard this email or contact our support team immediately at teamurbangroove@gmail.com.

			We look forward to partnering with you!

		Warm regards,
			The Urban Groove Team
		""";
private static String vendor_status_mail_text_approval="""
		Dear [Name],

			We are pleased to inform you that your registration as a vendor on Urban Groove has been successfully approved! 🎉

			You can now log in to your vendor account and start uploading your products to showcase them to our growing community of customers.
			
			Please log in using your credentials and navigate to the Vendor Dashboard to upload your products. If you encounter any issues or have any questions, feel free to reach out to our support team at teamurbangroove@gmail.com.

			We’re excited to have you as part of our vendor network and look forward to seeing your amazing products on our platform!

		Warm regards,
			The Urban Groove Team
		""";
private static String vendor_status_mail_text_rejection="""
		Dear [Name],

			We regret to inform you that your vendor registration request for Urban Groove has been rejected. Unfortunately, this means you will not be able to access the platform as a vendor at this time.

			Reason for Rejection:
			[State the reason, e.g., "Incomplete documentation," "Failure to meet vendor requirements," or "Inaccurate information provided."]

			If you believe this decision was made in error or wish to appeal the rejection, you can reach out to us at teamurbangroove@gmail.com for reconsideration. We’re happy to assist you in resolving any concerns or addressing any issues related to your registration.

			Thank you for your interest in becoming a part of Urban Groove.

		Warm regards,
			The Urban Groove Team
		""";
private static String vendor_status_mail_text_deletion="""
		Dear [Name],

			We regret to inform you that your vendor account on Urban Groove has been permanently deleted due to the following reason(s):

			[State the reason for deletion, e.g., "Violation of marketplace policies," "Repeated failure to fulfill orders," or "Misrepresentation of products."]

			As a result, you will no longer have access to the platform, and all data associated with your account, including product listings has been removed.

			If you believe this decision was made in error or require further clarification, you may contact us at teamurbangroove@gmail.com. However, please note that to regain access, you will need to re-register as a vendor and await approval from the administrators.

			We appreciate your understanding and cooperation.

		Warm regards,
			The Urban Groove Team
		""";
private static String vendor_status_mail_text_blacklisted="""
		Dear [Name],

			We’re writing to inform you that your vendor account on Urban Groove has been blacklisted. This action applies whether your account was pending approval or had already been approved.

			As a result of this action:

			Your access to the platform has been restricted.
			You are no longer authorized to list products or interact with Urban Groove customers.
			Reason for Blacklisting:
			[State the reason, e.g., "Violation of platform policies," "Repeated customer complaints," or "Suspicious activities detected."]

			If you believe this action was taken in error or wish to appeal this decision, please contact us at teamurbangroove@gmail.com. We are open to reviewing your case and assisting you further.

			Thank you for your understanding.

		Warm regards,
			The Urban Groove Team
		""";
private static String vendor_product_status_mail_text_approval="""
		Dear [Name],

			We are excited to inform you that your product in the category [Category] have been successfully approved by the Urban Groove Admin Team! 🎉

			Here are the details of the approved product:

			Product Name: [Product Name]
			Category: [Category]
			Approval Date: [Date]
			Your approved product are now live on our platform and available for customers to view and purchase. We encourage you to monitor the performance through your Vendor Dashboard.

			If you have any questions or require assistance, feel free to reach out to us at teamurbangroove@gmail.com.

			Thank you for contributing to the Urban Groove marketplace, and we wish you great success with your sales!

		Warm regards,
			The Urban Groove Team
		""";
private static String vendor_product_status_mail_text_rejection="""
		Dear [Name],

			We regret to inform you that your product in the category [Category] have not been approved by the Urban Groove Admin Team.

			Here are the details of the rejected product:

			Product Name: [Product Name]
			Category: [Category]
			Rejection Date: [Date]
			Reason for Rejection: [State the reason, e.g., "Incomplete product details," "Non-compliance with platform guidelines," or "Inappropriate content."]
			We kindly request you to review our platform’s product guidelines and address the stated issues. Once updated, you can resubmit your product for approval through your Vendor Dashboard.

			If you believe this decision was made in error or require further clarification, feel free to contact us at teamurbangroove@gmail.com. We’re here to assist you.

			Thank you for your understanding.

		Warm regards,
			The Urban Groove Team
		""";
private static String vendor_product_status_mail_text_deletion="""
		Dear [Name],

			We regret to inform you that your product in the category [Category] has been permanently deleted from the Urban Groove platform.

			Here are the details of the deleted product:

			Product Name: [Product Name]
			Category: [Category]
			Reason for Deletion: [State the reason, e.g., "Violation of platform guidelines," "Repeated non-compliance," or "Obsolete product information."]
			As a result, this product will no longer be available for customers to view or purchase on the platform.

			If you believe this action was taken in error or require further clarification, please do not hesitate to contact us at teamurbangroove@gmail.com. Kindly note that even after resolving any issues, the product will need to be re-uploaded to the platform for approval.

			Thank you for your understanding.

		Warm regards,
			The Urban Groove Team
		""";
private static String vendor_product_status_mail_text_blacklisted="""
		Dear [Vendor's Name],

			We regret to inform you that your product in the category [Category] has been blacklisted by the Urban Groove Admin Team.

			Here are the details of the blacklisted product:

			Product Name: [Product Name]
			Category: [Category]
			Blacklisting Date: [Date]
			Reason for Blacklisting: [State the reason, e.g., "Violation of platform guidelines," "Repeated customer complaints," or "Inaccurate product descriptions."]
			As a result of this action:

			The blacklisted product is temporarily unavailable for purchase on the platform.
			You can work with our team to address the concerns raised and potentially restore the product's status without re-uploading it.
			If you believe this decision was made in error or require further clarification, please contact us at teamurbangroove@gmail.com. Our team is here to assist you in resolving the issue promptly.

			Thank you for your understanding and cooperation.

		Warm regards,
			The Urban Groove Team
		""";
private static String vendor_mail_text_forget_password="""
		Dear Vendor [Name],

				We received a request to reset your password. To proceed, please use the following One-Time Password (OTP):

				Your OTP: {OTP_CODE}

				This code is valid for the next 30 seconds. If you didn’t request a password reset, please ignore this email or contact our support team immediately.

				For security reasons, please do not share this code with anyone.

				If you need further assistance, feel free to reach out to our support team at teamurbangroove@gmail.com.

			Warm regards,
				The Urban Groove Team
		""";
final public static String get_text(String which_data) {
	switch(which_data) {
	case "vendor_security_mail_text"->{return vendor_security_mail_text;}
	case "vendor_status_mail_text_approval"->{return vendor_status_mail_text_approval;}
	case "vendor_status_mail_text_rejection"->{return vendor_status_mail_text_rejection;}
	case "vendor_status_mail_text_deletion"->{return vendor_status_mail_text_deletion;}
	case "vendor_status_mail_text_blacklisted"->{return vendor_status_mail_text_blacklisted;}
	case "vendor_product_status_mail_text_approval"->{return vendor_product_status_mail_text_approval;}
	case "vendor_product_status_mail_text_rejection"->{return vendor_product_status_mail_text_rejection;}
	case "vendor_product_status_mail_text_deletion"->{return vendor_product_status_mail_text_deletion;}
	case "vendor_product_status_mail_text_blacklisted"->{return vendor_product_status_mail_text_blacklisted;}
	case "vendor_mail_text_forget_password"->{return vendor_mail_text_forget_password;}
	}
	return null;
}
}
}
