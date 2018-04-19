package ilotte.com.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ilotte.com.model.CustomerAddress;
import ilotte.com.model.Order;
import ilotte.com.model.OrderDetail;
import ilotte.com.model.Order.OrderStatus;
import ilotte.com.model.Order.PaymentStatus;
import ilotte.com.model.PaymentMethod;
import ilotte.com.service.AddressService;
import ilotte.com.service.PaymentMethodService;
import ilotte.com.service.ProductService;
import ilotte.com.service.UserService;
import ilotte.com.model.Product;
import ilotte.com.model.User;

@RestController
public class ProcessingController {

	private static final Logger log = LoggerFactory.getLogger(ProcessingController.class);
	private List<Order> orders = new ArrayList<>();
	private List<OrderDetail> orderDetails = new ArrayList<>();

	@Autowired
	private UserService userService;
	@Autowired
	private ProductService productService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private PaymentMethodService paymentMethodService;

	@RequestMapping("/login")
	public ResponseEntity<?> login() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return ResponseEntity.status(HttpStatus.OK).body("Welcome " + auth.getName() + "!");
	}

	@RequestMapping(value = { "", "/", "/viewProduct" })
	public ResponseEntity<List<Product>> viewProduct() {
		if (productService == null) {
			productService = new ProductService();
		}
		return new ResponseEntity<List<Product>>(productService.retrieveAllProducts(), HttpStatus.OK);
	}

	@RequestMapping("/logoutuser")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		log.info("logout enter");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		return "logout " + name + " successfully";
	}

	@RequestMapping(value = "/addProductCart", method = RequestMethod.POST)
	public ResponseEntity<?> addProductCart(@RequestBody OrderDetail detail) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		log.info("login active = " + name);

		// get customer id
		int customerId = 0;

		if (userService == null) {
			userService = new UserService();
		}
		for (User user : userService.getUsers()) {
			if (user.getUsername().equalsIgnoreCase(name)) {
				customerId = user.getId();
				break;
			}
		}
		// get data product
		if (productService == null) {
			productService = new ProductService();
		}

		Product product = null;
		for (Product item : productService.retrieveAllProducts()) {
			if (item.getId() == detail.getProductId()) {
				product = item;
				break;
			}
		}

		// validate product
		if (product == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Product is not found. Please select another product");
		}

		Order order = null;
		for (Order item : orders) {
			if (item.getCustomerId() == customerId && item.getStatus().compareTo(OrderStatus.CART) == 0) {
				order = item;
				break;
			}
		}
		boolean isNewOrder = false;
		int orderId = 0;
		if (order == null) {
			order = new Order();
			orderId = orders.size() + 1;// increment id
			order.setId(orderId);
			order.setStatus(OrderStatus.CART);
			order.setCustomerId(customerId);
			isNewOrder = true;
		} else {
			orderId = order.getId();
			for (OrderDetail item : orderDetails) {
				// check if product is already in the cart
				if (item.getOrderId() == orderId && item.getProductId() == detail.getProductId()) {
					int totalQuantity = item.getQuantity() + detail.getQuantity();
					item.setQuantity(totalQuantity);
					BigDecimal totalPrice = item.getFixedPrice().multiply(new BigDecimal(totalQuantity));
					item.setTotalPrice(totalPrice);

					return ResponseEntity.status(HttpStatus.CREATED).build();
				}
			}
		}

		detail.setId(orderDetails.size() + 1); // increment id
		detail.setOrderId(orderId);
		detail.setProductNo(product.getProductNo());
		detail.setFixedPrice(product.getPrice());
		detail.setTotalPrice(product.getPrice().multiply(new BigDecimal(detail.getQuantity())));
		detail.setSize(product.getSize());
		detail.setColor(product.getColor());

		order.getOrderDetails().add(detail);
		if (isNewOrder) {
			orders.add(order);
		}
		orderDetails.add(detail);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@RequestMapping(value = "/viewCart", method = RequestMethod.GET)
	public ResponseEntity<Order> viewCart() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		log.info("login active = " + name);

		// get customer id
		int customerId = 0;
		for (User user : userService.getUsers()) {
			if (user.getUsername().equalsIgnoreCase(name)) {
				customerId = user.getId();
				break;
			}
		}

		// get data order
		Order order = null;
		for (Order item : orders) {
			if (item.getCustomerId() == customerId && item.getStatus().compareTo(OrderStatus.CART) == 0) {
				order = item;
				break;
			}
		}

		return new ResponseEntity<Order>(order, HttpStatus.OK);
	}

	@RequestMapping(value = "/modifyProductCart/{order_id}", method = RequestMethod.PUT)
	public ResponseEntity<?> modifyProductCart(@PathVariable("order_id") long orderId,
			@RequestBody OrderDetail detail) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		log.info("login active = " + name);

		// get data product in order to get price value
		Product product = null;
		for (Product item : productService.retrieveAllProducts()) {
			if (item.getId() == detail.getProductId()) {
				product = item;
				break;
			}
		}

		for (OrderDetail item : orderDetails) {
			if (item.getOrderId() == orderId && item.getProductId() == detail.getProductId()) {
				item.setQuantity(detail.getQuantity());
				item.setTotalPrice(product.getPrice().multiply(new BigDecimal(detail.getQuantity())));
			}
		}

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@RequestMapping(value = "/deleteProductCart/{order_id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteProductCart(@PathVariable("order_id") long orderId,
			@RequestBody OrderDetail detail) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		log.info("login active = " + name);

		for (Order item : orders) {
			if (item.getId() == orderId) {
				// validate status
				if (item.getStatus().compareTo(OrderStatus.CART) != 0) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Sorry you can't delete the product from cart. The order has been processed.");
				}

				// remove from list Order.orderDetails
				for (OrderDetail itemDetail : item.getOrderDetails()) {
					if (itemDetail.getProductId() == detail.getProductId()) {
						item.getOrderDetails().remove(itemDetail);
						break;
					}
				}
				break;
			}
		}
		// remove from list orderDetails
		for (OrderDetail itemDetail : orderDetails) {
			if (itemDetail.getOrderId() == orderId && itemDetail.getProductId() == detail.getProductId()) {
				orderDetails.remove(itemDetail);
				break;
			}
		}

		return new ResponseEntity<Order>(HttpStatus.OK);
	}

	@RequestMapping(value = "/submitOrder/{order_id}", method = RequestMethod.GET)
	public ResponseEntity<?> submitOrder(@PathVariable("order_id") long orderId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		log.info("submitOrder login active = " + name);

		// get customer id
		int customerId = 0;
		if (userService == null) {
			userService = new UserService();
		}
		for (User user : userService.getUsers()) {
			if (user.getUsername().equalsIgnoreCase(name)) {
				customerId = user.getId();
				break;
			}
		}

		Order order = new Order();
		for (Order item : orders) {
			if (item.getCustomerId() == customerId && item.getId() == orderId) {
				order = item;
				break;
			}
		}

		// validate stock availability
		for (OrderDetail itemDetail : orderDetails) {
			if (itemDetail.getOrderId() == order.getId()) {
				for (Product itemProduct : productService.retrieveAllProducts()) {
					if (itemProduct.getId() == itemDetail.getProductId()) {
						if (itemProduct.getUnitStock() < itemDetail.getQuantity()) {
							return ResponseEntity.status(HttpStatus.BAD_REQUEST)
									.body(itemProduct.getName() + " is only " + itemProduct.getUnitStock() + " left!"
											+ ". Please update the quantity from " + itemDetail.getQuantity());
						}
					}
				}
			}
		}

		// validate payment
		if (order.getPaymentMethodId() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Please select the payment method for this order.");
		} else {
			boolean isRegistered = false;
			for (PaymentMethod item : paymentMethodService.getPaymentMethods()) {
				if (item.getId() == order.getPaymentMethodId()) {
					isRegistered = true;
					if (!item.isStatus()) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Method " + item.getName()
								+ " is not available. Please select another payment method");
					}
				}
			}
			if (!isRegistered) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Payment Method is not registered. Please select another payment method");
			}
		}

		// validate delivery address
		if (order.getCustomerAddressId() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Please select the delivery address for this order.");
		} else {
			boolean isFoundAddress = false;
			for (CustomerAddress item : addressService.getAddresses()) {
				if (item.getId() == order.getCustomerAddressId()) {
					isFoundAddress = true;
				}
			}
			if (!isFoundAddress) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("The delivery address is unregistered. Please add a new address.");
			}
		}

		order.setStatus(OrderStatus.ORDERED);
		order.setDeliveryPrice(new BigDecimal(10000));
		order.setOrderDate(new Date());
		order.setPaymentDate(new Date());
		order.setPaymentStatus(PaymentStatus.PAID);

		// update unit stock of product
		for (OrderDetail itemDetail : orderDetails) {
			if (itemDetail.getOrderId() == order.getId()) {
				for (Product itemProduct : productService.retrieveAllProducts()) {
					if (itemProduct.getId() == itemDetail.getProductId()) {
						int totalStock = itemProduct.getUnitStock() - itemDetail.getQuantity();
						itemProduct.setUnitStock(totalStock);
					}
				}
			}
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@RequestMapping(value = "/selectPaymentMethod/{order_id}", method = RequestMethod.POST)
	public ResponseEntity<?> selectPaymentMethod(@PathVariable("order_id") long orderId,
			@RequestBody PaymentMethod payment) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();

		// get customer id
		int customerId = 0;
		for (User user : userService.getUsers()) {
			if (user.getUsername().equalsIgnoreCase(name)) {
				customerId = user.getId();
				break;
			}
		}

		// get data order
		Order order = null;
		for (Order item : orders) {
			if (item.getCustomerId() == customerId && item.getId() == orderId) {
				order = item;
				break;
			}
		}
		order.setPaymentMethodId(payment.getId());

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@RequestMapping(value = "/viewPaymentMethods", method = RequestMethod.GET)
	public ResponseEntity<List<PaymentMethod>> viewPaymentMethods() {
		List<PaymentMethod> activePayments = new ArrayList<>();
		for (PaymentMethod item : paymentMethodService.getPaymentMethods()) {
			if (item.isStatus()) {
				activePayments.add(item);
			}
		}
		return new ResponseEntity<List<PaymentMethod>>(activePayments, HttpStatus.OK);
	}

	@RequestMapping(value = "/viewAddress", method = RequestMethod.GET)
	public ResponseEntity<List<CustomerAddress>> viewAddress() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		// get customer id
		int customerId = 0;
		for (User user : userService.getUsers()) {
			if (user.getUsername().equalsIgnoreCase(name)) {
				customerId = user.getId();
				break;
			}
		}

		List<CustomerAddress> custAddresses = new ArrayList<>();
		for (CustomerAddress item : addressService.getAddresses()) {
			if (item.getUserId() == customerId) {
				custAddresses.add(item);
			}
		}

		return new ResponseEntity<List<CustomerAddress>>(custAddresses, HttpStatus.OK);
	}

	@RequestMapping(value = "/selectDeliveryAddress/{order_id}", method = RequestMethod.POST)
	public ResponseEntity<?> selectDeliveryAddress(@PathVariable("order_id") long orderId,
			@RequestBody CustomerAddress address) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();

		// get customer id
		int customerId = 0;
		for (User user : userService.getUsers()) {
			if (user.getUsername().equalsIgnoreCase(name)) {
				customerId = user.getId();
				break;
			}
		}

		// get data order
		Order order = null;
		for (Order item : orders) {
			if (item.getCustomerId() == customerId && item.getId() == orderId) {
				order = item;
				break;
			}
		}

		boolean isFoundAddress = false;
		for (CustomerAddress item : addressService.getAddresses()) {
			if (item.getId() == address.getId() && item.getUserId() == customerId) {
				isFoundAddress = true;
			}
		}
		if (!isFoundAddress) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("The delivery address does not belong to user " + name);
		}

		order.setCustomerAddressId(address.getId());

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@RequestMapping(value = "/deleteAddress/{address_id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAddress(@PathVariable("address_id") long addId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		log.info("login active = " + name);

		// get customer id
		int customerId = 0;
		for (User user : userService.getUsers()) {
			if (user.getUsername().equalsIgnoreCase(name)) {
				customerId = user.getId();
				break;
			}
		}

		List<CustomerAddress> toRemove = new ArrayList<>();
		for (CustomerAddress item : addressService.getAddresses()) {
			if (item.getId() == addId && item.getUserId() == customerId) {
				toRemove.add(item);
				break;
			}
		}
		addressService.getAddresses().removeAll(toRemove);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@RequestMapping(value = "/viewAllOrders", method = RequestMethod.GET)
	public ResponseEntity<List<Order>> viewAllOrders() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		log.info("login active = " + name);

		List<Order> fixedOrders = new ArrayList<>();
		for (Order order : orders) {
			if (order.getStatus().compareTo(OrderStatus.CART) != 0) {
				fixedOrders.add(order);
			}
		}
		return new ResponseEntity<List<Order>>(fixedOrders, HttpStatus.OK);
	}

	@RequestMapping(value = "/verifyOrder/{order_id}", method = RequestMethod.GET)
	public ResponseEntity<?> verifyOrder(@PathVariable("order_id") long orderId) {

		boolean isFoundOrder = false;
		Order verifiedOrder = null;
		for (Order order : orders) {
			if (order.getId() == orderId) {
				isFoundOrder = true;
				order.setStatus(OrderStatus.VERIFIED);
				verifiedOrder = order;
				break;
			}
		}
		if (!isFoundOrder) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order is not found. Please put the right one.");
		}
		return new ResponseEntity<Order>(verifiedOrder, HttpStatus.OK);
	}

	@RequestMapping(value = "/processShipOrder/{order_id}", method = RequestMethod.GET)
	public ResponseEntity<?> processShipOrder(@PathVariable("order_id") long orderId) {

		UUID uuid = UUID.randomUUID();
		Order shippedOrder = null;
		for (Order item : orders) {
			if (item.getId() == orderId && item.getStatus().compareTo(OrderStatus.VERIFIED) == 0) {
				item.setStatus(OrderStatus.SHIPPED);
				item.setShipDate(new Date());
				item.setShipNumber(uuid.toString());
				shippedOrder = item;
				break;
			}
		}
		return new ResponseEntity<Order>(shippedOrder, HttpStatus.OK);
	}

	@RequestMapping(value = "/viewSubmittedOrder", method = RequestMethod.GET)
	public ResponseEntity<Order> viewSubmittedOrder() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		log.info("login active = " + name);

		// get customer id
		int customerId = 0;
		if (userService == null) {
			userService = new UserService();
		}
		for (User user : userService.getUsers()) {
			if (user.getUsername().equalsIgnoreCase(name)) {
				customerId = user.getId();
				break;
			}
		}

		// get data order
		Order order = null;
		for (Order item : orders) {
			if (item.getCustomerId() == customerId && item.getStatus().compareTo(OrderStatus.CART) != 0) {
				order = item;
				break;
			}
		}

		return new ResponseEntity<Order>(order, HttpStatus.OK);
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

}
