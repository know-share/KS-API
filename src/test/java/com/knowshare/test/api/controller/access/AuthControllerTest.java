/**
 * 
 */
package com.knowshare.test.api.controller.access;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.knowshare.dto.perfilusuario.AuthDTO;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;
import com.knowshare.entities.perfilusuario.Usuario;
import com.knowshare.enums.TipoUsuariosEnum;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author miguel
 *
 */
public class AuthControllerTest extends AbstractApiTest {
	
	@MockBean
	private UsuarioFacade usuarioBean;
	
	private final static String LOGIN = "/api/auth/login";
	private final static String LOGOUT = "/api/auth/logout";
	
	@Test
	public void loginTest() throws Exception{
		final AuthDTO auth = new AuthDTO()
				.setPassword("PASSWORD")
				.setUsername("USERNAME");
		mockMvc.perform(post(LOGIN)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isBadRequest());
		
		when(usuarioBean.login("USERNAME", "PASSWORD"))
			.thenReturn(null);
		mockMvc.perform(post(LOGIN)
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(auth)))
			.andExpect(status().isUnauthorized());
		
		when(usuarioBean.login("USERNAME", "PASSWORD"))
			.thenReturn(new Usuario().setUsername("USERNAME")
					.setTipo(TipoUsuariosEnum.ESTUDIANTE));
		mockMvc.perform(post(LOGIN)
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(auth)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.token").isNotEmpty())
			.andExpect(jsonPath("$.role").isNotEmpty());
	}
	
	@Test
	public void logoutTest() throws Exception{
		mockMvc.perform(put(LOGOUT)
				.accept(contentType))
			.andExpect(status().isBadRequest());
		
		when(userSessionRepository.removeByToken(anyString()))
			.thenReturn(1L);
		mockMvc.perform(put(LOGOUT)
				.accept(contentType)
				.header("Authorization", "This token doesnt matter"))
			.andExpect(status().isOk());
		
		when(userSessionRepository.removeByToken(anyString()))
			.thenReturn(0L);
		mockMvc.perform(put(LOGOUT)
				.accept(contentType)
				.header("Authorization", "This token doesnt matter"))
			.andExpect(status().isNotModified());
	}

}
