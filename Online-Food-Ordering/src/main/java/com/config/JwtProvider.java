package com.config;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {
	
	private SecretKey keys = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
	
	public String generationToken(Authentication auth) {
		Collection<? extends GrantedAuthority>authorities=auth.getAuthorities();
		String role = populateAuthorities(authorities);
		
		String Jwt=Jwts.builder().setIssuedAt(new Date())
				.setExpiration((new Date(new Date().getTime()+65000000)))
				.claim("email",auth.getName())
				.claim("authorities",role)
				.signWith(keys)
				.compact();
		
		return Jwt;
	}
	
	public String getEmailFromJwtToken(String jwt) {
		jwt = jwt.substring(7);
		Claims claims = Jwts.parser().setSigningKey(keys).build().parseClaimsJws(jwt).getBody();
		
		String email = String.valueOf(claims.get("email"));
		return email;
	}

	private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
		
		Set<String> auths=new HashSet<>();
		
		for(GrantedAuthority authority:authorities) {
			auths.add(authority.getAuthority());
		}
		
		return String.join(",", auths);
	}
}
