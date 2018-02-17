import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DeleteProduct extends HttpServlet {

  protected void doPost(HttpServletRequest request,
  HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/html");
    PrintWriter pw = response.getWriter();
    HttpSession session=request.getSession(true);
    Helper helper = new Helper(request, pw);

    String username = (String)session.getAttribute("username");
    int prodId = Integer.parseInt(request.getParameter("pid"));
    helper.deleteProduct(prodId);
    response.sendRedirect("Account");

  }

}
