package pe.edu.galaxy.training.parqueaderov1.service.general.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.galaxy.training.parqueaderov1.dto.PersonaDto;
import pe.edu.galaxy.training.parqueaderov1.entity.PersonaEntity;
import pe.edu.galaxy.training.parqueaderov1.mapper.PersonaMapper;
import pe.edu.galaxy.training.parqueaderov1.repository.PersonaRepository;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.PersonaService;

import java.util.List;
import java.util.Optional;
@Service
public class PersonaServiceImpl implements PersonaService {
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private PersonaMapper personaMapper;
    @Override
    public Optional<PersonaDto> findById(long id) throws ServiceException {
        try{
            return personaRepository.findById(id).map(per -> personaMapper.toPersonaDto(per));
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<PersonaDto> findByNumeroDocumento(String numero) throws ServiceException {
        try{
            Optional<PersonaEntity> personaEntity = personaRepository.findByNumeroDocumento(numero);
            if(personaEntity.isPresent()) {
                return Optional.of(personaMapper.toPersonaDto(personaEntity.get()));
            }
            return null;
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public List<PersonaDto> findByObject(PersonaDto personaDto) throws ServiceException {
        return null;
    }
    @Override
    public List<PersonaDto> findAll() throws ServiceException{
        return null;
    }
    @Override
    public PersonaDto save(PersonaDto personaDto) throws ServiceException {
        return null;
    }

    @Override
    public PersonaDto update(PersonaDto personaDto) throws ServiceException {
        return null;
    }
    @Override
    public void deleteLogic(Long id) throws ServiceException {
    }

}
