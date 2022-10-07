package pe.edu.galaxy.training.parqueaderov1.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {
    @NotBlank
    private String password;
    @NotBlank
    private String tokenPassword;
}
