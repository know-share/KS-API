/**
 * 
 */
package com.knowshare.api.controller.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.enterprise.bean.rules.config.RulesAdminFacade;
import com.knowshare.enterprise.bean.rules.usuarios.RecomendacionConexionFacade;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;

/**
 * Controlador para reglas de negocio dentro de la aplicación de
 * KnowShare
 * @author miguel
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/rules")
public class RulesController {
	
	@Autowired
	private RecomendacionConexionFacade ruleTest;
	
	@Autowired
	private RulesAdminFacade rulesAdminBean;
	
	@Autowired
	private UsuarioFacade bean;
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public void testMethodRules(){
		ruleTest.recomendacionesUsuario(bean.getUsuario("MinMiguelM"));
	}
	
	@RequestMapping(value="/update", method=RequestMethod.GET)
	public void updateRules(){
		rulesAdminBean.updateRules();
	}
}
