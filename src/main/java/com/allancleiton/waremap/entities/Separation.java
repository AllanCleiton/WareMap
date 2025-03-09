package com.allancleiton.waremap.entities;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.allancleiton.waremap.config.parameters.Floor_separation;
import com.allancleiton.waremap.entities.DTO.ChamberDto;
import com.allancleiton.waremap.entities.DTO.Position;
import com.allancleiton.waremap.entities.DTO.ProductDto;
import com.allancleiton.waremap.entities.DTO.RoadDto;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Separation {
	protected Set<ProductDto> finalListOfProducts = new HashSet<>();
	protected Map<Integer, List<Product>> partialProducts = null;
	protected LoadOrder order = null;
	
	public Separation(Map<Integer, List<Product>> partialProducts, LoadOrder order){
		this.partialProducts = partialProducts;
		this.order = order;
		start(partialProducts, order);
	}
	
	public void start(Map<Integer, List<Product>> partialProducts, LoadOrder order) {
		Set<Position> positions = new HashSet<>();
		Set<RoadDto> roads = new HashSet<>();
		Set<ChamberDto> chams = new HashSet<>();
		ProductDto productDto = null;
		Position auxPosition = null;
		for (Order lp : order.getOrders()) {
			
			for (Product product : partialProducts.get(lp.note())) {
				
				if(product.activePackeg && product.getPackages() == lp.packeges) {
					productDto = new ProductDto(lp.note(), lp.qtdeBoxes(), lp.packeges);
					productDto.tam = lp.packeges;
					finalListOfProducts.add(productDto);

					chams.add(new ChamberDto(product.getChamber(), productDto.getEspecialNote()));
					roads.add(new RoadDto(product.getRoad(), product.getChamber(), productDto.getEspecialNote()));
					auxPosition = new Position(product.getHeight(), product.getCharDeoth(), product.getRoad(), productDto.getEspecialNote(), product.getChamber(), product.getDays() , product.getBoxes());
					auxPosition.setProduct(product);
					positions.add(auxPosition);
					
				}else if(!(product.activePackeg)){
					finalListOfProducts.add(new ProductDto(lp.note(), lp.qtdeBoxes()));
					chams.add(new ChamberDto(product.getChamber(), product.getNote()));
					roads.add(new RoadDto(product.getRoad(), product.getChamber(), product.getNote()));
					positions.add(new Position(product));
				}
				
			}
		}
		for (ProductDto product : finalListOfProducts) {
			for (ChamberDto chamberDto : chams) {
				if(chamberDto.getNote() == product.getEspecialNote()) {
					product.setChamber(chamberDto);
				}
			}
		}

		for(ProductDto product : finalListOfProducts) {
			for (ChamberDto chamberDto : product.getChambers()) {
				for (RoadDto road : roads) {
					if(road.getChamber() == chamberDto.getChamber() && road.getNote() == product.getEspecialNote()) {
						chamberDto.setRoad(road);
					}
				}
			}
		}

		for(ProductDto product : finalListOfProducts) {
			for (ChamberDto chamberDto : product.getChambers()) {
				for (RoadDto road : chamberDto.getRoads()) {
					for (Position position : positions) {
						if(position.getRoad() == road.getRoad() && position.getProduct() == product.getEspecialNote() && position.getChamber() == chamberDto.getChamber()) {
							road.setPosition(position);
						}
					}
				}
			}
		}
		
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ProductDto p : finalListOfProducts) {
			sb.append(p.toString());
		}
		
		return sb.toString();
	}
	
	public List<ProductDto> getDtoProducts(){

		return finalListOfProducts.stream().sorted().collect(Collectors.toList());

	}
	
	public Map<Integer, List<Product>> getPartialProducts(){
		return this.partialProducts;
	}
	
	public LoadOrder getLoadOrder() {
		return this.order;
	}
	
	public boolean createArquiveWithSeparation(String path) throws Exception{
    	ObjectMapper objectMapper = new ObjectMapper();
    	Floor_separation floorSeparation = null;
		floorSeparation = objectMapper.readValue(new File("src/main/resources/temp/config/geralParameters/floor_separation.json"), Floor_separation.class);
		Set<ProductDto> aux = new HashSet<>(finalListOfProducts);
		Set<ProductDto> productsNotFound = new HashSet<>();
		int floor = floorSeparation.getParameter();
		
		
    	Set<ProductDto> listFloor = aux.stream().filter(
    												x -> (x.getQuantity() <= floor)
    											).collect(Collectors.toSet());
    	
    	aux.removeAll(listFloor);
    	
    	
    	for (Order lp : getLoadOrder().getOrders()) {
			if(!(finalListOfProducts.stream().anyMatch(p -> p.getNote().equals(lp.getNote())))) {
				productsNotFound.add(new ProductDto(lp.note(), lp.qtdeBoxes()));

			}
		}
    	    
		try(BufferedWriter bW = new BufferedWriter(new FileWriter(path))) {
			bW.write("Separação da Empilhadeira.\n");
			for (ProductDto productDto : aux) {
				bW.write(productDto.toString());
			}
			if(!(listFloor.isEmpty())) {
				bW.write("\nSeparação do chão.\n");
				for (ProductDto productDto : listFloor) {
					bW.write(productDto.toString());
				}
			}
			
			if (!(productsNotFound.isEmpty())) {
				bW.write("\nProdutos não encontrados.\n");
				for (ProductDto productDto : productsNotFound) {
					bW.write(productDto.toString());
				}
			}
			return true;
		}
	}
}