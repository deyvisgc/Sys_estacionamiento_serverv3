package pe.edu.galaxy.training.parqueaderov1.entity.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.galaxy.training.parqueaderov1.entity.PersonaEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tb_users")
// @JsonPropertyOrder({ "id", "usuario"/* ,"clave" */, "nombre", "estado" })
public class UsuarioEntity {
    @Id
    @Column(name = "id_users")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "username", unique = true)
    private String usuario;
    @Column(name = "password_users", updatable = false)
    //@JsonIgnore
    private String clave;
    @Column(name = "status_users", length = 1)
    @Builder.Default
    private char estado = '1';
    @Column(name = "token_password")
    private String tokenPassword;
    @ManyToOne
    @JoinColumn(name = "id_person",  nullable = false)
    private PersonaEntity persona;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TBL_USUARIO_AUTHORITY", joinColumns = { @JoinColumn(name = "id_users") }, inverseJoinColumns = {
            @JoinColumn(name = "id_authority") })
    private Set<AuthorityEntity> authorities = new HashSet<>();



}
