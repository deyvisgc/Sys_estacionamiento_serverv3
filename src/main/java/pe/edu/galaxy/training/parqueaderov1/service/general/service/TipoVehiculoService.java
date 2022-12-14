package pe.edu.galaxy.training.parqueaderov1.service.general.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.edu.galaxy.training.parqueaderov1.dto.TipoVehiculoDto;
import pe.edu.galaxy.training.parqueaderov1.service.base.GenericService;

public interface TipoVehiculoService extends GenericService<TipoVehiculoDto> {
    Page<TipoVehiculoDto> page(Pageable pageable);
}
