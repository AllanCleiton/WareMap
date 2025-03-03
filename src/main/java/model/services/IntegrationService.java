package model.services;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.entities.LoadOrder;
import model.entities.Product;
import model.repositories.Repository;


/**
 * @author allan
 * Esta classe implementa a imterface Repository, A classe Repository
 * espera que sejam carregados as informações de endereçamentos e ordem
 * de carga para que o aplicativo funcione corretamente.
 */
public class IntegrationService implements Repository {
    private String path;
    private Integer choice;

    public IntegrationService(String path, Integer choice) {
        this.path = path;
        this.choice = choice;
    }

    @Override
    public String LoadProducts() throws IOException{
        List<Product> products;
        if(choice.equals(0)) {
            products = LoadProductsOfxlsx(path);
        }else {
            products = LoadProductsOfDb(path);
        }
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(products);
    }

    @Override
    public String LoadOrder() throws IOException {
        LoadOrder order = getloadOrder(path);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(order);
    }

    public String getPath() {
        return this.path;
    }
}
