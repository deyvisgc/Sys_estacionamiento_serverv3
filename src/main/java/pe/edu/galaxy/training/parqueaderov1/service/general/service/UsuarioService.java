package pe.edu.galaxy.training.parqueaderov1.service.general.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.edu.galaxy.training.parqueaderov1.dto.AuthorityDto;
import pe.edu.galaxy.training.parqueaderov1.dto.UsuarioDto;
import pe.edu.galaxy.training.parqueaderov1.entity.security.AuthorityEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.security.UsuarioEntity;
import pe.edu.galaxy.training.parqueaderov1.service.base.GenericService;

import java.util.List;

public interface UsuarioService extends GenericService<UsuarioDto> {
    UsuarioEntity existsByUsuario(String username);
    List<AuthorityEntity> findByAuthorities(Long id);
    Page<UsuarioDto> page(Pageable pageable);
}
