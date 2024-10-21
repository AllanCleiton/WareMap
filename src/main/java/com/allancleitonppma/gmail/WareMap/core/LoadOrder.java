package com.allancleitonppma.gmail.WareMap.core;


import java.util.List;

public class LoadOrder {
	private String orderCharger;
	private List<Product> products; 
	
	public LoadOrder(List<Product> listproducts) {
		if(!listproducts.isEmpty()) {
			this.products = listproducts;
		}else {
			throw new IllegalArgumentException("The list cannot are empty!");
		}
		
	}
	
	public LoadOrder(List<Product> listproducts, String orderCharger) {
		if(!listproducts.isEmpty()) {
			this.products = listproducts;
			this.orderCharger = orderCharger;
		}else {
			throw new IllegalArgumentException("The list cannot are empty!");
		}
		
	}
	
	public LoadOrder() {}
	
	
	public String getOrderCharger() {
		return orderCharger;
	}

	public void setOrderCharger(String orderCharger) {
		this.orderCharger = orderCharger;
	}

	public List<Product> getProducts() {
		return products;
	}
	
	//------------------------------------------------------------------//

	public record Product(Integer note, Integer qtdeBoxes) {

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("\n");
			sb.append("Produto: " + note + " Caixas: " + qtdeBoxes);
			
			return sb.toString();
		}

		
		
	}
}
