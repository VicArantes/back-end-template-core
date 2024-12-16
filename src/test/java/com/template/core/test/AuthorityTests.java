package com.template.core.test;

import com.template.core.entity.Authority;
import com.template.core.repository.AuthorityRepository;
import com.template.core.service.AuthorityService;
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

@DataJpaTest
class AuthorityTests {

    @Autowired
    private AuthorityRepository repository;

    private AuthorityService service;

    @BeforeEach
    void setup() {
        service = new AuthorityService(repository);
    }

    private Authority saveAuthorityTeste() {
        return service.save(new Authority(null, "AUTHORITY TESTE"));
    }

    @Test
    void findById_success() {
        Authority savedAuthority = this.saveAuthorityTeste();
        Authority authority = service.findById(savedAuthority.getId());

        Assertions.assertEquals(savedAuthority.getId(), authority.getId());
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
        Assertions.assertNotNull(this.saveAuthorityTeste());
    }

    @Test
    void save_withId() {
        Authority authority = this.saveAuthorityTeste();
        Assertions.assertThrows(IllegalStateException.class, () -> service.save(authority));
    }

    @Test
    void save_invalidParams() {
        Authority authority = new Authority(null, "");
        Assertions.assertThrows(ConstraintViolationException.class, () -> service.save(authority));
    }

    @Test
    void save_sameParams() {
        Assertions.assertNotNull(this.saveAuthorityTeste());
        Assertions.assertThrows(DataIntegrityViolationException.class, this::saveAuthorityTeste);
    }

    @Test
    void update_success() {
        Authority authority = this.saveAuthorityTeste();
        authority.setAuthorityName("AUTHORITY TESTE UPDATE");
        service.update(authority);

        Authority authorityFound = service.findById(authority.getId());
        Assertions.assertEquals(authority.getAuthorityName(), authorityFound.getAuthorityName());
    }

    @Test
    void update_invalidId() {
        Authority authority = this.saveAuthorityTeste();
        authority.setId(0L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.update(authority));
    }

    @Test
    void delete_success() {
        Long authorityId = this.saveAuthorityTeste().getId();

        service.deleteById(authorityId);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.findById(authorityId));
    }

    @Test
    void delete_invalidId() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> service.deleteById(null));
    }

}