/**
 * 
 */
package com.knowshare.api.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.enterprise.bean.dashboards.DashboardsFacade;

/**
 * @author Felipe Bautista
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminController{

	@Autowired
	private DashboardsFacade dashboardBean;
	
	
	@RequestMapping(value="/getUsuarios", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<Integer>> 
		getEstudiantes( @RequestParam String carrera){
		if(carrera == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(null);
		List<Integer> usuarios = dashboardBean.generoCarrera(carrera);
		if(usuarios == null)
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(usuarios);
	}
	
	@RequestMapping(value="/getTags", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<Map<String,Integer>> 
		getTags( ){
		Map<String,Integer>map = dashboardBean.usoTags();
		if(map == null)
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(map);
	}
}