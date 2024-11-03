package com.allancleiton.waremap.config.parameters;

import java.util.Objects;

import com.allancleiton.waremap.entities.Product;

public class Cold_in_state extends GeneralParameter{
	final String name = "resfri_dentro_estado";
	public Cold_in_state(String parameter) {
		super(parameter);
		
	}
	
	@Override
	public boolean test(Product product) {
		boolean test = product.getDays() >= parameter;
		if(test) {
			product.visited = true;
		}
		return test;
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
		Cold_in_state other = (Cold_in_state) obj;
		return Objects.equals(name, other.name);
	}
	
	
}