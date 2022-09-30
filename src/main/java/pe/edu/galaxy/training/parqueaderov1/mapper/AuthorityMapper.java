package pe.edu.galaxy.training.parqueaderov1.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pe.edu.galaxy.training.parqueaderov1.dto.AuthorityDto;
import pe.edu.galaxy.training.parqueaderov1.entity.security.AuthorityEntity;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {
    @Mappings({
            @Mapping(source = "name" , target = "descripcion"),
    })
    AuthorityDto toAuthorityDto(AuthorityEntity authorityEntity);
    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    AuthorityEntity toAuthorityEntity(AuthorityDto authorityDto);
}
