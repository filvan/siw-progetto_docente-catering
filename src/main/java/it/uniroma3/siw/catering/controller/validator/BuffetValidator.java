package it.uniroma3.siw.catering.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.service.BuffetService;

@Component
public class BuffetValidator implements Validator {

	@Autowired
	private BuffetService buffetService;

	private static final Logger logger = LoggerFactory.getLogger(BuffetValidator.class);

	@Override
	public void validate(Object target, Errors errors) {
		if (this.buffetService.alreadyExists((Buffet) target)) {
			logger.debug("e' un duplicato");
			errors.reject("buffet.duplicato");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Buffet.class.equals(aClass);
	}
}
