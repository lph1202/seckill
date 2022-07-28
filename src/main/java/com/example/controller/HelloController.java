package com.example.controller;

import com.example.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
public class HelloController {

    @Value("${name}")
    private String name;

    @Value("${address[0]}")
    private String beijing;

    @Autowired
    private Environment environment;

    @Autowired
    private Person person;

    @RequestMapping("/hello4")
    public String hello4(){
        System.out.println(person);
        return person.getName()+"|||"+person.getFather();
    }

    @RequestMapping("/hello3")
    public String hello3(){
        return environment.getProperty("person.name")+"|||||"+environment.getProperty("person.father");
    }

    @RequestMapping("/hello2")
    public String hello2(){
        return name+"|||||"+beijing;
    }

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }
}
