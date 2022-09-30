package pe.edu.galaxy.training.parqueaderov1.service.general.service;

import pe.edu.galaxy.training.parqueaderov1.dto.AuthorityDto;
import pe.edu.galaxy.training.parqueaderov1.dto.TipoVehiculoDto;
import pe.edu.galaxy.training.parqueaderov1.service.base.GenericService;

public interface AuthorityService extends GenericService<AuthorityDto> {
    AuthorityDto save(AuthorityDto authorityDto);
}
