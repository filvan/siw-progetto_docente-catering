package it.uniroma3.siw.catering.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.catering.model.Ingrediente;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Long> {

	public List<Ingrediente> findByNome(String nome);
	
	@Modifying
	@Query("update Ingrediente i set i.nome = :nome, i.origine = :origine, i.descrizione = :descrizione where i.id = :id")
	public void updateNomeAndOrigineAndDescrizioneById(@Param ("id") Long id, @Param("nome") String nome, @Param("origine") String origine, @Param("descrizione") String descrizione);
}
