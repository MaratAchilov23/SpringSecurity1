package com.rost.springsecurity1.service;


import com.rost.springsecurity1.models.Person;
import com.rost.springsecurity1.repository.PersonRep;
import com.rost.springsecurity1.security.PersonDetails;
import org.springframework.security.access.method.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailService implements UserDetailsService {
    private PersonRep personRep;

    public PersonDetailService(PersonRep personRep) {
        this.personRep = personRep;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person>person= personRep.findByUsername(username);
        if(person.isEmpty())
            throw new UsernameNotFoundException("User not found");
        return new PersonDetails(person.get());
    }
    public Optional<Person>findUserByName(String username){

          Optional<Person> person= personRep.findByUsername(username);
          if(person.isPresent())
              return person;
          throw new UsernameNotFoundException("Person is not found");
    }
}
