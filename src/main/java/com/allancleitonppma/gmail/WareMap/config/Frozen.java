package com.allancleitonppma.gmail.WareMap.config;

import java.util.Objects;

public class Frozen  extends Generalparameter{
	final String name = "congelado";
	
	public Frozen(String parameter) {
		super(parameter);
		
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Frozen other = (Frozen) obj;
		return Objects.equals(name, other.name);
	}
	
	
}
