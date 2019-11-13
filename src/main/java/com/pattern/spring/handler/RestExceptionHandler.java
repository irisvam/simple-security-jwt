package com.pattern.spring.handler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
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
import com.pattern.spring.exception.UnauthorizedException;

/**
 * Classe que {@code extends} {@link ResponseEntityExceptionHandler} para tratar os erros dos serviços.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Método para tratar dos erros de {@code HttpStatus.NOT_FOUND} dos serviços deste {@code WebService}.
	 *
	 * @param rnfException um {@link ResourceNotFoundException} quando enviado com a mensagem de erro
	 * @return {@link ResponseEntity} com a resposta {@code HTTP}
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handlerResourceNotFoundException(final ResourceNotFoundException rnfException) {

		final ErrorDetails errorDetails = ErrorDetails.Builder.newBuilder().timestamp(new Date().getTime()).status(HttpStatus.NOT_FOUND.value())
				.title("Recurso não encontrado!").detail(rnfException.getMessage()).devMessage(rnfException.getClass().getName()).build();

		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	/**
	 * Método para tratar dos erros de {@code HttpStatus.BAD_REQUEST} dos serviços deste {@code WebService}.
	 *
	 * @param pnvException um {@link ParameterNotValidException} quando enviado com a mensagem de erro
	 * @return {@link ResponseEntity} com a resposta {@code HTTP}
	 */
	@ExceptionHandler(ParameterNotValidException.class)
	public ResponseEntity<?> handlerParamNotValidException(final ParameterNotValidException pnvException) {

		final ErrorDetails errorDetails = ErrorDetails.Builder.newBuilder().timestamp(new Date().getTime()).status(HttpStatus.BAD_REQUEST.value())
				.title("Parâmetro não aceitável!").detail(pnvException.getMessage()).devMessage(pnvException.getClass().getName()).build();

		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Método para tratar os erros de {@code HttpStatus.UNAUTHORIZED}.
	 *
	 * @param unauthException um {@link UnauthorizedException} quando enviado com a mensagem de erro
	 * @return {@link ResponseEntity} com a resposta {@code HTTP}
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> handlerUnauthotizedException(final UnauthorizedException unauthException) {

		final ErrorDetails errorDetails = ErrorDetails.Builder.newBuilder().timestamp(new Date().getTime()).status(HttpStatus.UNAUTHORIZED.value())
				.title("Não Autorizado!").detail(unauthException.getMessage()).devMessage(unauthException.getClass().getName()).build();

		return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Método para tratar os erros de {@code HttpStatus.UNAUTHORIZED} de autenticações.
	 *
	 * @param authException um {@link AuthenticationException} quando enviado com a mensagem de erro
	 * @param response um {@link HttpServletResponse}
	 * @return {@link ResponseEntity} com a resposta {@code HTTP}
	 */
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> handleAuthenticationException(final AuthenticationException authException, final HttpServletResponse response) {

		final ErrorDetails errorDetails = ErrorDetails.Builder.newBuilder().timestamp(new Date().getTime()).status(HttpStatus.UNAUTHORIZED.value())
				.title("Não Autenticado!").detail(authException.getMessage()).devMessage(authException.getClass().getName()).build();

		return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Método para tratar os erros de {@code HttpStatus.UNAUTHORIZED} de autenticações service.
	 *
	 * @param authException um {@link AuthenticationServiceException} quando enviado com a mensagem de erro
	 * @return {@link ResponseEntity} com a resposta {@code HTTP}
	 */
	@ExceptionHandler(AuthenticationServiceException.class)
	public ResponseEntity<?> handleAuthenticationServiceException(final AuthenticationServiceException authException) {

		final ErrorDetails errorDetails = ErrorDetails.Builder.newBuilder().timestamp(new Date().getTime()).status(HttpStatus.UNAUTHORIZED.value())
				.title("Não Autenticado!").detail(authException.getMessage()).devMessage(authException.getClass().getName()).build();

		return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Método para tratar os erros de {@code HttpStatus.FORBIDDEN} de autenticações não permitidos.
	 *
	 * @param accessException um {@link AccessDeniedException} quando enviado com a mensagem de erro
	 * @param response um {@link HttpServletResponse}
	 * @return {@link ResponseEntity} com a resposta {@code HTTP}
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDeniedException(final AccessDeniedException accessException, final HttpServletResponse response) {

		final ErrorDetails errorDetails = ErrorDetails.Builder.newBuilder().timestamp(new Date().getTime()).status(HttpStatus.FORBIDDEN.value())
				.title("Não Autorizado!").detail(accessException.getMessage()).devMessage(accessException.getClass().getName()).build();

		return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
	}

	/**
	 * @param insauthException um {@link InsufficientAuthenticationException} quando enviado com a mensagem de erro
	 * @param response um {@link HttpServletResponse}
	 * @return {@link ResponseEntity} com a resposta {@code HTTP}
	 */
	@ExceptionHandler(InsufficientAuthenticationException.class)
	public ResponseEntity<?> handleInsufficientAuthenticationException(final InsufficientAuthenticationException insauthException,
			final HttpServletResponse response) {

		final ErrorDetails errorDetails = ErrorDetails.Builder.newBuilder().timestamp(new Date().getTime()).status(HttpStatus.UNAUTHORIZED.value())
				.title("Não Autorizado!").detail(insauthException.getMessage()).devMessage(insauthException.getClass().getName()).build();

		return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Método para tratar dos erros de {@code HttpStatus.BAD_REQUEST} dos serviços deste {@code WebService}.
	 *
	 * @param matmException um {@link MethodArgumentTypeMismatchException} quando enviado com a mensagem de erro
	 * @return {@link ResponseEntity} com a resposta {@code HTTP}
	 */
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
