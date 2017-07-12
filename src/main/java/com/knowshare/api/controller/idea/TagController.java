package com.knowshare.api.controller.idea;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.enterprise.bean.tag.TagFacade;
import com.knowshare.entities.idea.Tag;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tag")
public class TagController {
	
	@Autowired
	private TagFacade tagBean;
	
	
	@RequestMapping(value="/findAll" ,method = RequestMethod.GET)
	public ResponseEntity<?> findAllTags(){
		List<Tag> tags = tagBean.findAll();
		if(tags == null || tags.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(tags);
	}
}
