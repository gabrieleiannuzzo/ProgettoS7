package it.epicode.w7d5.controller;

import it.epicode.w7d5.DTO.ChangeRoleDTO;
import it.epicode.w7d5.DTO.UtenteDTO;
import it.epicode.w7d5.exception.BadRequestException;
import it.epicode.w7d5.exception.ErrorResponse;
import it.epicode.w7d5.model.CustomResponse;
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

    @GetMapping("/utenti")
    public CustomResponse getAll(Pageable pageable){
        return new CustomResponse(HttpStatus.OK.toString(), utenteService.getAll(pageable));
    }

    @GetMapping("/utenti/{username}")
    public CustomResponse getByUsername(@PathVariable String username){
        return new CustomResponse(HttpStatus.OK.toString(), utenteService.getByUsername(username));
    }

    @PutMapping("/utenti/{username}")
    public CustomResponse update(@PathVariable String username, @RequestBody @Validated UtenteDTO utenteDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new BadRequestException(ErrorResponse.handleValidationMessages(bindingResult));
        return new CustomResponse(HttpStatus.OK.toString(), utenteService.update(username, utenteDTO));
    }

    @PatchMapping("/utenti/{username}")
    public CustomResponse changeRole(@PathVariable String username, @RequestBody ChangeRoleDTO changeRoleDTO){
        return new CustomResponse(HttpStatus.OK.toString(), utenteService.changeRole(username, changeRoleDTO.getRole()));
    }

    @DeleteMapping("/utenti/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CustomResponse delete(@PathVariable String username){
        utenteService.delete(username);
        return new CustomResponse(HttpStatus.NO_CONTENT.toString(), null);
    }
}
