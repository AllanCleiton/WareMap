package com.allancleitonppma.gmail.WareMap.services;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.allancleitonppma.gmail.WareMap.core.LoadOrder;
import com.allancleitonppma.gmail.WareMap.core.Separation;
import com.allancleitonppma.gmail.WareMap.entities.Chamber;
import com.allancleitonppma.gmail.WareMap.entities.Product;
import com.allancleitonppma.gmail.WareMap.entities.Road;



public interface UtilServices {
	
	Separation customSeparation(LoadOrder order, List<Chamber> chambers);
	
	Separation simpleSeparation(LoadOrder order, List<Chamber> chambers);
	
	

	default LoadOrder getloadOrder(String path) throws IOException{
		final String defaultPath = "//ordercharger.xlsx";
		
		List<LoadOrder.Product> orders = new ArrayList<>();
		
		
		 try (FileInputStream file = new FileInputStream(path + defaultPath);
		            Workbook workbook = new XSSFWorkbook(file)) {

		            Sheet sheet = workbook.getSheetAt(0);
		            Iterator<Row> rowIterator = sheet.iterator();



		            while (rowIterator.hasNext()) {
		                Row row = rowIterator.next();
		                
		        		String[] fields = {
		        				String.valueOf(row.getCell(0).getNumericCellValue()).replace(".0", ""),
		        				String.valueOf(row.getCell(1).getNumericCellValue()).replace(".0", ""),
		        				
		        	};
		                
					orders.add(new LoadOrder.Product(
										Integer.parseInt(fields[0]), 
										Integer.parseInt(fields[2])
										));

		            }
		 }
		
		return new LoadOrder(orders);
	}
	
	default LoadOrder getloadOrder(String path, String orederCharger) throws Exception{
		final String defaultPath = "//ordercharger.xlsx";
		
		List<LoadOrder.Product> orders = new ArrayList<>();
		
		
		 try (FileInputStream file = new FileInputStream(path + defaultPath);
		            Workbook workbook = new XSSFWorkbook(file)) {

		            Sheet sheet = workbook.getSheetAt(0);
		            Iterator<Row> rowIterator = sheet.iterator();



		            while (rowIterator.hasNext()) {
		                Row row = rowIterator.next();
		                
		        		String[] fields = {
		        				String.valueOf(row.getCell(0).getNumericCellValue()).replace(".0", ""),
		        				String.valueOf(row.getCell(2).getNumericCellValue()).replace(".0", ""),
		        				
		        	};
		                
					orders.add(new LoadOrder.Product(
										Integer.parseInt(fields[0]), 
										Integer.parseInt(fields[1])
										));

		            }
		 }
		
		return new LoadOrder(orders, orederCharger);
	}
	
	default List<Chamber> chargeCameras(String path, int numberOfChambers) throws IOException{
		final String defaultPath = "/chambers.xlsx";
        
		
		List<Chamber> chambers = new ArrayList<>();
		

		for(int i = 0; i < numberOfChambers; i++) {
			chambers.add(new Chamber((i+1), 20, LoadProductsOfxlsx(path + defaultPath)));
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

	default List<Product> LoadProductsOfxlsx(String path) throws IOException{
		List<Product> listProducts = new ArrayList<>();
		
		 try (FileInputStream file = new FileInputStream(path);
	            Workbook workbook = new XSSFWorkbook(file)) {

	            Sheet sheet = workbook.getSheetAt(0);
	            Iterator<Row> rowIterator = sheet.iterator();



	            while (rowIterator.hasNext()) {
	                Row row = rowIterator.next();
	                
	        		String[] fields = {
	        				String.valueOf(row.getCell(0).getNumericCellValue()).replace(".0", ""),
	        				String.valueOf(row.getCell(1).getNumericCellValue()).replace(".0", ""),
	        				String.valueOf(row.getCell(2).getNumericCellValue()).replace(".0", ""),
	        				row.getCell(3).getStringCellValue(),
	        				row.getCell(4).getStringCellValue(),
	        				row.getCell(5).getStringCellValue(),
	        				row.getCell(6).getStringCellValue(),
	        				String.valueOf(row.getCell(7).getNumericCellValue()).replace(".0", ""),
	        				
	        		};
	                
					listProducts.add(new Product(Integer.parseInt(fields[0]), 
										Integer.parseInt(fields[1]), 
										Integer.parseInt(fields[2]), 
										Integer.parseInt(String.valueOf(fields[3].substring(3))), 
										Integer.parseInt(String.valueOf(fields[4].substring(1))), 
										Integer.parseInt(String.valueOf(fields[5].substring(1))), 
										fields[6], 
										Integer.parseInt(fields[7])));
					
	                
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
				
		
		
		return listProducts;
	}
	
}
