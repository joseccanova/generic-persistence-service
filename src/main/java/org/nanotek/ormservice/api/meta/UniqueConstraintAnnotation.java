package org.nanotek.ormservice.api.meta;

import java.lang.annotation.Annotation;

import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class UniqueConstraintAnnotation 
extends MetaAnnotation 
implements UniqueConstraint{

	@Getter
	@Setter
	private String name;
	
	@Override
	public Class<? extends Annotation> annotationType() {
		return UniqueConstraint.class;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String[] columnNames() {
		return new String[0];
	}

}
