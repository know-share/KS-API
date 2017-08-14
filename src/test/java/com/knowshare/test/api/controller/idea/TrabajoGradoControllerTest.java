/**
 * 
 */
package com.knowshare.test.api.controller.idea;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.knowshare.enterprise.bean.trabajogrado.TrabajoGradoFacade;
import com.knowshare.entities.academia.TrabajoGrado;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author Miguel Montañez
 *
 */
public class TrabajoGradoControllerTest extends AbstractApiTest{
	
	@MockBean
	private TrabajoGradoFacade tgBean;
	
	private List<TrabajoGrado> tgs;
	
	private static final String FIND_ALL = "/api/tg/findAll";
	
	@Before
	public void setup(){
		tgs = new ArrayList<>();
		tgs.add(new TrabajoGrado()
				.setId(new ObjectId())
				.setNombre("tg 1")
				.setNumEstudiantes(2)
				.setPeriodoFin("fin 1")
				.setResumen("resumen 1"));
		tgs.add(new TrabajoGrado()
				.setId(new ObjectId())
				.setNombre("tg 2")
				.setNumEstudiantes(3)
				.setPeriodoFin("fin 2")
				.setResumen("resumen 2"));
	}
	
	@Test
	public void findAllTgTest() throws Exception{
		when(tgBean.findAll())
			.thenReturn(null);
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isNoContent());
		
		when(tgBean.findAll())
			.thenReturn(new ArrayList<>());
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isNoContent());
		
		when(tgBean.findAll())
			.thenReturn(tgs);
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id").isNotEmpty())
			.andExpect(jsonPath("$[0].nombre",is("tg 1")))
			.andExpect(jsonPath("$[0].numEstudiantes",is(2)))
			.andExpect(jsonPath("$[0].periodoFin",is("fin 1")))
			.andExpect(jsonPath("$[0].resumen",is("resumen 1")))
			.andExpect(jsonPath("$[1].id").isNotEmpty())
			.andExpect(jsonPath("$[1].nombre",is("tg 2")))
			.andExpect(jsonPath("$[1].numEstudiantes",is(3)))
			.andExpect(jsonPath("$[1].periodoFin",is("fin 2")))
			.andExpect(jsonPath("$[1].resumen",is("resumen 2")));
	}

}
