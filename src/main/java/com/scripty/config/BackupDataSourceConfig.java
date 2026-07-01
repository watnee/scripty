package com.scripty.config;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(name = "backup.datasource.url")
public class BackupDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "backup.datasource")
    public DataSource backupDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcTemplate backupJdbcTemplate(DataSource backupDataSource) {
        return new JdbcTemplate(backupDataSource);
    }
}
