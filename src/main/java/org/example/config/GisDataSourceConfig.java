package org.example.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "org.example.gis.repository",             // <-- только GIS
        entityManagerFactoryRef = "gisEntityManagerFactory",
        transactionManagerRef = "gisTransactionManager"
)
public class GisDataSourceConfig {

    @Bean(name = "gisDataSource")
    public DataSource gisDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/gis")
                .username("postgres")
                .password("postgres")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "gisEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean gisEntityManagerFactory(
            @Qualifier("gisDataSource") DataSource dataSource
    ) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("org.example.gis.entity");
        emf.setPersistenceUnitName("gis");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProperties.put("hibernate.show_sql", "true");

        emf.setJpaPropertyMap(jpaProperties);
        return emf;
    }

    @Bean(name = "gisTransactionManager")
    public PlatformTransactionManager gisTransactionManager(
            @Qualifier("gisEntityManagerFactory") EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }
}
