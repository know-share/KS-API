/**
 * 
 */
package com.knowshare.test.api.controller.perfilusuario;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.knowshare.api.security.JWTFilter;
import com.knowshare.dto.academia.CarreraDTO;
import com.knowshare.dto.perfilusuario.AuthDTO;
import com.knowshare.dto.perfilusuario.UsuarioDTO;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;
import com.knowshare.enterprise.repository.app.UserSessionRepository;
import com.knowshare.entities.app.UserSession;
import com.knowshare.entities.perfilusuario.Personalidad;
import com.knowshare.enums.PreferenciaIdeaEnum;
import com.knowshare.enums.TipoUsuariosEnum;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author miguel
 *
 */
public class UsuarioControllerTest extends AbstractApiTest{
	
	@MockBean
	private UsuarioFacade usuarioBean;
	
	@MockBean
	private UserSessionRepository userSessionRepository;
	
	private UsuarioDTO usuario;
	private UserSession userSession;
	
	private static final String IS_USERNAME_TAKEN = "/api/usuario/isUsernameTaken";
	private static final String CREATE_USER = "/api/usuario";
	private static final String GET_USER = "/api/usuario/get";
	
	@Before
	public void setup(){
		usuario = new UsuarioDTO()
				.setAmigos(new ArrayList<>())
				.setApellido("Apellido user 1")
				.setAreasConocimiento(new ArrayList<>())
				.setCantidadAmigos(0)
				.setCarrera(new CarreraDTO().setNombre("carrera 1"))
				.setCantidadSeguidores(0)
				.setCualidades(new ArrayList<>())
				.setEnfasis(new ArrayList<>())
				.setGustos(new ArrayList<>())
				.setHabilidades(new ArrayList<>())
				.setInsignias(Arrays.asList("insignia 1","insignia 2","insignia 3"))
				.setNombre("Nombre user 1")
				.setPersonalidad(new Personalidad().setNombre("ENJP"))
				.setSeguidores(new ArrayList<>())
				.setTipoUsuario(TipoUsuariosEnum.ESTUDIANTE)
				.setUsername("username user 1")
				.setPassword("Password$")
				.setPreferenciaIdea(PreferenciaIdeaEnum.POR_RELEVANCIA);
		AuthDTO authDTO = new AuthDTO()
				.setPassword("Password$")
				.setUsername("username user 1");
		userSession = JWTFilter.generateToken(authDTO);
	}
	
	@Test
	public void isUsernameTakenTest() throws Exception{
		mockMvc.perform(get(IS_USERNAME_TAKEN))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.isUsernameTaken(anyString()))
			.thenReturn(true);
		mockMvc.perform(get(IS_USERNAME_TAKEN+"?username=AnyUsername"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$").isBoolean());
	}
	
	@Test
	public void crearUsuarioTest() throws Exception{
		mockMvc.perform(post(CREATE_USER)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.crearUsuario(anyObject()))
			.thenReturn(false);
		mockMvc.perform(post(CREATE_USER)
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new UsuarioDTO())))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$").isBoolean());
		
		when(usuarioBean.crearUsuario(anyObject()))
			.thenReturn(true);
		mockMvc.perform(post(CREATE_USER)
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new UsuarioDTO())))
			.andExpect(status().isCreated());
	}
	
	@Test
	public void getUsuario() throws Exception{
		mockMvc.perform(get(GET_USER))
			.andExpect(status().isNotFound());
		
		mockMvc.perform(get(GET_USER+"/usersearch"))
			.andExpect(status().isBadRequest());
		
		mockMvc.perform(get(GET_USER+"/usersearch")
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString())).thenReturn(userSession);
		when(usuarioBean.getUsuario(anyString())).thenReturn(null);
		mockMvc.perform(get(GET_USER+"/usersearch")
				.header("Authorization", userSession.getToken()))
			.andExpect(status().isNoContent());
		
		when(usuarioBean.getUsuario(anyString())).thenReturn(usuario);
		mockMvc.perform(get(GET_USER+"/usersearch")
				.header("Authorization", userSession.getToken()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.nombre",is("Nombre user 1")))
			.andExpect(jsonPath("$.amigos", hasSize(0)))
			.andExpect(jsonPath("$.apellido", is("Apellido user 1")))
			.andExpect(jsonPath("$.areasConocimiento", hasSize(0)))
			.andExpect(jsonPath("$.cantidadAmigos", is(0)))
			.andExpect(jsonPath("$.carrera.nombre", is("carrera 1")))
			.andExpect(jsonPath("$.cantidadSeguidores", is(0)))
			.andExpect(jsonPath("$.cualidades", hasSize(0)))
			.andExpect(jsonPath("$.enfasis", hasSize(0)))
			.andExpect(jsonPath("$.gustos", hasSize(0)))
			.andExpect(jsonPath("$.habilidades", hasSize(0)))
			.andExpect(jsonPath("$.insignias", hasSize(3)))
			.andExpect(jsonPath("$.insignias[0]", is("insignia 1")))
			.andExpect(jsonPath("$.insignias[1]", is("insignia 2")))
			.andExpect(jsonPath("$.insignias[2]", is("insignia 3")))
			.andExpect(jsonPath("$.personalidad.nombre", is("ENJP")))
			.andExpect(jsonPath("$.seguidores", hasSize(0)))
			.andExpect(jsonPath("$.tipoUsuario", is("ESTUDIANTE")))
			.andExpect(jsonPath("$.username", is("username user 1")))
			.andExpect(jsonPath("$.preferenciaIdea", is("POR_RELEVANCIA")));
	}

}
