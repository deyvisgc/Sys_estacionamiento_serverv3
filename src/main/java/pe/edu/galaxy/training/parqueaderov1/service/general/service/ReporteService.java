package pe.edu.galaxy.training.parqueaderov1.service.general.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.edu.galaxy.training.parqueaderov1.criteria.FiltroCriterio;
import pe.edu.galaxy.training.parqueaderov1.entity.TarifaEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.VehiculoEntity;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;

import java.util.List;

public interface ReporteService {
    Page<VehiculoEntity> findByIngresosFechaRegistro(Pageable pageable, String mes) throws ServiceException;
    Page<VehiculoEntity> findByCriteriaVehiculo(FiltroCriterio criterio, Pageable pageable) throws ServiceException;
    Page<TarifaEntity>  findByTarifaFechaRegistro(Pageable pageable, String mes) throws ServiceException;
    List<Long>  findAllIngresos() throws ServiceException;
    List<Double>  findAllGanancias() throws ServiceException;
    Integer totalCliente() throws ServiceException;
    Integer totalUsers() throws ServiceException;
    Double totalGanancias() throws ServiceException;
    Integer nuevosIngresos() throws ServiceException;
    Integer totalComprobantes() throws ServiceException;
}
