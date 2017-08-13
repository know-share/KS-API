/**
 * 
 */
package com.knowshare.test.api.controller.idea;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.knowshare.enterprise.bean.tag.TagFacade;
import com.knowshare.entities.idea.Tag;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author Miguel Montañez
 *
 */
public class TagControllerTest extends AbstractApiTest{
	
	@MockBean
	private TagFacade tagBean;
	
	private List<Tag> tags;
	
	private static final String FIND_ALL = "/api/tag/findAll";
	private static final String CREATE = "/api/tag/create";
	private static final String UPDATE = "/api/tag";
	private static final String DELETE = "/api/tag/delete/";
	
	@Before
	public void setup(){
		tags = new ArrayList<>();
		tags.add(new Tag()
				.setId("id tag1")
				.setNombre("tag1"));
		tags.add(new Tag()
				.setId("id tag2")
				.setNombre("tag2"));
		tags.add(new Tag()
				.setId("id tag3")
				.setNombre("tag3"));
	}
	
	@Test
	public void findAllTagsTest() throws Exception{
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(get(FIND_ALL)
				.header("Authorization", "bad request"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		when(tagBean.findAll())
			.thenReturn(null);
		mockMvc.perform(get(FIND_ALL)
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(tagBean.findAll())
			.thenReturn(new ArrayList<>());
		mockMvc.perform(get(FIND_ALL)
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(tagBean.findAll())
			.thenReturn(tags);
		mockMvc.perform(get(FIND_ALL)
				.header("Authorization", getToken()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[0].id", is("id tag1")))
			.andExpect(jsonPath("$[0].nombre", is("tag1")))
			.andExpect(jsonPath("$[1].id", is("id tag2")))
			.andExpect(jsonPath("$[1].nombre", is("tag2")))
			.andExpect(jsonPath("$[2].id", is("id tag3")))
			.andExpect(jsonPath("$[2].nombre", is("tag3")));
	}
	
	@Test
	public void createTest() throws Exception{
		mockMvc.perform(post(CREATE)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(post(CREATE)
				.header("Authorization", "bad token")
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(post(CREATE)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isBadRequest());
		
		when(tagBean.create(anyString()))
			.thenReturn(false);
		mockMvc.perform(post(CREATE)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString("new tag")))
			.andExpect(status().isNoContent());
		
		when(tagBean.create(anyString()))
			.thenReturn(true);
		mockMvc.perform(post(CREATE)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString("new tag")))
			.andExpect(status().isOk());
	}
	
	@Test
	public void updateTest() throws Exception{
		mockMvc.perform(patch(UPDATE)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(patch(UPDATE)
				.header("Authorization", "bad token")
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(patch(UPDATE)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isBadRequest());
		
		when(tagBean.update(anyObject()))
			.thenReturn(false);
		mockMvc.perform(patch(UPDATE)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new Tag())))
			.andExpect(status().isNoContent());
		
		when(tagBean.update(anyObject()))
			.thenReturn(true);
		mockMvc.perform(patch(UPDATE)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new Tag())))
			.andExpect(status().isOk());
	}

	@Test
	public void deleteTest() throws Exception{
		mockMvc.perform(delete(DELETE))
			.andExpect(status().isNotFound());
		
		mockMvc.perform(delete(DELETE+"id"))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(delete(DELETE+"id")
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		when(tagBean.delete(anyString()))
			.thenReturn(false);
		mockMvc.perform(delete(DELETE+"id")
				.header("Authorization", getToken()))
			.andExpect(status().isNotModified());
		
		when(tagBean.delete(anyString()))
			.thenReturn(true);
		mockMvc.perform(delete(DELETE+"id")
				.header("Authorization", getToken()))
			.andExpect(status().isOk());
	}
}
