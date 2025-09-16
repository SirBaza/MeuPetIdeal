package br.edu.iff.ccc.meupetideal.service;

import br.edu.iff.ccc.meupetideal.entities.Raca;
import br.edu.iff.ccc.meupetideal.entities.Tipo;
import br.edu.iff.ccc.meupetideal.repository.RacaRepository;
import br.edu.iff.ccc.meupetideal.repository.TipoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RacaService {
    private final RacaRepository racaRepository;
    private final TipoRepository tipoRepository;

    public RacaService(RacaRepository racaRepository, TipoRepository tipoRepository) {
        this.racaRepository = racaRepository;
        this.tipoRepository = tipoRepository;
    }

    public List<Raca> listarRacas() {
        return racaRepository.findAll();
    }

    public List<Raca> listarPorTipo(Long tipoId) {
        return racaRepository.findByTipoId(tipoId);
    }

    @PostConstruct
    public void popularRacas() {
        if (racaRepository.count() == 0 && tipoRepository.count() > 0) {
            
            Tipo cachorro = tipoRepository.findById(1L).orElse(null);
            Tipo gato = tipoRepository.findById(2L).orElse(null);
            Tipo passaro = tipoRepository.findById(3L).orElse(null);
            Tipo roedor = tipoRepository.findById(4L).orElse(null);
            Tipo outro = tipoRepository.findById(5L).orElse(null);

            // CORREÇÃO: Salvar cada raça individualmente para garantir que todas sejam persistidas.
            if (cachorro != null) {
                racaRepository.save(new Raca("Vira-lata (SRD)", cachorro));
                racaRepository.save(new Raca("Bulldog", cachorro));
                racaRepository.save(new Raca("Poodle", cachorro));
                racaRepository.save(new Raca("Golden Retriever", cachorro));
            }

            if (gato != null) {
                racaRepository.save(new Raca("Siamês", gato));
                racaRepository.save(new Raca("Persa", gato));
            }

            if (passaro != null) {
                racaRepository.save(new Raca("Arara Azul", passaro));
            }

            if (roedor != null) {
                racaRepository.save(new Raca("Hamster", roedor));
            }

            if (outro != null) {
                racaRepository.save(new Raca("Outra", outro));
            }
        }
    }
}