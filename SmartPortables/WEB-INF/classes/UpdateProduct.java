import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UpdateProduct extends HttpServlet {

  protected void doPost(HttpServletRequest request,
  HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/html");
    PrintWriter pw = response.getWriter();
    HttpSession session=request.getSession(true);
    Helper helper = new Helper(request, pw);

    String username = (String)session.getAttribute("username");
    int prodId = Integer.parseInt(request.getParameter("pid"));
    System.out.println("####### update prod id : "+ prodId);
    String name = request.getParameter("pname");
    float price = Float.parseFloat(request.getParameter("pprice"));
    String category = request.getParameter("pcategory");
    String brand = request.getParameter("pbrand");
    int discount = Integer.parseInt(request.getParameter("pdiscount"));

    helper.updateProduct(prodId,name,price,category,brand,discount);

    response.sendRedirect("Account");

  }

}
