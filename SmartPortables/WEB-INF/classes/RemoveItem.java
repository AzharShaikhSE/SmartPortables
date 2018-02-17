import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class RemoveItem extends HttpServlet {


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);

		String name = request.getParameter("name");

		helper.removeProduct(name);
		response.sendRedirect("Cart");

	}


}
