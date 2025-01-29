package com.ecom;

import java.util.UUID;
import org.json.JSONObject;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;

public class RazorPayService {
	final private static String apiKey = "rzp_test_Xq2JJkU6biRFF2"; 
    final private static String apiSecret = "7fiKLzbTk3dNcbZ0M08i1ltN";
    final public String create_order(double amount) throws Exception {
        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "ubg_" + UUID.randomUUID().toString());
        orderRequest.put("payment_capture", 1);
        Order order = razorpay.orders.create(orderRequest);
        return order.get("id");
    }
   final public void initiate_refund(double amount,String payment_id) throws RazorpayException { 
	   RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecret);
	   JSONObject refundRequest = new JSONObject();
	   refundRequest.put("payment_id", payment_id);
	   refundRequest.put("amount", (amount*100));
	   Refund refund = razorpay.payments.refund(refundRequest);
	   System.out.println(refund.toString());
   }
}
