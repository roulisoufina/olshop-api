package ilotte.com.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;

import ilotte.com.model.Order;
import ilotte.com.model.Order.OrderStatus;
import ilotte.com.model.OrderDetail;
import ilotte.com.service.UserService;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ProcessingControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private ProcessingController processingController;
	@Mock
	private UserService userService;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new ProcessingController()).build();
	}

	@Test
	public void viewProduct() throws Exception {
		mockMvc.perform(get("/viewProduct")).andExpect(status().isOk());
	}

	@Test
	public void login() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("dilan", "dilan"));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login").header(HttpHeaders.AUTHORIZATION,
				"Basic " + Base64Utils.encodeToString("dilan:dilan".getBytes()));
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	public void testAddProduct() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("dilan", "dilan"));
		String foundProductJson = "{\"productId\":" + 1 + ",\"quantity\":" + 1 + "}";

		RequestBuilder requestBuilder1 = MockMvcRequestBuilders.post("/addProductCart")
				.contentType(MediaType.APPLICATION_JSON).content(foundProductJson)
				.with(user("dilan").password("dilan"));
		MvcResult result1 = mockMvc.perform(requestBuilder1).andReturn();
		MockHttpServletResponse response1 = result1.getResponse();
		assertEquals(HttpStatus.CREATED.value(), response1.getStatus());

	}

	@Test
	public void testAddProductNotIdentified() throws Exception {
		String notFoundProductJson = "{\"productId\":" + 20 + ",\"quantity\":" + 1 + "}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/addProductCart")
				.contentType(MediaType.APPLICATION_JSON).content(notFoundProductJson)
				.with(user("dilan").password("dilan"));
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertTrue(response.getContentAsString().contains("Product is not found. Please select another product"));

	}

	@Test
	public void viewSubmittedOrderValidatePayment() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("dilan", "dilan"));

		Order order = new Order();
		order.setId(1);
		order.setCustomerId(5);
		order.setStatus(OrderStatus.ORDERED);
		List<Order> orders = new ArrayList<>();
		orders.add(order);

		OrderDetail detail = new OrderDetail();
		detail.setId(1);
		detail.setOrderId(1);
		List<OrderDetail> details = new ArrayList<>();
		details.add(detail);
		order.setOrderDetails(details);
		processingController.setOrders(orders);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/submitOrder/1");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertTrue(response.getContentAsString().contains("Please select the payment method for this order."));

	}

	@Test
	public void viewSubmittedOrderValidateAddress() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("dilan", "dilan"));

		Order order = new Order();
		order.setId(1);
		order.setCustomerId(5);
		order.setStatus(OrderStatus.ORDERED);
		order.setPaymentMethodId(1);
		order.setCustomerAddressId(4);
		List<Order> orders = new ArrayList<>();
		orders.add(order);

		OrderDetail detail = new OrderDetail();
		detail.setId(1);
		detail.setOrderId(1);
		List<OrderDetail> details = new ArrayList<>();
		details.add(detail);
		order.setOrderDetails(details);
		processingController.setOrders(orders);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/submitOrder/1");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}
}
