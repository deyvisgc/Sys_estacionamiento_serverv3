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
import pe.edu.galaxy.training.parqueaderov1.dto.TarifaDto;
import pe.edu.galaxy.training.parqueaderov1.dto.VehiculoDto;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.VehiculoService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Objects.isNull;
@RestController
@RequestMapping("v1/parqueo")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class VehiculoController extends GenericError {
    @Autowired
    private VehiculoService vehiculoService;
    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        try {
            List<VehiculoDto> lsVehiculo =  vehiculoService.findAll();
            if (isNull(lsVehiculo) || lsVehiculo.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(lsVehiculo);
        } catch (Exception e) {
            log.info("Error: " + e);
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/search-code-operacion")
    public ResponseEntity<?> buscarXcodigoOperacion(@RequestParam(value = "code", defaultValue = "") String code) {
        try {
            Optional<VehiculoDto> vehiculoDto = vehiculoService.findByCodigoOperacion(code);
            if (vehiculoDto.isPresent()) {
                return ResponseEntity.ok().body(vehiculoDto);
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id) {
        try {
            if (id <= 0) {
                return ResponseEntity.badRequest().body("El id " + id + " no existe");
            }
            Optional<VehiculoDto> vehiculoDto = vehiculoService.findById(id);
            if (vehiculoDto.isPresent()) {
                return ResponseEntity.ok().body(vehiculoDto);
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/")
    public ResponseEntity<?> ingresar(@Validated @RequestBody VehiculoDto vehiculoDto, BindingResult result) {
        ResMessage resMessage = new ResMessage();
        try {
            if (result.hasErrors()) {
                return super.getError(result);
            }
            Boolean existVehiculo = vehiculoService.existsByCedula(vehiculoDto.getCedula());
            if (existVehiculo) {
                resMessage.setMessage("Ya existe un vehiculo con la cedula: " + vehiculoDto.getCedula() + " en este parqueo.");
                return new  ResponseEntity(resMessage, HttpStatus.FORBIDDEN);
            }
            vehiculoService.ingreso(vehiculoDto);
            resMessage.setMessage("Ingreso exitoso");
            resMessage.setSuccess(true);
            return ResponseEntity.status(HttpStatus.CREATED).body(resMessage);
        } catch (RuntimeException e) {
            log.error("e" + e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/")
    public ResponseEntity<?> update(@Validated @RequestBody VehiculoDto vehiculoDto, BindingResult result) {
        ResMessage resMessage = new ResMessage();
        try {
            if (result.hasErrors()) {
                return super.getError(result);
            }
            Optional<VehiculoDto> editVehiculo = vehiculoService.findById(vehiculoDto.getId());
            if (editVehiculo.isEmpty()) {
                resMessage.setMessage("El vehiculo con la placa : " + vehiculoDto.getLicense_plate() + " no existe");
                return new  ResponseEntity(resMessage, HttpStatus.BAD_GATEWAY);
            } else if (editVehiculo.get().getStatus() != '1'){
                resMessage.setMessage("El vehiculo con la placa : " + vehiculoDto.getLicense_plate() + " ya fue retirado del estacionamiento");
                return new  ResponseEntity(resMessage, HttpStatus.BAD_GATEWAY);
            }
            VehiculoDto vehiculo = vehiculoService.update(vehiculoDto);
            if (isNull(vehiculo)) {
                resMessage.setMessage("El vehiculo con la placa: " + vehiculoDto.getLicense_plate() + " ya se encuentra registrado en nuestro establecimiento");
                return new  ResponseEntity(resMessage, HttpStatus.BAD_GATEWAY);
            }
            resMessage.setMessage("Registro actualizado");
            resMessage.setSuccess(true);
            return ResponseEntity.ok().body(resMessage);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/salida/{codigoOperacion}")
    public ResponseEntity<?> salida(@PathVariable(name = "codigoOperacion") String codigoOperacion) {
        ResMessage resMessage = new ResMessage();
        try {
            Optional<VehiculoDto> existVehiculoDto = vehiculoService.findByCodigoOperacionAndEstado(codigoOperacion, '1');
            if (existVehiculoDto.isEmpty()) {
                resMessage.setMessage("El vehiculo ya fue retirado del estableciento");
                return new  ResponseEntity(resMessage, HttpStatus.BAD_GATEWAY);
            }
            TarifaDto tarifaDto = vehiculoService.salida(existVehiculoDto.get());
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("vehiculo", tarifaDto.getVehiculo());
            resMessage.setSuccess(true);
            resMessage.setMessage("Pago exitoso");
            resMessage.setData(map);
            return ResponseEntity.status(HttpStatus.CREATED).body(resMessage);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/export/{id}")
    public void exprotar(@PathVariable(name = "id") Long id ,HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
            response.setHeader(headerKey,headerValue);
            vehiculoService.export(response, id);
            // return ResponseEntity.status(HttpStatus.CREATED).body(resMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        ResMessage resMessage = new ResMessage();
        try {
            log.info("id -> "+id);
            if (id <= 0) {
                resMessage.setMessage("El id " + " no existe");
                return ResponseEntity.badRequest().body(resMessage);
            }
            vehiculoService.deleteLogic(id);
            resMessage.setMessage("Vehiculo eliminado");
            resMessage.setSuccess(true);
            return ResponseEntity.ok().body(resMessage);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
