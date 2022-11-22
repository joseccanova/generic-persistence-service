package org.nanotek.ormservice.api.meta.builder;

import static net.bytebuddy.matcher.ElementMatchers.named;

import java.lang.annotation.Annotation;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import org.nanotek.ormservice.Base;
import org.nanotek.ormservice.Holder;
import org.nanotek.ormservice.api.meta.EntityAnnotation;
import org.nanotek.ormservice.api.meta.MappedSuperClassAnnotation;
import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.TableAnnotation;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.FixedValue;

//TODO: remake this class ... code is weird.
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
//		processors.stream().forEach(p -> p.process(cm11));
		String myClassName = cm11.getClassName();
		log.debug("class name " + myClassName);
		AnnotationDescription rootAnnotation =  AnnotationDescription.Builder.ofType(JsonRootName.class)
				.define("value", myClassName)
				.build();
		
//		Class<?> idClass = getIdClass(cm11);
		//TODO:generate another strategy to produce an ID for the EntityClass.
		Class<?> idClass = getIdClass(cm11).orElse(Long.class);
		TypeDefinition td = TypeDescription.Generic.Builder.parameterizedType(Base.class  , idClass).build();
		Builder<?> bd = new ByteBuddy(ClassFileVersion.JAVA_V8)
						.subclass(td)
						.name(PACKAGE+myClassName)
						.annotateType(rootAnnotation)
						.annotateType(processEntityType(cm11))
						.annotateType(processTableType(cm11))
						.withHashCodeEquals()
						.withToString()
						.method(named("getMetaClass"))
						.intercept(FixedValue.value(MetaClass.class.cast(cm11)));
		  bd = processMetaIdentity(bd , cm11);
		  return  processAttributes(bd , cm11);
	}

	private Builder processMetaIdentity(Builder<?> bd, MetaClass cm11) {
		return bd.defineProperty(cm11.getIdentity().getName(), MetaClassIdentityBuilder.prepare(cm11).build()).annotateField(MetaClassIdentityAnnotationBuilder.build());
	}

	@SuppressWarnings("rawtypes")
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
		switch(cm.getClassType()) {
		case EntityClass:
			return AnnotationDescription.Builder.ofType(Entity.class).define("name", cm.getClassName()).build();
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
