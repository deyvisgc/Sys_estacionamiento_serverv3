package pe.edu.galaxy.training.parqueaderov1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehiculoDto {
    private long id;
    @NotBlank(message = "Placa es requerida.")
    private String license_plate;
    @NotBlank(message = "Cedula es requerida.")
    private String cedula;
    private String code;
    @NotNull(message = "Tipo de vehiculo es requerido.")
    private TipoVehiculoDto type_vehicle;
    @NotNull(message = "Hora de entrada es requerido.")
    private LocalTime check_in_time; //hora entrada
    private LocalTime departure_time; // hora salida
    private Date fechaRegistro;
    private char status;
    private char statusDelete;

    @Valid
    private PersonaDto person;
}
