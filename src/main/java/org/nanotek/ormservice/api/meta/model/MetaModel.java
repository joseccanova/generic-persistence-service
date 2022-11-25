package org.nanotek.ormservice.api.meta.model;

import java.util.ArrayList;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.MetaDataAttribute;
import org.nanotek.ormservice.api.meta.MetaRelation;
import org.nanotek.ormservice.api.meta.RelationType;

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

	private MetaModel(T clazz) {
		this.clazz = clazz;
	}
	
	public MetaModel<?> intialize(T clazz) {
		return new MetaModel<>(clazz);
	}
	
	public MetaModel<?> defineAttribute(MetaDataAttribute att) {
		clazz.addMetaAttribute(att);
		return this;
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
	
}
