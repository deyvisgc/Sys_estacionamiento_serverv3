package pe.edu.galaxy.training.parqueaderov1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.galaxy.training.parqueaderov1.entity.PersonaEntity;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<PersonaEntity, Long> {
    //JPA
    Optional<PersonaEntity> findByNumeroDocumento(String numero);
}
