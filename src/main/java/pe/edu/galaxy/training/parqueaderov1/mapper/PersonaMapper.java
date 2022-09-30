package pe.edu.galaxy.training.parqueaderov1.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pe.edu.galaxy.training.parqueaderov1.dto.PersonaDto;
import pe.edu.galaxy.training.parqueaderov1.entity.PersonaEntity;
@Mapper(componentModel = "spring")
public interface PersonaMapper {
    @Mappings({
            @Mapping(source = "nombre", target = "name"),
            @Mapping(source = "telefono", target = "phone"),
            @Mapping(source = "numeroDocumento", target = "number"),
            @Mapping(source = "direccion", target = "addres"),
            @Mapping(source = "typePersona", target = "type_person"),
            @Mapping(source = "email", target = "gmail")
    })
    PersonaDto toPersonaDto(PersonaEntity persona);
    @InheritInverseConfiguration // que me haga el inverso de dto a entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    PersonaEntity toPersonaEntity(PersonaDto  personaDto);
}
