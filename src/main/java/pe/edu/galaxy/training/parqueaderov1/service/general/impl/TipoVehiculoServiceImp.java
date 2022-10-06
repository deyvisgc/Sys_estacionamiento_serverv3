package pe.edu.galaxy.training.parqueaderov1.service.general.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return tipoVehiculoRepository.findById(id).map(t -> vehiculoMapper.toTipoVehiculoDto(t));
    }

    @Override
    public List<TipoVehiculoDto> findByObject(TipoVehiculoDto tipoVehiculoDto) throws ServiceException {
        return null;
    }

    @Override
    public TipoVehiculoDto save(TipoVehiculoDto tipoVehiculoDto) throws ServiceException {
        return vehiculoMapper.toTipoVehiculoDto(tipoVehiculoRepository.save(vehiculoMapper.toTipoVehiculoEntity(tipoVehiculoDto)));
    }

    @Override
    public TipoVehiculoDto update(TipoVehiculoDto tipoVehiculoDto) throws ServiceException {
        Optional<TipoVehiculoEntity> tipoVehiculo = tipoVehiculoRepository.findById(tipoVehiculoDto.getId());
        if (tipoVehiculo.isPresent()) {
            TipoVehiculoEntity vehiculo = new TipoVehiculoEntity();
            vehiculo.setDescription(tipoVehiculoDto.getDescripcion());
            vehiculo.setPrice_hour(tipoVehiculoDto.getPrecioHora());
            vehiculo.setId(tipoVehiculoDto.getId());
            return vehiculoMapper.toTipoVehiculoDto(tipoVehiculoRepository.save(vehiculo));
        }
        return null;
    }

    @Override
    public void deleteLogic(Long id) throws ServiceException {
        try {
            tipoVehiculoRepository.deleteLogic(id);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
    @Override
    public Page<TipoVehiculoDto> page(Pageable pageable) {
        return  tipoVehiculoRepository.findAllByStatus(pageable, '1').map(t-> vehiculoMapper.toTipoVehiculoDto(t));
    }
}
