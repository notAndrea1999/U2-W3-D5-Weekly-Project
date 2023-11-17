package andreademasi.services;

import andreademasi.entities.Role;
import andreademasi.entities.User;
import andreademasi.exceptions.BadRequestException;
import andreademasi.exceptions.NotFoundException;
import andreademasi.payloads.users.NewUserDTO;
import andreademasi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserService {
    @Autowired
    PasswordEncoder bcrypt;
    @Autowired
    private UserRepository userRepo;

    public User findUserById(long id) throws NotFoundException {
        return userRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepo.findAll(pageable);
    }

    public User saveUser(NewUserDTO userDTO) throws IOException {
        userRepo.findByEmail(userDTO.email()).ifPresent(user -> {
            throw new BadRequestException("L'email " + user.getEmail() + " è già utilizzata!");
        });
        userRepo.findByUsername(userDTO.username()).ifPresent(user -> {
            throw new BadRequestException(
                    "Lo username " + user.getUsername() + " è già utilizzato!");
        });
        User newUser = new User();
        newUser.setUsername(userDTO.username());
        newUser.setRole(Role.USER);
        newUser.setEmail(userDTO.email());
        newUser.setPassword(bcrypt.encode(userDTO.password()));
        return userRepo.save(newUser);
    }


    public void findUserByIdAndDelete(long id) throws NotFoundException {
        User foundUser = this.findUserById(id);
        userRepo.delete(foundUser);
    }


    public User findByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User con email " + email + " non trovato!"));
    }

    public User findByIdAndUpdateRole(long id) {
        User foundUser = this.findUserById(id);
        foundUser.setRole(Role.ORGANIZZATORE_EVENTI);
        return userRepo.save(foundUser);
    }
}
