/**
 * 
 */
package com.knowshare.api.controller.academia;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.dto.academia.CarreraDTO;
import com.knowshare.enterprise.bean.carrera.CarreraFacade;

/**
 * @author miguel
 *
 */
@RestController
@RequestMapping("/carrera")
public class CarreraController {
	
	@Autowired
	private CarreraFacade carreraBean;
	
	@RequestMapping(value="/findAll", method=RequestMethod.GET)
	public ResponseEntity<List<CarreraDTO>> getAllCarreras(){
		return ResponseEntity.status(HttpStatus.OK)
				.body(carreraBean.getAllCarreras());
	}

}
