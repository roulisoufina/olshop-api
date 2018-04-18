package ilotte.com.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ilotte.com.model.Role;
import ilotte.com.model.User;

@Component
public class UserService {

	private static List<User> users = new ArrayList<User>();

	static {
		// Initialize Data
		User user1 = new User(1, "admin", "admin", Role.ADMIN);
		User user2 = new User(2, "rouly", "rouly", Role.ADMIN);
		User user3 = new User(3, "edsheeran", "edsheeran", Role.USER);
		User user4 = new User(4, "agnezmo", "agnezmo", Role.USER);
		User user5 = new User(5, "dilan", "dilan", Role.USER);
		users.add(user1);
		users.add(user2);
		users.add(user3);
		users.add(user4);
		users.add(user5);
	}

	public List<User> getUsers() {
		return users;
	}

}
