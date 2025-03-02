package com.allancleiton.waremap.config.parameters;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.allancleiton.waremap.config.ParameterProduct;
import com.allancleiton.waremap.config.parameters.enums.TypeSeparetion;
import com.allancleiton.waremap.entities.Category;
import com.allancleiton.waremap.entities.Product;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Frozen  extends GeneralParameter{
	final String name = "frozen";
	private TypeSeparetion type;

	public Frozen(ParameterProduct categories,TypeSeparetion type) {
		super(categories);
		this.type = type;
	}
	
	public Frozen() {
		super();
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
		Frozen other = (Frozen) obj;
		return Objects.equals(name, other.name);
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
	public void salveParameters(String path) throws StreamWriteException, DatabindException, IOException {
		// TODO Auto-generated method stub
		new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue( new File( path+"/config/geralParameters/frozen.json"), this);
		System.out.println(" Arquivo JSON criado com sucesso!");
		
	}
	
	
}
