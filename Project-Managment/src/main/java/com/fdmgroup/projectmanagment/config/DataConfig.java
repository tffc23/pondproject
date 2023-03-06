package com.fdmgroup.projectmanagment.config;

import java.util.Properties;


import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

//@Configuration
public class DataConfig {
	@Bean
    public DataSource dataSource(){
       
        DriverManagerDataSource source = new DriverManagerDataSource();
        source.setDriverClassName("com.mysql.cj.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/project-management");
        source.setUsername("eric");
        source.setPassword("askme123*");
        return source;
    }
	
	@Bean
	  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
	    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
	    em.setDataSource(dataSource);
	    em.setPackagesToScan("packages.to.scan");

	    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    em.setJpaVendorAdapter(vendorAdapter);

	    Properties properties = new Properties();
	    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
	    properties.setProperty("hibernate.hbm2ddl.auto", "create");
	    em.setJpaProperties(properties);

	    return em;
	  }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource());
        return namedParameterJdbcTemplate;
    }
}
