package org.nanotek.ormservice.api.meta.model;

import java.util.Optional;

@FunctionalInterface
public interface Provider {

	<T> Optional<T> generate(T t);
	
	default <T> Optional<T> with(T t) {
		return new Provider() {
			@Override
			public <T> Optional<T> generate(T t) {
				return Optional.of(t);
			}
			
		}.generate(t);
	}
	
}
