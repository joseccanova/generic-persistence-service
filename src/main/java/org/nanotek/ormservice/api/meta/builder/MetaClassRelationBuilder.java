package org.nanotek.ormservice.api.meta.builder;

import java.util.Map;

import org.nanotek.ormservice.api.meta.MetaClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class MetaClassRelationBuilder {

	@Autowired DefaultListableBeanFactory beanFactory;
	
	@Autowired 
	@Qualifier("classCache")
	Map<String, MetaClass> metaClassCache;
	
	
}
