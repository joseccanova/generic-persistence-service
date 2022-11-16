package org.nanotek.ormservice.api.meta;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MetaIdentity {

	private String definition;
	private String shortName;
	private String name;
	private List<MetaDataAttribute> attributes; 
	
	public static enum IdentityType {}

}
