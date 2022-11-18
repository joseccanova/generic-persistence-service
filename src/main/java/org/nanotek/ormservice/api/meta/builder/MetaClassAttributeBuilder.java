package org.nanotek.ormservice.api.meta.builder;

import java.util.Optional;

import org.nanotek.ormservice.api.meta.MetaDataAttribute;

import net.bytebuddy.dynamic.DynamicType.Builder;

public class MetaClassAttributeBuilder {

	public MetaClassAttributeBuilder() {
	}

	public Builder build(Optional<Builder> builder , MetaDataAttribute att) {
		return builder.get().defineProperty(att.getFieldName(), att.getClass());
	}
}
