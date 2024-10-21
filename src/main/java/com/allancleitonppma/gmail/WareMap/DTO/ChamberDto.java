package com.allancleitonppma.gmail.WareMap.DTO;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ChamberDto {
	int note;
	int chamber;
	Set<RoadDto> roads = new HashSet<>();
 	
	
	public ChamberDto(int chamber, int note) {
		this.chamber = chamber;
		this.note = note;
	}

	public ChamberDto() {}

	
	
	public int getChamber() {
		return chamber;
	}

	public void setChamber(int chamber) {
		this.chamber = chamber;
	}

	public Set<RoadDto> getRoads() {
		return roads;
	}

	public void setRoad(RoadDto road) {
		this.roads.add(road);
	}
	
	public void setRoads(Set<RoadDto> roads) {
		this.roads = roads;
	}
	
	
	
	public int getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(chamber, note, roads);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChamberDto other = (ChamberDto) obj;
		return chamber == other.chamber && note == other.note && Objects.equals(roads, other.roads);
	}

	@Override
	public String toString() {
		List<RoadDto> roadss = new ArrayList<>(roads);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Cam: " + chamber + " ");
		if(!(roads.isEmpty())) {
			sb.append(roadss.get(0).toString() + "\n");
		}else {
			sb.append("\n");
		}
		if(roadss.size() > 0) {
			for (int i=1; i < roadss.size(); i++) {
				sb.append("\t\t  " + roadss.get(i).toString() + "\n");
			}
		}
		return sb.toString();
	}
}
