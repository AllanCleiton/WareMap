package com.allancleiton.waremap.config.parameters;

import java.io.IOException;
import java.util.function.Predicate;

import com.allancleiton.waremap.entities.Product;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

public abstract class GeneralParameter implements Predicate<Product>{
	protected Integer divisor;
	protected Integer multiplicador;
	
	public GeneralParameter(int divisor, int multiplicador) {
		this.divisor = divisor;
		this.multiplicador = multiplicador;
	}
	
	public GeneralParameter() {}
	
	public Integer getDivisor() {
		return divisor;
	}



	public void setDivisor(Integer divisor) {
		this.divisor = divisor;
	}



	public Integer getMultiplicador() {
		return multiplicador;
	}



	public void setMultiplicador(Integer multiplicador) {
		this.multiplicador = multiplicador;
	}



	public abstract void salveParameters(String path) throws StreamWriteException, DatabindException, IOException;
}
