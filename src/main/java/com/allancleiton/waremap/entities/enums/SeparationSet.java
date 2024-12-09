package com.allancleiton.waremap.entities.enums;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.allancleiton.waremap.config.parameters.Floor_separation;
import com.allancleiton.waremap.entities.Order;
import com.allancleiton.waremap.entities.Separation;
import com.allancleiton.waremap.entities.DTO.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SeparationSet<T extends Separation, U extends Separation, V extends Separation>{
	private T forklift;
    private U floor;
    private V cold;
    protected Set<ProductDto> finalListOfProducts = new TreeSet<>();
    
    
    public SeparationSet(T forklift, U floor, V cold) {
        this.forklift = forklift;
        this.floor = floor;
        this.cold = cold;    
        
        finalListOfProducts.addAll(forklift.getDtoProducts());
        finalListOfProducts.addAll(floor.getDtoProducts());
        finalListOfProducts.addAll(cold.getDtoProducts());
        
		boolean exist = false;
		for (Order lp : forklift.getLoadOrder().getOrders()) {
			for(ProductDto p : finalListOfProducts) {
				if(p.getNote() == lp.getNote()) {
					exist = true;
				}
			}
			if(!exist) {
				finalListOfProducts.add(new ProductDto(lp.note(), lp.qtdeBoxes()));
			}
			exist = false;
		}
    }
      
    
    
    public T getForklift() { return forklift; }
    public U getFloor() { return floor; }
    public V getCold() { return cold; }
    
    public boolean createArquiveWithSeparation(String path) throws Exception{
    	ObjectMapper objectMapper = new ObjectMapper();
    	Floor_separation floorSeparation = null;
		floorSeparation = objectMapper.readValue(new File("temp/config/geralParameters/floor_separation.json"), Floor_separation.class);

		int floor = floorSeparation.getParameter();
		
		
    	Set<ProductDto> listFloor = finalListOfProducts.stream().filter(x -> x.getQuantity() <= floor).collect(Collectors.toSet());
    	finalListOfProducts.removeAll(listFloor);
    	
		try(BufferedWriter bW = new BufferedWriter(new FileWriter(path))) {
			bW.write("Separação da Empilhadeira.\n");
			for (ProductDto productDto : finalListOfProducts) {
				bW.write(productDto.toString());
			}
			bW.write("\nSeparação do chão.\n");
			for (ProductDto productDto : listFloor) {
				bW.write(productDto.toString());
			}
			return true;
		}
	}
}