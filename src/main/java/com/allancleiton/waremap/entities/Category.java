package com.allancleiton.waremap.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.allancleiton.waremap.entities.DTO.EntryProduct;

public class Category {
	private Integer validity;
	private Set<EntryProduct> entries = new HashSet<>();
	
	public Category(Integer validity, Set<EntryProduct> entries) {
		this.validity = validity;
		this.entries = entries;
	}
	
	public Category() {}
	
	
	public Integer getValidity() {
		return this.validity;
	}

	public Set<EntryProduct> getEntries() {
		return entries;
	}

	public void setEntries(Set<EntryProduct> entries) {
		this.entries = entries;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}
	
	public boolean setEntryProduct(EntryProduct p) {
		return entries.add(p);
	}

	public EntryProduct getEntryProduct(int code) {
		for (EntryProduct entryProduct : entries) {
			if(entryProduct.getCode() == code) {
				return entryProduct;
			}
		}
		return null;
	}
	
	public boolean removeEntryProduct(EntryProduct p) {
		return entries.remove(p);
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
