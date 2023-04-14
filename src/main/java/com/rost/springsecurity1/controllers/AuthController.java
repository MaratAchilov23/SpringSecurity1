package com.rost.springsecurity1.controllers;

import com.rost.springsecurity1.dto.PersonDTO;
import com.rost.springsecurity1.models.Person;
import com.rost.springsecurity1.security.JWTUtil;
import com.rost.springsecurity1.service.RegistrationService;
import com.rost.springsecurity1.util.PersonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PersonValidate personValidate;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    @Autowired
    public AuthController(PersonValidate personValidate, RegistrationService registrationService, JWTUtil jwtUtil, ModelMapper modelMapper) {
        this.personValidate = personValidate;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public String loginPage(){
        return  "auth";
    }
    @GetMapping ("/registration")
    public String registrationPage(@ModelAttribute("person") Person person){
        return "registration1";
    }
    @PostMapping("registration")
    public Map<String, String> performRegistration(@RequestBody  @Valid PersonDTO personDTO,
                                                   BindingResult bindingResult){
        Person person = convertToPerson(personDTO);

        personValidate.validate(person,bindingResult);
        if(bindingResult.hasErrors())
            return Map.of("message", "ошибка");
        registrationService.registerPerson(person);

        String token = jwtUtil.generateToken(person.getUsername());
        return Map.of("jwt-token",token);
    }
    public Person convertToPerson(PersonDTO personDTO){
        return modelMapper.map(personDTO, Person.class);
    }

}
