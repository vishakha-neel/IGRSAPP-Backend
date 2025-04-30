package com.example.igrsapp.Service;

import java.util.Iterator;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.igrsapp.Repository.dataEntry.AppUserRepository;
import com.example.igrsapp.beans.UserBean;
import com.example.igrsapp.modals.dataEntry.AppUser;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username);
        if (user == null || !user.isEnable()) {
            throw new UsernameNotFoundException("User not found or disabled");
        }

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
        // throw new UsernameNotFoundException("User not found or disabled");
    }

    public AppUser registerUser(String username, String rawPassword, String role) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setEnable(true);
        return userRepository.save(user);
        // return null;
    }
}
