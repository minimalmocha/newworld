package NewWorld.controller;

import NewWorld.dto.UserDto;
import NewWorld.exception.CustomError;
import NewWorld.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JoinController {

    private final UserService userService;

    @PostMapping(value = "/join")
    public String join(@RequestBody UserDto userDto) throws CustomError {
        String joinCheck = userService.join(userDto);
        return joinCheck;
    }
}
