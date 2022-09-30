package pe.edu.galaxy.training.parqueaderov1.service.general.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.edu.galaxy.training.parqueaderov1.criteria.FiltroCriterio;
import pe.edu.galaxy.training.parqueaderov1.dto.TarifaDto;
import pe.edu.galaxy.training.parqueaderov1.entity.TarifaEntity;
import pe.edu.galaxy.training.parqueaderov1.service.base.GenericService;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;

public interface TarifaService extends GenericService<TarifaDto> {
    Page<TarifaEntity> findByCriteriaTarifa(FiltroCriterio criterio, Pageable pageable) throws ServiceException;
}
