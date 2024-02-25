package it.epicode.w7d5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "eventi")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenza_eventi")
    @SequenceGenerator(name = "sequenza_eventi", initialValue = 1, allocationSize = 1)
    private int id;
    private String titolo;
    private String descrizione;
    private LocalDate data;
    private String luogo;
    private int numeroPostiDisponibili;
    @ManyToMany(mappedBy = "eventi")
    @JsonIgnore
    private List<Utente> utenti;
}
