package it.epicode.w7d5.service;

import it.epicode.w7d5.DTO.LoginDTO;
import it.epicode.w7d5.DTO.LoginResponseDTO;
import it.epicode.w7d5.DTO.UtenteDTO;
import it.epicode.w7d5.exception.BadRequestException;
import it.epicode.w7d5.exception.LoginFaultException;
import it.epicode.w7d5.exception.NotFoundException;
import it.epicode.w7d5.model.Role;
import it.epicode.w7d5.model.Utente;
import it.epicode.w7d5.repository.UtenteRepository;
import it.epicode.w7d5.security.JwtTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtTools jwtTools;

    public Page<Utente> getAll(Pageable pageable){
        return utenteRepository.findAll(pageable);
    }

    public Utente getById(int id){
        return utenteRepository.findById(id).orElseThrow(() -> new NotFoundException("Utente con id " + id + " non trovato"));
    }

    public Utente getByUsername(String username){
        return utenteRepository.getByUsername(username).orElseThrow(() -> new NotFoundException("Utente con username " + username + " non trovato"));
    }

    public Utente save(UtenteDTO utenteDTO){
        Utente utente = new Utente();
        utente.setNome(utenteDTO.getNome());
        utente.setCognome(utenteDTO.getCognome());
        utente.setUsername(utenteDTO.getUsername());
        utente.setPassword(encoder.encode(utenteDTO.getPassword()));
        utente.setRole(Role.UTENTE);
        return utenteRepository.save(utente);
    }

    public Utente update(String username, UtenteDTO utenteDTO){
        Utente utente = getByUsername(username);
        utente.setNome(utenteDTO.getNome());
        utente.setCognome(utenteDTO.getCognome());
        utente.setUsername(utenteDTO.getUsername());
        utente.setPassword(encoder.encode(utenteDTO.getPassword()));
        return utenteRepository.save(utente);
    }

    public void delete(String username){
        Utente utente = getByUsername(username);
        utenteRepository.delete(utente);
    }

    public LoginResponseDTO login(LoginDTO loginDTO){
        Utente utente = getByUsername(loginDTO.getUsername());
        if (!encoder.matches(loginDTO.getPassword(), utente.getPassword())) throw new LoginFaultException("Username/password errati");
        return new LoginResponseDTO(jwtTools.createToken(utente), utente);
    }

    public Utente changeRole(String username, Role role){
        Utente utente = getByUsername(username);
        if (utente.getRole() == role) throw new BadRequestException("Il ruolo selezionato è lo stesso ruolo che l'utente già ha");
        utente.setRole(role);
        return utenteRepository.save(utente);
    }
}
