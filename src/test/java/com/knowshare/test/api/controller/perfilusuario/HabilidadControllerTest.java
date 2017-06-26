/**
 * 
 */
package com.knowshare.test.api.controller.perfilusuario;

import static org.mockito.Matchers.anyString;
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

import com.knowshare.dto.perfilusuario.HabilidadDTO;
import com.knowshare.enterprise.bean.habilidad.HabilidadFacade;
import com.knowshare.enums.TipoHabilidadEnum;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author miguel
 *
 */
public class HabilidadControllerTest extends AbstractApiTest{
	
	@MockBean
	private HabilidadFacade habilidadBean;
	
	private List<HabilidadDTO> habilidades;
	
	private static final String FIND_HABILIDADES = "/habilidad/getHabilidades";
	private static final String FIND_HAB_PROFESIONALES = "/habilidad/getHabilidadesProfesionales";
	
	@Before
	public void setup(){
		habilidades = Arrays.asList(
					new HabilidadDTO()
						.setId(new ObjectId(new Date()))
						.setNombre("habilidad 1")
						.setTipo(TipoHabilidadEnum.PERSONALES),
					new HabilidadDTO()
						.setId(new ObjectId(new Date()))
						.setCarrera("carrera 2")
						.setNombre("habilidad 2")
						.setTipo(TipoHabilidadEnum.PROFESIONALES)
				);
	}
	
	@Test
	public void getHabilidadesTest() throws Exception{
		mockMvc.perform(get(FIND_HABILIDADES))
			.andExpect(status().isBadRequest());
		
		when(habilidadBean.getHabilidades(anyString()))
			.thenReturn(null);
		mockMvc.perform(get(FIND_HABILIDADES + "?carrera=AnyCarrera"))
			.andExpect(status().isNoContent());
		
		when(habilidadBean.getHabilidades(anyString()))
			.thenReturn(new ArrayList<>());
		mockMvc.perform(get(FIND_HABILIDADES + "?carrera=AnyCarrera"))
			.andExpect(status().isNoContent());
		
		when(habilidadBean.getHabilidades(anyString()))
			.thenReturn(habilidades);
		mockMvc.perform(get(FIND_HABILIDADES + "?carrera=AnyCarrera"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id").isNotEmpty())
			.andExpect(jsonPath("$[0].nombre", is("habilidad 1")))
			.andExpect(jsonPath("$[0].tipo", is(TipoHabilidadEnum.PERSONALES.toString())))
			.andExpect(jsonPath("$[0].carrera").isEmpty())
			.andExpect(jsonPath("$[1].id").isNotEmpty())
			.andExpect(jsonPath("$[1].nombre", is("habilidad 2")))
			.andExpect(jsonPath("$[1].tipo", is(TipoHabilidadEnum.PROFESIONALES.toString())))
			.andExpect(jsonPath("$[1].carrera",is("carrera 2")));
	}
	
	@Test
	public void getHabilidadesProfesionalesTest() throws Exception{
		mockMvc.perform(get(FIND_HAB_PROFESIONALES))
			.andExpect(status().isBadRequest());
		
		when(habilidadBean.getHabilidadesProfesionales(anyString()))
			.thenReturn(null);
		mockMvc.perform(get(FIND_HAB_PROFESIONALES + "?carrera=AnyCarrera"))
			.andExpect(status().isNoContent());
		
		when(habilidadBean.getHabilidadesProfesionales(anyString()))
			.thenReturn(new ArrayList<>());
		mockMvc.perform(get(FIND_HAB_PROFESIONALES + "?carrera=AnyCarrera"))
			.andExpect(status().isNoContent());
		
		when(habilidadBean.getHabilidadesProfesionales(anyString()))
			.thenReturn(habilidades);
		mockMvc.perform(get(FIND_HAB_PROFESIONALES + "?carrera=AnyCarrera"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id").isNotEmpty())
			.andExpect(jsonPath("$[0].nombre", is("habilidad 1")))
			.andExpect(jsonPath("$[0].tipo", is(TipoHabilidadEnum.PERSONALES.toString())))
			.andExpect(jsonPath("$[0].carrera").isEmpty())
			.andExpect(jsonPath("$[1].id").isNotEmpty())
			.andExpect(jsonPath("$[1].nombre", is("habilidad 2")))
			.andExpect(jsonPath("$[1].tipo", is(TipoHabilidadEnum.PROFESIONALES.toString())))
			.andExpect(jsonPath("$[1].carrera",is("carrera 2")));
	}

}
