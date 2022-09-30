package pe.edu.galaxy.training.parqueaderov1.service.general.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.galaxy.training.parqueaderov1.entity.security.UsuarioEntity;
import pe.edu.galaxy.training.parqueaderov1.repository.UsuarioRepository;

import static java.util.Objects.isNull;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("primer ejecutado loadUserByUsername de la clase UserDetailsServiceImpl.");

		UsuarioEntity usuarioEntity= this.usuarioRepository.findByUsuarioAndEstado(username, '1');
		if (isNull(usuarioEntity)) {
			throw new UsernameNotFoundException("Usuario no existe");
		}
		return new UserDetailsImpl(usuarioEntity);
		
		//return new User(usuarioEntity.getUsuario(),usuarioEntity.getClave(),emptyList());
	}

}
