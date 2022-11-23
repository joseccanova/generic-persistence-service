package org.nanotek.ormservice.api.meta.service;

import java.util.Map;
import java.util.Optional;

import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.builder.MetaClassDynamicTypeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import net.bytebuddy.dynamic.DynamicType.Builder;

public class DynamicTypeService {

	@Autowired
	DefaultListableBeanFactory beanFactory;
	
	@Autowired
	MetaClassDynamicTypeBuilder classBuilder;
	
	@Autowired
	Map<Class<?>, MetaClass> classCache;
	
	public Optional<?> build(MetaClass metaClass){
		if(hasRelations(metaClass))
			prepareRelationClasses(metaClass);
		Builder<?> builder = classBuilder.build(metaClass);
		return Optional.empty();
	}

	private void prepareRelationClasses(MetaClass metaClass) {
	}

	private boolean hasRelations(MetaClass metaClass) {
		return false;
	}
	
}
