package org.nanotek.ormservice;

import java.util.Optional;
import java.util.function.Supplier;

public class Holder<S> {
	S s;
	public Holder<S> put(S s){
		this.s = s;
		return  this;
	}
	public  Optional<S> get(){
		return Optional.ofNullable(s);
	}
	
	@Override
	public String toString() {
		return "Holder [s=" + s + "]";
	}
	
	public static <S>  Holder<S> with(Supplier<Holder<S>> sup , S s)
	{
		return sup.get().put(s);
	}
	
}