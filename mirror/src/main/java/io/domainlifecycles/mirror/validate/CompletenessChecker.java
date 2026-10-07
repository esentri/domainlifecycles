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
 *  Copyright 2019-2025 the original author or authors.
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

package io.domainlifecycles.mirror.validate;

import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ParamMirror;
import io.domainlifecycles.mirror.exception.MirrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is responsible for verifying the completeness of a given DomainModel.
 * It checks whether all domain types and their references (fields, methods, parameters, etc.)
 * within the DomainModel are correctly modeled and do not reference unknown or invalid domain types.
 *
 * If any incompleteness is found during the checks, a {@link MirrorException} is thrown with an appropriate message.
 * Unknown domain types referenced by a {@link DomainType#NON_DOMAIN} class are only logged as a warning: such a
 * class is no part of the domain model itself, so it must not make the model incomplete.
 */
public class CompletenessChecker {

    private static final Logger log = LoggerFactory.getLogger(CompletenessChecker.class);

    private final List<String> messages = new ArrayList<>();
    private final List<String> warnings = new ArrayList<>();
    private final DomainMirror domainMirror;
    private final List<String> ignoredPackages;

    /**
     * Constructs a new CompletenessChecker instance.
     *
     * The CompletenessChecker is responsible for verifying the completeness
     * of a provided DomainModel instance. All required fields and methods
     * within the DomainModel will be checked if defined domain types are
     * consistently declared within the model.
     *
     * @param domainMirror the DomainModel to be checked. Must not be null,
     *                    otherwise a NullPointerException will be thrown.
     */
    public CompletenessChecker(DomainMirror domainMirror) {
        this.domainMirror = Objects.requireNonNull(
            domainMirror,
            "A DomainMirror instance must be provided to be checked!"
        );
        this.ignoredPackages = new ArrayList<>();
        this.ignoredPackages.add("io.domainlifecycles");
        this.ignoredPackages.add("java");
    }

    /**
     * Checks the completeness of the domain model.
     *
     * This method iterates over all type mirrors in the domain model to
     * ensure consistency and completeness. Each type mirror undergoes a
     * verification process through the checkMirror method. If the domain
     * model is found to be incomplete, a {@link MirrorException} is thrown
     * with a detailed message indicating the encountered issues.
     *
     * @throws MirrorException if the domain model is incomplete, detailing the
     *                         specific errors encountered during the check.
     */
    public void checkForCompleteness() {
        domainMirror.getAllDomainTypeMirrors()
            .stream()
            .filter(t ->
                ignoredPackages.stream().noneMatch(p -> t.getTypeName().startsWith(p))
            )
            .forEach(this::checkMirror);
        if(!warnings.isEmpty()){
            log.warn("Non-domain classes reference unknown domain types: \n{}", String.join(",\n", warnings));
        }
        if(!messages.isEmpty()){
            throw MirrorException.fail("Domain Model is not complete: \n%s", String.join(",\n", messages));
        }
    }

    /**
     * Adds a package name to the list of ignored packages for completeness checks.
     *
     * This method allows specific packages to be excluded from the completeness
     * verification process. Ignored packages are not evaluated for completeness,
     * which can be useful to bypass third-party libraries or irrelevant domain
     * components.
     *
     * @param ignoredPackage the fully qualified name of the package to be ignored.
     *                       Must not be null or empty, otherwise the behavior is undefined.
     */
    public void addIgnoredPackage(String ignoredPackage){
        ignoredPackages.add(ignoredPackage);
    }

    private void checkMirror(DomainTypeMirror mirror) {
        var findings = DomainType.NON_DOMAIN.equals(mirror.getDomainType()) ? warnings : messages;
        mirror.getAllFields().forEach(field -> checkField(field, findings));
        mirror.getMethods().forEach(method -> checkMethod(method, findings));
    }

    private void checkField(FieldMirror field, List<String> findings) {
        if( ignoredPackages.stream().noneMatch(p -> field.getDeclaredByTypeName().startsWith(p)) ){
            check(
                field.getType(),
                String.format(
                    "The model class '%s' references an unknown domain type '%s' by the field declaration '%s'",
                    field.getDeclaredByTypeName(),
                    field.getType().getTypeName(),
                    field.getName()
                ),
                findings
            );
        }
    }

    private void checkMethod(MethodMirror method, List<String> findings) {
        if( ignoredPackages.stream().noneMatch(p -> method.getDeclaredByTypeName().startsWith(p)) ){
            checkReturnType(method, findings);
            method.getParameters().forEach(param -> checkMethodParam(method, param, findings));
        }
    }

    private void checkReturnType(MethodMirror method, List<String> findings) {
        check(
            method.getReturnType(),
            String.format(
                "The model class '%s' references an unknown domain type '%s' by the method return type declaration of method '%s'",
                method.getDeclaredByTypeName(),
                method.getReturnType().getTypeName(),
                method.getName()
            ),
            findings
        );
    }

    private void checkMethodParam(MethodMirror method, ParamMirror param, List<String> findings) {
        check(
            param.getType(),
            String.format(
                "The model class '%s' references an unknown domain type '%s' by the method param declaration of method '%s'",
                method.getDeclaredByTypeName(),
                param.getType().getTypeName(),
                method.getName()
            ),
            findings
        );
    }

    private void check(AssertedContainableTypeMirror typeToCheck, String message, List<String> findings){
        if(DomainType.NON_DOMAIN.equals(typeToCheck.getDomainType())
            || DomainType.ENUM.equals(typeToCheck.getDomainType())){
            return;
        }
        if (domainMirror.getDomainTypeMirror(typeToCheck.getTypeName()).isEmpty()
            && ignoredPackages.stream().noneMatch(p -> typeToCheck.getTypeName().startsWith(p))
        ) {
            findings.add(
                message
            );
        }
    }

}
