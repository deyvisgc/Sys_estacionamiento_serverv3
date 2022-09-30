package pe.edu.galaxy.training.parqueaderov1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.galaxy.training.parqueaderov1.entity.TarifaEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.VehiculoEntity;

public interface TarifaRepository extends JpaRepository<TarifaEntity, Long>, JpaSpecificationExecutor<TarifaEntity> {
    @Query(value = "select\n" +
            "    CASE\n" +
            "        when SUBSTRING(codigo_tarifa, 6) < 9 AND SUBSTRING(codigo_tarifa, 6) <> 0  then concat('CT000',(SUBSTRING(codigo_tarifa, 6) + 1))\n" +
            "        when SUBSTRING(codigo_tarifa, 5) < 99 AND SUBSTRING(codigo_tarifa, 5) <> 0 then concat('CT00',(SUBSTRING(codigo_tarifa, 5) + 1))\n" +
            "        when SUBSTRING(codigo_tarifa, 4) < 999 AND SUBSTRING(codigo_tarifa, 4) <> 0 then concat('CT0',(SUBSTRING(codigo_tarifa, 4) + 1))\n" +
            "        when SUBSTRING(codigo_tarifa, 3) < 9999 AND SUBSTRING(codigo_tarifa, 3) <> 0 then concat('CT',(SUBSTRING(codigo_tarifa, 3) + 1))\n" +
            "        ELSE 'terminado'\n" +
            "    END from tb_tarifa order by id_tarifa desc limit 1;", nativeQuery = true)
    String obtenerCodigoTarifa();
    TarifaEntity findByVehiculoEntity(VehiculoEntity vehiculo);
    @Query(value = "SELECT SUM(montoPagar) as total FROM TarifaEntity")
    Double totalGanancia();
    @Query(value = "SELECT COUNT(id) as total FROM TarifaEntity")
    Integer totalComprobantes();

    @Query(value = "SELECT * FROM tb_tarifa where DATE_FORMAT(fecha_registro, '%Y-%m') = :fecha", nativeQuery = true)
    Page<TarifaEntity> findByFechaRegistro(Pageable pageable, @Param("fecha") String date);

}
