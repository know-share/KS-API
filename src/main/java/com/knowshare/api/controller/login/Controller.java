package com.knowshare.api.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.enterprise.bean.habilidad.HabilidadFacade;

@RestController
public class Controller {
	
	@Autowired
	private HabilidadFacade habilidadBean;
	
	@RequestMapping(value="/hello", method=RequestMethod.GET)
	public String sum(){
		habilidadBean.createHabilidad("habilidad2");
		return "";
	}
}

