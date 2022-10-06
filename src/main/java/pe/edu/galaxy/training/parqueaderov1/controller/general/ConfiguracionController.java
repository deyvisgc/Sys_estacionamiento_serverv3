package pe.edu.galaxy.training.parqueaderov1.controller.general;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.edu.galaxy.training.parqueaderov1.controller.base.ResMessage;
import pe.edu.galaxy.training.parqueaderov1.controller.error.GenericError;
import pe.edu.galaxy.training.parqueaderov1.entity.security.ConfiguracionEntity;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.ConfiguracionService;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
@RestController
@Slf4j
@RequestMapping("v1/config")
@CrossOrigin
public class ConfiguracionController extends GenericError {
    @Autowired
    ConfiguracionService configuracionService;
    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        try {
            List<ConfiguracionEntity> config =  configuracionService.findAll();
            if (isNull(config) || config.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(config);
        } catch (Exception e) {
            log.info("Error: " + e);
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getbyId(@PathVariable(name = "id") Long id) {
        try {
            Optional<ConfiguracionEntity> config =  configuracionService.findById(id);
            if (isNull(config) || config.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(config.get());
        } catch (Exception e) {
            log.info("Error: " + e);
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/")
    public ResponseEntity<?> save(@Validated @RequestBody ConfiguracionEntity configuracionEntity, BindingResult result) {
        ResMessage resMessage = new ResMessage();
        try {

            if (result.hasErrors()) {
                return super.getError(result);
            }
            ConfiguracionEntity config = configuracionService.save(configuracionEntity);
            if (isNull(config)) {
                return ResponseEntity.noContent().build();
            }
            resMessage.setSuccess(true);
            resMessage.setMessage("Registro exitoso.");
            return ResponseEntity.ok().body(resMessage);
        } catch (Exception e) {
            System.err.println("e: " + e.getMessage());
            resMessage.setMessage(e.getMessage());
            return new ResponseEntity(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @Validated @RequestBody ConfiguracionEntity configuracionEntity, BindingResult result) {
        ResMessage resMessage = new ResMessage();
        try {

            if (result.hasErrors()) {
                return super.getError(result);
            }
            configuracionEntity.setId(id);
            ConfiguracionEntity config = configuracionService.update(configuracionEntity);
            if (isNull(config)) {
                return ResponseEntity.noContent().build();
            }
            resMessage.setSuccess(true);
            resMessage.setMessage("Registro actualizado.");
            return ResponseEntity.ok().body(resMessage);
        } catch (Exception e) {
            System.err.println("e: " + e.getMessage());
            resMessage.setMessage(e.getMessage());
            return new ResponseEntity(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
