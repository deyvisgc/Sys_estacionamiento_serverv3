package pe.edu.galaxy.training.parqueaderov1.criteria;

import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.StringFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroCriterio {
    private StringFilter codigoOperacion;
    private StringFilter placa;
    private IntegerFilter tipoVehiculo;
    private LocalDateFilter fecDesde;
    private LocalDateFilter fecHasta;
    private LocalDateFilter fechaRegistro;


}
