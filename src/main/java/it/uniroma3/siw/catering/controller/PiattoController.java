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

import it.uniroma3.siw.catering.controller.validator.PiattoValidator;
import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.service.IngredienteService;
import it.uniroma3.siw.catering.service.PiattoService;

@Controller
public class PiattoController {

	@Autowired
	private PiattoService piattoService;

	@Autowired
	private PiattoValidator piattoValidator;

	@Autowired
	private IngredienteService ingredienteService;

	/*
	 * Convenzione: GET per le operazioni di lettura, POST per le operazioni di scrittura.
	 * Il path è associato alle classi del dominio
	 */

	@GetMapping("/admin/piatto")
	public String getPiatto(Model model) {
		model.addAttribute("piatto", new Piatto()); // necessario perché la pagina piattoForm.html si aspetta sempre 
		// di avere un oggetto Piatto, anche vuoto, a disposizione
		return "admin/piattoForm.html";
	}

	@PostMapping("/admin/piatto")
	public String addPiatto(@Valid @ModelAttribute("piatto") Piatto piatto, Model model, BindingResult bindingResult) {
		this.piattoValidator.validate(piatto, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.piattoService.save(piatto);
			model.addAttribute("piatti", this.piattoService.findAll());
			return "admin/piatti.html";
		}
		return "admin/piattoForm.html";
	}
	
	@GetMapping("/admin/addIngredienteToPiatto/{piattoId}")
	public String getIngrediente(@PathVariable("piattoId") Long id,	Model model) {
		Piatto piatto = this.piattoService.findById(id);
		model.addAttribute("piatto", piatto);
		
		List<Ingrediente> ingredientiNonGiaInseriti = this.ingredienteService.findAll();

		for (Iterator<Ingrediente> iterator = ingredientiNonGiaInseriti.iterator(); iterator.hasNext();) {
			Ingrediente ingrediente = (Ingrediente) iterator.next();
			if (ingrediente.getPiatti().contains(piatto))
				iterator.remove();
		}
		
		model.addAttribute("ingredienti", ingredientiNonGiaInseriti);
		model.addAttribute("ingrediente", new Ingrediente()); // necessario perché la pagina aggiungiIngredienteAlPiatto.html si aspetta sempre 
		// di avere un oggetto Ingrediente, anche vuoto, a disposizione
		return "admin/aggiungiIngredienteAlPiatto.html";
	}
	
	@PostMapping("/admin/addIngredienteToPiatto/{piattoId}")
	public String addIngredienteToPiatto(@PathVariable("piattoId") Long id, @Valid @ModelAttribute("ingrediente") Ingrediente ingrediente,
			Model model, BindingResult bindingResult) {
		Piatto piatto = this.piattoService.findById(id);
		ingrediente.addPiatto(piatto);
		piatto.addIngrediente(ingrediente);
		this.ingredienteService.save(ingrediente);
		this.piattoService.save(piatto);
		model.addAttribute("piatti", this.piattoService.findAll());
		return "admin/piatti.html";
	}
	
	@GetMapping("/admin/toRemoveIngrediente/{ingredienteId}/From/{piattoId}")
	public String removeIngredienteById(@PathVariable("ingredienteId") Long ingredienteId, @PathVariable("piattoId") Long piattoId, Model model) {
		Ingrediente ingrediente =  this.ingredienteService.findById(ingredienteId);
		Piatto piatto =  this.piattoService.findById(piattoId);
		ingrediente.removePiatto(piatto);
		piatto.removeIngrediente(ingrediente);
		this.ingredienteService.save(ingrediente);
		this.piattoService.save(piatto);
		return this.getAdminPiattoById(piatto.getId(), model);
	}

	// richiede tutti i piatti (non viene specificato un id particolare)
	@GetMapping("/admin/piatti")
	public String getAdminPiatti(Model model) {
		List<Piatto> piatti = this.piattoService.findAll();
		model.addAttribute("piatti", piatti);
		return "admin/piatti.html";
	}

	// richiede tutti i piatti (non viene specificato un id particolare)
	@GetMapping("/user/piatti")
	public String getUserPiatti(Model model) {
		List<Piatto> piatti = this.piattoService.findAll();
		model.addAttribute("piatti", piatti);
		return "user/piatti.html";
	}

	// richiede un piatto, dato il suo id
	@GetMapping("/admin/piatto/{id}")
	public String getAdminPiattoById(@PathVariable("id") Long id, Model model) {
		Piatto piatto = this.piattoService.findById(id);
		model.addAttribute("piatto", piatto);
		List<Buffet> buffets = piatto.getBuffets();
		model.addAttribute("buffets", buffets);
		List<Ingrediente> ingredienti = piatto.getIngredienti();
		model.addAttribute("ingredienti", ingredienti);
		return "admin/piatto.html";
	}

	// richiede un piatto, dato il suo id
	@GetMapping("/user/piatto/{id}")
	public String getUserPiattoById(@PathVariable("id") Long id, Model model) {
		Piatto piatto = this.piattoService.findById(id);
		model.addAttribute("piatto", piatto);
		List<Buffet> buffets = piatto.getBuffets();
		model.addAttribute("buffets", buffets);
		List<Ingrediente> ingredienti = piatto.getIngredienti();
		model.addAttribute("ingredienti", ingredienti);
		return "user/piatto.html";
	}

	//	@GetMapping("/admin/toDeletePiatto/{id}")
	//	public String deletePiattoById(@PathVariable("id") Long id, Model model) {
	//		Piatto piatto =  this.piattoService.findById(id);
	//		this.piattoService.delete(piatto);
	//		List<Piatto> piatti = this.piattoService.findAll();
	//		model.addAttribute("piatti", piatti);
	//		return "admin/piatti.html";
	//	}

	@GetMapping("/admin/toDeletePiatto/{id}")
	public String deletePiattoById(@PathVariable("id") Long id, Model model) {
		Piatto piatto =  this.piattoService.findById(id);
		model.addAttribute("piatto", piatto);
		return "admin/confermaCancellazionePiatto.html";
	}

	@GetMapping("/admin/confirmDeletePiatto/{id}")
	public String confirmDeletePiattoById(@PathVariable("id") Long id, Model model) {
		Piatto piatto =  this.piattoService.findById(id);
		this.piattoService.delete(piatto);
		return this.getAdminPiatti(model);
	}
}