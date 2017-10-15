package com.knowshare.api.controller.idea;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.enterprise.bean.tag.TagFacade;
import com.knowshare.entities.idea.Tag;

/**
 * Endpoints para operaciones con objeto de tipo
 * {@link Tag}
 * @author Miguel Montañez
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tag")
public class TagController {
	
	@Autowired
	private TagFacade tagBean;
	
	/**
	 * Obtiene todos los tags en la base de datos.
	 * @return {@link HttpStatus.NO_CONTENT} Si no hay datos.
	 * {@link HttpStatus.OK} Si hay tags, los envía como respuesta. 
	 */
	@RequestMapping(value="/findAll" ,method = RequestMethod.GET)
	public ResponseEntity<Object> findAllTags(){
		List<Tag> tags = tagBean.findAll();
		if(tags == null || tags.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(tags);
	}
	
	/**
	 * Crea un nuevo tag
	 * @param tag 
	 * @return {@link HttpStatus.OK} Si pudo crear el tag.
	 * {@link HttpStatus.NO_CONTENT} Si no pudo crear el tag.
	 * {@link HttpStatus.BAD_REQUEST} Si no manda en el cuerpo de 
	 * la solicitud el nombre para el tag a crear.
	 */
	@RequestMapping(value="create", method=RequestMethod.POST)
	public ResponseEntity<Object> create (@RequestBody String tag){
		if(tag != null){
			if(tagBean.create(tag))
				return ResponseEntity.status(HttpStatus.OK).body(null);
			else
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	/**
	 * Actualiza el tag especificado en el cuerpo de la solicitud.
	 * @param tag
	 * @return {@link HttpStatus.OK} Si pudo realizar la actualización.
	 * {@link HttpStatus.NO_CONTENT} Si no pudo realizar la actualización.
	 * {@link HttpStatus.BAD_REQUEST} Si no se especifica el tag para 
	 * realizar la operación.
	 */
	@RequestMapping(value="", method=RequestMethod.PATCH)
	public ResponseEntity<Object> update (@RequestBody Tag tag){
		if(tag != null){
			if(tagBean.update(tag))
				return ResponseEntity.status(HttpStatus.OK).body(null);
			else
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	/**
	 * Elimina el tag con el id especificado en la solicitud.
	 * @param id
	 * @return {@link HttpStatus.OK} Si pudo realizar la eliminación.
	 * {@link HttpStatus.NOT_MODIFIED} Si no pudo realizar la eliminación.
	 */
	@RequestMapping(value="delete/{id:.+}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> delete (@PathVariable String id){
		if(tagBean.delete(id)) {
			return ResponseEntity.status(HttpStatus.OK).body(null); 
		}
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
	}
}
