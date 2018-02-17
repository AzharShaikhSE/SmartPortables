import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AccessoryList extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		String name = null;
		String category = request.getParameter("category");
		HashMap<String, AccessoriesBean> speakerAccessoryHm = new HashMap<String, AccessoriesBean>();
		AccessoriesBean accessory;
		HashMap<String, AccessoriesBean> accessoryhm;
		SaxParserXMLUtility sp = new SaxParserXMLUtility();
		accessoryhm = sp.getAccessoryMap();


		for (String prodId: accessoryhm.keySet()) {
			accessory = accessoryhm.get(prodId);

			if(accessory.getBrand().equalsIgnoreCase("ABC")){

				if(category==null){
					accessoryhm.put(accessory.getId(), accessory);
					name = "";
				}else{
					if(category.equals("bose") && accessory.getBrand().equalsIgnoreCase("Bose")){
						accessoryhm.put(accessory.getId(), accessory);
						name = accessory.getBrand();
					}
					else if(category.equals("doss") && accessory.getBrand().equalsIgnoreCase("Doss")){
						accessoryhm.put(accessory.getId(), accessory);
						name = accessory.getBrand();
					}
					else if(category.equals("logitech") && accessory.getBrand().equalsIgnoreCase("Logitech")){
						accessoryhm.put(accessory.getId(), accessory);
						name = accessory.getBrand();
					}
				}
			}
		} //end for loop
		Helper helper = new Helper(request,pw);
		helper.printHtml("Header.html");
		helper.printHtml("LeftNavigationBar.html");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'> Accessories </a>");
		pw.print("</h2><div class='entry'><table id='bestseller'>");
		int i = 1;
		int size= accessoryhm.size();

		for (String speakerId: accessoryhm.keySet()) {
			accessory = accessoryhm.get(speakerId);


			if(i%3==1) pw.print("<tr>");
			pw.print("<td><div id='shop_item'>");
			pw.print("<h3>"+accessory.getName()+"</h3>");
			pw.print("<strong>"+accessory.getCost()+"$</strong><ul>");
			pw.print("<li id='item'><img src='"+accessory.getImage()+"' alt='' /></li>");
			pw.print("<li><form method='post' action='Cart'>" +
			"<input type='hidden' name='id' value='"+accessory.getId()+"'>"+
			"<input type='hidden' name='name' value='"+accessory.getName()+"'>"+
			"<input type='hidden' name='type' value='"+accessory.getCategory()+"'>"+
			"<input type='hidden' name='cost' value='"+accessory.getCost()+"'>"+
			"<input type='hidden' name='brand' value='"+accessory.getBrand()+"'>"+
			"<input type='hidden' name='access' value=''>"+
			"<input type='submit' class='inputButton' value='Buy Now' href='#'></a></input></form></li>");
			pw.print("<li><a class='btnbuy' href='ViewReviews?productmodelname="+accessory.getName()+"'>View Reviews</a></li>");
			pw.print("<li><a class='btnbuy' href='WriteReview?name="+accessory.getName()+"&category="+accessory.getCategory()+"&price="+accessory.getCost()+"&brandname="+accessory.getBrand()+"'>Write Review</a></li>");
			pw.print("</ul></div></td>");
			if(i%3==0 || i == size) pw.print("</tr>");
			i++;
		}
		pw.print("</table></div></div></div>");
		helper.printHtml("Footer.html");

	}
}
