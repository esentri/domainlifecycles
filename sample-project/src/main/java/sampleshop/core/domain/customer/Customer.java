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

package sampleshop.core.domain.customer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ListensTo;
import io.domainlifecycles.domain.types.Publishes;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import io.domainlifecycles.events.api.DomainEvents;

import java.util.Optional;

/**
 * Customer aggregate root.
 *
 * @author Mario Herb
 * @author Tobias Herb
 */
@Getter
public final class Customer extends AggregateRootBase<Customer.CustomerId> {

    /**
     * The "typed" CustomerId as inner class (record) of the {@code Customer} class.
     */
    public record CustomerId(@NotNull Long value) implements Identity<Long> { }
    private final CustomerId id;
    @NotEmpty @Size(max = 100)
    private String userName;
    @NotNull
    private Address address;
    private Optional<CreditCard> creditCard;
    private boolean blocked;

    @Builder(setterPrefix = "set")
    public Customer(final CustomerId id,
                    final long concurrencyVersion,
                    final String userName,
                    final Address address,
                    final CreditCard creditCard,
                    final boolean blocked) {
        super(concurrencyVersion);
        this.id = id;
        this.userName = userName;
        this.address = address;
        this.creditCard = Optional.ofNullable(creditCard);
        this.blocked = blocked;
    }

    @ListensTo(domainEventType = FraudDetected.class)
    public void onFraudDetected(FraudDetected fraudDetected){
        block();
    }

    public Customer block(){
        this.blocked = true;
        return this;
    }

    @Publishes(domainEventTypes = CustomerCreditCardChanged.class)
    public Customer setCreditCard(CreditCard creditCard){
        this.creditCard = Optional.ofNullable(creditCard);
        DomainEvents.publish(new CustomerCreditCardChanged(
            id,
            this.creditCard
        ));
        return this;
    }

    @Publishes(domainEventTypes = CustomerAdressChanged.class)
    public Customer setAddress(Address address){
        this.address = address;
        DomainEvents.publish(new CustomerAdressChanged(
            id,
            address
        ));
        return this;
    }

    public Customer setUsername(String username){
        this.userName = username;
        return this;
    }


}
