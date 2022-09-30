package pe.edu.galaxy.training.parqueaderov1.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pe.edu.galaxy.training.parqueaderov1.dto.TarifaDto;
import pe.edu.galaxy.training.parqueaderov1.entity.TarifaEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.VehiculoEntity;

@Mapper(componentModel = "spring", uses = VehiculoEntity.class)
public interface TarifaMapper {
    @Mappings({
            @Mapping(source = "codigo" , target = "code"),
            @Mapping(source = "montoPagar" , target = "total"),
            @Mapping(source = "vehiculoEntity", target = "vehiculo"),
            @Mapping(source = "totalHoras", target = "totalHour")
    })
    TarifaDto toTarifaDto(TarifaEntity tarifaEntity);
    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    TarifaEntity toTarifaEntity(TarifaDto tarifaDto);
}
