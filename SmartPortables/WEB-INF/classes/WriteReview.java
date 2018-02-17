import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import javax.servlet.*;
import javax.servlet.http.*;


public class WriteReview extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);
    HttpSession session = request.getSession(true);
		int i = 1;

    String prodName = request.getParameter("name");
		String category = request.getParameter("category");
		String userid = (String)session.getAttribute("username");
		String price = request.getParameter("price");
		String manufacturerName = request.getParameter("brandname");

    if(!helper.checkLoggedIn()){

      session.setAttribute("login_msg", "Please Login in-order to write your review !");
      response.sendRedirect("Login");
      return;
    }

		helper.printHtml("Header.html");
		helper.printHtml("LeftNavigationBar.html");

		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Billing Details</a></h2>");
		pw.print("<form action='WriteReview' method='post'><table>"
		+ "<tr><td>Product Model Name</td><td><input type='text' name='productModelName' required='required'readonly='readonly' value='"+prodName+"'/></td></tr>"
		+ "<tr><td>Product Category</td><td><input type='text' name='productCategory' required='required'readonly='readonly' value='"+category+"'/></td></tr>"
		+ "<tr><td>Product Price</td><td><input type='text' name='productPrice' required='required'readonly='readonly' value='"+price+"'></td></tr>"
		+ "<tr><td>Retailer Name</td><td><input type='text' name='retailerName' required='required'/></td></tr>"
		+ "<tr><td>Retailer Zip</td><td><input type='text' name='retailerZip' required='required'/></td></tr>"
		+ "<tr><td>Retailer City</td><td><input type='text' name='retailerCity' required='required'/></td></tr>"
		+ "<tr><td>Retailer State</td><td><input type='text' name='retailerState' required='required'/></td></tr>"
    + "<tr><td>Product on Sale</td><td><select name='productSale' required='required'/><option value='yes'>Yes</option><option value='no'>No</option></select></td></tr>"
		+ "<tr><td>ManufacturerName</td><td><input type='text' name='manufactureName' required='required'readonly='readonly' value='"+manufacturerName+"'/></td></tr>"
		+ "<tr><td>Manufacturer Rebate</td><td><select name='manufactureRebate' required='required'/><option value='yes'>Yes</option><option value='no'>No</option></select></td></tr>"
		+ "<tr><td>User Id</td><td><input type='text' name='userName' required='required' readonly='readonly' value='"+userid+"'/></td></tr>"
    + "<tr><td>User Age</td><td><input type='text' name='userAge' required='required'/></td></tr>"
		+ "<tr><td>User gender</td><td><input type='text' name='userGender' required='required'/></td></tr>"
		+ "<tr><td>User Occupation</td><td><input type='text' name='userOccupation' required='required'/></td></tr>"
		+ "<tr><td>Review Rating</td><td><select name='reviewRating' required='required'/><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option></select></td></tr>"
    + "<tr><td>Review Date</td><td><input type='Date' name='reviewDate' required='required'/></td></tr>"
		+ "<tr><td>Review Text</td><td><input type='textarea' name='reviewText' required='required'/></td></tr>"
		+ "<tr><td></td><td><input type='submit' name='submitReview' class='buybtn' value='Submit Review'></td></tr>"
		+ "</table></form>");

		pw.print("</div></div>");
		helper.printHtml("Footer.html");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		response.setContentType("text/html");
    		PrintWriter pw = response.getWriter();
    		HttpSession session = request.getSession(true);
    		Helper helper = new Helper(request, pw);

        String productModelName = request.getParameter("productModelName");
			  String productCategory = request.getParameter("productCategory");
			  String retailerName = request.getParameter("retailerName");
			  String retailerCity = request.getParameter("retailerCity");
			  String retailerState = request.getParameter("retailerState");
			  String productSale = request.getParameter("productSale");
			  String manufactureName = request.getParameter("manufactureName");
			  String manufactureRebate = request.getParameter("manufactureRebate");
			  String userName = request.getParameter("userName");
			  String userGender = request.getParameter("userGender");
			  String userOccupation = request.getParameter("userOccupation");
			  Date reviewDate = new Date();
			  String reviewText = request.getParameter("reviewText");
			  double productPrice = Double.parseDouble(request.getParameter("productPrice"));
			  int retailerZip = Integer.parseInt(request.getParameter("retailerZip"));
			  int userAge = Integer.parseInt(request.getParameter("userAge"));
			  int reviewRating = Integer.parseInt(request.getParameter("reviewRating"));


			  ReviewBean rb = new ReviewBean();
			  rb.setproductModelName(productModelName);
			  rb.setproductCategory(productCategory);
			  rb.setretailerName(retailerName);
			  rb.setretailerCity(retailerCity);
			  rb.setretailerState(retailerState);
			  rb.setproductSale(productSale);
			  rb.setmanufactureName(manufactureName);
			  rb.setmanufactureRebate(manufactureRebate);
			  rb.setuserName(userName);
			  rb.setuserGender(userGender);
			  rb.setuserOccupation(userOccupation);
			  rb.setreviewDate(reviewDate);
			  rb.setreviewText(reviewText);
			  rb.setproductPrice(productPrice);
			  rb.setretailerZip(retailerZip);
			  rb.setuserAge(userAge);
			  rb.setreviewRating(reviewRating);

        try{
          MongoDBDataStoreUtilities m = new MongoDBDataStoreUtilities();
          DB db = m.getConnection();
          if(db!=null){
            m.insertReview(db, rb);
            helper.printHtml("Header.html");
        		helper.printHtml("LeftNavigationBar.html");
        		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        		pw.print("<a style='font-size: 24px;'>Review Status</a></h2>");
        		pw.print("<div class='entry'>");
        		pw.print("<p style='font-size:20px;color:green;'>Your Review for <b>#"+rb.getproductModelName()+"</b> is Stored Successfully !<p>");
        		pw.print("</div></div></div>");
        		helper.printHtml("Footer.html");
          }
          else{
            helper.printHtml("Header.html");
        		helper.printHtml("LeftNavigationBar.html");
        		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        		pw.print("<a style='font-size: 24px;'>Review Status</a></h2>");
        		pw.print("<div class='entry'>");
        		pw.print("<p style='font-size:20px;color:green;'>Mongo DB is not up and running. Try again later ! <p>");
        		pw.print("</div></div></div>");
        		helper.printHtml("Footer.html");
          }

      }
      catch(Exception e){
        e.printStackTrace();
      }
	}

}
