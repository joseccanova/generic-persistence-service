package org.nanotek.ormservice.api.meta;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


/**
 * Quite more complex processing need to be mounted on a second pass of the class builder
 * @author Jose.Canova
 *
 */
@Data
@Builder
public class MetaRelation {

	@Getter
	@Setter
	protected MetaClass to;
	
	@Getter
	@Setter
	protected RelationType type;

}
