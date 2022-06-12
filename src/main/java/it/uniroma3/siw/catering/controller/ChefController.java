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

import it.uniroma3.siw.catering.controller.validator.ChefValidator;
import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.service.BuffetService;
import it.uniroma3.siw.catering.service.ChefService;

@Controller
public class ChefController {

	@Autowired
	private ChefService chefService;

	@Autowired
	private BuffetService buffetService;

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
		return "admin/chefForm.html";
	}

	@PostMapping("/admin/chef")
	public String addChef(@Valid @ModelAttribute("chef") Chef chef, Model model, BindingResult bindingResult) {
		this.chefValidator.validate(chef, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.chefService.save(chef);
			model.addAttribute("chefs", this.chefService.findAll());
			return "admin/chefs.html";
		}
		return "admin/chefForm.html";
	}

	@GetMapping("/admin/addBuffetToChef/{chefId}")
	public String getBuffet(@PathVariable("chefId") Long id, Model model) {
		Chef chef = this.chefService.findById(id);
		model.addAttribute("chef", chef);

		List<Buffet> buffetSenzaChef = this.buffetService.findAll();

		for (Iterator<Buffet> iterator = buffetSenzaChef.iterator(); iterator.hasNext();) {
			Buffet buffet = (Buffet) iterator.next();
			if (buffet.getChef() != null)
				iterator.remove();
		}

		model.addAttribute("buffets", buffetSenzaChef);
		model.addAttribute("buffet", new Buffet()); // necessario perché la pagina aggiungiBuffetAlloChef.html si aspetta sempre 
		// di avere un oggetto Buffet, anche vuoto, a disposizione
		return "admin/aggiungiBuffetAlloChef.html";
	}

	@PostMapping("/admin/addBuffetToChef/{chefId}")
	public String addBuffetToChef(@PathVariable("chefId") Long id, @ModelAttribute("buffet") Buffet buffet, 
			Model model, BindingResult bindingResult) {
		Chef chef = this.chefService.findById(id);
		buffet.setChef(chef);
		this.buffetService.save(buffet);
		model.addAttribute("chefs", this.chefService.findAll());
		return "admin/chefs.html";
	}

	@GetMapping("/admin/toRemoveBuffet/{buffetId}")
	public String removeBuffetById(@PathVariable("buffetId") Long id, Model model) {
		Buffet buffet =  this.buffetService.findById(id);
		Chef chef = buffet.getChef();
		buffet.setChef(null);
		chef.getBuffetProposti().remove(buffet);
		this.buffetService.save(buffet);
		return this.getAdminChefById(chef.getId(), model);
	}

	// richiede tutti gli chef (non viene specificato un id particolare)
	@GetMapping("/admin/chefs")
	public String getAdminChefs(Model model) {
		List<Chef> chefs = this.chefService.findAll();
		model.addAttribute("chefs", chefs);
		return "admin/chefs.html";
	}

	// richiede tutti gli chef (non viene specificato un id particolare)
	@GetMapping("/user/chefs")
	public String getUserChefs(Model model) {
		List<Chef> chefs = this.chefService.findAll();
		model.addAttribute("chefs", chefs);
		return "user/chefs.html";
	}

	// richiede uno chef, dato il suo id
	@GetMapping("/admin/chef/{id}")
	public String getAdminChefById(@PathVariable("id") Long id, Model model) {
		Chef chef = this.chefService.findById(id);
		model.addAttribute("chef", chef);
		List<Buffet> buffets = chef.getBuffetProposti();
		model.addAttribute("buffets", buffets);
		return "admin/chef.html";
	}

	// richiede uno chef, dato il suo id
	@GetMapping("/user/chef/{id}")
	public String getUserChefById(@PathVariable("id") Long id, Model model) {
		Chef chef = this.chefService.findById(id);
		model.addAttribute("chef", chef);
		List<Buffet> buffets = chef.getBuffetProposti();
		model.addAttribute("buffets", buffets);
		return "user/chef.html";
	}

	//	@GetMapping("/admin/toDeleteChef/{id}")
	//	public String deleteChefById(@PathVariable("id") Long id, Model model) {
	//		Chef chef =  this.chefService.findById(id);
	//		this.chefService.delete(chef);
	//		List<Chef> chefs = this.chefService.findAll();
	//		model.addAttribute("chefs", chefs);
	//		return "admin/chefs.html";
	//	}

	@GetMapping("/admin/toDeleteChef/{id}")
	public String deleteChefById(@PathVariable("id") Long id, Model model) {
		Chef chef =  this.chefService.findById(id);
		model.addAttribute("chef", chef);
		return "admin/confermaCancellazioneChef.html";
	}

	@GetMapping("/admin/confirmDeleteChef/{id}")
	public String confirmDeleteChefById(@PathVariable("id") Long id, Model model) {
		Chef chef =  this.chefService.findById(id);
		for (Buffet buffet : chef.getBuffetProposti()) {
			buffet.setChef(null);
		}
		this.chefService.delete(chef);
		return this.getAdminChefs(model);
	}
}