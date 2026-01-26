package com.isentric.bulkgateway.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class ExtmtDBConfig {

    @Bean(name = "extDataSourceProperties")
    @ConfigurationProperties("spring.datasource.extmt")
    public DataSourceProperties extDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "extDataSource")
    @ConfigurationProperties("spring.datasource.extmt")
    public DataSource extDataSource() {
        return extDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name = "extmtEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean extEntityManagerFactory(
            @Qualifier("extDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        return builder
                .dataSource(dataSource)
                // no JPA entities required for native-only queries; keep packages minimal
                .packages("com.isentric.bulkgateway.extmt.model")
                .persistenceUnit("extmt")
                .properties(properties)
                .build();
    }

    @Bean(name = "extTransactionManager")
    public PlatformTransactionManager extTransactionManager(
            @Qualifier("extmtEntityManagerFactory") LocalContainerEntityManagerFactoryBean extEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(extEntityManagerFactory.getObject()));
    }
}
