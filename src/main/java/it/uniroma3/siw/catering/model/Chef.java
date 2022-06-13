package it.uniroma3.siw.catering.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class Chef {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String cognome;
	
	private String nazionalita;
	
	@OneToMany(mappedBy = "chef")
	private Set<Buffet> buffetProposti;

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

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNazionalita() {
		return nazionalita;
	}

	public void setNazionalita(String nazionalita) {
		this.nazionalita = nazionalita;
	}

	public Set<Buffet> getBuffetProposti() {
		return buffetProposti;
	}

	public void setBuffetProposti(Set<Buffet> buffetProposti) {
		this.buffetProposti = buffetProposti;
	}
	
	public void addBuffet(Buffet buffet) {
		Set<Buffet> buffetProposti = this.getBuffetProposti();
		buffetProposti.add(buffet);
	}
	
	public void removeBuffet(Buffet buffet) {
		Set<Buffet> buffetProposti = this.getBuffetProposti();
		buffetProposti.remove(buffet);
	}
}