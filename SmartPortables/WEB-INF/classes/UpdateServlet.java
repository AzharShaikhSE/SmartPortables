import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UpdateServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);
		HttpSession session = request.getSession(true);
		String userName = request.getParameter("username");
		int orderNum = Integer.parseInt(request.getParameter("ordernum"));
		String email = request.getParameter("email");
		String zipcode = request.getParameter("zipcode");
		String address1 = request.getParameter("address1");
		String address2 = request.getParameter("address2");

		helper.updateOrder(orderNum,userName,email,zipcode,address1,address2);
		for (BillingOrderBean bo : helper.getBilledOrders(userName)){
			if(bo.getOrderNum() == orderNum){
				bo.setEmail(email);
				bo.setZipcode(zipcode);
				bo.setAddress1(address1);
				bo.setAddress2(address2);



				session.setAttribute("update_msg", "Order "+orderNum+" has been Updated !");
				if(helper.checkLoggedIn() == true){
					response.sendRedirect("Account"); return;
				}

			}
		}

	}

}
