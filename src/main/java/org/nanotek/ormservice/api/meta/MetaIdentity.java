package org.nanotek.ormservice.api.meta;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Data
@Valid
@Builder
public class MetaIdentity {

	@Getter
	@Setter
	private String definition;

	@Getter
	@Setter
	private String shortName;
	
	@Getter
	@Setter
	@NotEmpty
	private String name;
	
	@Getter
	@Setter
	@Size(min = 1)
	private List<MetaDataAttribute> attributes; 
	
	@Getter
	@Setter
	@NotNull
	private IdentityType type;

	@Getter
	@Setter
	private Long id;
	
	public static enum IdentityType {
		Identity,
		Single,
		Embeddable
	}

}
