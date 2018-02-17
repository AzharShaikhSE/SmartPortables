import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;


public class MysqlError extends HttpServlet {

protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  response.setContentType("text/html");
  PrintWriter pw = response.getWriter();
  HttpSession session = request.getSession(true);
  Helper helper = new Helper(request, pw);

  helper.printHtml("Header.html");
  helper.printHtml("LeftNavigationBar.html");
  pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
  pw.print("<a style='font-size: 24px;'>MySQL Error</a></h2>");
  pw.print("<div class='entry'>");
  pw.print("<p style='font-size:20px;color:green;'>MySQL Server is down and not running. Sorry, Try again later !<p>");
  pw.print("</div></div></div>");
  helper.printHtml("Footer.html");
}

}
