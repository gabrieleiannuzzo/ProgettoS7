package it.epicode.w7d5.repository;

import it.epicode.w7d5.model.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Integer>, PagingAndSortingRepository<Prenotazione, Integer> {
    @Query("SELECT p FROM Prenotazione p WHERE p.evento.id = :idEvento AND p.utente.id = :idUtente")
    public Prenotazione checkPrenotazioneByIdEventoEIdUtente(int idEvento, int idUtente);

    @Query("SELECT p FROM Prenotazione p WHERE p.evento.id = :id")
    public List<Prenotazione> getPrenotazioniByIdEvento(int id);
}
