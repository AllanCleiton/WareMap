package com.allancleitonppma.gmail.WareMap.config;

import java.util.Objects;

public class Resfri_fora_estado extends Generalparameter{
	final String name = "resfri_fora_estado";
	public Resfri_fora_estado(String parameter) {
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
		Resfri_fora_estado other = (Resfri_fora_estado) obj;
		return Objects.equals(name, other.name);
	}
	
	
}
