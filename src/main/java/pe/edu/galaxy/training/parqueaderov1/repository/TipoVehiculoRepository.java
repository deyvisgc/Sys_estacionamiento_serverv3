package pe.edu.galaxy.training.parqueaderov1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.galaxy.training.parqueaderov1.entity.TipoVehiculoEntity;
@Repository
public interface TipoVehiculoRepository extends JpaRepository<TipoVehiculoEntity, Long> {
}
