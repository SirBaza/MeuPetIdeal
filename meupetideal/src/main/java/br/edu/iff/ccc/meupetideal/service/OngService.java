package br.edu.iff.ccc.meupetideal.service;

import br.edu.iff.ccc.meupetideal.entities.Ong;
import br.edu.iff.ccc.meupetideal.repository.OngRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OngService {

    private final OngRepository ongRepository;

    public OngService(OngRepository ongRepository) {
        this.ongRepository = ongRepository;
    }

    public Ong salvarOng(Ong ong) {
        return ongRepository.save(ong); 
    }

    public void excluirOng(Long id) {
        ongRepository.deleteById(id);
    }

    public Ong buscarOngPorId(Long id) {
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