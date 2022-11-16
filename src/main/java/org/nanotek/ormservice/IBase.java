package org.nanotek.ormservice;

import java.io.Serializable;

public interface IBase<T> extends Serializable{

	T getId();
	
}