/**
 * 
 */
package com.knowshare.test.api.general;

import java.nio.charset.Charset;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowshare.api.controller.academia.CarreraController;
import com.knowshare.api.controller.perfilusuario.CualidadController;
import com.knowshare.api.controller.perfilusuario.GustoController;
import com.knowshare.api.controller.perfilusuario.HabilidadController;
import com.knowshare.api.controller.perfilusuario.PersonalidadController;
import com.knowshare.api.controller.perfilusuario.UsuarioController;

/**
 * @author miguel
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers={CarreraController.class, CualidadController.class, GustoController.class,
		HabilidadController.class, PersonalidadController.class, UsuarioController.class})
@ContextConfiguration(classes={ConfigApiContext.class})
public abstract class AbstractApiTest {

	@Autowired
	protected MockMvc mockMvc;
	
	protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
	
	/*
     * converts a Java object into JSON representation
     */
    protected static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
