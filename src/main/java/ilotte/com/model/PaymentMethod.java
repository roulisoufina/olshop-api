package ilotte.com.model;

public class PaymentMethod {

	private int id;
	private String bank;
	private String name;
	private PaymentType type;
	private boolean status;

	public PaymentMethod() {
	}
	
	public PaymentMethod(int id, String name, String bank, PaymentType type, boolean status) {
		this.id = id;
		this.name = name;
		this.bank = bank;
		this.type = type;
		this.status = status;
	}

	public enum PaymentType {
		CREDITCARD, TRANSFER;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PaymentType getType() {
		return type;
	}

	public void setType(PaymentType type) {
		this.type = type;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
