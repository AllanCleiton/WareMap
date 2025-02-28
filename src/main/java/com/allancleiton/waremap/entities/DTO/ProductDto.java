package com.allancleiton.waremap.entities.DTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class ProductDto implements Comparable<ProductDto>{
	Integer note; 
	int quantity;
	public Integer tam = null;
	List<ChamberDto> chambers = new ArrayList<>();
	
	
	//___________________________________________CONSTRUTORES________________________________________//
	public ProductDto(int note, int quantity) {
		this.note = note;
		this.quantity = quantity;
	}
	
	public ProductDto(int note, int quantity, int qtdeBoxes) {
		this.note = Integer.parseInt(String.valueOf(note).concat(String.valueOf(qtdeBoxes)));;
		this.quantity = quantity;
	}
	
	public ProductDto() {}
	//________________________________________________________________________________________________//
	
	
	public void setChamber(ChamberDto chamber) {
		chambers.add(chamber);
	}
	
	public void addChamber(List<ChamberDto> chambers) {	
		for(ChamberDto c : chambers) {
			this.chambers.add(c);
		}
	}
	
	public void setChambers(List<ChamberDto> chambers) {
		this.chambers = chambers;
	}
	
	public Integer getNote() {
		if(tam != null) {
			return Integer.parseInt(String.valueOf(note).replace(String.valueOf(tam), ""));
		}
		return note;
	}
	
	public Integer getQuantity() {
		return this.quantity;
	}
	
	public Integer getEspecialNote() {
		return note;
	}
	
	public void setNote(int note) {
		this.note = note;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public List<ChamberDto> getChambers() {
		return chambers;
	}
	
	public void somatorioEndMoreNew(List<ProductDto> finalListOfProducts) {
		int sum = 0, sobra = 0;
		boolean verified = false;
		Position moreNew = null;
		ProductDto product = null;
		
		if(this.tam != null) {
			product = new ProductDto(this.note, this.quantity);
			product.tam = this.tam;
			
			for(ProductDto p : finalListOfProducts) {
				if(p.getEspecialNote().equals(product.getEspecialNote())) {
					product.addChamber(p.getChambers());
				}
			}
		}else{
			product = new ProductDto(this.note, this.quantity);
			for(ProductDto p : finalListOfProducts) {
				if(p.getNote().equals(product.getNote())) {
					product.addChamber(p.getChambers());
				}
			}
		}
		
		
		boolean exit = false, executed = false;
		if(!(this.chambers.isEmpty())) {
			while(!exit) {
				for (ChamberDto chamberDto : product.getChambers()) {
					for (RoadDto roadDto : chamberDto.getRoads()) {
						for (Position pos : roadDto.getPositions()) {
							if(!(pos.visited)) {	
								sum += pos.getBoxes();
								if(!verified) {
									moreNew = pos;
									verified = true;
								}
								if(executed) {
									if(pos.getDays() >= moreNew.getDays() && moreNew.getBoxes() >= sobra) {
										moreNew = pos;
									}
								}else {
									if(pos.getDays() >= moreNew.getDays()) {
										moreNew = pos;
									}
								}
							}else {
								sum += pos.getBoxes();;
							}
						}
					}
				}
				sobra = sum-quantity;
				if(moreNew.getBoxes() >= sobra) {
					/*SE A QUANTIDADE DE CAIXAS DO PRODUTO MAIS NOVO ENCONTRADO FOR IGUAL AO VALOR DE SOBRA ENTAO ESSE TREXO
						ESSE TREXO DO CODIGO RETIRA O MORENEW DA LISTA DE PRODUTOS ENCONTRADOS. E MARCA O MORENEW COMO NULO.
					*/
					//--------------------------------------------------------------------------------------------------//
					if(moreNew.getBoxes() == sobra) {
						externo:
						for (ChamberDto chamberDto : product.getChambers()) {
							for (RoadDto roadDto : chamberDto.getRoads()) {
								for (Position pos : roadDto.getPositions()) {
									if(pos.equals(moreNew)) {
										roadDto.getPositions().remove(moreNew);
										break externo;
									}
								}
							}
						}
						
						moreNew = null;
					}
					
					//--------------------------------------------------------------------------------------------------//
					exit = true;
				}else {
					moreNew.visited = true;
					executed = true;
					verified = false;
					sum = 0;
				}
			}
		}
		
		if(moreNew != null) {
			moreNew.setToWithdraw(moreNew.getBoxes() - sobra);
			
			moreNew.setMoreNew(sobra);	
		}
		
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(chambers, note, quantity, tam);
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
		return Objects.equals(chambers, other.chambers) && Objects.equals(note, other.note)
				&& quantity == other.quantity && Objects.equals(tam, other.tam);
	}
	public String print(List<ProductDto> finalListOfProducts) {
		somatorioEndMoreNew(finalListOfProducts);
		StringBuilder sb = new StringBuilder();
		if(tam != null ) {
			if(String.valueOf(note).length() == 7) {
				//sb.append(Integer.parseInt(String.valueOf(note).replace(String.valueOf(tam), "")) +" " + String.valueOf(note).substring(5) + " " + quantity);
				sb.append(Integer.parseInt(String.valueOf(note).substring(0, String.valueOf(note).length() - 2)) +"[" + String.valueOf(note).substring(5) + "] " + quantity);
			}else if(String.valueOf(note).length() == 6) {
				sb.append(Integer.parseInt(String.valueOf(note).substring(0, String.valueOf(note).length() - 1)) +"[" + String.valueOf(note).substring(5) + "] " + quantity);

			}
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
			sb.append(" Produto não encontrado!\n");
		}
		if(chambers.size() > 0) {
			for(int i = 1; i < chambers.size(); i++) {
				sb.append("\t   " + chambers.get(i).toString());
			}
		}
		
		
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(tam != null ) {
			if(String.valueOf(note).length() == 7) {
				//sb.append(Integer.parseInt(String.valueOf(note).replace(String.valueOf(tam), "")) +" " + String.valueOf(note).substring(5) + " " + quantity);
				sb.append(Integer.parseInt(String.valueOf(note).substring(0, String.valueOf(note).length() - 2)) +"[" + String.valueOf(note).substring(5) + "] " + quantity);
			}else if(String.valueOf(note).length() == 6) {
				sb.append(Integer.parseInt(String.valueOf(note).substring(0, String.valueOf(note).length() - 1)) +"[" + String.valueOf(note).substring(5) + "] " + quantity);

			}
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
			sb.append(" Produto não encontrado!\n");
		}
		if(chambers.size() > 0) {
			for(int i = 1; i < chambers.size(); i++) {
				sb.append("\t   " + chambers.get(i).toString());
			}
		}
		
		
		return sb.toString();
	}

	@Override
	public int compareTo(ProductDto o) {
		return this.getNote().compareTo(o.getNote());
	}

	
}