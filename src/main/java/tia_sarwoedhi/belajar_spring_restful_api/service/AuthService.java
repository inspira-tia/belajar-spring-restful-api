package tia_sarwoedhi.belajar_spring_restful_api.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tia_sarwoedhi.belajar_spring_restful_api.entity.User;
import tia_sarwoedhi.belajar_spring_restful_api.model.TokenResponse;
import tia_sarwoedhi.belajar_spring_restful_api.model.request.LoginUserRequest;
import tia_sarwoedhi.belajar_spring_restful_api.repository.UserRepository;
import tia_sarwoedhi.belajar_spring_restful_api.security.BCrypt;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validator;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validator.validate(request);

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong"));
        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpireAt(next30Days());
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpireAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }
    }

    @Transactional
    public void logout(User user){
        user.setToken(null);
        user.setTokenExpireAt(null);

        userRepository.save(user);
    }

    private Long next30Days() {
        return System.currentTimeMillis() * (1000 * 16 * 24 * 30);
    }
}
