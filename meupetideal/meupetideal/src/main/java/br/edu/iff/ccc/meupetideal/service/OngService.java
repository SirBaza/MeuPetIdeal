package br.edu.iff.ccc.meupetideal.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.edu.iff.ccc.meupetideal.DTOs.OngRequestDTO;
import br.edu.iff.ccc.meupetideal.entities.Ong;
import br.edu.iff.ccc.meupetideal.entities.Pet;
import br.edu.iff.ccc.meupetideal.exception.OngNotFoundException;
import br.edu.iff.ccc.meupetideal.exception.OngValidationException;
import br.edu.iff.ccc.meupetideal.repository.OngRepository;
import br.edu.iff.ccc.meupetideal.repository.PetRepository;
import jakarta.annotation.PostConstruct;

@Service
public class OngService {

    private final OngRepository ongRepository;
    private final PetRepository petRepository;
    // Usar caminho absoluto baseado no diretório do projeto
    private final String UPLOAD_DIR_ONGS = System.getProperty("user.dir") + "/src/main/resources/static/imgs/";
    private final String BASE_URL = "http://localhost:8080"; // Em produção, usar URL real

    public OngService(OngRepository ongRepository, PetRepository petRepository) {
        this.ongRepository = ongRepository;
        this.petRepository = petRepository;
    }

    @PostConstruct
    public void init() {
        // Debug: mostrar o diretório de upload
        System.out.println("OngService - Diretório de upload: " + UPLOAD_DIR_ONGS);
        System.out.println("OngService - Diretório atual: " + System.getProperty("user.dir"));
    }

    public Ong salvarOng(Ong ong) {
        validarOng(ong);
        return ongRepository.save(ong);
    }

    private void validarOng(Ong ong) {
        if (ong == null) {
            throw new OngValidationException("Dados da ONG não podem ser nulos.");
        }
        if (ong.getNome() == null || ong.getNome().trim().isEmpty()) {
            throw new OngValidationException("Nome da ONG é obrigatório.");
        }
        if (ong.getEndereco() == null || ong.getEndereco().trim().isEmpty()) {
            throw new OngValidationException("Endereço da ONG é obrigatório.");
        }
        if (ong.getFundacao() == null || ong.getFundacao().trim().isEmpty()) {
            throw new OngValidationException("Ano de fundação da ONG é obrigatório.");
        }
        if (ong.getAtuacao() == null || ong.getAtuacao().trim().isEmpty()) {
            throw new OngValidationException("Área de atuação da ONG é obrigatória.");
        }
    }

    public void excluirOng(Long id) {
        // Verificar se a ONG existe
        Ong ong = ongRepository.findById(id)
                .orElseThrow(() -> new OngNotFoundException(id));

        System.out.println("Iniciando exclusão da ONG ID: " + id + " (" + ong.getNome() + ")");

        // Buscar pets associados a esta ONG
        List<Pet> petsAssociados = petRepository.findByOngId(id);

        if (!petsAssociados.isEmpty()) {
            System.out.println("Encontrados " + petsAssociados.size() + " pets associados à ONG.");

            // OPÇÃO 1: Remover a associação (setar ONG como null)
            // Isso mantém os pets no banco mas sem ONG associada
            for (Pet pet : petsAssociados) {
                pet.setOng(null);
                petRepository.save(pet);
                System.out.println("Pet '" + pet.getNome() + "' desassociado da ONG");
            }

            // OPÇÃO 2: Se preferir excluir os pets junto (descomente as linhas abaixo)
            // petRepository.deleteAll(petsAssociados);
            // System.out.println(" 🗑️ " + petsAssociados.size() + " pets excluídos junto
            // com a ONG");
        }

        // Agora excluir a ONG (sem problemas de constraint)
        ongRepository.deleteById(id);
        System.out.println("ONG '" + ong.getNome() + "' excluída com sucesso!");
    }

    public Ong buscarOngPorId(Long id) {
        Optional<Ong> ong = ongRepository.findById(id);
        return ong.orElseThrow(() -> new OngNotFoundException(id));
    }

    // Método alternativo que retorna null (para casos onde não queremos exception)
    public Ong buscarOngPorIdOpcional(Long id) {
        Optional<Ong> ong = ongRepository.findById(id);
        return ong.orElse(null);
    }

    public List<Ong> listarOngs() {
        return ongRepository.findAll();
    }

    public List<Ong> buscarOngsFiltrados(String termo, String cidade, String atuacao) {
        List<Ong> ongs = ongRepository.findAll();

        return ongs.stream()
                .filter(ong -> termo == null || termo.isEmpty() ||
                        (ong.getNome() != null && ong.getNome().toLowerCase().contains(termo.toLowerCase())) ||
                        (ong.getEndereco() != null && ong.getEndereco().toLowerCase().contains(termo.toLowerCase())) ||
                        (ong.getAtuacao() != null && ong.getAtuacao().toLowerCase().contains(termo.toLowerCase())))
                .filter(ong -> cidade == null || cidade.isEmpty() ||
                        (ong.getEndereco() != null && ong.getEndereco().toLowerCase().contains(cidade.toLowerCase())))
                .filter(ong -> atuacao == null || atuacao.isEmpty() ||
                        (ong.getAtuacao() != null && ong.getAtuacao().toLowerCase().contains(atuacao.toLowerCase())))
                .collect(Collectors.toList());
    }

    /**
     * Salva imagem e retorna URL completa para acesso
     * 
     * @param file Arquivo de imagem
     * @return URL completa da imagem (ex: http://localhost:8080/imgs/ongs/foto.jpg)
     */

    public String salvarImagemERetornarURL(MultipartFile file) throws IOException {
        try {
            // Criar diretório se não existir
            Path uploadPath = Paths.get(UPLOAD_DIR_ONGS);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Diretório criado: " + uploadPath.toAbsolutePath());
            }

            // Validações
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("Arquivo não pode ser vazio");
            }

            if (!isValidImageType(file.getContentType())) {
                throw new IllegalArgumentException("Tipo de arquivo inválido. Apenas imagens são aceitas.");
            }

            if (file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("Arquivo muito grande. Máximo 10MB.");
            }

            // Gerar nome único
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String uniqueFilename = "ong_" + System.currentTimeMillis() + "_"
                    + UUID.randomUUID().toString().substring(0, 8) + fileExtension;

            // Salvar arquivo fisicamente
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Gerar URL completa
            String imageUrl = BASE_URL + "/imgs/" + uniqueFilename;

            System.out.println("Arquivo salvo em: " + filePath.toAbsolutePath());
            System.out.println("URL gerada: " + imageUrl);

            return imageUrl;

        } catch (IOException e) {
            System.err.println("ERRO ao salvar imagem: " + e.getMessage());
            throw new IOException("Erro ao salvar imagem: " + e.getMessage(), e);
        }
    }

    /**
     * Salva ONG com upload de imagem (converte para URL automaticamente)
     */
    public Ong salvarOngComUpload(Ong ong, MultipartFile file) throws IOException, OngValidationException {
        // Se há arquivo, processa upload e gera URL
        if (file != null && !file.isEmpty()) {
            String imageUrl = salvarImagemERetornarURL(file);
            ong.setImagem(imageUrl);
            System.out.println("Imagem processada - URL: " + imageUrl);
        }

        // Salva ONG com URL da imagem
        return salvarOng(ong);
    }

    /**
     * Cria uma ONG usando DTO + foto (método recomendado)
     * 
     * @param ongDTO - Dados da ONG em DTO (sem ID - será gerado automaticamente)
     * @param foto   - Arquivo de foto (obrigatório)
     * @return ONG criada e salva
     * @throws IOException - Se houver erro no upload da foto
     */
    public Ong criarOngComFoto(OngRequestDTO ongDTO, MultipartFile foto) throws IOException {
        System.out.println("Criando ONG com DTO: " + ongDTO.toString());

        // Validar se a foto foi enviada
        if (foto == null || foto.isEmpty()) {
            throw new IllegalArgumentException("A foto é obrigatória para cadastrar uma ONG!");
        }

        // Criar objeto ONG com os dados do DTO
        Ong ong = new Ong();
        ong.setNome(ongDTO.getNome());
        ong.setEndereco(ongDTO.getEndereco());
        ong.setFundacao(ongDTO.getFundacao());
        ong.setAtuacao(ongDTO.getAtuacao());
        ong.setTelefone(ongDTO.getTelefone());
        ong.setEmail(ongDTO.getEmail());
        ong.setSite(ongDTO.getSite());
        ong.setInstagram(ongDTO.getInstagram());
        ong.setDescricao(ongDTO.getDescricao());

        // Upload da foto
        String urlFoto = salvarImagemERetornarURL(foto);
        ong.setImagem(urlFoto);

        // Salvar e retornar
        Ong ongSalva = salvarOng(ong);
        System.out.println("ONG criada com sucesso: " + ongSalva.getNome() + " (ID: " + ongSalva.getId() + ")");

        return ongSalva;
    }

    /**
     * Atualiza uma ONG usando DTO + foto opcional
     * 
     * @param id     - ID da ONG a ser atualizada
     * @param ongDTO - Novos dados da ONG
     * @param foto   - Nova foto (opcional - se null, mantém a atual)
     * @return ONG atualizada
     * @throws IOException - Se houver erro no upload da foto
     */
    public Ong atualizarOngComDTO(Long id, OngRequestDTO ongDTO, MultipartFile foto) throws IOException {
        System.out.println("Atualizando ONG ID " + id + " com DTO: " + ongDTO.toString());

        // Buscar ONG existente
        Ong ongExistente = buscarOngPorId(id);

        // Atualizar campos básicos
        ongExistente.setNome(ongDTO.getNome());
        ongExistente.setEndereco(ongDTO.getEndereco());
        ongExistente.setFundacao(ongDTO.getFundacao());
        ongExistente.setAtuacao(ongDTO.getAtuacao());
        ongExistente.setTelefone(ongDTO.getTelefone());
        ongExistente.setEmail(ongDTO.getEmail());
        ongExistente.setSite(ongDTO.getSite());
        ongExistente.setInstagram(ongDTO.getInstagram());
        ongExistente.setDescricao(ongDTO.getDescricao());

        // Atualizar foto se fornecida
        if (foto != null && !foto.isEmpty()) {
            String urlFoto = salvarImagemERetornarURL(foto);
            ongExistente.setImagem(urlFoto);
        }

        // Salvar e retornar
        Ong ongAtualizada = salvarOng(ongExistente);
        System.out.println("ONG atualizada com sucesso: " + ongAtualizada.getNome());

        return ongAtualizada;
    }

    /**
     * Valida se o tipo de arquivo é uma imagem válida
     * 
     * @param contentType Tipo de conteúdo do arquivo
     * @return true se for uma imagem válida
     */
    private boolean isValidImageType(String contentType) {
        if (contentType == null) {
            return false;
        }
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/webp");
    }
}