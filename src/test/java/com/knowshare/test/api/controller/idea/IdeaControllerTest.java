/**
 * 
 */
package com.knowshare.test.api.controller.idea;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;

import com.knowshare.dto.idea.Comentario;
import com.knowshare.dto.idea.IdeaDTO;
import com.knowshare.enterprise.bean.idea.IdeaFacade;
import com.knowshare.enterprise.bean.rules.busqueda.BusquedaIdeaFacade;
import com.knowshare.entities.idea.OperacionIdea;
import com.knowshare.enums.TipoIdeaEnum;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author Miguel Montañez
 *
 */
public class IdeaControllerTest extends AbstractApiTest {
	
	@MockBean
	private IdeaFacade ideaBean;
	
	@MockBean
	private BusquedaIdeaFacade ideaBusq;
	
	private IdeaDTO idea;
	
	private static final String CREAR = "/api/idea/crear";
	private static final String FIND_BY_USUARIO = "/api/idea/findByUsuario/";
	private static final String FIND_BY_USUARIO_PROY = "/api/idea/findByUsuarioPro/";
	private static final String COMMENT = "/api/idea/comentar";
	private static final String LIGHT = "/api/idea/light";
	private static final String FIND_BY_ID = "/api/idea/findById/";
	private static final String SHARE = "/api/idea/compartir";
	private static final String FIND_OPERACION = "/api/idea/findOperacion/";
	private static final String CHANGE_ESTADO = "/api/idea/cambiarestado";
	
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
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(post(CREAR)
				.accept(contentType)
				.content(asJsonString(idea))
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
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
	
	@Test
	public void findByUsuarioTest() throws Exception{
		mockMvc.perform(get(FIND_BY_USUARIO))
			.andExpect(status().isNotFound());
		
		mockMvc.perform(get(FIND_BY_USUARIO+"username"))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(get(FIND_BY_USUARIO+"username")
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		when(ideaBean.findByUsuario(anyString(),anyString(),anyInt(),anyLong()))
			.thenReturn(null);
		mockMvc.perform(get(FIND_BY_USUARIO+"username?timestamp=1505617530706")
				.header("Authorization", getToken()))
			.andExpect(status().isInternalServerError());
		
		when(ideaBean.findByUsuario(anyString(),anyString(),anyInt(),anyLong()))
			.thenReturn(new PageImpl<>(new ArrayList<>()));
		mockMvc.perform(get(FIND_BY_USUARIO+"username?timestamp=1505617530706")
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(ideaBean.findByUsuario(anyString(),anyString(),anyInt(),anyLong()))
			.thenReturn(new PageImpl<>(Arrays.asList(idea)));
		mockMvc.perform(get(FIND_BY_USUARIO+"username?timestamp=1505617530706")
				.header("Authorization", getToken()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.totalElements", is(1)))
			.andExpect(jsonPath("$.last", is(true)))
			.andExpect(jsonPath("$.first", is(true)))
			.andExpect(jsonPath("$.totalPages", is(1)))
			.andExpect(jsonPath("$.number", is(0)))
			.andExpect(jsonPath("$.numberOfElements", is(1)))
			.andExpect(jsonPath("$.content", hasSize(1)))
			.andExpect(jsonPath("$.content[0].id").isNotEmpty())
			.andExpect(jsonPath("$.content[0].tipo").isNotEmpty())
			.andExpect(jsonPath("$.content[0].alcance").isNotEmpty())
			.andExpect(jsonPath("$.content[0].contenido").isNotEmpty())
			.andExpect(jsonPath("$.content[0].usuario").isNotEmpty())
			.andExpect(jsonPath("$.content[0].numeroEstudiantes",is(3)));
	}
	
	@Test
	public void findByUsuarioProTest() throws Exception{
		mockMvc.perform(get(FIND_BY_USUARIO_PROY))
			.andExpect(status().isNotFound());
		
		mockMvc.perform(get(FIND_BY_USUARIO_PROY+"username?timestamp=1505617530706"))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(get(FIND_BY_USUARIO_PROY+"username?timestamp=1505617530706")
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		when(ideaBean.findByUsuarioProyecto(anyString(),anyInt(),anyLong()))
			.thenReturn(null);
		mockMvc.perform(get(FIND_BY_USUARIO_PROY+"username?timestamp=1505617530706")
				.header("Authorization", getToken()))
			.andExpect(status().isInternalServerError());
		
		when(ideaBean.findByUsuarioProyecto(anyString(),anyInt(),anyLong()))
			.thenReturn(new PageImpl<>(new ArrayList<>()));
		mockMvc.perform(get(FIND_BY_USUARIO_PROY+"username?timestamp=1505617530706")
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(ideaBean.findByUsuarioProyecto(anyString(),anyInt(),anyLong()))
			.thenReturn(new PageImpl<>(Arrays.asList(idea)));
		mockMvc.perform(get(FIND_BY_USUARIO_PROY+"username?timestamp=1505617530706")
				.header("Authorization", getToken()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.totalElements", is(1)))
			.andExpect(jsonPath("$.last", is(true)))
			.andExpect(jsonPath("$.first", is(true)))
			.andExpect(jsonPath("$.totalPages", is(1)))
			.andExpect(jsonPath("$.number", is(0)))
			.andExpect(jsonPath("$.numberOfElements", is(1)))
			.andExpect(jsonPath("$.content", hasSize(1)))
			.andExpect(jsonPath("$.content[0].id").isNotEmpty())
			.andExpect(jsonPath("$.content[0].tipo").isNotEmpty())
			.andExpect(jsonPath("$.content[0].alcance").isNotEmpty())
			.andExpect(jsonPath("$.content[0].contenido").isNotEmpty())
			.andExpect(jsonPath("$.content[0].usuario").isNotEmpty())
			.andExpect(jsonPath("$.content[0].numeroEstudiantes",is(3)));
	}
	
	@Test
	public void comentarioTest() throws Exception{
		mockMvc.perform(post(COMMENT)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(post(COMMENT)
				.header("Authorization","bad token")
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(post(COMMENT)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isBadRequest());
		
		when(ideaBean.agregarOperacion(any(), anyObject()))
			.thenReturn(null);
		mockMvc.perform(post(COMMENT)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new Comentario())))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$", is(false)));
		
		when(ideaBean.agregarOperacion(any(), anyObject()))
			.thenReturn(new IdeaDTO());
		mockMvc.perform(post(COMMENT)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new Comentario())))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is(true)));
	}
	
	@Test
	public void lightTest() throws Exception{
		mockMvc.perform(post(LIGHT)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(post(LIGHT)
				.header("Authorization","bad token")
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(post(LIGHT)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isBadRequest());
		
		when(ideaBean.agregarOperacion(any(), anyObject()))
			.thenReturn(null);
		mockMvc.perform(post(LIGHT)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new IdeaDTO())))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$", is(false)));
		
		when(ideaBean.agregarOperacion(any(), anyObject()))
			.thenReturn(new IdeaDTO());
		mockMvc.perform(post(LIGHT)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new IdeaDTO())))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is(true)));
	}
	
	@Test
	public void findByIdTest() throws Exception{
		mockMvc.perform(get(FIND_BY_ID))
			.andExpect(status().isNotFound());
		
		mockMvc.perform(get(FIND_BY_ID+"id"))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(get(FIND_BY_ID+"id")
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		when(ideaBean.findById("id", "username user 1"))
			.thenReturn(null);
		mockMvc.perform(get(FIND_BY_ID+"id")
				.header("Authorization", getToken()))
			.andExpect(status().isInternalServerError());
		
		when(ideaBean.findById("id", "username user 1"))
			.thenReturn(idea);
		mockMvc.perform(get(FIND_BY_ID+"id")
				.header("Authorization", getToken()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.contenido", is("Idea nueva")))
			.andExpect(jsonPath("$.numeroEstudiantes", is(3)))
			.andExpect(jsonPath("$.alcance", is("Alcance")))
			.andExpect(jsonPath("$.tipo",is("NU")))
			.andExpect(jsonPath("$.usuario", is("username user 1")));
	}
	
	@Test
	public void compartirTest() throws Exception{
		mockMvc.perform(post(SHARE)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(post(SHARE)
				.header("Authorization", "bad token")
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(post(SHARE)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isBadRequest());
		
		when(ideaBean.compartir(anyObject(), anyString()))
			.thenReturn(null);
		mockMvc.perform(post(SHARE)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(idea)))
			.andExpect(status().isInternalServerError());
		
		when(ideaBean.compartir(any(), anyString()))
			.thenReturn(idea);
		mockMvc.perform(post(SHARE)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(idea)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.contenido", is("Idea nueva")))
			.andExpect(jsonPath("$.numeroEstudiantes", is(3)))
			.andExpect(jsonPath("$.alcance", is("Alcance")))
			.andExpect(jsonPath("$.tipo",is("NU")))
			.andExpect(jsonPath("$.usuario", is("username user 1")));
	}
	
	@Test
	public void findByOperacionesTest() throws Exception{
		mockMvc.perform(get(FIND_OPERACION))
			.andExpect(status().isNotFound());
		
		mockMvc.perform(get(FIND_OPERACION+"id"))
			.andExpect(status().isNotFound());
		
		mockMvc.perform(get(FIND_OPERACION+"id/tipo"))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(get(FIND_OPERACION+"id/tipo")
				.header("Authorization", "bad token"))
		.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		when(ideaBean.findOperaciones(anyString(), anyString()))
			.thenReturn(new ArrayList<>());
		mockMvc.perform(get(FIND_OPERACION+"id/tipo")
				.header("Authorization", getToken()))
		.andExpect(status().isNoContent());
		
		when(ideaBean.findOperaciones(anyString(), anyString()))
			.thenReturn(Arrays.asList(new OperacionIdea()));
		mockMvc.perform(get(FIND_OPERACION+"id/tipo")
				.header("Authorization", getToken()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(contentType))
		.andExpect(jsonPath("$", hasSize(1)));
	}
	
	@Test
	public void cambiarEstadoTest() throws Exception{
		mockMvc.perform(put(CHANGE_ESTADO))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(put(CHANGE_ESTADO)
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(put(CHANGE_ESTADO)
				.header("Authorization", getToken())
				.contentType(contentType)
				.content(asJsonString(null)))
			.andExpect(status().isBadRequest());
		
		when(ideaBean.cambiarEstado(anyObject()))
			.thenReturn(null);
		mockMvc.perform(put(CHANGE_ESTADO)
				.header("Authorization", getToken())
				.contentType(contentType)
				.content(asJsonString(new IdeaDTO())))
			.andExpect(status().isInternalServerError());
		
		when(ideaBean.cambiarEstado(anyObject()))
			.thenReturn(new IdeaDTO());
		mockMvc.perform(put(CHANGE_ESTADO)
				.header("Authorization", getToken())
				.contentType(contentType)
				.content(asJsonString(new IdeaDTO())))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$").exists());
	}

}
