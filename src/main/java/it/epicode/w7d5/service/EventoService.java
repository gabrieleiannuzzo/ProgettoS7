package it.epicode.w7d5.service;

import it.epicode.w7d5.DTO.EventoDTO;
import it.epicode.w7d5.exception.NotFoundException;
import it.epicode.w7d5.model.Evento;
import it.epicode.w7d5.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;

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
}
