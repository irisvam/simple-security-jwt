package com.pattern.spring.configuration;

import org.springframework.core.env.Environment;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Classse de configuração do Hibernate.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.pattern.spring.repositories")
@PropertySource(value = {"classpath:application.properties"})
public class AppConfigHibernate {

	@Autowired
	private Environment environment;

	/**
	 * Método na arquitetura {@code Spring} responsável pela criação do {@code @Bean} para Data Source.
	 *
	 * @return {@link DataSource} com as informações de conexão
	 * @throws IllegalStateException quando coloca uma variável errada
	 * @throws PropertyVetoException quando colocar um drive class errado
	 */
	@Bean(destroyMethod = "close")
	public DataSource dataSource() throws IllegalStateException, PropertyVetoException {

		final ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setJdbcUrl(environment.getRequiredProperty("jdbc.url"));
		dataSource.setUser(environment.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
		dataSource.setTestConnectionOnCheckout(false);
		dataSource.setTestConnectionOnCheckin(true);
		dataSource.setIdleConnectionTestPeriod(180);
		dataSource.setPreferredTestQuery("SELECT 1");

		return dataSource;
	}

	/**
	 * Método na arquitetura {@code Spring} responsável pela criação do {@code @Bean} para o Entity Manager.
	 *
	 * @return {@link LocalContainerEntityManagerFactoryBean} com a fábrica de entitys
	 * @throws IllegalStateException quando coloca uma variável errada
	 * @throws PropertyVetoException quando coloca uma variável errada
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws IllegalStateException, PropertyVetoException {

		final LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
		entityManager.setDataSource(dataSource());
		entityManager.setPackagesToScan("com.pattern.spring.model");
		entityManager.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		entityManager.setJpaDialect(new HibernateJpaDialect());
		entityManager.setJpaProperties(hibernateProperties());

		return entityManager;
	}

	/**
	 * Método para preparar o Propertie com as variáveis de ambiente do Hibernate.
	 *
	 * @return {@link Properties} com as variáveis de ambiente
	 */
	private Properties hibernateProperties() {

		final Properties properties = new Properties();
		properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
		properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
		properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
		properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
		properties.put("hibernate.jdbc.lob.non_contextual_creation", true);
		// properties.put("hibernate.temp.use_jdbc_metadata_defaults", false);

		return properties;
	}

	/**
	 * Método na arquitetura {@code Spring} responsável pela criação do {@code @Bean} para uma {@code Transaction}.
	 *
	 * @return {@link JpaTransactionManager} com a transação pronta para sre utilizada
	 * @throws IllegalStateException quando coloca uma variável errada
	 * @throws PropertyVetoException quando coloca uma variável errada
	 */
	@Bean
	@Autowired
	public JpaTransactionManager transactionManager() throws IllegalStateException, PropertyVetoException {

		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

		return transactionManager;
	}

}
