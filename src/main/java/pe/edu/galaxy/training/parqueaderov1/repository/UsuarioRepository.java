package pe.edu.galaxy.training.parqueaderov1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.galaxy.training.parqueaderov1.entity.security.AuthorityEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.security.UsuarioEntity;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE tb_users SET status_users='0' WHERE id_users=:id")
    void deleteLogic(@Param("id") Long id);
    Page<UsuarioEntity> findAllByEstado(Pageable pageable, char estado);
    UsuarioEntity findByUsuarioAndEstado(String usuario, char estado); // Spring Security
    List<UsuarioEntity> findAllByEstado(char estado);
    List<AuthorityEntity> findByAuthorities(Long id);
    @Query(value = "SELECT COUNT(id) as total FROM UsuarioEntity ")
    Integer totalUsers();
    UsuarioEntity findByPersona_Email(String correo);
    @Transactional
    @Modifying
    @Query(value = "UPDATE UsuarioEntity SET tokenPassword =:token where id =:id")
    void updateTokenPassword(@Param("token") String tokenPassword, @Param("id") long id);
    UsuarioEntity findByTokenPassword(String token);

    @Transactional
    @Modifying
    @Query(value = "UPDATE UsuarioEntity SET tokenPassword = null, clave =:password where id =:id")
    void changePassword(@Param("password") String password, @Param("id") long id);
    @Transactional
    @Procedure(name = "UsuarioEntity.updateUsers")
     void updateUSP(@Param("id") int id, @Param("nombre") String nombre, @Param("telefono") String telefono,
                   @Param("numerodocumento") String numerodocumento, @Param("correo") String correo,@Param("users_name")  String users_name,
                   @Param("direccion")  String direccion);
    //@Procedure(procedureName = "con_nombreSP", outputParameterName = "out_parameter") public double nameMethod (@Param ("in_param") double param, @Param ("in_param2") double param2, @Param ( "in_param3") int param3);
}
