package br.edu.iff.ccc.meupetideal.repository;

import br.edu.iff.ccc.meupetideal.entities.Raca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RacaRepository extends JpaRepository<Raca, Long> {
    List<Raca> findByTipoId(Long tipoId);
    
}