package com.rost.springsecurity1.service;

import com.rost.springsecurity1.models.Person;
import com.rost.springsecurity1.repository.PersonRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {
    private final PersonRep personRep;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public RegistrationService(PersonRep personRep, PasswordEncoder passwordEncoder) {
        this.personRep = personRep;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void registerPerson(Person person){
        String encodedPassword= passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
        person.setRole("ROLE_USER");
        personRep.save(person);
    }
}
