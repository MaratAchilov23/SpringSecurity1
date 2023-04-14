package com.rost.springsecurity1.util;

import com.rost.springsecurity1.models.Person;
import com.rost.springsecurity1.service.PersonDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class PersonValidate implements Validator {

    private final PersonDetailService personDetailService;
    @Autowired
    public PersonValidate(PersonDetailService personDetailService) {
        this.personDetailService = personDetailService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

 //   @Override
 //   public void validate(Object target, Errors errors) {
 //       Person person = (Person)target;
    //      if(personDetailService.findUserByName(person.getUsername()).isEmpty())
 //           return personDetailService.;
  //  }
    @Override
    public void validate(Object object, Errors errors){
        Person person = (Person)object;
        try{
            personDetailService.loadUserByUsername(person.getUsername());
        }catch (UsernameNotFoundException ignored) {
            return;
        }
        errors.rejectValue("username","","Чел с таким именем уже есть");
    }
}
