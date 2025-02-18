package com.allancleiton.waremap.config.parameters;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Predicate;

import com.allancleiton.waremap.config.ParameterProduct;
import com.allancleiton.waremap.config.parameters.enums.TypeSeparetion;
import com.allancleiton.waremap.entities.Category;
import com.allancleiton.waremap.entities.Product;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Cold_in_state extends GeneralParameter implements Predicate<Product>{
	final String name = "cold_in_state";
	private TypeSeparetion type;
	
	public Cold_in_state(ParameterProduct categories,TypeSeparetion type) {
		super(categories);
		this.type = type;
	}
	
	public Cold_in_state() {
		super();
	}
	
	@Override
	public boolean test(Product product) {
		if(product != null) {
			//Traz a categorya de acordo com a categoria do produto a ser testado.
			Category category = categories.getCategory(product.validity);
			int days = product.getDays();
			boolean test = false;
			
			switch (type.getType()) {
				case "InState":{
					test = days <= category.getInTheState();
					break;
				}
				case "outState":{
					test = days <= category.getOutOfState();
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
			return test;
		}
		else {
			return false;
		}
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
	public void salveParameters(String path) throws StreamWriteException, DatabindException, IOException {
		// TODO Auto-generated method stub
		new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue( new File( path+"/config/geralParameters/cold_in_state.json"), this);
		System.out.println(" Arquivo JSON criado com sucesso!");
	}
	
	
}