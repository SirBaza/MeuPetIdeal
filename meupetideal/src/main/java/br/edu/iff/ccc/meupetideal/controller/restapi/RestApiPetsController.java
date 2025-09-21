package br.edu.iff.ccc.meupetideal.controller.restapi;

import br.edu.iff.ccc.meupetideal.entities.Ong;
import br.edu.iff.ccc.meupetideal.entities.Pet;
import br.edu.iff.ccc.meupetideal.exception.PetNotFoundException;
import br.edu.iff.ccc.meupetideal.exception.PetValidationException;
import br.edu.iff.ccc.meupetideal.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.iff.ccc.meupetideal.service.OngService;
import br.edu.iff.ccc.meupetideal.service.RacaService;
import br.edu.iff.ccc.meupetideal.service.TipoService;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(path = "/api/v1")
public class RestApiPetsController {
    
    @Autowired
    private PetService petService;

    @Autowired
    private OngService ongService;

    @Autowired
    private TipoService tipoService;

    @Autowired
    private RacaService racaService;
    

    @GetMapping(path = "/pets")
    public ResponseEntity<List<Pet>> getAllPets() {
        
        List<Pet> pets = petService.listarPets();

        if (pets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pets);
    }

    @GetMapping(path = "/pets/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        Pet pet = petService.buscarPetPorId(id);

        if (pet == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pet);
    }

    @PostMapping(path = "/pets")
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet, @RequestParam("file") MultipartFile file) {
        try {
            Pet createdPet = petService.salvarPet(pet, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
        } catch (PetValidationException e) {
            return ResponseEntity.badRequest().build();
        } catch (java.io.IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(path = "/pets/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody Pet petDetails, @RequestParam("file") MultipartFile file) {
        try {
            Pet updatedPet = petService.atualizarPet(id, petDetails, file);
            return ResponseEntity.ok(updatedPet);
        } catch (PetNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (PetValidationException e) {
            return ResponseEntity.badRequest().build();
        } catch (java.io.IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(path = "/pets/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        try {
            petService.excluirPet(id);
            return ResponseEntity.noContent().build();
        } catch (PetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}



