package com.isentric.smsserver.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.isentric.smsserver.repository.avatar",
        entityManagerFactoryRef = "avatarEntityManagerFactory",
        transactionManagerRef = "avatarTransactionManager"
)
public class DataSourceConfig {

    // Avatar Database Configuration (Primary)
    @Primary
    @Bean(name = "avatarDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.avatar")
    public DataSource avatarDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = {"avatarEntityManagerFactory", "entityManagerFactory"})
    public LocalContainerEntityManagerFactoryBean avatarEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("avatarDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.isentric.smsserver.model.avatar")
                .persistenceUnit("avatar")
                .build();
    }

    @Primary
    @Bean(name = {"avatarTransactionManager", "transactionManager"})
    public PlatformTransactionManager avatarTransactionManager(
            @Qualifier("avatarEntityManagerFactory") LocalContainerEntityManagerFactoryBean avatarEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(avatarEntityManagerFactory.getObject()));
    }

    // General Database Configuration (Secondary)
    @Bean(name = "generalDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.general")
    public DataSource generalDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "generalEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean generalEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("generalDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.isentric.smsserver.model.general")
                .persistenceUnit("general")
                .build();
    }

    @Bean(name = "generalTransactionManager")
    public PlatformTransactionManager generalTransactionManager(
            @Qualifier("generalEntityManagerFactory") LocalContainerEntityManagerFactoryBean generalEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(generalEntityManagerFactory.getObject()));
    }
}

