package basico.task.management.dto;

import basico.task.management.model.primary.UserProfile;
import lombok.Data;

import java.io.Serializable;

@Data
public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private String token;
	private UserProfile user;

	public JwtResponse(UserProfile user, String token) {
		this.user = user;
		this.token = token;
	}
}
