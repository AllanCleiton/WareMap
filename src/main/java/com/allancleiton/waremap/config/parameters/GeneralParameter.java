package com.allancleiton.waremap.config.parameters;

import java.io.IOException;
import java.util.function.Predicate;

import com.allancleiton.waremap.config.ParameterProduct;
import com.allancleiton.waremap.entities.Category;
import com.allancleiton.waremap.entities.Product;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

public abstract class GeneralParameter implements Predicate<Product>{
	protected ParameterProduct categories;
	
	public GeneralParameter(ParameterProduct categories) {
		this.categories = categories;
	}
	
	public GeneralParameter() {}
	
	public Category getCategory(Integer validity) {
		return categories.getCategory(validity);
	}


	public abstract void salveParameters(String path) throws StreamWriteException, DatabindException, IOException;
}
