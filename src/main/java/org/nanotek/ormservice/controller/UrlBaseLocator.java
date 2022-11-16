package org.nanotek.ormservice.controller;

import org.nanotek.ormservice.IBase;

public interface UrlBaseLocator {

	default String getBaseUrl(IBase<?> base) {
		return "/"+base.getId();
	}
	
}
