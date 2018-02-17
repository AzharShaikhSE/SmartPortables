import java.io.*;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.*;

public class Registration extends HttpServlet {
	private String errorMessage;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		displayRegistrationPage(request, response, pw, false);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);
		MySqlDataStoreUtilities msdb = new MySqlDataStoreUtilities();

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String repassword = request.getParameter("repassword");
		String roleType = request.getParameter("roleType");


		if(helper.checkLoggedIn() == false)
		  roleType = request.getParameter("roleType");


		if(password.equals(repassword) == false){
			errorMessage = "Passwords doesn't match!";
		}else{
			HashMap<String, UserBean> hm = new HashMap<String, UserBean>();
			try{
				hm = msdb.selectUsers(username);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			if(hm.containsKey(username)){
				errorMessage = "Username already exist as " + roleType;
			}
			else{
				UserBean user = new UserBean(username,password,roleType);
				hm.put(username,user);
				msdb.insertUser(username,password,roleType);

				HttpSession session = request.getSession(true);
				session.setAttribute("login_msg", "Your "+roleType+" account has been created. Please login Now !");
				if(helper.checkLoggedIn() == false){
					response.sendRedirect("Login"); return;
				}
			}
		}

		if(helper.checkLoggedIn() == true){
			HttpSession session = request.getSession(true);
			session.setAttribute("login_msg", "Customer "+username+" account has been created!");
			response.sendRedirect("Account"); return;
		}
		displayRegistrationPage(request, response, pw, true);

	}

	protected void displayRegistrationPage(HttpServletRequest request,
	HttpServletResponse response, PrintWriter pw, boolean error)
	throws ServletException, IOException {

		Helper helper = new Helper(request, pw);
		helper.printHtml("Header.html");
		pw.print("<div class='post' style='float: none; width: 100%'>");
		pw.print("<h2 class='title meta'><a style='font-size: 24px;'>Registration</a></h2>"
		+ "<div class='entry'>"
		+ "<div style='width:400px; margin:25px; margin-left: auto;margin-right: auto;'>");
		if (error)
		pw.print("<h4 style='color:red'>"+errorMessage+"</h4>");
		pw.print("<form method='post' action='Registration'>"
		+ "<table style='width:100%'><tr><td>"
		+ "<h3>Username</h3></td><td><input type='text' name='username' value='' class='input' required></input>"
		+ "</td></tr><tr><td>"
		+ "<h3>Password</h3></td><td><input type='password' name='password' value='' class='input' required></input>"
		+ "</td></tr><tr><td>"
		+ "<h3>Re-Password</h3></td><td><input type='password' name='repassword' value='' class='input' required></input>"
		+ "</td></tr><tr><td>"
		+ "<h3>User Role</h3></td><td><select name='roleType' class='input'><option value='Customer' selected>Customer</option><option value='Retailer'>Store Manager</option><option value='SalesMan'>Sales Man</option></select>"
		+ "</td></tr></table>"
		+ "<input type='submit' class='btnbuy' name='ByUser' value='Create User' style='float: right;height: 20px margin: 20px; margin-right: 10px;'></input>"
		+ "</form>" + "</div></div></div>");
		helper.printHtml("Footer.html");
	}
}
