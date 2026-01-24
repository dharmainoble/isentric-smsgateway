package com.isentric.bulkgateway.security;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = {"com.isentric.bulkgateway.bc.repository"},
		entityManagerFactoryRef = "bcEntityManagerFactory",
		transactionManagerRef = "bcTransactionManager"
)
public class BulkConfigDBConfig {

	@Bean(name = "bcDataSourceProperties")
	@ConfigurationProperties("spring.datasource.bc")
	public DataSourceProperties bcDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "bcDataSource")
	@ConfigurationProperties("spring.datasource.bc")
	public DataSource bcDataSource() {
		return bcDataSourceProperties()
				.initializeDataSourceBuilder()
				.build();
	}

	@Bean(name = "bcEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean bcEntityManagerFactory(
			@Qualifier("bcDataSource") DataSource dataSource,
			EntityManagerFactoryBuilder builder) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("hibernate.hbm2ddl.auto", "none");
		return builder
				.dataSource(dataSource)
				.packages("com.isentric.bulkgateway.bc.model").persistenceUnit("Bulk Config")
				.properties(properties)
				.build();
	}

	@Bean(name = "bcTransactionManager")
	public PlatformTransactionManager bcTransactionManager(
			@Qualifier("bcEntityManagerFactory") LocalContainerEntityManagerFactoryBean bcEntityManagerFactory) {
		return new JpaTransactionManager(Objects.requireNonNull(bcEntityManagerFactory.getObject()));
	}
}