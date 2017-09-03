/**
 * 
 */
package com.knowshare.test.api.controller.ludificacion;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.Arrays;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.knowshare.dto.ludificacion.LeaderDTO;
import com.knowshare.enterprise.bean.avales.AvalFacade;
import com.knowshare.enterprise.bean.leaderboard.LeaderFacade;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author Miguel Montañez
 *
 */
public class LudificacionControllerTest extends AbstractApiTest{
	
	@MockBean
	private AvalFacade avalBean;
	
	@MockBean
	private LeaderFacade leaderBean;
	
	private static final String AVALAR = "/api/ludificacion/avalar";
	private static final String LEADER_CARRERA = "/api/ludificacion/leaderCarreras";
	private static final String LEADER_USUARIOS = "/api/ludificacion/leaderUsuarios";
	
	@Test
	public void avalarUsuarioTest() throws Exception{
		mockMvc.perform(put(AVALAR))
			.andExpect(status().isNotFound());
		
		mockMvc.perform(put(AVALAR+"/userTarget"))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(put(AVALAR+"/userTarget")
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString())).thenReturn(userSession);
		mockMvc.perform(put(AVALAR+"/userTarget")
				.header("Authorization", getToken()))
			.andExpect(status().isBadRequest());
		
		mockMvc.perform(put(AVALAR+"/userTarget?tipo=")
				.header("Authorization", getToken()))
			.andExpect(status().isBadRequest());
		
		mockMvc.perform(put(AVALAR+"/userTarget?tipo=HABILIDAD")
				.header("Authorization", getToken()))
			.andExpect(status().isBadRequest());
		
		when(avalBean.avalarUsuario(anyString(), anyString(), anyObject(), any()))
			.thenReturn(false);
		mockMvc.perform(put(AVALAR+"/userTarget?tipo=HABILIDAD")
				.header("Authorization", getToken())
				.contentType(contentType)
				.content(asJsonString(new ObjectId())))
			.andExpect(status().isNotModified());
		
		when(avalBean.avalarUsuario(anyString(), anyString(), anyObject(), any()))
			.thenReturn(true);
		mockMvc.perform(put(AVALAR+"/userTarget?tipo=HABILIDAD")
				.header("Authorization", getToken())
				.contentType(contentType)
				.content(asJsonString(new ObjectId())))
			.andExpect(status().isOk());
	}
	
	@Test
	public void getAllCarrerasTest() throws Exception{
		mockMvc.perform(get(LEADER_CARRERA))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(get(LEADER_CARRERA)
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString())).thenReturn(userSession);
		when(leaderBean.carrerasLeader()).thenReturn(null);
		mockMvc.perform(get(LEADER_CARRERA)
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(leaderBean.carrerasLeader()).thenReturn(new ArrayList<>());
		mockMvc.perform(get(LEADER_CARRERA)
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(leaderBean.carrerasLeader()).thenReturn(Arrays.asList(new LeaderDTO()));
		mockMvc.perform(get(LEADER_CARRERA)
				.header("Authorization", getToken()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasSize(1)));
	}
	
	@Test
	public void getEstudiantesTest() throws Exception{
		mockMvc.perform(get(LEADER_USUARIOS))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(get(LEADER_USUARIOS)
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString())).thenReturn(userSession);
		mockMvc.perform(get(LEADER_USUARIOS)
				.header("Authorization", getToken()))
			.andExpect(status().isBadRequest());
		
		mockMvc.perform(get(LEADER_USUARIOS+"?carrera=carrera")
				.header("Authorization", getToken()))
			.andExpect(status().isBadRequest());
		
		when(leaderBean.usuariosLeader(anyString(), anyString(), anyObject()))
			.thenReturn(null);
		mockMvc.perform(get(LEADER_USUARIOS+"?carrera=carrera&tipo=EGRESADO")
				.header("Authorization", getToken()))
			.andExpect(status().isBadRequest());
		
		mockMvc.perform(get(LEADER_USUARIOS+"?carrera=carrera&tipo=PROFESOR")
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(leaderBean.usuariosLeader(anyString(), anyString(), anyObject()))
			.thenReturn(new ArrayList<>());
		mockMvc.perform(get(LEADER_USUARIOS+"?carrera=carrera&tipo=PROFESOR")
				.header("Authorization", getToken()))
			.andExpect(status().isOk());
	}

}
