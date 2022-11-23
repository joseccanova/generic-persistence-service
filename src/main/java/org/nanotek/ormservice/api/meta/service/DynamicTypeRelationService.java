package org.nanotek.ormservice.api.meta.service;

import java.util.Map;
import java.util.Map.Entry;

import org.nanotek.ormservice.api.meta.MetaClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class DynamicTypeRelationService {

	@Autowired
	DefaultListableBeanFactory beanFactory;
	
	@Autowired
	Map<String, MetaClass> classCache;
	
	public void processRelationClasses() {
		ClassLoader loader = beanFactory.getBeanClassLoader();
		classCache.entrySet().forEach(e -> processRelationIfAny(e));
	}

	private Object processRelationIfAny(Entry<String, MetaClass> e) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
