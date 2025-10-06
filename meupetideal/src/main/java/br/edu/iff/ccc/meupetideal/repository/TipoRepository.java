package br.edu.iff.ccc.meupetideal.repository;


import br.edu.iff.ccc.meupetideal.entities.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoRepository extends JpaRepository<Tipo, Long> {
	
}
