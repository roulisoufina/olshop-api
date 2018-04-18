package ilotte.com.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class Cart {

	private int id;
	private int productId;
	private BigDecimal fixedPrice;
	private BigDecimal totalPrice;
	private int quantity;
	private String username;

	public Cart() {
		// TODO Auto-generated constructor stub
	}

	public Cart(int productId, BigDecimal fixedPrice, BigDecimal totalPrice, int quantity) {
		this.productId = productId;
		this.fixedPrice = fixedPrice;
		this.totalPrice = totalPrice;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public BigDecimal getFixedPrice() {
		return fixedPrice;
	}

	public void setFixedPrice(BigDecimal fixedPrice) {
		this.fixedPrice = fixedPrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
