package tia_sarwoedhi.belajar_spring_restful_api.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import tia_sarwoedhi.belajar_spring_restful_api.entity.User;
import tia_sarwoedhi.belajar_spring_restful_api.model.UserResponse;
import tia_sarwoedhi.belajar_spring_restful_api.model.request.RegisterUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tia_sarwoedhi.belajar_spring_restful_api.model.request.UpdateUserRequest;
import tia_sarwoedhi.belajar_spring_restful_api.repository.UserRepository;
import tia_sarwoedhi.belajar_spring_restful_api.security.BCrypt;

import java.util.Objects;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validator;
    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request) {

        validator.validate(request);

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already registered!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);
    }


    public UserResponse get(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request) {
        validationService.validate(request);
        log.info("REQUEST : {}"+ request);
        if (Objects.nonNull(request.getName())) {
            user.setName(request.getName());
        }

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }


        userRepository.save(user);

        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }



}
