package it.uniroma3.siw.catering.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.service.IngredienteService;

@Component
public class IngredienteValidator implements Validator {
	
	@Autowired
	private IngredienteService ingredienteService;

	private static final Logger logger = LoggerFactory.getLogger(IngredienteValidator.class);

	@Override
	public void validate(Object target, Errors errors) {
		if (this.ingredienteService.alreadyExists((Ingrediente) target)) {
			logger.debug("e' un duplicato");
			errors.reject("ingrediente.duplicato");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Ingrediente.class.equals(aClass);
	}
}