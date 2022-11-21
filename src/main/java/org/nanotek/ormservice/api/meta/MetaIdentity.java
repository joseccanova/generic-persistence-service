package org.nanotek.ormservice.api.meta;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Valid
public class MetaIdentity {

	private String definition;
	private String shortName;
	@NotEmpty
	private String name;
	@Size(min = 1)
	private List<MetaDataAttribute> attributes; 
	@NotNull
	private IdentityType type;
	
	public static enum IdentityType {
		Identity,
		Single,
		Embeddable
	}
	

}
