/*
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.validation.extend;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatchers;
import io.domainlifecycles.builder.DomainBuilderConfiguration;
import io.domainlifecycles.builder.innerclass.InnerClassDefaultDomainBuilderConfiguration;
import io.domainlifecycles.domain.types.Validatable;
import io.domainlifecycles.validation.BeanValidations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Runtime byte code manipulations (by using ByteBuddy) to extend the {@link Validatable} classes
 * to call 'validate' to provide a valid-by-construction way of asserting invariants.
 *
 * @author Mario Herb
 */
public class ValidationDomainClassExtender {

    private static final Logger log = LoggerFactory.getLogger(ValidationDomainClassExtender.class);
    private static final Set<TypeDescription> extendedValidate = new HashSet<>();
    private static final Set<TypeDescription> extendedValidateParams = new HashSet<>();
    private static final Set<TypeDescription> extendedValidateReturn = new HashSet<>();
    private static volatile boolean extensionProcessed = false;

    private static List<String> basePackages;

    /**
     * Start the runtime byte code extension (for 'validate') by calling this method
     * with default {@link InnerClassDefaultDomainBuilderConfiguration}.
     *
     * @param basePack base package to enable validation calls
     */
    public static synchronized void extend(String... basePack) {
        ValidationDomainClassExtender.extend(new InnerClassDefaultDomainBuilderConfiguration(), basePack);
    }


    /**
     * Start the runtime byte code extension (for 'validate') by calling this method.
     *
     * @param domainBuilderConfiguration configuration to obtain the builder method
     * @param basePack                   base package to enable validation calls
     */
    public static synchronized void extend(
        DomainBuilderConfiguration domainBuilderConfiguration,
        String... basePack
    ) {
        if (!extensionProcessed) {
            basePackages = List.of(basePack);
            Instrumentation instrumentation = ByteBuddyAgent.install();
            premain("", instrumentation, domainBuilderConfiguration);
            try {
                instrumentation.retransformClasses(Object.class);
            } catch (UnmodifiableClassException e) {
                e.printStackTrace();
            }
            extendedValidate.forEach(t -> log.info(t.getName() + " extended validate call!"));
            extendedValidateParams.forEach(t -> log.info(t.getName() + " extended method parameter bean validations!"));
            extendedValidateReturn.forEach(
                t -> log.info(t.getName() + " extended method return values bean validations!"));
            extensionProcessed = true;
        } else {
            log.info("DLCDomainExtensions already processed!");
        }
    }

    /**
     * ByteBuddy advice to perform the 'validate' method call
     * on any method exit (e.g. setters or business logic) in order to apply
     * to the always-valid strategy.
     */
    public static class ValidationAdvice {
        @Advice.OnMethodExit
        public static void after(@Advice.This Object thisObject) {
            ((Validatable) thisObject).validate();
            BeanValidations.validate(thisObject);
        }
    }

    /**
     * ByteBuddy advice to validate all constraints on the parameters passed to the method before execution.
     */
    public static class BeanValidationParameterAdvice {
        @Advice.OnMethodEnter
        public static void before(@Advice.This Object thisObject,
                                  @Advice.Origin Method method,
                                  @Advice.AllArguments Object[] args) {
            BeanValidations.validateMethodParameters(thisObject, method, args);
        }
    }

    /**
     * ByteBuddy advice to validate all constraints on the return value of the method after execution.
     */
    public static class BeanValidationReturnValueAdvice {
        @Advice.OnMethodExit
        public static void after(@Advice.This Object thisObject,
                                 @Advice.Origin Method method,
                                 @Advice.Return(readOnly = true) Object returnValue) {
            ((Validatable) thisObject).validate();
            BeanValidations.validateMethodReturnValue(thisObject, method, returnValue);
        }

    }

    private static void premain(String agentArgs,
                                Instrumentation inst,
                                DomainBuilderConfiguration domainBuilderConfiguration) {
        var agentBuilder = new AgentBuilder.Default()
            .disableClassFormatChanges()
            .with(AgentBuilder.RedefinitionStrategy.REDEFINITION)
            .with(new AgentBuilder.RedefinitionStrategy.Listener.Adapter() {
                @Override
                public Iterable<? extends List<Class<?>>> onError(int index, List<Class<?>> batch,
                                                                  Throwable throwable, List<Class<?>> types) {
                    extendedValidate.clear();
                    extendedValidateParams.clear();
                    extendedValidateReturn.clear();
                    types.forEach(
                        t -> System.err.println(t.getName() + " failed to be extended: " + throwable.getMessage()));
                    return super.onError(index, batch, throwable, types);
                }
            })
            .with(AgentBuilder.Listener.Adapter.StreamWriting.toSystemError().withErrorsOnly())
            .with(AgentBuilder.InstallationListener.StreamWriting.toSystemOut())
            .ignore(t -> {
                if (t.isAbstract()) {
                    return true;
                }
                return basePackages == null || basePackages.stream()
                    .filter(pack -> Objects.requireNonNull(t.getPackage()).getName().startsWith(pack))
                    .findAny().isEmpty();
            })
            .type(ElementMatchers.isSubTypeOf(Validatable.class).and(ElementMatchers.not(ElementMatchers.isAbstract())))
            .transform((builder, typeDescription, classLoader, module, protectionDomain) -> {
                extendedValidate.add(typeDescription);
                return builder.visit(
                    Advice
                        .to(ValidationAdvice.class)
                        .on(ElementMatchers.isConstructor()
                            .or(
                                ElementMatchers.isMethod()
                                    .and(ElementMatchers.not(ElementMatchers.hasMethodName("validate").and(
                                        ElementMatchers.takesNoArguments())))
                                    .and(ElementMatchers.not(ElementMatchers.hasMethodName("concurrencyVersion").and(
                                        ElementMatchers.takesNoArguments())))
                                    .and(ElementMatchers.not(
                                        ElementMatchers.hasMethodName(domainBuilderConfiguration.builderMethodName())))
                                    .and(ElementMatchers.isPublic())
                                    .and(ElementMatchers.not(ElementMatchers.isStatic()))
                                    .and(ElementMatchers.not(ElementMatchers.isEquals()))
                                    .and(ElementMatchers.not(ElementMatchers.isToString()))
                                    .and(ElementMatchers.not(ElementMatchers.isHashCode()))
                                    .and(ElementMatchers.not(ElementMatchers.isAnnotatedWith(Query.class)))
                                    .and(ElementMatchers.not(ElementMatchers.isAnnotatedWith(ExcludeValidation.class)))
                                    .and(ElementMatchers.not(ElementMatchers.isGetter()))
                            )
                        )
                );
            })
            .type(ElementMatchers.isSubTypeOf(Validatable.class).and(ElementMatchers.not(ElementMatchers.isAbstract())))
            .transform((builder, typeDescription, classLoader, module, protectionDomain) -> {

                    extendedValidateParams.add(typeDescription);
                    return builder.visit(
                        Advice
                            .to(BeanValidationParameterAdvice.class)
                            .on(ElementMatchers.not(ElementMatchers.isConstructor())
                                .and(
                                    ElementMatchers.isMethod()
                                        .and(ElementMatchers.not(ElementMatchers.takesNoArguments()))
                                        .and(ElementMatchers.not(ElementMatchers.isStatic()))
                                        .and(ElementMatchers.not(ElementMatchers.hasMethodName("validate").and(
                                            ElementMatchers.takesNoArguments())))
                                        .and(ElementMatchers.not(ElementMatchers.hasMethodName("concurrencyVersion").and(
                                            ElementMatchers.takesNoArguments())))
                                        .and(ElementMatchers.not(
                                            ElementMatchers.hasMethodName(domainBuilderConfiguration.builderMethodName())))
                                        .and(ElementMatchers.not(ElementMatchers.isEquals()))
                                        .and(ElementMatchers.not(ElementMatchers.isToString()))
                                        .and(ElementMatchers.not(ElementMatchers.isHashCode()))
                                        .and(ElementMatchers.not(ElementMatchers.isAnnotatedWith(Query.class)))
                                        .and(ElementMatchers.not(ElementMatchers.isAnnotatedWith(ExcludeValidation.class)))
                                        .and(ElementMatchers.not(ElementMatchers.isGetter()))
                                )
                            )
                    );
                }
            )
            .type(ElementMatchers.isSubTypeOf(Validatable.class).and(ElementMatchers.not(ElementMatchers.isAbstract())))
            .transform((builder, typeDescription, classLoader, module, protectionDomain) -> {
                    extendedValidateReturn.add(typeDescription);
                    return builder.visit(
                        Advice
                            .to(BeanValidationReturnValueAdvice.class)
                            .on(ElementMatchers.not(ElementMatchers.isConstructor())
                                .and(
                                    ElementMatchers.isMethod()
                                        .and(ElementMatchers.not(
                                            ElementMatchers.returns(TypeDescription.ForLoadedType.of(void.class))))
                                        .and(ElementMatchers.not(ElementMatchers.hasMethodName("validate").and(
                                            ElementMatchers.takesNoArguments())))
                                        .and(ElementMatchers.not(ElementMatchers.hasMethodName("concurrencyVersion").and(
                                            ElementMatchers.takesNoArguments())))
                                        .and(ElementMatchers.not(
                                            ElementMatchers.hasMethodName(domainBuilderConfiguration.builderMethodName())))
                                        .and(ElementMatchers.not(ElementMatchers.isEquals()))
                                        .and(ElementMatchers.not(ElementMatchers.isToString()))
                                        .and(ElementMatchers.not(ElementMatchers.isHashCode()))
                                        .and(ElementMatchers.not(ElementMatchers.isAnnotatedWith(Query.class)))
                                        .and(ElementMatchers.not(ElementMatchers.isAnnotatedWith(ExcludeValidation.class)))
                                        .and(ElementMatchers.not(ElementMatchers.isGetter()))
                                )
                            )
                    );
                }
            );
        agentBuilder.installOn(inst);
    }

    /**
     * Returns the list of packages that are extended by the agent.
     *
     * @return the list of packages that are extended by the agent
     */
    public static List<String> getExtendedPackages() {
        return basePackages;
    }
}
