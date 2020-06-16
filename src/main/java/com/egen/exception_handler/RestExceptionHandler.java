package com.egen.exception_handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.egen.exception.ParseException;
import com.egen.exception.ValidatorException;
import com.egen.exception.AlertServicException;
import com.egen.exception.ResourceNotFoundException;
import com.egen.response.ProductErrorResponse;
import com.egen.utils.ErrorConstants;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = Logger.getLogger(RestExceptionHandler.class.getName());

	/**
	 * 
	 * Handle MissingServletRequestParameterException. Triggered when a 'required'
	 * request parameter is missing.
	 *
	 * @param ex      MissingServletRequestParameterException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return ProductErrorResponse
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = ex.getParameterName() + " parameter is missing";
		LOGGER.info(ex.getMessage());
		return buildResponseEntity(new ProductErrorResponse(BAD_REQUEST, error, ex));
	}

	/**
	 * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is
	 * invalid as well.
	 *
	 * @param ex      HttpMediaTypeNotSupportedException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return ProductErrorResponse
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
		return buildResponseEntity(new ProductErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
				builder.substring(0, builder.length() - 2), ex));
	}

	/**
	 * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid
	 * validation.
	 *
	 * @param ex      the MethodArgumentNotValidException that is thrown when @Valid
	 *                validation fails
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return ProductErrorResponse
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		LOGGER.info(ex.getMessage());
		ProductErrorResponse productErrorResponse = new ProductErrorResponse(BAD_REQUEST);
		productErrorResponse.setMessage(ErrorConstants.VALIDATION_ERROR);
		productErrorResponse.addValidationErrors(ex.getBindingResult().getFieldErrors());
		productErrorResponse.addValidationError(ex.getBindingResult().getGlobalErrors());
		return buildResponseEntity(productErrorResponse);
	}

	/**
	 * Handles javax.validation.ConstraintViolationException. Thrown when @Validated
	 * fails.
	 *
	 * @param ex the ConstraintViolationException
	 * @return ProductErrorResponse
	 */
	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
		LOGGER.info(ex.getMessage());
		ProductErrorResponse productErrorResponse = new ProductErrorResponse(BAD_REQUEST);
		productErrorResponse.setMessage(ErrorConstants.VALIDATION_ERROR);
		productErrorResponse.addValidationErrors(ex.getConstraintViolations());
		return buildResponseEntity(productErrorResponse);
	}

	/**
	 * Handle HttpMessageNotReadableException. Happens when request JSON is
	 * malformed.
	 *
	 * @param ex      HttpMessageNotReadableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return ProductErrorResponse
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		LOGGER.info(ex.getMessage());
		return buildResponseEntity(new ProductErrorResponse(HttpStatus.BAD_REQUEST, ErrorConstants.MALFORMED_JSON, ex));
	}

	/**
	 * Handle HttpMessageNotWritableException.
	 *
	 * @param ex      HttpMessageNotWritableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return ProductErrorResponse
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// String error = "Error writing JSON output";
		return buildResponseEntity(new ProductErrorResponse(HttpStatus.BAD_REQUEST, ex));
	}

	/**
	 * Handle NoHandlerFoundException.
	 *
	 * @param NoHandlerFoundException
	 * @param headers
	 * @param status
	 * @param request
	 * @return ProductErrorResponse
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		LOGGER.info(ex.getMessage());
		ProductErrorResponse productErrorResponse = new ProductErrorResponse(BAD_REQUEST);
		productErrorResponse.setMessage(
				String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
		productErrorResponse.setDebugMessage(ex.getMessage());
		return buildResponseEntity(productErrorResponse);
	}

	/**
	 * 
	 * @param EntityNotFoundException
	 * @return ProductErrorResponse
	 */
	@ExceptionHandler(javax.persistence.EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
		return buildResponseEntity(new ProductErrorResponse(HttpStatus.NOT_FOUND, ex));
	}

	/**
	 * Handle Exception, handle generic Exception.class
	 *
	 * @param MethodArgumentTypeMismatchException
	 * @return ProductErrorResponse
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		LOGGER.info(ex.getMessage());
		ProductErrorResponse productErrorResponse = new ProductErrorResponse(BAD_REQUEST);
		productErrorResponse
				.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
						ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
		productErrorResponse.setDebugMessage(ex.getMessage());
		return buildResponseEntity(productErrorResponse);
	}

	/**
	 * Handle ValidatorException
	 * 
	 * @param productValidatorException
	 * @return ProductErrorResponse
	 */
	@ExceptionHandler(ValidatorException.class)
	protected ResponseEntity<Object> handleProductValidatorException(ValidatorException ex, WebRequest request) {
		LOGGER.info(ex.getMessage());
		ProductErrorResponse productErrorResponse = new ProductErrorResponse(BAD_REQUEST);
		productErrorResponse.setMessage(ErrorConstants.VALIDATION_ERROR);
		productErrorResponse.setDebugMessage(ex.getMessage());
		return buildResponseEntity(productErrorResponse);
	}

	/**
	 * Handles Various Parse Operations Exception
	 * 
	 * @param ParseException
	 * @param request
	 * @return ProductErrorResponse
	 */
	@ExceptionHandler(ParseException.class)
	public ResponseEntity<Object> handleParseServiceException(ParseException ex, WebRequest request) {
		LOGGER.info(ex.getMessage());
		ProductErrorResponse productErrorResponse = new ProductErrorResponse(HttpStatus.BAD_REQUEST, ex);
		productErrorResponse.setMessage(ErrorConstants.PARSE_EXCEPTION_ERROR);
		productErrorResponse.setDebugMessage(ex.getMessage());
		return buildResponseEntity(productErrorResponse);
	}

	/**
	 * Handles (HttpClientErrorException
	 * 
	 * @param HttpClientErrorException
	 * @param request
	 * @return ProductErrorResponse
	 */
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {
		LOGGER.info(ex.getMessage());
		ProductErrorResponse productErrorResponse = new ProductErrorResponse(HttpStatus.NOT_FOUND);
		productErrorResponse.setMessage(ErrorConstants.LOGIN_ERROR);
		productErrorResponse.setDebugMessage(ex.getMessage());
		return buildResponseEntity(productErrorResponse);
	}

	/**
	 * Handles InterruptedException thrown by Spring ExecutorServiceAdapter
	 * 
	 * @param InterruptedException
	 * @param request
	 * @return ProductErrorResponse
	 */
	@ExceptionHandler(InterruptedException.class)
	public ResponseEntity<Object> handleInterruptedException(InterruptedException ex, WebRequest request) {
		LOGGER.info(ex.getMessage());
		ProductErrorResponse productErrorResponse = new ProductErrorResponse(HttpStatus.BAD_REQUEST, ex);
		productErrorResponse.setMessage(ex.getMessage());
		productErrorResponse.setDebugMessage(ex.getLocalizedMessage());
		return buildResponseEntity(productErrorResponse);
	}

	/**
	 * Handles ExecutionException thrown by Spring ExecutorServiceAdapter
	 * 
	 * @param ExecutionException
	 * @param request
	 * @return ProductErrorResponse
	 */
	@ExceptionHandler(ExecutionException.class)
	public ResponseEntity<Object> handleExecutionException(ExecutionException ex, WebRequest request) {
		LOGGER.info(ex.getMessage());
		ProductErrorResponse productErrorResponse = new ProductErrorResponse(HttpStatus.BAD_REQUEST, ex);
		productErrorResponse.setMessage(ex.getMessage());
		productErrorResponse.setDebugMessage(ex.getLocalizedMessage());
		return buildResponseEntity(productErrorResponse);
	}

	/**
	 * Handles Various Runtime Exceptions
	 * 
	 * @param RuntimeException
	 * @param request
	 * @return ProductErrorResponse
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleRunTimeException(RuntimeException ex, WebRequest request) {
		LOGGER.info(ex.getMessage());
		ProductErrorResponse productErrorResponse = new ProductErrorResponse(HttpStatus.BAD_REQUEST, ex);
		productErrorResponse.setMessage(ex.getMessage());
		LOGGER.log(java.util.logging.Level.SEVERE, ex.getMessage(), ex);
		productErrorResponse.setDebugMessage(ex.getLocalizedMessage());
		return buildResponseEntity(productErrorResponse);
	}

	/**
	 * Handles Various Runtime Exceptions
	 * 
	 * @param RuntimeException
	 * @param request
	 * @return ProductErrorResponse
	 */
	@ExceptionHandler(AlertServicException.class)
	public ResponseEntity<Object> handleVehicleServiceException(AlertServicException ex, WebRequest request) {
		LOGGER.info(ex.getMessage());
		ProductErrorResponse productErrorResponse = new ProductErrorResponse(HttpStatus.BAD_REQUEST, ex);
		productErrorResponse.setMessage(ex.getMessage());
		LOGGER.log(java.util.logging.Level.SEVERE, ex.getMessage(), ex);
		productErrorResponse.setDebugMessage(ex.getLocalizedMessage());
		return buildResponseEntity(productErrorResponse);
	}
	
	/**
	 * Handles Various Runtime Exceptions
	 * 
	 * @param RuntimeException
	 * @param request
	 * @return ProductErrorResponse
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		LOGGER.info(ex.getMessage());
		ProductErrorResponse productErrorResponse = new ProductErrorResponse(HttpStatus.NOT_FOUND, ex);
		productErrorResponse.setMessage(ex.getMessage());
		LOGGER.log(java.util.logging.Level.SEVERE, ex.getMessage(), ex);
		productErrorResponse.setDebugMessage(ex.getLocalizedMessage());
		return buildResponseEntity(productErrorResponse);
	}

	/**
	 * 
	 * @param productError esponse
	 * @return ReponseBody
	 */
	protected static ResponseEntity<Object> buildResponseEntity(ProductErrorResponse productErrorResponse) {
		return new ResponseEntity<>(productErrorResponse, productErrorResponse.getStatus());
	}

}
