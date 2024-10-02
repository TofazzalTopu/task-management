package basico.task.management.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import basico.task.management.dto.GlpiSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DocumentLoader {


	@Value("${glpi.remote.user}")
	private String username;

	@Value("${glpi.remote.pass}")
	private String password;

	@Value("${url.glpi.init}")
	private String init;

	@Value("${url.glpi.token}")
	private String token;

	public InputStream getFiles(String urlString) {
		InputStream in = null;
		try {
			URL url = new URL(urlString);
			String passwdstring = username + ":" +password;
			String encoding = new Base64().encodeBase64String(passwdstring.getBytes());
			URLConnection uc = url.openConnection();
			uc.setRequestProperty("Session-Token",getSessionToken());
			in = uc.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return in;
	}

	public  InputStream getFilesByPath(String path) {

		InputStream in = null;
		String user = username;
		try {

			URL url = new URL(path);
			String passwdstring = username + ":"+password ;
			String encoding = new Base64().encodeBase64String(passwdstring.getBytes());
			URLConnection uc = url.openConnection();
			uc.setRequestProperty("Authorization", "Basic " + encoding);
			in = uc.getInputStream();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return in;
	}

	public String getSessionToken() throws JsonProcessingException {
		RestTemplate restTemplate =new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization","user_token "+token);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(null,headers);
		ResponseEntity<String> response = restTemplate.exchange(init, HttpMethod.GET, entity, String.class);
		GlpiSession glpiSession=new ObjectMapper().readValue(response.getBody(), GlpiSession.class);
		return  glpiSession.getSession_token();
	}


}
