import java.util.HashMap;

public class UserHashMap {
	public static HashMap<String, UserBean> Customer = new HashMap<String, UserBean>();
	public static HashMap<String, UserBean> StoreManager = new HashMap<String, UserBean>();
	public static HashMap<String, UserBean> SalesMan = new HashMap<String, UserBean>();

	public UserHashMap(){
		if(Customer.isEmpty()){
			UserBean user = new UserBean("customer","pass1","Customer");
			Customer.put("Customer",user);
		}
		if(StoreManager.isEmpty()){
			UserBean user = new UserBean("storemanager","pass2","StoreManager");
			StoreManager.put("StoreManager",user);
		}
		if(SalesMan.isEmpty()){
			UserBean user = new UserBean("salesman","pass3","SalesMan");
			SalesMan.put("SalesMan",user);
		}
	}

}
