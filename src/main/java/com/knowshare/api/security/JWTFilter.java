/**
 * 
 */
package com.knowshare.api.security;

import java.util.Calendar;
import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knowshare.dto.perfilusuario.AuthDTO;
import com.knowshare.entities.app.UserSession;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * Clase que permite hacer operaciones sobre un token
 * JWT. Con esta, se mira si el token provisto por el
 * cliente es válido y además si ha expirado o no.
 * @author Pablo Gaitan
 *
 */
public class JWTFilter {
	
	private final static Logger logger = LoggerFactory.getLogger(JWTFilter.class);
	
	/**
	 * A partir de las credenciales que ya fueron verificadas,
	 * previamente, se procede a generar el token único para
	 * el usuario.
	 * @param creds Credenciales de ingreso de cierto usuario.
	 * @return Se crea una sesión en el servidor que es posteriormente
	 * guardada.
	 */
	public static UserSession generateToken(AuthDTO creds){
		// Generate secret key on runtime execution.
		KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey secretKey = keyGen.generateKey();

			//store it in db sb.toString()
			byte[] byteAes = secretKey.getEncoded();
			String secretStore = Base64.encodeBase64String(byteAes);

			// Create the header
			JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);

			Calendar calendar = Calendar.getInstance();
			// Prepare JWT with claims set
			JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
					.subject(creds.getUsername())
					// token lasts a day
					.expirationTime(new Date(calendar.getTimeInMillis() + (1000 * 86400)))
					.build();
			
			// Set the payload
			Payload payload = new Payload(claimsSet.toJSONObject());

			// Create the JWE object and encrypt it
			JWEObject jweObject = new JWEObject(header, payload);
			jweObject.encrypt((JWEEncrypter) new DirectEncrypter(secretKey));
			String token = jweObject.serialize();
			UserSession us = new UserSession();
			us.setUsername(creds.getUsername());
			
			us.setSecretKey(secretStore);
			us.setToken(token);
			return us;
		} catch (Exception e) {
			logger.error("::::: Error generating token for user: "+creds.getUsername()
					+ " :::::");
			return null;
		}
	}

	/**
	 * Se encarga de verificar que el token enviado desde el cliente
	 * no haya expirado o que sea válido.
	 * @param token de tipo JWT
	 * @param secret SecretKey que es especial para cada sesión.
	 * @return true si el token es válido, de lo contrario false.
	 */
	public static boolean validateToken(String token,String secret){
		// Parse into JWE object again...
		SecretKey secretKey = convertSecretKey(secret);
		JWEObject jweObject;

		// Decrypt
		try {
			jweObject = JWEObject.parse(token);
			jweObject.decrypt(new DirectDecrypter(secretKey));
		} catch (Exception e) {
			logger.error("::::: Error validating the next token: "+token
				+ " :::::");
			return false;
		}

		// Get the plain text
		Payload payload = jweObject.getPayload();
		if(isExpired((Long)payload.toJSONObject().get("exp")))
			return false;

		return true;
	}
	
	/**
	 * Ya que un token puede tener información básica alojada
	 * en su composición, se obtiene la propiedad sub que es
	 * el username del usuario dueño del token.
	 * @param token de tipo JWT
	 * @param secret SecretKey que es especial para cada sesión.
	 * @return Username del usuario, de lo contrario null
	 */
	public static String getSub(String token, String secret){
		SecretKey secretKey = convertSecretKey(secret);
		JWEObject jweObject;

		// Decrypt
		try {
			jweObject = JWEObject.parse(token);
			jweObject.decrypt(new DirectDecrypter(secretKey));
		} catch (Exception e) {
			logger.error("::::: Error getting sub from the next token: "+token
				+ " :::::");
			return null;
		}
		
		Payload payload = jweObject.getPayload();
		return (String)payload.toJSONObject().get("sub");
	}
	
	/**
	 * Valida que la fecha del token dada en segundos no haya
	 * expirado.
	 * @param timestamp en segundos
	 * @return true si expiró, de lo contrario false
	 */
	private static boolean isExpired(long timestamp){
		return timestamp < (System.currentTimeMillis()/1000);
	}
	
	/**
	 * Reconstruye la secretKey que fue guardada en base64
	 * a formato {@link SecretKey}
	 * @param secretKey
	 * @return
	 */
	private static SecretKey convertSecretKey(String secretKey){
		// decode the base64 encoded string
		byte[] decodedKey = Base64.decodeBase64(secretKey);
		// rebuild key using SecretKeySpec
		SecretKey originalKey = new SecretKeySpec(decodedKey,  "HmacSHA256"); 
		return originalKey;
	}

}
