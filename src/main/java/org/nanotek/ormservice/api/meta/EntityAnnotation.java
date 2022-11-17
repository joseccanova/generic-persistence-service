package org.nanotek.ormservice.api.meta;

import java.lang.annotation.Annotation;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class EntityAnnotation extends MetaAnnotation implements Entity{

	@Getter
	@Setter
	private String name;
	
	@Override
	public Class<? extends Annotation> annotationType() {
		return Entity.class;
	}

	@Override
	public String name() {
		return name;
	}
}
