/**
 * 
 */
package com.knowshare.api.controller.perfilusuario;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.dto.perfilusuario.CualidadDTO;
import com.knowshare.enterprise.bean.cualidad.CualidadFacade;

/**
 * @author miguel
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cualidad")
public class CualidadController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CualidadFacade cualidadBean;
	
	@RequestMapping(value="/findAll", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<CualidadDTO>> findAll(){
		logger.debug(":::: Start method findAll() in CualidadController ::::");
		List<CualidadDTO> cualidades = cualidadBean.getAll();
		if(cualidades == null || cualidades.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(cualidades);
	}

}
