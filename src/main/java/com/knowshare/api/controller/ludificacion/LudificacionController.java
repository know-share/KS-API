/**
 * 
 */
package com.knowshare.api.controller.ludificacion;

import java.util.List;

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

import com.knowshare.dto.ludificacion.LeaderDTO;
import com.knowshare.enterprise.bean.avales.AvalFacade;
import com.knowshare.enterprise.bean.leaderboard.LeaderFacade;
import com.knowshare.enums.TipoAvalEnum;
import com.knowshare.enums.TipoUsuariosEnum;

/**
 * Endpoints para operaciones que tengan que 
 * ver con componente de ludificación
 * @author Miguel Montañez
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/ludificacion")
public class LudificacionController  {
	
	private static final String USERNAME = "username";

	@Autowired
	private AvalFacade avalBean;
	
	@Autowired
	private LeaderFacade leaderBean;
	
	/**
	 * Avala un usuario dado como parámetro
	 * @param request
	 * @param id de la cualidad o habilidad a avalar
	 * @param usernameTarget usuario a avalar
	 * @param tipo mapeada como {@link TipoAvalEnum}
	 * @return Si la operación se pudo ejecutar de forma
	 * correcta o si fue un aval válido retorna HttpStatus.OK.
	 * Si el aval no pudo realizarse debido a que ya fue dado
	 * retorna HttpStatus.NOT_MODIFIED
	 */
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
	
	/**
	 * Usado para generar el leaderboard de estudiantes registrados
	 * en cada carrera.
	 * @return Si la lista usada para el leaderboard está vacía
	 * retorna HttpStatus.NO_CONTENT. Si no está vacía
	 * retorna la lista de tipo {@link LeaderDTO} con estado
	 * HttpStatus.OK
	 */
	@RequestMapping(value="/leaderCarreras", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<LeaderDTO>> getAllCarreras(){
		List<LeaderDTO> carreras = leaderBean.carrerasLeader();
		if(carreras == null || carreras.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
			.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(carreras);
	}
	
	/**
	 * Usado para general el leaderboard de estudiantes con mayores
	 * avales.
	 * @param request
	 * @param carrera del leaderboard a generar
	 * @param tipo de usuario {@link TipoUsuariosEnum}
	 * @return Si no se manda la carrera o el tipo se retorna un
	 * HttpStatus.BAD_REQUEST, además si el tipo de usuario es 
	 * TipoUsuariosEnum.EGRESADO. Si la lista del leaderboard está vacía
	 * se retorna HttpStatus.NO_CONTENT. Si la lista no fue vacía se 
	 * retorna la lista con estado HttpStatus.OK
	 */
	@RequestMapping(value="/leaderUsuarios", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<LeaderDTO>> getEstudiantes(
			HttpServletRequest request, 
			@RequestParam String carrera,
			@RequestParam String tipo){
		if(carrera == null || tipo == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(null);
		final TipoUsuariosEnum tipoEnum = TipoUsuariosEnum.valueOf(tipo);
		if(tipoEnum.equals(TipoUsuariosEnum.EGRESADO))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(null);
		String username = request.getAttribute(USERNAME).toString();
		List<LeaderDTO> usuarios = leaderBean.usuariosLeader(username, carrera,tipoEnum);
		if(usuarios == null)
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(usuarios);
	}
}
