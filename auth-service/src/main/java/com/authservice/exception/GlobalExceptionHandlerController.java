package com.authservice.exception;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.example.dto.ApiResponseModel;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
/**
 * @author Sundar G
 * Date: 15/05/2021
 * Time: 11:34 PM
 */

@RestControllerAdvice
public class GlobalExceptionHandlerController{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandlerController.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

  @ExceptionHandler(AuthException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ApiResponseModel handleHitsException(AuthException ex) throws Exception {
	return getErrorResponseBody(ex.getHttpStatus(),ex);
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody ApiResponseModel handleBadCredentialsException(BadCredentialsException ex)  {
	  return getErrorResponseBody(HttpStatus.BAD_REQUEST,ex);
  }
  
  @ExceptionHandler(MalformedJwtException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody ApiResponseModel handleMalformedJwtException(MalformedJwtException ex) {
	  return getErrorResponseBody(HttpStatus.BAD_REQUEST,ex);
  }
  
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(value = HttpStatus.FORBIDDEN)
  public @ResponseBody ApiResponseModel handleAccessDeniedException(AccessDeniedException ex) {
    return getErrorResponseBody(HttpStatus.FORBIDDEN,ex);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ApiResponseModel handleException(Exception ex) throws Exception {
	  return getErrorResponseBody(HttpStatus.INTERNAL_SERVER_ERROR,ex);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody ApiResponseModel handleValidationException(MethodArgumentNotValidException ex) {
      return getErrorResponseBody(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler(SQLException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ApiResponseModel handleSQLException(SQLException ex) throws Exception {
	  return getErrorResponseBody(HttpStatus.INTERNAL_SERVER_ERROR,ex);
  }
  
  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ApiResponseModel handleRunTimeException(Exception ex) throws Exception {
	  return getErrorResponseBody(HttpStatus.INTERNAL_SERVER_ERROR,ex);
  }
  
  @ExceptionHandler(UnsupportedJwtException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody ApiResponseModel handleUnsupportedJwtException(UnsupportedJwtException ex)  {
	  return getErrorResponseBody(HttpStatus.BAD_REQUEST,ex);
  }
  
  @ExceptionHandler(ExpiredJwtException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public @ResponseBody ApiResponseModel handleExpiredJwtException(ExpiredJwtException ex) {
	  return getErrorResponseBody(HttpStatus.UNAUTHORIZED,ex);
  }
  
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  @ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
  public @ResponseBody ApiResponseModel handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
	  return getErrorResponseBody(HttpStatus.PAYLOAD_TOO_LARGE,ex);
  }
  
  @ExceptionHandler(FileNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody ApiResponseModel handleFileNotFoundException(FileNotFoundException ex) {
	  return getErrorResponseBody(HttpStatus.NOT_FOUND,ex);
  }
  
  public ApiResponseModel getErrorResponseBody(HttpStatus httpStatus,Exception ex) {
		ApiResponseModel error = new ApiResponseModel();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		StringWriter stackTrace = new StringWriter();
		ex.printStackTrace(new PrintWriter(stackTrace));
		error.setTrace(stackTrace.toString());
		error.setMessage(ex.getMessage());
		error.setPath(request.getRequestURI());
		error.setDate(dateFormat.format(new Date()));
		error.setError(String.valueOf(httpStatus.value()));
		error.setStatus(httpStatus.value());
		LOGGER.error("Exception: "+stackTrace.toString());
		return error;
	}

}
