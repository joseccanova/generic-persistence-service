package org.nanotek.ormservice;

import java.util.Optional;

@FunctionalInterface
public interface Provider<T> {
   
  Optional<T> get();
  
}
