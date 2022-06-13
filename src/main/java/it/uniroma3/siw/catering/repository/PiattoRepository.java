package it.uniroma3.siw.catering.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.catering.model.Piatto;

public interface PiattoRepository extends CrudRepository<Piatto, Long> {
	
	public Set<Piatto> findByNome(String nome);

}
