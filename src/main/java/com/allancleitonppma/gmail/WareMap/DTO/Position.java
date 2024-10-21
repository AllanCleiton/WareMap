package com.allancleitonppma.gmail.WareMap.DTO;

import java.util.Objects;

public class Position{
	int chamber;
	int product; 
	int road;
	int heigth; 
	char deoth; 
	
	
	
	public Position(int heigth, char deoth, int road, int product, int chamber) {
		this.heigth = heigth;
		this.deoth = deoth;
		this.road = road;
		this.product = product;
		this.chamber = chamber;
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
	
	
	
	public int getProduct() {
		return product;
	}

	public void setProduct(int product) {
		this.product = product;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%d%c ", heigth,deoth) );
		return sb.toString();
	}
}
