package com.authservice.utils;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.authservice.exception.AuthException;
import com.authservice.service.UserService;
import com.example.dto.UserModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * @author Sundar G
 * Date: 15/04/2021
 * Time: 07:43 PM
 */

@Service
public class JWTUtils {
	
	//private String secretKey = "sundar";
	private String app = "test";
	private long expire_in_mts = 120;
	
	//@Autowired
	//private AuthServiceConfig authConfig;
	
	@Value("${jwt.secret}")
    private String secret;
	
    @Autowired
    @Lazy
	private UserService userService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtils.class);
	
	
	
	

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        //System.out.println("role "+(String) claims.get("role"));
       // System.out.println("pwd "+(String) claims.get("password"));
        //String role = (String) claims.get("role");
        return claimsResolver.apply(claims);
    }
    public Object extractKeyClaim(String token, String keyClaim) {
        Claims claims = extractAllClaims(token);
        if(claims.get(keyClaim) != null) {
        	return claims.get(keyClaim);
        }else {
        	throw new AuthException("Invalid jwt token", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    public Claims extractAllClaims(String token) {
        try {
        	return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
		} catch (ExpiredJwtException ex) {
			throw ex;
		}

    }
    
    /*public String isTokenValid(String token) {
    	JwtResponseModel jwtModel = new  JwtResponseModel();
        jwtModel.setAccessToken((String) extractKeyClaim(token,HitsLimsConstants.JWT_ID_KEY).toString());
        jwtModel.setTokenType(HitsLimsConstants.JWT_OPAQUE_STRING);
       // jwtModel.setUserId(extractKeyClaim(token,HitsLimsConstants.JWT_USER_KEY).toString());
        UserModel userModel = userService.validateUserToken(jwtModel);
        if (userModel == null) {
            throw new BadCredentialsException("Authentication failed ");
        }
        if(userModel != null && userModel.getError() != null && !userModel.getError().trim().equalsIgnoreCase("")) {
        	throw new HitsException(userModel.getError(), HttpStatus.BAD_REQUEST);
        }
		
		return userModel.getUserId();
	}*/
    
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken() {
        Map<String, Object> claims = new HashMap<>(); // <role,list<authorities>
        claims.put("application", this.app);
        return createToken(claims);
    }
    
    public String generateTokenWithClaims(String username,String password,String roles) {
        Map<String, Object> claims = new HashMap<>(); // <role,list<authorities>
        if(roles != null){
        	 claims.put("role", roles);
        }
        if(password != null){
        	claims.put("password", password);
        }
        if(username != null){
        	claims.put("userid", username);
        } 
        claims.put("application", this.app);
        return createToken(claims);
        
    }

    protected Date getExpirationTime(Long expiryTimeDuration)
    {
    	Date now = new Date();
        Long expireInMilis = TimeUnit.MINUTES.toMillis(expiryTimeDuration);
        return new Date(expireInMilis + now.getTime());
    }
    
    private String createToken(Map<String, Object> claims) {
    	SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return Jwts.builder()
        		.setClaims(claims)
        		.setId(String.valueOf(new Random().nextInt(99999)))
        		.setSubject(this.app)
        		.setIssuedAt(new Date())
                .setExpiration(getExpirationTime(this.expire_in_mts))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    
    
    private Key getSigningKey() {
    	byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public boolean validateJwtToken(String token,HttpServletRequest request) {
    	try {
			//Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
    		Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
			return true;
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException  ex) {
			//throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
			request.setAttribute("exception", ex);
			throw ex;
		}catch (ExpiredJwtException exp) {
			request.setAttribute("exception", exp);
        	request.setAttribute("message", "Access token expired!.Please make a new sign-in request");
        	//this.logout(exp.getClaims().getId().toString());
			throw exp;
		}
      }
    
    public UsernamePasswordAuthenticationToken getAuthentication(String username,String application) {
    	String message = "";
    	UserModel userModel = new UserModel();
    	List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
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
      }
    
	public boolean validateAccessToken(String token,HttpServletRequest request) {
		try { 
		     boolean tokenFlag = !isTokenExpired(token);
		     if(!tokenFlag) {
		    	 throw new BadCredentialsException("Refresh token invalid.Please make new signin request" );
		     }
		     return true;
		}  catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			//throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
			request.setAttribute("exception", ex);
			throw ex;

		}catch (ExpiredJwtException exp) {
			request.setAttribute("exception", exp);
        	request.setAttribute("message", "Access token expired!.Please make a new sign-in request");
        	//this.logout(exp.getClaims().getId().toString());
			throw exp;
		}
	}


    public  String getSubject(HttpServletRequest httpServletRequest, String jwtTokenCookieName, String signingKey){
        String token = CookieUtils.getValue(httpServletRequest, jwtTokenCookieName);
        if(token == null) return null;
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody().getSubject();
    }
	
    public String getUserRefreshToken() {
    	return UUID.randomUUID().toString().replaceAll("-", "");
    }


}
