package pe.edu.galaxy.training.parqueaderov1.controller.general;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.galaxy.training.parqueaderov1.controller.error.GenericError;
import pe.edu.galaxy.training.parqueaderov1.dto.TipoVehiculoDto;
import pe.edu.galaxy.training.parqueaderov1.dto.UsuarioDto;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.TipoVehiculoService;

import java.util.List;

import static java.util.Objects.isNull;

@RestController
@Slf4j
@RequestMapping("v1/tipo-vehiculo")
@CrossOrigin
public class TipoVehiculoController extends GenericError {
    @Autowired
    TipoVehiculoService vehiculoService;
    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        try {
            List<TipoVehiculoDto> tipoVehiculoDtos =  vehiculoService.findAll();
            if (isNull(tipoVehiculoDtos) || tipoVehiculoDtos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(tipoVehiculoDtos);
        } catch (Exception e) {
            log.info("Error: " + e);
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
