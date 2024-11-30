package com.allancleiton.waremap.entities.DTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class ProductDto {
	int note; 
	int quantity;
	public Integer tam = null;
	List<ChamberDto> chambers = new ArrayList<>();
	
	public ProductDto(int note, int quantity) {
		this.note = note;
		this.quantity = quantity;
	}
	
	public ProductDto(int note, int quantity, int qtdeBoxes) {
		this.note = Integer.parseInt(String.valueOf(note).concat(String.valueOf(qtdeBoxes)));;
		this.quantity = quantity;
	}
	
	public ProductDto() {}
	
	public void setChamber(ChamberDto chamber) {
		chambers.add(chamber);
	}
	
	public void setChambers(List<ChamberDto> chambers) {
		this.chambers = chambers;
	}
	
	public int getNote() {
		if(tam != null) {
			return Integer.parseInt(String.valueOf(note).replace(String.valueOf(tam), ""));
		}
		return note;
	}
	
	public int getEspecialNote() {
		return note;
	}
	
	public void setNote(int note) {
		this.note = note;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public List<ChamberDto> getChambers() {
		return chambers;
	}
	
	private void somatorioEndMoreNew() {
		int sum = 0;
		boolean verified = false;
		Position moreNew = null;
		for (ChamberDto chamberDto : chambers) {
			for (RoadDto roadDto : chamberDto.getRoads()) {
				for (Position pos : roadDto.getPositions()) {
					sum += pos.getBoxes();
					if(!verified) {
						moreNew = pos;
						verified = true;
					}
					if(pos.getDays() > moreNew.getDays()) {
						moreNew = pos;
					}
					
				}
			}
		}
		
		if(moreNew != null) {
			moreNew.setMoreNew(sum-quantity);	
		}
		
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(note);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductDto other = (ProductDto) obj;
		return note == other.note;
	}

	@Override
	public String toString() {
		somatorioEndMoreNew();
		
		StringBuilder sb = new StringBuilder();
		if(tam != null ) {
			sb.append(Integer.parseInt(String.valueOf(note).replace(String.valueOf(tam), "")) +" " + String.valueOf(note).substring(5) + " " + quantity);
		}else {
			sb.append(note +" "+ quantity);
		}
		
		if(quantity < 10) {
			sb.append("    ");
		}else if(quantity < 100) {
			sb.append("   ");
		}else if(quantity > 100 && quantity < 1000) {
			sb.append("  ");
		}else {
			sb.append(" ");
		}
		
		if(!chambers.isEmpty()) {
			sb.append(chambers.get(0).toString());
		}else {
			sb.append(" Produto não encontrado!\n\n");
		}
		if(chambers.size() > 0) {
			for(int i = 1; i < chambers.size(); i++) {
				sb.append("\t   " + chambers.get(i).toString());
			}
		}
		
		
		return sb.toString();
	}
}