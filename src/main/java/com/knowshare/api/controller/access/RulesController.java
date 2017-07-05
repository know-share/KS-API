/**
 * 
 */
package com.knowshare.api.controller.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.enterprise.bean.rules.RuleTest;

/**
 * Inicialmente controlador para pruebas
 * @author miguel
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/rules")
public class RulesController {
	
	@Autowired
	private RuleTest ruleTest;
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public void testMethodRules(){
		ruleTest.methodTest("Hola");
	}
}
