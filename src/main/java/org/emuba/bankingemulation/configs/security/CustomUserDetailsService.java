package org.emuba.bankingemulation.configs.security;

import org.emuba.bankingemulation.models.CustomClient;
import org.emuba.bankingemulation.services.impl.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientServiceImpl clientService;

    @Autowired
    public CustomUserDetailsService(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomClient client = clientService.findClientByLogin(username);
        if (client == null)
            throw new UsernameNotFoundException("Username not found");

        List<GrantedAuthority> roles = List.of(
                new SimpleGrantedAuthority(client.getRole().toString()));

        return new User(client.getLogin(), client.getPassword(), roles);
    }
}

