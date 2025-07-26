package com.test.example.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
@RefreshScope
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    public boolean isInvalid(String token) {
        return this.isTokenExpired(token);
    }
    
    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    public Object extractKeyClaim(String token, String keyClaim) {
        Claims claims = getAllClaimsFromToken(token);
        if(claims.get(keyClaim) != null) {
        	return claims.get(keyClaim);
        }else {
        	throw new UnsupportedJwtException("Invalid Token "+token);
        }
    }
    
   /* public String getAuthentication(String username,String application) {
    	String message = "";
    	UserModel userModel = new UserModel();
    	//List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		try {
			message = "Invalid username/password supplied";
			userModel.setUserId(username);
			userModel = userService.validateUserLogin(userModel);
			if (userModel == null) {
	            throw new BadCredentialsException("Authentication failed for " + username);
	        }			
	        grantedAuthorities.add(new SimpleGrantedAuthority(userModel.getRoleName().toUpperCase()));
	        
	        
		} catch (Exception e) {
			throw new AuthException(message, HttpStatus.UNPROCESSABLE_ENTITY);
		}
        
    	return new UsernamePasswordAuthenticationToken(username, null,grantedAuthorities);
      }*/
    
    public boolean validateAccessToken(String token) {
		try { 
		     boolean tokenFlag = !isTokenExpired(token);
		     if(!tokenFlag) {
		    	 throw new UnsupportedJwtException("Invalid Token "+token);
		     }
		     return true;
		}  catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			//throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
			//request.setAttribute("exception", ex);
			throw ex;

		}catch (ExpiredJwtException exp) {
			//request.setAttribute("exception", exp);
        	//request.setAttribute("message", "Access token expired!.Please make a new sign-in request");
        	//this.logout(exp.getClaims().getId().toString());
			throw exp;
		}
	}
    public boolean validateJwtToken(String token) {
    	try {
			//Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
    		Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
			return true;
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException  ex) {
			//throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
			//request.setAttribute("exception", ex);
			throw ex;
		}catch (ExpiredJwtException exp) {
			//request.setAttribute("exception", exp);
        	//request.setAttribute("message", "Access token expired!.Please make a new sign-in request");
        	//this.logout(exp.getClaims().getId().toString());
			throw exp;
		}
      }
    

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}