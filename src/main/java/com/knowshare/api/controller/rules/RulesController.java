/**
 * 
 */
package com.knowshare.api.controller.rules;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.api.security.JWTFilter;
import com.knowshare.dto.rules.RecomendacionDTO;
import com.knowshare.enterprise.bean.rules.busqueda.BusquedaUsuarioFacade;
import com.knowshare.enterprise.bean.rules.config.RulesAdminFacade;
import com.knowshare.enterprise.bean.rules.usuarios.RecomendacionConexionFacade;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;
import com.knowshare.enterprise.repository.app.UserSessionRepository;
import com.knowshare.entities.app.UserSession;

/**
 * Controlador para reglas de negocio dentro de la aplicación de
 * KnowShare
 * @author Miguel Montañez
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/rules")
public class RulesController {
	
	@Autowired
	private RecomendacionConexionFacade recomendacionesBean;
	
	@Autowired
	private BusquedaUsuarioFacade busquedaBean;
	
	@Autowired
	private RulesAdminFacade rulesAdminBean;
	
	@Autowired
	private UserSessionRepository userSessionRepository;
	
	@Autowired
	private UsuarioFacade usuarioBean;
	
	/**
	 * Obtiene las recomendaciones para el usuario actual.
	 * @param token para validar la autenticidad del cliente.
	 * @return Lista de recomendaciones
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/recomendacionConexiones", method=RequestMethod.GET)
	public ResponseEntity<?> getRecomendaciones(
			@RequestHeader("Authorization") String token){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}
		String username = JWTFilter.getSub(token, user.getSecretKey());
		if(!username.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		final List<RecomendacionDTO> recomendaciones = (List<RecomendacionDTO>)recomendacionesBean
				.setDeRecomendaciones(usuarioBean.getUsuario(username));
		if(recomendaciones.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		return ResponseEntity.ok(recomendaciones);
	}
	
	/**
	 * Busca usuario según el filtro y el parámetro de entrada.
	 * @param token para validar la autenticidad del cliente.
	 * @param filtro los posibles valores son: HABILIDAD, AREA y
	 * NOMBRE
	 * @param param el string a buscar.
	 * @return Lista con los usuarios a buscar
	 */
	@RequestMapping(value="/buscarUsuario",method=RequestMethod.GET)
	public ResponseEntity<?> buscarUsuario(
			@RequestHeader("Authorization") String token,
			@RequestParam(defaultValue="NOMBRE") String filtro,
			@RequestParam String param){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}
		String username = JWTFilter.getSub(token, user.getSecretKey());
		if(!username.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		if(null == param || param.isEmpty())
			return ResponseEntity.badRequest().body(null);
		final List<RecomendacionDTO> busqueda = busquedaBean
				.buscarUsuario(usuarioBean.getUsuario(username), filtro, param);
		if(null == busqueda || busqueda.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		return ResponseEntity.ok(busqueda);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.GET)
	public void updateRules(){
		rulesAdminBean.updateRules();
	}
}
