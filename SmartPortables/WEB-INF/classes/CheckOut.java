import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;


public class CheckOut extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);
		int i = 1;
		helper.printHtml("Header.html");
		helper.printHtml("LeftNavigationBar.html");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Order Details</a>");
		pw.print("</h2><div class='entry'>");
		pw.print("<table  class='gridtable'>");
		pw.print("<th>No.</th><th>Product Name</th><th>Product Price</th>");
		for (OrderItemBean oi : helper.getCustomerOrders()) {
			pw.print("<tr>");
			pw.print("<td>"+i+".</td><td>"+oi.getName()+"</td><td>$  "+oi.getPrice()+"</td>");
			pw.print("</tr>");
			i++;
		}
		pw.print("</table></div></div></div>");

		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Billing Details</a></h2>");
		pw.print("<form action='CheckOut' method='post'><table>"
		+ "<tr><td>Total Amount</td><td><input type='number' name='total' readonly='readonly' value='"+helper.getCartTotal()+"'/></td></tr>"
		+ "<tr><td>Full Name</td><td><input type='text' name='fullname' required='required'/></td></tr>"
		+ "<tr><td>Email Id</td><td><input type='email' name='email' required='required'/></td></tr>"
		+ "<tr><td>Credit Card Number</td><td><input type='text' name='creditcard' required=required'/></td></tr>"
		+ "<tr><td>CVV</td><td><input type='text' name='cvv' required='required'/></td></tr>"
		+ "<tr><td>Address Line 1</td><td><input type='text' name='address1' required='required'/></td></tr>"
		+ "<tr><td>Address Line 2</td><td><input type='text' name='address2' required='required'/></td></tr>"
		+ "<tr><td>Zipcode</td><td><input type='text' name='zipcode' required='required'/></td></tr>"
		+ "<tr><td></td><td><input type='submit' name='login' class='buybtn' value='Place Order'></td></tr>"
		+ "</table></form>");

		pw.print("</div></div>");
		helper.printHtml("Footer.html");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		HttpSession session = request.getSession(true);
		Helper helper = new Helper(request, pw);

		String fullname = request.getParameter("fullname");
		String button = request.getParameter("add");
		//System.out.println("###########Checkout button is "+ button);
		if(button==null)
		button = "checkout";
		System.out.println("im here "+ button);
		double total = Double.parseDouble(request.getParameter("total"));
		String email = request.getParameter("email");
		String creditcardNo = request.getParameter("creditcard");
		int cvv = Integer.parseInt(request.getParameter("cvv"));
		String address1 = request.getParameter("address1");
		String address2 = request.getParameter("address2");
		String zipcode = request.getParameter("zipcode");
		String username = (String)session.getAttribute("username");
		String username1 = request.getParameter("username");
		String orderDate = helper.currentDate();
		String deliveryDate = helper.expectedDeliveryDate();
		int orderNum = 0;
		ArrayList<OrderItemBean> orderItems = helper.getCustomerOrders();

		if(button.equalsIgnoreCase("Add Order"))
			 orderNum = helper.storeBilledOrders(username1, fullname, email, total, creditcardNo, cvv, address1, address2, zipcode, orderDate, deliveryDate, orderItems);
		else
			 orderNum = helper.storeBilledOrders(username, fullname, email, total, creditcardNo, cvv, address1, address2, zipcode, orderDate, deliveryDate, orderItems);

		if(button.equalsIgnoreCase("Add Order")){
			session.setAttribute("add_msg", "Order "+orderNum+" has been Created Successfully !");
			if(helper.checkLoggedIn() == true){
				response.sendRedirect("Account"); return;
			}
		}

		helper.clearCart(username);

		helper.printHtml("Header.html");
		helper.printHtml("LeftNavigationBar.html");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Order Placed Successfully</a></h2>");
		pw.print("<div class='entry'>");
		pw.print("<p style='font-size:20px;color:green;'>Confirmation number: <b>#"+orderNum+"</b><p>");
		pw.print("<p style='font-size:20px;color:green;'>Estimated delivery date: <b>"+deliveryDate+"</b><p>");
		pw.print("</div></div></div>");
		helper.printHtml("Footer.html");
	}

}
