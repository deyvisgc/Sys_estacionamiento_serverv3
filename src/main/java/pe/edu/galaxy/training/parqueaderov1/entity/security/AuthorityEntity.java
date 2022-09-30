package pe.edu.galaxy.training.parqueaderov1.entity.security;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "tb_authority")
@ToString
@Data
public class AuthorityEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_authority")
	private Long id;
	@Column(name = "nombre")
	private String name;

}
