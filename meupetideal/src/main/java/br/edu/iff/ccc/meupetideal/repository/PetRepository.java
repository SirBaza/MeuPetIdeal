package br.edu.iff.ccc.meupetideal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

import br.edu.iff.ccc.meupetideal.entities.Pet;
import br.edu.iff.ccc.meupetideal.entities.Tipo;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    Pet findByNome(String nome);

    List<Pet> findByTipo(Tipo tipo);

    @Query("SELECT p FROM Pet p WHERE p.ong.id = :ongId")
    List<Pet> findByOngId(Long ongId);

    @Query("SELECT p FROM Pet p " +
           "LEFT JOIN p.raca r " +
           "LEFT JOIN p.tipo t " +
           "WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(r.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(t.nome) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Pet> findByTermo(String termo);
}