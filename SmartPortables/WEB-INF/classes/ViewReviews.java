import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class ViewReviews extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);
    String productModelName = request.getParameter("productmodelname");
		int i = 1;
		helper.printHtml("Header.html");
		helper.printHtml("LeftNavigationBar.html");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Reviews</a>");
		pw.print("</h2><div class='entry'>");
  //  System.out.println("product name -------------- "+productModelName);

    try{

      MongoDBDataStoreUtilities m = new MongoDBDataStoreUtilities();
      HashMap<String, ArrayList<ReviewBean>> reviewhm = m.getReviews();

      if(reviewhm.containsKey(productModelName)){

      //    System.out.println("product name -------------- "+prod);
    			ArrayList<ReviewBean> reviews = reviewhm.get(productModelName);
          for(ReviewBean rb : reviews){

    		//for (ReviewBean rb : reviewhm.get(productModelName)) {
            pw.print("<table  class='gridtable'>");
        	//	pw.print("<th>No.</th><th>Product Name</th><th>Product Price</th>");

      			pw.print("<tr><td>Review # </td><td>"+i+"</td></tr>");
            pw.print("<tr><td>Product Model Name</td><td>"+rb.getproductModelName()+"</td></tr>");
            pw.print("<tr><td>User Name</td><td>"+rb.getuserName()+"</td></tr>");
            pw.print("<tr><td>Product Category</td><td>"+rb.getproductCategory()+"</td></tr>");
            pw.print("<tr><td>Review Rating</td><td>"+rb.getreviewRating()+"</td></tr>");
            pw.print("<tr><td>Review date</td><td>"+rb.getreviewDate()+"</td></tr>");
            pw.print("<tr><td>Review text</td><td>"+rb.getreviewText()+"</td></tr>");
            pw.print("</table>");
            pw.print("<br><br>");
      			i++;
        }
      }
      else{
        pw.print("<p style='font-size:20px;color:green;'>No Reviews for Product: <b>"+productModelName+"</b> yet!<p>");
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
		pw.print("</div></div></div>");
    helper.printHtml("Footer.html");

  }

}
