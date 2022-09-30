package pe.edu.galaxy.training.parqueaderov1.controller.seguridad;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.edu.galaxy.training.parqueaderov1.entity.PersonaEntity;
import pe.edu.galaxy.training.parqueaderov1.entity.security.UsuarioEntity;
import pe.edu.galaxy.training.parqueaderov1.service.general.security.UserDetailsImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/* login */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {

			//System.out.println("usuario " + request.getInputStream());

			UsuarioEntity usuarioEntity = new ObjectMapper().readValue(request.getInputStream(), UsuarioEntity.class); // leo el json que obtengo y lo mapeo a la clase UsuarioEntity

			//System.out.println("usuario Entity:" + usuarioEntity);
			UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(usuarioEntity.getUsuario(), usuarioEntity.getClave()); // verifico si existe una autoenticacion y si los campos usuario y password estan siendo enviados

			 //System.out.println("upat: " + upat);
			 //System.out.println("usuarioEntity.getClave()" + usuarioEntity.getClave());
			Authentication aut = authenticationManager.authenticate(upat); // si los datos no son correctos envia un error fordiben 403

			//System.out.println("aut: "+aut);

			return aut;

		} catch (IOException e) {
			System.out.println("attemptAuthentication " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
    // solo entrara aqui si la autoenticacion fue correcta
	@Transactional
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		try {
			byte signingKey[] = Constants.SUPER_SECRET_KEY.getBytes();
			// System.out.println("signingKey" + Constants.SUPER_SECRET_KEY.getBytes());

			// principal hace referencia al objeto de la clase usuario entity
			UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
			// System.out.println("auth.getPrincipal()" + ((UserDetails) auth.getPrincipal()).getPassword()); //

			// System.out.println("User -> "+ userDetails.getUsername());

			Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities(); // obtengo los roles(autorizaciones) asignados al usuario logueado

			//System.out.println("Authorities -> "+ authorities); // si solo lo envio esta coleccion el token me agrega los roles como un array de objetos

			Collection<?> authoritiesItems = authorities.stream()
					.map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList()); // si envio asi lo que hago es agregar al token los roles en un array ya no en un array de objetos
			PersonaEntity  persona = userDetails.getUsuarioEntity().getPersona();
			UsuarioEntity  usuario = userDetails.getUsuarioEntity();
			LinkedHashMap person = new LinkedHashMap();
			person.put("nombre",persona.getNombre());
			person.put("telefono",persona.getTelefono());
			person.put("dni",persona.getNumeroDocumento());

			LinkedHashMap users = new LinkedHashMap();
			users.put("id_users", usuario.getId());
			users.put("username", usuario.getUsuario());
			users.put("person", person);

			// aqui armo el token
			LinkedHashMap usuarioAuth = new LinkedHashMap();
			usuarioAuth.put(Constants.AUTHORITIES, authoritiesItems);
			usuarioAuth.put("usuario", users);
			System.out.println("map" + usuarioAuth);
			System.out.println("signingKey" + signingKey);
			String token = Jwts.builder()
					.setIssuedAt(new Date())
					.setIssuer(Constants.ISSUER_INFO)
					.setSubject(userDetails.getUsername())
					.claim("data", usuarioAuth)
					.setExpiration(new Date(System.currentTimeMillis() + Constants.TOKEN_EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS512, signingKey)
					.compact();

			//System.out.println("token " + token);
			response.addHeader("access-control-expose-headers", "Authorization");

			response.addHeader(Constants.HEADER_AUTHORIZACION_KEY, Constants.TOKEN_BEARER_PREFIX + " " + token);
		} catch (RuntimeException e) {
			System.out.println("Error al construir el token " + e.getMessage());
			throw new IOException(e);
		}
	}

}
