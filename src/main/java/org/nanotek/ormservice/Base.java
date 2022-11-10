package org.nanotek.ormservice;

import java.io.Serializable;

public interface Base<T> extends Serializable{

	T getId();
	
}