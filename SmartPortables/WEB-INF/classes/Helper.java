import java.io.*;
import javax.servlet.http.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Helper {
	HttpServletRequest req;
	PrintWriter pw;
	String url;
	HttpSession session;
	HashMap<Integer, ProductBean> phm;
	ProductBean product;
	public static HashMap<String, ArrayList<OrderItemBean>> orders = new HashMap<String, ArrayList<OrderItemBean>>();
	public static HashMap<String, ArrayList<BillingOrderBean>> billedOrders = new HashMap<String, ArrayList<BillingOrderBean>>();
	public static HashMap<String, AccessoriesBean> accessoryCatalog ;
	DealMatches dealMatches = new DealMatches();

	public Helper(HttpServletRequest req, PrintWriter pw) {
		this.req = req;
		this.pw = pw;
		this.url = this.getFullURL();
		this.session = req.getSession(true);
	}

	public void printHtml(String file) {

		SaxParserXMLUtility sp = new SaxParserXMLUtility();
		phm = sp.getProductMap();

		String result = HtmlToString(file);
		if (file == "Header.html" || file =="DatavisualizationHeader.html") {

			if (session.getAttribute("username")!=null){
				String username = session.getAttribute("username").toString();
				username = Character.toUpperCase(username.charAt(0)) + username.substring(1);
				String userType = session.getAttribute("roleType").toString();
				if(userType.equalsIgnoreCase("SalesMan")){
					result = result
					+ "<li><a href='Inventory'>Inventory</a></li>"
					+ "<li><a href='SalesReport'>Sales Report</a></li>"
					+ "<li><a>Hello, "+username+"</a></li>"
					+ "<li><a href='Account'>Account</a></li>"
					+ "<li><a href='Cart'>Cart("+CartCount()+")</a></li>"
					+ "<li><a href='Logout'>Logout</a></li></ul></div></div>";
				}else{
					result = result
					+ "<li><a>Hello, "+username+"</a></li>"
					+ "<li><a href='Account'>Account</a></li>"
					+ "<li><a href='Cart'>Cart("+CartCount()+")</a></li>"
					+ "<li><a href='Logout'>Logout</a></li></ul></div></div>";
				}
			}
			else
			result = result + "<li><a href='Login'>Login</a></li></ul></div></div>";
			pw.print(result);
		} else if (file == "Content.html"){
			result = result
			+ "<tr>";

			// added Deal Match Section.
			dealMatches.readTweets();
			HashMap<Integer, ProductBean> selectedProductHm = dealMatches.getSelectedProducts();
			ArrayList<String> selectedTweets = dealMatches.getSelectedTweets();
			if(selectedTweets.isEmpty())
			{
				result = result
				+	"<p><h3>No Offers Found !</h3></p>";
			}
			else
			{
				for(String tweet: selectedTweets)
				{
					result = result
					+	"<p><h3>"+tweet+"</h3></p>";
				}
			}
			result = result
			+	"</div></div><div class='post'><h2 class='title meta'><a style='font-size: 24px;'>Deal Matches</a>"
			+ "</h2><div class='entry'><table id='bestseller'>";

			if(selectedProductHm.isEmpty())
			{
				result = result
				+	"<p><h3>No Offers Found !</h3></p>";
			}
			else
			{
				for (Integer prodId: selectedProductHm.keySet()) {
					product = selectedProductHm.get(prodId);

					result = result
					+	"<td><div id='shop_item'>"
					+	"<h3>"+product.getName()+"</h3>"
					+	"<strong>"+product.getCost()+"$</strong><ul><li id='item'><img src='"+product.getImage()+"' />"
					+ "<li><form method='post' action='Cart'>"
					+ "<input type='hidden' name='id' value='"+product.getId()+"'>"
					+ "<input type='hidden' name='name' value='"+product.getName()+"'>"
					+ "<input type='hidden' name='type' value='"+product.getCategory()+"'>"
					+ "<input type='hidden' name='cost' value='"+product.getCost()+"'>"
					+ "<input type='hidden' name='brand' value='"+product.getBrand()+"'>"
					+ "<input type='hidden' name='access' value=''>"
					+ "<input type='submit' class='inputButton' value='Buy Now' href='#'></a></input></form></li>"
					+ "<li><a class='btnbuy' href='ViewReviews?productmodelname="+product.getName()+"'>View Reviews</a></li>"
					+ "<li><a class='btnbuy' href='WriteReview?name="+product.getName()+"&category="+product.getCategory()+"&price="+product.getCost()+"&brandname="+product.getBrand()+"'>Write Review</a></li></ul>"
					+ "</div></td>";
				}
			}

			//
			result = result+"</tr></tr></table></div></div>"
			+ "<div class='post'><h2 class='title meta'>"
			+ "<a style='font-size: 24px;'>Best Sellers</a>"
			+	"</h2><div class='entry'><table id='bestseller'>"
			+	"<tr>";
			for (Integer prodId: phm.keySet()) {
				product = phm.get(prodId);
				if(product.getRating().equalsIgnoreCase("Best Seller")){

					result = result
					+	"<td><div id='shop_item'>"
					+	"<h3>"+product.getName()+"</h3>"
					+	"<strong>"+product.getCost()+"$</strong><ul><li id='item'><img src='"+product.getImage()+"' />"
					+ "<li><form method='post' action='Cart'>"
					+ "<input type='hidden' name='id' value='"+product.getId()+"'>"
					+ "<input type='hidden' name='name' value='"+product.getName()+"'>"
					+ "<input type='hidden' name='type' value='"+product.getCategory()+"'>"
					+ "<input type='hidden' name='cost' value='"+product.getCost()+"'>"
					+ "<input type='hidden' name='brand' value='"+product.getBrand()+"'>"
					+ "<input type='hidden' name='access' value=''>"
					+ "<input type='submit' class='inputButton' value='Buy Now' href='#'></a></input></form></li>"
					+ "<li><a class='btnbuy' href='ViewReviews?productmodelname="+product.getName()+"'>View Reviews</a></li>"
					+ "<li><a class='btnbuy' href='WriteReview?name="+product.getName()+"&category="+product.getCategory()+"&price="+product.getCost()+"&brandname="+product.getBrand()+"'>Write Review</a></li></ul>"
					+ "</div></td>";
				}
			}

			result = result+"</tr></tr></table></div></div>"
			+ "<div class='post'><h2 class='title meta'>"
			+ "<a style='font-size: 24px;'>Discount Offers</a>"
			+	"</h2><div class='entry'><table id='bestseller'>"
			+	"<tr>";

			for (Integer prodId: phm.keySet()) {
				product = phm.get(prodId);

				if(product.getRating().equalsIgnoreCase("discount offer")){

					result = result
					+ "<td><div id='shop_item'>"
					+	"<h3>"+product.getName()+"</h3>"
					+	"<strong>"+product.getCost()+"$</strong><ul><li id='item'><img src='"+product.getImage()+"' />"
					+ "<li><form method='post' action='Cart'>"
					+ "<input type='hidden' name='id' value='"+product.getId()+"'>"
					+ "<input type='hidden' name='name' value='"+product.getName()+"'>"
					+ "<input type='hidden' name='type' value='"+product.getCategory()+"'>"
					+ "<input type='hidden' name='cost' value='"+product.getCost()+"'>"
					+ "<input type='hidden' name='brand' value='"+product.getBrand()+"'>"
					+ "<input type='hidden' name='access' value=''>"
					+ "<input type='submit' class='inputButton' value='Buy Now' href='#'></a></input></form></li>"
					+"	<li><a class='btnbuy' href='ViewReviews?productmodelname="+product.getName()+"'>View Reviews</a>"
					+"</li><li><a class='btnbuy' href='WriteReview?name="+product.getName()+"&category="+product.getCategory()+"&price="+product.getCost()+"&brandname="+product.getBrand()+"'>Write Review</a></li></ul>"
					+"</div></td>";
				}
			}

			result = result+"</tr></table></div></div></div>";
			pw.print(result);

		}
		else
		pw.print(result);
	}

	public String HtmlToString(String file) {
		String result = null;
		try {
			String webPage = url + file;
			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			result = sb.toString();
		} catch (Exception e) {
		}
		return result;
	}

	public String getFullURL() {
		String scheme = req.getScheme();
		String serverName = req.getServerName();
		int serverPort = req.getServerPort();
		String contextPath = req.getContextPath();
		StringBuffer url = new StringBuffer();
		url.append(scheme).append("://").append(serverName);

		if ((serverPort != 80) && (serverPort != 443)) {
			url.append(":").append(serverPort);
		}
		url.append(contextPath);
		url.append("/");
		return url.toString();
	}

	public void logout(){
		session.removeAttribute("username");
		session.removeAttribute("roleType");
	}

	public boolean checkLoggedIn(){
		if (session.getAttribute("username")==null)
		return false;
		return true;
	}

	public String getUsertype(){
		if (session.getAttribute("roleType")!=null)
		return session.getAttribute("roleType").toString();
		return null;
	}

	public void storeProduct(int id, String name, double price, String type,String brand){

		OrderItemBean OrderItem = new OrderItemBean(id, name, price, type, brand);
		if(!orders.containsKey((String)session.getAttribute("username"))){
			ArrayList<OrderItemBean> arr = new ArrayList<OrderItemBean>();
			orders.put((String)session.getAttribute("username"), arr);
		}

		ArrayList<OrderItemBean> orderItems = orders.get((String)session.getAttribute("username"));
		orderItems.add(OrderItem);
	}

	public void addProduct(String name,float price, String image, String category, String rating, String brand,int discount, String onsale, String manufacturerebate,int quantity){
		SaxParserXMLUtility sp = new SaxParserXMLUtility();
		MySqlDataStoreUtilities sqdb = new MySqlDataStoreUtilities();
		phm = sp.getProductMap();
		Integer id = phm.size()+1;

		try{
			sqdb.addProduct(name, price, image, category, rating, brand, discount, onsale, manufacturerebate, quantity);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		ProductBean prod = new ProductBean(id, name, price, image, category, "normal", brand,  discount,onsale,manufacturerebate,quantity);
		phm.put(id, prod);
	}

	public void updateProduct(int prodId,String pname,float pprice,String pcategory,String pbrand,int pdiscount){
		MySqlDataStoreUtilities sqdb = new MySqlDataStoreUtilities();
		System.out.println("####### helper prod id : "+ prodId);
		try{
			sqdb.updateProduct(prodId, pname, pprice, pcategory, pbrand, pdiscount);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void deleteProduct(int prodId){
		SaxParserXMLUtility sp = new SaxParserXMLUtility();
		MySqlDataStoreUtilities sqdb = new MySqlDataStoreUtilities();
		phm = sp.getProductMap();
		if(phm.containsKey(prodId)){
			ProductBean pd = phm.get(prodId);
			sqdb.deleteProduct(prodId);
			if(pd!=null){
				phm.remove(prodId);
			}
		}

	}

	public HashMap<Integer,ProductBean> getProductList(){

		SaxParserXMLUtility sp = new SaxParserXMLUtility();
		phm = sp.getProductMap();

		return phm;
	}


	public int storeBilledOrders(String username, String fullname, String email, double total, String creditcardNo, int cvv, String address1, String address2, String zipcode, String orderDate, String deliveryDate, ArrayList<OrderItemBean> orderItems){

		BillingOrderBean  SalesOrder = new BillingOrderBean(username, fullname, email, total, creditcardNo, cvv, address1, address2, zipcode, orderDate, deliveryDate, orderItems);
		int orderNum = 0;
		MySqlDataStoreUtilities sqdb = new MySqlDataStoreUtilities();
		//retrieve billed orders from db
		try{
			billedOrders = sqdb.getBilledOrders(username);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		//check if that user has any existing orders
		if(!billedOrders.containsKey(username)){
			ArrayList<BillingOrderBean> arr = new ArrayList<BillingOrderBean>();
			billedOrders.put(username, arr);
		}

		//Insert into db
		try{
			orderNum = sqdb.insertBilledOrder(SalesOrder);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		// Add orders in HashMap
		SalesOrder.setOrderNum(orderNum);
		ArrayList<BillingOrderBean> salesOrders = billedOrders.get(username);
		salesOrders.add(SalesOrder);

		return orderNum;

	}

	public void removeProduct(String name){

		ArrayList<OrderItemBean> orderItems = orders.get((String)session.getAttribute("username"));
		if(!orderItems.isEmpty()){
			Iterator<OrderItemBean> itr = orderItems.iterator();
			while(itr.hasNext()){
				OrderItemBean oi = itr.next();
				if(name.equals(oi.getName()))
				itr.remove();
			}
		}
	}

	public void deleteOrder(String userName, int orderNum){
		MySqlDataStoreUtilities sqdb = new MySqlDataStoreUtilities();
		//	System.out.println("#########Delete order helper username : " + userName);
		//	System.out.println("#########Delete order helper orderno : " + orderNum);
		try{
			sqdb.deleteBilledOrder(userName, orderNum);
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	public void updateOrder(int orderNum, String userName, String email, String zipcode, String address1, String address2){
		MySqlDataStoreUtilities sqdb = new MySqlDataStoreUtilities();
		//System.out.println("####### helper prod id : "+ prodId);
		try{
			sqdb.updateOrder(orderNum, userName, email, zipcode, address1, address2);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void clearCart(String username){

		ArrayList<OrderItemBean> orderItems = orders.get((String)session.getAttribute("username"));
		if(!orderItems.isEmpty()){
			orderItems.clear();
		}
	}

	public ArrayList<OrderItemBean> getCustomerOrders(){
		ArrayList<OrderItemBean> order = new ArrayList<OrderItemBean>();
		if(orders.containsKey((String)session.getAttribute("username")))
		order= orders.get((String)session.getAttribute("username"));
		return order;
	}

	public ArrayList<BillingOrderBean> getBilledOrders(){

		MySqlDataStoreUtilities sqdb = new MySqlDataStoreUtilities();
		ArrayList<BillingOrderBean> salesOrders = new ArrayList<BillingOrderBean>();
		try{
			billedOrders = sqdb.getBilledOrders((String)session.getAttribute("username"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		if(billedOrders.containsKey((String)session.getAttribute("username")))
		salesOrders = billedOrders.get((String)session.getAttribute("username"));
		return salesOrders;

	}

	public ArrayList<BillingOrderBean> getBilledOrders(String userName){

		MySqlDataStoreUtilities sqdb = new MySqlDataStoreUtilities();
		try{
			billedOrders = sqdb.getBilledOrders(userName);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		ArrayList<BillingOrderBean> salesOrders = new ArrayList<BillingOrderBean>();
		if(billedOrders.containsKey(userName))
		salesOrders = billedOrders.get(userName);
		return salesOrders;
	}

	public ArrayList<BillingOrderBean> getAllCustomersOrders(){
		MySqlDataStoreUtilities sqdb = new MySqlDataStoreUtilities();
		try{
			billedOrders = sqdb.getBilledOrders();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		ArrayList<BillingOrderBean> salesOrders = new ArrayList<BillingOrderBean>();
		ArrayList<BillingOrderBean> allSalesOrders = new ArrayList<BillingOrderBean>();
		//	System.out.println("key before ###### "+billedOrders.size());
		for(String key : billedOrders.keySet()){
			//		System.out.println("key ###### "+ key);
			if(billedOrders.containsKey(key)){
				salesOrders = billedOrders.get(key);
				for(BillingOrderBean bo : salesOrders)
				allSalesOrders.add(bo);
			}
		}
		return allSalesOrders;
	}

	public double getCartTotal(){
		double total = 0;
		for (OrderItemBean oi : getCustomerOrders()) {
			total = total + oi.getPrice();
		}
		return total;
	}

	public int CartCount(){
		if(checkLoggedIn())
		return getCustomerOrders().size();
		return 0;
	}

	public String currentDate(){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		return dateFormat.format(date).toString();
	}

	public String expectedDeliveryDate(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 14);
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
		String deliveryDate = format1.format(cal.getTime());
		return deliveryDate;
	}

}
