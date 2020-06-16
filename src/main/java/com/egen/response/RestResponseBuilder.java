package com.egen.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 
 * @author sachinsharma
 *
 *         Custom Builder class for Response Entity for App
 */

public class RestResponseBuilder {

	private RestResponseBuilder() {

	}

	/**
	 * 
	 * @param <T>
	 * @param response
	 * @param message
	 * @param httpStatus
	 * @return ResponseEntity
	 */
	public static <T> ResponseEntity<Object> buildResponseEntity(List<T> response, String message,
			HttpStatus httpStatus) {
		ProductResponse<T> productResponse = new ProductResponse<T>();
		productResponse.setStatus(httpStatus);
		productResponse.setMessage(message);
		productResponse.setResponse(response);
		return buildResponseEntity(productResponse);

	}

	/**
	 * 
	 * @param <T>
	 * @param response
	 * @param message
	 * @param httpStatus
	 * @return
	 */
	public static <T> ResponseEntity<Object> buildResponseEntity(String response, String message,
			HttpStatus httpStatus) {
		ProductResponse<T> productResponse = new ProductResponse<T>();
		productResponse.setStatus(httpStatus);
		productResponse.setMessage(response);
		return buildResponseEntity(productResponse);
	}

	/**
	 * 
	 * @param <T>
	 * @param response
	 * @param message
	 * @param httpStatus
	 * @return
	 */
	public static <T> ResponseEntity<Object> buildResponseEntity(Object response, String message,
			HttpStatus httpStatus) {
		ProductResponse<T> productResponse = new ProductResponse<T>();
		productResponse.setStatus(httpStatus);
		productResponse.setMessage(response.toString());
		return buildResponseEntity(productResponse);
	}

	/**
	 * 
	 * @param productErrorResponse
	 * @return ReponseBody
	 */
	protected static ResponseEntity<Object> buildResponseEntity(ProductResponse<?> productResponse) {
		return new ResponseEntity<>(productResponse, productResponse.getStatus());
	}
}
