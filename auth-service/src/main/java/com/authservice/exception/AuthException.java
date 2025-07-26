package com.authservice.exception;



import org.springframework.http.HttpStatus;
/**
 * @author Sundar G
 * Date: 15/05/2021
 * Time: 11:55 PM
 */
public class AuthException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final String message;
  private final HttpStatus httpStatus;

  public AuthException(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

}
