package it.epicode.w7d5.controller;

import it.epicode.w7d5.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtenteController {
    @Autowired
    private UtenteService utenteService;
}
