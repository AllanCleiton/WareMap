package com.allancleitonppma.gmail.WareMap.entities;

import java.util.Objects;

import com.allancleitonppma.gmail.WareMap.enums.Deoth;



public class Product {
	private Integer note, days, boxes, chamber, road, height, packages;
	private Deoth deoth;
	
	
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
	
	public Character getCharDeoth() {
		
		return deoth.getValue(this.getDeoth());
	}
	
	public Integer getPackages() {
		return packages;
	}

	@Override
	public String toString() {
		return "codigo: " + note + ", cam: " + chamber + ", rua: " + road + ", andar: " + height + ", posicão: "+ deoth;
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

	/*@Override
	public int hashCode() {
		return Objects.hash(deoth, height, road);
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
		return deoth == other.deoth && Objects.equals(height, other.height) && Objects.equals(road, other.road);
	}*/
	
	
	
}
