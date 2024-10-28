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
	private Generalparameter cold_out = null;
	private Generalparameter floorSeparation = null;
	
	
	public UtilService() {}

	@Override
	public Separations<ForkliftSeparation, FloorSeparation, ForkliftSeparation> stateSeparation(LoadOrder order, List<Chamber> chambers, ConfigManager propert ) {
		boolean executed = false;
		boolean qtdeSatisfiend = false;
		int sum = 0;
		
		for (Map.Entry<Object, Object> entry : propert.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			
			switch (key) {
			case "congelado": {
				frozen = new Frozen(value);
				break;
			}
			case "resfri_dentro_estado": {
				cold_out = new Cold_in_state(value);
				break;
			}
			case "separacao_chao": {
				floorSeparation = new Floor_separation(value);
				break;
			}
			default:
				continue;
			}
        }
		
		for (LoadOrder.Product p : order.getProducts()) {
			partialProducts.put(p.note(), filterChamber(p.note(), chambers));
		}
		
										System.out.println("Antes do filtro de quantidade.");
										for (LoadOrder.Product lp : order.getProducts()) {
											partialProducts.get(lp.note()).forEach( x ->  System.out.println(x + (x.visited ? " = visited" : " = nao")));
										
										}
		
		Map<Integer, List<Product>> frozenProducts = new HashMap<>();
		Map<Integer, List<Product>> coldProducts = new HashMap<>();
		Map<Integer, List<Product>> floorProducts = new HashMap<>();
		List<Product> frozenList = new ArrayList<>();
		List<Product> coldList = new ArrayList<>();
		List<Product> floorList = new ArrayList<>();
		Entry<Integer, Product> result = null;
		Product temporary = null;
		
		for (LoadOrder.Product lp : order.getProducts()) {
			do {
				if(!(executed)) {
					executed = true;
					/*LOOP FOR PARA PERCORRER AS LISTAS DE PRODUTOS CONTIDAS NO MAP DE LISTAS DE PRODUTOS DENOMINADTO PARTIAL_PRODUCTS, TESTANDO
					 * E ADICIONANDO AS LISTAS FROZENLIST, COLDLIST E FLOORLIST, SEGUNDO OS TESTE DAS CONDICIONAIS. E DEPOIS ADICIONANDO OS RESULTADOS 
					 * AOS MAP'S RELACIONADOS.*/ 
					for (Product product : partialProducts.get(lp.note())) {
						if((product.getHeight() == 1) && (product.getDeoth() == 0 || product.getDeoth() == 1 || product.getDeoth() == 2) && !(product.visited) && floorSeparation.test(product) ) {
							product.visited  = true;
							floorList.add(product);
						}		
						if(!(floorList.isEmpty())) {
							floorList = floorList.stream().filter(floorSeparation).collect(Collectors.toList());
						}
						if(product.isFrozen && !(product.visited)) {
							frozenList.add(product);
						}
						if(!(product.isFrozen) && !(product.visited)){
							coldList.add(product);
						}
					}
					if(!(frozenList.isEmpty())) {
						frozenList = frozenList.stream().filter(frozen).collect(Collectors.toList());
						frozenProducts.put(lp.note(), new ArrayList<>(frozenList));
					}
					if(!(coldList.isEmpty())) {
						coldList = coldList.stream().filter(cold_out).collect(Collectors.toList());
						coldProducts.put(lp.note(), new ArrayList<>(coldList));
					}
					floorProducts.put(lp.note(), new ArrayList<>(floorList));
				}
				
				//PEGA O PRODUTO MAIS VELHO QUE NAO FOI CONTEMPLADO NO PASSO ANTERIOR.
				if(executed) {
					result = older(lp.note(), partialProducts.get(lp.note()));
					temporary = result.getValue();
					
					if(temporary != null) {
						
						partialProducts.get(lp.note()).get(result.getKey()).visited = true;
						if(temporary.isFrozen) {
							frozenProducts.get(lp.note()).add(temporary);
						}
						if(!(temporary.isFrozen)){
							coldProducts.get(lp.note()).add(temporary);
						}
					}
				}
				
				//CALCULANDO A QUANTIDADE DO PRODUTO CORRENTE.
				if(!(frozenProducts.isEmpty())) {
					for (Product product : frozenProducts.get(lp.note())) {
						sum += product.getBoxes();
					}
				}
				if(!(coldProducts.isEmpty()))
					for (Product product : coldProducts.get(lp.note())) {
						sum += product.getBoxes();
					}
				if(!(floorProducts.isEmpty())) {
					for (Product product : floorProducts.get(lp.note())) {
						sum += product.getBoxes();
					}
				}
				if(sum >= lp.qtdeBoxes()) {
					qtdeSatisfiend = true;
				}
				
				sum = 0;
				//CONTINUAR...
			} while (!(qtdeSatisfiend || !(partialProducts.get(lp.note()).stream().anyMatch(x -> x.visited == false))));	
		}
										/*
										for (LoadOrder.Product lp : order.getProducts()) {
											if(!(frozenProducts.isEmpty())) { 
												System.out.println("lista congelados");
												frozenProducts.get(lp.note()).forEach( x ->  System.out.println(x + (x.visited ? " = visited" : " = não")));
											}
											if(!(coldProducts.isEmpty())) {
												System.out.println("lista resfriados");
												coldProducts.get(lp.note()).forEach( x ->  System.out.println(x + (x.visited ? " = visited" : " = não")));
											}
											if(!(floorProducts.isEmpty())) {
												System.out.println("lista do chao");
												floorProducts.get(lp.note()).forEach( x ->  System.out.println(x + (x.visited ? " = visited" : " = não")));
											}
										}
										
										System.out.println("depois do filtro de quantidade.");
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
		
		/*System.out.println("finalListOfProducts");
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
