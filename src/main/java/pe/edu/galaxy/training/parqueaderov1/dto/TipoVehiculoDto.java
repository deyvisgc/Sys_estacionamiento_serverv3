package pe.edu.galaxy.training.parqueaderov1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoVehiculoDto {
    private long id;
    @NotBlank(message = "La descripcion es requerida")
    private String descripcion;
    @Min(value = 1, message = "Como minimo el precio debe ser 1 sol")
    private double precioHora;
    private String estado;
}
