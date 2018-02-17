import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LaptopList extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		String name = null;
		String brandName = request.getParameter("brand");
		HashMap<Integer, ProductBean> laptophm = new HashMap<Integer, ProductBean>();
		ProductBean product;
		HashMap<Integer, ProductBean> producthm;
		SaxParserXMLUtility sp = new SaxParserXMLUtility();
		producthm = sp.getProductMap();

		for (Integer prodId: producthm.keySet()) {
			product = producthm.get(prodId);

			if(product.getCategory().equalsIgnoreCase("Laptops")){

				if(brandName==null){
					laptophm.put(product.getId(), product);
					name = "";
				}else{
					if(brandName.equals("apple") && product.getBrand().equalsIgnoreCase("Apple")){
						laptophm.put(product.getId(), product);
						name = product.getBrand();
					}
					else if(brandName.equals("dell") && product.getBrand().equalsIgnoreCase("dell")){
						laptophm.put(product.getId(), product);
						name = product.getBrand();
					}
					else if(brandName.equals("hp") && product.getBrand().equalsIgnoreCase("hp")){
						laptophm.put(product.getId(), product);
						name = product.getBrand();
					}
				}
			}
		} //end for loop
		Helper helper = new Helper(request,pw);
		helper.printHtml("Header.html");
		helper.printHtml("LeftNavigationBar.html");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>"+name+" Laptops</a>");
		pw.print("</h2><div class='entry'><table id='bestseller'>");
		int i = 1; int size= laptophm.size();

		for (Integer laptopId: laptophm.keySet()) {
			product = laptophm.get(laptopId);


			if(i%3==1) pw.print("<tr>");
			pw.print("<td><div id='shop_item'>");
			pw.print("<h3>"+product.getName()+"</h3>");
			pw.print("<strong>"+product.getCost()+"$</strong><ul>");
			pw.print("<li id='item'><img src='"+product.getImage()+"' alt='' /></li>");
			pw.print("<li><form method='post' action='Cart'>" +
			"<input type='hidden' name='id' value='"+product.getId()+"'>"+
			"<input type='hidden' name='name' value='"+product.getName()+"'>"+
			"<input type='hidden' name='type' value='"+product.getCategory()+"'>"+
			"<input type='hidden' name='cost' value='"+product.getCost()+"'>"+
			"<input type='hidden' name='brand' value='"+brandName+"'>"+
			"<input type='hidden' name='access' value=''>"+
			"<input type='submit' class='inputButton' value='Buy Now' href='#'></a></input></form></li>");
			pw.print("<li><a class='btnbuy' href='ViewReviews?productmodelname="+product.getName()+"'>View Reviews</a></li>");
			pw.print("<li><a class='btnbuy' href='WriteReview?name="+product.getName()+"&category="+product.getCategory()+"&price="+product.getCost()+"&brandname="+product.getBrand()+"'>Write Review</a></li>");
			pw.print("</ul></div></td>");
			if(i%3==0 || i == size) pw.print("</tr>");
			i++;
		}
		pw.print("</table></div></div></div>");
		helper.printHtml("Footer.html");

	}
}
