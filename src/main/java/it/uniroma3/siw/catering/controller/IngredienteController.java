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

import it.uniroma3.siw.catering.controller.validator.IngredienteValidator;
import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.service.IngredienteService;
import it.uniroma3.siw.catering.service.PiattoService;

@Controller
public class IngredienteController {

	@Autowired
	private IngredienteService ingredienteService;

	@Autowired
	private IngredienteValidator ingredienteValidator;

	@Autowired
	private PiattoService piattoService;

	/*
	 * Convenzione: GET per le operazioni di lettura, POST per le operazioni di scrittura.
	 * Il path è associato alle classi del dominio
	 */

	@GetMapping("/admin/ingrediente")
	public String getIngrediente(Model model) {
		model.addAttribute("ingrediente", new Ingrediente()); // necessario perché la pagina ingredienteForm.html si aspetta sempre 
		// di avere un oggetto Ingrediente, anche vuoto, a disposizione
		return "admin/ingredienteForm.html";
	}

	@PostMapping("/admin/ingrediente")
	public String addIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, Model model, BindingResult bindingResult) {
		this.ingredienteValidator.validate(ingrediente, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.ingredienteService.save(ingrediente);
			model.addAttribute("ingredienti", this.ingredienteService.findAll());
			return "admin/ingredienti.html";
		}
		return "admin/ingredienteForm.html";
	}

	// richiede tutti gli ingredienti (non viene specificato un id particolare)
	@GetMapping("/admin/ingredienti")
	public String getAdminIngredienti(Model model) {
		List<Ingrediente> ingredienti = this.ingredienteService.findAll();
		model.addAttribute("ingredienti", ingredienti);
		return "admin/ingredienti.html";
	}

	/* L'utente è interessato solo agli ingredienti utilizzati in un piatto di un buffet.
	 * Gli altri ingredienti possono comparire nell'ambiente dell'amministratore ma,
	 * finchè i rispettivi piatti non vengono inseriti in un buffet, è come se fossero in uno stato inconsistente e,
	 * perciò, non devono comparire nell'ambiente dell'utente */
	
	@GetMapping("/user/ingredienti")
	public String getUserIngredienti(Model model) {
		List<Ingrediente> ingredienti = this.ingredienteService.findAll();

		for (Iterator<Ingrediente> iterator = ingredienti.iterator(); iterator.hasNext();) {
			int numeroPiattiAssentiDaOgniBuffet = 0;
			Ingrediente ingrediente = (Ingrediente) iterator.next();
			List<Piatto> piatti = ingrediente.getPiatti();
			for(Piatto piatto : piatti)
				if (piatto.getBuffets().size() == 0)
					numeroPiattiAssentiDaOgniBuffet++;
			if (piatti.size() == numeroPiattiAssentiDaOgniBuffet)
				iterator.remove();
		}

		model.addAttribute("ingredienti", ingredienti);
		return "user/ingredienti.html";
	}

	// richiede un ingrediente, dato il suo id
	@GetMapping("/admin/ingrediente/{id}")
	public String getAdminIngredienteById(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = this.ingredienteService.findById(id);
		model.addAttribute("ingrediente", ingrediente);
		List<Piatto> piatti = ingrediente.getPiatti();
		model.addAttribute("piatti", piatti);
		return "admin/ingrediente.html";
	}

	// richiede un ingrediente, dato il suo id
	@GetMapping("/user/ingrediente/{id}")
	public String getUserIngredienteById(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = this.ingredienteService.findById(id);
		model.addAttribute("ingrediente", ingrediente);
		List<Piatto> piatti = ingrediente.getPiatti();
		model.addAttribute("piatti", piatti);
		return "user/ingrediente.html";
	}

	//	@GetMapping("/admin/toDeleteIngrediente/{id}")
	//	public String deleteIngredienteById(@PathVariable("id") Long id, Model model) {
	//		Ingrediente ingrediente =  this.ingredienteService.findById(id);
	//		this.ingredienteService.delete(ingrediente);
	//		List<Ingrediente> ingredienti = this.ingredienteService.findAll();
	//		model.addAttribute("ingredienti", ingredienti);
	//		return "admin/ingredienti.html";
	//	}

	@GetMapping("/admin/toDeleteIngrediente/{id}")
	public String deleteIngredienteById(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente =  this.ingredienteService.findById(id);
		model.addAttribute("ingrediente", ingrediente);
		return "admin/confermaCancellazioneIngrediente.html";
	}

	@GetMapping("/admin/confirmDeleteIngrediente/{id}")
	public String confirmDeleteIngredienteById(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente =  this.ingredienteService.findById(id);
		for (Piatto piatto : ingrediente.getPiatti()) {
			piatto.removeIngrediente(ingrediente);
			this.piattoService.save(piatto);
		}
		this.ingredienteService.delete(ingrediente);
		return this.getAdminIngredienti(model);
	}
	
	@GetMapping("/admin/modifyIngrediente/{id}")
	public String modifyIngrediente(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente =  this.ingredienteService.findById(id);
		model.addAttribute("ingrediente", ingrediente);
		return "admin/modificaIngredienteForm.html";
	}
	
	@PostMapping("/admin/confirmModifyIngrediente/{id}")
	public String confirmModifyIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, Model model, BindingResult bindingResult) {
			this.ingredienteService.update(ingrediente.getId(), ingrediente.getNome(), ingrediente.getOrigine(), ingrediente.getDescrizione());
			model.addAttribute("ingredienti", this.ingredienteService.findAll());
			return "admin/ingredienti.html";
	}
}