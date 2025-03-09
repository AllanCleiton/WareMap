package com.allancleiton.waremap.entities.DTO;

import java.util.Objects;

import com.allancleiton.waremap.entities.Product;



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
	public boolean visited = false;
	int sobra;	
	private int toWithdraw;
	
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
	
	public Product getFisicProduct() {
		return this.product;
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
	
	public int getToWithdraw() {
		return toWithdraw;
	}
	
	public void setToWithdraw(int value) {
		this.toWithdraw = value;
	}
	public void setMoreNew(int sobra) {		
		boolean test = false;
		this.moreNew = true;
		this.sobra = sobra;
		
		if(product.getBoxes() > sobra && sobra > 0) {
			this.product.setBoxes(sobra);
			this.product.visited = false;
			test = true;
		}
		if(this.toWithdraw > product.getBoxes() && test == false) {
			setToWithdraw((this.toWithdraw - product.getBoxes()) * (0-1));
		}
	}
	
	public int getBoxes() {
		return this.boxes;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%d%c", product.getHeight(),product.getCharDeoth()) );
		return sb.toString();
	}

	
	private Byte comparable() {
		String position = String.valueOf(product.getHeight()).concat(String.valueOf(deoth));
		
		switch(position) {
			case "1A":{
				return 1;
			}
			case "2A":{
				return 2;
			}
			case "3A":{
				return 3;
			}
			case "4A":{
				return 4;
			}
			case "5A":{
				return 5;
			}
			case "6A":{
				return 6;
			}
			case "7A":{
				return 7;
			}
			
			case "1B":{
				return 8;
			}
			case "2B":{
				return 9;
			}
			case "3B":{
				return 10;
			}
			case "4B":{
				return 11;
			}
			case "5B":{
				return 12;
			}
			case "6B":{
				return 13;
			}
			case "7B":{
				return 14;
			}
			
			case "1C":{
				return 15;
			}
			case "2C":{
				return 16;
			}
			case "3C":{
				return 17;
			}
			case "4C":{
				return 18;
			}
			case "5C":{
				return 19;
			}
			case "6C":{
				return 20;
			}
			case "7C":{
				return 21;
			}
			default:{
				return null;
			}
				
		}

	}

	@Override
	public int compareTo(Position other) {
		return this.comparable().compareTo(other.comparable());
	}
}
