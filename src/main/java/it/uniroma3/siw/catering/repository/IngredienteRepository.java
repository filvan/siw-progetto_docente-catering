package it.uniroma3.siw.catering.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.catering.model.Ingrediente;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Long> {

	public Set<Ingrediente> findByNome(String nome);
	
}
