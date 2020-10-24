package com.example.webservicelibrary.services;

import com.example.webservicelibrary.entities.LibraryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class LibraryUserDetailsService implements UserDetailsService {

    @Autowired
    private LibraryUserService libraryUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LibraryUser user = libraryUserService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username :" + username + "not found");
        }
        return new User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user));
    }

    /**
     *
     * @param user - with the logged in username
     * @return the role(s) the user logged in have.
     */
    private Collection<GrantedAuthority> getGrantedAuthorities(LibraryUser user) {
        return user.getAcl().stream()
                .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority))
                .collect(Collectors.toList());
    }
}
