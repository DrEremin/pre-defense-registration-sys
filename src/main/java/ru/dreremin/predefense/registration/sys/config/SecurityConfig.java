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
				.antMatchers("/users/admin/create").hasRole(ADMIN)
				.antMatchers("/users/student/create").hasRole(ADMIN)
				.antMatchers("/users/teacher/create").hasRole(ADMIN)
				.antMatchers("/users/admin/delete/**").hasRole(ADMIN)
				.antMatchers("/users/student/delete/**").hasRole(ADMIN)
				.antMatchers("/users/teacher/delete/**").hasRole(ADMIN)
				.antMatchers("/users/students/delete/all").hasRole(ADMIN)
				.antMatchers("/commission/create").hasRole(ADMIN)
				.antMatchers("/commission/delete/**").hasRole(ADMIN)
				.antMatchers("/mailing/**").hasRole(ADMIN)
				.antMatchers("/users/admins/read/all").hasRole(ADMIN)
				.antMatchers("/users/teachers/read/all").hasRole(ADMIN)
				.antMatchers("/users/students/read/all").hasRole(ADMIN)
				.antMatchers("/commission/update/**").hasRole(ADMIN)
				.antMatchers("/users/teacher/update/**").hasRole(ADMIN)
				.antMatchers("/users/student/update/**").hasRole(ADMIN)
				.antMatchers("/user/update/password").hasRole(ADMIN)
				.antMatchers("/user/delete/**").hasRole(ADMIN)
				.antMatchers("/note/write").hasRole(TEACHER)
				.antMatchers("/registration/create/**")
						.hasAnyRole(TEACHER, STUDENT)
				.antMatchers("/registration/delete")
						.hasAnyRole(TEACHER, STUDENT)
				.antMatchers("/commission/read").hasAnyRole(ADMIN, STUDENT)
				.antMatchers("/commissions/read")
						.hasAnyRole(ADMIN, STUDENT, TEACHER)
				.antMatchers("/user/get").hasAnyRole(ADMIN, STUDENT, TEACHER)
				.antMatchers("/user/login").permitAll()
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
