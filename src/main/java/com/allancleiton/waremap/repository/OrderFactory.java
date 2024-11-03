package com.allancleiton.waremap.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.allancleiton.waremap.entities.LoadOrder;
import com.allancleiton.waremap.entities.Order;


public interface OrderFactory {
	
	
	default LoadOrder getloadOrder(String path) throws IOException{
		final String defaultPath = "/ordercharger.xlsx";
		List<Order> orders = new ArrayList<>();


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
                
			orders.add(new Order(
								Integer.parseInt(fields[0]), 
								Integer.parseInt(fields[1])
								));

            }
 }

 		return new LoadOrder(orders);
	}
	
	default LoadOrder getloadOrder(String path, String orederCharger) throws IOException{
		final String defaultPath = "//ordercharger.xlsx";
		
		List<Order> orders = new ArrayList<>();
		
		
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
		                
					orders.add(new Order(
										Integer.parseInt(fields[0]), 
										Integer.parseInt(fields[1])
										));

		            }
		 }
		
		return new LoadOrder(orders, orederCharger);
	}
}
