package com.scripty.service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnBean(name = "backupJdbcTemplate")
public class DatabaseBackupService {

    private static final Logger log = LoggerFactory.getLogger(DatabaseBackupService.class);

    private static final List<String> TABLES_IN_ORDER = List.of(
        "user", "authority", "project", "actor", "scene", "person", "block"
    );

    private static final List<String> TABLES_IN_REVERSE = List.of(
        "block", "person", "scene", "actor", "project", "authority", "user"
    );

    private final JdbcTemplate sourceJdbc;
    private final JdbcTemplate backupJdbc;

    public DatabaseBackupService(
            JdbcTemplate jdbcTemplate,
            @Qualifier("backupJdbcTemplate") JdbcTemplate backupJdbcTemplate) {
        this.sourceJdbc = jdbcTemplate;
        this.backupJdbc = backupJdbcTemplate;
    }

    @Scheduled(cron = "${backup.cron:0 0 3 * * *}")
    public void scheduledBackup() {
        runBackup();
    }

    public BackupResult runBackup() {
        log.info("Starting database backup");
        long start = System.currentTimeMillis();
        int totalRows = 0;

        try {
            createTablesIfNeeded();
            disableForeignKeyChecks();

            for (String table : TABLES_IN_REVERSE) {
                backupJdbc.execute("DELETE FROM `" + table + "`");
            }

            for (String table : TABLES_IN_ORDER) {
                int rows = copyTable(table);
                totalRows += rows;
                log.info("Backed up {} rows from table '{}'", rows, table);
            }

            enableForeignKeyChecks();

            long duration = System.currentTimeMillis() - start;
            log.info("Database backup completed: {} total rows in {}ms", totalRows, duration);
            return new BackupResult(true, totalRows, duration, null);
        } catch (Exception e) {
            enableForeignKeyChecks();
            log.error("Database backup failed", e);
            long duration = System.currentTimeMillis() - start;
            return new BackupResult(false, totalRows, duration, e.getMessage());
        }
    }

    private void createTablesIfNeeded() {
        backupJdbc.execute("""
            CREATE TABLE IF NOT EXISTS `user` (
                id int NOT NULL AUTO_INCREMENT,
                username varchar(20) NOT NULL,
                `password` varchar(100) NOT NULL,
                enabled tinyint(1) NOT NULL,
                first_name varchar(30) NOT NULL,
                last_name varchar(30) NOT NULL,
                PRIMARY KEY (id),
                INDEX idx_user_username (username)
            )""");

        backupJdbc.execute("""
            CREATE TABLE IF NOT EXISTS authority (
                username varchar(20) NOT NULL,
                authority varchar(20) NOT NULL,
                PRIMARY KEY (username, authority),
                INDEX idx_authority_username (username)
            )""");

        backupJdbc.execute("""
            CREATE TABLE IF NOT EXISTS project (
                id int NOT NULL AUTO_INCREMENT,
                title varchar(100) NOT NULL,
                PRIMARY KEY (id)
            )""");

        backupJdbc.execute("""
            CREATE TABLE IF NOT EXISTS scene (
                id int NOT NULL AUTO_INCREMENT,
                `order` int NOT NULL,
                `name` varchar(255) NOT NULL,
                project_id int NOT NULL,
                PRIMARY KEY (id),
                FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
            )""");

        backupJdbc.execute("""
            CREATE TABLE IF NOT EXISTS actor (
                id int NOT NULL AUTO_INCREMENT,
                first_name varchar(30) NOT NULL,
                last_name varchar(30) NULL,
                phone varchar(20) NULL,
                email varchar(30) NULL,
                PRIMARY KEY (id)
            )""");

        backupJdbc.execute("""
            CREATE TABLE IF NOT EXISTS person (
                id int NOT NULL AUTO_INCREMENT,
                `name` varchar(60) NOT NULL,
                full_name varchar(60) NOT NULL,
                actor_id int NULL,
                project_id int NOT NULL,
                PRIMARY KEY (id),
                FOREIGN KEY (actor_id) REFERENCES actor(id) ON DELETE SET NULL,
                FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
            )""");

        backupJdbc.execute("""
            CREATE TABLE IF NOT EXISTS `block` (
                id int NOT NULL AUTO_INCREMENT,
                `order` int NOT NULL,
                content TEXT NOT NULL,
                person_id int NULL,
                scene_id int NOT NULL,
                PRIMARY KEY (id),
                FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE SET NULL,
                FOREIGN KEY (scene_id) REFERENCES scene(id) ON DELETE CASCADE
            )""");
    }

    private int copyTable(String table) {
        List<Object[]> rows = sourceJdbc.query(
            "SELECT * FROM `" + table + "`",
            (ResultSet rs, int rowNum) -> {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();
                Object[] row = new Object[colCount];
                for (int i = 1; i <= colCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                return row;
            }
        );

        if (rows.isEmpty()) {
            return 0;
        }

        String[] columnNames = sourceJdbc.query(
            "SELECT * FROM `" + table + "` LIMIT 1",
            rs -> {
                ResultSetMetaData meta = rs.getMetaData();
                String[] names = new String[meta.getColumnCount()];
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    names[i - 1] = meta.getColumnName(i);
                }
                return names;
            }
        );

        StringJoiner cols = new StringJoiner(", ");
        StringJoiner placeholders = new StringJoiner(", ");
        for (String col : columnNames) {
            cols.add("`" + col + "`");
            placeholders.add("?");
        }

        String insertSql = "INSERT INTO `" + table + "` (" + cols + ") VALUES (" + placeholders + ")";
        backupJdbc.batchUpdate(insertSql, rows);

        return rows.size();
    }

    private void disableForeignKeyChecks() {
        backupJdbc.execute("SET FOREIGN_KEY_CHECKS = 0");
    }

    private void enableForeignKeyChecks() {
        try {
            backupJdbc.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (Exception e) {
            log.warn("Failed to re-enable foreign key checks", e);
        }
    }

    public record BackupResult(boolean success, int totalRows, long durationMs, String error) {}
}
