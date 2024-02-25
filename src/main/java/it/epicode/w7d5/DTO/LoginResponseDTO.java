package it.epicode.w7d5.DTO;

import it.epicode.w7d5.model.Utente;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String jwt;
    private Utente utente;

    public LoginResponseDTO(String jwt, Utente utente){
        this.jwt = jwt;
        this.utente = utente;
    }
}
