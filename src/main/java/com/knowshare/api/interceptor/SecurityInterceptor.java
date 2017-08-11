/**
 * 
 */
package com.knowshare.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.knowshare.api.security.JWTFilter;
import com.knowshare.enterprise.repository.app.UserSessionRepository;
import com.knowshare.entities.app.UserSession;

/**
 * Interceptor encargado de revisar la autenticidad de un mensaje
 * que llega al servidor.
 * @author Miguel Montañez
 *
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter{
	
	private UserSessionRepository userSessionRepository;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public SecurityInterceptor(UserSessionRepository userSessionRepository) {
		this.userSessionRepository = userSessionRepository;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(!request.getMethod().equalsIgnoreCase("options")){
			String token = request.getHeader("Authorization");
			response.addHeader("Access-Control-Allow-Origin", "*");
			if(token == null){
				response.sendError(401);
				return false;
			}
			logger.info("Path: {} validando token {}",request.getServletPath(),token);
			UserSession user = userSessionRepository.findByToken(token);
			if(null == user || !JWTFilter.validateToken(token, user.getSecretKey())){
				response.sendError(401);
				return false;
			}
			String username = JWTFilter.getSub(token, user.getSecretKey());
			if(!username.equalsIgnoreCase(user.getUsername())){
				response.sendError(401);
				return false;
			}
			request.setAttribute("username", username);
		}
		return true;
	}
}
