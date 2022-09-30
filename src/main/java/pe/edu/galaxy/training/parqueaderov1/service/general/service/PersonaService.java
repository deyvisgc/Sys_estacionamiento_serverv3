package pe.edu.galaxy.training.parqueaderov1.service.general.service;

import pe.edu.galaxy.training.parqueaderov1.dto.PersonaDto;
import pe.edu.galaxy.training.parqueaderov1.entity.PersonaEntity;
import pe.edu.galaxy.training.parqueaderov1.service.base.GenericService;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;

import java.util.Optional;

public interface PersonaService extends GenericService<PersonaDto> {
    Optional<PersonaDto> findByNumeroDocumento(String numero) throws ServiceException;

}
