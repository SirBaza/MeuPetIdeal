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

import br.edu.iff.ccc.meupetideal.DTOs.PetRequestDTO;
import br.edu.iff.ccc.meupetideal.entities.Pet;
import br.edu.iff.ccc.meupetideal.entities.Tipo;
import br.edu.iff.ccc.meupetideal.entities.Ong;
import br.edu.iff.ccc.meupetideal.entities.Raca;
import br.edu.iff.ccc.meupetideal.exception.PetNotFoundException;
import br.edu.iff.ccc.meupetideal.repository.PetRepository;
import br.edu.iff.ccc.meupetideal.repository.TipoRepository;
import br.edu.iff.ccc.meupetideal.repository.OngRepository;
import br.edu.iff.ccc.meupetideal.repository.RacaRepository;
import jakarta.annotation.PostConstruct;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final TipoRepository tipoRepository;
    private final OngRepository ongRepository;
    private final RacaRepository racaRepository;
    // Usar caminho absoluto baseado no diretório do projeto
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/imgs/";
    private final String BASE_URL = "http://localhost:8080"; // Em produção, usar URL real

    public PetService(PetRepository petRepository, TipoRepository tipoRepository,
            OngRepository ongRepository, RacaRepository racaRepository) {
        this.petRepository = petRepository;
        this.tipoRepository = tipoRepository;
        this.ongRepository = ongRepository;
        this.racaRepository = racaRepository;
    }

    @PostConstruct
    public void init() {
        // Debug: mostrar o diretório de upload
        System.out.println("PetService - Diretório de upload: " + UPLOAD_DIR);
        System.out.println("PetService - Diretório atual: " + System.getProperty("user.dir"));

        // Corrige URLs de imagens existentes na inicialização
        corrigirURLsPetsExistentes();
    }

    public Pet buscarPetPorId(Long id) {
        Optional<Pet> pet = petRepository.findById(id);
        if (pet.isEmpty()) {
            throw new PetNotFoundException("Pet não encontrado com ID: " + id);
        }
        return pet.get();
    }

    public List<Pet> listarPets() {
        return petRepository.findAll();
    }

    public List<Pet> buscarPetsFiltrados(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return petRepository.findAll();// retorna todos os pets
        }
        return petRepository.findByTermo(termo);// retorna pets filtrados
    }

    public List<Pet> filtrarPorTipo(String nomeTipo) {
        Optional<Tipo> tipo = tipoRepository.findAll().stream()
                .filter(t -> t.getNome().equalsIgnoreCase(nomeTipo))
                .findFirst();

        return tipo.map(petRepository::findByTipo).orElseGet(List::of);
    }

    // Método para salvar pet sem foto (usado pelo endpoint JSON)
    public Pet salvarPet(Pet pet) {
        // Se o pet já tem uma foto definida (como URL ou Base64), mantém
        // Se não tem foto, deixa como null
        if (pet.getFoto() == null || pet.getFoto().trim().isEmpty()) {
            pet.setFoto(null);
        }
        return petRepository.save(pet);
    }

    // Método para salvar pet com foto (usado pelo endpoint multipart)
    public Pet salvarPet(Pet pet, MultipartFile fotoArquivo) throws IOException {
        // Se não há arquivo, salva pet sem foto
        if (fotoArquivo == null || fotoArquivo.isEmpty()) {
            pet.setFoto(null);
            return petRepository.save(pet);
        }

        String contentType = fotoArquivo.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new PetNotFoundException("Formato de arquivo inválido. Use apenas JPG ou PNG.");
        }

        // Lógica de negócio para salvar o arquivo e o pet
        String caminhoFoto = salvarArquivoFoto(fotoArquivo);
        pet.setFoto(caminhoFoto);

        return petRepository.save(pet);
    }

    // função para atualizar um pet existente
    public Pet atualizarPet(Long id, Pet dadosNovos, MultipartFile foto) throws IOException {
        Pet petExistente = buscarPetPorId(id); // Agora lança exceção se não encontrar

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

        // Gerar URL completa como no OngService
        return BASE_URL + "/imgs/" + fileName;
    }

    /**
     * Cria um pet usando DTO + foto (método recomendado)
     * 
     * @param petDTO - Dados do pet em DTO (sem ID - será gerado automaticamente)
     * @param foto   - Arquivo de foto (obrigatório)
     * @return Pet criado e salvo
     * @throws IOException - Se houver erro no upload da foto
     */
    public Pet criarPetComFoto(PetRequestDTO petDTO, MultipartFile foto) throws IOException {
        System.out.println("Criando pet com DTO: " + petDTO.toString());

        // Validar se a foto foi enviada
        if (foto == null || foto.isEmpty()) {
            throw new IllegalArgumentException("A foto é obrigatória para cadastrar um pet!");
        }

        // Buscar entidades relacionadas pelos IDs
        Ong ong = ongRepository.findById(petDTO.getOngId())
                .orElseThrow(() -> new IllegalArgumentException("ONG não encontrada com ID: " + petDTO.getOngId()));

        Raca raca = racaRepository.findById(petDTO.getRacaId())
                .orElseThrow(() -> new IllegalArgumentException("Raça não encontrada com ID: " + petDTO.getRacaId()));

        Tipo tipo = tipoRepository.findById(petDTO.getTipoId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo não encontrado com ID: " + petDTO.getTipoId()));

        // Criar objeto Pet com os dados do DTO
        Pet pet = new Pet();
        pet.setNome(petDTO.getNome());
        pet.setDescricao(petDTO.getDescricao());
        pet.setIdade(petDTO.getIdade());
        pet.setSexo(petDTO.getSexo());
        pet.setOng(ong);
        pet.setRaca(raca);
        pet.setTipo(tipo);

        // Upload da foto
        String caminhoFoto = salvarArquivoFoto(foto);
        pet.setFoto(caminhoFoto);

        // Salvar e retornar
        Pet petSalvo = petRepository.save(pet);
        System.out.println("Pet criado com sucesso: " + petSalvo.getNome() + " (ID: " + petSalvo.getId() + ")");

        return petSalvo;
    }

    /**
     * Atualiza um pet usando DTO + foto opcional
     * 
     * @param id     - ID do pet a ser atualizado
     * @param petDTO - Novos dados do pet
     * @param foto   - Nova foto (opcional - se null, mantém a atual)
     * @return Pet atualizado
     * @throws IOException - Se houver erro no upload da foto
     */
    public Pet atualizarPetComDTO(Long id, PetRequestDTO petDTO, MultipartFile foto) throws IOException {
        System.out.println("Atualizando pet ID " + id + " com DTO: " + petDTO.toString());

        // Buscar pet existente
        Pet petExistente = buscarPetPorId(id);

        // Buscar entidades relacionadas pelos IDs (apenas se foram alteradas)
        if (!petExistente.getOng().getId().equals(petDTO.getOngId())) {
            Ong ong = ongRepository.findById(petDTO.getOngId())
                    .orElseThrow(() -> new IllegalArgumentException("ONG não encontrada com ID: " + petDTO.getOngId()));
            petExistente.setOng(ong);
        }

        if (!petExistente.getRaca().getId().equals(petDTO.getRacaId())) {
            Raca raca = racaRepository.findById(petDTO.getRacaId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Raça não encontrada com ID: " + petDTO.getRacaId()));
            petExistente.setRaca(raca);
        }

        if (!petExistente.getTipo().getId().equals(petDTO.getTipoId())) {
            Tipo tipo = tipoRepository.findById(petDTO.getTipoId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Tipo não encontrado com ID: " + petDTO.getTipoId()));
            petExistente.setTipo(tipo);
        }

        // Atualizar campos básicos
        petExistente.setNome(petDTO.getNome());
        petExistente.setDescricao(petDTO.getDescricao());
        petExistente.setIdade(petDTO.getIdade());
        petExistente.setSexo(petDTO.getSexo());

        // Atualizar foto se fornecida
        if (foto != null && !foto.isEmpty()) {
            String caminhoFoto = salvarArquivoFoto(foto);
            petExistente.setFoto(caminhoFoto);
        }

        // Salvar e retornar
        Pet petAtualizado = petRepository.save(petExistente);
        System.out.println("✅ Pet atualizado com sucesso: " + petAtualizado.getNome());

        return petAtualizado;
    }

    /**
     * Corrige URLs de imagens de Pets existentes no banco
     * Converte caminhos relativos para URLs completas
     */
    public void corrigirURLsPetsExistentes() {
        System.out.println("Corrigindo URLs de imagens de Pets existentes...");

        List<Pet> todosPets = petRepository.findAll();
        int corrigidas = 0;

        for (Pet pet : todosPets) {
            String fotoAtual = pet.getFoto();

            if (fotoAtual != null && !fotoAtual.startsWith("http")) {
                // Se a foto começa com "/imgs/" mas não é URL completa
                if (fotoAtual.startsWith("/imgs/")) {
                    String novaURL = BASE_URL + fotoAtual;
                    pet.setFoto(novaURL);
                    petRepository.save(pet);
                    corrigidas++;
                    System.out.println("Corrigida Pet '" + pet.getNome() + "': " + fotoAtual + " -> " + novaURL);
                }
            }
        }

        System.out.println("Total de Pets corrigidos: " + corrigidas);
    }
}