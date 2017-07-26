/**
 * 
 */
package com.knowshare.api.controller.perfilusuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.enterprise.bean.personalidad.PersonalidadFacade;
import com.knowshare.entities.perfilusuario.Personalidad;

/**
 * Endpoints para operaciones con objeto de tipo {@link Personalidad}
 * @author Miguel Montañez
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/personalidad")
public class PersonalidadController{
	
	@Autowired
	private PersonalidadFacade personalidadBean;
	
	/**
	 * Obtiene todas las personalidades registradas en la aplicación
	 * @return {@link HttpStatus.OK} si la lista de personalidades tiene
	 * al menos un elemento, si no {@link HttpStatus.NO_CONTENT}
	 */
	@RequestMapping(value="/findAll", method=RequestMethod.GET)
	public ResponseEntity<List<Personalidad>> getAllPersonalidades(){	
		List<Personalidad> personalidades = personalidadBean.getAllPersonalidades();
		if(personalidades == null || personalidades.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(personalidades);
	}

}
