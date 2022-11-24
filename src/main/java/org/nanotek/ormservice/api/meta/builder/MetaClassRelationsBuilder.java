package org.nanotek.ormservice.api.meta.builder;

import java.util.Map;
import java.util.Optional;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.nanotek.ormservice.Holder;
import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.MetaDataAttribute;
import org.nanotek.ormservice.api.meta.MetaDataAttribute.AttributeType;
import org.nanotek.ormservice.api.meta.MetaRelation;
import org.nanotek.ormservice.api.meta.RelationType;
import org.nanotek.ormservice.api.meta.builder.MetaAttributeTypeDescriptionBuilder.ListTypeDefinitionBuilder;
import org.nanotek.ormservice.api.meta.builder.MetaAttributeTypeDescriptionBuilder.SingleTypeDefinitionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;

public class MetaClassRelationsBuilder {

	@Autowired 
	@Qualifier("classCache")
	Map<String, MetaClass> metaClassCache;
	
	//TODO: Handle BiDirectional Relations
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
					Optional<Builder<?>> ob = holder.get().map(b -> {
						return b
								.defineProperty(clsR.getSimpleName(), MetaRelationPropertyBuilder.build(rel.getType(), clsR))
								.annotateField(defineAnnotationType(rel,cl));
					});
					ob.ifPresent(b -> holder.put(b));
				});
				return holder.get().orElseThrow();
	}

	private AnnotationDescription defineAnnotationType(MetaRelation rel, ClassLoader cl) {
		switch(rel.getType()) {
		case ONE:
			return AnnotationDescription.Builder.ofType(OneToOne.class).build();
		case MANY:
			return AnnotationDescription.Builder.ofType(OneToMany.class).build();
		default:
			return null;
		}
	}

	private Class<?> loadClass(ClassLoader cl  , MetaClass mc) {
		try {
			return  cl.loadClass(mc.defaultFullClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public static String lower(String className) {
		return className.substring(0,1).toLowerCase().concat(className.substring(1));
	}
	
	class MetaRelationPropertyBuilder {
		public static TypeDefinition build(RelationType relationType , Class<?> clsR) {
			//TODO: Populate properties relation type.
			MetaDataAttribute metaAttribute = MetaDataAttribute
										.builder()
										.fieldName(clsR.getSimpleName())
										.clazz(clsR)
										.attributeType(AttributeType.Single).build();
			switch(relationType) {
				case ONE: 
					return TypeDescription.Generic.Builder.rawType(clsR).build();
				case  MANY:
					return  ListTypeDefinitionBuilder.build(metaAttribute);
			default:
				return null;
			}
		}
	}
	
}
	
