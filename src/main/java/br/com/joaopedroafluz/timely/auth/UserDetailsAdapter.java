package br.com.joaopedroafluz.timely.auth;

import br.com.joaopedroafluz.timely.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class UserDetailsAdapter implements UserDetails {

    private final transient User user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var roleUser = new SimpleGrantedAuthority("ROLE_USER");

        System.out.println(roleUser);

        return List.of(roleUser);
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

}
