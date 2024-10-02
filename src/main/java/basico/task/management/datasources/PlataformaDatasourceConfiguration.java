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
        entityManagerFactoryRef = "plataformaEntityManagerFactory",
        transactionManagerRef = "plataformaTransactionManager",
        basePackages = {"basico.task.management.repository.plataforma"})
public class PlataformaDatasourceConfiguration {



    @Bean(name = "plataformaSourceProperties")
    @ConfigurationProperties("plataforma.datasource")
    public DataSourceProperties secondaryDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean(name = "plataformaDataSource")
    @ConfigurationProperties("plataforma.datasource.configuration")
    public DataSource secondaryDataSource(@Qualifier("plataformaSourceProperties") DataSourceProperties secondaryAssetDataSourceProperties) {
        return secondaryAssetDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }


    @Bean(name = "plataformaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
            EntityManagerFactoryBuilder secondaryEntityManagerFactoryBuilder, @Qualifier("plataformaDataSource") DataSource assetDataSource) {

        Map<String, String> secondaryJpaProperties = new HashMap<>();
        secondaryJpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        secondaryJpaProperties.put("hibernate.hbm2ddl.auto", "none");

        return secondaryEntityManagerFactoryBuilder
                .dataSource(assetDataSource)
                .packages("basico.task.management.model.plataforma")
                .persistenceUnit("plataformaDataSource")
                .properties(secondaryJpaProperties)
                .build();
    }


    @Bean(name = "plataformaTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("plataformaEntityManagerFactory") EntityManagerFactory secondaryEntityManagerFactory) {
        return new JpaTransactionManager(secondaryEntityManagerFactory);
    }


}
