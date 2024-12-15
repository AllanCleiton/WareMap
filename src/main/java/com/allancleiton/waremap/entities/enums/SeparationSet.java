package com.allancleiton.waremap.entities.enums;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.allancleiton.waremap.entities.Order;
import com.allancleiton.waremap.entities.Separation;
import com.allancleiton.waremap.entities.DTO.ProductDto;

public class SeparationSet<T extends Separation, U extends Separation, V extends Separation>{
	private T forklift;
    private U floor;
    private V cold;
    protected List<ProductDto> finalListOfProducts = new ArrayList<>();
    protected Set<ProductDto> productsNotFound = new TreeSet<>();

    
    public SeparationSet(T forklift, U floor, V cold) {
        this.forklift = forklift;
        this.floor = floor;
        this.cold = cold;    
        
        finalListOfProducts.addAll(forklift.getDtoProducts());
        finalListOfProducts.addAll(floor.getDtoProducts());
        finalListOfProducts.addAll(cold.getDtoProducts());
        
		for (Order lp : forklift.getLoadOrder().getOrders()) {
			
			if(!(finalListOfProducts.stream().anyMatch(p -> p.getNote().equals(lp.getNote())))) {
				productsNotFound.add(new ProductDto(lp.note(), lp.qtdeBoxes()));

			}
		}
    }
      
    
    
    public T getForklift() { return forklift; }
    public U getFloor() { return floor; }
    public V getCold() { return cold; }
    
    public boolean createArquiveWithSeparation(String path) throws Exception{    	
		try(BufferedWriter bW = new BufferedWriter(new FileWriter(path))) {
			if(!(getForklift().getDtoProducts().isEmpty())) {
				bW.write("Separação da Empilhadeira. [Congelados]\n");
				for (ProductDto productDto : getForklift().getDtoProducts()) {
					bW.write(productDto.print(finalListOfProducts));
				}
			}
			
			if(!(getCold().getDtoProducts().isEmpty())) {
				bW.write("\nSeparação da Empilhadeira. [Resfriados]\n");
				for (ProductDto productDto : getCold().getDtoProducts()) {
					bW.write(productDto.print(finalListOfProducts));
				}
			}
			
			
			if(!(getFloor().getDtoProducts().isEmpty())) {
				bW.write("\nSeparação do chão.\n");
				for (ProductDto productDto : getFloor().getDtoProducts()) {
					bW.write(productDto.print(finalListOfProducts));
				}
			}
			
			if(!(productsNotFound.isEmpty())) {
				bW.write("\nProdutos não encontrados.\n");
				for (ProductDto productDto : productsNotFound) {
					bW.write(productDto.toString());
				}
			}
			return true;
		}
	}

}