package pe.edu.galaxy.training.parqueaderov1.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pe.edu.galaxy.training.parqueaderov1.entity.security.AuthorityEntity;
import pe.edu.galaxy.training.parqueaderov1.repository.AuthorityRepository;
import pe.edu.galaxy.training.parqueaderov1.repository.TipoVehiculoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InserDefault implements CommandLineRunner {
    @Autowired
    private AuthorityRepository authorityService;
    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository;
    @Override
    public void run(String... args) throws Exception {
        // ESTO SE EJECUTA CUANDO CORRE EL SISTEMA
        createRol();

    }

     void  createRol() {
        /*
         List<AuthorityEntity> list = authorityService.findAll();
         List<AuthorityEntity> rol = new ArrayList<>();
         AuthorityEntity roleAdmin = new AuthorityEntity();
         roleAdmin.setName("ROLE_ADMIN");
         rol.add(roleAdmin);
         if(list.indexOf(rol) == -1) {
             roleAdmin.setName("ROLE_ADMIN");
             authorityService.save(roleAdmin);
         }
         if(list.indexOf("ROLE_USERS") == -1) {
             AuthorityEntity roleUsers = new AuthorityEntity();
             roleUsers.setName("ROLE_USERS");
             authorityService.save(roleUsers);
         }
         if(list.indexOf("ROLE_VENDEDOR") == -1) {
             AuthorityEntity roleVendedor = new AuthorityEntity();
             roleVendedor.setName("ROLE_VENDEDOR");
             authorityService.save(roleVendedor);
         }

         */
    }
    void  tipoVehiculo() {
        AuthorityEntity roleAdmin = new AuthorityEntity();
        roleAdmin.setName("ROLE_ADMIN");
        AuthorityEntity roleUsers = new AuthorityEntity();
        roleUsers.setName("ROLE_USERS");
        authorityService.save(roleAdmin);
        authorityService.save(roleUsers);
    }
}
