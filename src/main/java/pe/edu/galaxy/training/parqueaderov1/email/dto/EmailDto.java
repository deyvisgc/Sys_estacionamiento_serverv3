package pe.edu.galaxy.training.parqueaderov1.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    private String mailFrom;
    private String mailTo;
    private String subject;
    private String userName;
    private String tokenPassword;
}
