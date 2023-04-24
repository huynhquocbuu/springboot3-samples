package learning.java.springboot3.samples.domain01.config;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Configuration
public class Domain01DbMigration {

    @Autowired
    @Qualifier("domain01DataSource")
    private DataSource dataSource;

    @PostConstruct
    public void domain01Migrate() {
        String schemaLocation = "classpath:db/migration/domain01";
        Flyway flyway = Flyway
                .configure()
                .dataSource(dataSource)
                .locations(schemaLocation)
                .load();
        flyway.migrate();
    }

}
