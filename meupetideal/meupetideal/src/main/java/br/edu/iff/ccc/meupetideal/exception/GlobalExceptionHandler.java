package br.edu.iff.ccc.meupetideal.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    // ==================== PET NOT FOUND EXCEPTION ====================
    
    @ExceptionHandler(PetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handlePetNotFoundException(PetNotFoundException ex, HttpServletRequest request) {
        // Se a requisição é para API REST, retorna ProblemDetail
        if (request.getRequestURI().startsWith("/api/")) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, 
                ex.getMessage()
            );
            problemDetail.setTitle("Pet não encontrado");
            problemDetail.setProperty("petId", ex.getPetId());
            problemDetail.setProperty("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
        }
        
        // Se a requisição é para View, retorna ModelAndView
        ModelAndView modelAndView = new ModelAndView("error/petNaoEncontrado");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("petId", ex.getPetId());
        modelAndView.addObject("url", request.getRequestURL().toString());
        modelAndView.addObject("timestamp", LocalDateTime.now());
        return modelAndView;
    }

    // ==================== PET VALIDATION EXCEPTION ====================
    
    @ExceptionHandler(PetValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handlePetValidationException(PetValidationException ex, HttpServletRequest request) {
        // Se a requisição é para API REST, retorna ProblemDetail
        if (request.getRequestURI().startsWith("/api/")) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage()
            );
            problemDetail.setTitle("Erro de validação do Pet");
            problemDetail.setProperty("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
        }
        
        // Se a requisição é para View, retorna ModelAndView
        ModelAndView modelAndView = new ModelAndView("error/validacaoPet");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("url", request.getRequestURL().toString());
        modelAndView.addObject("timestamp", LocalDateTime.now());
        return modelAndView;
    }

    // ==================== ONG NOT FOUND EXCEPTION ====================
    
    @ExceptionHandler(OngNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleOngNotFoundException(OngNotFoundException ex, HttpServletRequest request) {
        // Se a requisição é para API REST, retorna ProblemDetail
        if (request.getRequestURI().startsWith("/api/")) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, 
                ex.getMessage()
            );
            problemDetail.setTitle("ONG não encontrada");
            problemDetail.setProperty("ongId", ex.getOngId());
            problemDetail.setProperty("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
        }
        
        // Se a requisição é para View, retorna ModelAndView
        ModelAndView modelAndView = new ModelAndView("error/ongNaoEncontrada");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("ongId", ex.getOngId());
        modelAndView.addObject("url", request.getRequestURL().toString());
        modelAndView.addObject("timestamp", LocalDateTime.now());
        return modelAndView;
    }

    // ==================== ONG VALIDATION EXCEPTION ====================
    
    @ExceptionHandler(OngValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleOngValidationException(OngValidationException ex, HttpServletRequest request) {
        // Se a requisição é para API REST, retorna ProblemDetail
        if (request.getRequestURI().startsWith("/api/")) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage()
            );
            problemDetail.setTitle("Erro de validação da ONG");
            problemDetail.setProperty("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
        }
        
        // Se a requisição é para View, retorna ModelAndView
        ModelAndView modelAndView = new ModelAndView("error/validacaoOng");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("url", request.getRequestURL().toString());
        modelAndView.addObject("timestamp", LocalDateTime.now());
        return modelAndView;
    }

    // ==================== EXCEÇÕES GERAIS (APENAS PARA API REST) ====================

    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        // Só trata se for requisição da API
        if (!request.getRequestURI().startsWith("/api/")) {
            throw ex; // Re-lança para tratamento padrão
        }
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "Erro interno do servidor"
        );
        problemDetail.setTitle("Erro Interno");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("originalMessage", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        // Só trata se for requisição da API
        if (!request.getRequestURI().startsWith("/api/")) {
            throw ex; // Re-lança para tratamento padrão
        }
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            ex.getMessage()
        );
        problemDetail.setTitle("Argumento inválido");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}