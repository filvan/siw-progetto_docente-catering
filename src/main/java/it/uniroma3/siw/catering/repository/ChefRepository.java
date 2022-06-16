package it.uniroma3.siw.catering.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.catering.model.Chef;

public interface ChefRepository extends CrudRepository<Chef, Long> {
	
	public List<Chef> findByNomeAndCognome(String nome, String cognome);
	
	@Modifying
	@Query("update Chef c set c.nome = :nome, c.cognome = :cognome, c.nazionalita = :nazionalita where c.id = :id")
	public void updateNomeAndCognomeAndNazionalitaById(@Param ("id") Long id, @Param("nome") String nome, @Param("cognome") String cognome, @Param("nazionalita") String nazionalita);

}
