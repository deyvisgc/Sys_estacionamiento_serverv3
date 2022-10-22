package pe.edu.galaxy.training.parqueaderov1.service.general.impl.ps;

import org.springframework.stereotype.Repository;
import pe.edu.galaxy.training.parqueaderov1.dto.UsuarioDto;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.ps.ServicePS;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

@Repository
public class ServicePsImpl implements ServicePS {
    @PersistenceContext
    EntityManager manager;
    // con este metodo voy aplicar el llamado del procedure por medio de la anotacion @Procedure
    @Override
    public void updateusersusp(UsuarioDto usuarioDto) throws ServiceException {
        try {
            System.out.println("entro aquissssss");
            StoredProcedureQuery ps = manager.createNamedStoredProcedureQuery("use.updateUser");
            ps.setParameter("id", (int) usuarioDto.getId());
            ps.setParameter("nombre", usuarioDto.getPerson().getName());
            ps.setParameter("telefono", usuarioDto.getPerson().getPhone());
            ps.setParameter("numerodocumento", usuarioDto.getPerson().getNumber());
            ps.setParameter("correo", usuarioDto.getPerson().getGmail());
            ps.setParameter("users_name", usuarioDto.getUser_name());
            ps.setParameter("direccion", usuarioDto.getPerson().getAddres());
            ps.execute();
        } catch (Exception e) {
            System.out.println("e: " + e.getMessage());
            throw new ServiceException(e);
        }
    }
}
