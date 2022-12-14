package pe.edu.galaxy.training.parqueaderov1.service.general.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import pe.edu.galaxy.training.parqueaderov1.dto.AuthorityDto;
import pe.edu.galaxy.training.parqueaderov1.dto.PersonaDto;
import pe.edu.galaxy.training.parqueaderov1.dto.UsuarioDto;
import pe.edu.galaxy.training.parqueaderov1.entity.security.AuthorityEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.security.UsuarioEntity;
import pe.edu.galaxy.training.parqueaderov1.service.base.GenericService;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;

import java.util.List;

public interface UsuarioService extends GenericService<UsuarioDto> {
    UsuarioEntity existsByUsuario(String username)  throws ServiceException;
    List<AuthorityEntity> findByAuthorities(Long id)  throws ServiceException;
    Page<UsuarioDto> page(Pageable pageable)  throws ServiceException;
    UsuarioDto findByEmail(String correo)  throws ServiceException;
    UsuarioDto findByTokenPassword(String token)  throws ServiceException;
    void updateTokenPassword(String tokenPassword, long correo)  throws ServiceException;
    void changePassword(String password, long id) throws ServiceException;
}
