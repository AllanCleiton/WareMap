package com.allancleiton.waremap.config.parameters;

import java.util.function.Predicate;

import com.allancleiton.waremap.entities.Product;


public abstract class GeneralParameter implements Predicate<Product>{
	protected Integer parameter;
	
	public GeneralParameter(String parameter) {
		this.parameter = Integer.parseInt(parameter);
		
	}
	
	@Override
	public boolean test(Product product) {
		boolean test = product.getDays() < parameter;
		if(test) {
			product.visited = true;
		}
		return test;
	}
	
}
