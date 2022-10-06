package pe.edu.galaxy.training.parqueaderov1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_type_vehicle")
public class TipoVehiculoEntity {
    @Id
    @Column(name = "id_type_vehicle")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private double price_hour;
    @Builder.Default
    private char status = '1';
    @JsonIgnore
    @OneToMany(mappedBy = "tipoVehiculo", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<VehiculoEntity> lsVehiculo;
}
