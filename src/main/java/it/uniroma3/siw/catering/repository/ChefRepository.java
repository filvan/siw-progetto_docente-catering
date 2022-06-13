package it.uniroma3.siw.catering.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.catering.model.Chef;

public interface ChefRepository extends CrudRepository<Chef, Long> {
	
	public Set<Chef> findByNomeAndCognome(String nome, String cognome);

}
