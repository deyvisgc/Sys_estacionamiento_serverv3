package pe.edu.galaxy.training.parqueaderov1.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pe.edu.galaxy.training.parqueaderov1.dto.UsuarioDto;
import pe.edu.galaxy.training.parqueaderov1.entity.security.UsuarioEntity;

import java.util.List;
@Mapper(componentModel = "spring", uses = {PersonaMapper.class})
public interface UsuarioMapper {
    @Mappings({
            @Mapping(source = "usuario", target = "user_name"),
            @Mapping(source = "clave", target = "password"),
            @Mapping(source = "persona", target = "person"),
            @Mapping(source = "estado", target = "status"),
            @Mapping(source = "authorities", target = "role")
    })
    UsuarioDto toUsuarioDto(UsuarioEntity usuarioEntity);
    List<UsuarioDto> toListUsuarioDto(List<UsuarioEntity> usuarioEntities);
    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tokenPassword", ignore = true)
    UsuarioEntity toUsuarioEntity(UsuarioDto usuarioDto);
}
