package org.nanotek.ormservice.api.meta;

import java.util.List;
import java.util.Map;

public interface IDataAttribute {

	java.lang.String toString();

	String getColumnName();

	void setColumnName(String columnName);

	void setClazz(Class<?> clazz);

	Class<?> getClazz();

	String getSqlType();

	void setSqlType(String string);

	String getLength();

	void setLength(String length);

	boolean isRequired();

	void setRequired(boolean required);

	void setFieldName(String name);

	String getFieldName();

	void setAttributes(Map<String, Object> attributes);

	Map<String, Object> getAttributes();

	<T> List<T> getAliases();

	void setAliases(List<Alias> idAliases);

}