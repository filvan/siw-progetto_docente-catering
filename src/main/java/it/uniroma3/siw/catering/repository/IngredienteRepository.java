package it.uniroma3.siw.catering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.catering.model.Ingrediente;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Long> {

	public List<Ingrediente> findByNome(String nome);
	
}
