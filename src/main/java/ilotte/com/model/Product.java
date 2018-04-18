package ilotte.com.model;

import java.math.BigDecimal;

public class Product {

	private int id;
	private String name;
	private String brand;
	private String productNo;
	private BigDecimal price;
	private String size;
	private String color;
	private String unitWeight;
	private int unitStock;
	private String description;
	private Category category;

	public Product() {
	}

	public Product(int id, String name, String brand, String productNo, BigDecimal price, String size, String color,
			String unitWeight, int unitStock, String description, Category category) {
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.productNo = productNo;
		this.price = price;
		this.size = size;
		this.color = color;
		this.unitWeight = unitWeight;
		this.unitStock = unitStock;
		this.description = description;
		this.unitStock = unitStock;
		this.category = category;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getUnitWeight() {
		return unitWeight;
	}

	public void setUnitWeight(String unitWeight) {
		this.unitWeight = unitWeight;
	}

	public int getUnitStock() {
		return unitStock;
	}

	public void setUnitStock(int unitStock) {
		this.unitStock = unitStock;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

}
