package org.nanotek.ormservice.api.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Quite more complex processing need to be mounted on a second pass of the class builder
 * @author Jose.Canova
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MetaRelation {

	protected MetaClass from; 
	
	protected MetaClass to;
	
	protected RelationType type;
	
}
