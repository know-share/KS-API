/**
 * 
 */
package com.knowshare.api.controller.academia;

import java.util.List;

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

import com.knowshare.dto.academia.CarreraDTO;
import com.knowshare.dto.academia.EnfasisAreaConocimientoDTO;
import com.knowshare.enterprise.bean.carrera.CarreraFacade;

/**
 * Clase controller que permite la comunicación entre el 
 * cliente y el servidor.
 * @author Miguel Montañez
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/carrera")
public class CarreraController{
	
	@Autowired
	private CarreraFacade carreraBean;
	
	/**
	 * Descripción: busca todas las Carreras y las retorna.
	 * @return OK si se logro con exito, NO_CONTENT si no se encontro nada en el servidor.
	 */
	@RequestMapping(value="/findAll", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<CarreraDTO>> getAllCarreras(){
		List<CarreraDTO> carreras = carreraBean.getAllCarreras();
		if(carreras == null || carreras.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
			.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(carreras);
	}
	
	/**
	 * Descripción:
	 * @param carrera
	 * @return
	 */
	@RequestMapping(value="/getEnfasisAreaConocimiento", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<EnfasisAreaConocimientoDTO> 
		getEnfasisAreaConocimiento(@RequestParam String carrera){
		if(carrera == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(null);
		EnfasisAreaConocimientoDTO dto = carreraBean.getEnfasisAreaConocimiento(carrera);
		if(dto == null)
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(dto);
	}
	
	/**
	 * Descripción: actualiza una Carrera.
	 * @param carrera
	 * @return OK si logro actualizar con éxito, NO_CONTENT si no encontro la Carrera a actualizar, 
	 * BAD_REQUEST si hubo una falla en sintaxis. 
	 */
	@RequestMapping(value="", method=RequestMethod.PATCH)
	public ResponseEntity<Object> update (@RequestBody CarreraDTO carrera){
		if(carrera != null){
			if(carreraBean.update(carrera))
				return ResponseEntity.status(HttpStatus.OK).body(null);
			else
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	/**
	 * Descripción: actualiza los enfasis de una Carrera.
	 * @param carrera
	 * @return OK si logro actualizar con éxito, NO_CONTENT si no encontro la Carrera a actualizar, 
	 * BAD_REQUEST si hubo una falla en sintaxis. 
	 */
	@RequestMapping(value="updateEnfasis", method=RequestMethod.PATCH)
	public ResponseEntity<Object> updateEnfasis (@RequestBody CarreraDTO carrera){
		if(carrera != null){
			if(carreraBean.updateEnfasis(carrera))
				return ResponseEntity.status(HttpStatus.OK).body(null);
			else
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	/**
	 * Descripción: elimina una Carrera
	 * @param id
	 * @return Ok si elimina con éxito, NOT_MODIFIED si hubo algún error en el proceso de eliminar. 
	 */
	@RequestMapping(value="delete/{id:.+}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> delete (@PathVariable String id){
		if(carreraBean.delete(id)) {
			return ResponseEntity.status(HttpStatus.OK).body(null); 
		}
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
	}
	
	/**
	 * Descripción: crea una Carrera
	 * @param carrera
	 * @return Ok si creó con éxito, NO_CONTENT si no se encontró la carrera a crear, 
	 * BAD_REQUEST si hubo una falla en sintaxis.
	 */
	@RequestMapping(value="create", method=RequestMethod.POST)
	public ResponseEntity<Object> create (@RequestBody CarreraDTO carrera){
		if(carrera != null){
			if(carreraBean.create(carrera))
				return ResponseEntity.status(HttpStatus.OK).body(null);
			else
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
}
