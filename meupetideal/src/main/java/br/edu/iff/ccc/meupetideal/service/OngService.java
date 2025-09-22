package br.edu.iff.ccc.meupetideal.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.edu.iff.ccc.meupetideal.entities.Ong;
import br.edu.iff.ccc.meupetideal.exception.OngNotFoundException;
import br.edu.iff.ccc.meupetideal.exception.OngValidationException;
import br.edu.iff.ccc.meupetideal.repository.OngRepository;

@Service
public class OngService {

    private final OngRepository ongRepository;

    public OngService(OngRepository ongRepository) {
        this.ongRepository = ongRepository;
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
        if (!ongRepository.existsById(id)) {
            throw new OngNotFoundException(id);
        }
        ongRepository.deleteById(id);
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
}