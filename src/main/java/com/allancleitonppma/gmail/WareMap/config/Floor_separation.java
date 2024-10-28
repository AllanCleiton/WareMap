package com.allancleitonppma.gmail.WareMap.config;

import java.util.Objects;

import com.allancleitonppma.gmail.WareMap.entities.Product;

public class Floor_separation extends Generalparameter{
	final String name = "Separacao_chao";
	public Floor_separation(String parameter) {
		super(parameter);
	}
	
	@Override
	public boolean test(Product product) {
		return product.getBoxes() < parameter;
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
		Floor_separation other = (Floor_separation) obj;
		return Objects.equals(name, other.name);
	}
	
	

}
