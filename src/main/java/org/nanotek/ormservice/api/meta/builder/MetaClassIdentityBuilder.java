package org.nanotek.ormservice.api.meta.builder;

import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.MetaIdentity;

import net.bytebuddy.description.type.TypeDefinition;

public class MetaClassIdentityBuilder {

	private MetaIdentity metaIdentity;

	private MetaClassIdentityBuilder(MetaClass mcls) {
		this.metaIdentity = mcls.getIdentity();
	}
	
	public static MetaClassIdentityBuilder prepare(MetaClass mcls) {
		return new MetaClassIdentityBuilder(mcls);
	}
	
	public TypeDefinition build() {
		switch(metaIdentity.getType()) {
			case Identity:
				return IdentityTypeDefinitionBuilder.build(metaIdentity);
			case Embeddable:
				return null;
			case Single:
				return null;
			default:
				return null;
		}
	}
	
	static class IdentityTypeDefinitionBuilder {
		static TypeDefinition build(MetaIdentity identity) {
			assert(identity.getAttributes().size() == 1);
			return MetaAttributeTypeDescriptionBuilder.prepare(identity.getAttributes().get(0)).build();
		}
	}
	
}
