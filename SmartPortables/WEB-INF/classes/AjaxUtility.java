import java.sql.*;
import java.util.*;

public class AjaxUtility
{
  Connection conn = null;
  AccessoriesBean accessoryBean;
  ProductBean productBean;
  HashMap<Integer, ProductBean> producthm;
  HashMap<String, AccessoriesBean> acessoryhm;
  ArrayList<Object> products = new ArrayList<Object>();

  public Connection getConnection() {
    try{
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartportables?useSSL=false","root","root");
    }
    catch(Exception e){
      e.printStackTrace();
    }

    return conn;
  }

  public ArrayList<Object> getDataFromSQL(){

    // Get Products

    producthm = new HashMap<Integer, ProductBean>();
    try{
      conn = getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("select * from products;");

      while(rs.next()){
        productBean = new ProductBean();
        productBean.setId(rs.getInt("productId"));
        productBean.setName(rs.getString("productName"));
        productBean.setCost(rs.getFloat("productCost"));
        productBean.setImage(rs.getString("productImage"));
        productBean.setCategory(rs.getString("productCategory"));
        productBean.setRating(rs.getString("productRating"));
        productBean.setBrand(rs.getString("productBrand"));
        productBean.setDiscount(rs.getInt("productDiscount"));
        productBean.setOnsale(rs.getString("productOnsale"));
        productBean.setManufacturerebate(rs.getString("productManufacturerebate"));
        productBean.setQuantity(rs.getInt("productQuantity"));

        Statement stmt1 = conn.createStatement();
        ResultSet rs1 = stmt1.executeQuery("select * from accessories acc where acc.productId = '"+ productBean.getId()+"'");
        ArrayList<String> accessoryIdList = new ArrayList<String>();
        while(rs1.next()){
          accessoryIdList.add(rs1.getInt("accessoryId")+"");
        }
        stmt1.close();
        rs1.close();

        productBean.setAccessories(accessoryIdList);
        producthm.put(productBean.getId(), productBean);
      }

      rs.close();
      conn.close();
      products.add(producthm);
      //  return producthm;
    }catch(Exception e){
      e.printStackTrace();
    }


    //get Accessories
    acessoryhm = new HashMap<String, AccessoriesBean>();

    try{
      conn = getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("select distinct accessoryId, accessoryName, accessoryCost, accessoryImage, accessoryCategory, accessoryRating, accessoryBrand, accessoryDiscount, accessoryOnsale, accessoryManufacturerebate, accessoryQuantity from accessories group by accessoryName");

      while(rs.next()){
        AccessoriesBean accessoryBean = new AccessoriesBean();
        accessoryBean.setId(rs.getInt("accessoryId")+"");
        accessoryBean.setName(rs.getString("accessoryName"));
        accessoryBean.setCost(rs.getFloat("accessoryCost"));
        accessoryBean.setImage(rs.getString("accessoryImage"));
        accessoryBean.setCategory(rs.getString("accessoryCategory"));
        accessoryBean.setRating(rs.getString("accessoryRating"));
        accessoryBean.setBrand(rs.getString("accessoryBrand"));
        accessoryBean.setDiscount(rs.getInt("accessoryDiscount"));
        accessoryBean.setOnsale(rs.getString("accessoryOnsale"));
        accessoryBean.setManufacturerebate(rs.getString("accessoryManufacturerebate"));
        accessoryBean.setQuantity(rs.getInt("accessoryQuantity"));
        acessoryhm.put(accessoryBean.getId(), accessoryBean);
      }
      rs.close();
      conn.close();
      products.add(acessoryhm);
    }catch(Exception e){
      e.printStackTrace();
    }

    return products;
  }

  public StringBuffer readData(String searchId)
  {
    products = getDataFromSQL();
    producthm = (HashMap<Integer, ProductBean>)products.get(0);
    acessoryhm = (HashMap<String, AccessoriesBean>)products.get(1);
    StringBuffer sb = new StringBuffer();

    Iterator it = producthm.entrySet().iterator();
    while (it.hasNext())
    {
      Map.Entry pi = (Map.Entry)it.next();
      ProductBean prod=(ProductBean)pi.getValue();
      if (prod.getName().toLowerCase().startsWith(searchId))
      {
        System.out.println("utility prod name:"+prod.getName());
        sb.append("<product>");
        sb.append("<productId>" + prod.getId() + "</productId>");
        sb.append("<productName>" + prod.getName() + "</productName>");
        sb.append("</product>");
      }
    }

    Iterator it1 = acessoryhm.entrySet().iterator();
    while (it1.hasNext())
    {
      Map.Entry ai = (Map.Entry)it1.next();
      AccessoriesBean acc =(AccessoriesBean)ai.getValue();
      if (acc.getName().toLowerCase().startsWith(searchId))
      {
        sb.append("<product>");
        sb.append("<productId>" + acc.getId() + "</productId>");
        sb.append("<productName>" + acc.getName() + "</productName>");
        sb.append("</product>");
      }
    }

    return sb;
  }

}
