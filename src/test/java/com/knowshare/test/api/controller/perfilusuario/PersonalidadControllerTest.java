/**
 * 
 */
package com.knowshare.test.api.controller.perfilusuario;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.knowshare.enterprise.bean.personalidad.PersonalidadFacade;
import com.knowshare.entities.perfilusuario.Personalidad;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author miguel
 *
 */
public class PersonalidadControllerTest extends AbstractApiTest{

	@MockBean
	private PersonalidadFacade personalidadBean;
	
	private List<Personalidad> personalidades;
	
	private static final String FIND_ALL = "/api/personalidad/findAll";
	
	@Before
	public void setup(){
		personalidades = Arrays.asList(
					new Personalidad()
						.setNombre("personalidad 1"),
					new Personalidad()
						.setNombre("personalidad 2"),
					new Personalidad()
						.setNombre("personalidad 3")
				);
	}
	
	@Test
	public void findAllTest() throws Exception{
		when(personalidadBean.getAllPersonalidades())
			.thenReturn(null);
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isNoContent());
		
		when(personalidadBean.getAllPersonalidades())
			.thenReturn(new ArrayList<>());
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isNoContent());
		
		when(personalidadBean.getAllPersonalidades())
			.thenReturn(personalidades);
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[0].nombre", is("personalidad 1")))
			.andExpect(jsonPath("$[1].nombre", is("personalidad 2")))
			.andExpect(jsonPath("$[2].nombre", is("personalidad 3")));
	}
}
