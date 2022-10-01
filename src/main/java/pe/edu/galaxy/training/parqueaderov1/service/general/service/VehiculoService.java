package pe.edu.galaxy.training.parqueaderov1.service.general.service;

import pe.edu.galaxy.training.parqueaderov1.dto.TarifaDto;
import pe.edu.galaxy.training.parqueaderov1.dto.VehiculoDto;
import pe.edu.galaxy.training.parqueaderov1.entity.VehiculoEntity;
import pe.edu.galaxy.training.parqueaderov1.service.base.GenericService;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public interface VehiculoService extends GenericService<VehiculoDto> {
    Optional<VehiculoDto> findByCodigoOperacionAndEstado(String codigo, char estado) throws ServiceException;
    Optional<VehiculoDto> findByCodigoOperacion(String codigo) throws ServiceException;

    boolean existsByCedula(String cedula) throws ServiceException;
    void ingreso(VehiculoDto vehiculoDto) throws ServiceException;
    TarifaDto salida(VehiculoDto vehiculoDto) throws ServiceException;
    VehiculoDto update(VehiculoDto vehiculoDto) throws ServiceException;
    void export(HttpServletResponse response, long idVehiculo) throws IOException;
}
