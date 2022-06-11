package it.uniroma3.siw.catering.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.repository.BuffetRepository;

@Service
public class BuffetService {

	@Autowired
	private BuffetRepository buffetRepository; 
	
	@Transactional
	public Buffet save(Buffet buffet) {
		return buffetRepository.save(buffet);
	}

	@Transactional
	public List<Buffet> findAll() {
		return (List<Buffet>) buffetRepository.findAll();
	}

	@Transactional
	public Buffet findById(Long id) {
		Optional<Buffet> optional = buffetRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else 
			return null;
	}

	@Transactional
	public boolean alreadyExists(Buffet buffet) {
		List<Buffet> buffets = this.buffetRepository.findByNome(buffet.getNome());
		if (buffets.size() > 0)
			return true;
		else 
			return false;
	}

	@Transactional
	public void deleteById(Long id) {
		buffetRepository.deleteById(id);
	}
	
	@Transactional
	public void delete(Buffet buffet) {
		buffetRepository.delete(buffet);
	}
}
