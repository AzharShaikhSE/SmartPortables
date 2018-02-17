import java.util.Date;

public class ReviewAnalytics implements java.io.Serializable{

	String productModelName;
	String retailerCity;
	//int reviewRating;


	public ReviewAnalytics(){}

	public ReviewAnalytics(String productModelName,String retailerCity){
					this.productModelName = productModelName;
					this.retailerCity = retailerCity;
	}

	public String getproductModelName(){
		return productModelName;
	}

  public void setproductModelName(String productModelName){
    this.productModelName=productModelName;
  }

  public String getretailerCity()
  {
    return retailerCity;
  }

  public void setretailerCity(String retailerCity){
    this.retailerCity=retailerCity;
  }

}
