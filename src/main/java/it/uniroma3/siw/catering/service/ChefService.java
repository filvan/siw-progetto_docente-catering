package it.uniroma3.siw.catering.service;

import java.util.Set;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.repository.ChefRepository;

@Service
public class ChefService {

	@Autowired
	private ChefRepository chefRepository; 
	
	@Transactional
	public Chef save(Chef chef) {
		return chefRepository.save(chef);
	}

	@Transactional
	public Set<Chef> findAll() {
		return (Set<Chef>) chefRepository.findAll();
	}

	@Transactional
	public Chef findById(Long id) {
		Optional<Chef> optional = chefRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else 
			return null;
	}

	@Transactional
	public boolean alreadyExists(Chef chef) {
		Set<Chef> chefs = this.chefRepository.findByNomeAndCognome(chef.getNome(), chef.getCognome());
		if (chefs.size() > 0)
			return true;
		else 
			return false;
	}
	
	@Transactional
	public void deleteById(Long id) {
		chefRepository.deleteById(id);
	}
	
	@Transactional
	public void delete(Chef chef) {
		chefRepository.delete(chef);
	}
}
