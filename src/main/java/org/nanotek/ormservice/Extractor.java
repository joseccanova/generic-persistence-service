package org.nanotek.ormservice;

@FunctionalInterface
public interface Extractor {
	
	<T , R> T extract (T t);
	
}
