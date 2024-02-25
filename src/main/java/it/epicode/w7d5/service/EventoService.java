package it.epicode.w7d5.service;

import it.epicode.w7d5.DTO.EventoDTO;
import it.epicode.w7d5.exception.BadRequestException;
import it.epicode.w7d5.exception.ConflictException;
import it.epicode.w7d5.exception.NotFoundException;
import it.epicode.w7d5.model.Evento;
import it.epicode.w7d5.model.Prenotazione;
import it.epicode.w7d5.model.Utente;
import it.epicode.w7d5.repository.EventoRepository;
import it.epicode.w7d5.repository.PrenotazioneRepository;
import it.epicode.w7d5.security.JwtTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private JwtTools jwtTools;

    public Page<Evento> getAll(Pageable pageable){
        return eventoRepository.findAll(pageable);
    }

    public Evento getById(int id){
        return eventoRepository.findById(id).orElseThrow(() -> new NotFoundException("Evento con id " + id + " non trovato"));
    }

    public Evento save(EventoDTO eventoDTO){
        Evento evento = new Evento();
        evento.setTitolo(eventoDTO.getTitolo());
        evento.setDescrizione(eventoDTO.getDescrizione());
        evento.setData(eventoDTO.getData());
        evento.setLuogo(eventoDTO.getLuogo());
        evento.setNumeroPostiDisponibili(eventoDTO.getNumeroPostiDisponibili());
        return eventoRepository.save(evento);
    }

    public Evento update(int id, EventoDTO eventoDTO){
        Evento evento = getById(id);
        if (evento.getPrenotazioni().size() > eventoDTO.getNumeroPostiDisponibili()) throw new ConflictException("Il numero di posti disponibili non può essere minore del numero di posti già prenotati dagli utenti");
        evento.setNumeroPostiDisponibili(eventoDTO.getNumeroPostiDisponibili());
        evento.setTitolo(eventoDTO.getTitolo());
        evento.setDescrizione(eventoDTO.getDescrizione());
        evento.setData(eventoDTO.getData());
        evento.setLuogo(eventoDTO.getLuogo());
        return eventoRepository.save(evento);
    }

    public void delete(int id){
        Evento evento = getById(id);
        eventoRepository.delete(evento);
    }

    public Prenotazione prenota(int id, String username){
        Evento evento = getById(id);
        if (evento.getPrenotazioni().size() == evento.getNumeroPostiDisponibili()) throw new ConflictException("Il numero massimo di posti è già stato prenotato");
        Utente utente = utenteService.getByUsername(username);
        if (prenotazioneRepository.getPrenotazioneByIdEventoEIdUtente(id, utente.getId()) != null) throw new ConflictException("Hai già prenotato questo evento");
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setEvento(evento);
        prenotazione.setUtente(utente);
        prenotazione.setDataPrenotazione(LocalDate.now());
        return prenotazioneRepository.save(prenotazione);
    }

    public void annullaPrenotazione(int id, String username){
        Utente utente = utenteService.getByUsername(username);
        Prenotazione prenotazione = prenotazioneRepository.getPrenotazioneByIdEventoEIdUtente(id, utente.getId());
        if (prenotazione == null) throw new ConflictException("Nessuna prenotazione di questo utente per questo evento");
        prenotazioneRepository.delete(prenotazione);
    }

    public List<Prenotazione> getPrenotazioniByIdEvento(int id){
        return prenotazioneRepository.getPrenotazioniByIdEvento(id);
    }
}
