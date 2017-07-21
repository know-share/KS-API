/**
 * 
 */
package com.knowshare.api.controller.idea;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.api.security.JWTFilter;
import com.knowshare.dto.idea.IdeaDTO;
import com.knowshare.enterprise.bean.idea.IdeaFacade;
import com.knowshare.enterprise.repository.app.UserSessionRepository;
import com.knowshare.entities.app.UserSession;

/**
 * @author Pablo Gaitan
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/idea")
public class IdeaController {
	
	@Autowired
	private IdeaFacade ideaBean;
	
	@Autowired
	private UserSessionRepository userSessionRepository;
	
	
	@RequestMapping(value="/crear" ,method = RequestMethod.POST)
	public ResponseEntity<?> crearIdea(@RequestBody IdeaDTO idea,
			@RequestHeader("Authorization") String token){
		UserSession sesion = userSessionRepository.findByToken(token);
		if(sesion == null|| !JWTFilter.validateToken(token, sesion.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		String username = JWTFilter.getSub(token, sesion.getSecretKey());
		if(!username.equalsIgnoreCase(sesion.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		idea.setUsuario(username);
		IdeaDTO crear = ideaBean.crearIdea(idea);
		if(crear!= null){
			return ResponseEntity.status(HttpStatus.OK).body(crear);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		
	}
	
	@RequestMapping(value = "/findByUsuario/{usernameObj:.+}",method=RequestMethod.GET)
	public ResponseEntity<?> findByUsuario(@RequestHeader("Authorization") String token,
			@PathVariable String usernameObj){
		UserSession sesion = userSessionRepository.findByToken(token);
		if(sesion == null || !JWTFilter.validateToken(token, sesion.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		String username = JWTFilter.getSub(token, sesion.getSecretKey());
		if(!username.equalsIgnoreCase(sesion.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		List<IdeaDTO> ret = ideaBean.findByUsuario(username);
		if(!ret.isEmpty()){
			return ResponseEntity.status(HttpStatus.OK).body(ret);
		}
		if(ret.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);	
	}
}
