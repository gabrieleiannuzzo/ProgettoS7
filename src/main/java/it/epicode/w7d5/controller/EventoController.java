package it.epicode.w7d5.controller;

import it.epicode.w7d5.DTO.EventoDTO;
import it.epicode.w7d5.exception.BadRequestException;
import it.epicode.w7d5.exception.ErrorResponse;
import it.epicode.w7d5.model.CustomResponse;
import it.epicode.w7d5.security.JwtTools;
import it.epicode.w7d5.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class EventoController {
    @Autowired
    private EventoService eventoService;

    @Autowired
    private JwtTools jwtTools;

    @GetMapping("/eventi")
    public CustomResponse getAll(Pageable pageable){
        return new CustomResponse(HttpStatus.OK.toString(), eventoService.getAll(pageable));
    }

    @GetMapping("/eventi/{id}")
    public CustomResponse getById(@PathVariable int id){
        return new CustomResponse(HttpStatus.OK.toString(), eventoService.getById(id));
    }

    @PostMapping("/eventi")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomResponse save(@RequestBody @Validated EventoDTO eventoDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new BadRequestException(ErrorResponse.handleValidationMessages(bindingResult));
        return new CustomResponse(HttpStatus.CREATED.toString(), eventoService.save(eventoDTO));
    }

    @PutMapping("/eventi/{id}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public CustomResponse update(@PathVariable int id, @RequestBody @Validated EventoDTO eventoDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new BadRequestException(ErrorResponse.handleValidationMessages(bindingResult));
        return new CustomResponse(HttpStatus.OK.toString(), eventoService.update(id, eventoDTO));
    }

    @DeleteMapping("/eventi/{id}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CustomResponse delete(@PathVariable int id){
        eventoService.delete(id);
        return new CustomResponse(HttpStatus.NO_CONTENT.toString(), null);
    }

    @PostMapping("/eventi/{id}/prenota")
    public CustomResponse prenota(@PathVariable int id, @RequestHeader("Authorization") String jwt){
        // Recupero dal jwt l'username dell'utente
        String username = jwtTools.extractUsernameFromToken(jwt.substring(7));
        return new CustomResponse(HttpStatus.OK.toString(), eventoService.prenota(id, username));
    }

    @GetMapping("/eventi/{id}/prenotazioni")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public CustomResponse getPrenotazioniByIdEvento(@PathVariable int id){
        return new CustomResponse(HttpStatus.OK.toString(), eventoService.getPrenotazioniByIdEvento(id));
    }
}
