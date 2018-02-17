import java.util.*;


public class AccessoriesBean implements java.io.Serializable{
	private String id;
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

	public AccessoriesBean(){

	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
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

}
