package it.uniroma3.siw.catering.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.catering.model.Piatto;

public interface PiattoRepository extends CrudRepository<Piatto, Long> {
	
	public List<Piatto> findByNome(String nome);
	
	@Modifying
	@Query("update Piatto p set p.nome = :nome, p.descrizione = :descrizione where p.id = :id")
	public void updateNomeAndDescrizioneById(@Param ("id") Long id, @Param("nome") String nome, @Param("descrizione") String descrizione);
}
