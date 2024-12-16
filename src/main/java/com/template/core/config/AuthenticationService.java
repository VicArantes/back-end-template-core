package com.template.core.config;

import com.template.core.entity.User;
import com.template.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Esta classe é um serviço de autenticação que implementa a interface UserDetailsService do Spring Security.
 */
@RequiredArgsConstructor
@Service
public class AuthenticationService implements UserDetailsService {
    private final UserRepository repository;

    /**
     * Carrega os detalhes do usuário com base no nome de usuário fornecido.
     *
     * @param username O nome de usuário do usuário a ser carregado.
     * @return Os detalhes do usuário como um objeto UserDetails.
     * @throws UsernameNotFoundException Se o usuário com o nome de usuário fornecido não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        } else {
            return user;
        }
    }

}
