package br.edu.iff.ccc.meupetideal.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.edu.iff.ccc.meupetideal.entities.Pet;
import br.edu.iff.ccc.meupetideal.entities.Tipo;
import br.edu.iff.ccc.meupetideal.exception.PetValidationException;
import br.edu.iff.ccc.meupetideal.repository.PetRepository;
import br.edu.iff.ccc.meupetideal.repository.TipoRepository;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final TipoRepository tipoRepository;
    private static final String UPLOAD_DIR = "src/main/resources/static/imgs/";

    public PetService(PetRepository petRepository, TipoRepository tipoRepository) {
        this.petRepository = petRepository;
        this.tipoRepository = tipoRepository;
    }

    public Pet buscarPetPorId(Long id) {
        Optional<Pet> pet = petRepository.findById(id);
        return pet.orElse(null);
    }

    public List<Pet> listarPets() {
        return petRepository.findAll();
    }

    public List<Pet> buscarPetsFiltrados(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return petRepository.findAll();//retorna todos os pets
        }
        return petRepository.findByTermo(termo);//retorna pets filtrados
    }

    public List<Pet> filtrarPorTipo(String nomeTipo) {
        Optional<Tipo> tipo = tipoRepository.findAll().stream()
            .filter(t -> t.getNome().equalsIgnoreCase(nomeTipo))
            .findFirst();
        
        return tipo.map(petRepository::findByTipo).orElseGet(List::of);
    }

    public Pet salvarPet(Pet pet, MultipartFile fotoArquivo) throws IOException {
        // 1. Validação do Arquivo de Imagem movida para o Service
        if (fotoArquivo == null || fotoArquivo.isEmpty()) {
            throw new PetValidationException("Por favor, envie uma foto do pet.");
        }

        String contentType = fotoArquivo.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new PetValidationException("Formato de arquivo inválido. Use apenas JPG ou PNG.");
        }

        // 2. Lógica de negócio para salvar o arquivo e o pet
        String caminhoFoto = salvarArquivoFoto(fotoArquivo);
        pet.setFoto(caminhoFoto);
        
        return petRepository.save(pet);
    }

    //função para atualizar um pet existente
    public Pet atualizarPet(Long id, Pet dadosNovos, MultipartFile foto) throws IOException {
        Pet petExistente = buscarPetPorId(id);
        if (petExistente == null) {
            throw new RuntimeException("Pet não encontrado com id: " + id);
        }

        petExistente.setNome(dadosNovos.getNome());
        petExistente.setRaca(dadosNovos.getRaca());
        petExistente.setDescricao(dadosNovos.getDescricao());
        petExistente.setIdade(dadosNovos.getIdade());
        petExistente.setOng(dadosNovos.getOng());
        petExistente.setTipo(dadosNovos.getTipo());
        petExistente.setSexo(dadosNovos.getSexo());

        if (foto != null && !foto.isEmpty()) {
            String caminhoFoto = salvarArquivoFoto(foto);
            petExistente.setFoto(caminhoFoto);
        }
        
        return petRepository.save(petExistente);
    }

    public void excluirPet(Long id) {
        if (!petRepository.existsById(id)) {
            throw new RuntimeException("Pet não encontrado com id: " + id);
        }
        petRepository.deleteById(id);
    }

    private String salvarArquivoFoto(MultipartFile foto) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = foto.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/imgs/" + fileName;
    }
}