package org.nanotek.ormservice.controller;

import org.nanotek.ormservice.Base;

public interface UrlBaseLocator {

	default String getBaseUrl(Base<?> base) {
		return "/"+base.getId();
	}
	
}
