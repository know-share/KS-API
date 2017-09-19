/**
 * 
 */
package com.knowshare.api.controller.rules;

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

import com.knowshare.dto.idea.IdeaDTO;
import com.knowshare.dto.rules.RecomendacionDTO;
import com.knowshare.enterprise.bean.rules.busqueda.BusquedaIdeaFacade;
import com.knowshare.enterprise.bean.rules.busqueda.BusquedaUsuarioFacade;
import com.knowshare.enterprise.bean.rules.config.RulesAdminFacade;
import com.knowshare.enterprise.bean.rules.usuarios.RecomendacionConexionFacade;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;
import com.knowshare.entities.idea.Tag;

/**
 * Controlador para reglas de negocio dentro de la aplicación de
 * KnowShare
 * @author Miguel Montañez
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/rules")
public class RulesController {
	
	@Autowired
	private RecomendacionConexionFacade recomendacionesBean;
	
	@Autowired
	private BusquedaUsuarioFacade busquedaBean;
	
	@Autowired
	private RulesAdminFacade rulesAdminBean;
	
	@Autowired
	private UsuarioFacade usuarioBean;
	
	@Autowired
	private BusquedaIdeaFacade ideaBusq;
	
	private static final String USERNAME = "username";
	
	/**
	 * Obtiene las recomendaciones para el usuario actual.
	 * @param token para validar la autenticidad del cliente.
	 * @return Lista de recomendaciones
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/recomendacionConexiones", method=RequestMethod.GET)
	public ResponseEntity<Object> getRecomendaciones(
			HttpServletRequest request){
		if(rulesAdminBean.isRulesOn()){
			final String username = request.getAttribute(USERNAME).toString();
			final List<RecomendacionDTO> recomendaciones = (List<RecomendacionDTO>)recomendacionesBean
					.setDeRecomendaciones(usuarioBean.getUsuario(username));
			if(recomendaciones == null || recomendaciones.isEmpty())
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			return ResponseEntity.ok(recomendaciones);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
	
	/**
	 * Busca usuario según el filtro y el parámetro de entrada.
	 * @param token para validar la autenticidad del cliente.
	 * @param filtro los posibles valores son: HABILIDAD, AREA y
	 * NOMBRE
	 * @param param el string a buscar.
	 * @return Lista con los usuarios a buscar
	 */
	@RequestMapping(value="/buscarUsuario",method=RequestMethod.GET)
	public ResponseEntity<Object> buscarUsuario(
			HttpServletRequest request,
			@RequestParam(defaultValue="NOMBRE") String filtro,
			@RequestParam String param){
		final String username = request.getAttribute(USERNAME).toString();
		if(null == param || param.isEmpty())
			return ResponseEntity.badRequest().body(null);
		final List<RecomendacionDTO> busqueda = busquedaBean
				.buscarUsuario(usuarioBean.getUsuario(username), filtro, param);
		if(null == busqueda || busqueda.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		return ResponseEntity.ok(busqueda);
	}
	
	/**
	 * Se encarga de actualizar en tiempo de ejecución las reglas
	 * que están en el motor.
	 * @return verdadero si pudo llevar a cabo la actualización,
	 * de lo contrario falso.
	 */
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	public ResponseEntity<Object> updateRules(){
		if(rulesAdminBean.updateRules())
			return ResponseEntity.status(HttpStatus.OK).body(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * Controlador encargado de actualizar el atributo rules de las
	 * preferencias del sistema.
	 * @param state 1 o 0
	 * @return HttpStatus.OK si pudo actualizar de lo contrario, HttpStatus.NOT_MODIFIED
	 */
	@RequestMapping(value="/rulesPreferences", method=RequestMethod.PATCH)
	public ResponseEntity<Object> updateRulesPreferences(
			@RequestParam(required=true) short state){
		if(rulesAdminBean.updateRulesSystem(state))
			return ResponseEntity.status(HttpStatus.OK).body(null);
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
	}
	
	/**
	 * Obtiene el estado de la preferencia rules del sistema
	 * @return HttpStatus.OK y true o false si las reglas están o no 
	 * habilitadas
	 */
	@RequestMapping(value="/rulesPreferences", method=RequestMethod.GET)
	public ResponseEntity<Object> getRulesPreferences(){
		return ResponseEntity.status(HttpStatus.OK).body(rulesAdminBean.isRulesOn());
	}
	
	/**
	 * Busca ideas
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/buscarIdea/{criterio}" ,method = RequestMethod.POST)
	public ResponseEntity<Object> findByTags(HttpServletRequest request,
			@RequestBody List<Tag> tags,@PathVariable String criterio){
		List<IdeaDTO> ideas = ideaBusq.findIdeas(tags,criterio,request.getAttribute(USERNAME).toString());
		if(ideas == null || ideas.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(ideas);
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/findIdeasRed" ,method = RequestMethod.GET)
	public ResponseEntity<Object> findRed(
			HttpServletRequest request,
			@RequestParam(defaultValue="0") Integer page){
		final String username = request.getAttribute(USERNAME).toString();
		Page<IdeaDTO> ideas = ideaBusq.findRed(username,page);
		if(ideas == null || !ideas.hasContent()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(ideas);
	}
}
