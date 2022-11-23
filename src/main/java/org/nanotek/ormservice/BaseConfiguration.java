package org.nanotek.ormservice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.builder.MetaClassDynamicTypeBuilder;
import org.nanotek.ormservice.api.meta.service.DynamicTypeService;
import org.nanotek.ormservice.api.meta.service.TypeService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.transaction.PlatformTransactionManager;

import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import net.bytebuddy.dynamic.loading.MultipleParentClassLoader;

@SpringBootConfiguration
@EnableJpaRepositories(
		basePackages = 
	{"org.nanotek.ormservice.repository"}
		, transactionManagerRef = "transactionManager")
public class BaseConfiguration implements ApplicationContextAware{

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

	@Bean
	@Qualifier(value="classCache")
	public Map<String, MetaClass> metaClassCache (){
		return new HashMap<>();
	}

	
	@Bean(name = "entityManagerFactory")
	@Qualifier(value="entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Autowired DataSource dataSource , @Autowired DefaultPersistenceUnitManager pum ) {
		 Map<String, Object> jpaPropertiesMap = new HashMap<>();
	        jpaPropertiesMap.put(Environment.FORMAT_SQL, true);
	        jpaPropertiesMap.put(Environment.SHOW_SQL, true);
	        jpaPropertiesMap.put("hibernate.globally_quoted_identifiers", true);
	        jpaPropertiesMap.put("hibernate.transaction.flush_before_completion"  , true);
	        jpaPropertiesMap.put("hibernate.transaction.auto_close_session" , true);
			jpaPropertiesMap.put("hibernate.current_session_context_class" , "thread" );
			jpaPropertiesMap.put("hibernate.enable_lazy_load_no_trans" , true);
			jpaPropertiesMap.put("hibermate.hbm2ddl.auto" , "none" );
			jpaPropertiesMap.put("hibernate.default_schema", "public");
			jpaPropertiesMap.put("javax.persistence.schema-generation.scripts.action", "drop-and-create");
			jpaPropertiesMap.put("javax.persistence.schema-generation.scripts.create-target", "c:\\softwares\\connemat\\create-connemat.sql");
			jpaPropertiesMap.put("javax.persistence.schema-generation.scripts.drop-target" , "c:\\softwares\\connemat\\drop-connemat.sql");
			
		LocalContainerEntityManagerFactoryBean factory =  new LocalContainerEntityManagerFactoryBean();
		factory.setJpaPropertyMap(jpaPropertiesMap);
		factory.setDataSource(dataSource);
//		factory.setPersistenceUnitManager(pum);
		factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		factory.setPackagesToScan(new String []{"org.nanotek.ormservice.api.base"});
		return factory;
	}
	
	@Bean("transactionManager")
	@Qualifier(value="transactionManager")
	public PlatformTransactionManager defaultTransactionManager(
			@Autowired	@Qualifier("entityManagerFactory") EntityManagerFactory factory) {
		JpaTransactionManager tm =  new JpaTransactionManager(factory);
		tm.setPersistenceUnitName("pum");
		tm.setEntityManagerFactory(factory);
		return tm;
	}
	
	@Bean
	@Primary
	InjectionClassLoader injectionClassLoader() {
		InjectionClassLoader ic = new  MultipleParentClassLoader(Thread.currentThread().getContextClassLoader() 
				, Arrays.asList(getClass().getClassLoader() , CrudMethodMetadata.class.getClassLoader())  , 
				false);
		return ic;
	}
	
	@Bean 
	@Qualifier(value="myBf")
	public DefaultListableBeanFactory defaultListableBeanFactory(@Autowired InjectionClassLoader classLoader )
	{
		DefaultListableBeanFactory v = new DefaultListableBeanFactory();
		v.setParentBeanFactory(context);
		v.setBeanClassLoader(classLoader);
		return v;
	}
	
	ApplicationContext context;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	@Bean
	public MetaClassDynamicTypeBuilder getMetaClassClassBuilder() {
		return new MetaClassDynamicTypeBuilder();
	}

	@Bean
	@TypeService
	public DynamicTypeService getDynamicTypeService() {
		return new DynamicTypeService();
	}
	
}

