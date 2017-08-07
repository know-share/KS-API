/**
 * 
 */
package com.knowshare.test.api.general;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowshare.api.controller.academia.CarreraController;
import com.knowshare.api.controller.access.AuthController;
import com.knowshare.api.controller.idea.IdeaController;
import com.knowshare.api.controller.perfilusuario.CualidadController;
import com.knowshare.api.controller.perfilusuario.GustoController;
import com.knowshare.api.controller.perfilusuario.HabilidadController;
import com.knowshare.api.controller.perfilusuario.PersonalidadController;
import com.knowshare.api.controller.perfilusuario.UsuarioController;
import com.knowshare.api.security.JWTFilter;
import com.knowshare.dto.perfilusuario.AuthDTO;
import com.knowshare.enterprise.repository.app.UserSessionRepository;
import com.knowshare.entities.app.UserSession;

import static org.mockito.Mockito.when;

/**
 * @author Miguel Montañez
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers={CarreraController.class, CualidadController.class, GustoController.class,
		HabilidadController.class, PersonalidadController.class, UsuarioController.class, AuthController.class,
		IdeaController.class})
@ContextConfiguration(classes={ConfigApiContext.class})
public abstract class AbstractApiTest {

	@Autowired
	protected MockMvc mockMvc;
	
	protected UserSession userSession;
	
	@MockBean
	protected UserSessionRepository userSessionRepository;
	
	protected HttpServletRequest request;
	
	protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
	
	@PostConstruct
	private void init(){
		request = Mockito.mock(HttpServletRequest.class);
		AuthDTO authDTO = new AuthDTO()
				.setPassword("Password$")
				.setUsername("username user 1");
		userSession = JWTFilter.generateToken(authDTO);
		
		when(request.getAttribute("username"))
			.thenReturn("username");
	}
	
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
    
    public String getToken(){
    	return this.userSession.getToken();
    }
}
