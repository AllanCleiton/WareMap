package com.allancleiton.waremap.config.parameters;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Predicate;

import com.allancleiton.waremap.entities.Order;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Floor_separation implements Predicate<Order>{
	final String name = "floor_separation";
	protected Integer parameter;
	
	public Floor_separation(Integer parameter) {
		this.parameter = parameter;
	}
	
	public Floor_separation() {}
	
	@Override
	public boolean test(Order order) {
		return order.qtdeBoxes() <= parameter;
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
		Floor_separation other = (Floor_separation) obj;
		return Objects.equals(name, other.name);
	}

	public void salveParameters(String path) throws StreamWriteException, DatabindException, IOException {
		// TODO Auto-generated method stub
		new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue( new File( path+"/config/geralParameters/floor_separation.json"), this);
		System.out.println(" Arquivo JSON criado com sucesso!");
	}

	public Integer getParameter() {
		return parameter;
	}

	public void setParameter(Integer parameter) {
		this.parameter = parameter;
	}
	
	
}

