package pe.edu.galaxy.training.parqueaderov1.service.general.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.edu.galaxy.training.parqueaderov1.dto.TipoVehiculoDto;
import pe.edu.galaxy.training.parqueaderov1.entity.security.ConfiguracionEntity;
import pe.edu.galaxy.training.parqueaderov1.service.base.GenericService;

import java.util.Optional;

public interface ConfiguracionService extends GenericService<ConfiguracionEntity> {
    Optional<ConfiguracionEntity> findByTypeApi(String type);
}
