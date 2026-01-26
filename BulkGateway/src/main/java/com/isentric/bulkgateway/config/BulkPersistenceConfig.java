package com.isentric.bulkgateway.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:application.properties")
public class BulkPersistenceConfig {

    private final Environment env;

    public BulkPersistenceConfig(Environment env) {
        this.env = env;
    }

    @Bean(name = "bulkConfigDataSource")
    public DataSource bulkConfigDataSource() {
        return DataSourceBuilder.create()
                .url(env.getProperty("bulk_config.datasource.url"))
                .username(env.getProperty("bulk_config.datasource.username"))
                .password(env.getProperty("bulk_config.datasource.password"))
                .driverClassName(env.getProperty("bulk_config.datasource.driver-class-name", "org.h2.Driver"))
                .build();
    }

    @Bean(name = "bulkgatewayDataSource")
    @Primary
    public DataSource bulkgatewayDataSource() {
        return DataSourceBuilder.create()
                .url(env.getProperty("bulkgateway.datasource.url"))
                .username(env.getProperty("bulkgateway.datasource.username"))
                .password(env.getProperty("bulkgateway.datasource.password"))
                .driverClassName(env.getProperty("bulkgateway.datasource.driver-class-name", "org.h2.Driver"))
                .build();
    }

    // Provide a default EntityManagerFactoryBuilder so other configs can autowire it
    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        // Pass null for PersistenceUnitManager (not used here) to match available constructor
        return new EntityManagerFactoryBuilder(vendorAdapter, new java.util.HashMap<>(), null);
    }

    // NOTE: EntityManagerFactory beans are intentionally omitted to allow Spring Boot
    // auto-configuration to create the default EMF for the primary DataSource. Manual
    // creation previously caused Hibernate to initialize JDBC metadata too early and
    // resulted in a NullPointerException during bootstrap. If you need multiple
    // persistence units, we'll implement a robust multi-EMF configuration explicitly.
    //
    // Provide a primary entityManagerFactory bean for Spring Data JPA to use
    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("bulkgatewayDataSource") DataSource dataSource) {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.dialect", env.getProperty("bulkgateway.jpa.properties.hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect"));
        props.put("hibernate.hbm2ddl.auto", env.getProperty("bulkgateway.jpa.hibernate.ddl-auto", "none"));
        props.put("hibernate.temp.use_jdbc_metadata_defaults", env.getProperty("bulkgateway.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults", "false"));
        // Scan the entire base package so all subpackage entities (including bg.model) are discovered
        return builder
                .dataSource(dataSource)
                .packages("com.isentric.bulkgateway")
                .persistenceUnit("bulkgateway")
                .properties(props)
                .build();
    }
}
