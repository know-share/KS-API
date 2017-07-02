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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.dto.perfilusuario.UsuarioDTO;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;

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
	
	/**
	 * This method is not available for testing and production
	 * @param usernameSol
	 * @param usernameObj
	 * @return
	 */
	@RequestMapping(value = "/seguir/{usernameSol}/{usernameObj}", method = RequestMethod.GET)
	public ResponseEntity<?> seguir(@PathVariable String usernameSol,@PathVariable String usernameObj){
		if(usuarioBean.isUsernameTaken(usernameSol) && usuarioBean.isUsernameTaken(usernameObj)){
			if(usuarioBean.seguir(usernameSol, usernameObj)){
				return ResponseEntity.status(HttpStatus.OK).body(null);
			}else
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); 
	}
	
	@RequestMapping(value = "/solicitud/{usernameSol}/{usernameObj}", method = RequestMethod.GET)
	public ResponseEntity<?> solicitudAmistad(@PathVariable String usernameSol,@PathVariable String usernameObj){
		if(usuarioBean.isUsernameTaken(usernameSol) && usuarioBean.isUsernameTaken(usernameObj)){
			if(usuarioBean.solicitudAmistad(usernameSol, usernameObj)){
				return ResponseEntity.status(HttpStatus.OK).body(null);
			}else
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); 
	}
	
	@RequestMapping(value="/get/{username:.+}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable String username){
		if (username == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		UsuarioDTO usuario = usuarioBean.getUsuario(username);
		if(usuario == null)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		return ResponseEntity.ok(usuario);
	}
}
