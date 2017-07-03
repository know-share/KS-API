/**
 * 
 */
package com.knowshare.api.controller.idea;

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
import com.knowshare.dto.perfilusuario.UsuarioDTO;
import com.knowshare.enterprise.bean.idea.IdeaFacade;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;
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
	
	@Autowired
	private UsuarioFacade usuarioBean;
	
	@RequestMapping(value="/crear/{username}" ,method = RequestMethod.POST)
	public ResponseEntity<?> crearIdea(@PathVariable String username,
			@RequestBody IdeaDTO idea,
			@RequestHeader("Authorization") String token){
		UserSession sesion = userSessionRepository.findByToken(token);
		if(!JWTFilter.validateToken(token, sesion.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		if(!usuarioBean.isUsernameTaken(username)){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		UsuarioDTO usuario = usuarioBean.getUsuario(username);
		idea.setUsuario(usuario);
		if(ideaBean.crearIdea(idea) != null){
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		
	}
}
