package com.codewiki.springpreauth.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import com.codewiki.springpreauth.service.UserDetailsServiceImpl;
import com.codewiki.springpreauth.util.AuthoritiesConstants;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Inject
    private UserDetailsServiceImpl userDetailsService;

	 @Inject
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	    	auth.authenticationProvider(customAuthenticationProvider());
	    }
	    
	    @Bean
	    public RequestHeaderAuthenticationFilter siteMinderFilter() throws Exception{
	    	RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter = new RequestHeaderAuthenticationFilter();
	    	requestHeaderAuthenticationFilter.setPrincipalRequestHeader("HTTP_LANID");
	    	requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
	    	return requestHeaderAuthenticationFilter;
	    }
	    

	    @Bean
	    public PreAuthenticatedAuthenticationProvider customAuthenticationProvider(){
	    	
	   	 UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = 
	                new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(userDetailsService);
	    	PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
	    	preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(wrapper);
			return preAuthenticatedAuthenticationProvider;
	    		
	    }
	  
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	    	
	    	 http
	          .addFilter(siteMinderFilter())
	          .authorizeRequests()
	    	 	.antMatchers("/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
	        .and()
	        .logout()
	        .and()
	        .csrf()
	            .disable()
	        .headers()
	            .frameOptions().disable();
	    }

	
}