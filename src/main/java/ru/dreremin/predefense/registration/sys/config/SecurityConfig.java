package ru.dreremin.predefense.registration.sys.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders
		  .AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders
		  .HttpSecurity;
import org.springframework.security.config.annotation.web.configuration
		  .EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration
		  .WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication
		  .UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.services.auth
		 .ActorDetailsService;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final ActorDetailsService actorDetailsService;
	private final JwtFilter jwtFilter;
	private final RestAuthenticationEntryPoint authEntryPoint;
	private final RestAccessDeniedHandler acessDeniedHandler;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) 
			throws Exception {
		auth.userDetailsService(actorDetailsService)
				.passwordEncoder(getPasswordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable()                             
				.authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/teacher/**").hasRole("TEACHER")
				.antMatchers("/student/**").hasRole("STUDENT")
				.antMatchers("/user/**").permitAll()
				.and()
				.formLogin().disable()                    
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.exceptionHandling().authenticationEntryPoint(authEntryPoint);
		http.exceptionHandling().accessDeniedHandler(acessDeniedHandler);
		http.addFilterBefore(jwtFilter, 
				UsernamePasswordAuthenticationFilter.class);
	}
	
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
