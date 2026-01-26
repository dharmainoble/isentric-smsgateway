package com.isentric.bulkgateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class DatabaseHealthChecker implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(DatabaseHealthChecker.class);

    private final DataSource dataSource;

    // Inject the specific bulkgateway DataSource to avoid ambiguity when multiple DataSource beans exist
    public DatabaseHealthChecker(@Qualifier("bulkgatewayDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Running DatabaseHealthChecker...");
        try (Connection c = dataSource.getConnection(); Statement s = c.createStatement()) {
            try (ResultSet rs = s.executeQuery("SELECT 1")) {
                if (rs.next()) {
                    log.info("Database connectivity test succeeded: {}", rs.getInt(1));
                } else {
                    log.warn("Database connectivity test returned no rows");
                }
            }
        } catch (Exception ex) {
            log.error("Database connectivity test failed: {}", ex.getMessage(), ex);
            // rethrow so startup fails fast with a clear error
            throw new RuntimeException("Database connectivity test failed", ex);
        }
    }
}
