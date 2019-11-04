package com.pattern.spring.handler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pattern.spring.exception.ParameterNotValidException;
import com.pattern.spring.exception.ResourceNotFoundException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handlerResourceNotFoundException(final ResourceNotFoundException rnfException) {
		
		final ErrorDetails errorDetails = ErrorDetails.Builder.newBuilder().timestamp(new Date().getTime()).status(HttpStatus.NOT_FOUND.value())
				.title("Recurso não encontrado!").detail(rnfException.getMessage()).devMessage(rnfException.getClass().getName()).build();
		
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ParameterNotValidException.class)
	public ResponseEntity<?> handlerParamNotValidException(final ParameterNotValidException pnvException) {
		
		final ErrorDetails errorDetails = ErrorDetails.Builder.newBuilder().timestamp(new Date().getTime()).status(HttpStatus.BAD_REQUEST.value())
				.title("Parâmetro não aceitável!").detail(pnvException.getMessage()).devMessage(pnvException.getClass().getName()).build();
		
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<?> handlerMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException matmException) {
		
		String typeEsperado = null;
		final StringBuilder valorErrado = new StringBuilder();
		
		if (matmException.getRequiredType().getName().equals("java.util.List")) {
			
			typeEsperado = matmException.getParameter().getGenericParameterType().getTypeName().replaceAll("java.util.List", "");
			final String[] arrEsperado = (String[]) matmException.getValue();
			valorErrado.append("[");
			for (int index = 0; index < arrEsperado.length; index++) {
				if (index != 0) {
					valorErrado.append(",");
				}
				valorErrado.append(arrEsperado[index]);
			}
			valorErrado.append("]");
		} else {
			
			typeEsperado = matmException.getRequiredType().getName();
			valorErrado.append(matmException.getValue());
		}
		
		final ErrorDetails errorDetails = ErrorDetails.Builder.newBuilder().timestamp(new Date().getTime()).status(HttpStatus.BAD_REQUEST.value())
				.title("Valor de parâmetro não esperado!").detail("Falha ao tentar converter valor(es) '" + valorErrado.toString()
						+ "' do parâmetro '" + matmException.getName() + "' para o tipo '" + typeEsperado + "' esperado.")
				.devMessage(matmException.getClass().getName()).build();
		
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException msrpException,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		
		final ErrorDetails errorDetails = ErrorDetails.Builder.newBuilder().timestamp(new Date().getTime()).status(HttpStatus.BAD_REQUEST.value())
				.title("Parâmetro requerido não recebido!").detail("Parâmetro '" + msrpException.getParameterName() + "' está faltando.")
				.devMessage(msrpException.getClass().getName()).build();
		
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException manvException, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {
		
		final List<FieldError> lista = manvException.getBindingResult().getFieldErrors();
		
		final String fildMessage = lista.stream().map(field -> {
			return new StringBuilder("'").append(field.getField()).append("': ").append(field.getDefaultMessage()).toString();
		}).collect(Collectors.joining(" , "));
		
		final ErrorDetails errorDetails = ErrorDetails.Builder.newBuilder().timestamp(new Date().getTime()).status(HttpStatus.BAD_REQUEST.value())
				.title("Erro de validação de campo!").detail(fildMessage).devMessage(manvException.getClass().getName()).build();
		
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(final Exception exception, final Object body, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {
		
		final ErrorDetails errorDetails = ErrorDetails.Builder.newBuilder().timestamp(new Date().getTime()).status(status.value())
				.title("Erro na requisição interna!").detail(exception.getMessage()).devMessage(exception.getClass().getName()).build();
		
		return new ResponseEntity<>(errorDetails, headers, status);
	}
	
}
