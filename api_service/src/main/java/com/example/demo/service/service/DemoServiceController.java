package com.example.demo.service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("service")
public class DemoServiceController {

    @Value("${word}")
    private String word;

    @RequestMapping("/hello")
    public String hello(@RequestParam String name){
        System.out.println(word);
        return word + "," + name;
    }

}
