package pe.edu.galaxy.training.parqueaderov1.service.general.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.galaxy.training.parqueaderov1.dto.AuthorityDto;
import pe.edu.galaxy.training.parqueaderov1.entity.security.AuthorityEntity;
import pe.edu.galaxy.training.parqueaderov1.mapper.AuthorityMapper;
import pe.edu.galaxy.training.parqueaderov1.repository.AuthorityRepository;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.AuthorityService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class AutorithyServiceImpl implements AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private AuthorityMapper authorityMapper;
    @Override
    public List<AuthorityDto> findAll() throws ServiceException {
        try {
            List<AuthorityEntity> list = authorityRepository.findAll();
            return list.stream().map(l -> authorityMapper.toAuthorityDto(l)).collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }

    }
    @Override
    public Optional<AuthorityDto> findById(long id) throws ServiceException {
        try {
            return authorityRepository.findById(id).map( v -> authorityMapper.toAuthorityDto(v));
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<AuthorityDto> findByObject(AuthorityDto authorityDto) throws ServiceException {
        return null;
    }

    @Override
    public AuthorityDto save(AuthorityDto authorityDto) throws ServiceException {
        try {
            return authorityMapper.toAuthorityDto(authorityRepository.save(authorityMapper.toAuthorityEntity(authorityDto)));
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public AuthorityDto update(AuthorityDto authorityDto) throws ServiceException {
        return null;
    }

    @Override
    public void deleteLogic(Long id) throws ServiceException {

    }
}
