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

import com.knowshare.enterprise.bean.trabajogrado.TrabajoGradoFacade;
import com.knowshare.entities.academia.TrabajoGrado;

/**
 * Endpoints para operaciones con objeto de tipo
 * {@link TrabajoGrado}
 * @author Pablo Gaitan
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tg")
public class TrabajoGradoController {
	
	@Autowired
	private TrabajoGradoFacade tgBean;
	
	/**
	 * Retorna la lista de trabajos de grado registrados dentro
	 * de la aplicación
	 * @return Si la lista no está vacía retorna esa lista con 
	 * estado HttpStatus.OK. Si la lista está vacía, retorna
	 * HttpStatus.NO_CONTENT 
	 */
	@RequestMapping(value="/findAll" ,method = RequestMethod.GET)
	public ResponseEntity<Object> findAllTg(){
		List<TrabajoGrado> tgs = tgBean.findAll();
		if(tgs == null || tgs.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(tgs);
	}
}
