package org.nanotek.ormservice.api.meta.service;

import java.util.Map;
import java.util.Optional;

import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.builder.MetaClassDynamicTypeBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Loaded;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

public class DynamicTypeService {

	@Autowired
	InjectionClassLoader classLoader;
	
	@Autowired
	MetaClassDynamicTypeBuilder classBuilder;
	
	@Autowired
	Map<String, MetaClass> classCache;
	
	public Optional<Loaded<?>> build(MetaClass metaClass){
		Builder<?> builder = classBuilder.build(metaClass);
		Loaded<?> loaded = builder.make().load(classLoader);
		classCache.put(metaClass.defaultFullClassName(), metaClass);
		return Optional.of(loaded);
	}

}
