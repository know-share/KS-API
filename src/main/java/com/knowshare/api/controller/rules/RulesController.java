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
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.api.security.JWTFilter;
import com.knowshare.enterprise.bean.rules.config.RulesAdminFacade;
import com.knowshare.enterprise.bean.rules.usuarios.RecomendacionConexionFacade;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;
import com.knowshare.enterprise.repository.app.UserSessionRepository;
import com.knowshare.entities.app.UserSession;
import com.knowshare.entities.perfilusuario.InfoUsuario;

/**
 * Controlador para reglas de negocio dentro de la aplicación de
 * KnowShare
 * @author miguel
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/rules")
public class RulesController {
	
	@Autowired
	private RecomendacionConexionFacade ruleTest;
	
	@Autowired
	private RulesAdminFacade rulesAdminBean;
	
	@Autowired
	private UserSessionRepository userSessionRepository;
	
	@Autowired
	private UsuarioFacade bean;
	
	/**
	 * Controller aún en pruebas
	 * @param token
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/recomendacionConexiones", method=RequestMethod.GET)
	public ResponseEntity<?> testMethodRules(
			@RequestHeader("Authorization") String token){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}
		String username = JWTFilter.getSub(token, user.getSecretKey());
		if(!username.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		List<InfoUsuario> recomendaciones = (List<InfoUsuario>)ruleTest
				.recomendacionesUsuario(bean.getUsuario("MinMiguelM"));
		if(recomendaciones.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		return ResponseEntity.ok(recomendaciones);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.GET)
	public void updateRules(){
		rulesAdminBean.updateRules();
	}
}
