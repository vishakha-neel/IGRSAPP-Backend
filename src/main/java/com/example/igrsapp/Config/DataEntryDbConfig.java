package com.example.igrsapp.Config;


import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = {
        "com.example.igrsapp.repository.dataentry",
        "com.example.igrsapp.modals.dataEntry"
    },
    entityManagerFactoryRef = "dataEntryEntityManager",
    transactionManagerRef = "dataEntryTransactionManager"
)
public class DataEntryDbConfig {

    @Bean(name = "dataEntryDataSource")
    @ConfigurationProperties(prefix = "dataentry.datasource")
    public DataSource dataEntryDataSource() {
        return DataSourceBuilder.create()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean(name = "dataEntryEntityManager")
    public LocalContainerEntityManagerFactoryBean dataEntryEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataEntryDataSource") DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .packages("com.example.igrsapp.modals.dataentry")
                .persistenceUnit("dataentry")
                .properties(jpaProperties())
                .build();
    }

    @Bean(name = "dataEntryTransactionManager")
    public PlatformTransactionManager dataEntryTransactionManager(
            @Qualifier("dataEntryEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map<String, String> jpaProperties() {
        Map<String, String> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        props.put("hibernate.show_sql", "true");
        return props;
    }
}