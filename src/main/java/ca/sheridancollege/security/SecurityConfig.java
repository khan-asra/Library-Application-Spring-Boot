package ca.sheridancollege.security;



import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;


//@SuppressWarnings("deprecation")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
@Autowired
private LoggingAccessDenied accessDeniedHandler;

@Bean
public BCryptPasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();	
}

@Autowired
@Lazy
private BCryptPasswordEncoder passwordEncoder;


@Autowired
private DataSource dataSource;
/**
 * create a Bean of type in JdbcUserDetailManager that will be 
 * used in HomeController
 * @return an instance of JdbcUserDetailManager configured to use 
 * our database
 * @throws Exception
 */
	
@Bean 
public JdbcUserDetailsManager jdbcUserDetailsManager() throws Exception {
	JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
	jdbcUserDetailsManager.setDataSource(dataSource);
	return jdbcUserDetailsManager;
	
}

/**
 * HttpSecurity configure web based security for specified Http request
 * we use antMatcher to specify the resource/request and which 
 * roles are required for access
 */
	@Override
	public void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests()
		.antMatchers("/user/**").hasAnyRole("USER")// i removed manager ?
		.antMatchers("/admin/**").hasAnyRole("MANAGER")
		.antMatchers("/","/**").permitAll()
		.antMatchers("/h2-console/**").permitAll()
		.antMatchers("/","/**").permitAll()
		.and() //Alls the chain of configuration calls
		.formLogin().loginPage("/login") //  request to map login.html
		.defaultSuccessUrl("/") // if the login is success 
		.and()
		.logout().invalidateHttpSession(true)
		.clearAuthentication(true)
		.and().exceptionHandling()
		.accessDeniedHandler(accessDeniedHandler);
		
		http.csrf().disable();
		http.headers().frameOptions().disable();
		
	}
	
	
// depreciated : future version may not include it . run it at your own risk.

	@Override
	protected void configure(AuthenticationManagerBuilder auth)throws Exception {
		auth.jdbcAuthentication()
		//used to inject the data source
		.dataSource(dataSource)
		//use the default schema 
		.withDefaultSchema()
		//secure BCryptoPasswordEncoder
		.passwordEncoder(passwordEncoder)
		//to populate the table with user and manager
		.withUser("bugs").password(passwordEncoder.encode("bunny")).roles("USER")
		.and()
		.withUser("daffy").password(passwordEncoder.encode("duck")).roles("MANAGER","USER");
		
	}
	
	
	
	   
	

	
	
}
