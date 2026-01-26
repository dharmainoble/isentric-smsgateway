package com.isentric.bulkgateway.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

import com.isentric.bulkgateway.repository.BulkSkipAutoResendRepository;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = {"com.isentric.bulkgateway.bg.repository"},
		entityManagerFactoryRef = "bgEntityManagerFactory",
		transactionManagerRef = "bgTransactionManager"
)
public class BulkGatewayDBConfig {

    @Primary
    @Bean(name = "bgDataSourceProperties")
    @ConfigurationProperties("spring.datasource.bg")
    public DataSourceProperties bgDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "bgDataSource")
    @ConfigurationProperties("spring.datasource.bg")
    public DataSource bgDataSource() {
        return bgDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Primary
    @Bean(name = "bgEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean bgEntityManagerFactory(
            @Qualifier("bgDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        return builder
                .dataSource(dataSource)
                .packages("com.isentric.bulkgateway.bg.model").persistenceUnit("Bulk Gateway")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "bgTransactionManager")
    public PlatformTransactionManager bgTransactionManager(
            @Qualifier("bgEntityManagerFactory") LocalContainerEntityManagerFactoryBean bgEntityManagerFactory) {
        // register the EntityManagerFactory with the repository helper so it can create EntityManagers
        if (bgEntityManagerFactory != null && bgEntityManagerFactory.getObject() != null) {
            BulkSkipAutoResendRepository.setEntityManagerFactory(bgEntityManagerFactory.getObject());
        }
        return new JpaTransactionManager(Objects.requireNonNull(bgEntityManagerFactory.getObject()));
    }
}