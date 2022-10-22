package pe.edu.galaxy.training.parqueaderov1.service.general.service.ps;

import pe.edu.galaxy.training.parqueaderov1.dto.UsuarioDto;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;

public interface ServicePS {
    void updateusersusp(UsuarioDto usuarioDto) throws ServiceException;
}
