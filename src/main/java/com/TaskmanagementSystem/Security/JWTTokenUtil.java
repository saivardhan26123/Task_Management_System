package com.TaskmanagementSystem.Security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.TaskmanagementSystem.Entity.UserAuthenticate;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTTokenUtil {
	
	private final Key key;

	private final long expireToken=1000L*60*60*24;
	
	public String extractUsername(String token) {
	    return Jwts.parserBuilder()
	            .setSigningKey(key)
	            .build()
	            .parseClaimsJws(token)
	            .getBody()
	            .getSubject();
	}
	public JWTTokenUtil() {
		String secret =System.getenv("JWT_SECRET");
		if(secret==null || secret.isEmpty()) {
			secret="ReplaceThisWithVerySecretKey";
		}
		key=Keys.hmacShaKeyFor(secret.getBytes());
	}
	public String genrateToken(UserAuthenticate user) {
		Date now = new Date();
		Date expiry = new Date(now.getTime()+expireToken);
		
		return Jwts.builder()
				.setSubject(user.getUserOfficialEmail())
				.setIssuedAt(now)
				.setExpiration(expiry)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public boolean validToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		}catch(JwtException e) {
			return false;
		}	
	}
	
	public String extractUserEmail(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
		 		.getSubject();
	}
}
