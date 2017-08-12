/**
 * 
 */
package com.knowshare.api.controller.rules;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.dto.rules.RecomendacionDTO;
import com.knowshare.enterprise.bean.rules.busqueda.BusquedaUsuarioFacade;
import com.knowshare.enterprise.bean.rules.config.RulesAdminFacade;
import com.knowshare.enterprise.bean.rules.usuarios.RecomendacionConexionFacade;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;

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
	
	/**
	 * Obtiene las recomendaciones para el usuario actual.
	 * @param token para validar la autenticidad del cliente.
	 * @return Lista de recomendaciones
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/recomendacionConexiones", method=RequestMethod.GET)
	public ResponseEntity<Object> getRecomendaciones(
			HttpServletRequest request){
		final String username = request.getAttribute("username").toString();
		final List<RecomendacionDTO> recomendaciones = (List<RecomendacionDTO>)recomendacionesBean
				.setDeRecomendaciones(usuarioBean.getUsuario(username));
		if(recomendaciones == null || recomendaciones.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		return ResponseEntity.ok(recomendaciones);
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
		final String username = request.getAttribute("username").toString();
		if(null == param || param.isEmpty())
			return ResponseEntity.badRequest().body(null);
		final List<RecomendacionDTO> busqueda = busquedaBean
				.buscarUsuario(usuarioBean.getUsuario(username), filtro, param);
		if(null == busqueda || busqueda.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		return ResponseEntity.ok(busqueda);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.GET)
	public void updateRules(){
		rulesAdminBean.updateRules();
	}
}
