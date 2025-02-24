package com.template.core.service;

import com.template.core.entity.Permissao;
import com.template.core.repository.PermissaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Optional;

/**
 * Serviço para manipulação de roles.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class PermissaoService {
    private final PermissaoRepository repository;
    private final ApplicationContext applicationContext;

    /**
     * Busca uma permissão pelo ID.
     *
     * @param id o ID da permissão a ser buscada
     * @return a permissão encontrada
     * @throws EntityNotFoundException se a permissão não for encontrada
     */
    public Permissao findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Permissao não encontrado"));
    }

    /**
     * Retorna uma página de permissões.
     *
     * @param pageable informações de paginação
     * @return a página de permissões
     */
    public Page<Permissao> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Salva uma permissão.
     *
     * @param permissao a permissão a ser salva
     * @return a permissao salva
     * @throws IllegalStateException se o permissão já possui um ID atribuído
     */
    public Permissao save(Permissao permissao) {
        if (permissao.getId() == null) {
            return repository.save(permissao);
        }

        throw new IllegalStateException("Entidade já possui um ID, utilizar a requisição de update.");
    }

    /**
     * Atualiza uma permissão.
     *
     * @param permissao a permissão a ser atualizada
     * @return a permissão atualizada
     * @throws EntityNotFoundException se a permissão não for encontrada
     */
    public Permissao update(Permissao permissao) {
        if (repository.findById(permissao.getId()).isPresent()) {
            return repository.save(permissao);
        }

        throw new EntityNotFoundException(MessageFormat.format("Permissão com ID {0} não encontrada.", permissao.getId()));
    }

    /**
     * Exclui uma permissão pelo ID.
     *
     * @param id o ID da permissão a ser excluída
     */
    public void deleteById(Long id) {
        repository.setInativo(id);
    }

    /**
     * Salva permissões para os endpoints combinados entre classe e método.
     *
     * @param classPaths  Paths da classe do controller.
     * @param methodPaths Paths do método do controller.
     */
    private void savePermissions(String[] classPaths, String[] methodPaths) {
        for (String classPath : classPaths) {
            for (String methodPath : methodPaths) {
                String fullPath = classPath + methodPath;
                if (!fullPath.startsWith("${springdoc.api-docs.path")) {
                    Optional<Permissao> permissao = repository.findByEndpoint(fullPath);
                    if (permissao.isEmpty()) {
                        repository.save(new Permissao(null, fullPath, true));
                    }
                }
            }
        }
    }

    /**
     * Método principal para adicionar permissões extraídas das controllers anotados com @RestController.
     */
    public void adicionaPermissoes() {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(RestController.class);
        for (String beanName : beanNames) {
            Class<?> controllerClass = applicationContext.getBean(beanName).getClass();
            RequestMapping classRequestMapping = AnnotatedElementUtils.findMergedAnnotation(controllerClass, RequestMapping.class);
            String[] classPaths = (classRequestMapping != null) ? classRequestMapping.value() : new String[]{""};

            for (Method method : controllerClass.getDeclaredMethods()) {
                RequestMapping methodRequestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
                if (methodRequestMapping != null) {
                    this.savePermissions(classPaths, methodRequestMapping.value());
                }
            }
        }
    }

}
