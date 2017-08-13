/**
 * 
 */
package com.knowshare.api.controller.idea;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.dto.idea.Comentario;
import com.knowshare.dto.idea.IdeaDTO;
import com.knowshare.enterprise.bean.idea.IdeaFacade;
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
	
	@RequestMapping(value="/crear" ,method = RequestMethod.POST)
	public ResponseEntity<?> crearIdea(@RequestBody IdeaDTO idea,
			HttpServletRequest request){
		final String username = request.getAttribute("username").toString();
		if(idea == null)
			return ResponseEntity.badRequest().body(null);
		idea.setUsuario(username);
		IdeaDTO crear = ideaBean.crearIdea(idea);
		if(crear!= null){
			return ResponseEntity.status(HttpStatus.OK).body(crear);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		
	}
	
	@RequestMapping(value = "/findByUsuario/{usernameObj:.+}",method=RequestMethod.GET)
	public ResponseEntity<?> findByUsuario(HttpServletRequest request,
			@PathVariable String usernameObj){
		List<IdeaDTO> ret = ideaBean.findByUsuario(usernameObj);
		if(!ret.isEmpty()){
			return ResponseEntity.status(HttpStatus.OK).body(ret);
		}
		if(ret.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);	
	}
	
	@RequestMapping(value = "/findByUsuarioPro/{usernameObj:.+}",method=RequestMethod.GET)
	public ResponseEntity<?> findByUsuarioPro(
			@PathVariable String usernameObj){
		List<IdeaDTO> ret = ideaBean.findByUsuarioProyecto(usernameObj);
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
			HttpServletRequest request){
		final String username = request.getAttribute("username").toString();
		List<IdeaDTO> ideas = ideaBean.find10(username);
		if(ideas == null || ideas.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(ideas);
	}
	
	@RequestMapping(value="/comentar" ,method = RequestMethod.POST)
	public ResponseEntity<?> comentario(HttpServletRequest request,
			@RequestBody Comentario params){
		if(params == null)
			return ResponseEntity.badRequest().body(null);
		final String username = request.getAttribute("username").toString();
		IdeaDTO idea = params.getIdea();
		OperacionIdea operacion = new OperacionIdea();
		operacion.setUsername(username);
		operacion.setFecha(new Date());
		operacion.setContenido(params.getComentario());
		operacion.setTipo(TipoOperacionEnum.COMENTARIO);
		
		if(ideaBean.agregarOperacion(idea, operacion) != null){
			return ResponseEntity.status(HttpStatus.OK).body(true);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);	
	}
	
	@RequestMapping(value="/light" ,method = RequestMethod.POST)
	public ResponseEntity<?> light(HttpServletRequest request,
			@RequestBody IdeaDTO params){
		if(params == null)
			return ResponseEntity.badRequest().body(null);
		final String username = request.getAttribute("username").toString();
		OperacionIdea operacion = new OperacionIdea();
		operacion.setUsername(username);
		operacion.setFecha(new Date());
		operacion.setContenido(null);
		operacion.setTipo(TipoOperacionEnum.LIGHT);
		
		if(ideaBean.agregarOperacion(params, operacion) != null){
			return ResponseEntity.status(HttpStatus.OK).body(true);
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		
	}
	
	@RequestMapping(value="/findById/{id}" ,method = RequestMethod.GET)
	public ResponseEntity<?> findById(HttpServletRequest request,
			@PathVariable String id){
		final String username = request.getAttribute("username").toString();
		IdeaDTO dto = ideaBean.findById(id, username) ;
		if(dto != null){
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="/compartir" ,method = RequestMethod.POST)
	public ResponseEntity<?> compartir(HttpServletRequest request,
			@RequestBody IdeaDTO dto){
		if(dto == null)
			return ResponseEntity.badRequest().body(null);
		final String username = request.getAttribute("username").toString();
		IdeaDTO ret = ideaBean.compartir(dto, username);
		if(ret != null){
			return ResponseEntity.status(HttpStatus.OK).body(ret);
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		
	}
	
	@RequestMapping(value="/findOperacion/{id}/{tipo}" ,method = RequestMethod.GET)
	public ResponseEntity<?> findByOperaciones(HttpServletRequest request,
			@PathVariable String id, @PathVariable String tipo){
		List<OperacionIdea> op = ideaBean.findOpreaciones(id, tipo);
		if(!op.isEmpty()){
			return ResponseEntity.status(HttpStatus.OK).body(op);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
