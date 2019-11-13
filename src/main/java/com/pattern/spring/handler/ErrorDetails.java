package com.pattern.spring.handler;

/**
 * Classe padronizada para apresentar o erros na aplicação.
 */
public class ErrorDetails {

	private String title;
	private int status;
	private String detail;
	private long timestamp;
	private String devMessage;

	public String getTitle() {

		return title;
	}

	public void setTitle(final String title) {

		this.title = title;
	}

	public int getStatus() {

		return status;
	}

	public void setStatus(final int status) {

		this.status = status;
	}

	public String getDetail() {

		return detail;
	}

	public void setDetail(final String detail) {

		this.detail = detail;
	}

	public long getTimestamp() {

		return timestamp;
	}

	public void setTimestamp(final long timestamp) {

		this.timestamp = timestamp;
	}

	public String getDevMessage() {

		return devMessage;
	}

	public void setDevMessage(final String devMessage) {

		this.devMessage = devMessage;
	}

	/**
	 * Classe {@code builder} para facilitar a criação da excessão.
	 */
	public static final class Builder {

		private String title;
		private int status;
		private String detail;
		private long timestamp;
		private String devMessage;

		/**
		 * Contrutor padrão.
		 */
		private Builder() {

		}

		/**
		 * Método para instanciar o build desta classe.
		 *
		 * @return {@link ErrorDetails.Builder}
		 */
		public static Builder newBuilder() {

			return new Builder();
		}

		/**
		 * Método de construção com parâmetro.
		 *
		 * @param title um {@code String} com o título do erro
		 * @return {@link ErrorDetails.Builder}
		 */
		public Builder title(final String title) {

			this.title = title;
			return this;
		}

		/**
		 * Método de construção com parâmetro.
		 *
		 * @param status um {@code int} com o {@code HTTP status}
		 * @return {@link ErrorDetails.Builder}
		 */
		public Builder status(final int status) {

			this.status = status;
			return this;
		}

		/**
		 * Método de construção com parâmetro.
		 *
		 * @param detail um {@code String} com o detalhe do erro
		 * @return {@link ErrorDetails.Builder}
		 */
		public Builder detail(final String detail) {

			this.detail = detail;
			return this;
		}

		/**
		 * Método de construção com parâmetro.
		 *
		 * @param timestamp um {@code long} com a data em formato long
		 * @return {@link ErrorDetails.Builder}
		 */
		public Builder timestamp(final long timestamp) {

			this.timestamp = timestamp;
			return this;
		}

		/**
		 * Método de construção com parâmetro.
		 *
		 * @param devMessage um {@code String} com alguma mensagem para os desenvolvedores
		 * @return {@link ErrorDetails.Builder}
		 */
		public Builder devMessage(final String devMessage) {

			this.devMessage = devMessage;
			return this;
		}

		/**
		 * Métdodo com retorno do {@code build} desta classe.
		 *
		 * @return {@link ErrorDetails} com as informações do erro
		 */
		public ErrorDetails build() {

			final ErrorDetails rnfDetails = new ErrorDetails();

			rnfDetails.setTitle(this.title);
			rnfDetails.setStatus(this.status);
			rnfDetails.setDetail(this.detail);
			rnfDetails.setTimestamp(this.timestamp);
			rnfDetails.setDevMessage(this.devMessage);

			return rnfDetails;
		}
	}
}
