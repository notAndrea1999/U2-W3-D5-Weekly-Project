package andreademasi.controllers;

import andreademasi.entities.User;
import andreademasi.exceptions.BadRequestException;
import andreademasi.payloads.users.NewUserDTO;
import andreademasi.payloads.users.SeccessfullLoginDTO;
import andreademasi.payloads.users.UserLoginDTO;
import andreademasi.services.AuthService;
import andreademasi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public SeccessfullLoginDTO login(@RequestBody UserLoginDTO userLogin) {
        return new SeccessfullLoginDTO(authService.authUser(userLogin));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    User saveUser(@RequestBody @Validated NewUserDTO userDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            try {
                return userService.saveUser(userDTO);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    @PatchMapping("/update/role/{id}")
    public User findByIdAndUpdateRole(@PathVariable long id) {
        return userService.findByIdAndUpdateRole(id);
    }

}
