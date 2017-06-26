/**
 * 
 */
package com.knowshare.api.controller.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.dto.perfilusuario.AuthDTO;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;

/**
 * @author miguel
 *
 */

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UsuarioFacade usuarioBean;

	@RequestMapping(value="/login", method=RequestMethod.PUT)
	public ResponseEntity<?> login(@RequestBody AuthDTO dto){
		logger.debug(":::: Start method logn() in authController ::::");
		if(dto != null){
			if(dto.getUsername() != null || dto.getPassword()!=null){
				if(usuarioBean.login(dto.getUsername(), dto.getPassword())){
					return ResponseEntity.status(HttpStatus.OK).body(null);
				}
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
}
