package pe.edu.galaxy.training.parqueaderov1.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pe.edu.galaxy.training.parqueaderov1.dto.VehiculoDto;
import pe.edu.galaxy.training.parqueaderov1.entity.VehiculoEntity;

@Mapper(componentModel = "spring", uses = { PersonaMapper.class, TipoVehiculoMapper.class})
public interface VehiculoMapper {
    @Mappings({
            @Mapping(source = "placa" , target = "license_plate"),
            @Mapping(source = "cedula" , target = "cedula"),
            @Mapping(source = "tipoVehiculo", target = "type_vehicle"),
            @Mapping(source = "horaEntrada", target = "check_in_time"),
            @Mapping(source = "horaSalida", target = "departure_time"),
            @Mapping(source = "personaVehiculo", target = "person"),
            @Mapping(source = "estado", target = "status"),
            @Mapping(source = "codigoOperacion", target = "code")
    })
    VehiculoDto toVehiculoDto(VehiculoEntity vehiculo);
    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "fechaSalida", ignore = true)
    @Mapping(target = "statusDelete", ignore = true)
    VehiculoEntity toVehiculoEntity(VehiculoDto vehiculoDto);
}
