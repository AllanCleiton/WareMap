package com.allancleiton.waremap.entities.enums;


public enum TypeSeparation {
	FORKLIFT(0),
	COLD(1),
	FLOOR(2);
	
	private int value;
	
	private TypeSeparation(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public String getValue(int position) {
		
		switch (position) {
		case 0: {
			return "FORKLIFT";
		}
		case 1: {
			return "COLD";
		}
		case 2: {
			return "FLOOR";
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + position);
		}
	}
	
	
}

