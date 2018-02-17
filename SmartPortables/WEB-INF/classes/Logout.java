import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Logout extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Helper helper = new Helper(request, null);
		helper.logout();
		response.sendRedirect("Home");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Helper helper = new Helper(request, null);
		helper.logout();
		response.sendRedirect("Home");
	}

}
