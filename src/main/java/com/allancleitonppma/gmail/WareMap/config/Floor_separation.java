package com.allancleitonppma.gmail.WareMap.config;

import java.util.Objects;
import java.util.function.Predicate;

import com.allancleitonppma.gmail.WareMap.core.LoadOrder;

public class Floor_separation implements Predicate<LoadOrder.Product>{
	final String name = "Separacao_chao";
	protected Integer parameter;
	public Floor_separation(String parameter) {
		this.parameter = Integer.parseInt(parameter);
	}
	
	@Override
	public boolean test(LoadOrder.Product product) {
		return product.qtdeBoxes() <= parameter;
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
