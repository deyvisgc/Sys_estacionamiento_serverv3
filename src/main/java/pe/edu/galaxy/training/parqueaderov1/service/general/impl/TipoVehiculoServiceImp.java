package pe.edu.galaxy.training.parqueaderov1.service.general.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.galaxy.training.parqueaderov1.dto.TipoVehiculoDto;
import pe.edu.galaxy.training.parqueaderov1.entity.TipoVehiculoEntity;
import pe.edu.galaxy.training.parqueaderov1.mapper.TipoVehiculoMapper;
import pe.edu.galaxy.training.parqueaderov1.repository.TipoVehiculoRepository;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.TipoVehiculoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class TipoVehiculoServiceImp implements TipoVehiculoService {
    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository;
    @Autowired
    TipoVehiculoMapper vehiculoMapper;
    @Override
    public List<TipoVehiculoDto> findAll() throws ServiceException {
        List<TipoVehiculoEntity> list = tipoVehiculoRepository.findAll();
        return list.stream().map(l -> vehiculoMapper.toTipoVehiculoDto(l)).collect(Collectors.toList());
    }

    @Override
    public Optional<TipoVehiculoDto> findById(long id) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public List<TipoVehiculoDto> findByObject(TipoVehiculoDto tipoVehiculoDto) throws ServiceException {
        return null;
    }

    @Override
    public TipoVehiculoDto save(TipoVehiculoDto tipoVehiculoDto) throws ServiceException {
        return null;
    }

    @Override
    public TipoVehiculoDto update(TipoVehiculoDto tipoVehiculoDto) throws ServiceException {
        return null;
    }

    @Override
    public void deleteLogic(Long id) throws ServiceException {

    }
}
