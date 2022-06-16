package it.uniroma3.siw.catering.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.repository.PiattoRepository;

@Service
public class PiattoService {

	@Autowired
	private PiattoRepository piattoRepository; 
	
	@Transactional
	public Piatto save(Piatto piatto) {
		return piattoRepository.save(piatto);
	}

	@Transactional
	public List<Piatto> findAll() {
		return (List<Piatto>) piattoRepository.findAll();
	}

	@Transactional
	public Piatto findById(Long id) {
		Optional<Piatto> optional = piattoRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else 
			return null;
	}

	@Transactional
	public boolean alreadyExists(Piatto piatto) {
		List<Piatto> piatti = this.piattoRepository.findByNome(piatto.getNome());
		if (piatti.size() > 0)
			return true;
		else 
			return false;
	}
	
	@Transactional
	public void deleteById(Long id) {
		piattoRepository.deleteById(id);
	}
	
	@Transactional
	public void delete(Piatto piatto) {
		piattoRepository.delete(piatto);
	}
	
	@Transactional
	public void update(Long id, String nome, String descrizione) {
		this.piattoRepository.updateNomeAndDescrizioneById(id, nome, descrizione);
	}
}