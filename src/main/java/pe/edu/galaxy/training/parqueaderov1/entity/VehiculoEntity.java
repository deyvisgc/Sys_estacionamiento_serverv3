package pe.edu.galaxy.training.parqueaderov1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.galaxy.training.parqueaderov1.utils.Constantes;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_vehicle")
public class VehiculoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicle", unique = true)
    private long id;
    @Column(name = "license_plate", nullable = false)
    private String placa;

    private String cedula;
    @Column(name = "date_register")
    private Date fechaRegistro;
    @Column(name = "departure_date")
    private Date fechaSalida;
    @Column(name = "check_in_time", nullable = false)
    private LocalTime horaEntrada;
    @Column(name = "departure_time")
    private LocalTime horaSalida;
    @Column(name = "opCode")
    private String codigoOperacion;
    @Column(name = "status")
    @Builder.Default
    private char estado = '1';

    @Column(name = "status_delete")
    @Builder.Default
    private char statusDelete = '1'; // 1 => activo, 0 => eliminado

    @JsonIgnore
    @OneToOne(mappedBy = "vehiculoEntity")
    private TarifaEntity tarifaEntity;
    @ManyToOne
    @JoinColumn(name = "id_person")
    private PersonaEntity personaVehiculo;
    @ManyToOne
    @JoinColumn(name = "id_type_vehicle", insertable = false, updatable = false)
    private TipoVehiculoEntity tipoVehiculo;
   @PrePersist
    private void persistFechaRegistro() {
        fechaRegistro = new Date();
    }
    @PreUpdate
    private void UpdateFechaSalida() {
        fechaSalida = new Date();
    }
    public  String formatFecha() {
        ZoneId zoneId = ZoneId.of("America/Lima");
        return Constantes.formatDateYearMonth(fechaRegistro.toInstant().atZone(zoneId).toLocalDate());
    };
}
