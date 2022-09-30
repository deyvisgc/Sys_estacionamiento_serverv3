package pe.edu.galaxy.training.parqueaderov1.utils.criteriosBusquedas;

import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.StringFilter;
import org.apache.commons.lang3.StringUtils;
import pe.edu.galaxy.training.parqueaderov1.criteria.FiltroCriterio;
import pe.edu.galaxy.training.parqueaderov1.dto.BusquedasDto;

import java.time.LocalDate;

public class CriterioBusquedas {

    public static FiltroCriterio createCriterio(BusquedasDto busquedasDto) {
        FiltroCriterio criterio = new FiltroCriterio();
        if (busquedasDto != null) {
            if(!StringUtils.isBlank(busquedasDto.getCodigoOperacion())) {
                StringFilter filter = new StringFilter();
                filter.setEquals(busquedasDto.getCodigoOperacion());
                criterio.setCodigoOperacion(filter);
            }
            if (busquedasDto.getTipoVehiculo() != null) {
                IntegerFilter filter = new IntegerFilter();
                filter.setEquals(busquedasDto.getTipoVehiculo());
                criterio.setTipoVehiculo(filter);
            }
            if(StringUtils.isNotBlank(busquedasDto.getFecDesde()) && StringUtils.isNotBlank(busquedasDto.getFecHasta())) {
                LocalDateFilter filterDesde = new LocalDateFilter();
                LocalDate fecDesde = LocalDate.parse(busquedasDto.getFecDesde());
                filterDesde.setGreaterThanOrEqual(fecDesde);
                criterio.setFecDesde(filterDesde);

                LocalDateFilter filterHasta = new LocalDateFilter();
                LocalDate fecHasta = LocalDate.parse(busquedasDto.getFecHasta());
                filterHasta.setLessThanOrEqual(fecHasta);
                criterio.setFecHasta(filterHasta);
            }
            if (StringUtils.isNotBlank(busquedasDto.getPlaca())) {
                StringFilter filter = new StringFilter();
                filter.setEquals(busquedasDto.getPlaca());
                criterio.setPlaca(filter);
            }
            /*
            if(busquedasDto.getMonth() != null) {
                IntegerFilter filter = new IntegerFilter();
                filter.setLessThanOrEqual(busquedasDto.getMonth());
                criterio.setMonth(filter);
            }

             */
        }
        return criterio;
    }
}
