package br.edu.iff.ccc.meupetideal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(PetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handlePetNotFoundException(PetNotFoundException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error/petNaoEncontrado");//caminho da view

        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("petId", ex.getPetId());
        modelAndView.addObject("url", request.getRequestURL().toString());
        modelAndView.addObject("timestamp", java.time.LocalDateTime.now());

        return modelAndView;
    }

    @ExceptionHandler(PetValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handlePetValidationException(PetValidationException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error/validacaoPet"); //caminho da view

        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("url", request.getRequestURL().toString());
        modelAndView.addObject("timestamp", java.time.LocalDateTime.now());

        return modelAndView;
    }
}
