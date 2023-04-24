package learning.java.springboot3.samples.domain01.config;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories(
        basePackages = "learning.java.springboot3.samples.domain01.data.repository",
        transactionManagerRef = "domain01TransactionManager",
        jdbcOperationsRef = "domain01JdbcOperations")
public class Domain01DbConfig {
    @Bean
    @ConfigurationProperties("spring.domain01.datasource")
    public DataSourceProperties domain01DataSourceProperties() {
        return new DataSourceProperties();
    }

    //1-
    @Bean(name = "domain01DataSource")
    @ConfigurationProperties("spring.domain01.datasource.hikari")
    public DataSource domain01DataSource() {
        return domain01DataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public NamedParameterJdbcTemplate domain01NamedParameterJdbcTemplate(
            @Qualifier("domain01DataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public JdbcTemplate domain01JdbcTemplate(
            @Qualifier("domain01NamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return namedParameterJdbcTemplate.getJdbcTemplate();
    }

    //2
    @Bean
    @Primary
    NamedParameterJdbcOperations domain01JdbcOperations(
            @Qualifier("domain01NamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        //return new NamedParameterJdbcTemplate(dataSource);123
        //----
        return namedParameterJdbcTemplate;
    }
    //3
    @Bean
    @Primary
    PlatformTransactionManager domain01TransactionManager(@Qualifier("domain01DataSource") DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }

    //flyway migration
//    @PostConstruct
//    public void domain01Migrate() {
//        String schemaLocation = "classpath:db/migration/domain01";
//        Flyway flyway = Flyway
//                .configure()
//                .dataSource(domain01DataSource())
//                .locations(schemaLocation)
//                .load();
//        flyway.migrate();
//    }
}
