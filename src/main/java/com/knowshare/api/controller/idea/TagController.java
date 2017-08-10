package com.knowshare.api.controller.idea;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
	public ResponseEntity<?> findAllTags(
			@RequestHeader("Authorization") String token){
		List<Tag> tags = tagBean.findAll();
		if(tags == null || tags.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(tags);
	}
	
	@RequestMapping(value="create", method=RequestMethod.POST)
	public ResponseEntity<?> create (@RequestBody String tag){
		if(tag != null){
			if(tagBean.create(tag))
				return ResponseEntity.status(HttpStatus.OK).body(null);
			else
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@RequestMapping(value="", method=RequestMethod.PATCH)
	public ResponseEntity<?> update (@RequestBody Tag tag){
		if(tag != null){
			if(tagBean.update(tag))
				return ResponseEntity.status(HttpStatus.OK).body(null);
			else
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	

	@RequestMapping(value="delete/{id:.+}", method=RequestMethod.DELETE)
	public ResponseEntity<?> delete (@PathVariable String id){
		if(tagBean.delete(id)) {
			return ResponseEntity.status(HttpStatus.OK).body(null); 
		}
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
	}
}
