package org.nanotek.ormservice.api.meta.builder;

import static net.bytebuddy.matcher.ElementMatchers.named;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import org.nanotek.ormservice.Base;
import org.nanotek.ormservice.Holder;
import org.nanotek.ormservice.api.meta.MetaClass;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.FixedValue;

/**
 * Process the creation of the ByteBuddy Builder that will generate the class definition.
 * 
 * @author Jose.Canova
 *
 */
@Slf4j
public class MetaClassDynamicTypeBuilder {

	public static final String PACKAGE  = "org.nanotek.meta.entities.";
	
	public MetaClassDynamicTypeBuilder() {
	}
	
	
	public Builder<?> build(MetaClass cm11) {
		String myClassName = cm11.getClassName();
		log.debug("class name " + myClassName);
		AnnotationDescription rootAnnotation =  AnnotationDescription.Builder.ofType(JsonRootName.class)
				.define("value", myClassName)
				.build();
		
		Class<?> idClass = getIdClass(cm11).orElse(Long.class);
		TypeDefinition td = TypeDescription.Generic.Builder.parameterizedType(Base.class  , idClass).build();
		Builder<?> bd = new ByteBuddy(ClassFileVersion.JAVA_V8)
						.subclass(td)
						.name(PACKAGE+myClassName)
						.annotateType(rootAnnotation)
						.annotateType(getJpaAnnotations(cm11))
						.withHashCodeEquals()
						.withToString()
						.method(named("getMetaClass"))
						.intercept(FixedValue.value(MetaClass.class.cast(cm11)));
		  bd = processMetaIdentity(bd , cm11);
		  return  processAttributes(bd , cm11);
	}


	private AnnotationDescription[] getJpaAnnotations(MetaClass cm11){
		var list = List.<AnnotationDescription>of();
		list.add(processEntityType(cm11));
		list.add(processTableType(cm11));
		list.add(processInheritance(cm11));
		return list.toArray(new AnnotationDescription[list.size()]);
	}
	
	private AnnotationDescription processInheritance(MetaClass metaClass) {
		return AnnotationDescription.Builder.ofType(Inheritance.class).build();
	}


	private Builder<?> processMetaIdentity(Builder<?> bd, MetaClass cm11) {
		return bd.defineProperty(cm11.getIdentity().getName(), MetaClassIdentityBuilder.prepare(cm11).build()).annotateField(MetaClassIdentityAnnotationBuilder.build());
	}

	private Builder<?> processAttributes(Builder<?> bd, MetaClass cm11) {
		var holder = new Holder<Builder<?>>().put(bd);
		cm11
		.getMetaAttributes()
		.stream()
		.forEach(att -> { 
			holder.put(MetaClassAttributeBuilder.build(holder.get(), att));
		});
		return holder.get().orElseThrow();
	}

	private AnnotationDescription processTableType(MetaClass cm11) {
		return AnnotationDescription.Builder.ofType(Table.class).define("name", cm11.getTableName()).build();
	}

	private AnnotationDescription processEntityType (MetaClass cm)
	{
		if (cm.getMetaRelations() != null && cm.getMetaRelations().size() > 0)
			return AnnotationDescription.Builder.ofType(MappedSuperclass.class).build();

		switch(cm.getClassType()) {
		case EntityClass:
			AnnotationDescription.Builder.ofType(Entity.class).define("name", cm.getClassName()).build();
		case MappedSuperClass:
			return AnnotationDescription.Builder.ofType(MappedSuperclass.class).build();
		default: 
			return null;
		}
	}
	
	//TODO: refactor to support composite id
	private Optional<Class<?>> getIdClass(MetaClass cm11) {
		return Optional
				.ofNullable(cm11.getIdentity())  
				.map(i -> i.getAttributes())
				.map(as -> as.get(0))
				.map(a -> a.getClazz());
	}

}
