package pe.edu.galaxy.training.parqueaderov1.controller.general;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.galaxy.training.parqueaderov1.controller.error.GenericError;
import pe.edu.galaxy.training.parqueaderov1.criteria.FiltroCriterio;
import pe.edu.galaxy.training.parqueaderov1.dto.BusquedasDto;
import pe.edu.galaxy.training.parqueaderov1.entity.TarifaEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.VehiculoEntity;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.ReporteService;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.TarifaService;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.VehiculoService;
import pe.edu.galaxy.training.parqueaderov1.utils.criteriosBusquedas.CriterioBusquedas;

import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("v1/report")
@Slf4j
@CrossOrigin
public class ReporteController extends GenericError {
    @Autowired
    ReporteService reporteService;
    @Autowired
    TarifaService tarifaService;

    @Autowired
    VehiculoService vehiculoService;
    @GetMapping("/client-all-filtros")
    public ResponseEntity<?> IngresosAllFilter(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "id") String order,
                                       @RequestParam(defaultValue = "true") boolean asc,
                                       @RequestParam(defaultValue = "") String codigoOperacion,
                                       @RequestParam(defaultValue = "0") int tipoVehiculo,
                                       @RequestParam(defaultValue = "") String fecDesde,
                                       @RequestParam(defaultValue = "") String fecHasta) {
        try {

            BusquedasDto busquedasDto = new BusquedasDto();
            busquedasDto.setCodigoOperacion(codigoOperacion);
            busquedasDto.setFecDesde(fecDesde);
            busquedasDto.setFecHasta(fecHasta);
            if(tipoVehiculo > 0) busquedasDto.setTipoVehiculo(tipoVehiculo);
            FiltroCriterio vehiculoCriterio = CriterioBusquedas.createCriterio(busquedasDto);
            Page<VehiculoEntity> vehiculoEntities = reporteService.findByCriteriaVehiculo(vehiculoCriterio, PageRequest.of(page, size, Sort.by(order)));
            if (!asc) {
                vehiculoEntities = reporteService.findByCriteriaVehiculo(vehiculoCriterio, PageRequest.of(page, size, Sort.by(order).descending()));
            }
            return new ResponseEntity(vehiculoEntities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/ganancias-all-filtros")
    public ResponseEntity<?> GananciasAllFilter(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(defaultValue = "id") String order,
                                               @RequestParam(defaultValue = "true") boolean asc,
                                               @RequestParam(defaultValue = "") String codigoTarifa,
                                                @RequestParam(defaultValue = "") String placa,
                                               @RequestParam(defaultValue = "0") int tipoVehiculo,
                                               @RequestParam(defaultValue = "") String fecDesde,
                                               @RequestParam(defaultValue = "") String fecHasta) {
        try {

            BusquedasDto busquedasDto = new BusquedasDto();
            busquedasDto.setCodigoOperacion(codigoTarifa);
            busquedasDto.setFecDesde(fecDesde);
            busquedasDto.setFecHasta(fecHasta);
            busquedasDto.setPlaca(placa);
            if(tipoVehiculo > 0) busquedasDto.setTipoVehiculo(tipoVehiculo);
            FiltroCriterio criterio = CriterioBusquedas.createCriterio(busquedasDto);
            Page<TarifaEntity> tarifa = tarifaService.findByCriteriaTarifa(criterio, PageRequest.of(page, size, Sort.by(order)));
            if (!asc) {
                tarifa = tarifaService.findByCriteriaTarifa(criterio, PageRequest.of(page, size, Sort.by(order).descending()));
            }
            return new ResponseEntity(tarifa, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/total-client")
    public ResponseEntity<?> totalClientes() {
        try {
            Integer total = reporteService.totalCliente();
            return ResponseEntity.ok().body(total);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/total-users")
    public ResponseEntity<?> totalUsers() {
        try {
            Integer total = reporteService.totalUsers();
            return ResponseEntity.ok().body(total);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/total-ganancia")
    public ResponseEntity<?> totalGanancia() {
        try {
            Double total = reporteService.totalGanancias();
            return ResponseEntity.ok().body(total);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/total-ingresos-dia")
    public ResponseEntity<?> totalIngreso() {
        try {
            Integer total = reporteService.nuevosIngresos();
            return ResponseEntity.ok().body(total);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/total-comprobante")
    public ResponseEntity<?> totalComprobante() {
        try {
            Integer total = reporteService.totalComprobantes();
            return ResponseEntity.ok().body(total);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/total-ingresos-all-mes")
    public ResponseEntity<?> totalIngresosAllMes() {
        try {
            List<Long> totales = reporteService.findAllIngresos();
            return ResponseEntity.ok().body(totales);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/total-ganancias-all-mes")
    public ResponseEntity<?> totalGanciasAllMes() {
        try {
            List<Double> totales = reporteService.findAllGanancias();
            return ResponseEntity.ok().body(totales);
        } catch (Exception e) {
            System.out.println("error");
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/total-ingresos-mes")
    public ResponseEntity<?> totalIngresosPorMes(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "id_vehicle") String order,
                                                 @RequestParam(defaultValue = "true") boolean asc,
                                                 @RequestParam(defaultValue = "") String mes) {
        try {
            Page<VehiculoEntity> list = reporteService.findByIngresosFechaRegistro(PageRequest.of(page, size, Sort.by(order)), mes);
            if (!asc) {
                list = reporteService.findByIngresosFechaRegistro(PageRequest.of(page, size, Sort.by(order).descending()), mes);
            }
            if (isNull(list) || list.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/total-ganancias-mes")
    public ResponseEntity<?> totalGanciasPorMes(@PathVariable@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "id_tarifa") String order,
                                                @RequestParam(defaultValue = "true") boolean asc,
                                                @RequestParam(defaultValue = "") String mes) {
        try {
            Page<TarifaEntity> list = reporteService.findByTarifaFechaRegistro(PageRequest.of(page, size, Sort.by(order)), mes);
            if (!asc) {
                list = reporteService.findByTarifaFechaRegistro(PageRequest.of(page, size, Sort.by(order).descending()), mes);
            }
            if (isNull(list) || list.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
