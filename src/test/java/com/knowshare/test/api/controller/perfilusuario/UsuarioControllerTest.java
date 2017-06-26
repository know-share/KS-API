/**
 * 
 */
package com.knowshare.test.api.controller.perfilusuario;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.knowshare.dto.perfilusuario.UsuarioDTO;
import com.knowshare.enterprise.bean.usuario.UsuarioFacade;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author miguel
 *
 */
public class UsuarioControllerTest extends AbstractApiTest{
	
	@MockBean
	private UsuarioFacade usuarioBean;
	
	private static final String IS_USERNAME_TAKEN = "/usuario/isUsernameTaken";
	private static final String CREATE_USER = "/usuario";
	
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

}
