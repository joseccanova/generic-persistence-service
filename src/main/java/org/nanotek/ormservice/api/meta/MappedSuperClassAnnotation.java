package org.nanotek.ormservice.api.meta;

import java.lang.annotation.Annotation;

import javax.persistence.*;

public class MappedSuperClassAnnotation 
	extends MetaAnnotation 
	implements MappedSuperclass{

	@Override
	public Class<? extends Annotation> annotationType() {
		return MappedSuperclass.class;
	}

}
