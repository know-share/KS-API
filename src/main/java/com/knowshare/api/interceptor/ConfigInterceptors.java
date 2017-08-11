/**
 * 
 */
package com.knowshare.api.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.knowshare.enterprise.repository.app.UserSessionRepository;

/**
 * Contiene la configuración básica para los
 * interceptores dentro de la aplicación.
 * @author Miguel Montañez
 *
 */
@Configuration
public class ConfigInterceptors extends WebMvcConfigurerAdapter {
	
	@Autowired
	private UserSessionRepository userSession;
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SecurityInterceptor(userSession))
			.addPathPatterns("/api/**")
			.excludePathPatterns(
				"/api/tg/findAll",
				"/api/carrera/findAll",
				"/api/carrera/getEnfasisAreaConocimiento",
				"/api/auth/**",
				"/api/cualidad/findAll",
				"/api/gusto/findAll",
				"/api/habilidad/getHabilidades",
				"/api/habilidad/getHabilidadesProfesionales",
				"/api/personalidad/**",
				"/api/usuario/isUsernameTaken",
				"/api/usuario/isCorreoTaken",
				"/api/usuario/",
				"/api/habilidad/getAll",
				"/api/usuario/image/**"
			);
    }
}
