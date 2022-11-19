package org.nanotek.ormservice.api.meta.builder;

import javax.persistence.Id;

import net.bytebuddy.description.annotation.AnnotationDescription;

public class MetaClassIdentityAnnotationBuilder {

	public MetaClassIdentityAnnotationBuilder() {
	}

	public static AnnotationDescription build() {
		return AnnotationDescription.Builder.ofType(Id.class).build();
	}
	
}
