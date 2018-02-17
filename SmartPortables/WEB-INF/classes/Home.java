import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class Home extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		Helper helper = new Helper(request,pw);
		helper.printHtml("Header.html");
		helper.printHtml("LeftNavigationBar.html");
		helper.printHtml("Content.html");
		helper.printHtml("Footer.html");

	}

}
