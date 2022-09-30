package pe.edu.galaxy.training.parqueaderov1.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pe.edu.galaxy.training.parqueaderov1.entity.security.UsuarioEntity;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tb_person")
public class PersonaEntity {
    @Id
    @Column(name = "id_person")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "email_person")
    private String email;
    @Column(name = "name_person", nullable = false)
    private String nombre;
    @Column(name = "phone_person", length = 9, nullable = false)
    private String telefono;
    @Column(name = "number_document_person", length = 8, nullable = false)
    private String numeroDocumento;
    @Column(name = "addres_person", length = 240)
    private String direccion;
    @Column(name = "type_person", length = 1)
    private String typePersona; // 1 = cliente, 0 => usuario
    @OneToMany(mappedBy = "persona", cascade = CascadeType.MERGE)
    @ToString.Exclude
    List<UsuarioEntity> usuario;

    @JsonIgnore
    @OneToMany(mappedBy = "personaVehiculo", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<VehiculoEntity> vehiculoEntities;
}
