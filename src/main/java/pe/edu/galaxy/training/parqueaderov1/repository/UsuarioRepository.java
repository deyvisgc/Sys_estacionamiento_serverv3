package pe.edu.galaxy.training.parqueaderov1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.galaxy.training.parqueaderov1.dto.AuthorityDto;
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
}