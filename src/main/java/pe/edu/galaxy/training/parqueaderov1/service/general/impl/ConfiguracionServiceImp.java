package pe.edu.galaxy.training.parqueaderov1.service.general.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.galaxy.training.parqueaderov1.entity.security.ConfiguracionEntity;
import pe.edu.galaxy.training.parqueaderov1.repository.ConfiguracionRepository;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.ConfiguracionService;

import java.util.List;
import java.util.Optional;
@Service
public class ConfiguracionServiceImp implements ConfiguracionService {
    @Autowired
    ConfiguracionRepository repository;
    @Override
    public List<ConfiguracionEntity> findAll() throws ServiceException {
        return repository.findAll();
    }

    @Override
    public Optional<ConfiguracionEntity> findById(long id) throws ServiceException {
        return repository.findById(id);
    }

    @Override
    public List<ConfiguracionEntity> findByObject(ConfiguracionEntity configuracionEntity) throws ServiceException {
        return null;
    }

    @Override
    public ConfiguracionEntity save(ConfiguracionEntity configuracionEntity) throws ServiceException {
        return repository.save(configuracionEntity);
    }

    @Override
    public ConfiguracionEntity update(ConfiguracionEntity configuracionEntity) throws ServiceException {
        Optional<ConfiguracionEntity> config = repository.findById(configuracionEntity.getId());
        if (config.isPresent()) {
            ConfiguracionEntity entity = config.get();
            BeanUtils.copyProperties(configuracionEntity, entity);
           return repository.save(entity);
        }
        return null;
    }

    @Override
    public void deleteLogic(Long id) throws ServiceException {

    }

    @Override
    public Optional<ConfiguracionEntity> findByTypeApi(String type) {
        return repository.findByTypeApi(type);
    }
}
