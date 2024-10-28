package com.allancleitonppma.gmail.WareMap.config;

import java.util.function.Predicate;

import com.allancleitonppma.gmail.WareMap.entities.Product;

public abstract class Generalparameter implements Predicate<Product>{
	protected Integer parameter;
	
	public Generalparameter(String parameter) {
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
