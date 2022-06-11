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

import it.uniroma3.siw.catering.controller.validator.ChefValidator;
import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.service.ChefService;

@Controller
public class ChefController {

	@Autowired
	private ChefService chefService;
	
	@Autowired
	private ChefValidator chefValidator;
	
	/*
	 * Convenzione: GET per le operazioni di lettura, POST per le operazioni di scrittura.
	 * Il path è associato alle classi del dominio
	 */
	
	@GetMapping("/admin/chef")
	public String getChef(Model model) {
		model.addAttribute("chef", new Chef()); // necessario perché la pagina chefForm.html si aspetta sempre 
												// di avere un oggetto Chef, anche vuoto, a disposizione
		return "chefForm.html";
	}
	
	@PostMapping("/admin/chef")
    public String addChef(@ModelAttribute("chef") Chef chef, 
    									Model model, BindingResult bindingResult) {
    	this.chefValidator.validate(chef, bindingResult);
        if (!bindingResult.hasErrors()) {
        	this.chefService.save(chef);
            model.addAttribute("chefs", this.chefService.findAll());
            return "chefs.html";
        }
        return "chefForm.html";
    }
	
	// richiede tutti gli chef (non viene specificato un id particolare)
	@GetMapping("/chef")
	public String getChefs(Model model) {
		List<Chef> chefs = this.chefService.findAll();
		model.addAttribute("chefs", chefs);
		return "chefs.html";
	}
	
	// richiede uno chef, dato il suo id
	@GetMapping("/chef/{id}")
	public String getChefById(@PathVariable("id") Long id, Model model) {
		Chef chef = this.chefService.findById(id);
		model.addAttribute("chef", chef);
		return "chef.html";
	}
	
//	@GetMapping("/toDeleteChef/{id}")
//	public String deleteChefById(@PathVariable("id") Long id, Model model) {
//		Chef chef =  this.chefService.findById(id);
//		this.chefService.delete(chef);
//		List<Chef> chefs = this.chefService.findAll();
//		model.addAttribute("chefs", chefs);
//		return "chefs.html";
//	}
	
	@GetMapping("/toDeleteChef/{id}")
	public String deleteChefById(@PathVariable("id") Long id, Model model) {
		Chef chef =  this.chefService.findById(id);
		model.addAttribute("chef", chef);
		return "confermaCancellazioneChef.html";
	}
	
	@GetMapping("/confirmDeleteChef/{id}")
	public String confirmDeleteChefById(@PathVariable("id") Long id, Model model) {
		this.chefService.deleteById(id);
		List<Chef> chefs = this.chefService.findAll();
		model.addAttribute("chefs", chefs);
		return "chefs.html";
	}
}