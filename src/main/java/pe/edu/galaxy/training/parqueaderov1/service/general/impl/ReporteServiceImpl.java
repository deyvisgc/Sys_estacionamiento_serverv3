package pe.edu.galaxy.training.parqueaderov1.service.general.impl;

import io.github.jhipster.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.galaxy.training.parqueaderov1.criteria.FiltroCriterio;
import pe.edu.galaxy.training.parqueaderov1.entity.*;
import pe.edu.galaxy.training.parqueaderov1.repository.TarifaRepository;
import pe.edu.galaxy.training.parqueaderov1.repository.UsuarioRepository;
import pe.edu.galaxy.training.parqueaderov1.repository.VehiculoRepository;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.ReporteService;
import pe.edu.galaxy.training.parqueaderov1.utils.Constantes;
import pe.edu.galaxy.training.parqueaderov1.utils.Meses;

import javax.persistence.criteria.JoinType;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReporteServiceImpl extends QueryService<VehiculoEntity> implements ReporteService {
    @Autowired
    VehiculoRepository vehiculoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    TarifaRepository tarifaRepository;

    @Override
    public Page<VehiculoEntity> findByIngresosFechaRegistro(Pageable pageable, String mes) throws ServiceException {
        try {
            List<String> text = Arrays.stream(Meses.values()).filter(f-> f.name().contains(mes))
                    .map(v-> v.getValue())
                    .collect(Collectors.toList());
            String date = String.valueOf(text.get(0));
           Page<VehiculoEntity> vehiculoEntities = vehiculoRepository.findByFechaRegistro(pageable, date);
            return vehiculoEntities;
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Page<TarifaEntity> findByTarifaFechaRegistro(Pageable pageable, String mes)  {
        try {
            List<String> text = Arrays.stream(Meses.values()).filter(f-> f.name().contains(mes))
                    .map(v-> v.getValue())
                    .collect(Collectors.toList());
            String date = String.valueOf(text.get(0));
            Page<TarifaEntity> tarifaEntities = tarifaRepository.findByFechaRegistro(pageable, date);
            return tarifaEntities;
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Long> findAllIngresos() throws ServiceException  {
        try {
            List<VehiculoEntity> lsVehiculo = vehiculoRepository.findAll();
            Map<String, Long> totales =  lsVehiculo.stream().collect(Collectors.groupingBy(VehiculoEntity::formatFecha, Collectors.counting())); // obtengo los meses con su total
            List<Long> totalMeses = new ArrayList<>();
            Constantes.meses().forEach((m) -> {
                if (totales.containsKey(m)) { // aqui compruebo si existe algun mes en el mapa
                    totales.forEach((k, v) -> {
                        if(k.contains(m)) {
                            totalMeses.add(v);
                        }
                    });
                }else{
                    totalMeses.add((long) 0);
                }
            });
            return totalMeses;
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public List<Double> findAllGanancias()  throws ServiceException  {
        try {
            List<TarifaEntity> list = tarifaRepository.findAll();
            // aqui agrupo por la fecha de registro y luego sumo los totales de la fecha
            Map<String, DoubleSummaryStatistics> totalXFecha =  list.stream().
                    collect(Collectors.groupingBy(TarifaEntity::formatFecha,
                            Collectors.summarizingDouble(TarifaEntity::getMontoPagar)));

            List<Double> totalMeses = new ArrayList<>();
            Constantes.meses().forEach((m) -> {
                if (totalXFecha.containsKey(m)) { // aqui compruebo si existe algun mes en el mapa con la lista de meses definida
                    totalXFecha.forEach((k, v) -> {
                        if(k.contains(m)) {
                            totalMeses.add(v.getSum());
                        }
                    });
                }else{
                    totalMeses.add(0.0);
                }
            });
            return totalMeses;
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public Integer totalCliente()  throws ServiceException  {
        try {
            return vehiculoRepository.totalClientes();
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public Integer totalUsers()  throws ServiceException  {
        try {
            return usuarioRepository.totalUsers();
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Double totalGanancias() throws ServiceException {
        try {
            return tarifaRepository.totalGanancia();
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public Integer nuevosIngresos() throws ServiceException {
        try {
            String date =  Constantes.formatDate(LocalDate.now());
            return vehiculoRepository.findByFechaRegistroAndEstado(date);
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public Integer totalComprobantes() throws ServiceException {
        try {
            return tarifaRepository.totalComprobantes();
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }
    @Override
    public Page<VehiculoEntity> findByCriteriaVehiculo(FiltroCriterio vehiculoCriterio, Pageable pageable) {
        final Specification<VehiculoEntity> specification = createSpecificationVehiculo(vehiculoCriterio);
        try {
            return vehiculoRepository.findAll(specification, pageable);
        }catch (Exception  e) {
            System.out.println("error: " + e);
            throw new ServiceException(e);
        }
    }
    private Specification<VehiculoEntity>createSpecificationVehiculo (FiltroCriterio criterio) {
        Specification<VehiculoEntity> specification = Specification.where(null);
        if(criterio != null) {
            if (criterio.getCodigoOperacion() != null) {

                specification = specification
                        .and(buildStringSpecification(criterio.getCodigoOperacion(), VehiculoEntity_.codigoOperacion));
            }

            if (criterio.getTipoVehiculo() != null) {
                specification = specification.
                        and(buildSpecification(criterio.getTipoVehiculo(), root -> root.join(VehiculoEntity_.TIPO_VEHICULO, JoinType.INNER)
                                .get(TipoVehiculoEntity_.ID)));
            }
            if (criterio.getFecDesde() != null) {
                specification = specification.
                        and(buildSpecification (criterio.getFecDesde(), root-> root.get("fechaRegistro").as(LocalDate.class)));
            }
            if (criterio.getFecHasta() != null) {
                specification = specification.
                        and(buildSpecification (criterio.getFecHasta(), root-> root.get("fechaRegistro").as(LocalDate.class)));
            }
        }
        return specification;
    }

}
