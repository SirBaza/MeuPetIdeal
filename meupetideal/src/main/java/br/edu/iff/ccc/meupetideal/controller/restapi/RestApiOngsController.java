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

import br.edu.iff.ccc.meupetideal.entities.Ong;
import br.edu.iff.ccc.meupetideal.exception.OngNotFoundException;
import br.edu.iff.ccc.meupetideal.exception.OngValidationException;
import br.edu.iff.ccc.meupetideal.service.OngService;

@RestController
@RequestMapping(path = "/api/v1")
public class RestApiOngsController {

    @Autowired
    private OngService ongService;

    @GetMapping(path = "/ongs")
    public ResponseEntity<List<Ong>> getAllOngs() {
        List<Ong> ongs = ongService.listarOngs();

        if (ongs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ongs);
    }

    @GetMapping(path = "/ongs/{id}")
    public ResponseEntity<Ong> getOngById(@PathVariable Long id) {
        try {
            Ong ong = ongService.buscarOngPorId(id);
            return ResponseEntity.ok(ong);
        } catch (OngNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/ongs/search")
    public ResponseEntity<List<Ong>> searchOngs(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String atuacao) {
        
        List<Ong> ongs = ongService.buscarOngsFiltrados(termo, cidade, atuacao);
        
        if (ongs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(ongs);
    }

    // Endpoint para criar ONG apenas com JSON (sem foto)
    @PostMapping(path = "/ongs/json-only", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ong> createOngJsonOnly(@RequestBody Ong ong) {
        try {
            System.out.println("POST /ongs/json-only - Criando ONG sem foto...");
            System.out.println("ONG recebida - Nome: " + (ong != null ? ong.getNome() : "null"));
            
            if (ong == null) {
                System.out.println("ERRO: ong é null!");
                return ResponseEntity.badRequest().build();
            }
            
            Ong createdOng = ongService.salvarOng(ong);
            System.out.println("ONG criada com sucesso: " + createdOng.getNome() + " (ID: " + createdOng.getId() + ")");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOng);
        } catch (OngValidationException e) {
            System.out.println("ERRO de validação: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("ERRO interno: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint principal para criar ONG com JSON + arquivo opcional
    @PostMapping(path = "/ongs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ong> createOng(
            @RequestParam("ong") String ongJson, 
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            System.out.println("POST /ongs - Criando ONG...");
            
            // Converter JSON string para Ong object
            ObjectMapper mapper = new ObjectMapper();
            Ong ong = mapper.readValue(ongJson, Ong.class);
            
            System.out.println("ONG recebida - Nome: " + ong.getNome());
            if (file != null && !file.isEmpty()) {
                System.out.println("Arquivo recebido: " + file.getOriginalFilename());
                // Aqui você pode implementar o salvamento da imagem
                // ong.setImagem(salvarImagem(file));
            } else {
                System.out.println("Nenhum arquivo enviado (opcional)");
            }
            
            Ong createdOng = ongService.salvarOng(ong);
            System.out.println("ONG criada com sucesso: " + createdOng.getNome() + " (ID: " + createdOng.getId() + ")");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOng);
        } catch (OngValidationException e) {
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

    @PutMapping(path = "/ongs/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ong> updateOng(@PathVariable Long id, @RequestBody Ong ongDetails) {
        try {
            System.out.println("PUT /ongs/" + id + " - Iniciando...");
            System.out.println("ONG recebida - Nome: " + (ongDetails != null ? ongDetails.getNome() : "null"));
            
            if (ongDetails == null) {
                System.out.println("ERRO: ongDetails é null!");
                return ResponseEntity.badRequest().build();
            }
            
            // Verifica se a ONG existe
            ongService.buscarOngPorId(id); // Lança exception se não existir
            
            // Define o ID correto
            ongDetails.setId(id);
            
            Ong updatedOng = ongService.salvarOng(ongDetails);
            System.out.println("ONG atualizada com sucesso: " + updatedOng.getNome());
            return ResponseEntity.ok(updatedOng);
        } catch (OngNotFoundException e) {
            System.out.println("ERRO: ONG não encontrada - " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (OngValidationException e) {
            System.out.println("ERRO: Validação - " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("ERRO: Exceção geral - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(path = "/ongs/{id}")
    public ResponseEntity<Void> deleteOng(@PathVariable Long id) {
        try {
            System.out.println("DELETE /ongs/" + id + " - Iniciando...");
            ongService.excluirOng(id);
            System.out.println("ONG excluída com sucesso: ID " + id);
            return ResponseEntity.noContent().build();
        } catch (OngNotFoundException e) {
            System.out.println("ERRO: ONG não encontrada - " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println("ERRO: Exceção geral ao excluir - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ENDPOINT DE TESTE PARA VERIFICAR SE O CONTROLLER ESTÁ FUNCIONANDO
    @GetMapping(path = "/ongs/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Controller ONGs funcionando! Timestamp: " + System.currentTimeMillis());
    }
}