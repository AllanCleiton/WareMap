package model.repositories;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import model.config.EntryProduct;
import model.config.ParameterProduct;
import model.entities.Category;
import model.entities.LoadOrder;
import model.entities.Order;
import model.entities.Product;
import model.exceptions.NoSuchElement;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface Repository {

    /**This method must return a String JSON, with the following attributes:
     * code=XXXX,
     * description=String,
     * boxes=XX,
     * Where x represents numbers, and description is never user.*/
    String LoadOrder() throws IOException;

    /**This method must return a String JSON, with the following attributes:
     * code=XXXX,
     * days=XX,
     * boxes=XX,
     * camera=CAMXX,
     * street=RXX,
     * height=AXX,
     * position=({A} or {B} or {C} ),
     * packages=xx. Where x represents numbers.*/
    String LoadProducts() throws IOException;

    default List<Product> LoadProductsOfxlsx(String path) throws IOException{
        List<Product> listProducts = new ArrayList<>();
        final String defaultPath = "/chambers.xlsx";
        final String pathFileDb = "/config/cacheproducts/cache.db";
        DB db = DBMaker.fileDB(path + pathFileDb).make();
        @SuppressWarnings("unchecked")
        Set<Product> cache = (Set<Product>) db
                .hashSet("cache", Serializer.JAVA)  // Nome da coleção + Serializer
                .createOrOpen();

        var parameter = new ParameterProduct(path);

        try (FileInputStream file = new FileInputStream(path + defaultPath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();



            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if( row.getCell(0) != null) {
                    if(row.getCell(0).getNumericCellValue() != 0) {
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

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (Product product : listProducts) {
            for (Category category : parameter.getCategories()) {
                for(EntryProduct entry: category.getEntries()) {
                    if(product.getNote().equals(entry.getCode())) {
                        product.isFrozen = entry.getIsFrozen();
                        product.validity = category.getValidity();
                    }
                }
            }

            //corrigir categoria nao existente aqui!!!
        }

        cache.addAll(listProducts);
        db.commit();
        db.close();
        return listProducts;
    }

    default List<Product> LoadProductsOfDb(String path) {
        final String pathFileDb = "/config/cacheproducts/cache.db";
        DB db = DBMaker.fileDB(path + pathFileDb).make();
        @SuppressWarnings("unchecked")
        Set<Product> cache = (Set<Product>) db.hashSet("cache", Serializer.JAVA).createOrOpen();
        List<Product> list = new ArrayList<>(cache);
        db.close();
        return list;
    }

    default void shutDownDb(String path) {
        final String pathFileDb = "/config/cacheproducts/cache.db";
        DB db = DBMaker.fileDB(path + pathFileDb).make();
        @SuppressWarnings("unchecked")
        Set<Product> cache = (Set<Product>) db.hashSet("cache", Serializer.JAVA).createOrOpen();
        cache.clear();
        db.commit();
        db.close();
    }

    default void saveChanges(List<Product> allProducts, String path){
        final String pathFileDb = "/config/cacheproducts/cache.db";
        DB db = DBMaker.fileDB(path + pathFileDb).make();
        @SuppressWarnings("unchecked")
        Set<Product> cache = (Set<Product>) db.hashSet("cache", Serializer.JAVA).createOrOpen();
        cache.clear();
        cache.addAll(allProducts);

        db.commit();
        db.close();

    }

    default LoadOrder getloadOrder(String path) throws IOException{
        final String defaultPath = "/ordercharger.xlsx";
        List<Order> orders = new ArrayList<>();


        try (FileInputStream file = new FileInputStream(path + defaultPath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();



            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if(row.getCell(0) != null) {
                    if(row.getCell(0).getNumericCellValue() != 0){
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
            }
        }

        return new LoadOrder(orders);
    }

    default LoadOrder getloadOrder(String path, String orederCharger) throws IOException, NoSuchElement {
        final String defaultPath = "//ordercharger.xlsx";

        List<Order> orders = new ArrayList<>();


        try (FileInputStream file = new FileInputStream(path + defaultPath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();



            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if(row.getCell(0) != null) {
                    if(row.getCell(0).getNumericCellValue() != 0){
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

            }
        }

        return new LoadOrder(orders, orederCharger);
    }

    default LoadOrder jsonToLoadOrder(String json) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<LoadOrder>() {});
    }

    default List<Product> jsonToList(String json) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<List<Product>>() {});
    }


}
