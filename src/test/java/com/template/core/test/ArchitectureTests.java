package com.template.core.test;

import com.template.core.TemplateCoreApplication;
import com.template.core.util.annotation.IgnoreMaxLinesCheck;
import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import jakarta.persistence.*;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.LineNumberAttribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packagesOf = TemplateCoreApplication.class)
public class ArchitectureTests {

    public static ArchCondition<JavaClass> satisfyRepositoryHasCorrectEndingName() {
        return new ArchCondition<>("verify id repositories ends with Repository") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes") && !javaClass.getSimpleName().endsWith("Repository")) {
                    events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - should ends with Repository".formatted(javaClass.getSimpleName())));
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyControllerHasCorrectEndingName() {
        return new ArchCondition<>("verify if controllers ends with Controller") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes") && !javaClass.getSimpleName().endsWith("Controller")) {
                    events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - should ends with Controller".formatted(javaClass.getSimpleName())));
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyEntitiesAnnotationsProperties() {
        return new ArchCondition<>("verify if entities be annotated with @Table(name = 'tableName') and @Entity") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                boolean hasTableAnnotation = false;
                boolean tableNameNotEmpty = false;
                boolean hasEntityAnnotation = false;
                boolean entityNameIsEmpty = false;

                JavaClass originalClass = javaClass;

                if (!javaClass.getSimpleName().endsWith("Builder") && javaClass.getSuperclass().isPresent()) {
                    while (!javaClass.getSuperclass().get().getName().equals("java.lang.Object") && !javaClass.getSuperclass().get().getName().contains("java.lang.Enum")) {
                        javaClass = javaClass.getSuperclass().get().toErasure();
                    }

                    for (JavaAnnotation<JavaClass> annotation : javaClass.getAnnotations()) {
                        if (annotation.getRawType().isAssignableTo(Table.class)) {
                            hasTableAnnotation = true;

                            if (!annotation.getProperties().get("name").toString().isEmpty()) {
                                tableNameNotEmpty = true;
                            }
                        }

                        if (annotation.getRawType().isAssignableTo(Entity.class)) {
                            hasEntityAnnotation = true;

                            if (annotation.getProperties().get("name").toString().isEmpty()) {
                                entityNameIsEmpty = true;
                            }
                        }
                    }

                    if (!hasTableAnnotation) {
                        events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - should be annotated with @Table".formatted(originalClass.getSimpleName())));
                    }

                    if (!hasEntityAnnotation) {
                        events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - should be annotated with @Entity".formatted(originalClass.getSimpleName())));
                    }

                    if (hasTableAnnotation && !tableNameNotEmpty) {
                        events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - @Table must have 'name' property".formatted(originalClass.getSimpleName())));
                    }

                    if (hasEntityAnnotation && !entityNameIsEmpty) {
                        events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - @Entity must haven't 'name' property".formatted(originalClass.getSimpleName())));
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyIdFieldConditions() {
        return new ArchCondition<>("verify Id field exists with Long type and be annotated with @Id and @GeneratedValue(strategy = GenerationType.IDENTITY)") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                boolean idHasRequiredAnnotations = false;

                JavaClass originalClass = javaClass;

                if (!javaClass.getSimpleName().endsWith("Builder") && javaClass.getSuperclass().isPresent()) {
                    while (!javaClass.getSuperclass().get().getName().equals("java.lang.Object") && !javaClass.getSuperclass().get().getName().contains("java.lang.Enum")) {
                        javaClass = javaClass.getSuperclass().get().toErasure();
                    }

                    for (JavaField field : javaClass.getFields()) {
                        if (!idHasRequiredAnnotations) {
                            idHasRequiredAnnotations = field.getName().equals("id") && field.getType().getName().equals(Long.class.getName()) && field.isAnnotatedWith(Id.class) &&
                                    field.isAnnotatedWith(GeneratedValue.class) && field.getAnnotationOfType(GeneratedValue.class).strategy() == GenerationType.IDENTITY;
                        }
                    }

                    if (!idHasRequiredAnnotations) {
                        events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - id field should be Long and be annotated with @Id and @GeneratedValue(strategy = GenerationType.IDENTITY)".formatted(originalClass.getSimpleName())));
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyFieldsShouldHaveJakartaAnnotation() {
        return new ArchCondition<>("verify if fields have Jakarta annotation") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (!javaClass.getSimpleName().endsWith("Builder") && javaClass.getSuperclass().isPresent()) {
                    for (JavaField field : javaClass.getFields()) {
                        boolean hasJakartaAnnotation = false;

                        for (JavaAnnotation<JavaField> annotation : field.getAnnotations()) {
                            if (annotation.getRawType().getName().startsWith("jakarta.persistence.")) {
                                hasJakartaAnnotation = true;
                            }
                        }

                        if (!hasJakartaAnnotation) {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - Field: '%s' - should be annotated with Jakarta annotation".formatted(javaClass.getSimpleName(), field.getName())));
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyRepositoryConditions() {
        return new ArchCondition<>("verify if repositories is a interface and extends JpaRepository") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes")) {
                    boolean extendsJpaRepository = javaClass.getInterfaces().stream().anyMatch(interfaceType -> interfaceType.toErasure().isAssignableTo("org.springframework.data.jpa.repository.JpaRepository"));

                    if (!javaClass.isInterface() && !extendsJpaRepository) {
                        events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - should be interface and extends JpaRepository".formatted(javaClass.getSimpleName())));
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyServiceAnnotations() {
        return new ArchCondition<>("verify if services be annotated with @Service and not be annotated with @Transactional") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                boolean hasServiceAnnotation = false;
                boolean hasTransactionalAnnotation = false;

                if (!javaClass.isInterface() && javaClass.getSuperclass().isPresent()) {
                    for (JavaAnnotation<JavaClass> annotation : javaClass.getAnnotations()) {
                        if (annotation.getRawType().isAssignableTo(Service.class)) {
                            hasServiceAnnotation = true;
                        }

                        if (annotation.getRawType().isAssignableTo(Transactional.class)) {
                            hasTransactionalAnnotation = true;
                        }
                    }

                    if (!hasServiceAnnotation) {
                        events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - should be annotated with @Service".formatted(javaClass.getSimpleName())));
                    }

                    if (!hasTransactionalAnnotation) {
                        events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - should be annotated with @Transactional".formatted(javaClass.getSimpleName())));
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyControllerAnnotations() {
        return new ArchCondition<>("verify if controllers be annotated with @RestController and @RequestMapping") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                boolean hasRestControllerAnnotation = false;
                boolean hasRequestMappingAnnotation = false;

                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes")) {
                    for (JavaAnnotation<JavaClass> annotation : javaClass.getAnnotations()) {
                        if (annotation.getRawType().isAssignableTo(RestController.class)) {
                            hasRestControllerAnnotation = true;
                        }

                        if (annotation.getRawType().isAssignableTo(RequestMapping.class)) {
                            hasRequestMappingAnnotation = true;
                        }
                    }

                    if (!hasRestControllerAnnotation) {
                        events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - should be annotated with @RestController".formatted(javaClass.getSimpleName())));
                    }

                    if (!hasRequestMappingAnnotation) {
                        events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - should be annotated with @RequestMapping".formatted(javaClass.getSimpleName())));
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyControllersMethodsShouldHaveWebAnnotation() {
        return new ArchCondition<>("verify if controllers have Web annotation") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes")) {
                    for (JavaMethod method : javaClass.getMethods()) {
                        boolean hasSpringAnnotation = false;

                        if (!method.getAnnotations().isEmpty()) {
                            for (JavaAnnotation<JavaMethod> annotation : method.getAnnotations()) {
                                if (annotation.getRawType().getPackage().getName().startsWith("org.springframework.web.bind.annotation")) {
                                    hasSpringAnnotation = true;
                                }

                                if (!hasSpringAnnotation) {
                                    events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - Method: '%s' - should have Web annotation".formatted(javaClass.getSimpleName(), method.getName())));
                                }
                            }
                        } else {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - Method: '%s' - should have Web annotation".formatted(javaClass.getSimpleName(), method.getName())));
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyMethodsMustNotExceedMaxLines(int maxLines) {
        return new ArchCondition<>("verify if method lines have not exceeded %d lines".formatted(maxLines)) {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes") && !javaClass.isInterface()) {
                    try {
                        CtClass ctClass = ClassPool.getDefault().get(javaClass.getName());

                        for (Method method : javaClass.reflect().getDeclaredMethods()) {
                            if (!method.isAnnotationPresent(IgnoreMaxLinesCheck.class) && !method.getName().contains("lambda$")) {
                                CtMethod ctMethod = ctClass.getDeclaredMethod(method.getName());

                                int methodLines = ((LineNumberAttribute) ctMethod.getMethodInfo().getCodeAttribute().getAttribute(LineNumberAttribute.tag)).tableLength();

                                if (methodLines > maxLines) {
                                    events.add(SimpleConditionEvent.violated(method, "Class: '%s' - Method: '%s' - lines must not exceed %d lines".formatted(javaClass.getSimpleName(), method.getName(), maxLines)));
                                }
                            }
                        }
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        };
    }

    public static ArchCondition<JavaClass> satisfyControllersMethodsIsPublic() {
        return new ArchCondition<>("verify if controller methods is public") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes")) {
                    for (JavaMethod method : javaClass.getMethods()) {
                        if (!method.getModifiers().contains(JavaModifier.PUBLIC)) {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - Method: '%s' - should be public".formatted(javaClass.getSimpleName(), method.getName())));
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyFieldsIsPrivate() {
        return new ArchCondition<>("verify if fields are private") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (!javaClass.getSimpleName().endsWith("Builder") && javaClass.getSuperclass().isPresent()) {
                    for (JavaField field : javaClass.getFields()) {
                        if (!field.getModifiers().contains(JavaModifier.PRIVATE)) {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - Field: '%s' - should be private".formatted(javaClass.getSimpleName(), field.getName())));
                        }
                    }
                }
            }
        };
    }

    /**
     * Obtém o primeiro tipo genérico de um JavaType convertido para JavaClass.
     */
    private static JavaClass getFirstGenericType(JavaType javaType) {
        return javaType instanceof JavaClass javaClass
                ? javaClass.getAllRawInterfaces().stream().findFirst().orElse(null)
                : null;
    }

    public static ArchCondition<JavaClass> satisfyControllersMethodsReturnResponseEntityAndUseRecords() {
        return new ArchCondition<>("verify if controllers return ResponseEntity and use records") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes")) {
                    for (JavaMethod method : javaClass.getMethods()) {

                        JavaClass returnType = method.getReturnType().toErasure();
                        if (!returnType.isAssignableTo(ResponseEntity.class)) {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - Method: '%s' - should return ResponseEntity".formatted(javaClass.getSimpleName(), method.getName())));
                            continue;
                        }

                        for (JavaParameter param : method.getParameters()) {
                            if (param.isAnnotatedWith(RequestBody.class)) {
                                JavaClass paramType = param.getRawType();
                                if (!paramType.isRecord()) {
                                    events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - Method: '%s' - @RequestBody parameter '%s' should be a record".formatted(javaClass.getSimpleName(), method.getName(), paramType.getSimpleName())));
                                }
                            }
                        }

                        JavaClass genericType = getFirstGenericType(method.getReturnType());
                        if (genericType != null && !genericType.isEquivalentTo(Void.class)) {
                            if (genericType.isAssignableTo(List.class) || genericType.isAssignableTo(Page.class) || genericType.isAssignableTo(Set.class)) {
                                JavaClass innerGenericType = getFirstGenericType(genericType);
                                if (innerGenericType != null && !innerGenericType.isRecord()) {
                                    events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - Method: '%s' - ResponseEntity<List/Page/Set> should contain records, but found: %s".formatted(javaClass.getSimpleName(), method.getName(), innerGenericType.getSimpleName())));
                                }
                            } else if (!genericType.isRecord()) {
                                events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - Method: '%s' - ResponseEntity body should be a record, but found: %s".formatted(javaClass.getSimpleName(), method.getName(), genericType.getSimpleName())));
                            }
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyRepositoriesParametersMethodsHasParamAnnotation() {
        return new ArchCondition<>("has @Param in parameters") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                for (JavaMethod method : javaClass.getMethods()) {
                    boolean hasQueryAnnotation = false;

                    for (JavaAnnotation<JavaMethod> annotation : method.getAnnotations()) {
                        if (annotation.getRawType().getName().equals(Query.class.getName())) {
                            hasQueryAnnotation = true;
                        }
                    }

                    if (hasQueryAnnotation) {
                        boolean allParamsHaveParamAnnotation = true;

                        for (JavaParameter parameter : method.getParameters()) {
                            boolean hasParamAnnotation = false;
                            String paramName = method.reflect().getParameters()[parameter.getIndex()].getName();

                            if (!parameter.getAnnotations().isEmpty()) {
                                for (JavaAnnotation<JavaParameter> paramAnnotation : parameter.getAnnotations()) {
                                    if (paramAnnotation.getRawType().getName().equals(Param.class.getName())) {
                                        hasParamAnnotation = true;
                                        break;
                                    }
                                }

                                if (!hasParamAnnotation) {
                                    allParamsHaveParamAnnotation = false;
                                }

                                if (!allParamsHaveParamAnnotation) {
                                    events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - Method: '%s' - Parameter - '%s' - should has @Param".formatted(javaClass.getSimpleName(), method.getName(), paramName)));
                                }
                            } else if (!parameter.getType().toErasure().isAssignableTo(Pageable.class)) {
                                events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - Method: '%s' - Parameter - '%s' - should has @Param".formatted(javaClass.getSimpleName(), method.getName(), paramName)));
                            }
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyEntitiesShouldHaveAtivoField() {
        return new ArchCondition<>("Entities should have a boolean 'ativo' field with @Column(name = \"bl_ativo\")") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.isAnnotatedWith(Entity.class)) {
                    Optional<JavaField> ativoField = javaClass.getFields().stream().filter(field -> field.getName().equals("ativo") && field.getRawType().isEquivalentTo(boolean.class)).findFirst();

                    if (ativoField.isPresent()) {
                        JavaField field = ativoField.get();
                        Optional<Column> columnAnnotation = field.tryGetAnnotationOfType(Column.class);

                        boolean hasCorrectColumnAnnotation = columnAnnotation.map(annotation -> "bl_ativo".equals(annotation.name())).orElse(false);

                        if (!hasCorrectColumnAnnotation) {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' - Field: 'ativo' must have @Column(name = \"bl_ativo\")".formatted(javaClass.getSimpleName())));
                        }
                    } else {
                        events.add(SimpleConditionEvent.violated(javaClass, "Class: '%s' must have a boolean field named 'ativo' with @Column(name = \"bl_ativo\")".formatted(javaClass.getSimpleName())));
                    }
                }
            }
        };
    }

    @ArchTest
    public static final ArchRule repositoriesShould_SatisfyRepositoryHasCorrectEndingName = classes()
            .that().resideInAPackage("..repository..")
            .should(satisfyRepositoryHasCorrectEndingName())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule servicesShouldHaveSimpleNameEndingWith_Service = classes()
            .that().resideInAPackage("..service..")
            .and().haveSimpleNameEndingWith("Service")
            .should().haveSimpleNameEndingWith("Service")
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule controllersShould_SatisfyControllerHasCorrectEndingName = classes()
            .that().resideInAPackage("..controller..")
            .should(satisfyControllerHasCorrectEndingName())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule repositoriesShouldOnlyBeAccessedBy_Service_Test_Config = classes()
            .that().resideInAPackage("..repository..")
            .should().onlyBeAccessed().byAnyPackage("..service..", "..test..", "..config..")
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule servicesShouldOnlyBeAccessedBy_Service_Controller_Test_Config = classes()
            .that().resideInAPackage("..service..")
            .should().onlyBeAccessed().byAnyPackage("..service..", "..controller..", "..test..", "..config..")
            .allowEmptyShould(true);


    @ArchTest
    public static final ArchRule entitiesShould_SatisfyEntitiesAnnotationsProperties = classes()
            .that().resideInAPackage("..entity..")
            .should(satisfyEntitiesAnnotationsProperties())
            .allowEmptyShould(true);


    @ArchTest
    public static final ArchRule entitiesShould_satisfyIdFieldAnnotations = classes()
            .that().resideInAPackage("..entity..")
            .should(satisfyIdFieldConditions())
            .allowEmptyShould(true);


    @ArchTest
    public static final ArchRule entitiesShould_SatisfyFieldsShouldHaveJakartaAnnotation = classes()
            .that().resideInAPackage("..entity..")
            .should(satisfyFieldsShouldHaveJakartaAnnotation())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule repositoriesShould_SatisfyRepositoryConditions = classes()
            .that().resideInAPackage("..repository..")
            .should(satisfyRepositoryConditions())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule servicesShould_SatisfyServiceAnnotations = classes()
            .that().resideInAPackage("..service..")
            .and().haveSimpleNameEndingWith("Service")
            .should(satisfyServiceAnnotations())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule controllersShould_SatisfyControllerAnnotations = classes()
            .that().resideInAPackage("..controller..")
            .should(satisfyControllerAnnotations())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule controllersShould_SatisfyControllersShouldHaveWebAnnotation = classes()
            .that().resideInAPackage("..controller..")
            .should(satisfyControllersMethodsShouldHaveWebAnnotation())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule enumsShouldBe_Enum = classes()
            .that().resideInAPackage("..enums..")
            .should().beEnums()
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule dtosShouldNotBeAnnotatedWith_Entity_Table = classes()
            .that().resideInAPackage("..dto..")
            .should().notBeAnnotatedWith(Entity.class)
            .andShould().notBeAnnotatedWith(Table.class)
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule controllerShould_SatisfyMethodsMustNotExceedMaxLines = classes()
            .that().resideInAPackage("..controller..")
            .should(satisfyMethodsMustNotExceedMaxLines(5))
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule servicesShould_SatisfyMethodsMustNotExceedMaxLines = classes()
            .that().resideInAPackage("..service..")
            .should(satisfyMethodsMustNotExceedMaxLines(15)).allowEmptyShould(true);

    @ArchTest
    public static final ArchRule controllersShould_SatisfyControllersMethodsIsPublic = classes()
            .that().resideInAPackage("..controller..")
            .should(satisfyControllersMethodsIsPublic())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule entitiesShould_SatisfyFieldsIsPrivate = classes()
            .that().resideInAPackage("..entity..")
            .should(satisfyFieldsIsPrivate())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule dtosShould_SatisfyFieldsIsPrivate = classes()
            .that().resideInAPackage("..dto..")
            .should(satisfyFieldsIsPrivate())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule controllersShould_SatisfyControllersMethodsReturnResponseEntityAndUseRecords = classes()
            .that().resideInAPackage("..controller..")
            .should(satisfyControllersMethodsReturnResponseEntityAndUseRecords())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule repositoriesShould_SatisfyRepositoriesParametersMethodsHasParamAnnotation = classes()
            .that().resideInAPackage("..repository..")
            .should(satisfyRepositoriesParametersMethodsHasParamAnnotation())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule repositoriesShould_SatisfyEntitiesShouldHaveAtivoField = classes()
            .that().resideInAPackage("..entity..")
            .should(satisfyEntitiesShouldHaveAtivoField())
            .allowEmptyShould(true);

}