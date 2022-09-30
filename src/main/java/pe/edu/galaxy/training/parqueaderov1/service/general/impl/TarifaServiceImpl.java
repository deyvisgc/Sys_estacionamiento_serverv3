package pe.edu.galaxy.training.parqueaderov1.service.general.impl;

import io.github.jhipster.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pe.edu.galaxy.training.parqueaderov1.criteria.FiltroCriterio;
import pe.edu.galaxy.training.parqueaderov1.dto.TarifaDto;
import pe.edu.galaxy.training.parqueaderov1.entity.TarifaEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.TarifaEntity_;
import pe.edu.galaxy.training.parqueaderov1.entity.TipoVehiculoEntity_;
import pe.edu.galaxy.training.parqueaderov1.entity.VehiculoEntity_;
import pe.edu.galaxy.training.parqueaderov1.mapper.TarifaMapper;
import pe.edu.galaxy.training.parqueaderov1.repository.TarifaRepository;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.TarifaService;

import javax.persistence.criteria.JoinType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TarifaServiceImpl extends QueryService<TarifaEntity> implements TarifaService {
    @Autowired
    private TarifaRepository tarifaRepository;
    @Autowired
    private TarifaMapper tarifaMapper;
    @Override
    public List<TarifaDto> findAll() throws ServiceException {
        return null;
    }

    @Override
    public Optional<TarifaDto> findById(long id) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public List<TarifaDto> findByObject(TarifaDto tarifaDto) throws ServiceException {
        return null;
    }

    @Override
    public TarifaDto save(TarifaDto tarifaDto) throws ServiceException {
        return null;
    }

    @Override
    public TarifaDto update(TarifaDto tarifaDto) throws ServiceException {
        return null;
    }

    @Override
    public void deleteLogic(Long id) throws ServiceException {

    }

    @Override
    public Page<TarifaEntity> findByCriteriaTarifa(FiltroCriterio criterio, Pageable pageable) throws ServiceException {
        final Specification<TarifaEntity> specification = createSpecification(criterio);
        try {
            return tarifaRepository.findAll(specification, pageable);
        }catch (Exception  e) {
            System.out.println("error: " + e);
            throw new ServiceException(e);
        }
    }
    public Specification<TarifaEntity>createSpecification (FiltroCriterio criterio) {
        Specification<TarifaEntity> specification = Specification.where(null);
        if(criterio != null) {

            if (criterio.getCodigoOperacion() != null) {

                specification = specification
                        .and(buildStringSpecification(criterio.getCodigoOperacion(), TarifaEntity_.codigo));
            }
            if (criterio.getPlaca() != null) {
                specification = specification.
                        and(buildSpecification(criterio.getPlaca(), root -> root.join(TarifaEntity_.vehiculoEntity, JoinType.INNER)
                                .get(VehiculoEntity_.placa)));
            }
            if (criterio.getTipoVehiculo() != null) {
                specification = specification.
                        and(buildSpecification(criterio.getTipoVehiculo(), root -> root.join(TarifaEntity_.vehiculoEntity, JoinType.INNER)
                                .join(VehiculoEntity_.TIPO_VEHICULO, JoinType.INNER)
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
