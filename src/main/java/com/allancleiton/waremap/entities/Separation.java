package com.allancleiton.waremap.entities;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.allancleiton.waremap.entities.DTO.ChamberDto;
import com.allancleiton.waremap.entities.DTO.Position;
import com.allancleiton.waremap.entities.DTO.ProductDto;
import com.allancleiton.waremap.entities.DTO.RoadDto;


public class Separation {
	protected String orderCharger;
	protected List<ProductDto> products = null;
	
	public Separation(Map<Integer, List<Product>> partialProducts, LoadOrder order){
		Set<ProductDto> finalListOfProducts = new HashSet<>();
		Set<Position> positions = new HashSet<>();
		Set<RoadDto> roads = new HashSet<>();
		Set<ChamberDto> chams = new HashSet<>();
		
		
		for (Order lp : order.getOrders()) {
			for (Product product : partialProducts.get(lp.note())) {
				finalListOfProducts.add(new ProductDto(lp.note(), lp.qtdeBoxes()));
				chams.add(new ChamberDto(product.getChamber(), product.getNote()));
				roads.add(new RoadDto(product.getRoad(), product.getChamber(), product.getNote()));
				positions.add(new Position(product.getHeight(), product.getCharDeoth(), product.getRoad(), product.getNote(), product.getChamber(), product.getDays() , product.getBoxes()));

			}
		}
		

						
		for (ProductDto product : finalListOfProducts) {
			for (ChamberDto chamberDto : chams) {
				if(chamberDto.getNote() == product.getNote()) {
					product.setChamber(chamberDto);
				}
			}
		}
		
		for(ProductDto product : finalListOfProducts) {
			for (ChamberDto chamberDto : product.getChambers()) {
				for (RoadDto road : roads) {
					if(road.getChamber() == chamberDto.getChamber() && road.getNote() == product.getNote()) {
						chamberDto.setRoad(road);
					}
				}
			}
		}
		
		for(ProductDto product : finalListOfProducts) {
			for (ChamberDto chamberDto : product.getChambers()) {
				for (RoadDto road : chamberDto.getRoads()) {
					for (Position position : positions) {
						if(position.getRoad() == road.getRoad() && position.getProduct() == product.getNote() && position.getChamber() == chamberDto.getChamber()) {
							road.setPosition(position);
						}
					}
				}
			}
		}
		/*
		System.out.println("finalListOfProducts");
		for (ProductDto product : finalListOfProducts) {
			System.out.println(product);
		}
		System.out.println();*/
		
		this.products = new ArrayList<>(finalListOfProducts);
	}
	
	
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
	
	public List<ProductDto> getDtoProducts(){
		return this.products;
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