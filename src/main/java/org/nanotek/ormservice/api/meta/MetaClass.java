package org.nanotek.ormservice.api.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(value = Include.NON_NULL)
@Data
public class MetaClass  {

	public static final String DEFAULT_PACKAGE = "org.nanotek.entity";
	
	@Getter
	@Setter
	@JsonProperty("tableName")
	protected String tableName;
	

	@Getter
	@Setter	@JsonProperty("className")
	protected String className; 
	

	@Getter
	@Setter
	@JsonProperty("classType")
	protected MetaClassType classType;
	
	protected List<MetaDataAttribute> metaAttributes = new ArrayList<MetaDataAttribute>();


	@Getter
	@Setter
	@JsonIgnore
	protected List<MetaRelation> metaRelations;


	@Getter
	@Setter
	@JsonIgnore
	private boolean hasPrimraryKey;


	@Getter
	@Setter
	protected MetaIdentity identity;

	public boolean addMetaAttribute(MetaDataAttribute attr) {
		return metaAttributes.add(attr);
	}

	public void hasPrimaryKey(boolean b) {
		this.hasPrimraryKey=b;
	}

	public boolean isHasPrimeraryKey() {
		return hasPrimraryKey;
	}

	public void setHasPrimeraryKey(boolean hasPrimeraryKey) {
		this.hasPrimraryKey=hasPrimeraryKey;
	}
	
	public String defaultFullClassName() {
		return new StringBuilder().append(DEFAULT_PACKAGE).append('.').append(className).toString();
	}
	
	public Optional<?> hasRelations() {
		return Optional.ofNullable(metaRelations).filter(r -> r.size()>0);
	}
}
