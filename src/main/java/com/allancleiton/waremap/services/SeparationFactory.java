package com.allancleiton.waremap.services;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.allancleiton.waremap.WareMapApplication;
import com.allancleiton.waremap.config.parameters.Cold_in_state;
import com.allancleiton.waremap.config.parameters.Cold_out_state;
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
import com.allancleiton.waremap.exceptions.NoSuchElement;
import com.allancleiton.waremap.repository.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SeparationFactory{
	private List<Chamber> chambers = null;
	public Repository repository;
	private LoadOrder order = null;
	private final Integer numberOFChambers = 5;
	private Scanner sc;
	
	
	
	
	public SeparationFactory(Repository repository, Scanner sc) throws IOException{
		this.repository = repository;
		this.order = repository.jsonToLoadOrder(repository.LoadOrder());
		this.chambers = loadCameras(numberOFChambers);
		this.sc = sc;
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
		
		try {
			floorOrder = new LoadOrder(order.getOrders().stream().filter(floorSeparation).collect(Collectors.toList()), order.getOrderCharger());
		} catch (NoSuchElement e) {
			// TODO Auto-generated catch block
			System.out.println("ex...73");		
		} 	
		
		order.getOrders().forEach(p -> {
											partialProducts.put(p.note(), filterChamber(p.note(), chambers));
										});
										
										
		
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
		Order lpFlor = null;
		
		//ESTA LISTA É UMA COPIA DA LISTA DE ORDEM PRINCIPAL DESTA CLASSE
		List<Order> auxOrder = new ArrayList<>(order.getOrders());
				
		for (Order lp : auxOrder) {
			//TRECHO EXCLUSIVO PARA PROCEDIMENTO COM O PRODUTO 11046.
			if(lp.note() == 11046) {
				TupleListProducts tuple = exclusive11046Separation(frozenList, floorList, floorOrder, lp, partialProducts, floorSeparation, frozen, sc);
				if (tuple != null) {
					frozenProducts.get(lp.note()).addAll(tuple.frozenProducts);
					floorProducts.get(lp.note()).addAll(tuple.floorProducts);
					continue;
				}
			}
			
			if(floorOrder != null) {
				lpFlor = floorOrder.getOrders().stream().filter(x -> x.note().equals(lp.note())) .findFirst().orElse(null);      
			}
			boolean executed = false;
			boolean frozenIsOk = false;
			boolean coldIsOk = false;
			boolean floorIsOk = false;
			
			boolean listOnlyOneIten = false; //reference -> conditional line 249
			boolean _ifexecuted = false; //reference -> if(partialProducts.get(lp.note()).size() == 1 && y == false) line 248
			
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
							}else {
								temporary.visited = false;
							}
						}
					}
					if(!(frozenList.isEmpty()) && !(frozenIsOk) && !(floorIsOk)) {
						
						for (@SuppressWarnings("unused") Product p : frozenList) {
							result = older(lp.note(), frozenList);
							temporary = result.getValue();
							if(frozen.test(temporary) && temporary != null && !(frozenIsOk)) {
								
								sumFrozen += temporary.getBoxes() + sumfloor;
								frozenProducts.get(lp.note()).add(temporary);
								
								if(sumFrozen > lp.qtdeBoxes()) {
									frozenIsOk = true;
									break;
								}
							}else {
								temporary.visited = false;
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
				
				/*ESTE TREXO DO CODIGO SERVE PARA CORRIGIR O ERRO DE FIM DO LAÇO WHILE, 
				 * QUANDO NA LISTA partialProduct NA POSICAO lp.note(), EXISTE APENAS 1 ELEMENTO.
				*/
				if(partialProducts.get(lp.note()).size() == 1 && _ifexecuted == false) {
					listOnlyOneIten = true;
					_ifexecuted = true;
				}else {
					listOnlyOneIten = false;
				}
			} while (!((frozenIsOk || coldIsOk || floorIsOk)|| !(partialProducts.get(lp.note()).stream().anyMatch(x -> x.visited == false))) || listOnlyOneIten);	
		}
		
		
		SeparationSet<Separation, Separation, Separation> separations = new SeparationSet<Separation, Separation, Separation>(
				new Separation(frozenProducts, order), 
				new Separation(floorProducts, order), 
				new Separation(coldProducts, order)
		);
						
		return separations;
	}
	
	public SeparationSet<Separation, Separation, Separation> outOfStateSeparation(String path ) {
		Map<Integer, List<Product>> partialProducts = new HashMap<>();
		GeneralParameter frozen = null;
		GeneralParameter cold_out = null;
		Floor_separation floorSeparation = null;
		LoadOrder floorOrder = null;
		
		for (Order p : order.getOrders()) {
			partialProducts.put(p.note(), filterChamber(p.note(), this.chambers));
		}
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			frozen = objectMapper.readValue(new File(path + "/config/geralParameters/frozen.json"), Frozen.class);
			cold_out = objectMapper.readValue(new File(path + "/config/geralParameters/cold_out_state.json"), Cold_out_state.class);
			floorSeparation = objectMapper.readValue(new File(path + "/config/geralParameters/floor_separation.json"), Floor_separation.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			floorOrder = new LoadOrder(order.getOrders().stream().filter(floorSeparation).collect(Collectors.toList()), order.getOrderCharger());
		} catch (NoSuchElement e) {
			// TODO Auto-generated catch block
			System.out.println("ex...260");
		} 	
		
		//PREENCHE A LISTA partialProducts COM TODOS OS DE CADA PRODUTO DA ORDEM DE CARGA.
		order.getOrders().forEach(p -> {
											partialProducts.put(p.note(), filterChamber(p.note(), chambers));
										});
										
										/*
										System.out.println("Antes do filtro de quantidade.");
										for (LoadOrder.Product lp : order.getProducts()) {
											partialProducts.get(lp.note()).forEach( x ->  System.out.println(x + (x.visited ? " = visited" : " = nao")));
										}*/
		
		//LISTAS QUE SERAM RETORNADAS POR ESTE METODO.
		Map<Integer, List<Product>> frozenProducts = new HashMap<>();	
		Map<Integer, List<Product>> coldProducts = new HashMap<>();
		Map<Integer, List<Product>> floorProducts = new HashMap<>();
			//INICIA A LISTA frozenProducts, PARA CADA CODIGO DE PRODUTO NA ORDEM DE CARGA, INICIA-SE UMA LISTA VAZIA, AFIM DE EVITAR EXCESOES.
			order.getOrders().forEach(x -> {frozenProducts.put(x.note(), new ArrayList<>());});
			order.getOrders().forEach(x -> {coldProducts.put(x.note(), new ArrayList<>());});
			order.getOrders().forEach(x -> {floorProducts.put(x.note(), new ArrayList<>());});

		//LISTAS AUXILIARES PARA A INTERACAO NO LACO FOR PRINCIPAL.
		List<Product> frozenList = new ArrayList<>();
		List<Product> coldList = new ArrayList<>();
		List<Product> floorList = new ArrayList<>();
	
		Entry<Integer, Product> result = null;
		Product temporary = null;
		Order lpFlor = null;
		
		//ESTA LISTA É UMA COPIA DA LISTA DE ORDEM PRINCIPAL DESTA CLASSE
		List<Order> auxOrder = new ArrayList<>(order.getOrders());
		
		for (Order lp : auxOrder) {
			//TRECHO EXCLUSIVO PARA PROCEDIMENTO COM O PRODUTO 11046.
			if(lp.note() == 11046) {
				TupleListProducts tuple = exclusive11046Separation(frozenList, floorList, floorOrder, lp, partialProducts, floorSeparation, frozen, sc);
				if (tuple != null) {
					frozenProducts.get(lp.note()).addAll(tuple.frozenProducts);
					floorProducts.get(lp.note()).addAll(tuple.floorProducts);
					continue;
				}
			}
			
			if(floorOrder != null) {
				lpFlor = floorOrder.getOrders().stream().filter(x -> x.note().equals(lp.note())) .findFirst().orElse(null);      
			}
			boolean executed = false;
			boolean frozenIsOk = false;
			boolean coldIsOk = false;
			boolean floorIsOk = false;
			
			boolean listOnlyOneIten = false; 
			boolean _ifexecuted = false; 
			
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
							}else {
								temporary.visited = false;
							}
						}
					}
					if(!(frozenList.isEmpty()) && !(frozenIsOk) && !(floorIsOk)) {
						
						for (@SuppressWarnings("unused") Product p : frozenList) {
							result = older(lp.note(), frozenList);
							temporary = result.getValue();
							if(frozen.test(temporary) && temporary != null && !(frozenIsOk)) {
								
								sumFrozen += temporary.getBoxes() + sumfloor;
								frozenProducts.get(lp.note()).add(temporary);
								
								if(sumFrozen > lp.qtdeBoxes()) {
									frozenIsOk = true;
									break;
								}
							}else {
								temporary.visited = false;
							}
						}
						
						
					}
					if(!(coldList.isEmpty()) && !(coldIsOk)) {
						
						for (@SuppressWarnings("unused") Product p : coldList) {
							result = older(lp.note(), coldList);
							temporary = result.getValue();
							if(cold_out.test(temporary) && temporary != null && !(coldIsOk)) {
								
								sumCold += temporary.getBoxes() + sumfloor;
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
				/*ESTE TREXO DO CODIGO SERVE PARA CORRIGIR O ERRO DE FIM DO LAÇO WHILE, 
				 * QUANDO NA LISTA partialProduct NA POSICAO lp.note(), EXISTE APENAS 1 ELEMENTO.
				*/
				if(partialProducts.get(lp.note()).size() == 1 && _ifexecuted == false) {
					listOnlyOneIten = true;
					_ifexecuted = true;
				}else {
					listOnlyOneIten = false;
				}
			} while (!((frozenIsOk || coldIsOk || floorIsOk)|| !(partialProducts.get(lp.note()).stream().anyMatch(x -> x.visited == false))) || listOnlyOneIten);	
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
	
	private List<Chamber> loadCameras(int numberOfChambers) throws IOException{
		List<Chamber> chambers = new ArrayList<>();
		//final String defaultPath = "/chambers.xlsx";
		for(int i = 0; i < numberOfChambers; i++) {
			chambers.add(new Chamber((i+1), 20, repository.jsonToList(repository.LoadProducts())));
		}
		chambers.removeIf(x -> x.isEmpty());
		return chambers;
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
	
	private TupleListProducts exclusive11046Separation(
			List<Product> frozenList,
			List<Product> floorList,
			LoadOrder floorOrder,
			Order lp,
			Map<Integer, List<Product>> partialProducts,
			Floor_separation floorSeparation,
			GeneralParameter frozen,
			Scanner scan){
		Entry<Integer, Product> result = null;
		Product temporary = null;
		
		int _11046_7;
		int _11046_8;
		int _11046_9;
		int _11046_10;
		int _11046_11;
		String choice;
		System.out.println("----------------------------------------");
		System.out.println("* (O sistema detectou o cóodigo 11046) *");
		System.out.println("----------------------------------------");

		System.out.println(" Deseja especificar a quantidade individual?");
		System.out.print(" S/N -> ");
		choice = scan.nextLine().toUpperCase().trim();
			
		if(choice.equals("S")) {
			List<Order> orders11046 = new ArrayList<>();
			Map<Integer, List<Product>> frozenProducts = new HashMap<>();
			Map<Integer, List<Product>> floorProducts = new HashMap<>();

			Order actual = null;
			int choice_ = 2;
			do {
				
				//fazer debug aqui para verificar erro de illegalargumentexception the kist are empty.
				System.out.println(" Informe as quantidades para cada tipo de 11046:");
				System.out.print(" \nPara 11046 c/ 7 caixas: "); _11046_7 = scan.nextInt();
				System.out.print(" \nPara 11046 c/ 8 caixas: "); _11046_8 = scan.nextInt();
				System.out.print(" \nPara 11046 c/ 9 caixas: "); _11046_9 = scan.nextInt();
				System.out.print(" \nPara 11046 c/ 10 caixas: "); _11046_10 = scan.nextInt();
				System.out.print(" \nPara 11046 c/ 11 caixas: "); _11046_11 = scan.nextInt();
				WareMapApplication.clearScreen();
				
				System.out.println(" Verifique os dados ");
				System.out.println(" 11046 c/ 7 caixas = " + _11046_7);
				System.out.println(" 11046 c/ 8 caixas = " + _11046_8);
				System.out.println(" 11046 c/ 9 caixas = " + _11046_9); 
				System.out.println(" 11046 c/ 10 caixas = " + _11046_10); 
				System.out.println(" 11046 c/ 11 caixas = " + _11046_11); 
				System.out.println(" Confirmar:....................(1)");
				System.out.println(" Alterar:......................(2)");
				System.out.println(" Cancelar:.....................(0)");
				System.out.print(" -> ");
				choice_ = scan.nextInt();
				scan.nextLine();
				WareMapApplication.clearScreen();

				if(choice_ == 0) {
					return null;
				}
				else if(choice_ == 1) {
					Integer[] list = {_11046_7,_11046_8,_11046_9,_11046_10,_11046_11};
					
					for(int i =0; i < 4; i++) {
						if (list[i] > 0) {
							actual = new Order(11046, i+7, list[i]);
							order.setOrder(actual);
							orders11046.add(actual);
							order.remeveOrder(lp);
						}
					}
					
					break;
				}
				
			}while(choice_ == 2);
			
			try {
				floorOrder = new LoadOrder(orders11046.stream().filter(floorSeparation).collect(Collectors.toList()), order.getOrderCharger());
			} catch (NoSuchElement e) {
				// TODO Auto-generated catch block
				System.out.println("ex...656");
			} 	
			
			orders11046.forEach(x -> {frozenProducts.put(x.note(), new ArrayList<>());});
			orders11046.forEach(x -> {floorProducts.put(x.note(), new ArrayList<>());});

			
			Order lpFlor = null;
			for (Order order : orders11046) {
				if(floorOrder != null) {
					lpFlor = floorOrder.getOrders().stream().filter(x -> x.note().equals(lp.note())) .findFirst().orElse(null);      
				}
				boolean executed = false;
				boolean frozenIsOk = false;
				boolean floorIsOk = false;
				int sumfloor = 0;
				int sumFrozen = 0;
				
				
				frozenList.clear();
				floorList.clear();
				externo:
				do {
					if(!(executed)) {
						//PEGA TODOS OS PRODUTOS COM CODIGO LP.note E ATRIBUE ÀS LISTAS -------------------
						for (Product product : partialProducts.get(order.note())) {
							boolean ok = false;
							
							if(product.getPackages() == order.packeges && product.isFrozen && !(product.visited)) {
								if( product.getHeight() == 1 && (product.getDeoth() == 0 || product.getDeoth() == 1 || product.getDeoth() == 2) && lpFlor != null){
									ok = floorList.add(product);
								}
								if(!(ok)){ frozenList.add(product);}
							}
							
						}//FIM LAÇO FOR-------------------------------------------------
						
						//ATUALIZANDO OS MAP'S frozenProducts e coldProducts
						if(!(floorList.isEmpty()) && !(floorIsOk) && lpFlor != null) {
							for (int i = 0; i < floorList.size(); i++) {
								
								result = older(order.note(), floorList);
								temporary = result.getValue();
								
								if(floorSeparation.test(order) && temporary != null && !(floorIsOk)) {
									
									boolean x = floorProducts.get(order.note()).add(temporary);  
									
									if(x) {
										sumfloor += temporary.getBoxes(); 
										temporary.visited = true;
										
										switch(temporary.getPackages()) {
											case 7:{
												temporary.activePackeg = true;
												break;
											}
											case 8:{
												temporary.activePackeg = true;
												break;
											}
											case 9:{
												temporary.activePackeg = true;
												break;
											}
											case 10:{
												temporary.activePackeg = true;
												break;
											}
											case 11:{
												temporary.activePackeg = true;
												break;
											}
												
										}
										
									}
									
									
									if(sumfloor > lpFlor.qtdeBoxes() ) {
										floorIsOk = true;
										break;
									}
								}
							}
						}
						if(!(frozenList.isEmpty()) && !(frozenIsOk) && !(floorIsOk)) {
							
							for (@SuppressWarnings("unused") Product p : frozenList) {
								result = older(order.note(), frozenList);
								temporary = result.getValue();
								if(frozen.test(temporary) && temporary != null && !(frozenIsOk)) {
									
									sumFrozen += temporary.getBoxes() + sumfloor;
									frozenProducts.get(order.note()).add(temporary);
									
									switch(temporary.getPackages()) {
										case 7:{
											temporary.activePackeg = true;
											break;
										}
										case 8:{
											temporary.activePackeg = true;
											break;
										}
										case 9:{
											temporary.activePackeg = true;
											break;
										}
										case 10:{
											temporary.activePackeg = true;
											break;
										}
										case 11:{
											temporary.activePackeg = true;
											break;
										}
										
									}
									if(sumFrozen >= order.qtdeBoxes()) {
										frozenIsOk = true;
										break;
									}
								}else {
									temporary.visited = false;
								}
							}
							
							
						}
						
					}
					
					//PEGA O PRODUTO MAIS VELHO QUE NAO FOI CONTEMPLADO NO PASSO ANTERIOR.
					if(executed) {
						result = older(order.note(), partialProducts.get(order.note())); //O METODO OLDER RETORNA O PRODUTO MAIS VELHO, E MARCA ELE COMO VISITADO.
						temporary = result.getValue(); // VARIAVEL TEMPORARY, RECEBE O VALOR DO RETORNO DE OLDER.
						
						//SE A CONDICIONAL ABAIXO NAO FOR EXECUTADA, É PRECISO ALTERAR O CONTEUDO DE TEMPORARY, EM VISITED PARA FALSE.
						if(temporary.getPackages() == order.packeges && temporary != null && temporary.isFrozen && !(frozenIsOk)) {
							sumFrozen += temporary.getBoxes();
							frozenProducts.get(order.note()).add(temporary);
							
							switch(temporary.getPackages()) {
								case 7:{
									temporary.activePackeg = true;
									break;
								}
								case 8:{
									temporary.activePackeg = true;
									break;
								}
								case 9:{
									temporary.activePackeg = true;
									break;
								}
								case 10:{
									temporary.activePackeg = true;
									break;
								}
								case 11:{
									temporary.activePackeg = true;
									break;
								}
								
							}
							
							
							if(sumFrozen > order.qtdeBoxes()) {
								frozenIsOk = true;
							}
						}		

					}
					executed = true;
					
					if(sumFrozen >= order.qtdeBoxes()) {
						frozenIsOk = true;
						break externo;
					}
					
					
				} while (!((frozenIsOk || floorIsOk)|| !(partialProducts.get(lp.note()).stream().anyMatch(x -> x.visited == false)) ));	
			}
			return new TupleListProducts(floorProducts.get(lp.note()), frozenProducts.get(lp.note())); 
		}	
		return null;

	}
	
	private record TupleListProducts(List<Product> floorProducts, List<Product> frozenProducts) {}
}
