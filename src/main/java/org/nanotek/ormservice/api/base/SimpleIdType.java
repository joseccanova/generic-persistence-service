package org.nanotek.ormservice.api.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.nanotek.ormservice.IBase;
import org.nanotek.ormservice.validation.CreateValidationGroup;
import org.nanotek.ormservice.validation.UpdateValidationGroup;

@Entity
@Table(name="single_id_table")
@Valid
public class SimpleIdType implements IBase<Long>{

	private static final long serialVersionUID = -8993023295908048041L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups=UpdateValidationGroup.class)
	private Long id;
	
	@Column(name="provided_id")
	@NotNull(groups= {UpdateValidationGroup.class , CreateValidationGroup.class})
	private Long providedId;
	
	@NotNull(groups= {UpdateValidationGroup.class , CreateValidationGroup.class})
	@NotEmpty(groups= {UpdateValidationGroup.class , CreateValidationGroup.class})
	@Column(name="name" , nullable = false)
	private String name;
	
	public SimpleIdType() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProvidedId() {
		return providedId;
	}

	public void setProvidedId(Long providedId) {
		this.providedId = providedId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
