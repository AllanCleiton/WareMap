package com.allancleitonppma.gmail.WareMap.services;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.allancleitonppma.gmail.WareMap.DTO.ChamberDto;
import com.allancleitonppma.gmail.WareMap.DTO.Position;
import com.allancleitonppma.gmail.WareMap.DTO.ProductDto;
import com.allancleitonppma.gmail.WareMap.DTO.RoadDto;
import com.allancleitonppma.gmail.WareMap.config.Cold_in_state;
import com.allancleitonppma.gmail.WareMap.config.ConfigManager;
import com.allancleitonppma.gmail.WareMap.config.Floor_separation;
import com.allancleitonppma.gmail.WareMap.config.Frozen;
import com.allancleitonppma.gmail.WareMap.config.Generalparameter;
import com.allancleitonppma.gmail.WareMap.core.FloorSeparation;
import com.allancleitonppma.gmail.WareMap.core.ForkliftSeparation;
import com.allancleitonppma.gmail.WareMap.core.LoadOrder;
import com.allancleitonppma.gmail.WareMap.core.Separation;
import com.allancleitonppma.gmail.WareMap.core.Separations;
import com.allancleitonppma.gmail.WareMap.entities.Chamber;
import com.allancleitonppma.gmail.WareMap.entities.Product;
import com.allancleitonppma.gmail.WareMap.entities.Road;


public class UtilService implements UtilServices{
	private Map<Integer, List<Product>> partialProducts = new HashMap<>();
	private Generalparameter frozen = null;
	private Generalparameter cold_in = null;
	private Floor_separation floorSeparation = null;
	private LoadOrder floorOrder = null;
	
	
	public UtilService() {}

	@Override
	public Separations<ForkliftSeparation, FloorSeparation, ForkliftSeparation> stateSeparation(LoadOrder order, List<Chamber> chambers, ConfigManager propert ) {
		for (Map.Entry<Object, Object> entry : propert.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			
			switch (key) {	
				case "congelado": {	frozen = new Frozen(value); break;}
				case "resfri_dentro_estado": {cold_in = new Cold_in_state(value); break;}
				case "separacao_chao": {floorSeparation = new Floor_separation(value); break;}
				default:
					continue;
				}
        }
		
		floorOrder = new LoadOrder(order.getProducts().stream().filter(floorSeparation).collect(Collectors.toList()), order.getOrderCharger()); 
		//forkliftOrder = new LoadOrder(order.getProducts().stream().filter(notFloor).collect(Collectors.toList()), order.getOrderCharger());
	
		
		order.getProducts().forEach(p -> {
											partialProducts.put(p.note(), filterChamber(p.note(), chambers));
										});
										
										/*
										System.out.println("Antes do filtro de quantidade.");
										for (LoadOrder.Product lp : order.getProducts()) {
											partialProducts.get(lp.note()).forEach( x ->  System.out.println(x + (x.visited ? " = visited" : " = nao")));
										}*/
		
		Map<Integer, List<Product>> frozenProducts = new HashMap<>();
			order.getProducts().forEach(x -> {frozenProducts.put(x.note(), new ArrayList<>());});
		Map<Integer, List<Product>> coldProducts = new HashMap<>();
			order.getProducts().forEach(x -> {coldProducts.put(x.note(), new ArrayList<>());});
		Map<Integer, List<Product>> floorProducts = new HashMap<>();
			order.getProducts().forEach(x -> {floorProducts.put(x.note(), new ArrayList<>());});
		List<Product> frozenList = new ArrayList<>();
		List<Product> coldList = new ArrayList<>();
		List<Product> floorList = new ArrayList<>();
		Entry<Integer, Product> result = null;
		Product temporary = null;
		
		for (LoadOrder.Product lp : order.getProducts()) {
			LoadOrder.Product lpFlor = floorOrder.getProducts().stream().filter(x -> x.note().equals(lp.note())) .findFirst().orElse(null);      
			boolean executed = false;
			boolean frozenIsOk = false;
			boolean coldIsOk = false;
			boolean floorIsOk = false;
			int sumfloor = 0;
			int sumFrozen = 0;
			int sumCold = 0;
			frozenList.clear();
			coldList.clear();
			floorList.clear();
			externo:
			do {
				if(!(executed)) {
					//INICIO LAÇO FOR-----------------------------------------------
					for (Product product : partialProducts.get(lp.note())) {
						boolean ok = false;
						if(product.isFrozen && !(product.visited)) {
							if(!(product.visited) && product.getHeight() == 1 && (product.getDeoth() == 0 || product.getDeoth() == 1 || product.getDeoth() == 2)) {
								ok = floorList.add(product);
							}
							if(!(ok)){ frozenList.add(product);}
						}
						if(!(product.isFrozen) && !(product.visited)){
							if(!(product.visited) && product.getHeight() == 1 && (product.getDeoth() == 0 || product.getDeoth() == 1 || product.getDeoth() == 2)) {
								ok = floorList.add(product);
							}
							if(!(ok)){ coldList.add(product);}
						}
					}//FIM LAÇO FOR-------------------------------------------------
					
					//ATUALIZANDO OS MAP'S frozenProducts e coldProducts
					if(!(floorList.isEmpty()) && !(floorIsOk) && lpFlor != null) {
						for (Product p : floorList) {
							result = older(lp.note(), floorList);
							temporary = result.getValue();
							
							if(floorSeparation.test(lp) && temporary != null && !(floorIsOk)) {
								
								boolean x = floorProducts.get(lp.note()).add(temporary);  
								
								if(x) {sumfloor += p.getBoxes(); temporary.visited = true;}
								
								
								if(sumfloor >= lpFlor.qtdeBoxes() ) {
									floorIsOk = true;
									break;
								}
							}
						}
					}
					if(!(frozenList.isEmpty()) && !(frozenIsOk) && !(floorIsOk)) {
						
						for (Product p : frozenList) {
							result = older(lp.note(), frozenList);
							temporary = result.getValue();
							if(frozen.test(temporary) && temporary != null && !(frozenIsOk)) {
								
								sumFrozen += p.getBoxes() + sumfloor;
								frozenProducts.get(lp.note()).add(temporary);
								
								if(sumFrozen >= lp.qtdeBoxes()) {
									frozenIsOk = true;
									break;
								}
							}
						}
						
						
					}
					if(!(coldList.isEmpty()) && !(coldIsOk)) {
						
						for (Product p : coldList) {
							result = older(lp.note(), coldList);
							temporary = result.getValue();
							if(cold_in.test(temporary) && temporary != null && !(coldIsOk)) {
								
								sumCold += p.getBoxes() + sumfloor;
								coldProducts.get(lp.note()).add(temporary);
								
								if(sumCold >= lp.qtdeBoxes()) {
									coldIsOk = true;
									break;
								}
							}
						}
					}
					
				}
				
				//PEGA O PRODUTO MAIS VELHO QUE NAO FOI CONTEMPLADO NO PASSO ANTERIOR.
				if(executed) {
					result = older(lp.note(), partialProducts.get(lp.note()));
					temporary = result.getValue();
					
					if(temporary != null && temporary.isFrozen && !(frozenIsOk)) {
						sumFrozen += temporary.getBoxes();
						frozenProducts.get(lp.note()).add(temporary);
						if(sumFrozen >= lp.qtdeBoxes()) {
							frozenIsOk = true;
						}
					}if(temporary != null && !(temporary.isFrozen) && !(coldIsOk)) {
						sumCold += temporary.getBoxes();
						coldProducts.get(lp.note()).add(temporary);
						if(sumCold >= lp.qtdeBoxes()) {
							coldIsOk = true;
						}
					}
				}
				executed = true;
				
				if(sumFrozen >= lp.qtdeBoxes()) {
					frozenIsOk = true;
					break externo;
				}
				if(sumCold >= lp.qtdeBoxes()) {
					coldIsOk = true;
					break externo;
				}
			
			} while (!((frozenIsOk || coldIsOk || floorIsOk)|| !(partialProducts.get(lp.note()).stream().anyMatch(x -> x.visited == false))));	
		}
		
										/*
										for (LoadOrder.Product lp : floorOrder.getProducts()) {
											if(!(floorProducts.isEmpty())) {
												System.out.println("lista do chao");
												floorProducts.get(lp.note()).forEach( x ->  System.out.println(x + (x.visited ? " = visited" : " = não")));
											}
										}
										System.out.println();
										for (LoadOrder.Product lp : forkliftOrder.getProducts()) {
											if(!(frozenProducts.isEmpty())) { 
												System.out.println("lista congelados");
												frozenProducts.get(lp.note()).forEach( x ->  System.out.println(x + (x.visited ? " = visited" : " = não")));
											}
										}*/
										
										
										/*System.out.println("depois do filtro de quantidade.");
										for (LoadOrder.Product lp : order.getProducts()) {
											partialProducts.get(lp.note()).forEach( x ->  System.out.println(x + (x.visited ? " = visited" : " = nao")));
										
										}*/
		
		Separations<ForkliftSeparation, FloorSeparation, ForkliftSeparation> separations = new Separations<ForkliftSeparation, FloorSeparation, ForkliftSeparation>(
				new ForkliftSeparation(this.wareMap(frozenProducts, order), order.getOrderCharger()), 
				new FloorSeparation(this.wareMap(floorProducts, order), order.getOrderCharger()), 
				new ForkliftSeparation(this.wareMap(coldProducts, order), order.getOrderCharger())
		);
						
		return separations;
	}

	@Override
	public Separation simpleSeparation(LoadOrder order, List<Chamber> chambers) {
		for (LoadOrder.Product p : order.getProducts()) {
			partialProducts.put(p.note(), filterChamber(p.note(), chambers));
		}
		
		List<Product> aux = new ArrayList<>();
		List<Product> listActual = null;
		Entry<Integer, Product> result = null;
 		int acumulation = 0;
		Product actual = null;
 		for (LoadOrder.Product lp : order.getProducts()) {
 			listActual = partialProducts.get(lp.note());
 			
			for(int i = 0; i <= listActual.size(); i++) {	
				result = youngest(lp.note(), listActual);
				
				actual = result.getValue();
				
				if(actual != null) {
					acumulation += actual.getBoxes(); 
					aux.add(actual);
				}
				
				
				if(result.getKey() != -1) {
					listActual.remove((int) result.getKey());
					i--;
				}
				
				if(acumulation >= lp.qtdeBoxes()) {
					break;
				}
			    
			}
			
			partialProducts.replace(lp.note(), new ArrayList<>(aux));
			aux.clear();
			acumulation = 0;
		}
		
 		
		return new ForkliftSeparation(this.processPartialProducts(partialProducts, order), order.getOrderCharger());
	}
		
	private List<Product> filterChamber(int code, List<Chamber> chambers){
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

	private Entry<Integer, Product> youngest(int code, List<Product> products) {
		int index = -1;		
		int max = 10;
		int aux;
		boolean exists = false;
		Product product=null;
		
		do {
			exists = false;
			
			for (Product p : products) {
				aux = p.getHeight() + p.getDeoth();
				
				if (p.getDeoth() == 0) {
					return new SimpleEntry<>(products.indexOf(p), p);
				}
				
				if(aux < max ) {
					max = aux;
					product = p;
					exists = true;
					index = products.indexOf(p);
					
				}
			}
		}while(exists);  

		return new SimpleEntry<>(index, product);
	}
		
	private Entry<Integer, Product> older(int code, List<Product> products) {
		int index = -1;		
		int max = 365;
		int aux;
		boolean exists = false;
		Product product=null;
		
		do {
			exists = false;
			
			for (Product p : products) {
				if(p.visited == false) {	
					aux = p.getDays();
					
					if(aux < max) {
						max = aux;
						product = p;
						exists = true;
						index = products.indexOf(p);
						
					}
				}	
			}
		}while(exists);  
		
		products.get(index).visited = true;
		return new SimpleEntry<>(index, product);
	}
	
	//Metodo para processar o map com uma lista de produtos filtrados para cada produto, e gerar a relacao de separacao.
	private List<ProductDto> processPartialProducts(Map<Integer, List<Product>> partialProducts, LoadOrder order){
		
		Set<ProductDto> finalListOfProducts = new HashSet<>();
		Set<Position> positions = new HashSet<>();
		Set<RoadDto> roads = new HashSet<>();
		Set<ChamberDto> chams = new HashSet<>();
		
		
		for (LoadOrder.Product lp : order.getProducts()) {
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
		
		return new ArrayList<>(finalListOfProducts);
	}

	//Metodo para processar o map com uma lista de produtos filtrados para cada produto, e gerar a relacao de separacao.
	private List<ProductDto> wareMap(Map<Integer, List<Product>> partialProducts, LoadOrder order){
			
			Set<ProductDto> finalListOfProducts = new HashSet<>();
			Set<Position> positions = new HashSet<>();
			Set<RoadDto> roads = new HashSet<>();
			Set<ChamberDto> chams = new HashSet<>();
			
			List<Product> list = null;
			for (LoadOrder.Product lp : order.getProducts()) {
				if(!(partialProducts.isEmpty())) {
					list = partialProducts.get(lp.note());
					if (list != null) {
					    list.stream().forEach(x -> {
					        finalListOfProducts.add(new ProductDto(lp.note(), lp.qtdeBoxes()));
					        chams.add(new ChamberDto(x.getChamber(), x.getNote()));
					        roads.add(new RoadDto(x.getRoad(), x.getChamber(), x.getNote()));
					        positions.add(new Position(x.getHeight(), x.getCharDeoth(), x.getRoad(), x.getNote(), x.getChamber(), x.getDays(), x.getBoxes()));
					    });
					}	
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
