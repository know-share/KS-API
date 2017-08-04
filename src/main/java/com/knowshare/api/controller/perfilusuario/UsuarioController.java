/**
 * 
 */
package com.knowshare.api.controller.perfilusuario;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.knowshare.dto.perfilusuario.UsuarioDTO;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;
import com.knowshare.entities.academia.FormacionAcademica;
import com.knowshare.entities.academia.TrabajoGrado;
import com.knowshare.entities.perfilusuario.Usuario;

/**
 * Endpoints para operaciones con objeto de tipo {@link Usuario}
 * @author Miguel Montañez
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/usuario")
public class UsuarioController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UsuarioFacade usuarioBean;

	/**
	 * Revisa si el username dado ya está registrado en
	 * la aplicación.
	 * @param username que se verificará
	 * @return true si ya está tomado dicho username, de lo
	 * contrario false.
	 */
	@RequestMapping(value = "isUsernameTaken", method = RequestMethod.GET)
	public ResponseEntity<Boolean> isUsernameTaken(@RequestParam String username) {
		logger.debug(":::: Start method isUsernameTaken(String) in UsuarioController ::::");
		if (username == null || username.isEmpty()) {
			logger.error(":::: Error parameter username is not in the request ::::");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(usuarioBean.isUsernameTaken(username));
	}
	
	/**
	 * Revisa si el correo dado ya está registrado en 
	 * la aplicación
	 * @param correo que se verificará
	 * @return true si ya está tomado dicho correo, de lo
	 * contrario false.
	 */
	@RequestMapping(value = "isCorreoTaken", method = RequestMethod.GET)
	public ResponseEntity<Boolean> isCorreoTaken(@RequestParam String correo) {
		logger.debug(":::: Start method isCorreoTaken(String) in UsuarioController ::::");
		if (correo == null || correo.isEmpty()) {
			logger.error(":::: Error parameter correo is not in the request ::::");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(usuarioBean.isCorreoTaken(correo));
	}

	/**
	 * Controlador de creación de un usuario, recibe el DTO
	 * que posteriormente es convertido a la entidad correspondiente.
	 * @param dto Usuario con la información de registro de tipo {@link UsuarioDTO}
	 * @return {@link HttpStatus.OK}, si no se pudo crear, pero la 
	 * solicitud fue correcta. {@link HttpStatus.CREATED}, si el usuario pudo ser creado.
	 * De lo contrario {@link HttpStatus.BAD_REQUEST}
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, consumes="application/json")
	public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDTO dto) {
		logger.debug(":::: Start method crearUsuario(UsuarioDTO) in UsuarioController ::::");
		if (dto == null) {
			logger.error(":::: Error parameter dto is not in the request ::::");
			return ResponseEntity.badRequest().body(null);
		}
		if (usuarioBean.crearUsuario(dto))
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		return ResponseEntity.status(HttpStatus.OK).body(false);
	}
	
	/**
	 * Acción de seguir sobre un usuario específico.
	 * @param token para validar la autenticidad del cliente.
	 * @param usernameObj Usuario a seguir
	 * @return {@link HttpStatus.UNAUTHORIZED} si el token no es válido o
	 * ha expirado. {@link HttpStatus.OK} si la operación pudo llevarse
	 * a cabo. {@link HttpStatus.NOT_MODIFIED} si no pudo seguirlo debido a
	 * que ya lo seguía previamente. {@link HttpStatus.NOT_FOUND} el usuario
	 * especificado en el parámetro no se encuentra.
	 */
	@RequestMapping(value = "/seguir/{usernameObj:.+}", method = RequestMethod.PUT)
	public ResponseEntity<?> seguir(
			@PathVariable String usernameObj,
			HttpServletRequest request){
		final String username = request.getAttribute("username").toString();
		if(usuarioBean.isUsernameTaken(usernameObj)){
			if(usuarioBean.seguir( username,usernameObj)){
				return ResponseEntity.status(HttpStatus.OK).body(null);
			}else
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
	}
	
	/**
	 * Acción de dejar de seguir sobre un usuario específico.
	 * @param token para validar la autenticidad del cliente.
	 * @param usernameObj usuario a dejar de seguir.
	 * @return {@link HttpStatus.UNAUTHORIZED} si el token no es válido o
	 * ha expirado. {@link HttpStatus.OK} si la operación pudo llevarse
	 * a cabo. {@link HttpStatus.NOT_MODIFIED} si no pudo dejarlo de seguir 
	 * debido a que no lo seguía previamente. {@link HttpStatus.NOT_FOUND} el usuario
	 * especificado en el parámetro no se encuentra.
	 */
	@RequestMapping(value = "/dejarseguir/{usernameObj:.+}", method = RequestMethod.PUT)
	public ResponseEntity<?> unfollow(
			HttpServletRequest request,
			@PathVariable String usernameObj){
		final String username = request.getAttribute("username").toString();
		if(usuarioBean.isUsernameTaken(usernameObj)){
			if(usuarioBean.dejarSeguir( username,usernameObj)){
				return ResponseEntity.status(HttpStatus.OK).body(null);
			}else
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
	}
	
	/**
	 * Registro de una solicitud de amistad, sin embargo, si se envía
	 * el parámetro 'action' en la solicitud, la operación es aceptar 
	 * o rechazar una solicitud de amistad.
	 * @param token para validar la autenticidad del cliente.
	 * @param usernameObj usuario dueño de la solicitud
	 * @param action puede ser: ACCEPT or REJECT
	 * @return {@link HttpStatus.UNAUTHORIZED} si el token no es válido o
	 * ha expirado. {@link HttpStatus.OK} si la operación pudo llevarse
	 * a cabo. {@link HttpStatus.NOT_MODIFIED} si no se pudo ejecutar la
	 * acción. {@link HttpStatus.NOT_FOUND} el usuario especificado en el 
	 * parámetro no se encuentra. {@link HttpStatus.BAD_REQUEST} si el parámetro
	 * action no es de los dos posibles valores.
	 */
	@RequestMapping(value = "/solicitud/{usernameObj:.+}", method = RequestMethod.PUT)
	public ResponseEntity<?> solicitudAmistad(
			HttpServletRequest request,
			@PathVariable String usernameObj,
			@RequestParam(name="action",required=false) String action){
		final String username = request.getAttribute("username").toString();
		if(usuarioBean.isUsernameTaken(usernameObj)){
			if(null == action)
				if(usuarioBean.solicitudAmistad(username, usernameObj)){
					return ResponseEntity.status(HttpStatus.OK).body(null);
				}else
					return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
			else{
				if(action.equalsIgnoreCase("accept") || action.equalsIgnoreCase("reject")){
					if(usuarioBean.accionSolicitud(username, usernameObj, action))
						return ResponseEntity.status(HttpStatus.OK).body(null);
					return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	/**
	 * 
	 * @param token
	 * @param username
	 * @return
	 */
	@RequestMapping(value="/get/{username:.+}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<UsuarioDTO> getUsuario(
			@PathVariable String username){
		if (username == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		UsuarioDTO usuario = usuarioBean.getUsuario(username);
		if(usuario == null)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		return ResponseEntity.ok(usuario);
	}
	
	@RequestMapping(value="/addTG", method=RequestMethod.POST,consumes="application/json")
	public ResponseEntity<?> addTG(
			HttpServletRequest request,
			@RequestBody TrabajoGrado tg){
		final String username = request.getAttribute("username").toString();
		if(tg == null)
			return ResponseEntity.badRequest().body(null);
		if(usuarioBean.agregarTGDirigido(tg, username))
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value ="/addFormacionAcademica", method=RequestMethod.POST,consumes="application/json")
	public ResponseEntity<?> addFormacionAcademica(
			HttpServletRequest request,
			@RequestBody FormacionAcademica fa){
		final String username = request.getAttribute("username").toString();
		if(fa == null)
			return ResponseEntity.badRequest().body(null);
		if(usuarioBean.agregarFormacionAcademica(fa, username))
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="/eliminarAmigo/{username:.+}", method =RequestMethod.PUT)
	public ResponseEntity<?> eliminarAmigo(
			HttpServletRequest request,
			@PathVariable String username){
		if(username == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		final String usernameToken = request.getAttribute("username").toString();
		if(usuarioBean.eliminarAmigo(usernameToken, username))
			return ResponseEntity.status(HttpStatus.OK).body(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="/actualizarInfoAcademica", method =RequestMethod.PATCH)
	public ResponseEntity<?> actualizarInfoAcademica(
			HttpServletRequest request,
			@RequestBody UsuarioDTO usuario){
		if(usuario == null)
			return ResponseEntity.badRequest().body(null);
		if(usuarioBean.actualizarInfoAcademica(usuario))
			return ResponseEntity.ok(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="/actualizarHabilidadCualidad", method =RequestMethod.PATCH)
	public ResponseEntity<?> actualizarHabilidadCualidad(
			HttpServletRequest request,
			@RequestBody UsuarioDTO usuario){
		if(usuario == null)
			return ResponseEntity.badRequest().body(null);
		if(usuarioBean.actualizarHabilidadCualidad(usuario))
			return ResponseEntity.ok(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="/actualizarBasis", method =RequestMethod.PATCH)
	public ResponseEntity<?> actualizarBasis(
			HttpServletRequest request,
			@RequestBody UsuarioDTO usuario){
		if(usuario == null)
			return ResponseEntity.badRequest().body(null);
		if(usuarioBean.actualizarBasis(usuario))
			return ResponseEntity.ok(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
