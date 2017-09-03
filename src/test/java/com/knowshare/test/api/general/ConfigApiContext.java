/**
 * 
 */
package com.knowshare.test.api.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.knowshare.api.interceptor.SecurityInterceptor;
import com.knowshare.enterprise.repository.app.UserSessionRepository;

/**
 * @author Miguel Montañez
 *
 */
@Configuration
@ComponentScan(basePackages = { "com.knowshare.api.controller", "com.knowshare.api.interceptor" }, 
	lazyInit = true)
public class ConfigApiContext extends WebMvcConfigurerAdapter{

	UserSessionRepository userSessionRepository;

	@Autowired
	public ConfigApiContext(UserSessionRepository userSessionRepository) {
		this.userSessionRepository = userSessionRepository;
	}

	@Bean
	public SecurityInterceptor customHandlerInterceptor() {
		return new SecurityInterceptor(userSessionRepository);
	}
}
