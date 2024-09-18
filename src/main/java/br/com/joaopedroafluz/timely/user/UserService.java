package br.com.joaopedroafluz.timely.user;

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


    public Optional<User> findByCode(UUID code) {
        return userRepository.findByCode(code);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllByEmail(List<String> emails) {
        return userRepository.findAllByEmails(emails);
    }

    public List<User> findAllByTripCode(UUID tripCode) {
        return userRepository.findAllByTripsAsParticipantTripCode(tripCode);
    }

    public User registerUser(User user) {
        findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new IllegalStateException("Email already in use");
        });

        var hashedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(hashedPassword);

        return save(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

}
