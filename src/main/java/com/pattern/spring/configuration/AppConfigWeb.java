package com.pattern.spring.configuration;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.pattern.spring.converter.StringToEnumConverter;

/**
 * Classe de Configuração Spring para os Beans iniciais.
 */
@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
@ComponentScan(basePackages = "com.pattern.spring")
public class AppConfigWeb extends WebMvcConfigurationSupport {

	/**
	 * Método na arquitetura {@code Spring} responsável pela criação do {@code @Bean} para Locale.
	 *
	 * @return {@link LocaleResolver} com o local da aplicação
	 */
	@Bean
	public LocaleResolver localeResolver() {

		final SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(new Locale("pt_br"));

		return resolver;
	}

	/**
	 * Método na arquitetura {@code Spring} responsável pela criação do {@code @Bean} para Message.
	 *
	 * @return {@link MessageSource} com as mensagens da aplicação
	 */
	@Bean
	public MessageSource messageSource() {

		final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");

		return messageSource;
	}

	@Override
	protected void addFormatters(final FormatterRegistry registry) {

		registry.addConverter(new StringToEnumConverter());
	}

	@Override
	protected void addCorsMappings(final CorsRegistry registry) {

		registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*").allowedHeaders("*").allowCredentials(true);
	}

}
