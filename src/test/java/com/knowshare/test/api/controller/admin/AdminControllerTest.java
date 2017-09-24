/**
 * 
 */
package com.knowshare.test.api.controller.admin;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.knowshare.enterprise.bean.dashboards.DashboardsFacade;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author Miguel Montañez
 *
 */
public class AdminControllerTest extends AbstractApiTest {

	@MockBean
	private DashboardsFacade dashboardBean;
	
	private static final String GET_USUARIOS = "/api/admin/getUsuarios";
	private static final String GET_TAGS = "/api/admin/getTags";
	
	@Test
	public void getEstudiantesTest() throws Exception{
		mockMvc.perform(get(GET_USUARIOS))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(get(GET_USUARIOS)
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(get(GET_USUARIOS)
				.header("Authorization", getToken()))
			.andExpect(status().isBadRequest());
		
		when(dashboardBean.generoCarrera(anyString()))
			.thenReturn(null);
		mockMvc.perform(get(GET_USUARIOS+"?carrera=any")
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(dashboardBean.generoCarrera(anyString()))
			.thenReturn(new ArrayList<>());
		mockMvc.perform(get(GET_USUARIOS+"?carrera=any")
				.header("Authorization", getToken()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType));
	}
	
	@Test
	public void getTagsTest() throws Exception{
		mockMvc.perform(get(GET_TAGS))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(get(GET_TAGS)
				.header("Authorization", "bad token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		when(dashboardBean.usoTags())
			.thenReturn(null);
		mockMvc.perform(get(GET_TAGS)
				.header("Authorization", getToken()))
			.andExpect(status().isNoContent());
		
		when(dashboardBean.usoTags())
			.thenReturn(new HashMap<>());
		mockMvc.perform(get(GET_TAGS)
				.header("Authorization", getToken()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType));
	}
}
