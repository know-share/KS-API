/**
 * 
 */
package com.knowshare.test.api.controller.perfilusuario;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.knowshare.enterprise.bean.gusto.GustoFacade;
import com.knowshare.entities.perfilusuario.Gusto;
import com.knowshare.enums.TipoGustoEnum;
import com.knowshare.test.api.general.AbstractApiTest;

/**
 * @author miguel
 *
 */
public class GustoControllerTest extends AbstractApiTest{
	
	@MockBean
	private GustoFacade gustoBean;
	
	private List<Gusto> gustos;
	
	private static final String FIND_ALL = "/gusto/findAll";
	
	@Before
	public void setup(){
		gustos = Arrays.asList(
					new Gusto()
						.setId(new ObjectId(new Date()))
						.setImagePath("path/imageArtes")
						.setTipo(TipoGustoEnum.ARTES),
					new Gusto()
						.setId(new ObjectId(new Date()))
						.setImagePath("path/imageDeportes")
						.setTipo(TipoGustoEnum.DEPORTES),
					new Gusto()
						.setId(new ObjectId(new Date()))
						.setImagePath("path/imageGenerales")
						.setTipo(TipoGustoEnum.GENERALES)
				);
	}
	
	@Test
	public void findAllTest() throws Exception{
		when(gustoBean.getAllGustos()).thenReturn(null);
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isNoContent());
		
		when(gustoBean.getAllGustos()).thenReturn(new ArrayList<>());
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isNoContent());
		
		when(gustoBean.getAllGustos()).thenReturn(gustos);
		mockMvc.perform(get(FIND_ALL))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[0].id").isNotEmpty())
			.andExpect(jsonPath("$[0].imagePath", is("path/imageArtes")))
			.andExpect(jsonPath("$[0].tipo", is(TipoGustoEnum.ARTES.toString())))
			.andExpect(jsonPath("$[1].id").isNotEmpty())
			.andExpect(jsonPath("$[1].imagePath", is("path/imageDeportes")))
			.andExpect(jsonPath("$[1].tipo", is(TipoGustoEnum.DEPORTES.toString())))
			.andExpect(jsonPath("$[2].id").isNotEmpty())
			.andExpect(jsonPath("$[2].imagePath", is("path/imageGenerales")))
			.andExpect(jsonPath("$[2].tipo", is(TipoGustoEnum.GENERALES.toString())));
	}

}
