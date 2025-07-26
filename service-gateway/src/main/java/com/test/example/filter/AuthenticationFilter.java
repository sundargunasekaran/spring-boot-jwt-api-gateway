package com.test.example.filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import javax.servlet.http.HttpServletRequest;
import com.test.example.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.UnsupportedJwtException;
import reactor.core.publisher.Mono;




@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

    @Autowired
    private RouterValidator routerValidator;
    @Autowired
    private JwtUtil jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String userId = null;
        String jwt = null;
        String application = null;
        String subject = null;
        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request))
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);

             jwt = this.getAuthHeader(request);
            if (jwt != null && jwt.startsWith("Bearer ")) {
            	jwt = jwt.substring(7);
            }else {
            	return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
            }
            if (jwtUtils.isInvalid(jwt))
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

            if (jwt != null  && jwtUtils.validateJwtToken(jwt)) {
            	this.populateRequestWithHeaders(exchange, jwt);
                
            }else {
            	 return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
            }
            
            
            //start
            
            
            
          /*  if (jwt != null  && jwtUtils.validateJwtToken(jwt)) {
				userId = jwtUtils.extractKeyClaim(jwt,"userid").toString();
                application = jwtUtils.extractKeyClaim(jwt,"application").toString();
                subject = jwtUtils.extractKeyClaim(jwt,"sub").toString();
                if(subject != null && !subject.equalsIgnoreCase("test")) {
                	throw new UnsupportedJwtException("Invalid Token "+jwt);
        		}
                if(userId == null || userId.trim().equals("")) {
                	throw new UnsupportedJwtException("Invalid Token "+jwt);
                }

                 if (userId != null && !userId.trim().equals("") ) {
                	// do user validation in DB
                	if (jwtUtils.validateAccessToken(jwt)) {
                    	
                    	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtUtils.getAuthentication(userId,"");   //3
                    	usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   	 	SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                   	 	request.setAttribute("userId", userId);
                   	 	request.setAttribute("claims", jwtUtils.extractAllClaims(jwt));
						String[] roles = usernamePasswordAuthenticationToken
								.getAuthorities()
								.stream()
								.map(GrantedAuthority::getAuthority)
								.toArray(String[]::new);
						request.setAttribute("roles", roles);
					}else {
                    	throw new UnsupportedJwtException("Invalid Token "+jwt);
                    } 
                }
            }else {
            	throw new UnsupportedJwtException("Invalid Token "+jwt);
            }
            */
            
            
            
            
            //ens
            
            
         /*   try {
//              //REST call to AUTH service
//              template.getForObject("http://IDENTITY-SERVICE//validate?token" + authHeader, String.class);
              jwtUtil.validateToken(authHeader);

          } catch (Exception e) {
              System.out.println("invalid access...!");
              throw new RuntimeException("un authorized access to application");
          }*/
            
            
            
        
        
    }
        return chain.filter(exchange);
    }
    /*PRIVATE*/

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtils.getAllClaimsFromToken(token);
        exchange.getRequest().mutate()
                .header("id", String.valueOf(claims.get("jti")))
                .header("userId", String.valueOf(claims.get("userid")))
                .build();
    }
}
