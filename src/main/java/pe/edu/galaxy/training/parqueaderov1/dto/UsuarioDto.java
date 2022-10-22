package pe.edu.galaxy.training.parqueaderov1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@JsonIgnoreProperties(value = {}) => cuando hay varios atributos para ignorar
public class UsuarioDto {
    private long id;
    @NotBlank(message = "Nombre de usuario requerido")
    private String user_name;
    //@NotBlank(message = "Password es requerido")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // que solo permita escribir
    private String password;
    private String status;
    private String tokenPassword;
    @Valid
    private PersonaDto person;
    //@NotNull(message = "Roles requeridos")
    private Set<AuthorityDto> role;
}
