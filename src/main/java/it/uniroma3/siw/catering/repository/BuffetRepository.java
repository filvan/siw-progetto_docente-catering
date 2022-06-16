package it.uniroma3.siw.catering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.catering.model.Buffet;

public interface BuffetRepository extends CrudRepository<Buffet, Long> {
	
	public List<Buffet> findByNome(String nome);
	
//	@Modifying
//	@Query("update buffet set nome = nome set descrizione=descrizione where id = id")
//	public void updateNomeAndDescrizioneById(String nome, String descrizione, Long id);

}
