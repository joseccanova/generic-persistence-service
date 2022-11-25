package org.nanotek.ormservice.api.meta.service;

import java.util.Map;

import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.builder.MetaClassRelationsBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import net.bytebuddy.dynamic.loading.InjectionClassLoader;

public class DynamicTypeRelationService {

	@Autowired
	InjectionClassLoader classLoader;
	
	@Autowired
	Map<String, MetaClass> classCache;
	
	@Autowired
	MetaClassRelationsBuilder relationBuilder;
	
	public void processRelationClasses() {
		classCache.entrySet().forEach(e -> processRelationIfAny(e.getValue()));
	}

	private void processRelationIfAny(MetaClass metaClass) {
		metaClass
		.hasRelations()
		.ifPresent(r -> 
			 { 
				 var bd = relationBuilder.build(classLoader, metaClass);
				 bd.make().load(classLoader);
			 });
	}
	
}