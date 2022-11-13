package org.nanotek.ormservice.api.meta;

public 	interface Populator <T,P>{
	
	T populate(T instance , P payload);
	
}