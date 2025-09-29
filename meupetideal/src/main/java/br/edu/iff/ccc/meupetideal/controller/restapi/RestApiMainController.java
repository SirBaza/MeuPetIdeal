package br.edu.iff.ccc.meupetideal.controller.restapi;

import br.edu.iff.ccc.meupetideal.entities.Pet;
import br.edu.iff.ccc.meupetideal.exception.PetNotFoundException;
import br.edu.iff.ccc.meupetideal.exception.PetValidationException;
import br.edu.iff.ccc.meupetideal.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/api/v1")
public class RestApiMainController {
    
    @Autowired
    private PetService petService;
    
    @GetMapping()
    public ResponseEntity<String> getApiHome()
    {
        return ResponseEntity.ok("Em implementação");
    }


}

