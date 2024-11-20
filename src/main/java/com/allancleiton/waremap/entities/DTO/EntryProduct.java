package com.allancleiton.waremap.entities.DTO;

import java.util.Objects;

public class EntryProduct{
	private Integer code; 
	private Boolean isFrozen;
	public EntryProduct(Integer code, Boolean isFrozen) {
		this.code = code;
		this.isFrozen = isFrozen;
	}
	
	public EntryProduct() {}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Boolean getIsFrozen() {
		return isFrozen;
	}

	public void setIsFrozen(Boolean isFrozen) {
		this.isFrozen = isFrozen;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, isFrozen);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntryProduct other = (EntryProduct) obj;
		return Objects.equals(code, other.code) && Objects.equals(isFrozen, other.isFrozen);
	}

	@Override
	public String toString() {
		return "EntryProduct [code=" + code + ", isFrozen=" + isFrozen + "]";
	}
	
	
	
}
