package org.nanotek.ormservice.api.meta.builder;

import java.util.List;

import org.nanotek.ormservice.api.meta.MetaDataAttribute;

import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;

public class MetaAttributeTypeDescriptionBuilder {

	private MetaDataAttribute metaAttribute;
	
	private MetaAttributeTypeDescriptionBuilder(MetaDataAttribute att) {
		this.metaAttribute = att;
	}

	public static MetaAttributeTypeDescriptionBuilder prepare(MetaDataAttribute metaAttribute) {
		return new MetaAttributeTypeDescriptionBuilder (metaAttribute);
	}
	
	public TypeDefinition build() {
			switch (metaAttribute.getAttributeType()) {
			case Single:
				return  SingleTypeDefinitionBuilder.build(metaAttribute);
			case List:
				return ListTypeDefinitionBuilder.build(metaAttribute);
			case Map:
				return defineMapType(metaAttribute);
			default:
				return null;
			}
		}

	private TypeDescription defineMapType(MetaDataAttribute metaAttribute2) {
		return null;
	}


	static class SingleTypeDefinitionBuilder{
		public static TypeDefinition build(MetaDataAttribute att) {
			return TypeDescription.Generic.Builder.of(att.getClazz()).build();
		}
	}
	
	static class ListTypeDefinitionBuilder{
		public static TypeDefinition build(MetaDataAttribute att) {
			return TypeDescription.Generic.Builder.parameterizedType(List.class, att.getClazz()).build();
		}
	}
}
