import java.util.Date;

public class ReviewBean implements java.io.Serializable{

	String productModelName;
	String productCategory;
  double productPrice;
	String retailerName;
  int retailerZip;
	String retailerCity;
	String retailerState;
	String productSale;
	String manufactureName;
	String manufactureRebate;
	String userName;
  int userAge;
	String userGender;
	String userOccupation;
	Date reviewDate;
	String reviewText;
	double reviewRating;
	int reviewCount=0;

	public ReviewBean(){}

	public ReviewBean(String productModelName,String retailerCity){
					this.productModelName = productModelName;
					this.retailerCity = retailerCity;
	}

	public String getproductModelName(){
		return productModelName;
	}

  public void setproductModelName(String productModelName){
    this.productModelName=productModelName;
  }

  public String getproductCategory(){
    return productCategory;
  }

  public void setproductCategory(String productCategory){
    this.productCategory=productCategory;
  }

  public double getproductPrice(){
  	return productPrice;
  }

  public void setproductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

  public String getretailerName(){
    return retailerName;
  }

  public void setretailerName(String retailerName){
    this.retailerName=retailerName;
  }

  public int getretailerZip(){
    return retailerZip;
  }

  public void setretailerZip(int retailerZip) {
		this.retailerZip = retailerZip;
	}

  public String getretailerCity()
  {
    return retailerCity;
  }

  public void setretailerCity(String retailerCity){
    this.retailerCity=retailerCity;
  }

  public String getretailerState(){
    return retailerState;
  }

  public void setretailerState(String retailerState)
  {
    this.retailerState=retailerState;
  }

  public String getproductSale(){
    return productSale;
  }

  public void setproductSale(String productSale)
  {
    this.productSale=productSale;
  }

  public String getmanufactureName(){
    return manufactureName;
  }

  public void setmanufactureName(String manufactureName)
  {
    this.manufactureName=manufactureName;
  }

  public String getmanufactureRebate(){
    return manufactureRebate;
  }

  public void setmanufactureRebate(String manufactureRebate)
  {
    this.manufactureRebate=manufactureRebate;
  }

  public String getuserName(){
    return userName;
  }

  public void setuserName(String userName) {
		this.userName = userName;
	}

  public int getuserAge(){
    return userAge;
  }

  public void setuserAge(int userAge) {
		this.userAge = userAge;
	}

  public String getuserGender(){
  	return userGender;
  }

  public void setuserGender(String userGender) {
		this.userGender = userGender;
	}

  public String getuserOccupation() {
    return userOccupation;
  }

  public void setuserOccupation(String userOccupation) {
		this.userOccupation = userOccupation;
	}

  public double getreviewRating() {
    return reviewRating;
  }

  public void setreviewRating(double reviewRating) {
		this.reviewRating = reviewRating;
	}

	public int getreviewCount() {
    return reviewCount;
  }

  public void setreviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

  public Date getreviewDate() {
    return reviewDate;
  }

  public void setreviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}

  public String getreviewText() {
    return reviewText;
  }

	public void setreviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	public String toString() {
		return "ReviewBean [productModelName=" + productModelName + ", productCategory=" + productCategory + ", retailerName="
				+ retailerName + ", retailerCity=" + retailerCity + ", retailerState=" + retailerState
				+ ", productSale=" + productSale + ", manufactureName=" + manufactureName + ", manufactureRebate="
				+ manufactureRebate + ", userName=" + userName + ", userGender=" + userGender + ", userOccupation=" + userOccupation
				+ ", reviewDate=" + reviewDate + ", reviewText=" + reviewText + ", productPrice=" + productPrice
				+ ", retailerZip=" + retailerZip + ", userAge=" + userAge + ", reviewRating=" + reviewRating + "]";
	}
}
