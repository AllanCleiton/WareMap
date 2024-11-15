package com.allancleiton.waremap.config.parameters;

import java.util.Objects;
import java.util.function.Predicate;

import com.allancleiton.waremap.entities.Order;

public class Floor_separation implements Predicate<Order>{
	final String name = "Separacao_chao";
	protected Integer parameter;
	public Floor_separation(String parameter) {
		this.parameter = Integer.parseInt(parameter);
	}
	
	@Override
	public boolean test(Order order) {
		return order.qtdeBoxes() <= parameter;
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

