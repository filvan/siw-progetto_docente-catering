package it.uniroma3.siw.catering.controller;

import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.catering.controller.validator.BuffetValidator;
import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.service.BuffetService;
import it.uniroma3.siw.catering.service.PiattoService;

@Controller
public class BuffetController {

	@Autowired
	private BuffetService buffetService;

	@Autowired
	private BuffetValidator buffetValidator;

	@Autowired
	private PiattoService piattoService;

	/*
	 * Convenzione: GET per le operazioni di lettura, POST per le operazioni di scrittura.
	 * Il path è associato alle classi del dominio
	 */

	@GetMapping("/admin/buffet")
	public String getBuffet(Model model) {
		model.addAttribute("buffet", new Buffet()); // necessario perché la pagina buffetForm.html si aspetta sempre 
													// di avere un oggetto Buffet, anche vuoto, a disposizione
		return "admin/buffetForm.html";
	}
	
//	@GetMapping("/admin/modifyBuffet/{id}")
//	public String modifyBuffet(@PathVariable("id") Long id, Model model) {
//		Buffet buffet =  this.buffetService.findById(id);
//		model.addAttribute("buffet", buffet);
//		return "admin/buffetForm2.html";
//	}

	@PostMapping("/admin/buffet")
	public String addBuffet(@Valid @ModelAttribute("buffet") Buffet buffet,
			Model model, BindingResult bindingResult) {
		this.buffetValidator.validate(buffet, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.buffetService.save(buffet);
			model.addAttribute("buffets", this.buffetService.findAll());
			return "admin/buffets.html";
		}
		return "admin/buffetForm.html";
	}
	
	@GetMapping("/admin/addPiattoToBuffet/{buffetId}")
	public String getPiatto(@PathVariable("buffetId") Long id,	Model model) {
		Buffet buffet = this.buffetService.findById(id);
		model.addAttribute("buffet", buffet);
		
		List<Piatto> piattiNonGiaInseriti = this.piattoService.findAll();

		for (Iterator<Piatto> iterator = piattiNonGiaInseriti.iterator(); iterator.hasNext();) {
			Piatto piatto = (Piatto) iterator.next();
			if (piatto.getBuffets().contains(buffet))
				iterator.remove();
		}
		
		model.addAttribute("piatti", piattiNonGiaInseriti);
		model.addAttribute("piatto", new Piatto()); // necessario perché la pagina aggiungiPiattoAlBuffet.html si aspetta sempre 
		// di avere un oggetto Piatto, anche vuoto, a disposizione
		return "admin/aggiungiPiattoAlBuffet.html";
	}
	
	@PostMapping("/admin/addPiattoToBuffet/{buffetId}")
	public String addPiattoToBuffet(@PathVariable("buffetId") Long id, @Valid @ModelAttribute("piatto") Piatto piatto,
			Model model, BindingResult bindingResult) {
		Buffet buffet = this.buffetService.findById(id);
		piatto.addBuffet(buffet);
		buffet.addPiatto(piatto);
		this.piattoService.save(piatto);
		this.buffetService.save(buffet);
		return this.getPiatto(id, model);
	}
	
	@GetMapping("/admin/toRemovePiatto/{piattoId}/From/{buffetId}")
	public String removePiattoById(@PathVariable("piattoId") Long piattoId, @PathVariable("buffetId") Long buffetId, Model model) {
		Piatto piatto =  this.piattoService.findById(piattoId);
		Buffet buffet =  this.buffetService.findById(buffetId);
		piatto.removeBuffet(buffet);
		buffet.removePiatto(piatto);
		this.piattoService.save(piatto);
		this.buffetService.save(buffet);
		return this.getAdminBuffetById(buffet.getId(), model);
	}

	// richiede tutti i buffet (non viene specificato un id particolare)
	@GetMapping("/admin/buffets")
	public String getAdminBuffets(Model model) {
		List<Buffet> buffets = this.buffetService.findAll();
		model.addAttribute("buffets", buffets);
		return "admin/buffets.html";
	}

	/* L'utente è interessato solo ai buffet che contengono almeno un piatto.
	 * I buffet vuoti possono comparire nell'ambiente dell'amministratore ma,
	 * finchè non vengono completati con la lista dei piatti, è come se fossero in uno stato inconsistente e,
	 *  perciò, non devono comparire nell'ambiente dell'utente */
	
	
	@GetMapping("/user/buffets")
	public String getUserBuffets(Model model) {
		List<Buffet> buffets = this.buffetService.findAll();
		
		for (Iterator<Buffet> iterator = buffets.iterator(); iterator.hasNext();) {
			Buffet buffet = (Buffet) iterator.next();
			if (buffet.getPiatti().size() == 0)
				iterator.remove();
		}
		
		model.addAttribute("buffets", buffets);
		return "user/buffets.html";
	}

	// richiede un buffet, dato il suo id
	@GetMapping("/admin/buffet/{id}")
	public String getAdminBuffetById(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		model.addAttribute("buffet", buffet);
		List<Piatto> piatti = buffet.getPiatti();
		model.addAttribute("piatti", piatti);
		return "admin/buffet.html";
	}

	// richiede un buffet, dato il suo id
	@GetMapping("/user/buffet/{id}")
	public String getUserBuffetById(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		model.addAttribute("buffet", buffet);
		List<Piatto> piatti = buffet.getPiatti();
		model.addAttribute("piatti", piatti);
		return "user/buffet.html";
	}

	//	@GetMapping("/admin/toDeleteBuffet/{id}")
	//	public String deleteBuffetById(@PathVariable("id") Long id, Model model) {
	//		Buffet buffet =  this.buffetService.findById(id);
	//		this.buffetService.delete(buffet);
	//		List<Buffet> buffets = this.buffetService.findAll();
	//		model.addAttribute("buffets", buffets);
	//		return "admin/buffets.html";
	//	}

	@GetMapping("/admin/toDeleteBuffet/{id}")
	public String deleteBuffetById(@PathVariable("id") Long id, Model model) {
		Buffet buffet =  this.buffetService.findById(id);
		model.addAttribute("buffet", buffet);
		return "admin/confermaCancellazioneBuffet.html";
	}

	@GetMapping("/admin/confirmDeleteBuffet/{id}")
	public String confirmDeleteBuffetById(@PathVariable("id") Long id, Model model) {
		this.buffetService.deleteById(id);
		List<Buffet> buffets = this.buffetService.findAll();
		model.addAttribute("buffets", buffets);
		return "admin/buffets.html";
	}
}
