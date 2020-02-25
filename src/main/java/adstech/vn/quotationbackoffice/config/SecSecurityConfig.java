package adstech.vn.quotationbackoffice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import adstech.vn.quotationbackoffice.service.UserService;

@Configuration
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserService userService;

//	@Override
//	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("user1").password(passwordEncoder().encode("user1Pass")).roles("USER")
//				.and().withUser("user2").password(passwordEncoder().encode("user2Pass")).roles("USER").and()
//				.withUser("admin").password(passwordEncoder().encode("adminPass")).roles("ADMIN");
//	}

	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());

	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http
				// .csrf().disable()
				.authorizeRequests().antMatchers("/resources/**").permitAll().antMatchers("/admin/**").hasRole("SYS_ADMIN")
				.antMatchers("/login*", "/logout*", "/perform_login").permitAll().anyRequest().hasAnyRole("ADMIN", "SALE", "TECHNICIAN", "SYS_ADMIN").and().formLogin().loginPage("/login")
				.loginProcessingUrl("/perform_login").defaultSuccessUrl("/")
				// .failureUrl("/login.html?error=true")
				// .failureHandler(authenticationFailureHandler())
				.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login");
		// .logoutSuccessHandler(logoutSuccessHandler());
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
