package it.epicode.w7d5.service;

import it.epicode.w7d5.exception.NotFoundException;
import it.epicode.w7d5.model.Utente;
import it.epicode.w7d5.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;

    public Utente getByUsername(String username){
        return utenteRepository.getByUsername(username).orElseThrow(() -> new NotFoundException("Utente con username " + username + " non trovato"));
    }
}
