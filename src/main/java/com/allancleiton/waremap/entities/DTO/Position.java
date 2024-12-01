package com.allancleiton.waremap.entities.DTO;

import java.util.Objects;

import com.allancleiton.waremap.entities.Product;
import com.allancleiton.waremap.entities.enums.Deoth;



public class Position implements Comparable<Position>{
	private Product product;
	private int chamber;
	int note; 
	int road;
	int heigth; 
	char deoth; 
	int days;
	int boxes;
	boolean moreNew = false;
	int sobra;	
	
	public Position(int heigth, char deoth, int road, int note, int chamber, int days, int boxes) {
		this.heigth = heigth;
		this.deoth = deoth;
		this.road = road;
		this.note = note;
		this.chamber = chamber;
		this.days = days;
		this.boxes = boxes;
	}
	
	public Position(Product product) {
		this.product = product;
		this.heigth = product.getHeight();
		this.deoth = product.getCharDeoth();
		this.road = product.getRoad();
		this.note = product.getNote();
		this.chamber = product.getChamber();
		this.days = product.getDays();
		this.boxes = product.getBoxes();
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(chamber, deoth, heigth, product, road);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return chamber == other.chamber && deoth == other.deoth && heigth == other.heigth && product == other.product
				&& road == other.road;
	}
	
	public int getChamber() {
		return chamber;
	}



	public void setChamber(int chamber) {
		this.chamber = chamber;
	}



	public int getHeigth() {
		return heigth;
	}

	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}

	public char getDeoth() {
		return deoth;
	}

	public void setDeoth(char deoth) {
		this.deoth = deoth;
	}

	public int getRoad() {
		return road;
	}

	public void setRoad(int road) {
		this.road = road;
	}
	
	public int getDays() {
		return this.days;
	}
	
	public int getProduct() {
		return note;
	}

	public void setProduct(int note) {
		this.note = note;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public void setMoreNew(int sobra) {
		this.moreNew = true;
		this.sobra = sobra;
		if(product.getBoxes() > sobra && sobra > 0) {
			this.product.setBoxes(sobra);
			this.product.visited = false;
		}
	}
	
	public int getBoxes() {
		return this.boxes;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%d%c ", product.getHeight(),product.getCharDeoth()) );
		return sb.toString();
	}


	@Override
	public int compareTo(Position other) {
		Deoth d1 = Deoth.valueOf(String.valueOf(this.deoth));
		Deoth d2 = Deoth.valueOf(String.valueOf(other.deoth));
		Integer D1 = d1.getValue();
		Integer D2 = d2.getValue();
		
		return D1.compareTo(D2);
	}
}
