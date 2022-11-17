package org.nanotek.ormservice.api.meta;

import java.lang.annotation.Annotation;
import java.util.Optional;

import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class TableAnnotation extends MetaAnnotation implements Table{

	@Getter
	@Setter
	private String name;
	
	@Getter
	@Setter
	private String catalog;

	@Getter
	@Setter
	private String schema;
	
	public TableAnnotation(String name) {
		this.name = name;
	}
	
	@Override
	public Class<? extends Annotation> annotationType() {
		return Table.class;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String catalog() {
		return Optional.ofNullable(catalog).orElse("");
	}

	@Override
	public String schema() {
		return Optional.ofNullable(schema).orElse("");
	}

	@Override
	public UniqueConstraint[] uniqueConstraints() {
		return new UniqueConstraint[0];
	}

	@Override
	public Index[] indexes() {
		return new Index[0];
	}
	
}

