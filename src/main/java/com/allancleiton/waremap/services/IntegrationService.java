package com.allancleiton.waremap.services;

import java.io.IOException;
import java.util.List;
import com.allancleiton.waremap.entities.LoadOrder;
import com.allancleiton.waremap.entities.Product;
import com.allancleiton.waremap.repository.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IntegrationService implements Repository{
	private String path;
	
	public IntegrationService(String path) {
		this.path = path;
	}
	
	@Override
	public String LoadProducts() throws IOException{
		List<Product> products = LoadProductsOfxlsx(path);
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.writeValueAsString(products);
	}

	@Override
	public String LoadOrder() throws IOException {
		LoadOrder order = getloadOrder(path);
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.writeValueAsString(order);
	}

	public String getPath() {
		return this.path;
	}
}
