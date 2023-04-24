package learning.java.springboot3.samples.domain02.config;

import jakarta.annotation.PostConstruct;
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
        basePackages = "learning.java.springboot3.samples.domain02.data.repository",
        transactionManagerRef = "domain02TransactionManager",
        jdbcOperationsRef = "domain02JdbcOperations")
public class Domain02DbConfig {
    @Bean
    @ConfigurationProperties("spring.domain02.datasource")
    public DataSourceProperties domain02DataSourceProperties() {
        return new DataSourceProperties();
    }

    //1-
    @Bean
    @ConfigurationProperties("spring.domain02.datasource.hikari")
    public DataSource domain02DataSource() {
        return domain02DataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public NamedParameterJdbcTemplate domain02NamedParameterJdbcTemplate(
            @Qualifier("domain02DataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public JdbcTemplate domain02JdbcTemplate(
            @Qualifier("domain02NamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return namedParameterJdbcTemplate.getJdbcTemplate();
    }

    //2
    @Bean
    //@Primary
    NamedParameterJdbcOperations domain02JdbcOperations(
            @Qualifier("domain02NamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        //return new NamedParameterJdbcTemplate(dataSource);123
        //----
        return namedParameterJdbcTemplate;
    }
    //3
    @Bean
    //@Primary
    PlatformTransactionManager domain02TransactionManager(@Qualifier("domain02DataSource") DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }

    //flyway migration
//    @PostConstruct
//    public void domain02Migrate(@Qualifier("domain02DataSource") DataSource dataSource) {
//        String schemaLocation = "classpath:db/migration/domain02";
//        Flyway flyway = Flyway
//                .configure()
//                .dataSource(dataSource)
//                .locations(schemaLocation)
//                .load();
//        flyway.migrate();
//    }
}
