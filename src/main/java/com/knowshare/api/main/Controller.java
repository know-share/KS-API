package com.knowshare.api.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.enterprise.bean.ISum;

@RestController
public class Controller {
	
	@Autowired
	private ISum sumi;
	
	@RequestMapping(value="/hello", method=RequestMethod.GET)
	public String sum(){
		return ""+sumi.sum(4,5);
	}

}
