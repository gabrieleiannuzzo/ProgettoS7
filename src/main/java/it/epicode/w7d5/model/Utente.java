package it.epicode.w7d5.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "utenti")
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenza_utenti")
    @SequenceGenerator(name = "sequenza_utenti", initialValue = 1, allocationSize = 1)
    private int id;
    private String nome;
    private String cognome;
    @Column(unique = true)
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}