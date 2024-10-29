package com.allancleitonppma.gmail.WareMap.config;

import com.allancleitonppma.gmail.WareMap.core.LoadOrder;

public class NotFloor_separation extends Floor_separation{

	public NotFloor_separation(String parameter) {
		super(parameter);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean test(LoadOrder.Product product) {
		return product.qtdeBoxes() > parameter;
	}
	
}
