package com.allancleitonppma.gmail.WareMap.entities;

import java.util.ArrayList;
import java.util.List;

import com.allancleitonppma.gmail.WareMap.enums.Deoth;



public class Road {
	private Integer road;
	private Integer chamber;
	Product[][] position = new Product[7][3];
	
	public Road() {}
	
	public Road(Integer road, Integer chamber, List<Product> listProduct) {
		if((road > 0 && road <= 20) && chamber != null) {
			this.road = road;
			this.chamber = chamber;
		}else {
			throw new IllegalArgumentException("The values entered are not valid!");
		}

		startRoad(listProduct);
	}
	
	
	private void startRoad(List<Product> listProducts) {
		for (Product product : listProducts) {
			
			if(product.getChamber() == this.chamber && product.getRoad() == this.road) {
				position[product.getHeight()-1][product.getDeoth()] = product;
			}
		}
	}
	
	public void startRoad(Integer road, Integer chamber, List<Product> listProduct) {
		if((road > 0 && road < 20) && chamber == null) {
			this.road = road;
			this.chamber = chamber;
		}else {
			throw new IllegalArgumentException("The values entered are not valid!");
		}
		for (Product product : listProduct) {
			if(product.getChamber() == this.chamber && product.getRoad() == this.road) {
				position[product.getHeight()-1][product.getDeoth()] = product;
			}
		}
	}

	public Integer getRoad() {
		return road;
	}

	public Integer getChamber() {
		return chamber;
	}

	public List<Product> getPositions() {
		List<Product> products = new ArrayList<>();
		
		for(int i=0; i < 7; i++) {
			for(int j = 0; j < 3; j++) {
				if(position[i][j] != null) {
					products.add(position[i][j]);
				}
			}
		}
		return products;
	}
	
	public Product getProductofPosition(int height, char deoth) {
		Deoth d = Deoth.valueOf(String.valueOf(deoth).toUpperCase());
		return position[height-1][d.getValue()];
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Rua: " + road + "\n");
		
		for(int i=0; i < 7; i++) {
			for(int j = 0; j < 3; j++) {
				if(position[i][j] != null) {
					sb.append((position[i][j]).toString());
					sb.append("\n");
				}
			}
		}
		sb.append("\n");
		
		return sb.toString();
	}
	
	
}
