/**
 * 
 */
package com.knowshare.api.controller.perfilusuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.dto.perfilusuario.HabilidadDTO;
import com.knowshare.enterprise.bean.habilidad.HabilidadFacade;
import com.knowshare.entities.perfilusuario.Habilidad;

/**
 * @author miguel
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/habilidad")
public class HabilidadController {
	
	@Autowired
	private HabilidadFacade habilidadBean;
	
	@RequestMapping(value="/getHabilidades", method=RequestMethod.GET)
	public ResponseEntity<List<HabilidadDTO>> getHabilidades(
			@RequestParam String carrera){
		if(carrera == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(null);
		final List<HabilidadDTO> habilidades = habilidadBean.getHabilidades(carrera.toLowerCase());
		if(habilidades.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(habilidades);
	}
	
	/**
	 * Example of pagination
	 * @return
	 */
	@RequestMapping(value="/findAll", method=RequestMethod.GET)
	public Page<Habilidad> findAll(){
		return habilidadBean.getAll();
	}

}
