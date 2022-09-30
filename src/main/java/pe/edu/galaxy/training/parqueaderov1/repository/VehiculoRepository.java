package pe.edu.galaxy.training.parqueaderov1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import pe.edu.galaxy.training.parqueaderov1.dto.VehiculoDto;
import pe.edu.galaxy.training.parqueaderov1.entity.VehiculoEntity;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<VehiculoEntity, Long>, JpaSpecificationExecutor<VehiculoEntity> {
    @Transactional
    @Modifying
    @Query("update VehiculoEntity SET horaSalida =:horaSalida, fechaSalida=:fechaSalida, estado = '0' where id =:id")
    void salida(@Param("horaSalida") LocalTime horaSalida, @Param("fechaSalida") Date fechaSalida , @Param("id") long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE tb_vehicle SET status_delete='0' WHERE id_vehicle=:id")
    void deleteLogic(@Param("id") Long id);
    Optional<VehiculoEntity> findByCodigoOperacionAndEstado(String codigo, char estado);
    Optional<VehiculoEntity> findByCodigoOperacion(String codigo);
   // sqlNativo
    // @Query(nativeQuery = true, value = "select * from tb_vehicle where cedula=:cedula and id_person=:id")
    boolean existsByCedula(String cedula);
    Optional<VehiculoEntity> findByPlaca(String placa);
    @Query(value = "call ingresarVehiculo(" +
            ":placaVehiculo, :cedulaVehiculo, :tipoVehiculo, :horaEntrada, :nombreCliente," +
            ":telefono, :numeroDocumento, :direccion)", nativeQuery = true)
    Integer ingresoVehiculo(
            @Param("placaVehiculo") String placaVehiculo,
            @Param("cedulaVehiculo") String cedulaVehiculo,
            @Param("tipoVehiculo") Long tipoVehiculo,
            @Param("horaEntrada") String horaEntrada,
            @Param("nombreCliente") String nombreCliente,
            @Param("telefono") String telefono,
            @Param("numeroDocumento") String numeroDocumento,
            @Param("direccion") String direccion
    );
    @Query(value = "SELECT COUNT(id) as total FROM VehiculoEntity")
    Integer totalClientes();
    @Query(value = "select COUNT(*) from tb_vehicle where DATE(date_register) = :date and status = '1'", nativeQuery = true)
    Integer findByFechaRegistroAndEstado(@Param("date") String hoy);

    @Query(value = "SELECT * FROM tb_vehicle where DATE_FORMAT(date_register, '%Y-%m') = :fecha", nativeQuery = true)
    Page<VehiculoEntity> findByFechaRegistro(Pageable pageable,  @Param("fecha") String hoy);
    @Query(value = "SELECT v FROM VehiculoEntity v where year (v.fechaRegistro) = :fecha")
    Page<VehiculoEntity> findByFechaRegistro1(Pageable pageable,  @Param("fecha") String hoy);
}
