package org.nanotek.ormservice;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import javax.persistence.MappedSuperclass;
import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
import org.nanotek.ormservice.api.meta.MetaClass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@MappedSuperclass
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@Valid
public abstract class Base<ID> implements IBase<ID> {

	public static <K extends Base<?>> K newType(Supplier<K> baseSupplier)
	{ 
		return baseSupplier.get();
	}
	
	public Object getProperty(Field f, Base<ID> base) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return 	PropertyUtils.getProperty(base, f.getName());
	}
	
	@JsonIgnore
	public abstract MetaClass getMetaClass();
	
}
