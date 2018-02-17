import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SaxParserXMLUtility extends DefaultHandler {
  ProductBean product;
  AccessoriesBean accessory;
  static HashMap<Integer, ProductBean> productCatalog ;
  static HashMap<String, AccessoriesBean> accessoryCatalog ;
  MySqlDataStoreUtilities sqdb = new MySqlDataStoreUtilities();
  String productsXmlFileName;
  String elementValueRead;

  public SaxParserXMLUtility(){

  }

  public SaxParserXMLUtility(String productsXmlFileName) {
    this.productsXmlFileName = productsXmlFileName;
    productCatalog = new HashMap<Integer, ProductBean>();
    accessoryCatalog = new HashMap<String, AccessoriesBean>();
    parseDocument();
    try{
        sqdb.dbCleanup();
		    sqdb.insert_products_accessories(productCatalog, accessoryCatalog);
		}
		catch(Exception e){
	    e.printStackTrace();
	  }
    prettyPrint();
  }


  private void parseDocument() {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    try {
      SAXParser parser = factory.newSAXParser();
      parser.parse(productsXmlFileName, this);
    } catch (ParserConfigurationException e) {
      System.out.println("ParserConfig error");
    } catch (SAXException e) {
      System.out.println("SAXException : xml not well formed");
    } catch (IOException e) {
      System.out.println("IO error");
    }
  }


  private void prettyPrint() {
    System.out.println("prettyPrint ");
    for (Integer prodId: productCatalog.keySet()) {
      product = productCatalog.get(prodId);
    //  System.out.println("Keys are: "+product.getId()+" Value: "+ product.getName());
    }

    for (String prodId: accessoryCatalog.keySet()) {
      accessory = accessoryCatalog.get(prodId);
    //  System.out.println("Keys are: "+accessory.getId()+" Value: "+ accessory.getName());
    }

  }

  public HashMap<Integer, ProductBean> getProductMap() {
    return productCatalog = sqdb.getProducts();
  }

  public HashMap<String, AccessoriesBean> getAccessoryMap() {
    return accessoryCatalog = sqdb.getAccessories();
  }

  @Override
  public void startElement(String str1, String str2, String elementName, Attributes attributes) throws SAXException {
  //  System.out.println("In startElement ");
    if (elementName.equalsIgnoreCase("Product")) {
      product = new ProductBean();
      product.setId(Integer.parseInt(attributes.getValue("id")));
    }

    if (elementName.equalsIgnoreCase("ProductAccessory")) {
      accessory = new AccessoriesBean();
      accessory.setId(attributes.getValue("aid"));
    }

  }

  @Override
  public void endElement(String str1, String str2, String element) throws SAXException {
  //  System.out.println("In endElement ");
    if (element.equals("Product")) {
      productCatalog.put(product.getId(),product);
      return;
    }

    if (element.equals("ProductAccessory")) {
      accessoryCatalog.put(accessory.getId(),accessory);
      return;
    }

    if (element.equalsIgnoreCase("name")) {
      product.setName(elementValueRead);
      return;
    }

    if(element.equalsIgnoreCase("cost")){
      product.setCost(Integer.parseInt(elementValueRead));
      return;
    }

    if (element.equalsIgnoreCase("image")) {
      product.setImage(elementValueRead);
      return;
    }

    if (element.equalsIgnoreCase("category")) {
      product.setCategory(elementValueRead);
      return;
    }

    if (element.equalsIgnoreCase("rating")) {
      product.setRating(elementValueRead);
      return;
    }

    if (element.equalsIgnoreCase("brand")) {
      product.setBrand(elementValueRead);
      return;
    }

    if (element.equalsIgnoreCase("discount")) {
      product.setDiscount(Integer.parseInt(elementValueRead));
      return;
    }

    if (element.equalsIgnoreCase("onsale")) {
      product.setOnsale(elementValueRead);
      return;
    }

    if (element.equalsIgnoreCase("manufacturerebate")) {
      product.setManufacturerebate(elementValueRead);
      return;
    }

    if (element.equalsIgnoreCase("quantity")) {
      product.setQuantity(Integer.parseInt(elementValueRead));
      return;
    }

    if(element.equalsIgnoreCase("accessory")){
      product.getAccessories().add(elementValueRead);
      return;
    }

    if (element.equalsIgnoreCase("accname")) {
      accessory.setName(elementValueRead);
      return;
    }

    if(element.equalsIgnoreCase("acccost")){
      accessory.setCost(Integer.parseInt(elementValueRead));
      return;
    }

    if (element.equalsIgnoreCase("accimage")) {
      accessory.setImage(elementValueRead);
      return;
    }

    if (element.equalsIgnoreCase("acccategory")) {
      accessory.setCategory(elementValueRead);
      return;
    }

    if (element.equalsIgnoreCase("accrating")) {
      accessory.setRating(elementValueRead);
      return;
    }

    if (element.equalsIgnoreCase("accbrand")) {
      accessory.setBrand(elementValueRead);
      return;
    }

    if (element.equalsIgnoreCase("accdiscount")) {
      accessory.setDiscount(Integer.parseInt(elementValueRead));
      return;
    }

    if (element.equalsIgnoreCase("acconsale")) {
      accessory.setOnsale(elementValueRead);
      return;
    }

    if (element.equalsIgnoreCase("accmanufacturerebate")) {
      accessory.setManufacturerebate(elementValueRead);
      return;
    }

    if (element.equalsIgnoreCase("accquantity")) {
      accessory.setQuantity(Integer.parseInt(elementValueRead));
      return;
    }
  }

  @Override
  public void characters(char[] content, int begin, int end) throws SAXException {
    elementValueRead = new String(content, begin, end);
  }

  public static void addProductHashmap() {
    String TOMCAT_HOME = System.getProperty("catalina.home");
    new SaxParserXMLUtility(TOMCAT_HOME+"\\webapps\\SmartPortables\\WEB-INF\\xml\\ProductCatalog.xml");

  }
}
