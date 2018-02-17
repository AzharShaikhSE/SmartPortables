import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

@MultipartConfig(fileSizeThreshold=1024*1024*2,
maxFileSize=1024*1024*10,
maxRequestSize=1024*1024*50)

public class Account extends HttpServlet {

	public static HashMap<String, ArrayList<BillingOrderBean>> billedOrders = new HashMap<String, ArrayList<BillingOrderBean>>();
	ArrayList<BillingOrderBean> salesOrders = new ArrayList<BillingOrderBean>();

	protected void doGet(HttpServletRequest request,
	HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		HttpSession session = request.getSession(true);
		Helper helper = new Helper(request, pw);
		if (!helper.checkLoggedIn()) {
			response.sendRedirect("Login");
			return;
		}

		String usertype = helper.getUsertype();
		if (usertype.equalsIgnoreCase("Customer")) {
			displayCustomer(request, response);
		} else if (usertype.equalsIgnoreCase("Retailer")) {
			displayRetailer(request, response);
		} else if (usertype.equalsIgnoreCase("SalesMan")) {
			displayManager(request, response);
		}
	}

	protected void displayCustomer(HttpServletRequest request,
	HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);
		boolean empty = true;
		helper.printHtml("Header.html");
		pw.print("<div class='post' style='float: none; width: 100%'>");
		pw.print("<h2 class='title meta'><a style='font-size: 24px;'>My Orders</a></h2>"
		+ "<div class='entry'>");
		HttpSession session = request.getSession(true);
		if(session.getAttribute("error_msg")!=null){
			pw.print("<h4 style='color:red'>"+session.getAttribute("error_msg")+"</h4>");
			session.removeAttribute("error_msg");
		}
		pw.print("<table class='gridtable'>");
		pw.println("<tr><th>Email Id</th><th>Order Num.</th><th>Ordered Date</th><th>Delivery Date</th><th>Shipping Address</th><th>Price</th><th>Action</th></tr>");

		for (BillingOrderBean bo : helper.getBilledOrders()) {
			ArrayList<OrderItemBean> orderItems = bo.getOrderItems();

			pw.println("<form action='DeleteOrder' method='post'>");
			pw.println("<input type='hidden' name='username' value='"+bo.getUsername()+"'>");
			pw.println("<input type='hidden' name='ordernum' value='"+bo.getOrderNum()+"'>");
			pw.println("<input type='hidden' name='deliverydate' value='"+bo.getDeliveryDate()+"'>");
			pw.println("<td>"+bo.getEmail()+"</td>");
			pw.println("<td>"+bo.getOrderNum()+"</td>");
			pw.println("<td>"+bo.getOrderDate()+"</td>");
			pw.println("<td>"+bo.getDeliveryDate()+"</td>");
			pw.println("<td>"+bo.getAddress1()+", "+bo.getAddress2()+"</td>");
			pw.println("<td>"+bo.getTotal()+"</td>");
			pw.println("<td><input type='submit' class='btnbuy' value='Delete Order'></td></tr>");
			pw.println("</form>");
			empty = false;
		}

		pw.println("</table>");
		if (empty) {
			pw.print("<h4 style='color:red'>No orders are found</h4>");
		}
		pw.print("</div></div>");
		helper.printHtml("Footer.html");
	}


	protected void displayRetailer(HttpServletRequest request,
	HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);

		helper.printHtml("Header.html");
		pw.print("<div class='post' style='float: none; width: 100%'>");
		pw.print("<h2 class='title meta'><a style='font-size: 24px;'>Add Products </a></h2>"
		+ "<div class='entry'>");
		HttpSession session = request.getSession(true);
		if(session.getAttribute("error_msg")!=null){
			pw.print("<h4 style='color:red'>"+session.getAttribute("error_msg")+"</h4>");
			session.removeAttribute("error_msg");
		}
		if(session.getAttribute("msg")!= null){
			pw.print("<h4 style='color:red'>"+session.getAttribute("msg")+"</h4>");
			session.removeAttribute("msg");
		}
		pw.print("<table class='gridtable'>");
		pw.print("<div><form method='post' action='Account' enctype='multipart/form-data' >");

		pw.print("<tr><td>&nbsp;</td><td>Product Name </td><td><input type='text' name='pname' value='' required></td></tr>");
		pw.print("<tr><td>&nbsp;</td><td>Product Price($) </td><td><input type='text' name='pprice' value='' required></td></tr>");
		pw.print("<tr><td>&nbsp;</td><td>Upload Image </td><td><input type='file' name='pimage' value='' required></td></tr>");
		pw.print("<tr><td>&nbsp;</td><td>Select Category </td><td>"
		+"<select name='pcategory'>"
		+"<option value='smartWatches' selected>Smart Watches</option>"
		+"<option value='speakers'>Speakers</option>"
		+"<option value='headphones'>Headphones</option>"
		+"<option value='phones' selected>Phones</option>"
		+"<option value='laptops'>Laptops</option>"
		+"<option value='externalStorage'>External Storage</option>"
		+"</select>"
		+" </td></tr>");
		pw.print("<tr><td>&nbsp;</td><td>Product Brand </td><td><input type='text' name='pbrand' value='' required></td></tr>");
		pw.print("<tr><td>&nbsp;</td><td>Product Discount </td><td><input type='text' name='pdiscount' value='' required></td></tr>");
		pw.print("<tr><td>&nbsp;</td><td>Product Onsale </td><td><input type='text' name='ponsale' value='' required></td></tr>");
		pw.print("<tr><td>&nbsp;</td><td>Product Manufacture Rebate </td><td><input type='text' name='pmanufacturerebate' value='' required></td></tr>");
		pw.print("<tr><td>&nbsp;</td><td>Product Quantity </td><td><input type='text' name='pquantity' value='' required></td></tr>");
		pw.print("<tr><td>&nbsp;</td><td><input type='hidden' name='option' value='add'></td><td><input type='submit' class='btn1' value='Add Product'></td></tr>");
		pw.print("</form></table></div>");

		HashMap<Integer,ProductBean> productList = helper.getProductList();

		pw.print("<div class='post' style='float: none; width: 100%'>");
		pw.print("<h2 class='title meta'><a style='font-size: 24px;'>Products List </a></h2>"
		+ "<div class='entry'>");

		pw.print("<div><table class='gridtable' width='80%'>");
		pw.print("<tr><td><b>Product Name</b></td><td><b>Product Price($)</b> </td><td> <b>Category </b></td><td><b>Product Brand </b></td><td><b> Discount % </b></td><td><b>Action</b></td></tr>");

		for(Integer prodID: productList.keySet()){
			ProductBean prod = (ProductBean)productList.get(prodID);

			pw.print("<form method='post'><tr><td><input type='hidden' name='pid' value='"+prod.getId()+"'><input type='text' name='pname' value='"+prod.getName()+"' required></td><td><input type='text' name='pprice' value='"+prod.getCost()+"' required></td><td><input type='hidden' name='pcategory' value='"+prod.getCategory()+"'>"+prod.getCategory()+"</td><td><input type='text' name='pbrand' value='"+prod.getBrand()+"' required></td><td><input type='text' name='pdiscount' value='"+prod.getDiscount()+"' required></td><td><input type='submit' class='btn1' name='update' value='Update' onclick='form.action=\"UpdateProduct\";'><input type='submit' class='btn1' name='delete' value='Delete' onclick='form.action=\"DeleteProduct\";'></td></tr>");
			pw.print("</form>");
		}

		pw.print("</table></div>");

		helper.printHtml("Footer.html");
	}

	protected void displayManager(HttpServletRequest request,
	HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);
		boolean empty = true;

		helper.printHtml("Header.html");
		pw.print("<div class='post' style='float: none; width: 100%'>");
		pw.print("<h2 class='title meta'><a style='font-size: 24px;'>Create User</a></h2>"
		+ "<div class='entry'>");
		HttpSession session = request.getSession(true);
		if (session.getAttribute("login_msg") != null)
		pw.print("<h4 style='color:red'>"
		+ session.getAttribute("login_msg") + "</h4>");
		pw.print("<form method='post' action='Registration'>"
		+ "<table style='width:75%'><tr><td>"
		+ "<h3>Username</h3></td><td><input style='width=30%' type='text' name='username' value='' class='input' required></input>"
		+ "</td></tr><tr><td>"
		+ "<h3>Password</h3></td><td><input style='width=30%' type='password' name='password' value='' class='input' required></input>"
		+ "</td></tr><tr><td>"
		+ "<h3>Re-Password</h3></td><td><input style='width=30%' type='password' name='repassword' value='' class='input' required></input>"
		+ "</td></tr></table>"
		+ "<input type='hidden'name='roleType' value='Customer'></input>"
		+ "<input type='submit' class='btnbuy' name='ByUser' value='Create User' style='float: center'></input>"
		+ "</form>" + "</div></div></div>");
		pw.print("</div></div>");

		pw.print("<div class='post' style='float: none; width: 100%'>");
		pw.print("<h2 class='title meta'><a style='font-size: 24px;'>Customers Orders</a></h2>"
		+ "<div class='entry'>");
		if(session.getAttribute("error_msg")!=null){
			pw.print("<h4 style='color:red'>"+session.getAttribute("error_msg")+"</h4>");
			session.removeAttribute("error_msg");
		}
		pw.print("<table class='gridtable'>");
		if (session.getAttribute("update_msg") != null){
			pw.print("<h4 style='color:red'>"+ session.getAttribute("update_msg") + "</h4>");
			session.removeAttribute("update_msg");
		}
		pw.println("<tr><th>User Name</th><th>Email Id</th><th>Order Num.</th><th>Zip Code</th><th>Ordered Date</th><th>Delivery Date</th><th>Address1</th><th>Address2</th><th>Price</th><th>Action</th></tr>");


		for (BillingOrderBean bo : helper.getAllCustomersOrders()) {
			ArrayList<OrderItemBean> orderItems = bo.getOrderItems();
			pw.println("<form  method='post'>");
			pw.println("<input type='hidden' name='username' value='"+bo.getUsername()+"'>");
			pw.println("<input type='hidden' name='ordernum' value='"+bo.getOrderNum()+"'>");
			pw.println("<input type='hidden' name='deliverydate' value='"+bo.getDeliveryDate()+"'>");
			pw.println("<td>"+bo.getUsername()+"</td>");
			pw.println("<td><input type='text' name='email' value='"+bo.getEmail()+"'</td>");
			pw.println("<td>"+bo.getOrderNum()+"</td>");
			pw.println("<td><input type='text' name='zipcode' value='"+bo.getZipcode()+"'</td>");
			pw.println("<td>"+bo.getOrderDate()+"</td>");
			pw.println("<td>"+bo.getDeliveryDate()+"</td>");
			pw.println("<td><input type='text' name='address1' value='"+bo.getAddress1()+"'</td>");
			pw.println("<td><input type='text' name='address2' value='"+bo.getAddress2()+"'</td>");
			pw.println("<td>"+bo.getTotal()+"</td>");
			pw.println("<td><input type='submit' class='btnbuy' name='dbutton' value='Delete Order' onclick='form.action=\"DeleteOrder\";'><input type='submit' class='btnbuy' name='ubutton' value='Update Order' onclick='form.action=\"UpdateServlet\";'></td></tr></form>");
			empty = false;
		}

		pw.println("</table>");
		if (empty) {
			pw.print("<h4 style='color:red'>No orders are found</h4>");
		}
		pw.print("</div></div>");

		pw.print("<div class='post' style='float: none; width: 100%'>");
		pw.print("<h2 class='title meta'><a style='font-size: 24px;'>Add Customer Orders</a></h2>"
		+ "<div class='entry'>");
		if(session.getAttribute("error_msg")!=null){
			pw.print("<h4 style='color:red'>"+session.getAttribute("error_msg")+"</h4>");
			session.removeAttribute("error_msg");
		}
		pw.print("<table class='gridtable'>");
		if (session.getAttribute("add_msg") != null){
			pw.print("<h4 style='color:red'>"+ session.getAttribute("add_msg") + "</h4>");
			session.removeAttribute("add_msg");
		}
		pw.println("<tr><th>User Name</th><th>Full Name</th><th>Email Id</th><th>Credit Card No.</th><th>CVV</th><th>Zip Code</th><th>Shipping Address1</th><th>Shipping Address2</th><th>Price</th><th>Action</th></tr>");

		pw.println("<form action='CheckOut' method='post'>");
		pw.println("<td><input type='text' name='username' value=''</td>");
		pw.println("<td><input type='text' name='fullname' value=''</td>");
		pw.println("<td><input type='text' name='email' value=''</td>");
		pw.println("<td><input type='text' name='creditcard' value=''</td>");
		pw.println("<td><input type='text' name='cvv' value=''</td>");
		pw.println("<td><input type='text' name='zipcode' value=''</td>");
		pw.println("<td><input type='text' name='address1' value=''</td>");
		pw.println("<td><input type='text' name='address2' value=''</td>");
		pw.println("<td><input type='text' name='total' value=''</td>");
		pw.println("<td><input type='submit' class='btnbuy' name='add' value='Add Order'</td>");


		pw.println("</form></table>");
		pw.print("</div></div>");


		helper.printHtml("Footer.html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		HttpSession session = request.getSession(true);
		Helper helper = new Helper(request, pw);
		if (!helper.checkLoggedIn()) {
			response.sendRedirect("Login");
			return;
		}

		String username = (String)session.getAttribute("username");
		String name = request.getParameter("pname");
		float cost = Float.parseFloat(request.getParameter("pprice"));
		String category = request.getParameter("pcategory");
		String brand = request.getParameter("pbrand");
		int discount = Integer.parseInt(request.getParameter("pdiscount"));
		String onsale = request.getParameter("ponsale");
		String manufacturerebate = request.getParameter("pmanufacturerebate");
		int quantity = Integer.parseInt(request.getParameter("pquantity"));
		String image = "";


		String folder = "";
		if(category.equals("smartWatches")){
			folder = "smart_watches";
		}else if(category.equals("speakers")){
			folder = "speakers";
		}else if(category.equals("headphones")){
			folder =  "headphones";
		}else if(category.equals("phones")){
			folder =  "phones";
		}else if(category.equals("laptops")){
			folder =  "laptops";
		}else if(category.equals("externalStorage")){
			folder =  "External Storage";
		}

		Part part = request.getPart("pimage");
		String fileName = extractFileName(part);
		System.out.print("File name ********* "+fileName);
		if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
			part.write("C:\\apache-tomcat-7.0.34\\webapps\\SmartPortables\\images\\"+folder+"\\"+fileName);
		}

		image = "images/"+folder+"/"+fileName;
		helper.addProduct(name, cost, image, category,"normal", brand, discount, onsale, manufacturerebate, quantity);
		session.setAttribute("msg", "Product Added.");

		displayRetailer(request, response);

	}

	private String extractFileName(Part part) {
		System.out.println("---------------- in extract");
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			System.out.println("value of s "+s);
			if (s.trim().startsWith("filename"))
			return s.substring(21, s.length() - 1);
		}
		return "";
	}


}
