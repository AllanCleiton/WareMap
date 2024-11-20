package com.allancleiton.waremap.entities;


import java.util.Objects;

import com.allancleiton.waremap.entities.enums.Deoth;

public class Product {
	private Integer note, days, boxes, chamber, road, height, packages;
	private Deoth deoth;
	public boolean visited;
	public boolean isFrozen;
	public Integer validity;
	
	
	public Product(Integer note, Integer days, Integer boxes, Integer chamber, Integer road, Integer height, String deoth, Integer packages) {
		this.note = note;
		this.days = days;
		this.boxes = boxes;
		this.chamber = chamber;
		this.road = road;
		this.height = height;
		this.deoth = Deoth.valueOf(deoth);
		this.packages = packages;
	}

	public Product() {}

	public Integer getNote() {
		return note;
	}

	public Integer getDays() {
		return days;
	}

	public Integer getBoxes() {
		return boxes;
	}

	public Integer getChamber() {
		return chamber;
	}

	public Integer getRoad() {
		return road;
	}

	public Integer getHeight() {
		return height;
	}

	public Integer getDeoth() {
		return deoth.getValue();
	}
	
	protected Character getCharDeoth() {
		return deoth.getValue(this.getDeoth());
	}
	
	public Integer getPackages() {
		return packages;
	}
	
	public Integer getValiddity() {
		return this.validity;
	}

	@Override
	public String toString() {
		return "codigo: " + note + " dias: " + days + " caixas: " + boxes + ", cam: " + chamber + ", rua: " + road + ", andar: " + height + ", posicão: "+ deoth + " pacotes: " + packages;
	}

	@Override
	public int hashCode() {
		return Objects.hash(chamber, deoth, height, note, road);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return Objects.equals(chamber, other.chamber) && deoth == other.deoth && Objects.equals(height, other.height)
				&& Objects.equals(note, other.note) && Objects.equals(road, other.road);
	}
}
