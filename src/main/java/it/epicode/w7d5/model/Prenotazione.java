package it.epicode.w7d5.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "prenotazioni")
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenza_prenotazioni")
    @SequenceGenerator(name = "sequenza_prenotazioni", initialValue = 1, allocationSize = 1)
    private int id;
    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;
    @ManyToOne
    @JoinColumn(name = "id_evento")
    private Evento evento;
    private LocalDate dataPrenotazione;
}
