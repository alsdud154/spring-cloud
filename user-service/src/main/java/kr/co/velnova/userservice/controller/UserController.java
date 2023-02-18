package kr.co.velnova.userservice.controller;

import kr.co.velnova.userservice.dto.UserDto;
import kr.co.velnova.userservice.service.UserService;
import kr.co.velnova.userservice.vo.RequestUser;
import kr.co.velnova.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping
public class UserController {

    private final UserService service;
    private final Environment env;

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service on PORT %s", env.getProperty("local.server.port"));
    }

    @GetMapping("/welcome")
    public String welcome() {
        return env.getProperty("greeting.message");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public ResponseUser createUser(@RequestBody RequestUser requestUser) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(requestUser, UserDto.class);

        return mapper.map(service.createUser(userDto), ResponseUser.class);
    }

    @GetMapping("/users")
    public List<ResponseUser> getUsers() {
        List<ResponseUser> result = new ArrayList<>();
        service.getUserByAll().forEach(userEntity -> {
            result.add(new ModelMapper().map(userEntity, ResponseUser.class));
        });

        return result;
    }

    @GetMapping("/users/{userId}")
    public ResponseUser getUser( @PathVariable String userId) {
        UserDto userDto = service.getUserByUserId(userId);
        return new ModelMapper().map(userDto, ResponseUser.class);

    }
}
