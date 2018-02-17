import java.util.*;

public class SalesReportBean implements java.io.Serializable{

	private String itemName;
	private float itemPrice;
	private int itemCount;
	private float totalSales;
	private String orderDate;
	private float daySales;

  public SalesReportBean(){

  }
  
  public SalesReportBean(String itemName, float itemPrice, int itemCount,
	float totalSales, String orderDate, float daySales) {
		this.itemName = itemName;
		this.itemPrice = itemPrice;
		this.itemCount = itemCount;
		this.totalSales = totalSales;
		this.orderDate = orderDate;
		this.daySales = daySales;
	}

  public String getItemName() {
    return itemName;
  }
  public void setItemName(String itemName) {
    this.itemName = itemName;
  }
  public float getItemPrice() {
    return itemPrice;
  }
  public void setItemPrice(float itemPrice) {
    this.itemPrice = itemPrice;
  }
  public int getItemCount() {
    return itemCount;
  }
  public void setItemCount(int itemCount) {
    this.itemCount = itemCount;
  }

  public float getTotalSales() {
    return totalSales;
  }
  public void setTotalSales(float totalSales) {
    this.totalSales = totalSales;
  }

  public String getOrderDate() {
    return orderDate;
  }
  public void setOrderDate(String orderDate) {
    this.orderDate = orderDate;
  }

  public float getDaySales() {
      return daySales;
  }
  public void setDaySales(float daySales) {
      this.daySales = daySales;
  }

  }
