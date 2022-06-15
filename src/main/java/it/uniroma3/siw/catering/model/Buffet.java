package it.uniroma3.siw.catering.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Buffet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@Column(length = 2000)
	private String descrizione;
	
	@ManyToOne
	private Chef chef;
	
	/* Le politiche di fetch e di cascade sono diverse da quelle di default, poichè tra buffet e piatto c'è un'associazione particolarmente forte.
	 * Nelle specifiche, infatti, è scritto che "un buffet contiene uno o più piatti".
	 * Un buffet privo di piatti non sembrerebbe dunque un oggetto d'interesse.
	 * Inoltre, da ciò che è scritto nelle specifiche si può assumere che un piatto non possa esistere al di fuori di un buffet.
	 * Tra buffet e piatto, quindi, sembra quasi esserci una composizione.
	 * In realtà, non si tratta di una vera e propria composizione e, perciò, non ho inserito il cascadeType.DELETE.
	 * Infatti, quando viene eliminato un buffet dal database, non devono essere eliminati anche tutti i suoi piatti,
	 * poichè potrebbe far parte di più di un buffet. */
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Piatto> piatti;

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

	public Chef getChef() {
		return chef;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public List<Piatto> getPiatti() {
		return piatti;
	}

	public void setPiatti(List<Piatto> piatti) {
		this.piatti = piatti;
	}
	
	public void addPiatto(Piatto piatto) {
		List<Piatto> piatti = this.getPiatti();
		piatti.add(piatto);
	}
	
	public void removePiatto(Piatto piatto) {
		List<Piatto> piatti = this.getPiatti();
		piatti.remove(piatto);
	}
}