import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
//import org.json.simple.JSONObject;


public class Inventory extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);

		helper.printHtml("Header.html");
		helper.printHtml("LeftNavigationBar.html");
		pw.print("<div id='content'>");
    pw.print("<div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Inventory Report</a>");

		pw.print("</h2><div class='entry'>");
    pw.print("<p style='font-size:20px;color:green;'>Products and their quantity available:<p>");

    try{
      int i = 1;
      MySqlDataStoreUtilities msqldb = new MySqlDataStoreUtilities();
      HashMap<Integer,ProductBean> producthm = msqldb.getProducts();
      HashMap<String, AccessoriesBean> accessoryhm = msqldb.getAccessories();


      pw.print("<table  class='gridtable'>");
      pw.print("<th>No.</th><th>Product Name</th><th>Product Price</th><th>Product Quantity</th>");
          for(Integer prodID: producthm.keySet()){
            ProductBean prod = (ProductBean)producthm.get(prodID);
      			pw.print("<tr><td>"+i+"</td>");
            pw.print("<td>"+prod.getName()+"</td>");
            pw.print("<td>"+prod.getCost()+"</td>");
            pw.print("<td>"+prod.getQuantity()+"</td>");
      			i++;
        }
        for(String accID: accessoryhm.keySet()){
          AccessoriesBean acc = (AccessoriesBean)accessoryhm.get(accID);
          pw.print("<tr><td>"+i+"</td>");
          pw.print("<td>"+acc.getName()+"</td>");
          pw.print("<td>"+acc.getCost()+"</td>");
          pw.print("<td>"+acc.getQuantity()+"</td>");
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
    pw.print("<p style='font-size:20px;color:green;'>BarChart: Productname Vs TotalItemsAvailable :<p>");

    try{

      MySqlDataStoreUtilities msqldb = new MySqlDataStoreUtilities();
      HashMap<Integer,ProductBean> producthm = msqldb.getProducts();
      HashMap<String, AccessoriesBean> accessoryhm = msqldb.getAccessories();

      pw.println("<script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script>");
  		pw.println("<script type='text/javascript'>");
  		pw.println("google.charts.load('current', {packages: ['corechart', 'bar']});");
  		pw.println("google.charts.setOnLoadCallback(drawBasic);");
  		pw.println("function drawBasic() {");
  		pw.println("var data = google.visualization.arrayToDataTable([");
  		pw.println("['Product name', 'Quantity',],");

      for(Integer prodID: producthm.keySet()){
          ProductBean prod = (ProductBean)producthm.get(prodID);

          String prodName = prod.getName();
          int prodQuantity = prod.getQuantity();
          pw.print("[' " +prodName+ " ', "+prodQuantity+ "],");


        }
        for(String accID: accessoryhm.keySet()){
          AccessoriesBean acc = (AccessoriesBean)accessoryhm.get(accID);

          String accName = acc.getName();
          int accQuantity = acc.getQuantity();
          pw.print("[' " +accName+ " ', "+accQuantity+ "],");

      }

      	pw.println("]);");

        pw.println("var options = {");
    		pw.println("title: 'Productname Vs TotalItemsAvailable',");
    		pw.println("chartArea: {width: '60%', height: 1000},");
    		pw.println("hAxis: {");
    		pw.println("title: 'Quantity of products',");
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

        pw.println("<div id = 'chart_div' style='width: 900px; height: 1100px;'></div>");

        pw.print("<br><br>");

    }
    catch(Exception e){
      e.printStackTrace();
    }
		pw.print("</div>");


		pw.print("</h2><div class='entry'>");
    pw.print("<p style='font-size:20px;color:green;'>Products on Sale:<p>");

    try{
      int i = 1;
      MySqlDataStoreUtilities msqldb = new MySqlDataStoreUtilities();
      HashMap<Integer,ProductBean> producthm = msqldb.getProducts();
      HashMap<String, AccessoriesBean> accessoryhm = msqldb.getAccessories();


      pw.print("<table  class='gridtable'>");
      pw.print("<th>No.</th><th>Product Name</th><th>Product Price</th><th>Product on Sale?</th>");
          for(Integer prodID: producthm.keySet()){
            ProductBean prod = (ProductBean)producthm.get(prodID);
            if(prod.getOnsale().equalsIgnoreCase("yes")){
              pw.print("<tr><td>"+i+"</td>");
              pw.print("<td>"+prod.getName()+"</td>");
              pw.print("<td>"+prod.getCost()+"</td>");
              pw.print("<td>"+prod.getOnsale()+"</td>");
        			i++;
            }

        }
        for(String accID: accessoryhm.keySet()){
          AccessoriesBean acc = (AccessoriesBean)accessoryhm.get(accID);
          if(acc.getOnsale().equalsIgnoreCase("yes")){
          pw.print("<tr><td>"+i+"</td>");
          pw.print("<td>"+acc.getName()+"</td>");
          pw.print("<td>"+acc.getCost()+"</td>");
          pw.print("<td>"+acc.getOnsale()+"</td>");
          i++;
        }

      }
        pw.print("</table>");
        pw.print("<br><br>");

    }
    catch(Exception e){
      e.printStackTrace();
    }
		pw.print("</div>");


		pw.print("</h2><div class='entry'>");
    pw.print("<p style='font-size:20px;color:green;'>Products with Manufacture Rebates: <p>");
  //  System.out.println("product name -------------- "+productModelName);

  try{
    int i = 1;
    MySqlDataStoreUtilities msqldb = new MySqlDataStoreUtilities();
    HashMap<Integer,ProductBean> producthm = msqldb.getProducts();
    HashMap<String, AccessoriesBean> accessoryhm = msqldb.getAccessories();


    pw.print("<table  class='gridtable'>");
    pw.print("<th>No.</th><th>Product Name</th><th>Product Price</th><th>Product with ManufactureRebate?</th>");
        for(Integer prodID: producthm.keySet()){
          ProductBean prod = (ProductBean)producthm.get(prodID);
          if(prod.getManufacturerebate().equalsIgnoreCase("yes")){
            pw.print("<tr><td>"+i+"</td>");
            pw.print("<td>"+prod.getName()+"</td>");
            pw.print("<td>"+prod.getCost()+"</td>");
            pw.print("<td>"+prod.getManufacturerebate()+"</td>");
            i++;
          }

      }
      for(String accID: accessoryhm.keySet()){
        AccessoriesBean acc = (AccessoriesBean)accessoryhm.get(accID);
        if(acc.getManufacturerebate().equalsIgnoreCase("yes")){
        pw.print("<tr><td>"+i+"</td>");
        pw.print("<td>"+acc.getName()+"</td>");
        pw.print("<td>"+acc.getCost()+"</td>");
        pw.print("<td>"+acc.getManufacturerebate()+"</td>");
        i++;
      }

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
