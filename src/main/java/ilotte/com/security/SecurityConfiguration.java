package ilotte.com.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import ilotte.com.model.User;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

	private List<User> users = new ArrayList<User>();

	public List<User> getUsers() {
		return users;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().httpBasic().and().authorizeRequests().antMatchers("/").permitAll()
				.antMatchers("/viewProduct").permitAll().antMatchers(HttpMethod.POST, "/addProductCart").hasRole("USER")
				.antMatchers(HttpMethod.POST, "/modifyProductCart").hasRole("USER")
				.antMatchers(HttpMethod.DELETE, "/deleteProductCart").hasRole("USER")
				.antMatchers(HttpMethod.GET, "/viewCart").hasRole("USER").antMatchers(HttpMethod.GET, "/viewAddress")
				.hasRole("USER").antMatchers(HttpMethod.GET, "/selectDeliveryAddress/*").hasRole("USER")
				.antMatchers(HttpMethod.GET, "/viewAllOrders").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "/processShipOrder/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "/verifyOrder/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "/viewPaymentMethods").hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.GET, "/viewSubmittedOrder").hasRole("USER").anyRequest().authenticated();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
		auth.inMemoryAuthentication().withUser("rouly").password("rouly").roles("ADMIN");
		auth.inMemoryAuthentication().withUser("edsheeran").password("edsheeran").roles("USER");
		auth.inMemoryAuthentication().withUser("agnezmo").password("agnezmo").roles("USER");
		auth.inMemoryAuthentication().withUser("dilan").password("dilan").roles("USER");
	}
}
