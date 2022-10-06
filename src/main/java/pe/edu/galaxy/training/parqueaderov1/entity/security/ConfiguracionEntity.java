package pe.edu.galaxy.training.parqueaderov1.entity.security;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "tb_configuration")
@Entity
public class ConfiguracionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_config")
    private Long id;


    @NotBlank(message = "Tipo Api es requerido")
    @Column(name = "type_api")
    private String typeApi;

    @NotBlank(message = "url del api es requerido")
    @Column(name = "url_api")
    private String urlApi;

    @NotBlank(message = "token del api es requerido")
    @Column(name = "api_token", length = 10000)
    private String token;
}
