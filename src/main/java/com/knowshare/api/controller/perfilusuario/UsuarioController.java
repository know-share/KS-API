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
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UsuarioFacade usuarioBean;

	@RequestMapping(value = "isUsernameTaken", method = RequestMethod.GET)
	public ResponseEntity<Boolean> isUsernameTaken(@RequestParam String username) {
		logger.debug(":::: Start method isUsernameTaken(String) in UsuarioController ::::");
		if (username == null) {
			logger.error(":::: Error parameter username is not in the request ::::");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(usuarioBean.isUsernameTaken(username));
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDTO dto) {
		logger.debug(":::: Start method crearUsuario(UsuarioDTO) in UsuarioController ::::");
		if (dto == null) {
			logger.error(":::: Error parameter dto is not in the request ::::");
			return ResponseEntity.badRequest().body(null);
		}
		if (usuarioBean.crearUsuario(dto))
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
