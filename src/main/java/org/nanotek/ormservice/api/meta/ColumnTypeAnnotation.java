package org.nanotek.ormservice.api.meta;

import java.lang.annotation.Annotation;

import javax.persistence.Column;

import lombok.Data;

@Data
public class ColumnTypeAnnotation implements javax.persistence.Column{

	private String name = "";
	private boolean unique = false;
	private boolean nullable = true;
	private boolean insertable = true;
	private boolean updatable = true;
	private String columnDefinition = "";;
	private String table = "";;
	private int length;
	private int precision;
	private int scale;

	@Override
	public Class<? extends Annotation> annotationType() {
		return Column.class;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public boolean unique() {
		return unique;
	}

	@Override
	public boolean nullable() {
		return nullable;
	}

	@Override
	public boolean insertable() {
		return insertable;
	}

	@Override
	public boolean updatable() {
		return updatable;
	}

	@Override
	public String columnDefinition() {
		return columnDefinition;
	}

	@Override
	public String table() {
		return "";
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public int precision() {
		return precision;
	}

	@Override
	public int scale() {
		return scale;
	}
}
