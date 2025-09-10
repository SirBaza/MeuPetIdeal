package br.edu.iff.ccc.meupetideal.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.edu.iff.ccc.meupetideal.entities.Pet;
import br.edu.iff.ccc.meupetideal.repository.PetRepository;

@Service
public class PetService {

    private final ArrayList<Pet> pets = new ArrayList<>(); 
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet findByNome(String nome) {
        Pet pet = petRepository.findByNome(nome);
        return pet != null ? pet : null;
    }
    
   
    public Pet buscarPetPorId(Long id) {
        for (Pet pet : pets) {
            if (pet.getId().equals(id)) {
                return pet;
            }
        }
        return null;
    }

    //método para atualizar um Pet
    public Pet atualizarPet(Long id, Pet novosDados, MultipartFile foto) throws IOException {
    for (Pet pet : pets) {
        if (pet.getId().equals(id)) {
            pet.setNome(novosDados.getNome());
            pet.setRaca(novosDados.getRaca());
            pet.setDescricao(novosDados.getDescricao());
            pet.setIdade(novosDados.getIdade());
            pet.setOng(novosDados.getOng());
            pet.setTipo(novosDados.getTipo());
            if (foto != null && !foto.isEmpty()) {
                String uploadDir = "src/main/resources/static/imgs/";
                String fileName = foto.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                pet.setFoto("/imgs/" + fileName);
            }
            return pet;
        }
    }
    return null;
}


    public ArrayList<Pet> buscarPetsFiltrados(String nomeOuRacaOuTipo, String ignorar, Integer idade) {
    ArrayList<Pet> resultado = new ArrayList<>();

    for (Pet pet : pets) {
        boolean match = false;

        if (nomeOuRacaOuTipo != null && !nomeOuRacaOuTipo.isEmpty()) {
            String buscaLower = nomeOuRacaOuTipo.toLowerCase();
            if (pet.getNome().toLowerCase().contains(buscaLower)
                || pet.getRaca().toLowerCase().contains(buscaLower)
                || pet.getTipo().toLowerCase().contains(buscaLower)) {
                match = true;
            }
        } else {
            match = true; // se a busca estiver vazia, retorna todos
        }

        if (idade != null && pet.getIdade() != idade) {
            match = false;
        }

        if (match) {
            resultado.add(pet);
        }
    }

    return resultado;
}

public ArrayList<Pet> filtrarPorTipo(String tipo) {
    ArrayList<Pet> filtrados = new ArrayList<>();
    for (Pet pet : pets) {
        if (pet.getTipo().equalsIgnoreCase(tipo)) {
            filtrados.add(pet);
        }
    }
    return filtrados;
}


    //retorna todos os pets
    public ArrayList<Pet> listarPets()
    {
        return pets;
    }

    
    public Pet cadastrarPetComFoto(Pet pet, MultipartFile foto) throws IOException {
        // Define diretório de upload
        String uploadDir = "src/main/resources/static/imgs";
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();

        // Cria o diretório se ele não existir
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = foto.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Salva a imagem fisicamente
        Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Salva apenas o caminho relativo no pet
        pet.setFoto("/imgs/" + fileName);

        // Gera ID e adiciona à lista fake
        pet.setId(idGenerator.getAndIncrement());
        pets.add(pet);

        return pet;
        }

       public boolean excluirPet(Long id) {
        return pets.removeIf(p -> p.getId().equals(id));
        }


}