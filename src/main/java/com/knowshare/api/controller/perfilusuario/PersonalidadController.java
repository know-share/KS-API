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
 * @author miguel
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/personalidad")
public class PersonalidadController{
	
	@Autowired
	private PersonalidadFacade personalidadBean;
	
	@RequestMapping(value="/findAll", method=RequestMethod.GET)
	public ResponseEntity<List<Personalidad>> getAllPersonalidades(){
		List<Personalidad> personalidades = personalidadBean.getAllPersonalidades();
		if(personalidades.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(personalidades);
	}

}