package org.nanotek.ormservice.api.meta.service;

import java.util.Optional;

public interface Result<T> {

	default Optional<T> getResult() {
		return Optional.empty();
	}
}
