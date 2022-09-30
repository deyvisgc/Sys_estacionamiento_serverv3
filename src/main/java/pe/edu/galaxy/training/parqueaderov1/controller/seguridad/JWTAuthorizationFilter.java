package pe.edu.galaxy.training.parqueaderov1.controller.seguridad;

import com.google.gson.JsonObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pe.edu.galaxy.training.parqueaderov1.entity.security.UsuarioEntity;
import pe.edu.galaxy.training.parqueaderov1.service.general.security.UserDetailsServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import static java.util.Objects.isNull;
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  public JWTAuthorizationFilter(AuthenticationManager authManager) {
    super(authManager);
  }

  // Cada peticion- request
  //@Autowired
  //UserDetailsServiceImpl userDetailsService;
  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
	  
   //System.out.println("doFilterInternal...x");
   
    String header = req.getHeader(Constants.HEADER_AUTHORIZACION_KEY);
    
    if (header == null || !header.startsWith(Constants.TOKEN_BEARER_PREFIX)) {
      System.out.println("no existe token");
      chain.doFilter(req, res);
      return;
    }
    //UserDetails userDetails = userDetailsService.loadUserByUsername("leslygc");
    //System.out.println("doFilterInternal...y:  " + userDetails);
    
    UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
    
    SecurityContextHolder.getContext().setAuthentication(authentication);
    
    chain.doFilter(req, res);
  }
  

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
	  
    String token = request.getHeader(Constants.HEADER_AUTHORIZACION_KEY);
    if (token != null) {

      byte signingKey[] = Constants.SUPER_SECRET_KEY.getBytes();

      // Se procesa el token y se recupera el usuario.

      String tokenValue=token.replace(Constants.TOKEN_BEARER_PREFIX, "");
      // System.out.println("tokenValue" + tokenValue);

      String user = Jwts.parser()
            .setSigningKey(signingKey)
            .parseClaimsJws(tokenValue)
            .getBody()
            .getSubject();
     // System.out.println("users: " + user);

      //System.out.println("userDetails" + userDetails.getPassword());
      /*
      if (isNull(userDetails)) {
        System.out.println("entro");
        return null;
      }

       */
      //System.out.println("user -> "+user);
      
      Collection<? extends GrantedAuthority> authorities=getAuthorities(tokenValue);
      
      //System.out.println("authorities -> "+authorities);
      
      List<GrantedAuthority> authoritiesItems      = new ArrayList<>();
      for (Object authority : authorities.toArray()) {
    	  //System.out.println("authority -> "+authority);
    	  authoritiesItems.add(new SimpleGrantedAuthority(authority.toString()));
      }

      
      //System.out.println("authoritiesItems -> "+authoritiesItems);
      
      if (user != null) {
        return new UsernamePasswordAuthenticationToken(user, null,authoritiesItems);
      }
    }
    return null;
  }
  
  public Collection<? extends GrantedAuthority> getAuthorities(String token) {
	  byte signingKey[] = Constants.SUPER_SECRET_KEY.getBytes();
      Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
      HashMap<String, Object> objectToken = (HashMap<String, Object>) claims.get("data"); // obtengo el cuerpo del json para obtener las autorizaciones
      return (Collection<? extends GrantedAuthority>) objectToken.get("authorities");
  }
}