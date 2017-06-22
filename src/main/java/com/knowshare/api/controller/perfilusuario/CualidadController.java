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

import com.knowshare.enterprise.bean.cualidad.CualidadFacade;
import com.knowshare.entities.perfilusuario.Cualidad;

/**
 * @author miguel
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cualidad")
public class CualidadController {
	
	@Autowired
	private CualidadFacade cualidadBean;
	
	@RequestMapping(value="/findAll", method=RequestMethod.GET)
	public ResponseEntity<List<Cualidad>> findAll(){
		List<Cualidad> cualidades = cualidadBean.getAll();
		if(cualidades.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(cualidades);
	}

}
