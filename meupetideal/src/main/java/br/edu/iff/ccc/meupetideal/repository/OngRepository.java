package br.edu.iff.ccc.meupetideal.repository;

import br.edu.iff.ccc.meupetideal.entities.Ong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OngRepository extends JpaRepository<Ong, Long> {

    @Query("SELECT o FROM Ong o WHERE " +
           "LOWER(o.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(o.endereco) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Ong> findByTermo(String termo);
}