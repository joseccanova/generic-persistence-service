package org.nanotek.ormservice.api.meta;

import java.lang.annotation.Annotation;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

public class ManyToOneTypeAnnotation implements javax.persistence.ManyToOne{

	@Override
	public Class<? extends Annotation> annotationType() {
		return ManyToOne.class;
	}

	@Override
	public Class targetEntity() {
		return null;
	}

	@Override
	public CascadeType[] cascade() {
		return new CascadeType[] {CascadeType.ALL};
	}

	@Override
	public FetchType fetch() {
		return FetchType.LAZY;
	}

	@Override
	public boolean optional() {
		return true;
	}
	
}