package pe.edu.galaxy.training.parqueaderov1.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pe.edu.galaxy.training.parqueaderov1.entity.TipoVehiculoEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.security.AuthorityEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.security.ConfiguracionEntity;
import pe.edu.galaxy.training.parqueaderov1.mapper.TipoVehiculoMapper;
import pe.edu.galaxy.training.parqueaderov1.repository.AuthorityRepository;
import pe.edu.galaxy.training.parqueaderov1.repository.TipoVehiculoRepository;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.ConfiguracionService;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.TipoVehiculoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class InserDefault implements CommandLineRunner {
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    TipoVehiculoRepository  tipoVehiculoRepository;
    @Autowired
    ConfiguracionService configuracionService;
    @Override
    public void run(String... args) throws Exception {
        // ESTO SE EJECUTA CUANDO CORRE EL SISTEMA
        createRol();
        insertConfig();
        inserTipoVehiculo();
    }
     void  createRol() {
         List<AuthorityEntity> list = authorityRepository.findAll();
         if (list.isEmpty()) {
             AuthorityEntity rolAdmin  = AuthorityEntity.builder()
                     .name("ROLE_ADMIN")
                     .build();
             list.add(rolAdmin);
             AuthorityEntity roleUsers  = AuthorityEntity.builder()
                     .name("ROLE_USERS")
                     .build();
             list.add(roleUsers);
             AuthorityEntity roleVendedor  = AuthorityEntity.builder()
                     .name("ROLE_VENDEDOR")
                     .build();
             list.add(roleVendedor);
             authorityRepository.saveAll(list);
         }
    }
    void  inserTipoVehiculo() {
        List<TipoVehiculoEntity> refTipo = tipoVehiculoRepository.findAll();
        if (refTipo.isEmpty()) {
            List<TipoVehiculoEntity> vehiculoEntities = new ArrayList<>();
            TipoVehiculoEntity vehiculo = TipoVehiculoEntity.builder()
                    .description("Moto")
                    .price_hour(3.5)
                    .status('1')
                    .build();
            vehiculoEntities.add(vehiculo);
            TipoVehiculoEntity vehiculo2 = TipoVehiculoEntity.builder()
                    .description("Carro Particular")
                    .price_hour(5.5)
                    .status('1')
                    .build();
            vehiculoEntities.add(vehiculo2);
            TipoVehiculoEntity vehiculo3 = TipoVehiculoEntity.builder()
                    .description("Camion")
                    .price_hour(6.5)
                    .status('1')
                    .build();
            vehiculoEntities.add(vehiculo3);
            tipoVehiculoRepository.saveAll(vehiculoEntities);
        }

    }

    void insertConfig() {
        List<ConfiguracionEntity> config = configuracionService.findAll();
        if( config.isEmpty()) {
            ConfiguracionEntity entity = ConfiguracionEntity.builder()
                    .token("c63cebb91665fa2d355875b19743e1403ef243aaf184a15c4f4e081f20e74c8c")
                    .urlApi("https://apiperu.dev/api")
                    .typeApi("documento")
                    .build();
            configuracionService.save(entity);
        }
    }
}
