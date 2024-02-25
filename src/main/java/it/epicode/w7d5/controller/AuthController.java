package it.epicode.w7d5.controller;

import it.epicode.w7d5.DTO.LoginDTO;
import it.epicode.w7d5.DTO.UtenteDTO;
import it.epicode.w7d5.exception.BadRequestException;
import it.epicode.w7d5.exception.ErrorResponse;
import it.epicode.w7d5.model.CustomResponse;
import it.epicode.w7d5.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private UtenteService utenteService;

    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomResponse register(@RequestBody @Validated UtenteDTO utenteDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new BadRequestException(ErrorResponse.handleValidationMessages(bindingResult));
        return new CustomResponse(HttpStatus.CREATED.toString(), utenteService.save(utenteDTO));
    }

    @PostMapping("/auth/login")
    public CustomResponse login(@RequestBody @Validated LoginDTO loginDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new BadRequestException(ErrorResponse.handleValidationMessages(bindingResult));
        return new CustomResponse(HttpStatus.OK.toString(), utenteService.login(loginDTO));
    }
}
