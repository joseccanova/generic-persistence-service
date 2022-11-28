package org.nanotek.ormservice.api.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.hibernate.mapping.MetaAttribute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//TODO:Define the validation groups for the MetaClass.
@JsonInclude(value = Include.NON_NULL)
@Data
@Valid
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaClass  {

	public static final String DEFAULT_PACKAGE = "org.nanotek.entity";
	
	@Setter
	@JsonProperty("tableName")
	protected String tableName;

	@Setter	@JsonProperty("className")
	protected String className; 
	

	@Setter
	@JsonProperty("classType")
	protected MetaClassType classType;
	
	protected List<MetaDataAttribute> metaAttributes;

	@Getter
	@Setter
	@JsonIgnore
	protected List<MetaRelation> metaRelations;

	@Setter
	@JsonIgnore
	private boolean hasPrimaryKey;

	@Setter
	protected MetaIdentity identity;

	public boolean addMetaAttribute(MetaDataAttribute attr) {
		return metaAttributes==null ? 
			createAndAdd(ArrayList::new, attr)
			: metaAttributes.add(attr);
	}

	private boolean createAndAdd(Supplier<List<MetaDataAttribute>> sup , MetaDataAttribute attr) {
		return (metaAttributes = sup.get()).add(attr);
	}

	public void hasPrimaryKey(boolean b) {
		this.hasPrimaryKey=b;
	}

	public boolean isHasPrimaryKey() {
		return hasPrimaryKey;
	}

	public void setHasPrimeraryKey(boolean hasPrimeraryKey) {
		this.hasPrimaryKey=hasPrimeraryKey;
	}
	
	public String defaultFullClassName() {
		return new StringBuilder().append(DEFAULT_PACKAGE).append('.').append(className).toString();
	}
	
	public Optional<List<MetaRelation>> hasRelations() {
		return Optional.ofNullable(metaRelations);
	}
	
	public List<MetaRelation> setRelations(Supplier<List<MetaRelation>> rl) {
		return rl.get();
	}
	
	public Optional<MetaClass> and(){
		return Optional.of(this);
	}
}
