package pe.edu.galaxy.training.parqueaderov1.controller.general;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.edu.galaxy.training.parqueaderov1.controller.base.ResMessage;
import pe.edu.galaxy.training.parqueaderov1.controller.error.GenericError;
import pe.edu.galaxy.training.parqueaderov1.dto.AuthorityDto;
import pe.edu.galaxy.training.parqueaderov1.dto.PersonaDto;
import pe.edu.galaxy.training.parqueaderov1.dto.UsuarioDto;
import pe.edu.galaxy.training.parqueaderov1.entity.PersonaEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.security.AuthorityEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.security.ConfiguracionEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.security.UsuarioEntity;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.AuthorityService;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.ConfiguracionService;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.PersonaService;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.UsuarioService;
import pe.edu.galaxy.training.parqueaderov1.utils.Api;
import pe.edu.galaxy.training.parqueaderov1.utils.Constantes;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.*;

import static java.util.Objects.isNull;
@RestController
@Slf4j
@RequestMapping("v1/usuario")
@CrossOrigin
public class UsuarioController extends GenericError {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PersonaService personaService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    ConfiguracionService configuracionService;
    @GetMapping("/")
    public ResponseEntity<Page<UsuarioDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String order,
            @RequestParam(defaultValue = "true") boolean asc
            ) {
        try {
            Page<UsuarioDto> users = usuarioService.page(PageRequest.of(page, size, Sort.by(order)));
            if (!asc) {
                users = usuarioService.page( PageRequest.of(page, size, Sort.by(order).descending()));
            }
            if (isNull(users) || users.isEmpty()) {
               return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.info("Error: " + e);
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id) {
        try {
            if (id <= 0) {
                return ResponseEntity.badRequest().body("El id " + id + " no existe");
            }

            Optional<UsuarioDto> usuarioDto = usuarioService.findById(id);
            if (usuarioDto.isPresent()) {
                return ResponseEntity.ok().body(usuarioDto);
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/users-rol/{id}")
    public ResponseEntity<?> getByRole(@PathVariable(name = "id") Long id) {
        try {
            System.out.println("id" + id);
             List<AuthorityDto> list = new ArrayList<>();
             Optional<UsuarioDto> usuarioDto = usuarioService.findById(id);

             for ( AuthorityDto  authorityDto: usuarioDto.get().getRole()){
                list.add(authorityService.findById(authorityDto.getId()).get());
             }

             if (isNull(list) || list.isEmpty()) {
                return ResponseEntity.noContent().build();
             }
             return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            System.err.println("e: " + e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/save-rol")
    public ResponseEntity<?> saveRole(@Validated @RequestBody AuthorityDto authorityDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return super.getError(result);
            }
            AuthorityDto usuarioDto = authorityService.save(authorityDto);
            return ResponseEntity.ok().body(usuarioDto);
        } catch (Exception e) {
            System.err.println("e: " + e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/search-reniec/{dni}")
    public ResponseEntity<?> getSearchRuc(@PathVariable(name = "dni") String dni) {
        try {
            Optional<ConfiguracionEntity> configApi = configuracionService.findByTypeApi("documento");
            if (configApi.isEmpty()) {
                 return ResponseEntity.badRequest().body("No existe token");
            }
            HttpHeaders headers = Api.obtenerHeaders(configApi.get().getToken());
            String uri = configApi.get().getUrlApi().concat("/dni/") + dni;
            ResponseEntity<String> response = Api.fetchApi(uri, HttpMethod.GET, headers);
            if (isNull(response.getBody())) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(response.getBody());

        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/rol")
    public ResponseEntity<?> getRol() {
        try {
            List<AuthorityDto> authorityDto = authorityService.findAll();
            if (isNull(authorityDto) || authorityDto.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(authorityDto);

        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')") // AQUI LE DIJO QUE SOLO EL ADMIN PUEDE GUARDAR
    @PostMapping("/")
    public ResponseEntity<?> save(@Validated @RequestBody UsuarioDto usuarioDto, BindingResult result) {
         ResMessage resMessage = new ResMessage();

        try {
            if (result.hasErrors()) {
                return super.getError(result);
            }
            UsuarioEntity existUsers = usuarioService.existsByUsuario(usuarioDto.getUser_name());
            Optional<PersonaDto> existCorreo = personaService.findByEmailAndTypePersona(usuarioDto.getPerson().getGmail());
            if (!isNull(existUsers)) {
                resMessage.setMessage("El usuario: " + usuarioDto.getUser_name() + " ya existe, digitar un usuario diferente");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resMessage);
            }
            if (!isNull(existCorreo)) {
                resMessage.setMessage("El correo: " + usuarioDto.getPerson().getGmail() + " ya existe, digitar un correo diferente");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resMessage);
            }
            System.out.println("por que no entra");
            Optional<PersonaDto> personaDto = personaService.findByNumeroDocumento(usuarioDto.getPerson().getNumber());

            if (isNull(personaDto)) {
                Set<AuthorityDto> roles = new HashSet<>();
                for (AuthorityDto authorityDto: usuarioDto.getRole()) {
                    roles.add(authorityService.findById(authorityDto.getId()).get());
                }
                usuarioDto.setPassword(new BCryptPasswordEncoder().encode(usuarioDto.getPassword()));
                usuarioDto.setRole(roles);
                UsuarioDto usuario = usuarioService.save(usuarioDto);
                if (isNull(usuario)) {
                    return ResponseEntity.noContent().build();
                }
                resMessage.setMessage("Usuario registrado");
                resMessage.setSuccess(true);
                return ResponseEntity.status(HttpStatus.CREATED).body(resMessage);
            } else {
                resMessage.setMessage("Ya existe un usuario con el numero documento: " + usuarioDto.getPerson().getNumber());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resMessage);
            }
        } catch (RuntimeException e) {
            log.error("e" + e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')") // AQUI LE DIJO QUE SOLO EL ADMIN PUEDE ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> update( @PathVariable("id") Long id, @Validated @RequestBody UsuarioDto usuarioDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return super.getError(result);
            }
            ResMessage resMessage = validate(usuarioDto, id);
            System.out.println("existCorreo: " + resMessage);
            if (resMessage.getSuccess()) {
                usuarioDto.setId(id);
                UsuarioDto usuario = usuarioService.update(usuarioDto);
                if (isNull(usuario)) {
                    return ResponseEntity.noContent().build();
                }
                resMessage.setMessage("Usuario actualizado");
                resMessage.setSuccess(true);
                return ResponseEntity.status(HttpStatus.CREATED).body(resMessage);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resMessage);
        } catch (RuntimeException e) {
            log.error("Error: " + e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private ResMessage validate(UsuarioDto usuarioDto, long id) {
        ResMessage resMessage = new ResMessage();
        Optional<UsuarioDto> existUsuario = usuarioService.findById(id);

        if (existUsuario.isEmpty()) {
            resMessage.setMessage("El usuario: " + usuarioDto.getUser_name() + " no existe");
            return resMessage;
        }

        UsuarioEntity emailRepet = usuarioService.existsByUsuario(usuarioDto.getUser_name());
        Optional<PersonaDto> existCorreo = personaService.findByEmailAndTypePersona(usuarioDto.getPerson().getGmail());
        if (isNull(emailRepet)) {
            resMessage.setSuccess(true);
        }
        if (isNull(existCorreo)) { // si no existe si se puede cambiar
            resMessage.setSuccess(true);
        }

        if (!isNull(emailRepet) && emailRepet.getId() != existUsuario.get().getId()) {
            resMessage.setMessage("El usuario: " + usuarioDto.getUser_name() + " ya existe, digitar un usuario diferente");
            return resMessage;
        }

        if (!isNull(existCorreo) && existCorreo.get().getId() != existUsuario.get().getPerson().getId()) {
            resMessage.setMessage("El correo: " + usuarioDto.getPerson().getGmail() + " ya existe, digitar un correo diferente");
            return resMessage;
        }

        Optional<PersonaDto> existPerson = personaService.findById(existUsuario.get().getPerson().getId());
        if (existPerson.isPresent() && existUsuario.get().getPerson().getId() != existPerson.get().getId()) {
            resMessage.setMessage("Los datos de la tabla usuario y persona no concuerdan");
            log.error(resMessage.getMessage());
            return resMessage;
        }

        Optional<PersonaDto> personaDto = personaService.findByNumeroDocumento(usuarioDto.getPerson().getNumber());

        if (isNull(personaDto)) {
            resMessage.setSuccess(true);
            return resMessage;
        }
        if (personaDto.get().getId() == existPerson.get().getId()) {
           resMessage.setSuccess(true);
        } else {
            resMessage.setMessage("Ya existe un usuario con el numero documento documento: " + usuarioDto.getPerson().getNumber());
        }
        return resMessage;
    }
    @PreAuthorize("hasRole('ADMIN')") // AQUI LE DIJO QUE SOLO EL ADMIN PUEDE ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        ResMessage resMessage = new ResMessage();
        try {
            log.info("id -> "+id);
            if (id <= 0) {
                resMessage.setMessage("El id " + " no existe");
                return ResponseEntity.badRequest().body(resMessage);
            }
            usuarioService.deleteLogic(id);
            resMessage.setMessage("Usuario Eliminado");
            resMessage.setSuccess(true);
            return ResponseEntity.ok().body(resMessage);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
