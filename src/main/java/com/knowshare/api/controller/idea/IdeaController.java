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
import com.knowshare.entities.idea.OperacionIdea;
import com.knowshare.enums.TipoIdeaEnum;
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
	
	private static final String USERNAME = "username";
	
	/**
	 * Realiza la creación de una idea.
	 * @param idea
	 * @param request
	 * @return {@link HttpStatus.BAD_REQUEST} Si no se manda una idea
	 * para la creación.
	 * {@link HttpStatus.OK} Si pudo crear la idea. Esta idea nueva es
	 * enviada de vuelta.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo un problema en 
	 * la creación
	 */
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
	
	/**
	 * Busca las ideas credas por el usuario especificado en la 
	 * solicitud.
	 * @param request
	 * @param usernameObj
	 * @param page
	 * @param timestamp
	 * @return {@link HttpStatus.OK} Si encontró ideas.
	 * {@link HttpStatus.NO_CONTENT} Si no hay ideas para cierto usuario.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo un problema con la
	 * búsqueda de las ideas.
	 */
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
	
	/**
	 * Busca ideas que puedan ser usadas para la creación de ideas
	 * de tipo {@link TipoIdeaEnum.PR} que le pertenezcan al usuario
	 * especificado en la solicitud.
	 * @param usernameObj
	 * @param page
	 * @param timestamp
	 * @return {@link HttpStatus.OK} Si encontró ideas para retornar.
	 * {@link HttpStatus.NO_CONTENT} Si no encontró ideas.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo problemas en la
	 * búsqueda de ideas.
	 */
	@RequestMapping(value = "/findByUsuarioPro/{usernameObj:.+}",method=RequestMethod.GET)
	public ResponseEntity<Object> findByUsuarioPro(
			@PathVariable String usernameObj,
			@RequestParam(defaultValue="0") Integer page,
			@RequestParam(required = true) long timestamp){
		Page<IdeaDTO> ret = ideaBean.findByUsuarioProyecto(usernameObj,page,timestamp);
		if(ret !=null && ret.hasContent()){
			return ResponseEntity.status(HttpStatus.OK).body(ret);
		}
		if(ret !=null && !ret.hasContent()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);	
	}
	
	/**
	 * Permite agregar un comentario a la idea correspondiente.
	 * @param request
	 * @param params
	 * @return {@link HttpStatus.BAD_REQUEST} Si el comentario no es
	 * enviado en el cuerpo de la solicitud.
	 * {@link HttpStatus.OK} Si al agregar la operación todo fue bien.
	 * Retornará verdadero.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo problemas en
	 * la agregación del comentario. Retornará falso.
	 */
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
	
	/**
	 * Permite agregar un light a la idea correspondiente.
	 * @param request
	 * @param params
	 * @return {@link HttpStatus.BAD_REQUEST} Si la idea no es enviada
	 * en el cuerpo de la solicitud.
	 * {@link HttpStatus.OK} Si al agregar la operación todo fue bien.
	 * Retornará verdadero.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo problemas en
	 * la agregación del comentario. Retornará falso.
	 */
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
	
	/**
	 * Encuentra una idea por su id.
	 * @param request
	 * @param id
	 * @return {@link HttpStatus.OK} Si encontró la idea.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo problemas
	 * en la búsqueda de la idea.
	 */
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
	
	/**
	 * Comparte la idea especificado en el cuerpo de la solicitud.
	 * @param request
	 * @param dto
	 * @return {@link HttpStatus.BAD_REQUEST} Si no se envía la idea
	 * a compartir dentro de la solicitud.
	 * {@link HttpStatus.OK} Si la operación se realizó correctamente.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo problemas al
	 * realizar la operación.
	 */
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
	
	/**
	 * Busca la lista de Lights o comentarios hechos a cierta idea.
	 * @param request
	 * @param id
	 * @param tipo de la operación a entrontrar.
	 * @return {@link HttpStatus.OK} Si encontró operaciones que retornar.
	 * {@link HttpStatus.NO_CONTENT} Si la idea no tiene operaciones de ese
	 * tipo registradas.
	 */
	@RequestMapping(value="/findOperacion/{id}/{tipo}" ,method = RequestMethod.GET)
	public ResponseEntity<Object> findByOperaciones(HttpServletRequest request,
			@PathVariable String id, @PathVariable String tipo){
		List<OperacionIdea> op = ideaBean.findOperaciones(id, tipo);
		if(!op.isEmpty()){
			return ResponseEntity.status(HttpStatus.OK).body(op);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
	
	/**
	 * Permite cambiar el estado de una idea, de NOTG a TG
	 * @param request
	 * @param dto
	 * @return {@link HttpStatus.OK} Si pudo realizar la actualización
	 * de estado.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo problemas en la actualización
	 * {@link HttpStatus.BAD_REQUEST} Si no se manda la idea a actualizar
	 * en el cuerpo de la solicitud.
	 */
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
}
