package it.uniroma3.siw.catering.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.service.ChefService;

@Component
public class ChefValidator implements Validator {

	@Autowired
	private ChefService chefService;

	private static final Logger logger = LoggerFactory.getLogger(ChefValidator.class);

	@Override
	public void validate(Object target, Errors errors) {
		if (this.chefService.alreadyExists((Chef) target)) {
			logger.debug("e' un duplicato");
			errors.reject("chef.duplicato");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Chef.class.equals(aClass);
	}
}
