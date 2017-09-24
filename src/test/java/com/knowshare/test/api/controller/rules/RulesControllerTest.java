/**
 * 
 */
package com.knowshare.test.api.controller.rules;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyShort;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;

import com.knowshare.dto.idea.IdeaDTO;
import com.knowshare.dto.perfilusuario.UsuarioDTO;
import com.knowshare.dto.rules.RecomendacionDTO;
import com.knowshare.enterprise.bean.rules.busqueda.BusquedaIdeaFacade;
import com.knowshare.enterprise.bean.rules.busqueda.BusquedaUsuarioFacade;
import com.knowshare.enterprise.bean.rules.config.RulesAdminFacade;
import com.knowshare.enterprise.bean.rules.usuarios.RecomendacionConexionFacade;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;
import com.knowshare.enums.TipoIdeaEnum;
import com.knowshare.enums.TipoUsuariosEnum;
import com.knowshare.fact.rules.TipoConexionEnum;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author Miguel Montañez
 *
 */
public class RulesControllerTest extends AbstractApiTest{

	@MockBean
	private UsuarioFacade usuarioBean;
	
	@MockBean
	private RecomendacionConexionFacade recomendacionesBean;
	
	@MockBean
	private BusquedaUsuarioFacade busquedaBean;
	
	@MockBean
	private RulesAdminFacade rulesAdminBean;
	
	@MockBean
	private BusquedaIdeaFacade busquedaIdea;
	
	private static final String RECOMENDACION_CONEXIONES = "/api/rules/recomendacionConexiones";
	private static final String BUSCAR_USUARIO = "/api/rules/buscarUsuario";
	private static final String ACTUALIZAR = "/api/rules/update";
	private static final String PREFERENCIAS = "/api/rules/rulesPreferences";
	private static final String FIND_RED = "/api/rules/findIdeasRed";
	
	private List<?> recomendaciones;
	private IdeaDTO idea;
	
	@Before
	public void setup(){
		final List<RecomendacionDTO> recomendacionesCopy = new ArrayList<>();
		recomendacionesCopy.add(new RecomendacionDTO()
				.setCarrera("Carrera mock")
				.setConexion(TipoConexionEnum.CONFIANZA)
				.setPorcentaje(23d)
				.setTipoUsuario(TipoUsuariosEnum.PROFESOR)
				.setUsername("username mock 1"));
		recomendacionesCopy.add(new RecomendacionDTO()
				.setCarrera("Carrera mock")
				.setConexion(TipoConexionEnum.RELEVANTE)
				.setPorcentaje(2d)
				.setTipoUsuario(TipoUsuariosEnum.EGRESADO)
				.setUsername("username mock 2"));
		recomendaciones = recomendacionesCopy;
		
		idea = new IdeaDTO()
				.setId(new String("id"))
				.setAlcance("Alcance")
				.setContenido("Idea nueva")
				.setNumeroEstudiantes(3)
				.setUsuario("username user 1")
				.setTipo(TipoIdeaEnum.NU);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void getRecomendacionesTest() throws Exception{
		mockMvc.perform(get(RECOMENDACION_CONEXIONES))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(get(RECOMENDACION_CONEXIONES)
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString())).thenReturn(userSession);
		when(rulesAdminBean.isRulesOn()).thenReturn(false);
		mockMvc.perform(get(RECOMENDACION_CONEXIONES)
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(rulesAdminBean.isRulesOn()).thenReturn(true);
		when(recomendacionesBean.setDeRecomendaciones(anyObject()))
			.thenReturn(null);
		when(usuarioBean.getUsuario(anyString()))
			.thenReturn(new UsuarioDTO());
		mockMvc.perform(get(RECOMENDACION_CONEXIONES)
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(recomendacionesBean.setDeRecomendaciones(anyObject()))
			.thenReturn(new ArrayList<>());
		mockMvc.perform(get(RECOMENDACION_CONEXIONES)
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(recomendacionesBean.setDeRecomendaciones(anyObject()))
			.thenReturn((List) recomendaciones);
		mockMvc.perform(get(RECOMENDACION_CONEXIONES)
				.header("Authorization", getToken()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].carrera", is("Carrera mock")))
			.andExpect(jsonPath("$[0].conexion", is("CONFIANZA")))
			.andExpect(jsonPath("$[0].porcentaje", is(23d)))
			.andExpect(jsonPath("$[0].tipoUsuario", is("PROFESOR")))
			.andExpect(jsonPath("$[0].username", is("username mock 1")))
			.andExpect(jsonPath("$[1].carrera", is("Carrera mock")))
			.andExpect(jsonPath("$[1].conexion", is("RELEVANTE")))
			.andExpect(jsonPath("$[1].porcentaje", is(2d)))
			.andExpect(jsonPath("$[1].tipoUsuario", is("EGRESADO")))
			.andExpect(jsonPath("$[1].username", is("username mock 2")));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void buscarUsuarioTest() throws Exception{
		mockMvc.perform(get(BUSCAR_USUARIO))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(get(BUSCAR_USUARIO)
				.header("Authorization","bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString())).thenReturn(userSession);
		mockMvc.perform(get(BUSCAR_USUARIO)
				.header("Authorization",getToken()))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.getUsuario(anyString()))
			.thenReturn(new UsuarioDTO());
		when(busquedaBean.buscarUsuario(anyObject(), anyString(), anyString()))
			.thenReturn(null);
		mockMvc.perform(get(BUSCAR_USUARIO+"?param=")
				.header("Authorization",getToken()))
			.andExpect(status().isBadRequest());
		
		mockMvc.perform(get(BUSCAR_USUARIO+"?param=something")
				.header("Authorization",getToken()))
			.andExpect(status().isNoContent());
		
		when(busquedaBean.buscarUsuario(anyObject(), anyString(), anyString()))
			.thenReturn(new ArrayList<>());
		mockMvc.perform(get(BUSCAR_USUARIO+"?param=something")
				.header("Authorization",getToken()))
			.andExpect(status().isNoContent());
		
		when(busquedaBean.buscarUsuario(anyObject(), anyString(), anyString()))
			.thenReturn((List)recomendaciones);
		mockMvc.perform(get(BUSCAR_USUARIO+"?param=something")
				.header("Authorization",getToken()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].carrera", is("Carrera mock")))
			.andExpect(jsonPath("$[0].conexion", is("CONFIANZA")))
			.andExpect(jsonPath("$[0].porcentaje", is(23d)))
			.andExpect(jsonPath("$[0].tipoUsuario", is("PROFESOR")))
			.andExpect(jsonPath("$[0].username", is("username mock 1")))
			.andExpect(jsonPath("$[1].carrera", is("Carrera mock")))
			.andExpect(jsonPath("$[1].conexion", is("RELEVANTE")))
			.andExpect(jsonPath("$[1].porcentaje", is(2d)))
			.andExpect(jsonPath("$[1].tipoUsuario", is("EGRESADO")))
			.andExpect(jsonPath("$[1].username", is("username mock 2")));
	}
	
	@Test
	public void updateRulesTest() throws Exception{
		mockMvc.perform(put(ACTUALIZAR))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(put(ACTUALIZAR)
			.header("Authorization","bad token"))
		.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString())).thenReturn(userSession);
		when(rulesAdminBean.updateRules()).thenReturn(false);
		mockMvc.perform(put(ACTUALIZAR)
				.header("Authorization",getToken()))
			.andExpect(status().isInternalServerError());
		
		when(rulesAdminBean.updateRules()).thenReturn(true);
		mockMvc.perform(put(ACTUALIZAR)
				.header("Authorization",getToken()))
			.andExpect(status().isOk());
	}
	
	@Test
	public void updateRulesPreferencesTest() throws Exception{
		mockMvc.perform(patch(PREFERENCIAS))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(patch(PREFERENCIAS)
			.header("Authorization","bad token"))
		.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString())).thenReturn(userSession);
		when(rulesAdminBean.updateRulesSystem(anyShort())).thenReturn(false);
		mockMvc.perform(patch(PREFERENCIAS)
				.header("Authorization",getToken()))
			.andExpect(status().isBadRequest());
		
		mockMvc.perform(patch(PREFERENCIAS+"?state=1")
				.header("Authorization",getToken()))
			.andExpect(status().isNotModified());
		
		when(rulesAdminBean.updateRulesSystem(anyShort())).thenReturn(true);
		mockMvc.perform(patch(PREFERENCIAS+"?state=1")
				.header("Authorization",getToken()))
			.andExpect(status().isOk());
	}
	
	@Test
	public void getRulesPreferencesTest() throws Exception{
		mockMvc.perform(get(PREFERENCIAS))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(get(PREFERENCIAS)
			.header("Authorization","bad token"))
		.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString())).thenReturn(userSession);
		when(rulesAdminBean.isRulesOn()).thenReturn(false);
		mockMvc.perform(get(PREFERENCIAS)
				.header("Authorization",getToken()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$",is(false)));
		
		when(rulesAdminBean.isRulesOn()).thenReturn(true);
		mockMvc.perform(get(PREFERENCIAS)
				.header("Authorization",getToken()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$",is(true)));
	}
	
	@Test
	public void findRedTest() throws Exception {
		mockMvc.perform(get(FIND_RED))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(get(FIND_RED)
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		when(busquedaIdea.findRed("username user 1",0))
			.thenReturn(null);
		mockMvc.perform(get(FIND_RED)
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(busquedaIdea.findRed("username user 1",0))
			.thenReturn(new PageImpl<>(new ArrayList<>()));
		mockMvc.perform(get(FIND_RED)
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(busquedaIdea.findRed("username user 1",0))
			.thenReturn(new PageImpl<>(Arrays.asList(idea)));
		mockMvc.perform(get(FIND_RED)
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
}
