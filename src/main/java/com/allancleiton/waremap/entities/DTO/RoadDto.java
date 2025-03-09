package com.allancleiton.waremap.entities.DTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RoadDto {
	int note;
	int chamber;
	int road;
	List<Position> positions = new ArrayList<>();
	
	public RoadDto(int road, int chamber, int note) {
		this.road = road;
		this.chamber = chamber;
		this.note = note;
	}
	

	public Integer getRoad() {
		return road;
	}

	public void setRoad(int road) {
		this.road = road;
	}



	public List<Position> getPositions() {
		return positions;
	}



	public void setPosition(Position position) {
		this.positions.add(position);
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	public int getChamber() {
		return chamber;
	}

	public void setChamber(int chamber) {
		this.chamber = chamber;
	}

	
	public int getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(chamber, note, road);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoadDto other = (RoadDto) obj;
		return chamber == other.chamber && note == other.note && road == other.road;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if(road > 9) {
			sb.append("Rua " + road + " = ");
		}else {
			sb.append("Rua " + road + "  = ");
		}
		
		//tenta ordenar as posicoes.
		try {
			positions.sort(Position::compareTo);
		}catch(Exception e){
			System.out.println("Nao foi possivel ordenar as posições: Erro: "+ e.getMessage());
		}
		
		int count = 0;
		String moreNew="";
		
		for (Position position : positions) {
			
			if(count > 7) {
				if(position.moreNew && position.sobra != 0) {
					moreNew = "\n\t\t\t     " + position.toString() + "->" + position.getToWithdraw() + "cx";
					continue;
					//sb.append("\n\t\t\t     " + position.toString() + "-> " + position.getToWithdraw() + "cx ");
				}else {	
					sb.append("\n\t\t\t     " + position.toString()+" ");
				}
				count = 0;
			}else {
				if(position.moreNew && position.sobra != 0) {
					moreNew = position.toString() + "->" + position.getToWithdraw() + "cx";
					continue;
					//sb.append(position.toString() + "-> " + position.getToWithdraw() + "cx ");
				}else {
					sb.append(position.toString()+" ");
				}

			}
	
			count++;
		}
		
		sb.append(moreNew);
	
		return sb.toString();
	}
}

