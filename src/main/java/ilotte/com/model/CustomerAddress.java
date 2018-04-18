package ilotte.com.model;

public class CustomerAddress {

	private int id;
	private int userId;
	private String name;
	private String label;
	private String address;
	private String province;
	private String city;
	private String email;
	private String contactNo;

	public CustomerAddress() {
		// TODO Auto-generated constructor stub
	}

	public CustomerAddress(int id, int userId, String name, String label, String address, String province, String city,
			String email, String contactNo) {
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.label = label;
		this.address = address;
		this.province = province;
		this.city = city;
		this.email = email;
		this.contactNo = contactNo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

}
