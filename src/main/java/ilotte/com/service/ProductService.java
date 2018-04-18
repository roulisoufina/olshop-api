package ilotte.com.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ilotte.com.model.Category;
import ilotte.com.model.Product;

@Component
public class ProductService {

	private static List<Category> categories = new ArrayList<>();
	private static List<Product> products = new ArrayList<>();

	static {
		// Initialize Data
		Category category1 = new Category(1, "Fashion Wanita", "Fashion Wanita Desc");
		Category category2 = new Category(2, "Fashion Pria", "Fashion Pria Desc");
		Category category3 = new Category(3, "Kecantikan & Kesehatan", "Kecantikan & Kesehatan Desc");
		Category category4 = new Category(4, "Elektronik & Rumah", "Elektronik & Rumah Desc");
		Category category5 = new Category(5, "Olahraga & Hobi", "Olahraga & Hobi Desc");
		categories.add(category1);
		categories.add(category2);
		categories.add(category3);
		categories.add(category4);
		categories.add(category5);

		Product product1 = new Product(1, "Product 1", "EPRISE", "000000111601", new BigDecimal(199900), "M", null,
				"o.1gr", 10, "description", category1);
		Product product2 = new Product(2, "Product 1", "EPRISE", "000000111601", new BigDecimal(199900), "L", null,
				"o.1gr", 14, "description", category1);
		Product product3 = new Product(3, "Product 2", "Magnificients", "000000229317", new BigDecimal(99900), "S",
				"white", null, 17, "description", category1);
		Product product4 = new Product(4, "Product 3", "Osella", "000000268865", new BigDecimal(99950), "L", "brown",
				null, 10, "description", category2);
		Product product5 = new Product(5, "Product 3", "Osella", "000000268865", new BigDecimal(99950), "XXL", "brown",
				null, 20, "description", category2);
		Product product6 = new Product(6, "Product 4", "Chinos", "000000111606", new BigDecimal(199900), "28", null,
				null, 10, "description", category2);
		Product product7 = new Product(7, "Product 5", "Osella", "000000001270", new BigDecimal(199900), "29", null,
				null, 15, "description", category2);
		Product product8 = new Product(8, "Product 5", "Osella", "000000001270", new BigDecimal(199900), "30", null,
				null, 18, "description", category2);
		Product product9 = new Product(9, "Product 6", "Kiehls", "000000111608", new BigDecimal(500000), null, null,
				null, 30, "description", category3);
		Product product10 = new Product(10, "Product 7", "Bourjois", "000000046376", new BigDecimal(198000), null, null,
				null, 20, "description", category3);
		Product product11 = new Product(11, "Product 8", "Vivo", "000000225852", new BigDecimal(4699000), null, null,
				null, 10, "description", category4);
		Product product12 = new Product(12, "Product 9", "Apple", "000000219296", new BigDecimal(1799900), null, null,
				null, 15, "description", category4);
		Product product13 = new Product(13, "Product 10", "NIKE", "000000044182", new BigDecimal(1210300), "12", null,
				null, 15, "description", category5);
		Product product14 = new Product(14, "Product 11", "NIKE", "000000011656", new BigDecimal(549000), "L", null,
				null, 25, "description", category5);
		Product product15 = new Product(15, "Product 11", "NIKE", "000000011656", new BigDecimal(549000), "XL", null,
				null, 1, "description", category5);
		products.add(product1);
		products.add(product2);
		products.add(product3);
		products.add(product4);
		products.add(product5);
		products.add(product6);
		products.add(product7);
		products.add(product8);
		products.add(product9);
		products.add(product10);
		products.add(product11);
		products.add(product12);
		products.add(product13);
		products.add(product14);
		products.add(product15);
	}
	
	public List<Product> retrieveAllProducts(){
		return products;
	};

}
