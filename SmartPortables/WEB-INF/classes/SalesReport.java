import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;



public class SalesReport extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);

		helper.printHtml("Header.html");
		helper.printHtml("LeftNavigationBar.html");
		pw.print("<div id='content'>");
    pw.print("<div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Sales Report</a>");

		pw.print("</h2><div class='entry'>");
    pw.print("<p style='font-size:20px;color:green;'>Overall Product Sales Report:<p>");

    try{
      int i = 1;
      MySqlDataStoreUtilities mysqldb = new MySqlDataStoreUtilities();
      ArrayList<SalesReportBean> salesList = mysqldb.getSalesReport();

      pw.print("<table  class='gridtable'>");
      pw.print("<th>No.</th><th>Product Name</th><th>Product Price</th><th>No. of Items Sold</th><th>Total Sales</th>");
          for(SalesReportBean sl : salesList){

      			pw.print("<tr><td>"+i+"</td>");
            pw.print("<td>"+sl.getItemName()+"</td>");
            pw.print("<td>"+sl.getItemPrice()+"</td>");
            pw.print("<td>"+sl.getItemCount()+"</td>");
            pw.print("<td>"+sl.getTotalSales()+"</td></tr>");
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
    pw.print("<p style='font-size:20px;color:green;'>BarChart: Productname Vs TotalSales :<p>");

    try{

      MySqlDataStoreUtilities mysqldb = new MySqlDataStoreUtilities();
      ArrayList<SalesReportBean> salesList = mysqldb.getSalesReport();

      pw.println("<script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script>");
  		pw.println("<script type='text/javascript'>");
  		pw.println("google.charts.load('current', {packages: ['corechart', 'bar']});");
  		pw.println("google.charts.setOnLoadCallback(drawBasic);");
  		pw.println("function drawBasic() {");
  		pw.println("var data = google.visualization.arrayToDataTable([");
  		pw.println("['Product name', 'Total Sales',],");

      for(SalesReportBean sl: salesList){

          String prodName = sl.getItemName();
          float prodSale = sl.getTotalSales();
          pw.print("[' " +prodName+ " ', "+prodSale+ "],");


        }

      	pw.println("]);");

        pw.println("var options = {");
    		pw.println("title: 'ProductName Vs TotalSales',");
    		pw.println("chartArea: {width: '60%', height: 300},");
    		pw.println("hAxis: {");
    		pw.println("title: 'Total Sales',");
    		pw.println("minValue: 0");
    		pw.println("},");
    		pw.println("vAxis: {");
    		pw.println("title: 'Product Name'");
    		pw.println("}");
    		pw.println("};");
    		pw.println("var chart = new google.visualization.BarChart(document.getElementById('chart_div'));");
    		pw.println("chart.draw(data, options);");
    		pw.println("}");
    		pw.println("</script>");

        pw.println("<div id = 'chart_div' style='width: 900px; height: 400px;'></div>");

        pw.print("<br><br>");

    }
    catch(Exception e){
      e.printStackTrace();
    }
		pw.print("</div>");


		pw.print("</h2><div class='entry'>");
    pw.print("<p style='font-size:20px;color:green;'>Total daily sales transactions:<p>");

    try{
      int i = 1;
      MySqlDataStoreUtilities mysqldb = new MySqlDataStoreUtilities();
      ArrayList<SalesReportBean> dailySalesList = mysqldb.getDaywiseSalesReport();

      pw.print("<table  class='gridtable'>");
      pw.print("<th>No.</th><th>Transaction Date</th><th>Total Sales</th>");
          for(SalesReportBean sb : dailySalesList){

      			pw.print("<tr><td>"+i+"</td>");
            pw.print("<td>"+sb.getOrderDate()+"</td>");
            pw.print("<td>"+sb.getDaySales()+"</td></tr>");

      			i++;
        }
        pw.print("</table>");
        pw.print("<br><br>");

    }
    catch(Exception e){
      e.printStackTrace();
    }
		pw.print("</div>");

/*
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
*/
    pw.print("</div></div>");
    helper.printHtml("Footer.html");

  }

}
