package it.uniroma3.siw.catering.controller;

import java.util.List;

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
import it.uniroma3.siw.catering.service.BuffetService;

@Controller
public class BuffetController {

	@Autowired
	private BuffetService buffetService;
	
	@Autowired
	private BuffetValidator buffetValidator;
	
	/*
	 * Convenzione: GET per le operazioni di lettura, POST per le operazioni di scrittura.
	 * Il path è associato alle classi del dominio
	 */
	
	@GetMapping("/admin/buffet")
	public String getBuffet(Model model) {
		model.addAttribute("buffet", new Buffet()); // necessario perché la pagina buffetForm.html si aspetta sempre 
													// di avere un oggetto Buffet, anche vuoto, a disposizione
		return "buffetForm.html";
	}
	
	@PostMapping("/admin/buffet")
    public String addBuffet(@ModelAttribute("buffet") Buffet buffet, 
    									Model model, BindingResult bindingResult) {
    	this.buffetValidator.validate(buffet, bindingResult);
        if (!bindingResult.hasErrors()) {
        	this.buffetService.save(buffet);
            model.addAttribute("buffets", this.buffetService.findAll());
            return "buffets.html";
        }
        return "buffetForm.html";
    }
	
	// richiede tutti i buffet (non viene specificato un id particolare)
	@GetMapping("/buffet")
	public String getBuffets(Model model) {
		List<Buffet> buffets = this.buffetService.findAll();
		model.addAttribute("buffets", buffets);
		return "buffets.html";
	}
	
	// richiede un buffet, dato il suo id
	@GetMapping("/buffet/{id}")
	public String getBuffetById(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		model.addAttribute("buffet", buffet);
		return "buffet.html";
	}
	
//	@GetMapping("/toDeleteBuffet/{id}")
//	public String deleteBuffetById(@PathVariable("id") Long id, Model model) {
//		Buffet buffet =  this.buffetService.findById(id);
//		this.buffetService.delete(buffet);
//		List<Buffet> buffets = this.buffetService.findAll();
//		model.addAttribute("buffets", buffets);
//		return "buffets.html";
//	}
	
	@GetMapping("/toDeleteBuffet/{id}")
	public String deleteBuffetById(@PathVariable("id") Long id, Model model) {
		Buffet buffet =  this.buffetService.findById(id);
		model.addAttribute("buffet", buffet);
		return "confermaCancellazioneBuffet.html";
	}
	
	@GetMapping("/confirmDeleteBuffet/{id}")
	public String confirmDeleteBuffetById(@PathVariable("id") Long id, Model model) {
		this.buffetService.deleteById(id);
		List<Buffet> buffets = this.buffetService.findAll();
		model.addAttribute("buffets", buffets);
		return "buffets.html";
	}
}
