package com.allancleiton.waremap.entities;

import java.util.List;
import java.util.Objects;

import com.allancleiton.waremap.entities.DTO.EntryProduct;

public class Category {
	private Integer validity;
	private List<EntryProduct> entries = null;
	
	public Category(Integer validity, List<EntryProduct> entries) {
		this.validity = validity;
		this.entries = entries;
	}
	
	public Category() {}
	
	
	public Integer getValidity() {
		return this.validity;
	}

	public List<EntryProduct> getEntries() {
		return entries;
	}

	public void setEntries(List<EntryProduct> entries) {
		this.entries = entries;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}

	@Override
	public String toString() {
		return "Category [validity=" + validity + ", entries=" + entries + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(validity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(validity, other.validity);
	}
	
	
	
	
}
