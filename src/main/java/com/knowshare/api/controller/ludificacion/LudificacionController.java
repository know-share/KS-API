/**
 * 
 */
package com.knowshare.api.controller.ludificacion;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
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

import com.knowshare.enterprise.bean.avales.AvalFacade;
import com.knowshare.enums.TipoAvalEnum;

/**
 * @author Miguel Montañez
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/ludificacion")
public class LudificacionController {
	
	private static final String USERNAME = "username";

	@Autowired
	private AvalFacade avalBean;
	
	@RequestMapping(value = "avalar/{usernameTarget:.+}", method = RequestMethod.PUT)
	public ResponseEntity<Object> avalarUsuario(
			HttpServletRequest request,
			@RequestBody ObjectId id,
			@PathVariable String usernameTarget,
			@RequestParam String tipo){
		final String username = request.getAttribute(USERNAME).toString();
		if(id == null || tipo == null || tipo.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		
		if(avalBean.avalarUsuario(username, usernameTarget, id, TipoAvalEnum.valueOf(tipo)))
			return ResponseEntity.status(HttpStatus.OK).body(null);
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
	}
}