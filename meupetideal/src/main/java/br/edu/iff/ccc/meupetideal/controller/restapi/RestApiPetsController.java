package br.edu.iff.ccc.meupetideal.controller.restapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.iff.ccc.meupetideal.entities.Pet;
import br.edu.iff.ccc.meupetideal.exception.PetNotFoundException;
import br.edu.iff.ccc.meupetideal.exception.PetValidationException;
import br.edu.iff.ccc.meupetideal.service.OngService;
import br.edu.iff.ccc.meupetideal.service.PetService;
import br.edu.iff.ccc.meupetideal.service.RacaService;
import br.edu.iff.ccc.meupetideal.service.TipoService;

//
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

    // ENDPOINT DE TESTE PARA VERIFICAR SE O CONTROLLER ESTÁ FUNCIONANDO
    @GetMapping(path = "/pets/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Controller funcionando! Timestamp: " + System.currentTimeMillis());
    }

    // PUT DE TESTE SIMPLES
    @PutMapping(path = "/pets/test/{id}")
    public ResponseEntity<String> testPutEndpoint(@PathVariable Long id, @RequestBody String message) {
        return ResponseEntity.ok("PUT funcionando para ID: " + id + " - Mensagem: " + message);
    }

    // TESTE ESPECÍFICO PARA DESERIALIZAÇÃO DE PET
    @PostMapping(path = "/pets/debug")
    public ResponseEntity<String> debugPetDeserialization(@RequestBody Pet pet) {
        try {
            return ResponseEntity.ok("Pet deserializado com sucesso! Nome: " + pet.getNome() + ", ID: " + pet.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro na deserialização: " + e.getMessage());
        }
    }

    @GetMapping(path = "/pets/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        Pet pet = petService.buscarPetPorId(id);

        if (pet == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pet);
    }

    // Endpoint principal para criar pet com JSON + arquivo opcional
    @PostMapping(path = "/pets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> createPet(
            @RequestParam("pet") String petJson, 
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            System.out.println("POST /pets - Criando pet...");
            
            // Converter JSON string para Pet object
            ObjectMapper mapper = new ObjectMapper();
            Pet pet = mapper.readValue(petJson, Pet.class);
            
            System.out.println("Pet recebido - Nome: " + pet.getNome());
            if (file != null && !file.isEmpty()) {
                System.out.println("Arquivo recebido: " + file.getOriginalFilename());
            } else {
                System.out.println("Nenhum arquivo enviado (opcional)");
            }
            
            Pet createdPet = petService.salvarPet(pet, file);
            System.out.println("Pet criado com sucesso: " + createdPet.getNome() + " (ID: " + createdPet.getId() + ")");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
        } catch (PetValidationException e) {
            System.out.println("ERRO de validação: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (java.io.IOException e) {
            System.out.println("ERRO de I/O: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            System.out.println("ERRO interno: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint alternativo apenas para JSON (sem foto)
    @PostMapping(path = "/pets/json-only", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> createPetJsonOnly(@RequestBody Pet pet) {
        try {
            System.out.println("POST /pets/json-only - Criando pet sem foto...");
            System.out.println("Pet recebido - Nome: " + (pet != null ? pet.getNome() : "null"));
            
            if (pet == null) {
                System.out.println("ERRO: pet é null!");
                return ResponseEntity.badRequest().build();
            }
            
            Pet createdPet = petService.salvarPet(pet); // Método sem arquivo
            System.out.println("Pet criado com sucesso: " + createdPet.getNome() + " (ID: " + createdPet.getId() + ")");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
        } catch (PetValidationException e) {
            System.out.println("ERRO de validação: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("ERRO interno: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint para criar pet com form-data (incluindo foto)
    @PostMapping(path = "/pets/with-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> createPetWithPhoto(@RequestParam("pet") String petJson, @RequestParam("file") MultipartFile file) {
        try {
            System.out.println("POST /pets/with-photo - Criando pet com foto...");
            
            // Converter JSON string para Pet object
            ObjectMapper mapper = new ObjectMapper();
            Pet pet = mapper.readValue(petJson, Pet.class);
            
            System.out.println("Pet recebido - Nome: " + pet.getNome());
            System.out.println("Arquivo recebido: " + file.getOriginalFilename());
            
            Pet createdPet = petService.salvarPet(pet, file);
            System.out.println("Pet criado com sucesso: " + createdPet.getNome() + " (ID: " + createdPet.getId() + ")");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
        } catch (PetValidationException e) {
            System.out.println("ERRO de validação: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (java.io.IOException e) {
            System.out.println("ERRO de I/O: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            System.out.println("ERRO interno: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(path = "/pets/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody Pet petDetails) {
        try {
            System.out.println("PUT /pets/" + id + " - Iniciando...");
            System.out.println("Pet recebido - Nome: " + (petDetails != null ? petDetails.getNome() : "null"));
            System.out.println("Pet recebido - ID: " + (petDetails != null ? petDetails.getId() : "null"));
            
            if (petDetails == null) {
                System.out.println("ERRO: petDetails é null!");
                return ResponseEntity.badRequest().build();
            }
            
            Pet updatedPet = petService.atualizarPet(id, petDetails, null); // Sem arquivo
            System.out.println("Pet atualizado com sucesso: " + updatedPet.getNome());
            return ResponseEntity.ok(updatedPet);
        } catch (PetNotFoundException e) {
            System.out.println("ERRO: Pet não encontrado - " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (PetValidationException e) {
            System.out.println("ERRO: Validação - " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("ERRO: Exceção geral - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(path = "/pets/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Pet> updatePetPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            Pet existingPet = petService.buscarPetPorId(id);
            Pet updatedPet = petService.atualizarPet(id, existingPet, file);
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

    @GetMapping(path = "/pets/search")
    public ResponseEntity<List<Pet>> searchPets(@RequestParam(required = false) String busca) {
        try {
            List<Pet> pets = petService.buscarPetsFiltrados(busca);
            if (pets.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(pets);
        } catch (Exception e) {
            System.out.println("ERRO ao buscar pets: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint para filtrar pets por tipo
    @GetMapping(path = "/pets/tipo/{tipo}")
    public ResponseEntity<List<Pet>> getPetsByTipo(@PathVariable String tipo) {
        try {
            List<Pet> petsFiltrados = petService.filtrarPorTipo(tipo);
            if (petsFiltrados.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(petsFiltrados);
        } catch (Exception e) {
            System.out.println("ERRO ao filtrar pets por tipo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}



