package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michael Isvy Simple test to make sure that Bean Validation is working
 *         (useful when upgrading to a new version of Hibernate Validator/ Bean
 *         Validation)
 */
public class ValidatorTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void shouldNotValidateWhenFirstNameEmpty() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Person person = new Person();
		person.setFirstName("");
		person.setLastName("smith");

		Validator validator = createValidator();
		Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Person> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
	}

//	@Test
//	public void shouldNotValidateWhentelEmpty() {
//
//		LocaleContextHolder.setLocale(Locale.ENGLISH);
//
//		Owner owner = new Owner();
//		owner.setFirstName("test");
//		owner.setLastName("owner");
//		owner.setAddress("test");
//		owner.setCity("sample");
//		owner.setTelephone("11224455");
//
//		Validator validator = createValidator();
//		Set<ConstraintViolation<Owner>> constraintViolations = validator.validate(owner);
//
//		assertThat(constraintViolations.size()).isEqualTo(0);
//		ConstraintViolation<Owner> violation = constraintViolations.iterator().next();
//		assertThat(violation.getPropertyPath().toString()).isEqualTo("telephone");
//		assertThat(violation.getMessage()).isEqualTo("must not be empty");
//	}

}
