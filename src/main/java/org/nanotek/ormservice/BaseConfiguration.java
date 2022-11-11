package org.nanotek.ormservice;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootConfiguration
@EnableJpaRepositories(
		basePackages = 
	{"org.nanotek.ormservice.repository"}
		, transactionManagerRef = "transactionManager")
public class BaseConfiguration {

	/*
	 * @Bean(name="Reflections")
	 * 
	 * @Qualifier(value="Reflections") public Reflections getReflections() { return
	 * new Reflections(new ConfigurationBuilder()
	 * .setUrls(ClasspathHelper.forPackage("org.nanotek.ormservice.api"))
	 * .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())); }
	 */
	
	@Primary
	@Bean(name = "defaultDataSourceProperties")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSourceProperties defaultDataSourceProperties() {
		DataSourceProperties dsp = new DataSourceProperties();
		dsp.setName("ormservice-data-source");
		return dsp;
	}
	
	@Primary
	@Bean
	public DataSource defaultDataSource(
			@Qualifier("defaultDataSourceProperties") DataSourceProperties dataSourceProperties) {
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}
	
	
	@Bean 
	public DefaultPersistenceUnitManager getPumManager(@Autowired DataSource dataSource) {
		DefaultPersistenceUnitManager pum = new DefaultPersistenceUnitManager();
		pum.setDefaultJtaDataSource(dataSource);
		pum.setPackagesToScan("org.nanotek.ormservice.api.base");
		return pum;
	}
	
	
	@Bean(name = "entityManagerFactory")
	@Qualifier(value="entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Autowired DataSource dataSource , @Autowired DefaultPersistenceUnitManager pum ) {
		LocalContainerEntityManagerFactoryBean factory =  new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setPersistenceUnitManager(pum);
		factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		return factory;
	}
	
	@Bean("transactionManager")
	@Qualifier(value="transactionManager")
	public PlatformTransactionManager defaultTransactionManager(
			@Autowired	@Qualifier("entityManagerFactory") EntityManagerFactory factory) {
		return new JpaTransactionManager(factory);
	}


}

