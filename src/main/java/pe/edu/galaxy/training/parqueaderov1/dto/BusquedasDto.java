package pe.edu.galaxy.training.parqueaderov1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusquedasDto {

    private String codigoOperacion;
    private Integer tipoVehiculo;
    private String fecDesde;
    private String fecHasta;
    private String placa;
    private Date fechaRegistro;
}
