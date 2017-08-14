/**
 * 
 */
package com.knowshare.api.controller.perfilusuario;

import java.util.List;

import org.bson.types.ObjectId;
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

import com.knowshare.dto.perfilusuario.HabilidadDTO;
import com.knowshare.enterprise.bean.habilidad.HabilidadFacade;
import com.knowshare.entities.perfilusuario.Habilidad;


/**
 * @author Miguel Montañez
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/habilidad")
public class HabilidadController {
	
	@Autowired
	private HabilidadFacade habilidadBean;
	
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
	 * Example of pagination
	 * this method is not complete, therefore is not tested
	 * @return
	 */
	@RequestMapping(value="/findAll", method=RequestMethod.GET)
	public Page<Habilidad> findAll(
			@RequestParam(defaultValue="0") Integer page){
		return habilidadBean.getAll(page);
	}
	
	@RequestMapping(value="/getAll", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<HabilidadDTO>> getAll(){
		List<HabilidadDTO> habilidades = habilidadBean.getAll();
		if(habilidades == null || habilidades.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
			.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(habilidades);
	}
	
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
