package model.config;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Predicate;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.config.TypeSeparation;
import model.entities.Category;
import model.entities.Product;

public class Cold_in_state extends GeneralParameter implements Predicate<Product>{
    final String name = "cold_in_state";
    private TypeSeparation type;

    public Cold_in_state(ParameterProduct categories,TypeSeparation type) {
        super(categories);
        this.type = type;
    }

    public Cold_in_state() {
        super();
    }

    @Override
    public boolean test(Product product) {

        boolean test = false;
        try {
            if(product != null) {
                //Traz a categorya de acordo com a categoria do produto a ser testado.
                Category category = categories.getCategory(product.validity);



                if(type.getType().equals("inState")) {
                    test = product.getDays() >= category.getInTheState();
                }
                if(type.getType().equals("outState")) {
                    test = product.getDays() >= category.getOutOfState();
                }
                if(type.getType().equals("default")) {
                    test = true;
                }

                if(test) {
                    product.visited = true;
                }
                return test;
            }

        }catch(RuntimeException e) {
            System.out.printf(" Erro ao tentar carregar a categoria. Produto: %d \n", product.getNote());
        }

        return test;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cold_in_state other = (Cold_in_state) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public void salveParameters(String path) throws IOException {
        // TODO Auto-generated method stub
        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue( new File( path+"/config/geralParameters/cold_in_state.json"), this);
        System.out.println(" Arquivo JSON criado com sucesso!");
    }


}
