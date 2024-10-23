package com.allancleitonppma.gmail.WareMap.core;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import com.allancleitonppma.gmail.WareMap.DTO.ProductDto;


public abstract class Separation {
	protected String orderCharger;
	protected List<ProductDto> products;
	
	public Separation(List<ProductDto> products, String orderCharger) {
		this.products = products;
		this.orderCharger = orderCharger;
	}
	
	public Separation() {}
	
	public String getOrderChager() {
		return this.orderCharger;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Ordem de carga: " + orderCharger + "\n");
		sb.append("_________________________________________\n\n");
		for (ProductDto p : products) {
			sb.append(p.toString());
		}
		
		return sb.toString();
	}
	
	public boolean createArquiveWithSeparation(String path) throws Exception{

		try(BufferedWriter bW = new BufferedWriter(new FileWriter(path))) {
			for (ProductDto productDto : products) {
				bW.write(productDto.toString());
			}
			
			return true;
		}
	}

}