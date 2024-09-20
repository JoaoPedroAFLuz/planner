package br.com.joaopedroafluz.timely.user;

import br.com.joaopedroafluz.timely.exceptions.InvalidRequestException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllByEmail(List<String> emails) {
        return userRepository.findAllByEmails(emails);
    }

    public List<User> findAllByTripCode(UUID tripCode) {
        return userRepository.findAllByTripsAsParticipantTripCode(tripCode);
    }

    public User registerUser(User newUser) {
        var registeredUser = findByEmail(newUser.getEmail());

        if (registeredUser.isPresent() && registeredUser.get().getPassword() != null) {
            throw new InvalidRequestException("E-mail já está em uso.");
        }

        if (registeredUser.isPresent()) {
            registeredUser.get().setName(newUser.getName());
            registeredUser.get().setPassword(newUser.getPassword());
            newUser = registeredUser.get();
        }

        var hashedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);
        newUser.setCode(UUID.randomUUID());

        return save(newUser);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

}
