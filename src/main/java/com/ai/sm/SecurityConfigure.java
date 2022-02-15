package com.ai.sm;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfigure extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder encode;

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().passwordEncoder(encode).dataSource(dataSource)
				.usersByUsernameQuery("select id,password,enable from user where id=?")
				.authoritiesByUsernameQuery("select id,role from user where id=?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().
		antMatchers(
				"/resources/static/resources/js/**",
				"/resources/static/resources/css/**"
				).permitAll()
		.antMatchers("/user/**")
		.hasRole("USER")
		.and()
		.formLogin()
		.loginPage("/")
		.defaultSuccessUrl("/user/welcome")
		.and()
		.logout();
	
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

}
