package pe.edu.galaxy.training.parqueaderov1.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pe.edu.galaxy.training.parqueaderov1.dto.TipoVehiculoDto;
import pe.edu.galaxy.training.parqueaderov1.entity.TipoVehiculoEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.VehiculoEntity;

@Mapper(componentModel = "spring", uses = VehiculoEntity.class)
public interface TipoVehiculoMapper {
    @Mappings({
            @Mapping(source = "description" , target = "descripcion"),
            @Mapping(source = "price_hour" , target = "precioHora"),
            @Mapping(source = "status", target = "estado")
    })
    TipoVehiculoDto toTipoVehiculoDto(TipoVehiculoEntity tipoVehiculo);
    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    TipoVehiculoEntity toTipoVehiculoEntity(TipoVehiculoDto tipoVehiculoDto);
}
