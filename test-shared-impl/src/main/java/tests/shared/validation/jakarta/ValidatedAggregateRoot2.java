/*
 *
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

package tests.shared.validation.jakarta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.AggregateRootBase;

import java.util.Optional;

@Getter
public class ValidatedAggregateRoot2 extends AggregateRootBase<ValidatedAggregateRoot2Id> {

    private final ValidatedAggregateRoot2Id id;
    @NotEmpty
    @Size(max = 100)
    private String text;

    private Optional<@Size(max = 10) String> optionalText;


    @Builder(setterPrefix = "set")
    public ValidatedAggregateRoot2(ValidatedAggregateRoot2Id id, String text, String optionalText,
                                   long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
        this.text = text;
        this.optionalText = Optional.ofNullable(optionalText);
    }

    public void komischeBerechnungMitValidationError() {
        text = "WRONG";
    }

    public ValidatedAggregateRoot2 textSetzenMitReturn(String neuerText) {
        this.text = neuerText;
        return this;
    }

    public ValidatedAggregateRoot2 optionalTextSetzenMitReturn(Optional<@NotBlank String> neuerTextOptional) {
        this.optionalText = neuerTextOptional;
        return this;
    }

    public void textSetzenPrecondition(@NotBlank String neuerText) {
        this.text = neuerText;
    }

    public @NotBlank String textSetzenReturnVal(String neuerText) {
        this.text = neuerText;
        return this.text;
    }

    @Override
    public void validate() {
        super.validate();
        DomainAssertions.isTrue(!"WRONG".equals(text), "text darf niemals 'WRONG' sein!");
    }

    public ValidatedAggregateRoot2Id getId() {
        return this.id;
    }

    public @NotEmpty @Size(max = 100) String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Optional<@Size(max = 10) String> getOptionalText() {
        return this.optionalText;
    }

    public void setOptionalText(String text) {
        this.optionalText = Optional.ofNullable(text);
    }

}
