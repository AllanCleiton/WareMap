package model.config;

import model.entities.Category;
import model.entities.Product;

import java.io.IOException;
import java.util.function.Predicate;

public abstract class GeneralParameter implements Predicate<Product>{
    protected ParameterProduct categories;

    public GeneralParameter(ParameterProduct categories) {
        this.categories = categories;
    }

    public GeneralParameter() {}

    public Category getCategory(Integer validity) {
        return categories.getCategory(validity);
    }


    public abstract void salveParameters(String path) throws IOException;
}
