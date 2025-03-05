package model.services;

import model.entities.Product;
import java.util.List;

public record TupleListProducts(List<Product> floorProducts, List<Product> frozenProducts) {
    public List<Product> getFloorProducts(){
        return floorProducts;
    }
    public List<Product> getFrozenProducts(){
        return floorProducts;
    }
}