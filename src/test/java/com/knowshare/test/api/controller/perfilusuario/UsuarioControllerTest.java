/**
 * 
 */
package com.knowshare.test.api.controller.perfilusuario;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import com.knowshare.dto.academia.CarreraDTO;
import com.knowshare.dto.ludificacion.InsigniaDTO;
import com.knowshare.dto.perfilusuario.ImagenDTO;
import com.knowshare.dto.perfilusuario.UsuarioDTO;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;
import com.knowshare.entities.academia.FormacionAcademica;
import com.knowshare.entities.academia.TrabajoGrado;
import com.knowshare.entities.perfilusuario.Personalidad;
import com.knowshare.enums.PreferenciaIdeaEnum;
import com.knowshare.enums.TipoImagenEnum;
import com.knowshare.enums.TipoUsuariosEnum;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author Miguel Montañez
 *
 */
public class UsuarioControllerTest extends AbstractApiTest{
	
	@MockBean
	private UsuarioFacade usuarioBean;
	
	private UsuarioDTO usuario;
	
	private static final String IS_USERNAME_TAKEN = "/api/usuario/isUsernameTaken";
	private static final String CREATE_USER = "/api/usuario/";
	private static final String GET_USER = "/api/usuario/get";
	private static final String SEGUIR = "/api/usuario/seguir";
	private static final String DEJAR_SEGUIR = "/api/usuario/dejarseguir";
	private static final String SOLICITUD = "/api/usuario/solicitud";
	private static final String ADD_TG = "/api/usuario/addTG";
	private static final String ADD_FA = "/api/usuario/addFormacionAcademica";
	private static final String ELIMINAR_AMIGO = "/api/usuario/eliminarAmigo";
	private static final String IS_CORREO_TAKEN = "/api/usuario/isCorreoTaken";
	private static final String UPDATE_INFO_ACADEMICA = "/api/usuario/actualizarInfoAcademica";
	private static final String UPDATE_INFO_BASIS = "/api/usuario/actualizarBasis";
	private static final String UPDATE_HABILIDAD_CUALIDAD = "/api/usuario/actualizarHabilidadCualidad";
	private static final String UPLOAD = "/api/usuario/upload";
	private static final String GET_IMAGE = "/api/usuario/image/";
	private static final String UPDATE_PREFERENCIA = "/api/usuario/preferenciaIdea/";
	private static final String UPDATE_BADGES = "/api/usuario/updateInsignias";
	private static final String PROMOTE = "/api/usuario/promover/";
	private static final String UPDATE_GUSTOS = "/api/usuario/actualizarGustos";
	
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
				.setInsignias(Arrays.asList(new InsigniaDTO(),new InsigniaDTO(),new InsigniaDTO()))
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
		
		mockMvc.perform(get(IS_USERNAME_TAKEN+"?username="))
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
				.contentType(contentType)
				.content(asJsonString(null)))
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
			.andExpect(status().isUnauthorized());
		
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
			.andExpect(jsonPath("$.insignias[0]").isNotEmpty())
			.andExpect(jsonPath("$.insignias[1]").isNotEmpty())
			.andExpect(jsonPath("$.insignias[2]").isNotEmpty())
			.andExpect(jsonPath("$.personalidad.nombre", is("ENJP")))
			.andExpect(jsonPath("$.seguidores", hasSize(0)))
			.andExpect(jsonPath("$.tipoUsuario", is("ESTUDIANTE")))
			.andExpect(jsonPath("$.username", is("username user 1")))
			.andExpect(jsonPath("$.preferenciaIdea", is("POR_RELEVANCIA")));
	}
	
	@Test
	public void seguirTest() throws Exception{
		mockMvc.perform(put(SEGUIR+"/userToFollow"))
			.andExpect(status().isUnauthorized());
		
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
			.andExpect(status().isUnauthorized());
		
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
			.andExpect(status().isUnauthorized());
		
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
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(post(ADD_TG)
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(tg)))
			.andExpect(status().isUnauthorized());
		
		when(usuarioBean.agregarTGDirigido(anyObject(), anyString()))
			.thenReturn(false);
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(post(ADD_TG)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(null)))
			.andExpect(status().isBadRequest());
		
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
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(post(ADD_FA)
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(fa)))
			.andExpect(status().isUnauthorized());
		
		when(usuarioBean.agregarFormacionAcademica(anyObject(), anyString()))
			.thenReturn(false);
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(post(ADD_FA)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(null)))
			.andExpect(status().isBadRequest());
		
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
			.andExpect(status().isUnauthorized());
		
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
	
	@Test
	public void isCorreoTakenTest() throws Exception{
		mockMvc.perform(get(IS_CORREO_TAKEN))
			.andExpect(status().isBadRequest());
		
		mockMvc.perform(get(IS_CORREO_TAKEN+"?correo="))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.isCorreoTaken(anyString()))
			.thenReturn(true);
		mockMvc.perform(get(IS_CORREO_TAKEN+"?correo=AnyUsername"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$").isBoolean());
	}
	
	@Test
	public void actualizarInfoAcademicaTest() throws Exception{
		mockMvc.perform(patch(UPDATE_INFO_ACADEMICA)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(patch(UPDATE_INFO_ACADEMICA)
				.header("Authorization", "bad token")
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(patch(UPDATE_INFO_ACADEMICA)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(null)))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.actualizarInfoAcademica(anyObject()))
			.thenReturn(false);
		mockMvc.perform(patch(UPDATE_INFO_ACADEMICA)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new UsuarioDTO())))
			.andExpect(status().isInternalServerError());
		
		when(usuarioBean.actualizarInfoAcademica(anyObject()))
			.thenReturn(true);
		mockMvc.perform(patch(UPDATE_INFO_ACADEMICA)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new UsuarioDTO())))
			.andExpect(status().isOk());
	}
	
	@Test
	public void actualizarHabilidadCualidadTest() throws Exception{
		mockMvc.perform(patch(UPDATE_HABILIDAD_CUALIDAD)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(patch(UPDATE_HABILIDAD_CUALIDAD)
				.header("Authorization", "bad token")
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(patch(UPDATE_HABILIDAD_CUALIDAD)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(null)))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.actualizarHabilidadCualidad(anyObject()))
			.thenReturn(false);
		mockMvc.perform(patch(UPDATE_HABILIDAD_CUALIDAD)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new UsuarioDTO())))
			.andExpect(status().isInternalServerError());
		
		when(usuarioBean.actualizarHabilidadCualidad(anyObject()))
			.thenReturn(true);
		mockMvc.perform(patch(UPDATE_HABILIDAD_CUALIDAD)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new UsuarioDTO())))
			.andExpect(status().isOk());
	}
	
	@Test
	public void actualizarBasisTest() throws Exception{
		mockMvc.perform(patch(UPDATE_INFO_BASIS)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(patch(UPDATE_INFO_BASIS)
				.header("Authorization", "bad token")
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(patch(UPDATE_INFO_BASIS)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(null)))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.actualizarBasis(anyObject()))
			.thenReturn(false);
		mockMvc.perform(patch(UPDATE_INFO_BASIS)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new UsuarioDTO())))
			.andExpect(status().isInternalServerError());
		
		when(usuarioBean.actualizarBasis(anyObject()))
			.thenReturn(true);
		mockMvc.perform(patch(UPDATE_INFO_BASIS)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new UsuarioDTO())))
			.andExpect(status().isOk());
	}
	
	@Test
	public void uploadImageTest() throws Exception{
		final MockMultipartFile image = new MockMultipartFile("file", "picture.png", "image/png", bytesImage());
		mockMvc.perform(fileUpload(UPLOAD))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(fileUpload(UPLOAD)
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(fileUpload(UPLOAD)
				.header("Authorization", getToken()))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.uploadImage(anyString(), anyObject()))
			.thenReturn(false);
		mockMvc.perform(fileUpload(UPLOAD)
				.file(image)
				.header("Authorization", getToken()))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.uploadImage(anyString(), anyObject()))
			.thenReturn(true);
		mockMvc.perform(fileUpload(UPLOAD)
				.file(image)
				.header("Authorization", getToken()))
			.andExpect(status().isOk());
	}
	
	@Test
	public void getImage() throws Exception{
		final ImagenDTO image = new ImagenDTO()
				.setResult(false)
				.setType(TipoImagenEnum.PNG)
				.setBytes(bytesImage());
		mockMvc.perform(get(GET_IMAGE))
			.andExpect(status().isNotFound());
		
		when(usuarioBean.getImage(anyString()))
			.thenReturn(image);
		mockMvc.perform(get(GET_IMAGE+"username"))
			.andExpect(status().isNotFound());
		
		when(usuarioBean.getImage(anyString()))
			.thenReturn(image.setResult(true));
		mockMvc.perform(get(GET_IMAGE+"username"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("image/png"));
		
		when(usuarioBean.getImage(anyString()))
			.thenReturn(image.setType(TipoImagenEnum.JPEG));
		mockMvc.perform(get(GET_IMAGE+"username"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("image/jpeg"));
	}
	
	@Test
	public void updatePreferenciaTest() throws Exception{
		mockMvc.perform(patch(UPDATE_PREFERENCIA))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(patch(UPDATE_PREFERENCIA)
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(patch(UPDATE_PREFERENCIA)
				.header("Authorization", getToken())
				.contentType(contentType))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.updatePreferenciaIdea(anyString(), anyObject()))
			.thenReturn(false);
		mockMvc.perform(patch(UPDATE_PREFERENCIA)
				.header("Authorization", getToken())
				.contentType(contentType)
				.content(PreferenciaIdeaEnum.ORDEN_CRONOLOGICO.toString().getBytes()))
			.andExpect(status().isNotModified());
		
		when(usuarioBean.updatePreferenciaIdea(anyString(), anyObject()))
			.thenReturn(true);
		mockMvc.perform(patch(UPDATE_PREFERENCIA)
				.header("Authorization", getToken())
				.contentType(contentType)
				.content(PreferenciaIdeaEnum.ORDEN_CRONOLOGICO.toString().getBytes()))
			.andExpect(status().isOk());
	}
	
	@Test
	public void updateInsigniasTest() throws Exception{
		mockMvc.perform(put(UPDATE_BADGES))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(put(UPDATE_BADGES)
				.header("Authorization","bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		when(usuarioBean.updateInsignias(anyString()))
			.thenReturn(false);
		mockMvc.perform(put(UPDATE_BADGES)
				.header("Authorization",getToken()))
			.andExpect(status().isInternalServerError());
		
		when(usuarioBean.updateInsignias(anyString()))
			.thenReturn(true);
		mockMvc.perform(put(UPDATE_BADGES)
				.header("Authorization",getToken()))
			.andExpect(status().isOk());
	}
	
	@Test
	public void promoteTest() throws Exception{
		mockMvc.perform(put(PROMOTE))
			.andExpect(status().isNotFound());
		
		mockMvc.perform(put(PROMOTE+"anyUser"))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(put(PROMOTE+"anyUser")
				.header("Authorization","bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		when(usuarioBean.promoteEstudiante(anyString()))
			.thenReturn(false);
		mockMvc.perform(put(PROMOTE+"anyUser")
				.header("Authorization",getToken()))
			.andExpect(status().isNotModified());
		
		when(usuarioBean.promoteEstudiante(anyString()))
			.thenReturn(true);
		mockMvc.perform(put(PROMOTE+"anyUser")
				.header("Authorization",getToken()))
			.andExpect(status().isOk());
	}
	
	@Test
	public void actualizarGustosTest() throws Exception{
		mockMvc.perform(patch(UPDATE_GUSTOS))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(patch(UPDATE_GUSTOS)
				.header("Authorization","bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(patch(UPDATE_GUSTOS)
				.header("Authorization",getToken())
				.contentType(contentType)
				.content(asJsonString(null)))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.actualizarGustos(anyObject(), anyString()))
			.thenReturn(false);
		mockMvc.perform(patch(UPDATE_GUSTOS)
				.header("Authorization",getToken())
				.contentType(contentType)
				.content(asJsonString(new ArrayList<>())))
			.andExpect(status().isNotModified());
		
		when(usuarioBean.actualizarGustos(anyObject(), anyString()))
			.thenReturn(true);
		mockMvc.perform(patch(UPDATE_GUSTOS)
				.header("Authorization",getToken())
				.contentType(contentType)
				.content(asJsonString(new ArrayList<>())))
			.andExpect(status().isOk());
	}
	
	private byte[] bytesImage(){
		byte[] b = new byte[20];
		new Random().nextBytes(b);
		return b;
	}
}