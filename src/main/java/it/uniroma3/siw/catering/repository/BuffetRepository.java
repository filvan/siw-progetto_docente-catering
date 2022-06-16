package it.uniroma3.siw.catering.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.catering.model.Buffet;

public interface BuffetRepository extends CrudRepository<Buffet, Long> {
	
	public List<Buffet> findByNome(String nome);
	
	@Modifying
	@Query("update Buffet b set b.nome = :nome, b.descrizione = :descrizione where b.id = :id")
	public void updateNomeAndDescrizioneById(@Param ("id") Long id, @Param("nome") String nome, @Param("descrizione") String descrizione);

}
