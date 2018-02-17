import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.text.*;


public class DeleteOrder extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/html");
    PrintWriter pw = response.getWriter();
    Helper helper = new Helper(request, pw);
    HttpSession session = request.getSession(true);
    String userName = request.getParameter("username");
    int orderNum = Integer.parseInt(request.getParameter("ordernum"));
    String deliveryDate = request.getParameter("deliverydate");
    String currentDate = helper.currentDate();
    long diffdays=0;

    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    try{
      Date date1 = format.parse(deliveryDate);
      Date date2 = format.parse(currentDate);
      System.out.println("date 1: "+date1);
      System.out.println("date 2: "+date2);

      long difference = date1.getTime() - date2.getTime();
      diffdays = difference/(1000*60*60*24);
      System.out.println("date diff: "+diffdays);
    }

    catch(Exception e){
      e.printStackTrace();
    }
    if(diffdays > 5)
    helper.deleteOrder(userName, orderNum);
    else
    session.setAttribute("error_msg", "Order has been already SHIPPED, Cannot Cancel the order now. Sorry! you need to Cancel 5 days prior to the delivery date !");
    response.sendRedirect("Account");

  }


}
