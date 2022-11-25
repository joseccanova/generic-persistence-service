package org.nanotek.ormservice.api.meta.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.MetaDataAttribute;
import org.nanotek.ormservice.api.meta.MetaRelation;
import org.nanotek.ormservice.api.meta.RelationType;

import lombok.Getter;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Loaded;
import net.bytebuddy.jar.asm.Opcodes;
/**
 * This class could be used to validate the MetaClass -> i.e. its an abstraction of provide a single entry point for the validation.
 * @author Jose.Canova
 *
 * @param <T>
 */
//TODO: Define an abstraction for a model based on definition of the class.
@Valid
public class MetaModel <T extends MetaClass> {
	
	@NotNull
	private T clazz;
	
	@Getter
	private Map<String , Loaded<?>> attributeRegistry;
	
	private Map<String , Loaded<?>> relationRegistry;

	private ClassLoader classLoader;

	private MetaModel(T clazz , ClassLoader classLoader) {
		this.clazz = clazz;
		this.classLoader = classLoader;
		attributeRegistry = new HashMap<>();
		relationRegistry = new HashMap<>();
	}
	
	public static <T extends MetaClass> MetaModel<?> intialize(T clazz ,  ClassLoader classLoader) {
		return new MetaModel<>(clazz , classLoader);
	}
	
	public MetaModel<?> defineAttribute(Optional<MetaDataAttribute> provider) {
		provider
			.ifPresentOrElse(a->registryAttributeInterface(a) , RuntimeException::new);
		return this;
	}
	
	private void registryAttributeInterface(MetaDataAttribute att) {
		validateAttribute(att)
		.ifPresent(e -> attributeRegistry.put(e.getFieldName() , createAccessorMutatorInterface(e)));
		clazz.addMetaAttribute(att);
	}

	private Loaded<?> createAccessorMutatorInterface(MetaDataAttribute a) {
		return new ByteBuddy(ClassFileVersion.JAVA_V11)
		.makeInterface()
		.defineMethod(normalizeNameAccessor(a.getFieldName()), withReturnTypeDefinition(a.getClazz()) , Opcodes.ACC_PUBLIC)
		.withoutCode()
		.make().load(classLoader);
	}

	private TypeDefinition withReturnTypeDefinition(Class<?> clazz2) {
		return TypeDescription.Generic.Builder.of(clazz2).build();
	}

	private String normalizeNameAccessor(String fieldName) {
		String met = fieldName.substring(0, 1).toUpperCase().concat(fieldName.substring(1));
		return new StringBuilder().append("get").append(met).toString();
	}

	//TODO: implement validation returning optional or empty
	private Optional<MetaDataAttribute> validateAttribute(MetaDataAttribute att) {
		return Optional.of(att);
	}

	public MetaModel<?> defineRelation(T type1 , RelationType relation1) {
		MetaRelation mr = MetaRelation.builder().to(type1).type(relation1).build();
		clazz
		.hasRelations()
		.ifPresentOrElse(rl -> rl.add(mr), ()->{
			clazz.setRelations(ArrayList::new).add(mr);
		});
		return this;
	}
	
	
	public Set<?> validate(Validator validator){
		return validator.validate(clazz, Default.class);
	}
	
}