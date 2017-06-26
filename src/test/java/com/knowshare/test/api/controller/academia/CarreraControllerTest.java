/**
 * 
 */
package com.knowshare.test.api.controller.academia;

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
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.knowshare.dto.academia.CarreraDTO;
import com.knowshare.dto.academia.EnfasisAreaConocimientoDTO;
import com.knowshare.enterprise.bean.carrera.CarreraFacade;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author miguel
 *
 */
public class CarreraControllerTest extends AbstractApiTest{
	
	@MockBean
	private CarreraFacade carreraBean;
	
	private List<CarreraDTO> carreras;
	
	private EnfasisAreaConocimientoDTO enfasisAreaConocimiento;
	
	private final static String FIND_ALL = "/carrera/findAll";
	private final static String GET_ENFASIS_AC = "/carrera/getEnfasisAreaConocimiento";
	
	@Before
	public void setup(){
		carreras = Arrays.asList(new CarreraDTO()
						.setNombre("Ingeniería de sistemas")
						.setFacultad("Ingeniería")
						, new CarreraDTO()
						.setNombre("Estudios musicales")
						.setFacultad("Artes")
						, new CarreraDTO()
						.setNombre("Ingeniería industrial")
						.setFacultad("Ingeniería")
					);
		enfasisAreaConocimiento = new EnfasisAreaConocimientoDTO()
				.setAreaConocimiento(Arrays.asList(
						"AreaConocimiento 1",
						"AreaConocimiento 2",
						"AreaConocimiento 3"
					))
				.setEnfasis(Arrays.asList(
						"Enfasis 1",
						"Enfasis 2",
						"Enfasis 3",
						"Enfasis 4"
					));
	}
	
	@Test
	public void findAllTest() throws Exception{
		when(carreraBean.getAllCarreras()).thenReturn(null);
		
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isNoContent());
		
		when(carreraBean.getAllCarreras()).thenReturn(new ArrayList<>());
		
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isNoContent());
		
		when(carreraBean.getAllCarreras()).thenReturn(carreras);
		
		mockMvc.perform(get(FIND_ALL))
			.andExpect(content().contentType(contentType))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[0].nombre", is("Ingeniería de sistemas")))
			.andExpect(jsonPath("$[0].facultad", is("Ingeniería")))
			.andExpect(jsonPath("$[1].nombre", is("Estudios musicales")))
			.andExpect(jsonPath("$[1].facultad", is("Artes")))
			.andExpect(jsonPath("$[2].nombre", is("Ingeniería industrial")))
			.andExpect(jsonPath("$[2].facultad", is("Ingeniería")));
	}
	
	@Test
	public void getEnfasisAreaConocimientoTest() throws Exception{
		mockMvc.perform(get(GET_ENFASIS_AC))
			.andExpect(status().isBadRequest());
		
		when(carreraBean.getEnfasisAreaConocimiento(anyString()))
			.thenReturn(null);
		
		mockMvc.perform(get(GET_ENFASIS_AC+"?carrera=noexist"))
			.andExpect(status().isNoContent());
		
		when(carreraBean.getEnfasisAreaConocimiento(anyString()))
			.thenReturn(enfasisAreaConocimiento);
	
		mockMvc.perform(get(GET_ENFASIS_AC+"?carrera=CarreraExist"))
			.andExpect(content().contentType(contentType))
			.andExpect(status().isOk())
			.andExpect(jsonPath("enfasis", hasSize(4)))
			.andExpect(jsonPath("areaConocimiento", hasSize(3)));
	}
}
