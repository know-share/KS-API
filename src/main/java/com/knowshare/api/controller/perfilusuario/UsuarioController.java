/**
 * 
 */
package com.knowshare.api.controller.perfilusuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.api.security.JWTFilter;
import com.knowshare.dto.perfilusuario.UsuarioDTO;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;
import com.knowshare.enterprise.repository.app.UserSessionRepository;
import com.knowshare.entities.academia.FormacionAcademica;
import com.knowshare.entities.academia.TrabajoGrado;
import com.knowshare.entities.app.UserSession;

/**
 * @author miguel
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/usuario")
public class UsuarioController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UsuarioFacade usuarioBean;
	
	@Autowired
	private UserSessionRepository userSessionRepository;

	@RequestMapping(value = "isUsernameTaken", method = RequestMethod.GET)
	public ResponseEntity<Boolean> isUsernameTaken(@RequestParam String username) {
		logger.debug(":::: Start method isUsernameTaken(String) in UsuarioController ::::");
		if (username == null || username.isEmpty()) {
			logger.error(":::: Error parameter username is not in the request ::::");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(usuarioBean.isUsernameTaken(username));
	}

	@RequestMapping(value = "", method = RequestMethod.POST, consumes="application/json")
	public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDTO dto) {
		logger.debug(":::: Start method crearUsuario(UsuarioDTO) in UsuarioController ::::");
		if (dto == null) {
			logger.error(":::: Error parameter dto is not in the request ::::");
			return ResponseEntity.badRequest().body(null);
		}
		if (usuarioBean.crearUsuario(dto))
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		return ResponseEntity.status(HttpStatus.OK).body(false);
	}
	
	@RequestMapping(value = "/seguir/{usernameObj:.+}", method = RequestMethod.PUT)
	public ResponseEntity<?> seguir(
			@RequestHeader("Authorization") String token,
			@PathVariable String usernameObj){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}
		String username = JWTFilter.getSub(token, user.getSecretKey());
		if(!username.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		if(usuarioBean.isUsernameTaken(usernameObj)){
			if(usuarioBean.seguir( username,usernameObj)){
				return ResponseEntity.status(HttpStatus.OK).body(null);
			}else
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
	}
	
	@RequestMapping(value = "/dejarseguir/{usernameObj:.+}", method = RequestMethod.PUT)
	public ResponseEntity<?> unfollow(
			@RequestHeader("Authorization") String token,
			@PathVariable String usernameObj){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}
		String username = JWTFilter.getSub(token, user.getSecretKey());
		if(!username.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		if(usuarioBean.isUsernameTaken(usernameObj)){
			if(usuarioBean.dejarSeguir( username,usernameObj)){
				return ResponseEntity.status(HttpStatus.OK).body(null);
			}else
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
	}
	
	/**
	 * 
	 * @param token
	 * @param usernameObj
	 * @param action puede ser: ACCEPT or REJECT
	 * @return
	 */
	@RequestMapping(value = "/solicitud/{usernameObj:.+}", method = RequestMethod.PUT)
	public ResponseEntity<?> solicitudAmistad(
			@RequestHeader("Authorization") String token,
			@PathVariable String usernameObj,
			@RequestParam(name="action",required=false) String action){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}
		String username = JWTFilter.getSub(token, user.getSecretKey());
		if(!username.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		if(usuarioBean.isUsernameTaken(usernameObj)){
			if(null == action)
				if(usuarioBean.solicitudAmistad(username, usernameObj)){
					return ResponseEntity.status(HttpStatus.OK).body(null);
				}else
					return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
			else{
				if(action.equalsIgnoreCase("accept") || action.equalsIgnoreCase("reject")){
					if(usuarioBean.accionSolicitud(username, usernameObj, action))
						return ResponseEntity.status(HttpStatus.OK).body(null);
					return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	@RequestMapping(value="/get/{username:.+}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<UsuarioDTO> getUsuario(
			@RequestHeader("Authorization") String token,
			@PathVariable String username){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}
		String usernameToken = JWTFilter.getSub(token, user.getSecretKey());
		if(!usernameToken.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		if (username == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		UsuarioDTO usuario = usuarioBean.getUsuario(username);
		if(usuario == null)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		return ResponseEntity.ok(usuario);
	}
	
	@RequestMapping(value="/addTG", method=RequestMethod.POST)
	public ResponseEntity<?> addTG(
			@RequestHeader("Authorization") String token,
			@RequestBody TrabajoGrado tg){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}
		String usernameToken = JWTFilter.getSub(token, user.getSecretKey());
		if(!usernameToken.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		if(tg == null)
			return ResponseEntity.badRequest().body(null);
		if(usuarioBean.agregarTGDirigido(tg, usernameToken))
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value ="/addFormacionAcademica", method=RequestMethod.POST)
	public ResponseEntity<?> addFormacionAcademica(
			@RequestHeader("Authorization") String token,
			@RequestBody FormacionAcademica fa){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}
		String usernameToken = JWTFilter.getSub(token, user.getSecretKey());
		if(!usernameToken.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		if(fa == null)
			return ResponseEntity.badRequest().body(null);
		if(usuarioBean.agregarFormacionAcademica(fa, usernameToken))
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="/eliminarAmigo/{username:.+}", method =RequestMethod.PUT)
	public ResponseEntity<?> eliminarAmigo(
			@RequestHeader("Authorization") String token,
			@PathVariable String username){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}
		String usernameToken = JWTFilter.getSub(token, user.getSecretKey());
		if(!usernameToken.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		if(username == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		if(usuarioBean.eliminarAmigo(usernameToken, username))
			return ResponseEntity.status(HttpStatus.OK).body(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
