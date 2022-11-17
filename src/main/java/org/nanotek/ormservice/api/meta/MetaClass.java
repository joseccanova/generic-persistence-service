package org.nanotek.ormservice.api.meta;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(value = Include.NON_NULL)
@Data
public class MetaClass implements IClass {

	@JsonProperty("tableName")
	protected String tableName;
	
	@JsonProperty("className")
	protected String className; 
	
	@JsonProperty("classType")
	protected MetaClassType classType;
	
	protected List<MetaDataAttribute> metaAttributes;

	@JsonIgnore
	private boolean hasPrimraryKey;

	@JsonIgnore
	protected List<MetaRelationClass> metaRelationsClasses;

	protected MetaIdentity identity;

	@Override
	public boolean addMetaAttribute(MetaDataAttribute attr) {
		return metaAttributes.add(attr);
	}

	@Override
	public void hasPrimaryKey(boolean b) {
		this.hasPrimraryKey=b;
	}

	@Override
	public boolean isHasPrimeraryKey() {
		return hasPrimraryKey;
	}

	@Override
	public void setHasPrimeraryKey(boolean hasPrimeraryKey) {
		this.hasPrimraryKey=hasPrimeraryKey;
	}
	
}
