package com.allancleiton.waremap.config.parameters.enums;

public enum TypeSeparetion {
	
	IN_STATE("inState"),
	OUT_STATE("outState"),
	DEFAULT("default");
	
	private String type;
	
	private TypeSeparetion(String value) {
		this.type = value;
	}
	
	public String getType() {
		return this.type;
	}

}
