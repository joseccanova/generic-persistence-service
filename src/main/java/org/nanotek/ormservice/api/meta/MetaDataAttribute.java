package org.nanotek.ormservice.api.meta;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//TODO: verify complex attributes being used as IDClasses.
@Data
public class MetaDataAttribute 
 {

	@Getter
	@Setter
	@JsonProperty("clazz")
	protected Class<?> clazz;
	@Getter
	@Setter
	@JsonProperty("columnName")
	
	protected String columnName;
	//TODO:Fix Length to integer
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
	private List<Alias> aliases;
	
	@Getter
	@Setter
	private AttributeType attributeType;
	
	public static enum AttributeType {
		Single, 
		Collection, 
		Set, 
		Map,
		List
	}
}
