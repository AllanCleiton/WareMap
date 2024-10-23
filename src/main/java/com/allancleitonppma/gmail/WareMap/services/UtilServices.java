package com.allancleitonppma.gmail.WareMap.services;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import com.allancleitonppma.gmail.WareMap.core.LoadOrder;
import com.allancleitonppma.gmail.WareMap.core.Separation;
import com.allancleitonppma.gmail.WareMap.entities.Chamber;
import com.allancleitonppma.gmail.WareMap.entities.Product;
import com.allancleitonppma.gmail.WareMap.entities.Road;



public interface UtilServices {
	
	Separation customSeparation(LoadOrder order, List<Chamber> chambers);
	
	Separation simpleSeparation(LoadOrder order, List<Chamber> chambers);
	
	
	default List<Product> loadListOfProduct(String path) throws Exception{
		List<Product> listProducts = new ArrayList<>();
		String line;
		String[] fields;
		
		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
			while((line = br.readLine()) != null) {
				fields = line.split(",");
				listProducts.add(
						new Product(
								Integer.parseInt(fields[0]), 
								Integer.parseInt(fields[1]), 
								Integer.parseInt(fields[2]), 
								Integer.parseInt(String.valueOf(fields[3].charAt(3))), 
								Integer.parseInt(fields[4]), 
								Integer.parseInt(String.valueOf(fields[5].charAt(2))), 
								fields[6], 
								Integer.parseInt(fields[7])));
			}
			
			
			
		} 
		return listProducts;
	}

	default LoadOrder getloadOrder(String path) throws Exception{
		String os = System.getProperty("os.name");
		String defaultPath;
		if (os.contains("Windows")) {
			defaultPath = "//ordercharger.txt";
		} else {
			defaultPath = "/ordercharger.txt";
        }
		
		List<LoadOrder.Product> orders = new ArrayList<>();
		String line;
		String[] fields;
		
		try(BufferedReader br = new BufferedReader(new FileReader(path + defaultPath))) {
			while((line = br.readLine()) != null) {
				fields = line.split(",");
				orders.add(new LoadOrder.Product(Integer.parseInt(fields[0]), Integer.parseInt(fields[2])));
	
			}
			
		}
		
		return new LoadOrder(orders);
	}
	
	default LoadOrder getloadOrder(String path, String orederCharger) throws Exception{
		String os = System.getProperty("os.name");
		String defaultPath;
		if (os.contains("Windows")) {
			defaultPath = "//ordercharger.txt";
		} else {
			defaultPath = "/ordercharger.txt";
        }
		List<LoadOrder.Product> orders = new ArrayList<>();
		String line;
		String[] fields;
		
		try(BufferedReader br = new BufferedReader(new FileReader(path + defaultPath))) {
			while((line = br.readLine()) != null) {
				fields = line.split(",");
				orders.add(new LoadOrder.Product(Integer.parseInt(fields[0]), Integer.parseInt(fields[2])));
	
			}
			
		}
		
		return new LoadOrder(orders, orederCharger);
	}
	
	default List<Chamber> chargeCameras(String path, int numberOfChambers, Scanner scanner) throws Exception{
		String os = System.getProperty("os.name");
		String defaultPath;
		if (os.contains("Windows")) {
			defaultPath = "//chambers.txt";
		} else {
			defaultPath = "/chambers.txt";
        }
		
		List<Chamber> chambers = new ArrayList<>();
		

		for(int i = 0; i < numberOfChambers; i++) {
			chambers.add(new Chamber((i+1), 20, loadListOfProduct(path + defaultPath)));
		}
			 
			
		chambers.removeIf(x -> x.isEmpty());
		
		return chambers;
	}

	default void printRoadOfChamber(List<Chamber> chambers, Predicate<Chamber> filter){

		for (Chamber c : chambers) {
			if(filter.test(c)) {
				for (Road r : c.getAllRoads()) {
					if(r != null) {
						System.out.println(r.toString());
					}
				}
			}
		}
	}

}
