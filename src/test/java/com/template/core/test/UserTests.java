package com.template.core.test;

import com.template.core.entity.User;
import com.template.core.repository.UserRepository;
import com.template.core.service.RoleService;
import com.template.core.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
class UserTests {

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthorityRepository authorityRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private UserService service;
    private RoleService roleService;

    @BeforeEach
    void setup() {
        service = new UserService(repository, authorityRepository);
        roleService = new RoleService(authorityRepository);
    }

    private User saveUserTeste() {
        Authority authority = roleService.save(new Authority(null, "TESTE"));
        return service.save(new User(null, "USUÁRIO TESTE", bCryptPasswordEncoder().encode("SENHA TESTE"), "EMAIL TESTE", Status.ATIVO, new ArrayList<>(List.of(authority))));
    }

    @Test
    void findById_success() {
        User savedUser = this.saveUserTeste();
        User user = service.findById(savedUser.getId());

        Assertions.assertEquals(savedUser.getId(), user.getId());
    }

    @Test
    void findById_nonExistentId() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.findById(0L));
    }

    @Test
    void findById_nullId() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> service.findById(null));
    }

    @Test
    void findAll_success() {
        Assertions.assertTrue(service.findAll(PageRequest.of(0, 100)).getTotalElements() >= 0);
    }

    @Test
    void findAll_withoutPageable() {
        Assertions.assertThrows(NullPointerException.class, () -> service.findAll(null));
    }

    @Test
    void save_success() {
        Assertions.assertNotNull(this.saveUserTeste());
    }

    @Test
    void save_withId() {
        User user = this.saveUserTeste();
        Assertions.assertThrows(IllegalStateException.class, () -> service.save(user));
    }

    @Test
    void save_invalidParams() {
        User user = new User(null, "", "", "", null, null);
        Assertions.assertThrows(ConstraintViolationException.class, () -> service.save(user));
    }

    @Test
    void save_sameParams() {
        Assertions.assertNotNull(this.saveUserTeste());
        Assertions.assertThrows(DataIntegrityViolationException.class, this::saveUserTeste);
    }

    @Test
    void update_success() {
        User user = this.saveUserTeste();
        user.setStatus(Status.INATIVO);
        service.update(user);

        User userFound = service.findById(user.getId());
        Assertions.assertEquals(user.getStatus(), userFound.getStatus());
    }

    @Test
    void update_invalidId() {
        User user = this.saveUserTeste();
        user.setId(0L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.update(user));
    }

    @Test
    void delete_success() {
        Long userId = this.saveUserTeste().getId();

        service.deleteById(userId);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.findById(userId));
    }

    @Test
    void delete_invalidId() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> service.deleteById(null));
    }

}