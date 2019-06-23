package com.projects.binlist.security.jwt;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.projects.binlist.components.CardDetailCommandComponent;
import com.projects.binlist.components.CustomUserDetails;
import com.projects.binlist.components.JwtManager;
import com.projects.binlist.controllers.AuthController;
import com.projects.binlist.controllers.CardDetailController;
import com.projects.binlist.filters.JwtAuthenticationFilter;
import com.projects.binlist.models.User;
import com.projects.binlist.services.CardDetailService;
import com.projects.binlist.services.CustomUserDetailsService;
import com.projects.binlist.services.UserService;
import com.projects.binlist.utils.RoleType;

@RunWith(SpringRunner.class)
@WebMvcTest({ AuthController.class, CardDetailController.class })
public class SecurityTest {

	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;

	@MockBean
	private AuthenticationManager authenticationManager;

	@SpyBean
	private JwtManager jwtManager;

	@MockBean
	private CustomUserDetailsService customUserDetailsService;

	@MockBean
	private CardDetailService cardDetailService;

	@MockBean
	UserService userService;
	
	@MockBean
	CardDetailCommandComponent cardDetailCommandService;

	@Configuration
	@ComponentScan(basePackages = { "com.projects.binlist.controllers" })
	static class AppTestConfig {
		@Bean
		public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
			LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
			em.setDataSource(dataSource());
			em.setPackagesToScan(new String[] { "com.projects.binlist.models" });

			JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
			em.setJpaVendorAdapter(vendorAdapter);
			return em;
		}

		@Bean
		public DataSource dataSource() {
			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName("org.h2.Driver");
			dataSource.setUrl("jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1");
			return dataSource;
		}
	}

	@Before
	public void setup() {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
		jwtAuthenticationFilter.setJwtManager(jwtManager);
		mvc = MockMvcBuilders.webAppContextSetup(context)
				.addFilter(jwtAuthenticationFilter, "/*")
				.apply(springSecurity()).build();
	}

	@Test
	@WithMockUser(roles = "USER")
	public void successfulAccessToEndPointWithUserRoleAndWithoutToken() throws Exception {
		mvc.perform(get("/card-scheme/stats?start=1&limit=6").with(csrf()))
				.andExpect(status().is(401));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void successfulAccessToEndPointWithUserRoleAndToken() throws Exception {

		User user = new User();
		user.setUsername("user");
		List<String> myRoles = new ArrayList<>();
		myRoles.add(RoleType.ROLE_ADMIN.getValue());

		CustomUserDetails userdetails = CustomUserDetails.create(user);

		List<GrantedAuthority> authorities = CustomUserDetails.getAuthorities(myRoles);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userdetails, "",
				authorities);

		String token = jwtManager.generateToken(authentication);
		mvc.perform(get("/card-scheme/stats?start=1&limit=6").with(csrf()).header("Authorization", "Bearer " + token))
				.andExpect(status().is(200));
	}
	
	@Test
	@WithAnonymousUser
	public void shouldGetUnauthorizedWithAnonymousUser() throws Exception {

		mvc.perform(get("/card-scheme/stats?start=1&limit=6").with(csrf())).andExpect(status().isUnauthorized());
	}

}
