package org.nanotek.ormservice.api.meta;

import java.lang.annotation.Annotation;

import javax.persistence.Index;

public class IndexAnnotation 
extends MetaAnnotation 
implements Index{

	private String name;
	
	@Override
	public Class<? extends Annotation> annotationType() {
		return Index.class;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String columnList() {
		return null;
	}

	@Override
	public boolean unique() {
		return false;
	}


}
