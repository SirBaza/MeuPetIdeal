package br.edu.iff.ccc.meupetideal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.iff.ccc.meupetideal.entities.Tipo;
import br.edu.iff.ccc.meupetideal.repository.TipoRepository;
import jakarta.annotation.PostConstruct;

@Service
public class TipoService {

    private final TipoRepository tipoRepository;

    public TipoService(TipoRepository tipoRepository) {
        this.tipoRepository = tipoRepository;
    }

    public List<Tipo> listarTipos() {
        return tipoRepository.findAll();
    }

    @PostConstruct
    public void popularTipos() {
        if (tipoRepository.count() == 0) {
            tipoRepository.save(new Tipo("Cachorro"));
            tipoRepository.save(new Tipo("Gato"));
            tipoRepository.save(new Tipo("Pássaro"));
            tipoRepository.save(new Tipo("Roedor"));
            tipoRepository.save(new Tipo("Outro"));
        }
    }
}
