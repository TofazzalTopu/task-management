package basico.task.management.datasources;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "assetEntityManagerFactory",
        transactionManagerRef = "assetTransactionManager",
        basePackages = {"basico.task.management.repository.assets"})
public class SecondaryAssetDatasourceConfiguration {


    @Bean(name = "assetDataSourceProperties")
    @ConfigurationProperties("assets.datasource")
    public DataSourceProperties secondaryDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean(name = "assetDataSource")
    @ConfigurationProperties("assets.datasource.configuration")
    public DataSource secondaryDataSource(@Qualifier("assetDataSourceProperties") DataSourceProperties secondaryAssetDataSourceProperties) {
        return secondaryAssetDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }


    @Bean(name = "assetEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
            EntityManagerFactoryBuilder secondaryEntityManagerFactoryBuilder, @Qualifier("assetDataSource") DataSource assetDataSource) {
        Map<String, String> secondaryJpaProperties = new HashMap<>();
        secondaryJpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        secondaryJpaProperties.put("hibernate.hbm2ddl.auto", "none");

        return secondaryEntityManagerFactoryBuilder
                .dataSource(assetDataSource)
                .packages("basico.task.management.model.assets")
                .persistenceUnit("assetDataSource")
                .properties(secondaryJpaProperties)
                .build();
    }


    @Bean(name = "assetTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("assetEntityManagerFactory") EntityManagerFactory secondaryEntityManagerFactory) {
        return new JpaTransactionManager(secondaryEntityManagerFactory);
    }
}