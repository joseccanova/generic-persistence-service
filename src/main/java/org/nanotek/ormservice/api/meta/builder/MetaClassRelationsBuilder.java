package org.nanotek.ormservice.api.meta.builder;

import java.util.Map;

import org.hibernate.mapping.MetaAttribute;
import org.nanotek.ormservice.Base;
import org.nanotek.ormservice.Holder;
import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.MetaDataAttribute;
import org.nanotek.ormservice.api.meta.MetaRelation;
import org.nanotek.ormservice.api.meta.RelationType;
import org.nanotek.ormservice.api.meta.builder.MetaAttributeTypeDescriptionBuilder.SingleTypeDefinitionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;

public class MetaClassRelationsBuilder {

	@Autowired 
	@Qualifier("classCache")
	Map<String, MetaClass> metaClassCache;
	
	public Builder<?> build(ClassLoader cl , MetaClass metaClass){
				Class<?> cls = loadClass(cl , metaClass);
				TypeDefinition td = TypeDescription.ForLoadedType.of(cls);
				Builder<?> bd = new ByteBuddy(ClassFileVersion.JAVA_V8)
								.subclass(td)
								.name(metaClass.defaultFullClassName()+"Relation");
				var holder = new Holder<Builder<?>>().put(bd);
				metaClass
				.getMetaRelations()
				.stream()
				.forEach(rel -> {
					Class<?> clsR  = loadClass(cl , rel.getTo());
					//holder.get().map(b ->b.defineProperty(lower(clsR.getSimpleName()), clsR));
				});
				
				return null;
	}

	private Class<?> loadClass(ClassLoader cl  , MetaClass mc) {
		try {
			return  cl.loadClass(mc.defaultFullClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	//TODO: to be continued..
	static class MetaRelationPropertyBuilder {
		public static TypeDescription build(MetaClass metaClass , MetaRelation relation , RelationType relationType) {
			MetaDataAttribute metaAttribute = MetaDataAttribute.builder().build();
			SingleTypeDefinitionBuilder.build(metaAttribute);
			return null;
		}

		public static String lower(String className) {
			return className.substring(0,1).toLowerCase().concat(className.substring(1));
		}
	}
	
	static class MetaRelationAnnotationBuilder {
	}
	
}
	
