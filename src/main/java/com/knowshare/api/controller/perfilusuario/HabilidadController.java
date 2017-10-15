/**
 * 
 */
package com.knowshare.api.controller.perfilusuario;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.dto.perfilusuario.HabilidadDTO;
import com.knowshare.enterprise.bean.habilidad.HabilidadFacade;
import com.knowshare.entities.perfilusuario.Habilidad;
import com.knowshare.enums.TipoHabilidadEnum;

/**
 * Endpoints para operaciones con objeto de tipo {@link Habilidad}
 * @author Miguel Montañez
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/habilidad")
public class HabilidadController {
	
	@Autowired
	private HabilidadFacade habilidadBean;
	
	/**
	 * Obtiene las habilidades {@link TipoHabilidadEnum.PERSONAS} y 
	 * {@link TipoHabilidadEnum.PROFESIONALES} de la carrera especificada
	 * alojadas en la base de datos 
	 * @param carrera
	 * @return {@link HttpStatus.BAD_REQUEST} Si la carrera no es especificada.
	 * {@link HttpStatus.NO_CONTENT} Si no hay habilidades para dicha carrera.
	 * {@link HttpStatus.OK} Si se encontraron habilidades para la carrera
	 * especificada.
	 */
	@RequestMapping(value="/getHabilidades", method=RequestMethod.GET)
	public ResponseEntity<List<HabilidadDTO>> getHabilidades(
			@RequestParam String carrera){
		if(carrera == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(null);
		final List<HabilidadDTO> habilidades = habilidadBean.getHabilidades(carrera);
		if(habilidades == null || habilidades.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(habilidades);
	}
	
	/**
	 * Obtiene las habilidades {@link TipoHabilidadEnum.PROFESIONALES} de
	 * la carrera especificada.
	 * @param carrera
	 * @return {@link HttpStatus.BAD_REQUEST} Si no se especifica una carrera.
	 * {@link HttpStatus.NO_CONTENT} Si no hay habilidades para dicha carrera.
	 * {@link HttpStatus.OK} Si se encontraron habiliades para esa carrera.
	 */
	@RequestMapping(value="/getHabilidadesProfesionales", method=RequestMethod.GET)
	public ResponseEntity<List<HabilidadDTO>> getHabilidadesProfesionales(
			@RequestParam String carrera){
		if(carrera == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(null);
		final List<HabilidadDTO> habilidades = habilidadBean
				.getHabilidadesProfesionales(carrera);
		if(habilidades == null || habilidades.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(habilidades);
	}
	
	/**
	 * Obtiene todas las habilidades sin importar la carrera. Es usado
	 * para el crud de las habilidades.
	 * @return {@link HttpStatus.NO_CONTENT} Si no hay habilidades en
	 * la base de datos.
	 * {@link HttpStatus.OK} Si encontró habilidades.
	 */
	@RequestMapping(value="/getAll", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<HabilidadDTO>> getAll(){
		List<HabilidadDTO> habilidades = habilidadBean.getAll();
		if(habilidades == null || habilidades.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(habilidades);
	}
	
	/**
	 * Actualiza la habilidad especificada en el cuerpo de la solicitud
	 * @param habilidad
	 * @return {@link HttpStatus.OK} Si pudo realizar la operación.
	 * {@link HttpStatus.NO_CONTENT} Si no pudo realizar la actualización
	 * {@link HttpStatus.BAD_REQUEST} Si no se manda la habilidad con los cambios
	 * dentro del cuerpo de la solicitud.
	 */
	@RequestMapping(value="", method=RequestMethod.PATCH)
	public ResponseEntity<Object> update (@RequestBody HabilidadDTO habilidad){
		if(habilidad != null){
			if(habilidadBean.update(habilidad))
				return ResponseEntity.status(HttpStatus.OK).body(null);
			else
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	/**
	 * Crea una nueva habilidad.
	 * @param habilidad
	 * @return {@link HttpStatus.OK} Si pudo realizar la operación.
	 * {@link HttpStatus.NO_CONTENT} Si no pudo realizar la creación.
	 * {@link HttpStatus.BAD_REQUEST} Si no se manda la habilidad con los cambios
	 * dentro del cuerpo de la solicitud.
	 */
	@RequestMapping(value="create", method=RequestMethod.POST)
	public ResponseEntity<Object> create (@RequestBody HabilidadDTO habilidad){
		if(habilidad != null){
			if(habilidadBean.create(habilidad))
				return ResponseEntity.status(HttpStatus.OK).body(null);
			else
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	/**
	 * Elimina una habilidad
	 * @param id
	 * @return {@link HttpStatus.OK} Si pudo realizar la operación.
	 * {@link HttpStatus.NO_CONTENT} Si no pudo realizar la eliminación.
	 * {@link HttpStatus.BAD_REQUEST} Si no especifica el ID de la habilidad
	 * a eliminar.
	 */
	@RequestMapping(value="delete", method=RequestMethod.POST)
	public ResponseEntity<Object> delete (@RequestBody ObjectId id){
		if(id != null){
			if(habilidadBean.delete(id))
				return ResponseEntity.status(HttpStatus.OK).body(null);
			else
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
}
