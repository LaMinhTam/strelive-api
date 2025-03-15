package com.skilllease.configurations;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class FlywayMigrationExecutor {

    @Resource(lookup = "java:/PostgresDS")
    DataSource dataSource;

    @PostConstruct
    public void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .table("flyway_schema_history_agile_assessment")
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
    }

}
