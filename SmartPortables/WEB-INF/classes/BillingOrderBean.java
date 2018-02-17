import java.util.*;

public class BillingOrderBean implements java.io.Serializable {

	private String username;
	private String fullname;
	private String email;
	private String creditcardNo;
	private int cvv;
	private String address1;
	private String address2;
	private String zipcode;
	private String orderDate;
	private String deliveryDate;
	private int orderNum;
	private double total;
	private ArrayList<OrderItemBean> orderItems;

	public BillingOrderBean(){
		
	}
	public BillingOrderBean(String username, String fullname, String email, double total, String creditcardNo, int cvv, String address1, String address2, String zipcode, String orderDate,String deliveryDate,  ArrayList<OrderItemBean> orderItems){
	//	this.orderNum = orderNum;
		this.username = username;
		this.fullname = fullname;
		this.email = email;
		this.total = total;
		this.creditcardNo = creditcardNo;
		this.cvv = cvv;
		this.address1 = address1;
		this.address2 = address2;
		this.zipcode = zipcode;
		this.orderDate = orderDate;
		this.deliveryDate = deliveryDate;
		this.orderItems = orderItems;
	}

	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCreditcardNo() {
		return creditcardNo;
	}
	public void setCreditcardNo(String creditcardNo) {
		this.creditcardNo = creditcardNo;
	}
	public int getCvv() {
		return cvv;
	}
	public void setCvv(int cvv) {
		this.cvv = cvv;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}

	public ArrayList<OrderItemBean> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(ArrayList<OrderItemBean> orderItems) {
		this.orderItems = orderItems;
	}

}
