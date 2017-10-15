/**
 * 
 */
package com.knowshare.api.controller.perfilusuario;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.knowshare.dto.perfilusuario.ImagenDTO;
import com.knowshare.dto.perfilusuario.UsuarioDTO;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;
import com.knowshare.entities.academia.FormacionAcademica;
import com.knowshare.entities.academia.TrabajoGrado;
import com.knowshare.entities.perfilusuario.Gusto;
import com.knowshare.entities.perfilusuario.Usuario;
import com.knowshare.enums.PreferenciaIdeaEnum;
import com.knowshare.enums.TipoImagenEnum;
import com.knowshare.enums.TipoUsuariosEnum;

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
	
	private static final String USERNAME = "username";

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
	public ResponseEntity<Object> crearUsuario(@RequestBody UsuarioDTO dto) {
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
	public ResponseEntity<Object> seguir(
			@PathVariable String usernameObj,
			HttpServletRequest request){
		final String username = request.getAttribute(USERNAME).toString();
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
	public ResponseEntity<Object> unfollow(
			HttpServletRequest request,
			@PathVariable String usernameObj){
		final String username = request.getAttribute(USERNAME).toString();
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
	public ResponseEntity<Object> solicitudAmistad(
			HttpServletRequest request,
			@PathVariable String usernameObj,
			@RequestParam(name="action",required=false) String action){
		final String username = request.getAttribute(USERNAME).toString();
		if(usuarioBean.isUsernameTaken(usernameObj)){
			if(null == action){
				if(usuarioBean.solicitudAmistad(username, usernameObj))
					return ResponseEntity.status(HttpStatus.OK).body(null);
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
			}else{
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
	 * Obtiene el usuario especificado en la solicitud.
	 * @param username
	 * @return {@link HttpStatus.BAD_REQUEST} Si no especifica el usuario.
	 * {@link HttpStatus.NO_CONTENT} Si el usuario no existe.
	 * {@link HttpStatus.OK} Si el usuario fue encontrado.
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
	
	/**
	 * Agrega el trabajo de grado que debe ser enviado
	 * en la solicitud. 
	 * @param request
	 * @param tg
	 * @return {@link HttpStatus.BAD_REQUEST} Si no envía el trabajo de
	 * grado en la solicitud. 
	 * {@link HttpStatus.CREATED} Si pudo realizar la operación correctamente.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo un problema agregando
	 * el trabajo de grado.
	 */
	@RequestMapping(value="/addTG", method=RequestMethod.POST,consumes="application/json")
	public ResponseEntity<Object> addTG(
			HttpServletRequest request,
			@RequestBody TrabajoGrado tg){
		final String username = request.getAttribute(USERNAME).toString();
		if(tg == null)
			return ResponseEntity.badRequest().body(null);
		if(usuarioBean.agregarTGDirigido(tg, username))
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * Agrega la formación académica especificada en la solicitud
	 * en el usuario que realiza la operación.
	 * @param request
	 * @param fa
	 * @return {@link HttpStatus.BAD_REQUEST} Si no envía la formación
	 * académica a agregar. 
	 * {@link HttpStatus.CREATED} Si pudo crearse adecuadamente.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo problemas en
	 * la creación.
	 */
	@RequestMapping(value ="/addFormacionAcademica", method=RequestMethod.POST,consumes="application/json")
	public ResponseEntity<Object> addFormacionAcademica(
			HttpServletRequest request,
			@RequestBody FormacionAcademica fa){
		final String username = request.getAttribute(USERNAME).toString();
		if(fa == null)
			return ResponseEntity.badRequest().body(null);
		if(usuarioBean.agregarFormacionAcademica(fa, username))
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * Elimina el amigo especificado en el parámetro de la solicitud.
	 * @param request
	 * @param username
	 * @return {@link HttpStatus.BAD_REQUEST} Si no especifica un usuario
	 * a eliminar.
	 * {@link HttpStatus.OK} Si pudo eliminar al amigo.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo un problema
	 * eliminando el amigo.
	 */
	@RequestMapping(value="/eliminarAmigo/{username:.+}", method =RequestMethod.PUT)
	public ResponseEntity<Object> eliminarAmigo(
			HttpServletRequest request,
			@PathVariable String username){
		if(username == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		final String usernameToken = request.getAttribute("username").toString();
		if(usuarioBean.eliminarAmigo(usernameToken, username))
			return ResponseEntity.status(HttpStatus.OK).body(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * Actualiza la información académica.
	 * @param request
	 * @param usuario
	 * @return {@link HttpStatus.BAD_REQUEST} Si no se manda el usuario con
	 * la información a actualizar.
	 * {@link HttpStatus.OK} Si pudo actualizar de forma correcta la información.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo problemas en la
	 * actualización parcial.
	 */
	@RequestMapping(value="/actualizarInfoAcademica", method =RequestMethod.PATCH)
	public ResponseEntity<Object> actualizarInfoAcademica(
			HttpServletRequest request,
			@RequestBody UsuarioDTO usuario){
		if(usuario == null)
			return ResponseEntity.badRequest().body(null);
		if(usuarioBean.actualizarInfoAcademica(usuario))
			return ResponseEntity.ok(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * Actualiza habilidades o cualidades del usuario.
	 * @param request
	 * @param usuario
	 * @return {@link HttpStatus.BAD_REQUEST} Si no se manda el usuario con
	 * la información a actualizar.
	 * {@link HttpStatus.OK} Si pudo actualizar de forma correcta la información.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo problemas en la
	 * actualización parcial.
	 */
	@RequestMapping(value="/actualizarHabilidadCualidad", method =RequestMethod.PATCH)
	public ResponseEntity<Object> actualizarHabilidadCualidad(
			HttpServletRequest request,
			@RequestBody UsuarioDTO usuario){
		if(usuario == null)
			return ResponseEntity.badRequest().body(null);
		if(usuarioBean.actualizarHabilidadCualidad(usuario))
			return ResponseEntity.ok(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * Actualiza información básica de un usuario.
	 * @param request
	 * @param usuario
	 * @return {@link HttpStatus.BAD_REQUEST} Si no se manda el usuario con
	 * la información a actualizar.
	 * {@link HttpStatus.OK} Si pudo actualizar de forma correcta la información.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo problemas en la
	 * actualización parcial.
	 */
	@RequestMapping(value="/actualizarBasis", method =RequestMethod.PATCH)
	public ResponseEntity<Object> actualizarBasis(
			HttpServletRequest request,
			@RequestBody UsuarioDTO usuario){
		if(usuario == null)
			return ResponseEntity.badRequest().body(null);
		if(usuarioBean.actualizarBasis(usuario))
			return ResponseEntity.ok(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * Permite la carga de imágenes al servidor.
	 * @param file
	 * @param request
	 * @return {@link HttpStatus.BAD_REQUEST} Si el archivo no es
	 * mandado.
	 * {@link HttpStatus.OK} Si la imagen pudo ser subido.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo problemas
	 * en la carga de la imagen. 
	 */
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public ResponseEntity<Object> uploadImage(
			@RequestParam MultipartFile file,
			HttpServletRequest request){
		final String username = request.getAttribute(USERNAME).toString();
		if(null == file)
			return ResponseEntity.badRequest().body(null);
		if(this.usuarioBean.uploadImage(username, file))
			return ResponseEntity.ok(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * Permite obtener la imagen del usuario especificado
	 * @param username
	 * @return {@link HttpStatus.OK} Si el usuario posee una imagen
	 * en el servidor.
	 * {@link HttpStatus.NOT_FOUND} Si la imagen no fue encontrada
	 * para el usuario especificado
	 */
	@RequestMapping(value="/image/{username:.+}", method=RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(
			@PathVariable String username){
		final ImagenDTO image = this.usuarioBean.getImage(username);
		if(image.isResult()){
			HttpHeaders headers = new HttpHeaders();
			if(image.getType().equals(TipoImagenEnum.PNG))
				headers.setContentType(MediaType.IMAGE_PNG);
			else
				headers.setContentType(MediaType.IMAGE_JPEG);
		    headers.setContentLength(image.getBytes().length);
		    return new ResponseEntity<>(image.getBytes(), headers, HttpStatus.OK);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	/**
	 * Actualiza la preferencia de idea de un usuario.
	 * @param request
	 * @param preferencia
	 * @return {@link HttpStatus.OK} Si la actualización se realiza
	 * de forma exitosa.
	 * {@link HttpStatus.NOT_MODIFIED} Si actualiza a un estado que ya
	 * tenía.
	 * {@link HttpStatus.BAD_REQUEST} Si la preferencia no es enviada.
	 */
	@RequestMapping(value="preferenciaIdea", method=RequestMethod.PATCH)
	public ResponseEntity<Object> updatePreferencia(
			HttpServletRequest request,
			@RequestBody String preferencia){
		final String username = request.getAttribute(USERNAME).toString();
		if(preferencia != null){
			if(this.usuarioBean.updatePreferenciaIdea(username, PreferenciaIdeaEnum.valueOf(preferencia)))
				return ResponseEntity.status(HttpStatus.OK).body(null);
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	/**
	 * Actualiza las insignias que ya han sido vistas por
	 * el usuario.
	 * @param request
	 * @return {@link HttpStatus.OK} Si se pudo realizar la
	 * actualización de forma correcta.
	 * {@link HttpStatus.INTERNAL_SERVER_ERROR} Si hubo problemas
	 * en la actualización.
	 */
	@RequestMapping(value="updateInsignias", method=RequestMethod.PUT)
	public ResponseEntity<Object> updateInsignias(
			HttpServletRequest request){
		final String username = request.getAttribute(USERNAME).toString();
		if(usuarioBean.updateInsignias(username))
			return ResponseEntity.status(HttpStatus.OK).body(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * Promueve un {@link TipoUsuariosEnum.ESTUDIANTE} a {@link TipoUsuariosEnum.EGRESADO}
	 * @param username
	 * @return {@link HttpStatus.OK} Si la operación se pudo realizar.
	 * {@link HttpStatus.NOT_MODIFIED} Si el usuario ya es egresado o profesor.
	 */
	@RequestMapping(value="promover/{username:.+}", method=RequestMethod.PUT)
	public ResponseEntity<Object> promote(
			@PathVariable String username){
		if(usuarioBean.promoteEstudiante(username))
			return ResponseEntity.status(HttpStatus.OK).body(null);
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
	}
	
	/**
	 * Actualiza los gustos de un usuario de tipo {@link TipoUsuariosEnum.ESTUDIANTE}
	 * @param request
	 * @param gustos
	 * @return {@link HttpStatus.OK} Si la actualización se pudo realizar.
	 * {@link HttpStatus.NOT_MODIFIED} Si no hubieron cambios nuevos.
	 * {@link HttpStatus.BAD_REQUEST} Si no se mandan gustos en la solicitud.
	 */
	@RequestMapping(value ="actualizarGustos", method=RequestMethod.PATCH)
	public ResponseEntity<Object> actualizarGustos(
			HttpServletRequest request,
			@RequestBody List<Gusto> gustos){
		final String username = request.getAttribute(USERNAME).toString();
		if(null != gustos){
			if(this.usuarioBean.actualizarGustos(gustos, username))
				return ResponseEntity.status(HttpStatus.OK).body(null);
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
}
