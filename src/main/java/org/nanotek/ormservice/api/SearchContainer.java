package org.nanotek.ormservice.api;

import java.util.Map;

import org.nanotek.ormservice.Base;

public class SearchContainer <T extends Base<?>>{

	protected T entity;
	
	protected Map<String,String> sortParameters;

	public SearchContainer() {
		super();
	}

	public SearchContainer(T entity, Map<String, String> parameters) {
		super();
		this.entity = entity;
		this.sortParameters = parameters;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public Map<String, String> getSortParameters() {
		return sortParameters;
	}

	public void setSortParameters(Map<String, String> sortParameters) {
		this.sortParameters = sortParameters;
	}

}
