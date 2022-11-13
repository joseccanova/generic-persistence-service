package org.nanotek.ormservice.api.meta;

public interface Filter<T> {
	public T apply(T payload);
}