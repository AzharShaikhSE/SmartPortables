import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SearchedProductServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("in search servlet");
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		int prodId = Integer.parseInt((String)request.getAttribute("prodId"));
		System.out.println(prodId);
		ProductBean product;
		AccessoriesBean accessory;
		HashMap<Integer, ProductBean> producthm;
		HashMap<String, AccessoriesBean> accessoryhm;
		ArrayList<Object> products = new ArrayList<Object>();
		Helper helper = new Helper(request,pw);
		AjaxUtility aj = new AjaxUtility();
		products = aj.getDataFromSQL();
		producthm = (HashMap<Integer, ProductBean>)products.get(0);
		accessoryhm = (HashMap<String, AccessoriesBean>)products.get(1);

		if(prodId<100){
			for (Integer pid: producthm.keySet()) {
				product = producthm.get(pid);
				System.out.println(product.getName());
				if(product.getId() == prodId){
					helper.printHtml("Header.html");
					helper.printHtml("LeftNavigationBar.html");
					pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
					pw.print("<a style='font-size: 24px;'> Searched Product:"+product.getName()+"</a>");
					pw.print("</h2><div class='entry'><table id='bestseller'>");

					pw.print("<tr>");
					pw.print("<td><div id='shop_item'>");
					pw.print("<h3>"+product.getName()+"</h3>");
					pw.print("<strong>"+product.getCost()+"$</strong><ul>");
					pw.print("<li id='item'><img src='"+product.getImage()+"' alt='' /></li>");
					pw.print("<li><form method='post' action='Cart'>" +
					"<input type='hidden' name='id' value='"+product.getId()+"'>"+
					"<input type='hidden' name='name' value='"+product.getName()+"'>"+
					"<input type='hidden' name='type' value='"+product.getCategory()+"'>"+
					"<input type='hidden' name='cost' value='"+product.getCost()+"'>"+
					"<input type='hidden' name='brand' value='"+product.getBrand()+"'>"+
					"<input type='hidden' name='access' value=''>"+
					"<input type='submit' class='inputButton' value='Buy Now' href='#'></a></input></form></li>");
					pw.print("<li><a class='btnbuy' href='ViewReviews?productmodelname="+product.getName()+"'>View Reviews</a></li>");
					pw.print("<li><a class='btnbuy' href='WriteReview?name="+product.getName()+"&category="+product.getCategory()+"&price="+product.getCost()+"&brandname="+product.getBrand()+"'>Write Review</a></li>");
					pw.print("</ul></div></td>");
					pw.print("</tr>");
				}
			}
		}
		else{
			for (String aid: accessoryhm.keySet()) {
				accessory = accessoryhm.get(aid);
				if(accessory.getId().equals(prodId+"")){
					helper.printHtml("Header.html");
					helper.printHtml("LeftNavigationBar.html");
					pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
					pw.print("<a style='font-size: 24px;'> Searched Product:"+accessory.getName()+"</a>");
					pw.print("</h2><div class='entry'><table id='bestseller'>");

					pw.print("<tr>");
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
					pw.print("</tr>");
				}
			}
		}
		pw.print("</table></div></div></div>");
		helper.printHtml("Footer.html");
	}
}
