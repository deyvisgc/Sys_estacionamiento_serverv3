package pe.edu.galaxy.training.parqueaderov1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.galaxy.training.parqueaderov1.entity.security.ConfiguracionEntity;

import java.util.Optional;

@Repository
public interface ConfiguracionRepository extends JpaRepository<ConfiguracionEntity, Long> {
    Optional<ConfiguracionEntity> findByTypeApi(String type);
}
