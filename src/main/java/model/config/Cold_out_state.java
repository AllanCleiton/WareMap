package model.config;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Predicate;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.entities.Category;
import model.entities.Product;

public class Cold_out_state extends GeneralParameter implements Predicate<Product>{
    final String name = "cold_out_state";
    private TypeSeparation type;

    public Cold_out_state(ParameterProduct categories,TypeSeparation type) {
        super(categories);
        this.type = type;
    }

    public Cold_out_state() {
        super();
    }

    @Override
    public boolean test(Product product) {
        boolean test = false;

        try {
            //Traz a categorya de acordo com a categoria do produto a ser testado.
            Category category = categories.getCategory(product.validity);
            int days = product.getDays();


            switch (type.getType()) {
                case "inState":{
                    test = days >= category.getInTheState();
                    break;
                }
                case "outState":{
                    test = days >= category.getOutOfState();
                    break;
                }
                case "default":{
                    test = true;
                    break;
                }
            }


            if(test) {
                product.visited = true;
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
        Cold_out_state other = (Cold_out_state) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public void salveParameters(String path) throws IOException {
        // TODO Auto-generated method stub
        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue( new File( path+"/config/geralParameters/cold_out_state.json"), this);
        System.out.println(" Arquivo JSON criado com sucesso!");
    }


}
