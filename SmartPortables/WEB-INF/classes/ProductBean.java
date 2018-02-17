import java.util.*;

public class ProductBean implements java.io.Serializable{
	private int id;
	private String name;
	private float cost;
	private String image;
	private String category;
	private String rating;
	private String brand;
	private int discount;
	private String onsale;
	private String manufacturerebate;
	private int quantity;
	private ArrayList<String> accessories = new ArrayList();

	public ProductBean(){

	}

	public ProductBean(int id, String name, float cost, String image,
	String category, String rating, String brand, int discount) {
		this.id = id;
		this.name = name;
		this.cost = cost;
		this.image = image;
		this.category = category;
		this.rating = rating;
		this.brand = brand;
		this.discount = discount;
	}

	public ProductBean(int id, String name, float cost, String image,
	String category, String rating, String brand, int discount, String onsale, String manufacturerebate, int quantity) {
		this.id = id;
		this.name = name;
		this.cost = cost;
		this.image = image;
		this.category = category;
		this.rating = rating;
		this.brand = brand;
		this.discount = discount;
		this.onsale = onsale;
		this.manufacturerebate = manufacturerebate;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getCost() {
		return cost;
	}
	public void setCost(float cost) {
		this.cost = cost;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public String getOnsale() {
		return onsale;
	}

	public void setOnsale(String onsale) {
		this.onsale = onsale;
	}
	public String getManufacturerebate() {
		return manufacturerebate;
	}
	public void setManufacturerebate(String manufacturerebate) {
		this.manufacturerebate = manufacturerebate;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public ArrayList<String> getAccessories() {
		return accessories;
	}
	public void setAccessories(ArrayList<String> accessories) {
		this.accessories = accessories;
	}

}
