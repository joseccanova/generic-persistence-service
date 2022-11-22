package org.nanotek.ormservice.api.meta;

import java.util.List;

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

	<T> List<T> getAliases();

	void setAliases(List<Alias> idAliases);

}