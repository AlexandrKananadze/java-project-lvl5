package hexlet.code.app.service;

import java.util.Map;

public interface TokenService {
    String expiring(Map<String, Object> attr);
    Map<String, Object> verify(String token);
}
