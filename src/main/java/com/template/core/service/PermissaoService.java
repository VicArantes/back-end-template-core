package com.template.core.service;

import com.template.core.entity.Permissao;
import com.template.core.repository.PermissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
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

    public void adicionaPermissoes() {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(RestController.class);

        for (String beanName : beanNames) {
            Object controllerBean = applicationContext.getBean(beanName);
            Class<?> controllerClass = controllerBean.getClass();

            RequestMapping classRequestMapping = AnnotatedElementUtils.findMergedAnnotation(controllerClass, RequestMapping.class);
            String[] classPaths = classRequestMapping != null ? classRequestMapping.value() : new String[]{""};

            for (Method method : controllerClass.getDeclaredMethods()) {
                RequestMapping methodRequestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);

                if (methodRequestMapping != null) {
                    String[] methodPaths = methodRequestMapping.value();
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
            }
        }
    }

}
