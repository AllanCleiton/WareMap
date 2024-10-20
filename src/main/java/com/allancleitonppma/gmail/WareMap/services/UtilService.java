package com.allancleitonppma.gmail.WareMap.services;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;


import com.allancleitonppma.gmail.WareMap.DTO.ChamberDto;
import com.allancleitonppma.gmail.WareMap.DTO.Position;
import com.allancleitonppma.gmail.WareMap.DTO.ProductDto;
import com.allancleitonppma.gmail.WareMap.DTO.RoadDto;
import com.allancleitonppma.gmail.WareMap.core.ForkliftSeparation;
import com.allancleitonppma.gmail.WareMap.core.LoadOrder;
import com.allancleitonppma.gmail.WareMap.core.Separation;
import com.allancleitonppma.gmail.WareMap.entities.Chamber;
import com.allancleitonppma.gmail.WareMap.entities.Product;
import com.allancleitonppma.gmail.WareMap.entities.Road;


public class UtilService implements UtilServices{
	private Map<Integer, List<Product>> partialProducts = new HashMap<>();
	
	public UtilService() {}
	
	public Map<Integer, List<Product>> getPartialProducts() {
		return partialProducts;
	}



	@Override
	public Separation customSeparation(LoadOrder order, List<Chamber> chambers) {
		
		return null;
	}

	@Override
	public Separation simpleSeparation(LoadOrder order, List<Chamber> chambers) {
		
		
		for (LoadOrder.Product p : order.getProducts()) {
			partialProducts.put(p.note(), filterChamber(p.note(), chambers));
		}
		
		/*int SumQuantity = 0;
		List<Product> filter = null;
 		for (LoadOrder.Product p : order.getProducts()) {
			filter = filterChamber(p.note(), chambers);
			
			
			
			partialProducts.put(p.note(),null);
		}*/
		
		return new ForkliftSeparation(this.processPartialProducts(partialProducts, order), order.getOrderCharger());
	}


	public List<Product> filterChamber(int code, List<Chamber> chambers, Predicate<Product> filter){
		List<Product> products = new ArrayList<>();
		
		for (Chamber chamber : chambers) {
			for (Road road : chamber.getAllRoads()) {
				for (Product product : road.getPositions()) {
					if(filter.test(product) && product.getNote() == code) {
						products.add(product);
					}
				}
			}
		}
		return products;
	}
	
	public List<Product> filterChamber(int code, List<Chamber> chambers){
		List<Product> products = new ArrayList<>();
	
		for (Chamber chamber : chambers) {
			for (Road road : chamber.getAllRoads()) {
				if(road != null) {
					for (Product product : road.getPositions()) {
						if(product.getNote() == code) {
							products.add(product);
							
							
						}
					}
				}
			}
		}
		return products;
	}

	public List<Product> filterProducts(List<Product> products, Predicate<Product> filter){
		List<Product> list = new ArrayList<>();
		for (Product product : products) {
			if(filter.test(product)) {
				list.add(product);
			}
		}
		return list;
	}

	public Product moreEasy(int code, List<Product> products) {
		int max = 8;
		int aux;
		boolean exists;
		Product product=null;
			
		do {
			exists = false;
			
			for (Product p : products) {
				aux = p.getHeight() + p.getDeoth();
				if(aux < max ) {
					max = aux;
					product = p;
					exists = true;
					
				}
			}
		}while(exists); 
				
		
		return product;
	}
	
	
	//Metodo para processar o map com uma lista de produtos filtrados para cada produto, e gerar a relacao de separacao.
	public List<ProductDto> processPartialProducts(Map<Integer, List<Product>> partialProducts, LoadOrder order){
		
		Set<ProductDto> finalListOfProducts = new HashSet<>();
		Set<Position> positions = new HashSet<>();
		Set<RoadDto> roads = new HashSet<>();
		Set<ChamberDto> chams = new HashSet<>();
		
		
		for (LoadOrder.Product lp : order.getProducts()) {
			for (Product product : partialProducts.get(lp.note())) {
				finalListOfProducts.add(new ProductDto(lp.note(), lp.qtdeBoxes()));
				chams.add(new ChamberDto(product.getChamber(), product.getNote()));
				roads.add(new RoadDto(product.getRoad(), product.getChamber(), product.getNote()));
				positions.add(new Position(product.getHeight(), product.getCharDeoth(), product.getRoad(), product.getNote(), product.getChamber()));

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
		
		/*System.out.println("finalListOfProducts");
		for (ProductDto product : finalListOfProducts) {
			System.out.println(product);
		}
		System.out.println();*/
		
		return new ArrayList<>(finalListOfProducts);
	}

	public static void fileGenerator(String path, List<? extends Object> list) {
		try(BufferedWriter bW = new BufferedWriter(new FileWriter(path))) {
			for (Object obj : list) {
				bW.write(obj.toString());
			}
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
}
