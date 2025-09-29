package br.edu.iff.ccc.meupetideal.controller.restapi;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.iff.ccc.meupetideal.DTOs.PetRequestDTO;
import br.edu.iff.ccc.meupetideal.entities.Pet;
import br.edu.iff.ccc.meupetideal.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Pets", description = "API para gerenciamento de Pets")
public class ApiRestPetController {

    private final PetService petService;
    private final ObjectMapper objectMapper;

    public ApiRestPetController(PetService petService) {
        this.petService = petService;
        this.objectMapper = new ObjectMapper();
    }

    @Operation(summary = "Listar todos os pets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pets listados com sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Pet.class)) }),
            @ApiResponse(responseCode = "204", description = "Nenhum pet encontrado", content = @Content)
    })
    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> listarPets() {
        List<Pet> pets = petService.listarPets();
        return pets.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pets);
    }

    @Operation(summary = "Buscar pet por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet encontrado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Pet.class)) }),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado", content = @Content)
    })
    @GetMapping("/pets/{id}")
    public ResponseEntity<Pet> buscarPetPorId(@PathVariable Long id) {
        Pet pet = petService.buscarPetPorId(id);
        return ResponseEntity.ok(pet);
    }

    @Operation(summary = "Criar novo pet com foto", description = "Cria um pet usando DTO para dados estruturados + upload de foto obrigatório")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pet.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou foto não fornecida")
    })
    @PostMapping(value = "/pets", consumes = "multipart/form-data")
    public ResponseEntity<Pet> criarPet(
            @Parameter(description = "Dados do pet em JSON (string)", required = true) @RequestPart("pet") String petJson,

            @Parameter(description = "Arquivo de foto (JPG/PNG/JPEG)", required = true) @RequestPart("file") MultipartFile foto) {

        try {
            System.out.println("POST /pets - Criando pet com DTO...");
            System.out.println("JSON recebido: " + petJson);
            System.out.println("Arquivo: " + (foto != null ? foto.getOriginalFilename() : "nenhum"));

            // Converter JSON string para DTO
            PetRequestDTO petDTO = objectMapper.readValue(petJson, PetRequestDTO.class);
            System.out.println("DTO convertido: " + petDTO.toString());

            Pet petCriado = petService.criarPetComFoto(petDTO, foto);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(petCriado.getId())
                    .toUri();

            System.out.println("Pet criado com sucesso: " + petCriado.getNome() + " (ID: " + petCriado.getId() + ")");
            return ResponseEntity.created(location).body(petCriado);

        } catch (Exception e) {
            System.err.println("Erro ao criar pet: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar Pet: " + e.getMessage(), e);
        }
    }

    @Operation(summary = "Atualizar pet existente com DTO", description = "Atualiza um pet usando DTO para dados estruturados + foto opcional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pet.class))),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping(value = "/pets/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Pet> atualizarPet(
            @PathVariable Long id,

            @Parameter(description = "Dados do pet em JSON (string)", required = true) @RequestPart("pet") String petJson,

            @Parameter(description = "Nova foto (opcional - se não enviada, mantém a atual)") @RequestPart(value = "file", required = false) MultipartFile foto) {

        try {
            System.out.println("PUT /pets/" + id + " - Atualizando pet com DTO...");
            System.out.println("JSON recebido: " + petJson);
            System.out.println("Arquivo: " + (foto != null ? foto.getOriginalFilename() : "nenhuma alteração"));

            // Converter JSON string para DTO
            PetRequestDTO petDTO = objectMapper.readValue(petJson, PetRequestDTO.class);
            System.out.println("DTO convertido: " + petDTO.toString());

            Pet petAtualizado = petService.atualizarPetComDTO(id, petDTO, foto);

            System.out.println("Pet atualizado: " + petAtualizado.getNome());
            return ResponseEntity.ok(petAtualizado);

        } catch (Exception e) {
            System.err.println("Erro ao atualizar pet: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar Pet: " + e.getMessage(), e);
        }
    }

    // MÉTODO ALTERNATIVO: Atualizar apenas JSON (sem arquivo)
    @Operation(summary = "Atualizar pet (somente dados, sem foto)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet atualizado com sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Pet.class)) }),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PutMapping(value = "/pets/{id}/data", consumes = { "application/json" })
    public ResponseEntity<Pet> atualizarPetSemFoto(
            @PathVariable Long id,
            @RequestBody Pet pet) {

        try {
            System.out.println("PUT /pets/" + id + "/data - Atualizando dados do pet...");
            System.out.println("Pet recebido: " + pet.getNome());

            Pet petAtualizado = petService.atualizarPet(id, pet, null);

            System.out.println("Pet atualizado: " + petAtualizado.getNome());
            return ResponseEntity.ok(petAtualizado);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar pet: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar Pet", e);
        }
    }

    // ✨ MÉTODO ALTERNATIVO COM @RequestParam (Mais compatível com Swagger)
    @Operation(summary = "Criar novo pet com foto (método alternativo)", description = "Método alternativo usando @RequestParam - mais compatível com Swagger UI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pet.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou foto não fornecida")
    })
    @PostMapping(value = "/pets/alternative", consumes = "multipart/form-data")
    public ResponseEntity<Pet> criarPetAlternativo(
            @Parameter(description = "Dados do pet em JSON string", required = true) @RequestParam("pet") String petJson,

            @Parameter(description = "Arquivo de foto (JPG/PNG/JPEG)", required = true) @RequestParam("file") MultipartFile foto) {

        try {
            System.out.println("POST /pets/alternative - Criando pet (método alternativo)...");
            System.out.println("JSON recebido: " + petJson);
            System.out.println("Arquivo: " + (foto != null ? foto.getOriginalFilename() : "nenhum"));

            // Converter JSON string para DTO
            PetRequestDTO petDTO = objectMapper.readValue(petJson, PetRequestDTO.class);
            System.out.println("DTO convertido: " + petDTO.toString());

            Pet petCriado = petService.criarPetComFoto(petDTO, foto);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/../{id}")
                    .buildAndExpand(petCriado.getId())
                    .toUri();

            System.out.println("Pet criado com sucesso: " + petCriado.getNome() + " (ID: " + petCriado.getId() + ")");
            return ResponseEntity.created(location).body(petCriado);

        } catch (Exception e) {
            System.err.println("Erro ao criar pet: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar Pet: " + e.getMessage(), e);
        }
    }

    @Operation(summary = "Atualizar pet com DTO (método alternativo)", description = "Método alternativo usando @RequestParam - mais compatível com Swagger UI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pet.class))),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping(value = "/pets/{id}/dto", consumes = "multipart/form-data")
    public ResponseEntity<Pet> atualizarPetAlternativo(
            @PathVariable Long id,
            @RequestParam("pet") String petJson,
            @RequestParam(value = "file", required = false) MultipartFile foto) {

        try {
            System.out.println("PUT /pets/" + id + "/dto - Atualizando pet (método alternativo)...");
            System.out.println("JSON recebido: " + petJson);
            System.out.println("Arquivo: " + (foto != null ? foto.getOriginalFilename() : "nenhuma alteração"));

            // Converter JSON string para DTO
            PetRequestDTO petDTO = objectMapper.readValue(petJson, PetRequestDTO.class);
            System.out.println("DTO convertido: " + petDTO.toString());

            Pet petAtualizado = petService.atualizarPetComDTO(id, petDTO, foto);

            System.out.println("Pet atualizado: " + petAtualizado.getNome());
            return ResponseEntity.ok(petAtualizado);

        } catch (Exception e) {
            System.err.println("Erro ao atualizar pet: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar Pet: " + e.getMessage(), e);
        }
    }

    @Operation(summary = "Excluir pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado", content = @Content)
    })
    @DeleteMapping("/pets/{id}")
    public ResponseEntity<Void> excluirPet(@PathVariable Long id) {
        petService.excluirPet(id);
        return ResponseEntity.noContent().build();
    }
}