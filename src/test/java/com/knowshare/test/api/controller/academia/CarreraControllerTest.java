/**
 * 
 */
package com.knowshare.test.api.controller.academia;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
 * @author Miguel Montañez
 *
 */
public class CarreraControllerTest extends AbstractApiTest{
	
	@MockBean
	private CarreraFacade carreraBean;
	
	private List<CarreraDTO> carreras;
	
	private EnfasisAreaConocimientoDTO enfasisAreaConocimiento;
	
	private final static String FIND_ALL = "/api/carrera/findAll";
	private final static String GET_ENFASIS_AC = "/api/carrera/getEnfasisAreaConocimiento";
	private final static String UPDATE = "/api/carrera";
	private final static String DELETE = "/api/carrera/delete/";
	private final static String CREATE = "/api/carrera/create";
	
	
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
	
	@Test
	public void createTest() throws Exception{
		mockMvc.perform(post(CREATE)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(post(CREATE)
				.header("Authorization", "bad token")
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString())).thenReturn(userSession);
		mockMvc.perform(post(CREATE)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isBadRequest());
		
		when(carreraBean.create(anyObject()))
			.thenReturn(false);
		mockMvc.perform(post(CREATE)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new CarreraDTO())))
			.andExpect(status().isNoContent());
		
		when(carreraBean.create(anyObject()))
			.thenReturn(true);
		mockMvc.perform(post(CREATE)
				.header("Authorization", getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new CarreraDTO())))
			.andExpect(status().isOk());
	}
	
	@Test
	public void deleteTest() throws Exception{
		mockMvc.perform(delete(DELETE))
			.andExpect(status().isNotFound());
		
		mockMvc.perform(delete(DELETE+"anyId")
				.header("Authorization", "any token"))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString())).thenReturn(userSession);
		when(carreraBean.delete(anyString()))
			.thenReturn(false);
		mockMvc.perform(delete(DELETE+"anyId")
				.header("Authorization", getToken()))
			.andExpect(status().isNotModified());
		
		when(carreraBean.delete(anyString()))
			.thenReturn(true);
		mockMvc.perform(delete(DELETE+"anyId")
				.header("Authorization", getToken()))
			.andExpect(status().isOk());
	}
	
	@Test
	public void updateTest() throws Exception{
		mockMvc.perform(patch(UPDATE)
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		mockMvc.perform(patch(UPDATE)
				.header("Authorization","bad token")
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isUnauthorized());
		
		when(userSessionRepository.findByToken(anyString()))
			.thenReturn(userSession);
		mockMvc.perform(patch(UPDATE)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType))
			.andExpect(status().isBadRequest());
		
		when(carreraBean.update(anyObject()))
			.thenReturn(false);
		mockMvc.perform(patch(UPDATE)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new CarreraDTO())))
			.andExpect(status().isNoContent());
		
		when(carreraBean.update(anyObject()))
			.thenReturn(true);
		mockMvc.perform(patch(UPDATE)
				.header("Authorization",getToken())
				.accept(contentType)
				.contentType(contentType)
				.content(asJsonString(new CarreraDTO())))
			.andExpect(status().isOk());
	}
}
