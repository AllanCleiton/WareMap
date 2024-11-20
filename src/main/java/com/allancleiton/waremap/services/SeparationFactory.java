package com.allancleiton.waremap.services;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.allancleiton.waremap.config.parameters.Cold_in_state;
import com.allancleiton.waremap.config.parameters.Floor_separation;
import com.allancleiton.waremap.config.parameters.Frozen;
import com.allancleiton.waremap.config.parameters.GeneralParameter;
import com.allancleiton.waremap.entities.Chamber;
import com.allancleiton.waremap.entities.LoadOrder;
import com.allancleiton.waremap.entities.Order;
import com.allancleiton.waremap.entities.Product;
import com.allancleiton.waremap.entities.Road;
import com.allancleiton.waremap.entities.Separation;
import com.allancleiton.waremap.entities.enums.SeparationSet;
import com.allancleiton.waremap.repository.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SeparationFactory{
	private List<Chamber> chambers = new ArrayList<>();
	public Repository repository;
	private LoadOrder order = null;
	private final Integer numberOFChambers = 5;
	
	
	
	
	public SeparationFactory(Repository repository) throws IOException{
		this.repository = repository;
		this.order = repository.jsonToLoadOrder(repository.LoadOrder());
		loadCameras(this.numberOFChambers);
	}
	
	public SeparationSet<Separation, Separation, Separation> stateSeparation(String path ) {
		Map<Integer, List<Product>> partialProducts = new HashMap<>();
		GeneralParameter frozen = null;
		GeneralParameter cold_in = null;
		Floor_separation floorSeparation = null;
		LoadOrder floorOrder = null;
		
		for (Order p : order.getOrders()) {
			partialProducts.put(p.note(), filterChamber(p.note(), this.chambers));
		}
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			frozen = objectMapper.readValue(new File(path + "/config/geralParameters/frozen.json"), Frozen.class);
			cold_in = objectMapper.readValue(new File(path + "/config/geralParameters/cold_in_state.json"), Cold_in_state.class);
			floorSeparation = objectMapper.readValue(new File(path + "/config/geralParameters/floor_separation.json"), Floor_separation.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		floorOrder = new LoadOrder(order.getOrders().stream().filter(floorSeparation).collect(Collectors.toList()), order.getOrderCharger()); 	
		
		order.getOrders().forEach(p -> {
											partialProducts.put(p.note(), filterChamber(p.note(), chambers));
										});
										
										/*
										System.out.println("Antes do filtro de quantidade.");
										for (LoadOrder.Product lp : order.getProducts()) {
											partialProducts.get(lp.note()).forEach( x ->  System.out.println(x + (x.visited ? " = visited" : " = nao")));
										}*/
		
		Map<Integer, List<Product>> frozenProducts = new HashMap<>();
			order.getOrders().forEach(x -> {frozenProducts.put(x.note(), new ArrayList<>());});
		Map<Integer, List<Product>> coldProducts = new HashMap<>();
			order.getOrders().forEach(x -> {coldProducts.put(x.note(), new ArrayList<>());});
		Map<Integer, List<Product>> floorProducts = new HashMap<>();
			order.getOrders().forEach(x -> {floorProducts.put(x.note(), new ArrayList<>());});
		List<Product> frozenList = new ArrayList<>();
		List<Product> coldList = new ArrayList<>();
		List<Product> floorList = new ArrayList<>();
		Entry<Integer, Product> result = null;
		Product temporary = null;
		
		for (Order lp : order.getOrders()) {
			Order lpFlor = floorOrder.getOrders().stream().filter(x -> x.note().equals(lp.note())) .findFirst().orElse(null);      
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
							if(!(product.visited) 
									&& product.getHeight() == 1 
									&& (product.getDeoth() == 0 || product.getDeoth() == 1 || product.getDeoth() == 2)
									&& lpFlor != null) {
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
								
								if(sumFrozen > lp.qtdeBoxes()) {
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
		
		
		SeparationSet<Separation, Separation, Separation> separations = new SeparationSet<Separation, Separation, Separation>(
				new Separation(frozenProducts, order), 
				new Separation(floorProducts, order), 
				new Separation(coldProducts, order)
		);
						
		return separations;
	}
	
	public Separation simpleSeparation() {
		Map<Integer, List<Product>> partialProducts = new HashMap<>();
		for (Order p : order.getOrders()) {
			partialProducts.put(p.note(), filterChamber(p.note(), this.chambers));
		}
		
		List<Product> aux = new ArrayList<>();
		List<Product> listActual = null;
		Entry<Integer, Product> result = null;
 		int acumulation = 0;
		Product actual = null;
 		for (Order lp : order.getOrders()) {
 			listActual = partialProducts.get(lp.note());
 			
			for(int i = 0; i <= listActual.size(); i++) {	
				result = easier(lp.note(), listActual);
				
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
		
 		
		return new Separation(partialProducts, order);
	}
	
	private void loadCameras(int numberOfChambers) throws IOException{
		//final String defaultPath = "/chambers.xlsx";
		for(int i = 0; i < numberOfChambers; i++) {
			this.chambers.add(new Chamber((i+1), 20, repository.jsonToList(repository.LoadProducts())));
		}
		this.chambers.removeIf(x -> x.isEmpty());
		
	}

	public List<Chamber> getChambers() {
		return chambers;
	}
	
	private Entry<Integer, Product> easier(int code, List<Product> products) {
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

	public LoadOrder getLoadOrder() {
		return this.order;
	}
	
}
