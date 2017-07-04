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

import com.knowshare.enterprise.bean.gusto.GustoFacade;
import com.knowshare.entities.perfilusuario.Gusto;

/**
 * @author miguel
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/gusto")
public class GustoController {
	
	@Autowired
	private GustoFacade gustoBean;
	
	@RequestMapping(value="/findAll", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<Gusto>> findAll(){
		List<Gusto> gustos = gustoBean.getAllGustos();
		if(gustos == null || gustos.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(gustos);
	}

}
