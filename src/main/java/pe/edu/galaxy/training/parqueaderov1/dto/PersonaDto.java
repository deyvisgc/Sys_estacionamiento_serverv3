package pe.edu.galaxy.training.parqueaderov1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonaDto {
    // private static final long serialVersionUID = -5467458619551056586L;
    private long id;
    @NotBlank(message = "Nombre es requerido")
    private String name;
    @NotBlank(message = "El número telfonico es requerido.")
    @Size(min = 9, max = 9, message = "EL Numero telefonico debe tener como minimo y maximo {min} digitos.")
    private String phone;
    @NotBlank(message = "El número documento  es requerido.")
    @Size(min = 8, max = 8, message = "EL Numero documento es requerido, debe tener como minimo y maximo {min} digitos.")
    private String number; // numero documento
    @Email(message = "Email debe tener un formato @gmail.com")
    private String gmail;
    // @NotBlank(message = "Direccion es requerido")
    private String addres;
    private String type_person;
}
