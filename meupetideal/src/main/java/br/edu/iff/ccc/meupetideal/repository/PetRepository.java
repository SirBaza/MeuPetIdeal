package br.edu.iff.ccc.meupetideal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

import br.edu.iff.ccc.meupetideal.entities.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    Pet findByNome(String nome);

    List<Pet> findByTipo(String tipo);

    @Query("SELECT p FROM Pet p WHERE p.ong.id = :ongId")
    List<Pet> findByOngId(Long ongId);
}