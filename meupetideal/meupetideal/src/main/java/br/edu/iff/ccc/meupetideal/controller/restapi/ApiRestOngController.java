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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.iff.ccc.meupetideal.DTOs.OngRequestDTO;
import br.edu.iff.ccc.meupetideal.entities.Ong;
import br.edu.iff.ccc.meupetideal.service.OngService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "ONGs", description = "API para gerenciamento de ONGs")
public class ApiRestOngController {

    private final OngService ongService;
    private final ObjectMapper objectMapper;

    public ApiRestOngController(OngService ongService) {
        this.ongService = ongService;
        this.objectMapper = new ObjectMapper();
    }

    @Operation(summary = "Listar todas as ONGs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ONGs listadas com sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Ong.class)) }),
            @ApiResponse(responseCode = "204", description = "Nenhuma ONG encontrada", content = @Content)
    })
    @GetMapping("/ongs")
    public ResponseEntity<List<Ong>> listarOngs() {
        List<Ong> ongs = ongService.listarOngs();
        return ongs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(ongs);
    }

    @Operation(summary = "Buscar ONG por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ONG encontrada", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Ong.class)) }),
            @ApiResponse(responseCode = "404", description = "ONG não encontrada", content = @Content)
    })
    @GetMapping("/ongs/{id}")
    public ResponseEntity<Ong> buscarOngPorId(@PathVariable Long id) {
        Ong ong = ongService.buscarOngPorId(id);
        return ResponseEntity.ok(ong);
    }

    @Operation(summary = "Buscar ONGs com filtros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ONGs encontradas", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Ong.class)) }),
            @ApiResponse(responseCode = "204", description = "Nenhuma ONG encontrada", content = @Content)
    })
    @GetMapping("/ongs/search")
    public ResponseEntity<List<Ong>> buscarOngsComFiltro(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String atuacao) {
        List<Ong> ongs = ongService.buscarOngsFiltrados(termo, cidade, atuacao);
        return ongs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(ongs);
    }

    @Operation(summary = "Criar nova ONG com upload de imagem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "ONG criada com sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Ong.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping(value = "/ongs", consumes = { "multipart/form-data" })
    public ResponseEntity<Ong> criarOng(
            @RequestParam String nome,
            @RequestParam String endereco,
            @RequestParam String fundacao,
            @RequestParam String atuacao,
            @RequestParam String telefone,
            @RequestParam String email,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String site,
            @RequestParam(required = false) String instagram,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            // Criar objeto ONG
            Ong ong = new Ong();
            ong.setNome(nome);
            ong.setEndereco(endereco);
            ong.setFundacao(fundacao);
            ong.setAtuacao(atuacao);
            ong.setTelefone(telefone);
            ong.setEmail(email);
            ong.setDescricao(descricao);
            ong.setSite(site);
            ong.setInstagram(instagram);

            Ong ongSalva = ongService.salvarOngComUpload(ong, file);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(ongSalva.getId())
                    .toUri();

            return ResponseEntity.created(location).body(ongSalva);
        } catch (Exception e) {
            // As exceptions específicas são tratadas pelo GlobalExceptionHandler
            throw new RuntimeException("Erro ao criar ONG", e);
        }
    }

    @Operation(summary = "Atualizar ONG existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ONG atualizada com sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Ong.class)) }),
            @ApiResponse(responseCode = "404", description = "ONG não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PutMapping("/ongs/{id}")
    public ResponseEntity<Ong> atualizarOng(@PathVariable Long id, @RequestBody Ong ong) {
        ong.setId(id);
        Ong ongAtualizada = ongService.salvarOng(ong);
        return ResponseEntity.ok(ongAtualizada);
    }

    // ✨ NOVOS MÉTODOS COM DTO (Recomendados)
    @Operation(summary = "Criar nova ONG com DTO + foto", description = "Método recomendado usando DTO estruturado + upload de foto obrigatório")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "ONG criada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ong.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou foto não fornecida")
    })
    @PostMapping(value = "/ongs/dto", consumes = "multipart/form-data")
    public ResponseEntity<Ong> criarOngComDTO(
            @RequestParam("ong") String ongJson,
            @RequestParam("file") MultipartFile foto) {

        try {
            System.out.println("POST /ongs/dto - Criando ONG com DTO...");
            System.out.println("JSON recebido: " + ongJson);
            System.out.println("Arquivo: " + (foto != null ? foto.getOriginalFilename() : "nenhum"));

            // Converter JSON string para DTO
            OngRequestDTO ongDTO = objectMapper.readValue(ongJson, OngRequestDTO.class);
            System.out.println("DTO convertido: " + ongDTO.toString());

            Ong ongCriada = ongService.criarOngComFoto(ongDTO, foto);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/../{id}")
                    .buildAndExpand(ongCriada.getId())
                    .toUri();

            System.out.println("ONG criada com sucesso: " + ongCriada.getNome() + " (ID: " + ongCriada.getId() + ")");
            return ResponseEntity.created(location).body(ongCriada);

        } catch (Exception e) {
            System.err.println("Erro ao criar ONG: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar ONG: " + e.getMessage(), e);
        }
    }

    @Operation(summary = "Atualizar ONG existente com DTO", description = "Atualiza uma ONG usando DTO estruturado + foto opcional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ONG atualizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ong.class))),
            @ApiResponse(responseCode = "404", description = "ONG não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping(value = "/ongs/{id}/dto", consumes = "multipart/form-data")
    public ResponseEntity<Ong> atualizarOngComDTO(
            @PathVariable Long id,
            @RequestParam("ong") String ongJson,
            @RequestParam(value = "file", required = false) MultipartFile foto) {

        try {
            System.out.println("PUT /ongs/" + id + "/dto - Atualizando ONG com DTO...");
            System.out.println("JSON recebido: " + ongJson);
            System.out.println("Arquivo: " + (foto != null ? foto.getOriginalFilename() : "nenhuma alteração"));

            // Converter JSON string para DTO
            OngRequestDTO ongDTO = objectMapper.readValue(ongJson, OngRequestDTO.class);
            System.out.println("DTO convertido: " + ongDTO.toString());

            Ong ongAtualizada = ongService.atualizarOngComDTO(id, ongDTO, foto);

            System.out.println("ONG atualizada: " + ongAtualizada.getNome());
            return ResponseEntity.ok(ongAtualizada);

        } catch (Exception e) {
            System.err.println("Erro ao atualizar ONG: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar ONG: " + e.getMessage(), e);
        }
    }

    @Operation(summary = "Excluir ONG")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "ONG excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "ONG não encontrada", content = @Content)
    })
    @DeleteMapping("/ongs/{id}")
    public ResponseEntity<Void> excluirOng(@PathVariable Long id) {
        ongService.excluirOng(id);
        return ResponseEntity.noContent().build();
    }
}