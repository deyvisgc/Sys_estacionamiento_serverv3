package pe.edu.galaxy.training.parqueaderov1.service.general.impl;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.edu.galaxy.training.parqueaderov1.dto.AuthorityDto;
import pe.edu.galaxy.training.parqueaderov1.dto.UsuarioDto;
import pe.edu.galaxy.training.parqueaderov1.entity.PersonaEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.security.AuthorityEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.security.UsuarioEntity;
import pe.edu.galaxy.training.parqueaderov1.mapper.AuthorityMapper;
import pe.edu.galaxy.training.parqueaderov1.mapper.PersonaMapper;
import pe.edu.galaxy.training.parqueaderov1.mapper.UsuarioMapper;
import pe.edu.galaxy.training.parqueaderov1.repository.AuthorityRepository;
import pe.edu.galaxy.training.parqueaderov1.repository.PersonaRepository;
import pe.edu.galaxy.training.parqueaderov1.repository.UsuarioRepository;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceSqlException;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.UsuarioService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.Objects.isNull;
@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
     private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioMapper usuarioMapper;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private PersonaMapper personaMapper;
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Override
    public List<UsuarioDto> findAll() throws ServiceException {
        try {
            List<UsuarioEntity> lsUsuario = usuarioRepository.findAllByEstado('1');
            return lsUsuario.stream().map(us -> usuarioMapper.toUsuarioDto(us)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<UsuarioDto> findById(long id) throws ServiceException {
        try {
            return usuarioRepository.findById(id).map(us -> usuarioMapper.toUsuarioDto(us));
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<UsuarioDto> findByObject(UsuarioDto usuarioDto) throws ServiceException {
        return null;
    }
    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public UsuarioDto save(UsuarioDto usuarioDto) throws ServiceException {
        try {
            PersonaEntity persona = personaRepository.save(personaMapper.toPersonaEntity(usuarioDto.getPerson()));
            if (!isNull(persona)) {
                UsuarioEntity usuarioEntity = usuarioMapper.toUsuarioEntity(usuarioDto);
                usuarioEntity.setPersona(persona);
                return usuarioMapper.toUsuarioDto(usuarioRepository.save(usuarioEntity));
            }
            return null;
        } catch (ConstraintViolationException e) {
            throw new ServiceSqlException(e.getMessage(), e.getSQLException(), e.getConstraintName());
        }
    }
    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public UsuarioDto update(UsuarioDto usuarioDto) throws ServiceException {
        try {
            Optional<UsuarioEntity> usuarioEntity = Optional.of(usuarioRepository.findById(usuarioDto.getId()).get());
           if (usuarioEntity.isPresent() && !usuarioEntity.isEmpty()) {

               usuarioDto.getPerson().setId(usuarioEntity.get().getPersona().getId()); // seteo el id de la persona

               PersonaEntity refPersona = personaMapper.toPersonaEntity(usuarioDto.getPerson());
               BeanUtils.copyProperties(usuarioDto.getPerson(), refPersona);
               personaRepository.save(refPersona);

               UsuarioEntity refUsuario = usuarioMapper.toUsuarioEntity(usuarioDto);
               Set<AuthorityEntity> roles = new HashSet<>();
               for (AuthorityDto authorityDto: usuarioDto.getRole()) {
                   roles.add(authorityRepository.findById(authorityDto.getId()).get());
               }
               UsuarioEntity usuarioUpdate = UsuarioEntity.builder()
                       .id(usuarioDto.getId())
                       .usuario(usuarioDto.getUser_name())
                       .persona(usuarioEntity.get().getPersona())
                       .authorities(roles)
                       .build();
               BeanUtils.copyProperties(usuarioUpdate , refUsuario);
               //refUsuario.setPersona(usuarioEntity.get().getPersona());

               return usuarioMapper.toUsuarioDto(usuarioRepository.save(refUsuario));
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteLogic(Long id) throws ServiceException {
        try {
           usuarioRepository.deleteLogic(id);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public UsuarioEntity existsByUsuario(String usario) {
        return this.usuarioRepository.findByUsuarioAndEstado(usario, '1');
    }

    @Override
    public List<AuthorityEntity> findByAuthorities(Long id) {
        return usuarioRepository.findByAuthorities(id);
    }

    @Override
    public Page<UsuarioDto> page(Pageable pageable) {
        return usuarioRepository.findAllByEstado(pageable, '1').map(us -> usuarioMapper.toUsuarioDto(us));
    }
}
