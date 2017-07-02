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
 * @author Pablo Gaitan
 *
 */
public class JWTFilter {
	
	public static UserSession generateToken(AuthDTO creds) throws Exception {
		// Generate secret key on runtime execution.
		KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
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
				.expirationTime(new Date(calendar.getTimeInMillis() + (1000 * 120)))
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
		// Serialize to compact JOSE form...
		// This string is sent to Client.
		return us;
	}

	public static boolean validateToken(String token,String secret) throws Exception {
		// Parse into JWE object again...
		SecretKey secretKey = convertSecretKey(secret);
		JWEObject jweObject = JWEObject.parse(token);

		// Decrypt
		jweObject.decrypt(new DirectDecrypter(secretKey));

		// Get the plain text
		Payload payload = jweObject.getPayload();
//		if(isExpired(((Date)payload.toJSONObject().get("exp")).getTime()))
//			return false;

		return true;
	}
	
	private static boolean isExpired(long timestamp){
		return timestamp<System.currentTimeMillis();
	}
	
	private static SecretKey convertSecretKey(String secretKey){
		// decode the base64 encoded string
		byte[] decodedKey = Base64.decodeBase64(secretKey);
		// rebuild key using SecretKeySpec
		SecretKey originalKey = new SecretKeySpec(decodedKey,  "HmacSHA256"); 
		return originalKey;
	}

}
