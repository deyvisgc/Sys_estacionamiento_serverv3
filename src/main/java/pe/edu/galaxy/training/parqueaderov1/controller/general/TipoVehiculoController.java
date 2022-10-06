package pe.edu.galaxy.training.parqueaderov1.controller.general;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.edu.galaxy.training.parqueaderov1.controller.base.ResMessage;
import pe.edu.galaxy.training.parqueaderov1.controller.error.GenericError;
import pe.edu.galaxy.training.parqueaderov1.dto.AuthorityDto;
import pe.edu.galaxy.training.parqueaderov1.dto.TipoVehiculoDto;
import pe.edu.galaxy.training.parqueaderov1.dto.UsuarioDto;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.TipoVehiculoService;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@RestController
@Slf4j
@RequestMapping("v1/tipo-vehiculo")
@CrossOrigin
public class TipoVehiculoController extends GenericError {
    @Autowired
    TipoVehiculoService tipoVehiculoService;
    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        try {
            List<TipoVehiculoDto> tipoVehiculoDtos =  tipoVehiculoService.findAll();
            if (isNull(tipoVehiculoDtos) || tipoVehiculoDtos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(tipoVehiculoDtos);
        } catch (Exception e) {
            log.info("Error: " + e);
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/all-paginacion")
    public ResponseEntity<?> getAllPaginacion(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id") String order,
                                              @RequestParam(defaultValue = "true") boolean asc) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(order));
            if (!asc) {
                pageable = PageRequest.of(page, size, Sort.by(order).descending());
            }
            Page<TipoVehiculoDto> vehiculoDtos = tipoVehiculoService.page(pageable);
            if (isNull(vehiculoDtos) || vehiculoDtos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(vehiculoDtos);
        } catch (Exception e) {
            log.info("Error: " + e);
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getByID(@PathVariable(name = "id") Long id) {
        try {
            Optional<TipoVehiculoDto> tipoVehiculoDtos =  tipoVehiculoService.findById(id);
            if (tipoVehiculoDtos.isEmpty() || !tipoVehiculoDtos.isPresent()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(tipoVehiculoDtos);
        } catch (Exception e) {
            log.info("Error: " + e);
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping("/")
    public ResponseEntity<?> save(@Validated @RequestBody TipoVehiculoDto tipoVehiculoDto, BindingResult result) {
        ResMessage resMessage = new ResMessage();
        try {

            if (result.hasErrors()) {
                return super.getError(result);
            }
            TipoVehiculoDto vehiculoDto = tipoVehiculoService.save(tipoVehiculoDto);
            if (isNull(vehiculoDto)) {
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
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @Validated @RequestBody TipoVehiculoDto tipoVehiculoDto, BindingResult result) {
        ResMessage resMessage = new ResMessage();
        try {

            if (result.hasErrors()) {
                return super.getError(result);
            }
            tipoVehiculoDto.setId(id);
            TipoVehiculoDto vehiculoDto = tipoVehiculoService.update(tipoVehiculoDto);
            if (isNull(vehiculoDto)) {
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
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        ResMessage resMessage = new ResMessage();
        try {
            if (id <= 0) {
                resMessage.setMessage("El id " + " no existe");
                return ResponseEntity.badRequest().body(resMessage);
            }
            tipoVehiculoService.deleteLogic(id);
            resMessage.setMessage("Registro eliminado");
            resMessage.setSuccess(true);
            return ResponseEntity.ok().body(resMessage);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
