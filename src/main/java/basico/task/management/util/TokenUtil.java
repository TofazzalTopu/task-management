package basico.task.management.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static basico.task.management.constant.AppConstants.SIGNING_KEY;

public class TokenUtil {

    public static Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
