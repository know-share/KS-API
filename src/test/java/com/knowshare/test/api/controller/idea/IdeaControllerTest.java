/**
 * 
 */
package com.knowshare.test.api.controller.idea;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.knowshare.dto.idea.IdeaDTO;
import com.knowshare.enterprise.bean.idea.IdeaFacade;
import com.knowshare.enums.TipoIdeaEnum;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author miguel
 *
 */
public class IdeaControllerTest extends AbstractApiTest {
	
	@MockBean
	private IdeaFacade ideaBean;
	
	private IdeaDTO idea;
	
	private static final String CREAR = "/api/idea/crear";
	
	@Before
	public void setup(){
		idea = new IdeaDTO()
				.setId(new String("id"))
				.setAlcance("Alcance")
				.setContenido("Idea nueva")
				.setNumeroEstudiantes(3)
				.setUsuario("username user 1")
				.setTipo(TipoIdeaEnum.NU);
	}
	
	@Test
	public void crearIdeaTest() throws Exception{
		mockMvc.perform(post(CREAR)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isBadRequest());
		
		mockMvc.perform(post(CREAR)
				.accept(contentType)
				.content(asJsonString(idea))
				.contentType(contentType))
			.andExpect(status().isBadRequest());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(null);
		mockMvc.perform(post(CREAR)
				.header("Authorization", getToken())
				.accept(contentType)
				.content(asJsonString(idea))
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		when(ideaBean.crearIdea(any()))
			.thenReturn(idea);
		mockMvc.perform(post(CREAR)
				.header("Authorization", getToken())
				.accept(contentType)
				.content(asJsonString(idea))
				.contentType(contentType))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.contenido", is("Idea nueva")))
			.andExpect(jsonPath("$.numeroEstudiantes", is(3)))
			.andExpect(jsonPath("$.alcance", is("Alcance")))
			.andExpect(jsonPath("$.tipo",is("NU")))
			.andExpect(jsonPath("$.usuario", is("username user 1")));
	}

}
