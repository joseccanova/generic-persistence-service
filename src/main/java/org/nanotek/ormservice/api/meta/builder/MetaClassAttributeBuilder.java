package org.nanotek.ormservice.api.meta.builder;

import java.lang.annotation.Annotation;
import java.util.Optional;

import javax.persistence.Column;

import org.nanotek.ormservice.api.meta.MetaDataAttribute;

import lombok.Data;
import net.bytebuddy.dynamic.DynamicType.Builder;

public class MetaClassAttributeBuilder {

	public MetaClassAttributeBuilder() {
	}

	public Builder<?> build(Optional<Builder<?>> builder , MetaDataAttribute att) {
		return builder
				.get()
				.defineProperty(att.getFieldName(), att.getClazz())
				.annotateField(verifyAnnotationType(att));
	}

	private Annotation verifyAnnotationType(MetaDataAttribute att) {
		return Optional.of(att).map(a -> processAttributeType(a)).get();
	}

	private Annotation processAttributeType(MetaDataAttribute a) {
		 switch (a.getAttributeType()) {
		case Single:
			return columnType(a);
		case List:
			return listType(a);
		case Map:
			return mapType(a);
		default:
			return null;
		}
	}

	private Annotation mapType(MetaDataAttribute a) {
		return null;
	}

	private Annotation listType(MetaDataAttribute a) {
		return null;
	}

	private Annotation columnType(MetaDataAttribute a) {
		var ct = new ColumnType();
		ct.setName(a.getColumnName());
		ct.setNullable(!a.isRequired());
		return ct;
	}
	
	@Data
	class ColumnType implements javax.persistence.Column{

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
		}}
}
