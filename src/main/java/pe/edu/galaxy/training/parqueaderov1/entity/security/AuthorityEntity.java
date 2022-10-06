package pe.edu.galaxy.training.parqueaderov1.entity.security;

import lombok.*;

import javax.persistence.*;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_authority")
@ToString

public class AuthorityEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_authority")
	private Long id;
	@Column(name = "nombre")
	private String name;

}
