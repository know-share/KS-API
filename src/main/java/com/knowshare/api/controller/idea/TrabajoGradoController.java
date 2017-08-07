/**
 * 
 */
package com.knowshare.api.controller.idea;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.enterprise.bean.trabajoGrado.TrabajoGradoFacade;
import com.knowshare.entities.academia.TrabajoGrado;


/**
 * @author Pablo
 *
 */

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tg")
public class TrabajoGradoController {
	
	@Autowired
	private TrabajoGradoFacade tgBean;
	
	@RequestMapping(value="/findAll" ,method = RequestMethod.GET)
	public ResponseEntity<?> findAllTg(){
		List<TrabajoGrado> tgs = tgBean.findAll();
		if(tgs == null || tgs.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(tgs);
	}
}
