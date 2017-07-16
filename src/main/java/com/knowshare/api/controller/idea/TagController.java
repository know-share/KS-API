package com.knowshare.api.controller.idea;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.api.security.JWTFilter;
import com.knowshare.enterprise.bean.tag.TagFacade;
import com.knowshare.enterprise.repository.app.UserSessionRepository;
import com.knowshare.entities.app.UserSession;
import com.knowshare.entities.idea.Tag;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tag")
public class TagController {
	
	@Autowired
	private TagFacade tagBean;
	
	@Autowired
	private UserSessionRepository userSessionRepository;
	
	@RequestMapping(value="/findAll" ,method = RequestMethod.GET)
	public ResponseEntity<?> findAllTags(
			@RequestHeader("Authorization") String token){
		UserSession user = userSessionRepository.findByToken(token);
		if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
		}
		String username = JWTFilter.getSub(token, user.getSecretKey());
		if(!username.equalsIgnoreCase(user.getUsername()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		List<Tag> tags = tagBean.findAll();
		if(tags == null || tags.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(tags);
	}
}
