package ilotte.com.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ilotte.com.model.CustomerAddress;

@Component
public class AddressService {

	private static List<CustomerAddress> addresses = new ArrayList<>();

	static {
		// Initialize Data
		CustomerAddress address1 = new CustomerAddress(1, 3, "Ed Sheeran's Mom", "Ed Sheeran Hoz 1",
				"Jalan Karet Karya 1", "DKI Jakarta", "Jakarta", "a@yahoo.com", "021-6119191");
		CustomerAddress address2 = new CustomerAddress(2, 3, "Ed Sheeran", "Ed Sheeran's Cousin", "Jalan Buah Batu",
				"Bandung", "Bandung", "edsc@yahoo.com", "022-6119191");
		CustomerAddress address3 = new CustomerAddress(3, 3, "Ed Sheeran", "Ed Sheeran Hoz 2", "Jalan Danau Sunter",
				"DKI Jakarta", "Jakarta", "eds@yahoo.com", "62812812920191");
		CustomerAddress address4 = new CustomerAddress(4, 5, "Dilan", "Dilan Bandung", "Jalan Dayeuhkolot", "Bandung",
				"Bandung", "dilan@yahoo.com", "628111131141");
		CustomerAddress address5 = new CustomerAddress(5, 4, "Agnes Monica", "agnes Bandung", "Jalan Moh Toha",
				"Bandung", "Bandung", "am@yahoo.com", "628111131141");
		addresses.add(address1);
		addresses.add(address2);
		addresses.add(address3);
		addresses.add(address4);
		addresses.add(address5);
	}

	public List<CustomerAddress> getAddresses() {
		return addresses;
	}

}
