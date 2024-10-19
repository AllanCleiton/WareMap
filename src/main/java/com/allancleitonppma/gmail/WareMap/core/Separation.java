package com.allancleitonppma.gmail.WareMap.core;


import java.util.List;

import com.allancleitonppma.gmail.WareMap.DTO.ProductDto;


public abstract class Separation {
	protected Integer orderCharger;
	protected List<ProductDto> products;
	
	public Separation(List<ProductDto> products, Integer orderCharger) {
		this.products = products;
	}
	
	public Separation() {}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Ordem de carga: " + orderCharger + "\n");
		sb.append("_________________________________________\n");
		for (ProductDto p : products) {
			sb.append(p.toString());
		}
		
		return sb.toString();
	}


}