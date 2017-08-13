/**
 * 
 */
package com.knowshare.test.api.general;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowshare.api.security.JWTFilter;
import com.knowshare.dto.perfilusuario.AuthDTO;
import com.knowshare.enterprise.repository.app.UserSessionRepository;
import com.knowshare.entities.app.UserSession;

/**
 * @author Miguel Montañez
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes={ConfigApiContext.class})
public abstract class AbstractApiTest {

	@Autowired
	protected MockMvc mockMvc;
	
	protected UserSession userSession;
	
	@MockBean
	protected UserSessionRepository userSessionRepository;
	
	protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
	
	@PostConstruct
	private void init(){
		AuthDTO authDTO = new AuthDTO()
				.setPassword("Password$")
				.setUsername("username user 1");
		userSession = JWTFilter.generateToken(authDTO);
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
