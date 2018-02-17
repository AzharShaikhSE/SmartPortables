import javax.servlet.*;
import javax.servlet.http.*;

public class Startup extends HttpServlet {
  public void init() throws ServletException{
    SaxParserXMLUtility.addProductHashmap();
  }
}
