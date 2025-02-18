package com.allancleiton.waremap.config.parameters.enums;

public enum TypeSeparetion {
	
	IN_STATE("inState"),
	OUT_STATE("outState"),
	DEFAULT("default");
	
	private String value;
	
	private TypeSeparetion(String value) {
		this.value = value;
	}
	
	public String getType() {
		return value;
	}

}
