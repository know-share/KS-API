/**
 * 
 */
package com.knowshare.api.controller.idea;

import java.util.Date;
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
import com.knowshare.dto.idea.Comentario;
import com.knowshare.dto.idea.IdeaDTO;
import com.knowshare.enterprise.bean.idea.IdeaFacade;
import com.knowshare.enterprise.repository.app.UserSessionRepository;
import com.knowshare.entities.app.UserSession;
import com.knowshare.entities.idea.OperacionIdea;
import com.knowshare.enums.TipoOperacionEnum;

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
		
		List<IdeaDTO> ret = ideaBean.findByUsuario(usernameObj);
		if(!ret.isEmpty()){
			return ResponseEntity.status(HttpStatus.OK).body(ret);
		}
		if(ret.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);	
	}
	
	@RequestMapping(value="/find10" ,method = RequestMethod.GET)
	public ResponseEntity<?> find(
			@RequestHeader("Authorization") String token){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}
		String username = JWTFilter.getSub(token, user.getSecretKey());
		if(!username.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		List<IdeaDTO> ideas = ideaBean.find10(user.getUsername());
		if(ideas == null || ideas.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(ideas);
	}
	
	@RequestMapping(value="/comentar" ,method = RequestMethod.POST)
	public ResponseEntity<?> comentario(@RequestHeader("Authorization") String token,
			@RequestBody Comentario params){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false); 
		}
		String username = JWTFilter.getSub(token, user.getSecretKey());
		if(!username.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
		IdeaDTO idea = params.getIdea();
		OperacionIdea operacion = new OperacionIdea();
		operacion.setUsername(user.getUsername());
		operacion.setFecha(new Date());
		operacion.setContenido(params.getComentario());
		operacion.setTipo(TipoOperacionEnum.COMENTARIO);
		
		if(ideaBean.agregarOperacion(idea, operacion) != null){
			return ResponseEntity.status(HttpStatus.OK).body(true);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);	
	}
	
	@RequestMapping(value="/light" ,method = RequestMethod.POST)
	public ResponseEntity<?> light(@RequestHeader("Authorization") String token,
			@RequestBody IdeaDTO params){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false); 
		}
		String username = JWTFilter.getSub(token, user.getSecretKey());
		if(!username.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
		OperacionIdea operacion = new OperacionIdea();
		operacion.setUsername(user.getUsername());
		operacion.setFecha(new Date());
		operacion.setContenido(null);
		operacion.setTipo(TipoOperacionEnum.LIGHT);
		
		if(ideaBean.agregarOperacion(params, operacion) != null){
			return ResponseEntity.status(HttpStatus.OK).body(true);
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		
	}
	
	@RequestMapping(value="/findById/{id}" ,method = RequestMethod.GET)
	public ResponseEntity<?> findById(@RequestHeader("Authorization") String token,
			@PathVariable String id){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}
		String username = JWTFilter.getSub(token, user.getSecretKey());
		if(!username.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		IdeaDTO dto = ideaBean.findById(id, user.getUsername()) ;
		if(dto != null){
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		
	}
}
