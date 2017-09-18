/**
 * 
 */
package com.knowshare.api.controller.idea;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.dto.idea.Comentario;
import com.knowshare.dto.idea.IdeaDTO;
import com.knowshare.enterprise.bean.idea.IdeaFacade;
import com.knowshare.enterprise.bean.rules.busqueda.BusquedaIdeaFacade;
import com.knowshare.entities.idea.OperacionIdea;
import com.knowshare.enums.TipoOperacionEnum;

/**
 * Endpoints para operaciones con objeto de tipo
 * {@link Idea}
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
	private BusquedaIdeaFacade ideaBusq;
	
	private static final String USERNAME = "username";
	
	@RequestMapping(value="/crear" ,method = RequestMethod.POST)
	public ResponseEntity<Object> crearIdea(@RequestBody IdeaDTO idea,
			HttpServletRequest request){
		final String username = request.getAttribute(USERNAME).toString();
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
	public ResponseEntity<Object> findByUsuario(HttpServletRequest request,
			@PathVariable String usernameObj,
			@RequestParam(defaultValue="0") Integer page,
			@RequestParam(required=true) long timestamp){
		Page<IdeaDTO> ret = ideaBean
				.findByUsuario(request.getAttribute(USERNAME).toString(),usernameObj,
						page,timestamp);
		if(null != ret && ret.hasContent()){
			return ResponseEntity.status(HttpStatus.OK).body(ret);
		}
		if(null != ret && !ret.hasContent()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);	
	}
	
	@RequestMapping(value = "/findByUsuarioPro/{usernameObj:.+}",method=RequestMethod.GET)
	public ResponseEntity<Object> findByUsuarioPro(
			@PathVariable String usernameObj){
		List<IdeaDTO> ret = ideaBean.findByUsuarioProyecto(usernameObj);
		if(ret !=null && !ret.isEmpty()){
			return ResponseEntity.status(HttpStatus.OK).body(ret);
		}
		if(ret !=null && ret.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);	
	}
	
	@RequestMapping(value="/comentar" ,method = RequestMethod.POST)
	public ResponseEntity<Object> comentario(HttpServletRequest request,
			@RequestBody Comentario params){
		if(params == null)
			return ResponseEntity.badRequest().body(null);
		final String username = request.getAttribute(USERNAME).toString();
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
	public ResponseEntity<Object> light(HttpServletRequest request,
			@RequestBody IdeaDTO params){
		if(params == null)
			return ResponseEntity.badRequest().body(null);
		final String username = request.getAttribute(USERNAME).toString();
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
	public ResponseEntity<Object> findById(HttpServletRequest request,
			@PathVariable String id){
		final String username = request.getAttribute(USERNAME).toString();
		IdeaDTO dto = ideaBean.findById(id, username) ;
		if(dto != null){
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="/compartir" ,method = RequestMethod.POST)
	public ResponseEntity<Object> compartir(HttpServletRequest request,
			@RequestBody IdeaDTO dto){
		if(dto == null)
			return ResponseEntity.badRequest().body(null);
		final String username = request.getAttribute(USERNAME).toString();
		IdeaDTO ret = ideaBean.compartir(dto, username);
		if(ret != null){
			return ResponseEntity.status(HttpStatus.OK).body(ret);
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="/findOperacion/{id}/{tipo}" ,method = RequestMethod.GET)
	public ResponseEntity<Object> findByOperaciones(HttpServletRequest request,
			@PathVariable String id, @PathVariable String tipo){
		List<OperacionIdea> op = ideaBean.findOperaciones(id, tipo);
		if(!op.isEmpty()){
			return ResponseEntity.status(HttpStatus.OK).body(op);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
	
	@RequestMapping(value="/cambiarestado" ,method = RequestMethod.PUT)
	public ResponseEntity<Object> cambiarEstado(HttpServletRequest request,
			@RequestBody IdeaDTO dto ){
		if(null != dto){
			IdeaDTO ret = ideaBean.cambiarEstado(dto);
			if(ret != null){
				return ResponseEntity.status(HttpStatus.OK).body(ret);
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	
	
	/**
	 * Debe ser renombrado el endpoint
	 * @param request
	 * @return
	 */
//	@RequestMapping(value="/findByTags" ,method = RequestMethod.POST)
//	public ResponseEntity<Object> findByTags(HttpServletRequest request,
//			@RequestBody List<Tag> tags){
//		List<IdeaDTO> ideas = ideaBusq.findByTags(tags);
//		if(ideas == null || ideas.isEmpty()){
//			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
//		}
//		return ResponseEntity.status(HttpStatus.OK).body(ideas);
//	}
	
	
}
