package it.uniroma3.siw.catering.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class Piatto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String nome;
	
	@Column(length = 2000)
	private String descrizione;
	
	@ManyToMany(mappedBy = "piatti")
	private List<Buffet> buffets;
	
	@ManyToMany
	private List<Ingrediente> ingredienti;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<Buffet> getBuffets() {
		return buffets;
	}

	public void setBuffets(List<Buffet> buffet) {
		this.buffets = buffet;
	}
	
	public void addBuffet(Buffet buffet) {
		List<Buffet> buffets = this.getBuffets();
		buffets.add(buffet);
	}
	
	public void removeBuffet(Buffet buffet) {
		List<Buffet> buffets = this.getBuffets();
		buffets.remove(buffet);
	}

	public List<Ingrediente> getIngredienti() {
		return ingredienti;
	}

	public void setIngredienti(List<Ingrediente> elencoIngredienti) {
		this.ingredienti = elencoIngredienti;
	}
	
	public void addIngrediente(Ingrediente ingrediente) {
		List<Ingrediente> piatti = this.getIngredienti();
		piatti.add(ingrediente);
	}
	
	public void removeIngrediente(Ingrediente ingrediente) {
		List<Ingrediente> piatti = this.getIngredienti();
		piatti.remove(ingrediente);
	}
}