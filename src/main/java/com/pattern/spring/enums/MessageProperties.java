package com.pattern.spring.enums;

/**
 * {@code Enum} para opções utilizadas para mensagens de resposta.
 *
 * @see #MESSAGE
 * @see #POST_CONFLICT
 * @see #POST_CREATED
 * @see #GET_NOT_FOUND
 * @see #GET_OK
 * @see #GET_LIST_OK
 * @see #GET_NO_CONTENT
 * @see #PUT_CONFLICT
 * @see #PUT_NOT_FOUND
 * @see #PUT_OK
 * @see #BAD_REQUEST
 * @see #POST_BAD_REQUEST
 * @see #POST_BAD_CREDENTIALS
 */
public enum MessageProperties {

	MESSAGE 		("message"),
	POST_CONFLICT 	("controller.post.conflict"), 
	POST_CREATED 	("controller.post.created"),
	GET_NOT_FOUND 	("controller.get.not_found"),
	GET_OK 			("controller.get.ok"),
	GET_LIST_OK 	("controller.get.list.ok"),
	GET_NO_CONTENT	("controller.get.list.no_content"),  
	PUT_CONFLICT 	("controller.put.conflict"),
	PUT_NOT_FOUND 	("controller.put.not_found"),
	PUT_OK 			("controller.put.ok"),
	BAD_REQUEST		("controller.parameter.bad_request"),
	POST_BAD_REQUEST("controller.post.bad_request"),
	POST_BAD_CREDENTIALS ("controller.post.bad_credencial");

	private final String descricao;

	/**
	 * Construtor padrão para este {@code Enum}.
	 *
	 * @param descricao um {@code String} que represente a descrição
	 */
	MessageProperties(final String descricao) {

		this.descricao = descricao;
	}

	public String getDescricao() {

		return descricao;
	}

	/**
	 * Método para ordenar os valores deste {@code Enum}.
	 *
	 * @return {@link MessageProperties}{@code []}
	 */
	public Object readResolve() {

		return MessageProperties.values()[ordinal()];
	}

}
