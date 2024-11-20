package com.allancleiton.waremap.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.allancleiton.waremap.entities.Category;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParameterProduct {
	private final String defaultPath = "/config/productParameter.json";
	private ObjectMapper objectMapper = new ObjectMapper();
	List<Category> categories = null;
	
	public ParameterProduct(String path) throws IOException{
        // Carregando a lista de categorias do arquivo JSON
        categories = objectMapper.readValue(
            new File(path+defaultPath),
            new TypeReference<List<Category>>() {}
        );
		
	}

	public List<Category> getCategories() throws IOException{
		return categories;
	}
	
	public void salveParameters(String path) throws StreamWriteException, DatabindException, IOException {
		objectMapper.writerWithDefaultPrettyPrinter().writeValue( new File( path+"/config/productParameter.json"), categories);
		System.out.println(" Arquivo JSON criado com sucesso!");

	}
    
	public void createdCategory(Category category) {
		this.categories.add(category);
	}
	
	public Category getCategory (Integer validity){
		for (Category category : categories) {
			if(category.getValidity() == validity) {
				return category;
			};
		}
		return null;
	}
	
	public Boolean removeCategory(Category category) {
		return categories.remove(category);
	}
}
