package it.uniroma3.siw.catering.service;

import java.util.Set;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.repository.IngredienteRepository;

@Service
public class IngredienteService {
	
	@Autowired
	private IngredienteRepository ingredienteRepository; 
	
	@Transactional
	public Ingrediente save(Ingrediente ingrediente) {
		return ingredienteRepository.save(ingrediente);
	}

	@Transactional
	public Set<Ingrediente> findAll() {
		return (Set<Ingrediente>) ingredienteRepository.findAll();
	}

	@Transactional
	public Ingrediente findById(Long id) {
		Optional<Ingrediente> optional = ingredienteRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else 
			return null;
	}

	@Transactional
	public boolean alreadyExists(Ingrediente ingrediente) {
		Set<Ingrediente> ingredienti = this.ingredienteRepository.findByNome(ingrediente.getNome());
		if (ingredienti.size() > 0)
			return true;
		else 
			return false;
	}
	
	@Transactional
	public void deleteById(Long id) {
		ingredienteRepository.deleteById(id);
	}
	
	@Transactional
	public void delete(Ingrediente ingrediente) {
		ingredienteRepository.delete(ingrediente);
	}
}