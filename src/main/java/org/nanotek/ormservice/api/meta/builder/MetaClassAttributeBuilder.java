package org.nanotek.ormservice.api.meta.builder;

import java.util.Optional;

import org.nanotek.ormservice.api.meta.MetaDataAttribute;

import net.bytebuddy.dynamic.DynamicType.Builder;

public class MetaClassAttributeBuilder {

	public MetaClassAttributeBuilder() {
	}

	public static Builder<?> build(Optional<Builder<?>> builder , MetaDataAttribute att) {
		return builder
				.get()
				.defineProperty(att.getFieldName(), MetaAttributeTypeDescriptionBuilder.prepare(att).build())
				.annotateField(MetaAttributeAnnotationDescriptionBuilder.prepare(att).build());
	}
	
}
