package com.allancleiton.waremap.exceptions;

public class NoSuchElement extends Exception{
	/**
	 * exception thrown in case of invalid constructor argument 
	 */
	private static final long serialVersionUID = 1L;
	public NoSuchElement() {
		super("exception thrown in case of invalid constructor argument");	
	}	
}