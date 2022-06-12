package it.uniroma3.siw.catering.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.service.PiattoService;

@Component
public class PiattoValidator implements Validator {
	
	@Autowired
	private PiattoService piattoService;

	private static final Logger logger = LoggerFactory.getLogger(PiattoValidator.class);

	@Override
	public void validate(Object target, Errors errors) {
		if (this.piattoService.alreadyExists((Piatto) target)) {
			logger.debug("e' un duplicato");
			errors.reject("piatto.duplicato");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Piatto.class.equals(aClass);
	}
}