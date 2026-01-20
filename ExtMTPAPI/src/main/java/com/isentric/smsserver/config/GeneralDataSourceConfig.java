package com.isentric.smsserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.isentric.smsserver.repository.general",
        entityManagerFactoryRef = "generalEntityManagerFactory",
        transactionManagerRef = "generalTransactionManager"
)
public class GeneralDataSourceConfig {
    // This configuration class enables JPA repositories for the general data source
}

