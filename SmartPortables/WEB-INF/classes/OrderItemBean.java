public class OrderItemBean implements java.io.Serializable {
  private int id;
  private String name;
  private double price;
  private String type;
  private String brand;

  public OrderItemBean(){

  }

  public OrderItemBean(int id, String name, double price, String type, String brand){
    this.id = id;
    this.name = name;
    this.price = price;
    this.type = type;
    this.brand = brand;
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

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }
}
