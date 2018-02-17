import java.io.IOException;
import javax.servlet.*;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.util.*;


public class AutoCompleteServlet extends HttpServlet {

  private ServletContext context;

  public void init(ServletConfig config) throws ServletException {
    this.context = config.getServletContext();
  }
  public void doGet(HttpServletRequest request, HttpServletResponse response)
  throws IOException, ServletException {
    System.out.println("In Auto Complete");

    String action = request.getParameter("action");
    String searchId = request.getParameter("searchId");
    StringBuffer sb = new StringBuffer();

    try
    {
      boolean namesAdded = false;
      if (action.equals("complete"))
      {
        if (!searchId.equals(""))
        { AjaxUtility aj = new AjaxUtility();
          sb = aj.readData(searchId);
          if(sb!=null || !sb.equals(""))
          {
            namesAdded=true;
          }
          if (namesAdded)
          {
            System.out.println("In names added");
            response.setContentType("text/xml");
            response.getWriter().write("<products>" + sb.toString() + "</products>");
          }
        }
      }
    } catch (Exception e){
      e.printStackTrace();
    }

    if (action.equals("lookup")) {

      request.setAttribute("prodId", searchId);
      context.getRequestDispatcher("/SearchedProductServlet").forward(request, response);
    }
  }
}
