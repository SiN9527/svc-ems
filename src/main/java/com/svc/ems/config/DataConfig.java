package com.svc.ems.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.svc.ems.repo")
public class DataConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl; // 從 application.properties 讀取資料庫連線 URL

    @Value("${spring.datasource.username}")
    private String dbUsername; // 從 application.properties 讀取資料庫使用者名稱

    @Value("${spring.datasource.password}")
    private String dbPassword; // 從 application.properties 讀取資料庫密碼

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName; // 從 application.properties 讀取資料庫驅動類別

    @Bean
    public DataSource dataSource() {
        // 建立 HikariCP 連接池設定
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dbUrl); // 設定資料庫 URL
        hikariConfig.setUsername(dbUsername); // 設定使用者名稱
        hikariConfig.setPassword(dbPassword); // 設定密碼
        hikariConfig.setDriverClassName(driverClassName); // 設定 JDBC 驅動類別
        hikariConfig.setMaximumPoolSize(10); // 最大連接數為 10
        hikariConfig.setMinimumIdle(2); // 最少空閒連接數為 2
        hikariConfig.setConnectionTimeout(30000); // 連接逾時時間為 30 秒
        hikariConfig.setIdleTimeout(600000); // 空閒連接最大閒置時間為 10 分鐘
        hikariConfig.setMaxLifetime(1800000); // 連接最大存活時間為 30 分鐘
        return new HikariDataSource(hikariConfig); // 傳回 HikariDataSource 物件
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.svc.ems.entity");
        em.setPersistenceProviderClass(org.hibernate.jpa.HibernatePersistenceProvider.class); // 新增 PersistenceProvider 設定

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        jpaProperties.put("hibernate.show_sql", true);
        em.setJpaProperties(jpaProperties);

        return em;
    }
}

