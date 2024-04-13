package org.emuba.bankingemulation.service.impl;

import org.emuba.bankingemulation.model.CustomClient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final ClientServiceImpl clientService;

    public UserDetailServiceImpl(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        CustomClient client = clientService.findClientByLogin(login);
        if (client == null)
            throw new UsernameNotFoundException("Login: " + login);

        List<GrantedAuthority> roles = List.of(
                new SimpleGrantedAuthority(client.getRole().toString()));

        return new User(client.getLogin(), client.getPassword(), roles);
    }
}
