package ru.dreremin.predefense.registration.sys.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
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
	
	private final String ADMIN = "ADMIN";
	
	private final String STUDENT = "STUDENT";
	
	private final String TEACHER = "TEACHER";
	
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
				.antMatchers("/admin").hasRole(ADMIN)
				.antMatchers("/admin/**").hasRole(ADMIN)
				.antMatchers("/student").hasRole(ADMIN)
				.antMatchers("/student/**").hasRole(ADMIN)
				.antMatchers("/teacher").hasRole(ADMIN)
				.antMatchers("/teacher/**").hasRole(ADMIN)
				.antMatchers(HttpMethod.GET, "/commission").hasRole(STUDENT)
				.antMatchers(HttpMethod.POST, "/commission").hasRole(ADMIN)
				.antMatchers(HttpMethod.DELETE, "/commission/**")
						.hasRole(ADMIN)
				.antMatchers(HttpMethod.PATCH, "/commission/**")
						.hasRole(ADMIN)
				.antMatchers("/mailing").hasRole(ADMIN)
				.antMatchers("/user/**").hasRole(ADMIN)
				.antMatchers("note/commission/**").hasRole(TEACHER)
				.antMatchers("/registration/commission/**")
						.hasAnyRole(TEACHER, STUDENT)
				.antMatchers(HttpMethod.GET, "/commission/list")
						.hasAnyRole(ADMIN, STUDENT, TEACHER)
				.antMatchers("/user").hasAnyRole(ADMIN, STUDENT, TEACHER)
				.antMatchers("/session/login").permitAll()
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
