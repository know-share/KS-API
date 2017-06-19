/**
 * 
 */
package com.knowshare.api.controller.perfilusuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.enterprise.bean.usuario.UsuarioFacade;

/**
 * @author miguel
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioFacade usuarioBean;
	
	@RequestMapping(value="isUsernameTaken", method=RequestMethod.GET)
	public ResponseEntity<Boolean> isUsernameTaken(
			@RequestParam String username){
		if(username == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(usuarioBean.isUsernameTaken(username));
	}

}
