package com.knowshare.api.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.enterprise.repository.carrera.CarreraRepository;
import com.knowshare.enterprise.repository.habilidad.HabilidadRepository;
import com.knowshare.entities.academia.Carrera;
import com.knowshare.entities.perfilusuario.Habilidad;
import com.knowshare.enums.TipoHabilidadEnum;

@RestController
public class Controller {
	
	@Autowired
	private CarreraRepository carreraRepository;
	
	@Autowired
	private HabilidadRepository habilidadRepository;
	
	@RequestMapping(value="/hello", method=RequestMethod.GET)
	public String sum(){
		Carrera c = new Carrera().setNombre("carrera11")
				.setFacultad("facultad");
		c = carreraRepository.insert(c);
		
		Habilidad h = new Habilidad().setNombre("hello")
				.setTipo(TipoHabilidadEnum.PROFESIONALES)
				.setCarrera(c);
		
		habilidadRepository.insert(h);
		return "hello";
	}
}

