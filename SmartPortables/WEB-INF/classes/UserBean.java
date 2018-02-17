public class UserBean implements java.io.Serializable{
	private String name;
	private String password;
	private String roleType;

	public UserBean(){
		
	}
	public UserBean(String name, String password, String roleType) {
		this.name=name;
		this.password=password;
		this.roleType=roleType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
}
