package pe.edu.galaxy.training.parqueaderov1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.galaxy.training.parqueaderov1.entity.TipoVehiculoEntity;

import javax.transaction.Transactional;

@Repository
public interface TipoVehiculoRepository extends JpaRepository<TipoVehiculoEntity, Long> {
    Page<TipoVehiculoEntity> findAllByStatus(Pageable pageable, char estado);
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE tb_type_vehicle SET status='0' WHERE id_type_vehicle=:id")
    void deleteLogic(@Param("id") Long id);
}
