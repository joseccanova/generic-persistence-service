package org.nanotek.ormservice.api.meta.builder;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

import org.nanotek.ormservice.api.meta.MetaDataAttribute;

import net.bytebuddy.description.annotation.AnnotationDescription;

public class MetaAttributeAnnotationDescriptionBuilder {

	private MetaDataAttribute metaAttribute;
	
	public MetaAttributeAnnotationDescriptionBuilder(MetaDataAttribute a) {
		this.metaAttribute = a;
	}

	public static MetaAttributeAnnotationDescriptionBuilder prepare(MetaDataAttribute a ) {
		return new MetaAttributeAnnotationDescriptionBuilder(a);
	}
	
	public AnnotationDescription build() {
		switch (metaAttribute.getAttributeType()) {
		case Single:
			return  ColumnAnnotationBuilder.build(metaAttribute);
		case List: 
			return ManyToOneAnnotationBuilder.build(metaAttribute);
		default:
			return null;
		}
	}

	static class  ColumnAnnotationBuilder {
		public static AnnotationDescription build(MetaDataAttribute a) {
			return AnnotationDescription.Builder.ofType(Column.class).define("name", a.getColumnName()).build();
		}
	}
	
	static class ManyToOneAnnotationBuilder {
		public static AnnotationDescription build(MetaDataAttribute a) {
			return AnnotationDescription.Builder.ofType(ManyToOne.class).build();
		}
	}

	static class MapAnnotationBuilder {
	}

}
