package com.allancleitonppma.gmail.WareMap.entities;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Chamber {
	private Integer chamber;
	private Integer numOfRoads;
	private Map<Integer, Road> roads = new LinkedHashMap<>();
	
	public Chamber(Integer chamber, Integer numOfRoads, List<Product> listProducts){
		this.chamber = chamber;
		this.numOfRoads = numOfRoads;
		
		for(int i= 0; i < numOfRoads; i++) {
			roads.put(i+1, new Road(i+1, chamber, listProducts));
			if(getRoad(i+1).getPositions().isEmpty()) {
				roads.remove(i+1);
			}
		}
	}
	
	public Chamber(){}

	public Integer getChamber() {
		return chamber;
	}

	public int getNumOfRoads() {
		return numOfRoads;
	}
	
	public Road getRoad(Integer road) {
		return roads.get(road);
	}
	
	public Set<Integer> getKeyssRoads(){
		return roads.keySet();
	}
	
	public List<Road> getAllRoads(){
		List<Road> list = new ArrayList<>();
		for (int i=0; i < numOfRoads; i++) {
			list.add(roads.get(i+1));
		}
		return list;
	}
	
	public boolean isEmpty() {
		return roads.isEmpty();
	}
	
}
