import java.sql.*;
import java.util.*;
import java.io.*;

public class DealMatches implements java.io.Serializable
{

  HashMap<Integer, ProductBean> producthm;
  HashMap<Integer, ProductBean> selectedProductHm;

  ArrayList<String> tweets;
  ArrayList<String> selectedTweets;

  public void readTweets() {

    BufferedReader br;
    try {
      String deal=null;
      tweets = new ArrayList<String>();
      //Open DealMatches text file using bufferedReader class
      br = new BufferedReader(new FileReader(
      "C:\\apache-tomcat-7.0.34\\webapps\\SmartPortables\\DealMatches.txt"));

      //Read all deals from the DealMatches text file
      while ((deal = br.readLine()) != null) {
        //add each deal line from text file to tweets ArrayList
        tweets.add(deal);
      }
      br.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public HashMap<Integer, ProductBean> getSelectedProducts()
  {
    MySqlDataStoreUtilities sqdb = new MySqlDataStoreUtilities();
    producthm = sqdb.getProducts();
    selectedProductHm = new HashMap<Integer, ProductBean>();
    selectedTweets = new ArrayList<String>();
    String deal=null;

    try
    {
      for(Map.Entry<Integer, ProductBean> entry: producthm.entrySet())
      {
        if(selectedProductHm.size()<2 && !selectedProductHm.containsKey(entry.getKey()))
        {
        //  System.out.println("in deals match inside if "+selectedProductHm.size());
          for(String deals : tweets){
            ProductBean prod;
            prod = producthm.get(entry.getKey());
            if(deals.contains(prod.getName())){
              selectedTweets.add(deals);
              selectedProductHm.put(entry.getKey(), entry.getValue());
              break;
            }
          }
        }
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

    return selectedProductHm;
  }

  public ArrayList<String> getSelectedTweets() {
    return selectedTweets;
  }

}
