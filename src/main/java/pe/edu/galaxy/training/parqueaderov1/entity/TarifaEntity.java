package pe.edu.galaxy.training.parqueaderov1.entity;

import lombok.*;
import pe.edu.galaxy.training.parqueaderov1.utils.Constantes;

import javax.persistence.*;
import java.time.ZoneId;
import java.util.Date;

@Entity
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Table(name = "tb_tarifa")
public class TarifaEntity {
    @Id
    @Column(name = "id_tarifa")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codigo_tarifa", length = 10)
    private String codigo;
    @Column(name = "monto_tarifa")
    private Double montoPagar;
    @Column(name = "fechaRegistro")
    private Date fechaRegistro;

    @Column(name = "total_hour")
    private Integer totalHoras;

    @OneToOne
    @JoinColumn(name = "id_verhicle")
    @ToString.Exclude
    private VehiculoEntity vehiculoEntity;
    @PrePersist
    private void persistFechaRegistro() {
        fechaRegistro = new Date();
    }
    public  String formatFecha() {
        ZoneId zoneId = ZoneId.of("America/Lima");
        return Constantes.formatDateYearMonth(fechaRegistro.toInstant().atZone(zoneId).toLocalDate());
    };
}
