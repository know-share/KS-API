/**
 * 
 */
package com.knowshare.api.controller.perfilusuario;

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
import com.knowshare.enterprise.bean.personalidad.PersonalidadFacade;
import com.knowshare.enterprise.repository.app.UserSessionRepository;
import com.knowshare.entities.app.UserSession;
import com.knowshare.entities.perfilusuario.Personalidad;

/**
 * @author miguel
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/personalidad")
public class PersonalidadController{
	
	@Autowired
	private PersonalidadFacade personalidadBean;
	
	@Autowired
	private UserSessionRepository userSessionRepository;
	
	@RequestMapping(value="/findAll", method=RequestMethod.GET)
	public ResponseEntity<List<Personalidad>> getAllPersonalidades(
			@RequestHeader("Authorization") String token) throws Exception{
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}
		
		List<Personalidad> personalidades = personalidadBean.getAllPersonalidades();
		if(personalidades == null || personalidades.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(personalidades);
	}

}
