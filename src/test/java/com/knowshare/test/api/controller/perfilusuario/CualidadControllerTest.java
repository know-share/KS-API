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
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.knowshare.dto.perfilusuario.CualidadDTO;
import com.knowshare.enterprise.bean.cualidad.CualidadFacade;
import com.knowshare.enums.TipoCualidadEnum;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author miguel
 *
 */
public class CualidadControllerTest extends AbstractApiTest{
	
	@MockBean
	private CualidadFacade cualidadBean;
	
	private List<CualidadDTO> cualidades;
	
	private static final String FIND_ALL = "/api/cualidad/findAll";
	
	@Before
	public void setup(){
		cualidades = Arrays.asList(
					new CualidadDTO()
						.setId(new ObjectId(new Date()))
						.setNombre("Cualidad 1")
						.setTipo(TipoCualidadEnum.PROFESIONAL),
					new CualidadDTO()
						.setId(new ObjectId(new Date()))
						.setNombre("Cualidad 2")
						.setTipo(TipoCualidadEnum.PROFESOR),
					new CualidadDTO()
						.setId(new ObjectId(new Date()))
						.setNombre("Cualidad 3")
						.setTipo(TipoCualidadEnum.DIRECTOR)
				);
	}
	
	@Test
	public void findAllTest() throws Exception{
		when(cualidadBean.getAll()).thenReturn(null);
		
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isNoContent());
		
		when(cualidadBean.getAll()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isNoContent());
		
		when(cualidadBean.getAll()).thenReturn(cualidades);
		
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$[0].id").isNotEmpty())
			.andExpect(jsonPath("$[0].nombre", is("Cualidad 1")))
			.andExpect(jsonPath("$[0].tipo", is(TipoCualidadEnum.PROFESIONAL.toString())))
			.andExpect(jsonPath("$[1].id").isNotEmpty())
			.andExpect(jsonPath("$[1].nombre", is("Cualidad 2")))
			.andExpect(jsonPath("$[1].tipo", is(TipoCualidadEnum.PROFESOR.toString())))
			.andExpect(jsonPath("$[2].id").isNotEmpty())
			.andExpect(jsonPath("$[2].nombre", is("Cualidad 3")))
			.andExpect(jsonPath("$[2].tipo", is(TipoCualidadEnum.DIRECTOR.toString())));
	}

}
