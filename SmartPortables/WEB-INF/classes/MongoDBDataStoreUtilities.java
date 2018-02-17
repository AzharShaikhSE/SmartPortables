import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.util.*;

public class MongoDBDataStoreUtilities
{
  static DBCollection customerReviews;
  public static DB getConnection()
  {
    MongoClient mongo;
    mongo = new MongoClient("localhost", 27017);
    DB db = mongo.getDB("smartportables_db");
    return db;
  }

  public void insertReview(DB db, ReviewBean review) {
    customerReviews = db.getCollection("product_reviews");
    BasicDBObject document = new BasicDBObject("title", "MongoDB").append("ProductModelName", review.getproductModelName())
    .append("ProductCategory", review.getproductCategory()).append("productPrice", review.getproductPrice())
    .append("RetailerName", review.getretailerName()).append("RetailerZip", review.getretailerZip())
    .append("RetailerCity", review.getretailerCity()).append("RetailerState", review.getretailerState())
    .append("ProductOnSale", review.getproductSale()).append("ManufactureName", review.getmanufactureName())
    .append("ManufactureRebate", review.getmanufactureRebate()).append("UserID", review.getuserName())
    .append("UserAge", review.getuserAge()).append("UserGender", review.getuserGender())
    .append("UserOccupation", review.getuserOccupation()).append("ReviewRating", review.getreviewRating())
    .append("ReviewDate", review.getreviewDate()).append("ReviewText", review.getreviewText());
    customerReviews.insert(document);
  }

  public static HashMap<String, ArrayList<ReviewBean>> getReviews()
  {
    DB db = getConnection();
    customerReviews = db.getCollection("product_reviews");
    HashMap<String, ArrayList<ReviewBean>> reviewhm = new HashMap<String, ArrayList<ReviewBean>>();
    DBCursor cursor = customerReviews.find();
    while (cursor.hasNext())
    {
      BasicDBObject obj = (BasicDBObject) cursor.next();
      if(! reviewhm.containsKey(obj.getString("ProductModelName")))
      {
        ArrayList<ReviewBean> arr = new ArrayList<ReviewBean>();
        reviewhm.put(obj.getString("ProductModelName"), arr);
      }
      ArrayList<ReviewBean> listReview = reviewhm.get(obj.getString("ProductModelName"));
      ReviewBean review =new ReviewBean();
      review.setmanufactureName(obj.get("ManufactureName").toString());
      review.setmanufactureRebate(obj.get("ManufactureRebate").toString());
      review.setproductCategory(obj.get("ProductCategory").toString());
      review.setproductPrice(Double.parseDouble(obj.get("productPrice").toString()));
      review.setproductSale(obj.get("ProductOnSale").toString());
      review.setretailerCity(obj.get("RetailerCity").toString());
      review.setretailerName(obj.get("RetailerName").toString());
      review.setretailerState(obj.get("RetailerState").toString());
      review.setretailerZip(Integer.parseInt(obj.get("RetailerZip").toString()));
      review.setreviewDate(new Date(obj.get("ReviewDate").toString()));
      review.setreviewRating(Double.parseDouble(obj.get("ReviewRating").toString()));
      review.setreviewText(obj.get("ReviewText").toString());
      review.setuserAge(Integer.parseInt(obj.get("UserAge").toString()));
      review.setuserGender(obj.get("UserGender").toString());
      review.setuserOccupation(obj.get("UserOccupation").toString());
      review.setproductModelName(obj.get("ProductModelName").toString());
      review.setuserName(obj.get("UserID").toString());
      listReview.add(review);
      //reviewhm.put(obj.getString("productModelName"), listReview);
    }
    return reviewhm;
  }

  public static ArrayList<ReviewBean> getTop5LikedProducts()
  {
    DB db = getConnection();
    customerReviews = db.getCollection("product_reviews");
    ArrayList<ReviewBean> reviewList = new ArrayList<ReviewBean>();

    DBObject groupFields = new BasicDBObject("_id",0);
    groupFields.put("_id", "$ProductModelName");
    groupFields.put("ReviewRating", new BasicDBObject("$avg", "$ReviewRating"));
    groupFields.put("ProductCategory", new BasicDBObject("$first", "$ProductCategory"));
    groupFields.put("productPrice", new BasicDBObject("$first", "$productPrice"));
    DBObject group = new BasicDBObject("$group", groupFields);

    DBObject projectFields = new BasicDBObject("_id",0);
    projectFields.put("ProductModelName", "$_id");
    projectFields.put("ReviewRating", "$ReviewRating");
    projectFields.put("ProductCategory", "$ProductCategory");
    projectFields.put("productPrice", "$productPrice");
    DBObject project = new BasicDBObject("$project", projectFields);

    DBObject sort = new BasicDBObject();
    sort.put("ReviewRating", -1);

    DBObject limit = new BasicDBObject("$limit", 5);
    DBObject orderby = new BasicDBObject("$sort",sort);

    AggregationOutput aggregate = customerReviews.aggregate(group, project, orderby, limit);

    for(DBObject output : aggregate.results()){

      BasicDBObject dbobj = (BasicDBObject) output;
      ReviewBean review =new ReviewBean();
      review.setproductModelName(dbobj.get("ProductModelName").toString());
      review.setreviewRating(Double.parseDouble(dbobj.get("ReviewRating").toString()));
      review.setproductCategory(dbobj.get("ProductCategory").toString());
      review.setproductPrice(Double.parseDouble(dbobj.get("productPrice").toString()));
      reviewList.add(review);
    }

    return reviewList;

  }

  public static ArrayList<ReviewBean> getTop5Zipcodes()
  {
    DB db = getConnection();
    customerReviews = db.getCollection("product_reviews");
    ArrayList<ReviewBean> reviewList = new ArrayList<ReviewBean>();

    DBObject groupFields = new BasicDBObject("_id",0);
    groupFields.put("_id", "$RetailerZip");
    groupFields.put("Review Count", new BasicDBObject("$sum", 1));
    DBObject group = new BasicDBObject("$group", groupFields);

    DBObject projectFields = new BasicDBObject("_id",0);
    projectFields.put("RetailerZip", "$_id");
    projectFields.put("Review Count", "$Review Count");
    DBObject project = new BasicDBObject("$project", projectFields);

    DBObject sort = new BasicDBObject();
    sort.put("Review Count", -1);

    DBObject limit = new BasicDBObject("$limit", 5);
    DBObject orderby = new BasicDBObject("$sort",sort);

    AggregationOutput aggregate = customerReviews.aggregate(group, project, orderby, limit);

    for(DBObject output : aggregate.results()){

      BasicDBObject dbobj = (BasicDBObject) output;
      ReviewBean review =new ReviewBean();
      review.setretailerZip(Integer.parseInt(dbobj.get("RetailerZip").toString()));
      review.setreviewCount(Integer.parseInt(dbobj.get("Review Count").toString()));
      reviewList.add(review);
    }

    return reviewList;

  }

  public static ArrayList<ReviewBean> getTop5SoldProducts()
  {
    DB db = getConnection();
    customerReviews = db.getCollection("product_reviews");
    ArrayList<ReviewBean> reviewList = new ArrayList<ReviewBean>();

    DBObject groupFields = new BasicDBObject("_id",0);
    groupFields.put("_id", "$ProductModelName");
    groupFields.put("Review Count", new BasicDBObject("$sum", 1));
    groupFields.put("ReviewRating", new BasicDBObject("$max", "$ReviewRating"));
    groupFields.put("ProductCategory", new BasicDBObject("$first", "$ProductCategory"));
    groupFields.put("productPrice", new BasicDBObject("$first", "$productPrice"));
    DBObject group = new BasicDBObject("$group", groupFields);

    DBObject projectFields = new BasicDBObject("_id",0);
    projectFields.put("ProductModelName", "$_id");
    projectFields.put("Review Count", "$Review Count");
    projectFields.put("ReviewRating", "$ReviewRating");
    projectFields.put("ProductCategory", "$ProductCategory");
    projectFields.put("productPrice", "$productPrice");
    DBObject project = new BasicDBObject("$project", projectFields);

    DBObject sort = new BasicDBObject();
    sort.put("Review Count", -1);

    DBObject limit = new BasicDBObject("$limit", 5);
    DBObject orderby = new BasicDBObject("$sort",sort);

    AggregationOutput aggregate = customerReviews.aggregate(group, project, orderby, limit);

    for(DBObject output : aggregate.results()){

      BasicDBObject dbobj = (BasicDBObject) output;
      ReviewBean review =new ReviewBean();
      review.setproductModelName(dbobj.get("ProductModelName").toString());
      review.setreviewRating(Double.parseDouble(dbobj.get("ReviewRating").toString()));
      review.setproductCategory(dbobj.get("ProductCategory").toString());
      review.setproductPrice(Double.parseDouble(dbobj.get("productPrice").toString()));
      review.setreviewCount(Integer.parseInt(dbobj.get("Review Count").toString()));
      reviewList.add(review);
    }

    return reviewList;

  }

  public static ArrayList<ReviewBean> getTop3PerCity()
  {
    DB db = getConnection();
    customerReviews = db.getCollection("product_reviews");
    ArrayList<ReviewBean> reviewList = new ArrayList<ReviewBean>();

    Map<String, Object> dbobjMap = new HashMap<String, Object>();
    dbobjMap.put("RetailerCity","$RetailerCity");
    dbobjMap.put("ProductModelName","$ProductModelName");

    DBObject groupFields = new BasicDBObject("_id", new BasicDBObject(dbobjMap));
    groupFields.put("Count", new BasicDBObject("$sum", 1));

    DBObject group = new BasicDBObject("$group", groupFields);

    DBObject projectFields = new BasicDBObject("_id",0);
    projectFields.put("RetailerCity", "$_id");
    projectFields.put("ProductModelName", "$ProductModelName");
    projectFields.put("Review Count", "$Count");
    DBObject project = new BasicDBObject("$project", projectFields);

    DBObject sort = new BasicDBObject();
    sort.put("Review Count", -1);

    //    DBObject limit = new BasicDBObject("$limit", 3);
    DBObject orderby = new BasicDBObject("$sort",sort);

    AggregationOutput aggregate = customerReviews.aggregate(group, project, orderby);

    for(DBObject output : aggregate.results()){

      BasicDBObject dbobj = (BasicDBObject) output;
      ReviewBean review =new ReviewBean();

      review.setretailerCity(dbobj.getString("RetailerCity"));
      //review.setproductModelName(dbobj.get("ProductModelName").toString());
      review.setreviewCount(Integer.parseInt(dbobj.get("Review Count").toString()));
      reviewList.add(review);
    }

    return reviewList;

  }


}
