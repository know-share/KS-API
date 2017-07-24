/**
 * 
 */
package com.knowshare.test.api.controller.perfilusuario;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.knowshare.dto.academia.CarreraDTO;
import com.knowshare.dto.perfilusuario.UsuarioDTO;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;
import com.knowshare.entities.academia.FormacionAcademica;
import com.knowshare.entities.academia.TrabajoGrado;
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
	
	private UsuarioDTO usuario;
	
	private static final String IS_USERNAME_TAKEN = "/api/usuario/isUsernameTaken";
	private static final String CREATE_USER = "/api/usuario";
	private static final String GET_USER = "/api/usuario/get";
	private static final String SEGUIR = "/api/usuario/seguir";
	private static final String DEJAR_SEGUIR = "/api/usuario/dejarseguir";
	private static final String SOLICITUD = "/api/usuario/solicitud";
	private static final String ADD_TG = "/api/usuario/addTG";
	private static final String ADD_FA = "/api/usuario/addFormacionAcademica";
	private static final String ELIMINAR_AMIGO = "/api/usuario/eliminarAmigo";
	
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
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(usuarioBean.getUsuario(anyString())).thenReturn(usuario);
		mockMvc.perform(get(GET_USER+"/usersearch")
				.header("Authorization", getToken()))
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
	
	@Test
	public void seguirTest() throws Exception{
		mockMvc.perform(put(SEGUIR+"/userToFollow"))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.isUsernameTaken(anyString()))
			.thenReturn(false);
		mockMvc.perform(put(SEGUIR+"/userToFollow")
				.header("Authorization", getToken()))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(put(SEGUIR+"/userToFollow")
				.header("Authorization", getToken()))
			.andExpect(status().isNotFound()); 
		
		when(usuarioBean.isUsernameTaken(anyString()))
			.thenReturn(true);
		when(usuarioBean.seguir(anyString(), anyString()))
			.thenReturn(false);
		mockMvc.perform(put(SEGUIR+"/userToFollow")
				.header("Authorization", getToken()))
			.andExpect(status().isNotModified());
		
		when(usuarioBean.seguir(anyString(), anyString()))
			.thenReturn(true);
		mockMvc.perform(put(SEGUIR+"/userToFollow")
				.header("Authorization", getToken()))
			.andExpect(status().isOk());
	}

	@Test
	public void unfollowTest() throws Exception{
		mockMvc.perform(put(DEJAR_SEGUIR+"/userToUnFollow"))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.isUsernameTaken(anyString()))
			.thenReturn(false);
		mockMvc.perform(put(DEJAR_SEGUIR+"/userToUnFollow")
				.header("Authorization", getToken()))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(put(DEJAR_SEGUIR+"/userToUnFollow")
				.header("Authorization", getToken()))
			.andExpect(status().isNotFound()); 
		
		when(usuarioBean.isUsernameTaken(anyString()))
			.thenReturn(true);
		when(usuarioBean.dejarSeguir(anyString(), anyString()))
			.thenReturn(false);
		mockMvc.perform(put(DEJAR_SEGUIR+"/userToUnFollow")
				.header("Authorization", getToken()))
			.andExpect(status().isNotModified());
		
		when(usuarioBean.dejarSeguir(anyString(), anyString()))
			.thenReturn(true);
		mockMvc.perform(put(DEJAR_SEGUIR+"/userToUnFollow")
				.header("Authorization", getToken()))
			.andExpect(status().isOk());
	}
	
	@Test
	public void solicitudAmistadTest() throws Exception{
		mockMvc.perform(put(SOLICITUD+"/targetUser"))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.isUsernameTaken(anyString()))
			.thenReturn(false);
		mockMvc.perform(put(SOLICITUD+"/targetUser")
				.header("Authorization", getToken()))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(put(SOLICITUD+"/targetUser")
				.header("Authorization", getToken()))
			.andExpect(status().isNotFound());
		
		when(usuarioBean.isUsernameTaken(anyString()))
			.thenReturn(true);
		when(usuarioBean.solicitudAmistad(anyString(), anyString()))
			.thenReturn(false);
		mockMvc.perform(put(SOLICITUD+"/targetUser")
				.header("Authorization", getToken()))
			.andExpect(status().isNotModified());
		
		when(usuarioBean.solicitudAmistad(anyString(), anyString()))
			.thenReturn(true);
		mockMvc.perform(put(SOLICITUD+"/targetUser")
				.header("Authorization", getToken()))
			.andExpect(status().isOk());
		
		mockMvc.perform(put(SOLICITUD+"/targetUser?action=novalid")
				.header("Authorization", getToken()))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.accionSolicitud(anyString(), anyString(), eq("ACCEPT")))
			.thenReturn(true);
		mockMvc.perform(put(SOLICITUD+"/targetUser?action=ACCEPT")
				.header("Authorization", getToken()))
			.andExpect(status().isOk());
		
		when(usuarioBean.accionSolicitud(anyString(), anyString(), eq("ACCEPT")))
			.thenReturn(false);
		mockMvc.perform(put(SOLICITUD+"/targetUser?action=ACCEPT")
				.header("Authorization", getToken()))
			.andExpect(status().isNotModified());
	}
	
	@Test
	public void addTGTest() throws Exception{
		final TrabajoGrado tg= new TrabajoGrado();
		
		mockMvc.perform(post(ADD_TG)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isBadRequest());
		
		mockMvc.perform(post(ADD_TG)
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(tg)))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.agregarTGDirigido(anyObject(), anyString()))
			.thenReturn(false);
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(post(ADD_TG)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(tg)))
			.andExpect(status().isInternalServerError());
		
		when(usuarioBean.agregarTGDirigido(anyObject(), anyString()))
			.thenReturn(true);
		mockMvc.perform(post(ADD_TG)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(tg)))
			.andExpect(status().isCreated());
	}
	
	@Test
	public void addFormacionAcademicaTest() throws Exception{
		final FormacionAcademica fa = new FormacionAcademica();
		
		mockMvc.perform(post(ADD_FA)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isBadRequest());
		
		mockMvc.perform(post(ADD_FA)
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(fa)))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.agregarFormacionAcademica(anyObject(), anyString()))
			.thenReturn(false);
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(post(ADD_FA)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(fa)))
			.andExpect(status().isInternalServerError());
		
		when(usuarioBean.agregarFormacionAcademica(anyObject(), anyString()))
			.thenReturn(true);
		mockMvc.perform(post(ADD_FA)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(fa)))
			.andExpect(status().isCreated());
	}
	
	@Test
	public void eliminarAmigoTest() throws Exception{
		mockMvc.perform(put(ELIMINAR_AMIGO+"/Username"))
			.andExpect(status().isBadRequest());
		
		mockMvc.perform(put(ELIMINAR_AMIGO+"/Username")
				.header("Authorization", getToken()))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		when(usuarioBean.eliminarAmigo(anyString(), anyString()))
			.thenReturn(false);
		mockMvc.perform(put(ELIMINAR_AMIGO+"/Username")
				.header("Authorization", getToken()))
			.andExpect(status().isInternalServerError());
		
		when(usuarioBean.eliminarAmigo(anyString(), anyString()))
			.thenReturn(true);
		mockMvc.perform(put(ELIMINAR_AMIGO+"/Username")
				.header("Authorization", getToken()))
			.andExpect(status().isOk());
	}
}