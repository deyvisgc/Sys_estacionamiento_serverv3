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
            "        when cast(substring(codigo_tarifa, 6) as int) < 9 AND cast(substring(codigo_tarifa, 6) as int)  <> 0  then concat('CT000',(cast(SUBSTRING(codigo_tarifa, 6) as int) + 1))\n" +
            "        when cast(substring(codigo_tarifa, 5) as int) < 99 AND cast(substring(codigo_tarifa, 5) as int)  <> 0 then concat('CT00',(cast(SUBSTRING(codigo_tarifa, 5) as int) + 1))\n" +
            "        when cast(substring(codigo_tarifa, 4) as int) < 999 AND cast(substring(codigo_tarifa, 4) as int) <> 0 then concat('CT0',(cast(SUBSTRING(codigo_tarifa, 4) as int) + 1))\n" +
            "        when cast(substring(codigo_tarifa, 3) as int) < 9999 AND cast(substring(codigo_tarifa, 3) as int) <> 0 then concat('CT',(cast(SUBSTRING(codigo_tarifa, 3) as int) + 1))\n" +
            "        ELSE 'terminado'\n" +
            "    END from tb_tarifa order by id_tarifa desc limit 1;", nativeQuery = true)
    String obtenerCodigoTarifa();
    TarifaEntity findByVehiculoEntity(VehiculoEntity vehiculo);
    @Query(value = "SELECT SUM(montoPagar) as total FROM TarifaEntity")
    Double totalGanancia();
    @Query(value = "SELECT COUNT(id) as total FROM TarifaEntity")
    Integer totalComprobantes();

    @Query(value = "SELECT * FROM tb_tarifa where to_char(fecha_registro, 'YYYY-MM') = :fecha", nativeQuery = true)
    Page<TarifaEntity> findByFechaRegistro(Pageable pageable, @Param("fecha") String date);

}
