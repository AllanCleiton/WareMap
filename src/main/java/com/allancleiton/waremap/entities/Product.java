package com.allancleiton.waremap.entities;


import java.io.Serializable;
import java.util.Objects;

import com.allancleiton.waremap.Color;
import com.allancleiton.waremap.entities.enums.Deoth;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Product implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer note, days, boxes, chamber, road, height, packages;
	private Deoth deoth;
	public boolean visited;
	public boolean isFrozen;
	public Integer validity;
	public boolean activePackeg = false;
	
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

	public void setBoxes(Integer boxes) {
		this.boxes = boxes;
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
	
	@JsonIgnore
	public Character getCharDeoth() {
		return deoth.getValue(this.getDeoth());
	}
	
	public Integer getPackages() {
		return packages;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Color.ANSI_BLACK);
		sb.append("  " + note + (note > 10000 ? "  " : "   "));
		
		if(days > 100) {sb.append(days + "    "); }else if((days < 100) && (days >= 10)) {sb.append(days + "     "); }else{sb.append(days + "      "); }
		
		sb.append(boxes + (boxes < 10 ? "        " : "       "));
		
		sb.append("Cam" + chamber + (chamber < 10 ? "     " : "      "));
		
		sb.append("Rua" + road + (road < 10 ? "    " : "   "));
		
		sb.append(height + "      ");

		sb.append(deoth + "        ");
		
		sb.append(packages + (packages >= 10 ?"       " : "        " ));
		
		sb.append(visited?Color.ANSI_RED + "Sim    " + Color.ANSI_RESET: "NÂO    ");
		sb.append(Color.ANSI_RESET);
		return 	sb.toString();
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