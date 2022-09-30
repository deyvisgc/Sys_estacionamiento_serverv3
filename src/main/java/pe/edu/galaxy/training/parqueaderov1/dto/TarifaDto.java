package pe.edu.galaxy.training.parqueaderov1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarifaDto {
    private Long id;
    private String code;
    private double total;
    private Integer totalHour;
    private VehiculoDto vehiculo;
    private Date fechaRegistro;
}
