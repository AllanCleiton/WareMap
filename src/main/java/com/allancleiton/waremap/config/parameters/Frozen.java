package com.allancleiton.waremap.config.parameters;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.allancleiton.waremap.entities.Product;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Frozen  extends GeneralParameter{
	final String name = "frozen";

	public Frozen(int divisor, int multiplicador) {
		super(divisor, multiplicador);
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
		int validity = product.validity;
		int days = product.getDays();
		boolean test = days <= ((validity / divisor) * multiplicador);
		if(test) {
			product.visited = true;
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
