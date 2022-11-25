package org.nanotek.ormservice;

import java.util.Optional;
import java.util.function.Supplier;

@FunctionalInterface
public interface Provider<T> {
   
   Optional<T> get();
   
   public static <T> Provider <T> of(Supplier<T> t) {
	   return new Provider<T> () {
		@Override
		public Optional<T> get() {
			return Optional.of(t.get());
		}
	   };
   }
   
}
