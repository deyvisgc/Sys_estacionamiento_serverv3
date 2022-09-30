package pe.edu.galaxy.training.parqueaderov1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.galaxy.training.parqueaderov1.entity.security.AuthorityEntity;
@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
}
