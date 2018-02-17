import java.io.*;
import java.sql.*;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.*;

public class Login extends HttpServlet {

	protected void doPost(HttpServletRequest request,
	HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);
		MySqlDataStoreUtilities msdb = new MySqlDataStoreUtilities();


		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String roleType = request.getParameter("roleType");

		HashMap<String, UserBean> hm = new HashMap<String, UserBean>();

		if(!helper.checkLoggedIn()){
			try{
				Connection conn = msdb.getConnection();
				if(conn == null){
					response.sendRedirect("MysqlError");
				}
				hm = msdb.selectUsers(username);
			}
			catch(Exception e){
				e.printStackTrace();
				//response.sendRedirect("MysqlError");
			}
			if(hm.containsKey(username)){

				UserBean existing_user = hm.get(username);
				if(existing_user!=null){
					String existing_user_password = existing_user.getPassword();
					String existing_user_role = existing_user.getRoleType();

					if (password.equals(existing_user_password) && roleType.equals(existing_user_role)) {
						HttpSession session = request.getSession(true);

						session.setAttribute("username", existing_user.getName());
						session.setAttribute("roleType", existing_user.getRoleType());

						response.sendRedirect("Home");
						return;
					}
				}
			}
			displayLogin(request, response, pw, true);
		}
		else{
			response.sendRedirect("Home");
		}
	}

	protected void doGet(HttpServletRequest request,
	HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);
		if(!helper.checkLoggedIn()){
			displayLogin(request, response, pw, false);
		}
		else{
			response.sendRedirect("Home");
		}

	}

	protected void displayLogin(HttpServletRequest request,
	HttpServletResponse response, PrintWriter pw, boolean error)
	throws ServletException, IOException {

		Helper helper = new Helper(request, pw);
		helper.printHtml("Header.html");
		pw.print("<div class='post' style='float: none; width: 100%'>");
		pw.print("<h2 class='title meta'><a style='font-size: 24px;'>Login</a></h2>"
		+ "<div class='entry'>"
		+ "<div style='width:400px; margin:25px; margin-left: auto;margin-right: auto;'>");
		if (error)
		pw.print("<h4 style='color:red'>Please check your username, password and user role!</h4>");
		HttpSession session = request.getSession(true);
		if(session.getAttribute("login_msg")!=null){
			pw.print("<h4 style='color:red'>"+session.getAttribute("login_msg")+"</h4>");
			session.removeAttribute("login_msg");
		}
		pw.print("<form method='post' action='Login'>"
		+ "<table style='width:100%'><tr><td>"
		+ "<h3>Username</h3></td><td><input type='text' name='username' value='' class='input' required></input>"
		+ "</td></tr><tr><td>"
		+ "<h3>Password</h3></td><td><input type='password' name='password' value='' class='input' required></input>"
		+ "</td></tr><tr><td>"
		+ "<h3>User Role</h3></td><td><select name='roleType' class='input'><option value='Customer' selected>Customer</option><option value='Retailer'>Store Manager</option><option value='SalesMan'>Sales Man</option></select>"
		+ "</td></tr><tr><td></td><td>"
		+ "<input type='submit' class='btnbuy' value='Login' style='float: right;height: 20px margin: 20px; margin-right: 10px;'></input>"
		+ "</td></tr><tr><td></td><td>"
		+ "<strong><a class='' href='Registration' style='float: right;height: 20px margin: 20px;'>New User? Register here!</a></strong>"
		+ "</td></tr></table>"
		+ "</form>" + "</div></div></div>");
		helper.printHtml("Footer.html");
	}

}
