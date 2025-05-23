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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is responsible for verifying the completeness of a given DomainModel.
 * It checks whether all domain types and their references (fields, methods, parameters, etc.)
 * within the DomainModel are correctly modeled and do not reference unknown or invalid domain types.
 *
 * If any incompleteness is found during the checks, a {@link MirrorException} is thrown with an appropriate message.
 */
public class CompletenessChecker {

    private final List<String> messages = new ArrayList<>();
    private final DomainMirror domainMirror;

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
            .filter(t -> !t.getTypeName().startsWith("io.domainlifecycles."))
            .forEach(this::checkMirror);
        if(!messages.isEmpty()){
            throw MirrorException.fail("Domain Model is not complete: \n%s", String.join(",\n", messages));
        }
    }

    private void checkMirror(DomainTypeMirror mirror) {
        mirror.getAllFields().forEach(this::checkField);
        mirror.getMethods().forEach(this::checkMethod);
    }

    private void checkField(FieldMirror field) {
        if(!field.getDeclaredByTypeName().startsWith("io.domainlifecycles.")) {
            check(
                field.getType(),
                String.format(
                    "The model class '%s' references an unknown domain type '%s' by the field declaration '%s'",
                    field.getDeclaredByTypeName(),
                    field.getType().getTypeName(),
                    field.getName()
                )
            );
        }
    }

    private void checkMethod(MethodMirror method) {
        if(!method.getDeclaredByTypeName().startsWith("io.domainlifecycles.")) {
            checkReturnType(method);
            method.getParameters().forEach(param -> checkMethodParam(method, param));
        }
    }

    private void checkReturnType(MethodMirror method) {
        check(
            method.getReturnType(),
            String.format(
                "The model class '%s' references an unknown domain type '%s' by the method return type declaration of method '%s'",
                method.getDeclaredByTypeName(),
                method.getReturnType().getTypeName(),
                method.getName()
            )
        );
    }

    private void checkMethodParam(MethodMirror method, ParamMirror param) {
        check(
            param.getType(),
            String.format(
                "The model class '%s' references an unknown domain type '%s' by the method param declaration of method '%s'",
                method.getDeclaredByTypeName(),
                param.getType().getTypeName(),
                method.getName()
            )
        );
    }

    private void check(AssertedContainableTypeMirror typeToCheck, String message){
        if(DomainType.NON_DOMAIN.equals(typeToCheck.getDomainType())
            || DomainType.ENUM.equals(typeToCheck.getDomainType())){
            return;
        }
        if (domainMirror.getDomainTypeMirror(typeToCheck.getTypeName()).isEmpty()
            && !typeToCheck.getTypeName().startsWith("io.domainlifecycles.")
            && !typeToCheck.getTypeName().startsWith("java")
        ) {
            messages.add(
                message
            );
        }
    }

}
