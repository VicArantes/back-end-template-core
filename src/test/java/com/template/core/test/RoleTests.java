//package com.template.core.test;
//
//import com.template.core.entity.Role;
//import com.template.core.repository.RoleRepository;
//import com.template.core.service.RoleService;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.validation.ConstraintViolationException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.dao.InvalidDataAccessApiUsageException;
//import org.springframework.data.domain.PageRequest;
//
//@DataJpaTest
//class RoleTests {
//
//    @Autowired
//    private RoleRepository repository;
//
//    private RoleService service;
//
//    @BeforeEach
//    void setup() {
//        service = new RoleService(repository);
//    }
//
//    private Role saveAuthorityTeste() {
//        return service.save(new Role(null, "AUTHORITY TESTE"));
//    }
//
//    @Test
//    void findById_success() {
//        Role savedAuthority = this.saveAuthorityTeste();
//        Role role = service.findById(savedAuthority.getId());
//
//        Assertions.assertEquals(savedAuthority.getId(), role.getId());
//    }
//
//    @Test
//    void findById_nonExistentId() {
//        Assertions.assertThrows(EntityNotFoundException.class, () -> service.findById(0L));
//    }
//
//    @Test
//    void findById_nullId() {
//        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> service.findById(null));
//    }
//
//    @Test
//    void findAll_success() {
//        Assertions.assertTrue(service.findAll(PageRequest.of(0, 100)).getTotalElements() >= 0);
//    }
//
//    @Test
//    void findAll_withoutPageable() {
//        Assertions.assertThrows(NullPointerException.class, () -> service.findAll(null));
//    }
//
//    @Test
//    void save_success() {
//        Assertions.assertNotNull(this.saveAuthorityTeste());
//    }
//
//    @Test
//    void save_withId() {
//        Role role = this.saveAuthorityTeste();
//        Assertions.assertThrows(IllegalStateException.class, () -> service.save(role));
//    }
//
//    @Test
//    void save_invalidParams() {
//        Role role = new Role(null, "");
//        Assertions.assertThrows(ConstraintViolationException.class, () -> service.save(role));
//    }
//
//    @Test
//    void save_sameParams() {
//        Assertions.assertNotNull(this.saveAuthorityTeste());
//        Assertions.assertThrows(DataIntegrityViolationException.class, this::saveAuthorityTeste);
//    }
//
//    @Test
//    void update_success() {
//        Role role = this.saveAuthorityTeste();
//        role.setAuthorityName("AUTHORITY TESTE UPDATE");
//        service.update(role);
//
//        Role authorityFound = service.findById(role.getId());
//        Assertions.assertEquals(role.getAuthorityName(), authorityFound.getAuthorityName());
//    }
//
//    @Test
//    void update_invalidId() {
//        Role role = this.saveAuthorityTeste();
//        role.setId(0L);
//        Assertions.assertThrows(EntityNotFoundException.class, () -> service.update(role));
//    }
//
//    @Test
//    void delete_success() {
//        Long authorityId = this.saveAuthorityTeste().getId();
//
//        service.deleteById(authorityId);
//
//        Assertions.assertThrows(EntityNotFoundException.class, () -> service.findById(authorityId));
//    }
//
//    @Test
//    void delete_invalidId() {
//        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> service.deleteById(null));
//    }
//
//}