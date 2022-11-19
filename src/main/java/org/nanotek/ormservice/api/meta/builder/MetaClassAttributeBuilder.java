package org.nanotek.ormservice.api.meta.builder;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.nanotek.ormservice.api.meta.ColumnTypeAnnotation;
import org.nanotek.ormservice.api.meta.MetaDataAttribute;

import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;

public class MetaClassAttributeBuilder {

	public MetaClassAttributeBuilder() {
	}

	public Builder<?> build(Optional<Builder<?>> builder , MetaDataAttribute att) {
		return builder
				.get()
				.defineProperty(att.getFieldName(), processPropertyType(att))
				.annotateField(verifyAnnotationType(att));
	}

	private TypeDefinition processPropertyType(MetaDataAttribute att) {
		switch (att.getAttributeType()) {
		case Single:
			return defineSimpleType(att);
		case List:
			return defineListType(att);
		case Map:
			return defineMapType(att);
		default:
			return null;
		}
	}

	private TypeDefinition defineSimpleType(MetaDataAttribute att) {
		return TypeDescription.Generic.Builder.rawType(att.getClazz()).build(); 
	}
	
	private TypeDefinition defineListType(MetaDataAttribute att) {
		return TypeDescription.Generic.Builder.parameterizedType(List.class, att.getClazz()).build(); 
	}
	
	
	private TypeDefinition defineMapType(MetaDataAttribute att) {
		return TypeDescription.Generic.Builder.parameterizedType(List.class, att.getClazz()).build(); 
	}

	private Annotation verifyAnnotationType(MetaDataAttribute att) {
		return Optional.of(att).map(a -> processAttributeType(a)).get();
	}

	private Annotation processAttributeType(MetaDataAttribute a) {
		 switch (a.getAttributeType()) {
		case Single:
			return columnType(a);
		case List:
			return listType(a);
		case Map:
			return mapType(a);
		default:
			return null;
		}
	}

	private Annotation mapType(MetaDataAttribute a) {
		return null;
	}

	//TODO: refine this ... will be moved for a second processing on class.
	private Annotation listType(MetaDataAttribute a) {
		return new ManyToOneType();
	}

	private Annotation columnType(MetaDataAttribute a) {
		var ct = new ColumnTypeAnnotation();
		ct.setName(a.getColumnName());
		ct.setNullable(!a.isRequired());
		return ct;
	}
	
}
