import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
//import org.json.simple.JSONObject;


public class Trending extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);

		helper.printHtml("Header.html");
		helper.printHtml("LeftNavigationBar.html");
		pw.print("<div id='content'>");
    pw.print("<div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Reviews</a>");

		pw.print("</h2><div class='entry'>");
    pw.print("<p style='font-size:20px;color:green;'>Top 5 most liked products based on Rating:<p>");

    try{
      int i = 1;
      MongoDBDataStoreUtilities m = new MongoDBDataStoreUtilities();
      ArrayList<ReviewBean> reviewList = m.getTop5LikedProducts();

      pw.print("<table  class='gridtable'>");
      pw.print("<th>No.</th><th>Product Model Name</th><th>Product Category</th><th>Review Rating</th><th>Product Price</th>");
          for(ReviewBean rb : reviewList){

      			pw.print("<tr><td>"+i+"</td>");
            pw.print("<td>"+rb.getproductModelName()+"</td>");
            pw.print("<td>"+rb.getproductCategory()+"</td>");
            pw.print("<td>"+rb.getreviewRating()+"</td>");
            pw.print("<td>"+rb.getproductPrice()+"</td></tr>");
      			i++;
        }
        pw.print("</table>");
        pw.print("<br><br>");

    }
    catch(Exception e){
      e.printStackTrace();
    }
		pw.print("</div>");


		pw.print("</h2><div class='entry'>");
    pw.print("<p style='font-size:20px;color:green;'>Top 5 zip-codes where maximum number of products sold:<p>");
  //  System.out.println("product name -------------- "+productModelName);

    try{
      int i = 1;
      MongoDBDataStoreUtilities m = new MongoDBDataStoreUtilities();
      ArrayList<ReviewBean> reviewList = m.getTop5Zipcodes();

      pw.print("<table  class='gridtable'>");
      pw.print("<th>No.</th><th>Zip Code</th><th>Review Count</th>");
          for(ReviewBean rb : reviewList){

      			pw.print("<tr><td>"+i+"</td>");
            pw.print("<td>"+rb.getretailerZip()+"</td>");
            pw.print("<td>"+rb.getreviewCount()+"</td></tr>");

      			i++;
        }
        pw.print("</table>");
        pw.print("<br><br>");

    }
    catch(Exception e){
      e.printStackTrace();
    }
		pw.print("</div>");


		pw.print("</h2><div class='entry'>");
    pw.print("<p style='font-size:20px;color:green;'>Top 5 most sold products based on Review count: <p>");
  //  System.out.println("product name -------------- "+productModelName);

    try{
      int i = 1;
      MongoDBDataStoreUtilities m = new MongoDBDataStoreUtilities();
      ArrayList<ReviewBean> reviewList = m.getTop5SoldProducts();

      pw.print("<table  class='gridtable'>");
      pw.print("<th>No.</th><th>Product Model Name</th><th>Product Category</th><th>Review Rating</th><th>Product Price</th><th>Review Count</th>");
          for(ReviewBean rb : reviewList){

      			pw.print("<tr><td>"+i+"</td>");
            pw.print("<td>"+rb.getproductModelName()+"</td>");
            pw.print("<td>"+rb.getproductCategory()+"</td>");
            pw.print("<td>"+rb.getreviewRating()+"</td>");
            pw.print("<td>"+rb.getproductPrice()+"</td>");
            pw.print("<td>"+rb.getreviewCount()+"</td></tr>");
      			i++;
        }
        pw.print("</table>");
        pw.print("<br><br>");

    }
    catch(Exception e){
      e.printStackTrace();
    }
		pw.print("</div>");

    pw.print("</div></div>");
    helper.printHtml("Footer.html");

  }

}
