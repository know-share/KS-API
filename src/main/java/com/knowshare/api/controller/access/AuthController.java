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

/**
 * @author miguel
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
	public ResponseEntity<?> login(@RequestBody AuthDTO dto) throws Exception{
		logger.debug(":::: Start method login() in authController ::::");
		if(dto != null){
			if(dto.getUsername() != null || dto.getPassword()!=null){
				if(usuarioBean.login(dto.getUsername(), dto.getPassword())){
					UserSession us = JWTFilter.generateToken(dto);
					userSessionRepository.insert(us);
					Map<String, String> map = new HashMap<>();
					map.put("token", us.getToken());
					return ResponseEntity.status(HttpStatus.OK).body(map);
				}
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@RequestMapping(value="logout", method=RequestMethod.PUT)
	public ResponseEntity<?> logout(
			@RequestHeader("Authorization") String token){
		// remove token from db
		Long number = userSessionRepository.removeByToken(token);
		if(number == 1)
			return ResponseEntity.ok(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(null);
	}
}