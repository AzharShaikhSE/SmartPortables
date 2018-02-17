import java.sql.*;
import java.util.*;

public class MySqlDataStoreUtilities
{
  Connection conn = null;
  UserBean user;
  BillingOrderBean bo;
  ProductBean productBean;

  public Connection getConnection() {
    try{
      //Connection conn = null;
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartportables?useSSL=false","root","root");
    }
    catch(Exception e){
      e.printStackTrace();
    }

    return conn;
  }

  public HashMap<String,UserBean> selectUsers(String username){
    HashMap<String,UserBean> userhm = new HashMap<String,UserBean>();
    try{
      conn = getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("select * from registration r where r.userName = '"+ username+"'");
      while(rs.next()){
        user = new UserBean();
        user.setName(rs.getString("userName"));
        user.setPassword(rs.getString("password"));
        user.setRoleType(rs.getString("roleType"));
        userhm.put(user.getName(),user);
      }
      rs.close();
      conn.close();
      return userhm;
    }
    catch(Exception e){
      e.printStackTrace();
    }
    return userhm;
  }

  public void insertUser(String username, String password, String roleType){
    try{
      conn = getConnection();
      String insertUserQuery = "insert into registration (userName, password, roleType) values (?,?,?);";
      PreparedStatement ps = conn.prepareStatement(insertUserQuery);
      ps.setString(1,username);
      ps.setString(2,password);
      ps.setString(3,roleType);
      ps.execute();
      conn.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  public HashMap<String, ArrayList<BillingOrderBean>> getBilledOrders(String username){

    HashMap<String, ArrayList<BillingOrderBean>> billedOrderhm = new HashMap<String, ArrayList<BillingOrderBean>>();

    try{
      conn = getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("select * from customerorders co where co.userName = '"+ username+"'");
      ArrayList<BillingOrderBean> billedOrderList = new ArrayList<BillingOrderBean>();

      while(rs.next()){
        bo = new BillingOrderBean();
        bo.setOrderNum(rs.getInt("orderNum"));
        bo.setUsername(rs.getString("userName"));
        bo.setFullname(rs.getString("fullName"));
        bo.setEmail(rs.getString("email"));
        bo.setTotal(rs.getDouble("totalBill"));
        bo.setCreditcardNo(rs.getString("creditCardNo"));
        bo.setCvv(rs.getInt("cvv"));
        bo.setAddress1(rs.getString("address1"));
        bo.setAddress2(rs.getString("address2"));
        bo.setZipcode(rs.getString("zipcode"));
        bo.setOrderDate(rs.getString("orderDate"));
        bo.setDeliveryDate(rs.getString("deliveryDate"));

        // Retrieve orderItems for each billed order.
        Statement stmt1 = conn.createStatement();
        ResultSet rs1 = stmt1.executeQuery("select * from orderitem oi where oi.orderNum = '"+ bo.getOrderNum()+"'");
        ArrayList<OrderItemBean> itemList = new ArrayList<OrderItemBean>();
        while(rs1.next()){
          OrderItemBean oib = new OrderItemBean();
          oib.setId(rs1.getInt("itemId"));
          oib.setName(rs1.getString("itemName"));
          oib.setPrice(rs1.getDouble("itemPrice"));
          oib.setType(rs1.getString("itemType"));
          oib.setBrand(rs1.getString("itemBrand"));
          itemList.add(oib);
        }
        stmt1.close();
        rs1.close();
        bo.setOrderItems(itemList);
        billedOrderList.add(bo);
      }
      billedOrderhm.put(username,billedOrderList);
      rs.close();
      conn.close();
      return billedOrderhm;
    }catch(Exception e){
      e.printStackTrace();
    }
    return billedOrderhm;
  }

  public int insertBilledOrder(BillingOrderBean bo){
    int orderNum = 0;
    try{
      conn = getConnection();
      String insertCustomerOrderQuery = "insert into customerorders (userName, fullname, email, totalBill, creditCardNo, cvv, address1, address2, zipcode, orderDate, deliveryDate) values (?,?,?,?,?,?,?,?,?,?,?);";
      PreparedStatement ps = conn.prepareStatement(insertCustomerOrderQuery);
      ps.setString(1,bo.getUsername());
      ps.setString(2,bo.getFullname());
      ps.setString(3,bo.getEmail());
      ps.setDouble(4,bo.getTotal());
      ps.setString(5,bo.getCreditcardNo());
      ps.setInt(6,bo.getCvv());
      ps.setString(7,bo.getAddress1());
      ps.setString(8,bo.getAddress2());
      ps.setString(9,bo.getZipcode());
      ps.setString(10,bo.getOrderDate());
      ps.setString(11,bo.getDeliveryDate());
      ps.execute();

      // get orderNum for the last inserted order
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("select orderNum from customerorders where userName = '"+bo.getUsername()+"' order by orderNum desc limit 1;");

      while(rs.next()){
        orderNum = rs.getInt("orderNum");
      }
      rs.close();
      //insert order items
      for(OrderItemBean oi : bo.getOrderItems()){
        String insertOrderItemQuery = "insert into orderitem (orderNum, itemId, itemName,itemPrice, itemType, itemBrand) values (?,?,?,?,?,?);";
        PreparedStatement ps1 = conn.prepareStatement(insertOrderItemQuery);
        ps1.setInt(1,orderNum);
        ps1.setInt(2,oi.getId());
        ps1.setString(3,oi.getName());
        ps1.setDouble(4,oi.getPrice());
        ps1.setString(5,oi.getType());
        ps1.setString(6,oi.getBrand());
        ps1.execute();

        //Decrement quantity of each orderitem in the orderitem

        //check whether the current order item is Product or accessory
        if(oi.getId()<100){
          //get the quantity of the orderItem from Product table
          Statement stmt1 = conn.createStatement();
          ResultSet rs1 = stmt1.executeQuery("select productQuantity from products where productId = '"+oi.getId()+"';");
          int quantity = 0;
          while(rs1.next()){
            quantity = rs1.getInt("productQuantity");
          }

          System.out.print("the quantity of product is"+ quantity);
          //Update the product quantity in the product table
          String updateOrderQuery = "update products set productQuantity = ? where productId = ?;";
          PreparedStatement ps3 = conn.prepareStatement(updateOrderQuery);
          ps3.setInt(1,(quantity-1));
          ps3.setInt(2,oi.getId());
          ps3.execute();

        }
        else{
          //get the quantity of the orderItem from Accessory table
          Statement stmt1 = conn.createStatement();
          ResultSet rs1 = stmt1.executeQuery("select accessoryQuantity from accessories where accessoryId = '"+oi.getId()+"';");
          int quantity = 0;
          while(rs1.next()){
            quantity = rs1.getInt("accessoryQuantity");
          }

          //Update the accessory quantity in the accessories table
          String updateOrderQuery = "update accessories set accessoryQuantity = ? where accessoryId = ?;";
          PreparedStatement ps3 = conn.prepareStatement(updateOrderQuery);
          ps3.setInt(1,(quantity-1));
          ps3.setInt(2,oi.getId());
          ps3.execute();
        }

      }
      conn.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
    return orderNum;
  }

  public void deleteBilledOrder(String userName, int orderNum){

    try{
      conn = getConnection();
      String deleteCustomerOrderQuery = "delete from customerorders where userName = ? and orderNum = ?;";
      //check order items in deleted customer order.
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("select itemId from orderitem where orderNum = '"+orderNum+"';");
      int[] items = new int[50];
      int i=0;

      while(rs.next()){
        items[i] = rs.getInt("itemId");
        i++;
      }
      String deleteCustomerOrderItemsQuery = "delete from orderitem where orderNum = ?;";
      PreparedStatement ps = conn.prepareStatement(deleteCustomerOrderQuery);
      ps.setString(1,userName);
      ps.setInt(2,orderNum);
      ps.execute();

      PreparedStatement ps1 = conn.prepareStatement(deleteCustomerOrderItemsQuery);
      ps1.setInt(1,orderNum);
      ps1.execute();

      //increase the count of deleted order items in the product and accessory table
      //check whether the current order item is Product or accessory
      for(int item : items){
        if(item<100){
          //get the quantity of the orderItem from Product table
          Statement stmt1 = conn.createStatement();
          ResultSet rs1 = stmt1.executeQuery("select productQuantity from products where productId = '"+item+"';");
          int quantity = 0;
          while(rs1.next()){
            quantity = rs1.getInt("productQuantity");
          }

          System.out.print("the quantity of product is"+ quantity);
          //Update the product quantity in the product table
          String updateOrderQuery = "update products set productQuantity = ? where productId = ?;";
          PreparedStatement ps3 = conn.prepareStatement(updateOrderQuery);
          ps3.setInt(1,(quantity+1));
          ps3.setInt(2,item);
          ps3.execute();

        }
        else{
          //get the quantity of the orderItem from Accessory table
          Statement stmt1 = conn.createStatement();
          ResultSet rs1 = stmt1.executeQuery("select accessoryQuantity from accessories where accessoryId = '"+item+"';");
          int quantity = 0;
          while(rs1.next()){
            quantity = rs1.getInt("accessoryQuantity");
          }

          //Update the accessory quantity in the accessories table
          String updateOrderQuery = "update accessories set accessoryQuantity = ? where accessoryId = ?;";
          PreparedStatement ps3 = conn.prepareStatement(updateOrderQuery);
          ps3.setInt(1,(quantity+1));
          ps3.setInt(2,item);
          ps3.execute();
        }
    }

      conn.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }

  }

  public void insert_products_accessories(HashMap<Integer, ProductBean> productCatalog, HashMap<String, AccessoriesBean> accessoryCatalog){

    int productId = 0;
    try{
      conn = getConnection();

      Set keys = productCatalog.keySet();
      for(Object prodId : keys){
        ProductBean prod = productCatalog.get(prodId);

        String insertProductQuery = "insert into products (productName, productCost, productImage, productCategory, productRating, productBrand, productDiscount, productOnsale, productManufacturerebate, productQuantity) values (?,?,?,?,?,?,?,?,?,?);";
        PreparedStatement ps = conn.prepareStatement(insertProductQuery);
        ps.setString(1,prod.getName());
        ps.setFloat(2,prod.getCost());
        ps.setString(3,prod.getImage());
        ps.setString(4,prod.getCategory());
        ps.setString(5,prod.getRating());
        ps.setString(6,prod.getBrand());
        ps.setInt(7,prod.getDiscount());
        ps.setString(8,prod.getOnsale());
        ps.setString(9,prod.getManufacturerebate());
        ps.setInt(10,prod.getQuantity());
        ps.execute();
        ps.close();
        // get productId for the last inserted product
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select productId from products where productName = '"+prod.getName()+"' order by productId desc limit 1;");

        while(rs.next()){
          productId = rs.getInt("productId");
        }
        rs.close();

        //Insert accessory for current product
        for(String prodAcc : prod.getAccessories()){
          AccessoriesBean accessory = accessoryCatalog.get(prodAcc);
          if(prodAcc.equals(accessory.getId())){
            String insertAccessoriesQuery = "insert into accessories (accessoryId, productId, accessoryName, accessoryCost, accessoryImage, accessoryCategory, accessoryRating, accessoryBrand, accessoryDiscount, accessoryOnsale, accessoryManufacturerebate, accessoryQuantity) values (?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement ps1 = conn.prepareStatement(insertAccessoriesQuery);
            ps1.setInt(1,Integer.parseInt(accessory.getId()));
            ps1.setInt(2,productId);
            ps1.setString(3,accessory.getName());
            ps1.setFloat(4,accessory.getCost());
            ps1.setString(5,accessory.getImage());
            ps1.setString(6,accessory.getCategory());
            ps1.setString(7,accessory.getRating());
            ps1.setString(8,accessory.getBrand());
            ps1.setInt(9,accessory.getDiscount());
            ps1.setString(10,accessory.getOnsale());
            ps1.setString(11,accessory.getManufacturerebate());
            ps1.setInt(12,accessory.getQuantity());
            ps1.execute();
            ps1.close();
          }
        }
      }
      conn.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  public void dbCleanup(){

    try{
      conn = getConnection();
      String truncateProductQuery = "truncate table products;";
      String truncateAccessoryQuery = "truncate table accessories;";

      PreparedStatement ps = conn.prepareStatement(truncateProductQuery);
      ps.execute();

      PreparedStatement ps1 = conn.prepareStatement(truncateAccessoryQuery);
      ps1.execute();

      conn.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  public HashMap<Integer, ProductBean> getProducts(){

    HashMap<Integer, ProductBean> producthm = new HashMap<Integer, ProductBean>();

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

        // Retrieve orderItems for each billed order.
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
      return producthm;
    }catch(Exception e){
      e.printStackTrace();
    }
    return producthm;

  }


  public HashMap<String, AccessoriesBean> getAccessories(){

    HashMap<String, AccessoriesBean> acessoryhm = new HashMap<String, AccessoriesBean>();

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
      return acessoryhm;
    }catch(Exception e){
      e.printStackTrace();
    }
    return acessoryhm;

  }

  public void addProduct(String name,float price, String image, String category, String rating, String brand,int discount,String onsale,String manufacturerebate, int quantity){

    try{
      conn = getConnection();

      String insertProductQuery = "insert into products (productName, productCost, productImage, productCategory, productRating, productBrand, productDiscount, productOnsale, productManufacturerebate, productQuantity) values (?,?,?,?,?,?,?,?,?,?);";
      PreparedStatement ps = conn.prepareStatement(insertProductQuery);
      ps.setString(1,name);
      ps.setFloat(2,price);
      ps.setString(3,image);
      ps.setString(4,category);
      ps.setString(5,rating);
      ps.setString(6,brand);
      ps.setInt(7,discount);
      ps.setString(8,onsale);
      ps.setString(9,manufacturerebate);
      ps.setInt(10,quantity);
      ps.execute();
      ps.close();

      conn.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }


  public void updateProduct(int prodId,String pname,float pprice,String pcategory,String pbrand,int pdiscount){
    try{
      conn = getConnection();

      String updateProductQuery = "update products set productName = ?, productCost = ?, productCategory = ?,  productBrand = ?, productDiscount = ? where productId = ?;";
      PreparedStatement ps = conn.prepareStatement(updateProductQuery);
      ps.setString(1,pname);
      ps.setFloat(2,pprice);
      ps.setString(3,pcategory);
      ps.setString(4,pbrand);
      ps.setInt(5,pdiscount);
      ps.setInt(6,prodId);
      ps.execute();
      ps.close();

      conn.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }


  public void deleteProduct(int prodId){

    try{
      conn = getConnection();

      String deleteProductQuery = "delete from products where productId = ?;";
      PreparedStatement ps = conn.prepareStatement(deleteProductQuery);
      ps.setInt(1,prodId);
      ps.execute();

      conn.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }

  }

  public HashMap<String, ArrayList<BillingOrderBean>> getBilledOrders(){

    HashMap<String, ArrayList<BillingOrderBean>> billedOrderhm = new HashMap<String, ArrayList<BillingOrderBean>>();

    try{
      conn = getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("select * from customerorders ");
      ArrayList<BillingOrderBean> billedOrderList = new ArrayList<BillingOrderBean>();

      while(rs.next()){
        bo = new BillingOrderBean();
        bo.setOrderNum(rs.getInt("orderNum"));
        bo.setUsername(rs.getString("userName"));
        bo.setFullname(rs.getString("fullName"));
        bo.setEmail(rs.getString("email"));
        bo.setTotal(rs.getDouble("totalBill"));
        bo.setCreditcardNo(rs.getString("creditCardNo"));
        bo.setCvv(rs.getInt("cvv"));
        bo.setAddress1(rs.getString("address1"));
        bo.setAddress2(rs.getString("address2"));
        bo.setZipcode(rs.getString("zipcode"));
        bo.setOrderDate(rs.getString("orderDate"));
        bo.setDeliveryDate(rs.getString("deliveryDate"));

        // Retrieve orderItems for each billed order.
        Statement stmt1 = conn.createStatement();
        ResultSet rs1 = stmt1.executeQuery("select * from orderitem oi where oi.orderNum = '"+ bo.getOrderNum()+"'");
        ArrayList<OrderItemBean> itemList = new ArrayList<OrderItemBean>();
        while(rs1.next()){
          OrderItemBean oib = new OrderItemBean();
          oib.setId(rs1.getInt("itemId"));
          oib.setName(rs1.getString("itemName"));
          oib.setPrice(rs1.getDouble("itemPrice"));
          oib.setType(rs1.getString("itemType"));
          oib.setBrand(rs1.getString("itemBrand"));
          itemList.add(oib);
        }
        stmt1.close();
        rs1.close();
        bo.setOrderItems(itemList);
        billedOrderList.add(bo);

      }
      billedOrderhm.put(bo.getUsername(),billedOrderList);
      rs.close();
      conn.close();
      return billedOrderhm;
    }catch(Exception e){
      e.printStackTrace();
    }
    return billedOrderhm;
  }

  public void updateOrder(int orderNum, String userName, String email, String zipcode, String address1, String address2){
    try{
      conn = getConnection();

      String updateOrderQuery = "update customerorders set email = ?,  zipcode = ?, address1 = ?, address2 = ? where orderNum = ?;";
      PreparedStatement ps = conn.prepareStatement(updateOrderQuery);
      ps.setString(1,email);
      ps.setString(2,zipcode);
      ps.setString(3,address1);
      ps.setString(4,address2);
      ps.setInt(5,orderNum);
      ps.execute();
      ps.close();

      conn.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  public ArrayList<SalesReportBean> getSalesReport(){
    ArrayList<SalesReportBean> sales = new ArrayList<SalesReportBean>();

    try{
      conn = getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT itemName,itemPrice,count(itemName) as noOfItems,(itemPrice * count(itemName)) as totalSales FROM orderitem group by itemId;");

      while(rs.next()){
        SalesReportBean sb = new SalesReportBean();
        sb.setItemName(rs.getString("itemName"));
        sb.setItemPrice(rs.getFloat("itemPrice"));
        sb.setItemCount(rs.getInt("noOfItems"));
        sb.setTotalSales(rs.getFloat("totalSales"));
        sales.add(sb);
      }
      rs.close();
      conn.close();
      return sales;

    }catch(Exception e){
      e.printStackTrace();
    }
    return sales;

  }

  public ArrayList<SalesReportBean> getDaywiseSalesReport(){
    ArrayList<SalesReportBean> sales = new ArrayList<SalesReportBean>();

    try{
      conn = getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT orderDate,sum(totalBill) as totalSales FROM customerorders group by orderDate;");

      while(rs.next()){
        SalesReportBean sb = new SalesReportBean();
        sb.setOrderDate(rs.getString("orderDate"));
        sb.setDaySales(rs.getFloat("totalSales"));
        sales.add(sb);
      }
      rs.close();
      conn.close();
      return sales;

    }catch(Exception e){
      e.printStackTrace();
    }
    return sales;

  }

}
