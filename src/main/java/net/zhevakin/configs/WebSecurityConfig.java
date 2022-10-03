package net.zhevakin.configs;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zhevakin.details.MyUserDetails;
import net.zhevakin.models.UserData;
import net.zhevakin.models.enums.Provider;
import net.zhevakin.services.CustomOAuth2UserService;
import net.zhevakin.services.UserDetailsServiceImpl;
import net.zhevakin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	private CustomOAuth2UserService oauthUserService;


	@Autowired
	private Map<Provider, UserService> userServiceMap;


	@Override
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public SessionRegistry sessionRegistry () {

		return new SessionRegistryImpl();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
			.antMatchers("/", "/login", "/register", "/oauth/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin().permitAll()
				.loginPage("/login")
				.usernameParameter("email")
				.passwordParameter("pass")
				.defaultSuccessUrl("/welcome")
			.and()
			.oauth2Login()
				.loginPage("/login")
				.userInfoEndpoint()
					.userService(oauthUserService)
				.and()
				.successHandler(new AuthenticationSuccessHandler() {

					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						System.out.println("AuthenticationSuccessHandler invoked");
						System.out.println("Authentication name: " + authentication.getName());
						DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
						UserData user = new UserData();
						user.setEmail(oauthUser.getAttribute("email"));
						user.setUsername(oauthUser.getAttribute("email"));
						userServiceMap.get(Provider.GOOGLE).processOAuthPostLogin(user);
						Object creds = authentication.getCredentials();
						List<GrantedAuthority> authority = (List<GrantedAuthority>) authentication.getAuthorities();
						authentication = new UsernamePasswordAuthenticationToken(new MyUserDetails(userServiceMap.get(Provider.GOOGLE)
																.getUser(user.getUsername())), creds, authority );
						SecurityContextHolder.getContext().setAuthentication(authentication);
						response.sendRedirect("/welcome");
					}
				})
				//.defaultSuccessUrl("/list")
			.and()
			.logout().logoutSuccessUrl("/").permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/403")
			;
		http.sessionManagement()
				.maximumSessions(100)
				.maxSessionsPreventsLogin(false)
				.expiredUrl("/login")
				.sessionRegistry(sessionRegistry());
	}


}
