package it.epicode.w7d5.controller;

import it.epicode.w7d5.DTO.ChangeRoleDTO;
import it.epicode.w7d5.DTO.UtenteDTO;
import it.epicode.w7d5.exception.BadRequestException;
import it.epicode.w7d5.exception.ErrorResponse;
import it.epicode.w7d5.exception.UnauthorizedException;
import it.epicode.w7d5.model.CustomResponse;
import it.epicode.w7d5.security.JwtTools;
import it.epicode.w7d5.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class UtenteController {
    @Autowired
    private UtenteService utenteService;

    @Autowired
    private JwtTools jwtTools;

    // Siccome non ci sono admin, lascio gli endpoint GET liberi di essere visitati da tutti gli utenti (quindi bisogna essere comunque loggati per visitarli, ma non bisogna essere necessariamente organizzatori o admin)
    @GetMapping("/utenti")
    public CustomResponse getAll(Pageable pageable){
        return new CustomResponse(HttpStatus.OK.toString(), utenteService.getAll(pageable));
    }

    @GetMapping("/utenti/{username}")
    public CustomResponse getByUsername(@PathVariable String username){
        return new CustomResponse(HttpStatus.OK.toString(), utenteService.getByUsername(username));
    }

    @PutMapping("/utenti/{username}")
    public CustomResponse update(@PathVariable String username, @RequestBody @Validated UtenteDTO utenteDTO, BindingResult bindingResult, @RequestHeader("Authorization") String jwt){
        if (bindingResult.hasErrors()) throw new BadRequestException(ErrorResponse.handleValidationMessages(bindingResult));
        checkUser(username, jwt);
        return new CustomResponse(HttpStatus.OK.toString(), utenteService.update(username, utenteDTO));
    }

    @PatchMapping("/utenti/{username}")
    public CustomResponse changeRole(@PathVariable String username, @RequestBody ChangeRoleDTO changeRoleDTO, @RequestHeader("Authorization") String jwt){
        checkUser(username, jwt);
        return new CustomResponse(HttpStatus.OK.toString(), utenteService.changeRole(username, changeRoleDTO.getRole()));
    }

    @DeleteMapping("/utenti/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CustomResponse delete(@PathVariable String username, @RequestHeader("Authorization") String jwt){
        checkUser(username, jwt);
        utenteService.delete(username);
        return new CustomResponse(HttpStatus.NO_CONTENT.toString(), null);
    }

    private void checkUser(String username, String jwt){
        if (!username.equals(jwtTools.extractUsernameFromToken(jwt.substring(7)))) throw new UnauthorizedException("Non sei autorizzato ad eseguire questa operazione");
    }
}
