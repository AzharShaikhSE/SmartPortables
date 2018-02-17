import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class Cart extends HttpServlet {


  public static HashMap<String, ArrayList<OrderItemBean>> orders = new HashMap<String, ArrayList<OrderItemBean>>();
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/html");
    PrintWriter pw = response.getWriter();
    Helper helper = new Helper(request, pw);
    int id = Integer.parseInt(request.getParameter("id"));
    String name = request.getParameter("name");
    double price = Double.parseDouble(request.getParameter("cost"));
    String type = request.getParameter("type");
    String brand = request.getParameter("brand");

    helper.storeProduct(id, name, price, type, brand);
    displayCart(request, response);
  }
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    displayCart(request, response);
  }

  protected void displayCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter pw = response.getWriter();
    Helper helper = new Helper(request,pw);
    ProductBean product;
    AccessoriesBean accessory;
    HashMap<Integer, ProductBean> productCatalog ;
    HashMap<String, AccessoriesBean> accessoryCatalog ;
    SaxParserXMLUtility sp = new SaxParserXMLUtility();

    if(!helper.checkLoggedIn()){
      HttpSession session = request.getSession(true);
      session.setAttribute("login_msg", "Please Login to add items to cart");
      response.sendRedirect("Login");
      return;
    }

    helper.printHtml("Header.html");
    helper.printHtml("LeftNavigationBar.html");
    pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
    pw.print("<a style='font-size: 24px;'>Cart("+helper.CartCount()+")</a>");
    pw.print("</h2><div class='entry'>");
    if(helper.CartCount()>0){
      pw.print("<table  class='gridtable'>");
      int i = 1;
      double total = 0;
      pw.print("<th>No.</th><th>Product Name</th><th>Product Type</th><th>Manufacturer</th><th>Product Price</th><th>Action</th>");
      for (OrderItemBean oi : helper.getCustomerOrders()) {
        pw.print("<tr>");
        pw.print("<td>"+i+".</td><td>"+oi.getName()+"</td><td>"+oi.getType()+"</td><td>"+oi.getBrand()+"</td><td>$  "+oi.getPrice()+"</td><td><a href='RemoveItem?name="+oi.getName()+"' class='btnbuy'>Remove Item</a></td>");
        pw.print("</tr>");
        total = total +oi.getPrice();
        i++;
      }
      pw.print("<tr><th></th><th></th><th></th><th>Total</th><th>"+total+"</th>");
      pw.print("<tr><td></td><td></td><td></td><td></td><td><a href='CheckOut' class='btnbuy'>Check Out</a></td>");
      pw.print("</table>");
    }else{
      pw.print("<h4 style='color:red'>Your Cart is empty</h4>");
    }
    if(helper.CartCount()>0){

      int prodId = 0;
      ArrayList<OrderItemBean> oi = helper.getCustomerOrders();
      if(oi.size() != 0){
        OrderItemBean order = oi.get(oi.size()-1);
        prodId = order.getId();
        int i = 1;
        while(prodId > 100 == true){
          i+=1;
          if(oi.size()-i >= 0){
            order = oi.get(oi.size()-i);
            prodId = order.getId();
          } else{
            break;
          }
        }
      }
      ArrayList<String> accId = new ArrayList<String>();

      if(prodId <= 100){
        productCatalog = sp.getProductMap();
        product = productCatalog.get(prodId);
        accId = product.getAccessories();
        pw.print("<br><br><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>Accessories</a></h2>");


        pw.print("<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css'>");
        pw.print("<link rel='stylesheet prefetch' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css'>");
        pw.print("<link rel='stylesheet' href='css/carousel.css'>");
        pw.print("<div class='container'style='width:90%' ><div class='row'><div class='col-md-12'><div class='carousel slide multi-item-carousel' id='theCarousel'><div class='carousel-inner'>");
        int item = 0;
        for(String aid : accId){
          if(item == 0)
          pw.print("<div class='item active'>");
          else
          pw.print("<div class='item'>");

          item++;
          pw.print("<div class='col-xs-4'>");

          accessoryCatalog = sp.getAccessoryMap();
          accessory = accessoryCatalog.get(aid);
          System.out.println("aid ======= "+aid);
          pw.print("<div id='shop_item'>");
          pw.print("<h3>"+accessory.getName()+"</h3>");
          pw.print("<strong>"+accessory.getCost()+"$</strong><ul>");
          pw.print("<li id='item'><img src='"+accessory.getImage()+"' alt='' /></li>");
          pw.print("<li><form method='post' action='Cart'>" +
          "<input type='hidden' name='id' value='"+accessory.getId()+"'>"+
          "<input type='hidden' name='name' value='"+accessory.getName()+"'>"+
          "<input type='hidden' name='cost' value='"+accessory.getCost()+"'>"+
          "<input type='hidden' name='brand' value='"+accessory.getBrand()+"'>"+
          "<input type='hidden' name='access' value=''>"+
          "<input type='submit' class='inputButton' value='Buy Now' href='#'></a></input></form></li>");
          pw.print("<li><a class='btnbuy' href='ViewReviews?productmodelname="+accessory.getName()+"'>View Reviews</a></li>");
    			pw.print("<li><a class='btnbuy' href='WriteReview?name="+accessory.getName()+"&category="+accessory.getCategory()+"&price="+accessory.getCost()+"&brandname="+accessory.getBrand()+"'>Write Review</a></li>");
          pw.print("</ul></div>");

          pw.print("</div></div>"); // carousel div
        }

        pw.print("</div><a class='left carousel-control' href='#theCarousel' data-slide='prev'><i class='glyphicon glyphicon-chevron-left'></i></a><a class='right carousel-control' href='#theCarousel' data-slide='next'><i class='glyphicon glyphicon-chevron-right'></i></a></div></div></div></div>");
      }

    }


    pw.print("<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.2/jquery.min.js'></script>");
    pw.print("<script src='http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js'></script>");
    pw.print("<script  src='index.js'></script>");

    pw.print("</div></div></div>");
    helper.printHtml("Footer.html");

  }
}
