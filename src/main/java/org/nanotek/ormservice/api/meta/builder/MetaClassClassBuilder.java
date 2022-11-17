package org.nanotek.ormservice.api.meta.builder;

import static net.bytebuddy.matcher.ElementMatchers.named;

import java.lang.annotation.Annotation;

import org.apache.naming.factory.BeanFactory;
import org.nanotek.ormservice.Base;
import org.nanotek.ormservice.api.meta.EntityAnnotation;
import org.nanotek.ormservice.api.meta.MappedSuperClassAnnotation;
import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.TableAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

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
public class MetaClassClassBuilder {

	public static final String PACKAGE  = "org.nanotek.meta.entities.";
	
	@Autowired
	private DefaultListableBeanFactory beanFactory;
	
	public MetaClassClassBuilder() {
	}
	
	public Builder build(MetaClass cm11, ClassLoader classLoader) {
//		processors.stream().forEach(p -> p.process(cm11));
		String myClassName = cm11.getClassName();
		log.debug("class name " + myClassName);
		AnnotationDescription rootAnnotation =  AnnotationDescription.Builder.ofType(JsonRootName.class)
				.define("value", myClassName)
				.build();
		
//		Class<?> idClass = getIdClass(cm11);
		//TODO:generate another strategy to produce an ID for the EntityClass.
		Class<?> idClass = getIdClass(cm11);
		TypeDefinition td = TypeDescription.Generic.Builder.parameterizedType(Base.class  , idClass).build();
		Builder bd = new ByteBuddy(ClassFileVersion.JAVA_V8)
						.subclass(td)
						.name(PACKAGE+myClassName)
						.annotateType(rootAnnotation)
						.annotateType(processEntityType(cm11))
						.annotateType(processTableType(cm11))
						.withHashCodeEquals()
						.withToString()
						.method(named("getMetaClass"))
						.intercept(FixedValue.value(MetaClass.class.cast(cm11)));
			return bd;
	}

	private Annotation processTableType(MetaClass cm11) {
		return new TableAnnotation(cm11.getTableName());
	}

	private Annotation processEntityType (MetaClass cm)
	{
		switch(cm.getClassType()) {
		case EntityClass:
			return new EntityAnnotation(cm.getClassName());
		case MappedSuperClass:
			return new MappedSuperClassAnnotation();
		default: 
			return null;
		}
	}
	
	private Class<?> getIdClass(MetaClass cm11) {
		return Long.class;
	}

}
