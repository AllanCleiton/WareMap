package com.allancleiton.waremap.entities;

public record Order(Integer note, Integer qtdeBoxes) {
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("Produto: " + note + " Caixas: " + qtdeBoxes);
		
		return sb.toString();
	}
}