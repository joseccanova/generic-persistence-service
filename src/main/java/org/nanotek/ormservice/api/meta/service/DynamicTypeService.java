package org.nanotek.ormservice.api.meta.service;

import java.util.Map;
import java.util.Optional;

import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.builder.MetaClassDynamicTypeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Loaded;

public class DynamicTypeService {

	@Autowired
	DefaultListableBeanFactory beanFactory;
	
	@Autowired
	MetaClassDynamicTypeBuilder classBuilder;
	
	@Autowired
	Map<String, MetaClass> classCache;
	
	public Optional<?> build(MetaClass metaClass){
		Builder<?> builder = classBuilder.build(metaClass);
		Loaded<?> loaded = builder.make().load(beanFactory.getBeanClassLoader());
		classCache.put(metaClass.defaultFullClassName(), metaClass);
		return Optional.of(loaded.getLoaded());
	}

	private void prepareRelationClasses() {
	}

	private boolean hasRelations(MetaClass metaClass) {
		return false;
	}
	
}
