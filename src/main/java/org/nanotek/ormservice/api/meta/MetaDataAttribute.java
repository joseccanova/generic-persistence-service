package org.nanotek.ormservice.api.meta;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//TODO: verify complex attributes being used as IDClasses.
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MetaDataAttribute implements IDataAttribute {

	@Getter
	@Setter
	@JsonProperty("clazz")
	protected Class<?> clazz;
	@Getter
	@Setter
	@JsonProperty("columnName")
	protected String columnName;
	@Getter
	@Setter
	@JsonProperty("length")
	protected String length;
	@Getter
	@Setter
	@JsonProperty("required")
	protected boolean required;
	@Getter
	@Setter
	@JsonProperty("sqlType")
	private String sqlType;
	@Getter
	@Setter
	@JsonProperty("fieldName")
	private String fieldName;
	@Getter
	@Setter
	@JsonProperty("attributes")
	private Map<String, Object> attributes;

	@Getter
	@Setter
	private List<String> aliases;
	
	@Getter
	@Setter
	@JsonIgnore
	private MetaClass metaClass;
	
	public MetaDataAttribute(MetaClass mc) {
		super();
		this.metaClass=mc;
	}
	
}