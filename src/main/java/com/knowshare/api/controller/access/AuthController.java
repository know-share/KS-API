/**
 * 
 */
package com.knowshare.api.controller.access;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.api.security.JWTFilter;
import com.knowshare.dto.perfilusuario.AuthDTO;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;
import com.knowshare.enterprise.repository.app.UserSessionRepository;
import com.knowshare.entities.app.UserSession;
import com.knowshare.entities.perfilusuario.Usuario;

/**
 * @author Miguel Montañez
 *
 */

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UsuarioFacade usuarioBean;
	
	@Autowired
	private UserSessionRepository userSessionRepository;
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ResponseEntity<Object> login(@RequestBody AuthDTO dto) throws Exception{
		logger.debug(":::: Start method login() in authController ::::");
		if(dto != null && ( dto.getUsername() != null && dto.getPassword()!=null )){
			Usuario usuario = usuarioBean.login(dto.getUsername(), dto.getPassword()); 
			if(null != usuario){
				dto.setUsername(usuario.getUsername());
				UserSession us = JWTFilter.generateToken(dto);
				if(null == us)
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body(null);
				userSessionRepository.insert(us);
				Map<String, String> map = new HashMap<>();
				map.put("token", us.getToken());
				map.put("role", usuario.getTipo().name());
				return ResponseEntity.status(HttpStatus.OK).body(map);
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@RequestMapping(value="logout", method=RequestMethod.PUT)
	public ResponseEntity<Object> logout(
			@RequestHeader("Authorization") String token){
		// remove token from db
		Long number = userSessionRepository.removeByToken(token);
		if(number == 1)
			return ResponseEntity.ok(null);
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
				.body(null);
	}
}
