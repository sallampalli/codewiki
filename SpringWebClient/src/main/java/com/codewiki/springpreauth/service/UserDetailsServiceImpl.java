package com.codewiki.springpreauth.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl extends
		PreAuthenticatedGrantedAuthoritiesUserDetailsService implements
		org.springframework.security.core.userdetails.UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		System.out.println("SSO Username :"+username);
		
		// The roles are hardcoded in here, but the intention here is to pull the roles for a user based on the username
		//from the database or a custom authorization service. 
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(
				"ROLE_ADMIN");
		grantedAuthorities.add(grantedAuthority);
		return new User(username, "N/A", grantedAuthorities);
	}


}