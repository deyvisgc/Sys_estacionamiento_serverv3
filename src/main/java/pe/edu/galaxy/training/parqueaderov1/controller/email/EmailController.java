package pe.edu.galaxy.training.parqueaderov1.controller.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.edu.galaxy.training.parqueaderov1.controller.base.ResMessage;
import pe.edu.galaxy.training.parqueaderov1.controller.error.GenericError;
import pe.edu.galaxy.training.parqueaderov1.dto.UsuarioDto;
import pe.edu.galaxy.training.parqueaderov1.email.dto.ChangePasswordDto;
import pe.edu.galaxy.training.parqueaderov1.email.dto.EmailDto;
import pe.edu.galaxy.training.parqueaderov1.email.service.EmailService;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.UsuarioService;

import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("v1/change-password")
@CrossOrigin
public class EmailController extends GenericError {

    @Autowired
    EmailService emailService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String mailFrom;

    private static final String subject = "Cambio de Contraseña";

    @GetMapping("/send-email/{email}")
    public ResponseEntity<?> sendEmailTemplate(@PathVariable(name = "email") String correo) {
        ResMessage resMessage = new ResMessage();
        try {
            UsuarioDto usuarioDto = usuarioService.findByEmail(correo);
            System.out.println("holaaaaa");
            if (Objects.isNull(usuarioDto)) {
                resMessage.setMessage("El correo: " + correo + " no existe en nuestra base de datos");
                return ResponseEntity.badRequest().body(resMessage);
            }
            EmailDto dto = new EmailDto();
            UUID uuid = UUID.randomUUID();
            String tokenPassword = uuid.toString();
            dto.setTokenPassword(tokenPassword);
            dto.setMailFrom(mailFrom);
            dto.setMailTo(usuarioDto.getPerson().getGmail());
            dto.setSubject(subject);
            dto.setUserName(usuarioDto.getPerson().getName());
            usuarioService.updateTokenPassword(tokenPassword, usuarioDto.getId());
            emailService.sendEmail(dto);
            resMessage.setMessage("Te hemos enviado un email" + " a: " + correo);
            resMessage.setSuccess(true);
            return ResponseEntity.ok().body(resMessage);
        }catch (Exception e) {
            System.out.println("e: "+ e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PutMapping("/")
     public ResponseEntity updateChange(@Validated @RequestBody ChangePasswordDto changePasswordDto, BindingResult result) {
        ResMessage resMessage = new ResMessage();
        try {
            if (result.hasErrors()) {
                return super.getError(result);
            }

            UsuarioDto usuarioDto = usuarioService.findByTokenPassword(changePasswordDto.getTokenPassword());

            if(isNull(usuarioDto)) {
                resMessage.setMessage("El token: " + changePasswordDto.getTokenPassword() + " no existe");
                return ResponseEntity.badRequest().body(resMessage);
            }

            String password = new BCryptPasswordEncoder().encode(changePasswordDto.getPassword());
            usuarioService.changePassword(password, usuarioDto.getId());
            resMessage.setMessage("Contraseña actualizada");
            resMessage.setSuccess(true);
            return ResponseEntity.ok().body(resMessage);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
