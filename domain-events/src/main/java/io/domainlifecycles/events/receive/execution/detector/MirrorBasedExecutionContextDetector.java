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

package io.domainlifecycles.events.receive.execution.detector;

import io.domainlifecycles.domain.types.AggregateDomainEvent;
import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.services.api.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The MirrorBasedExecutionContextDetector class is an implementation of the ExecutionContextDetector interface.
 * It detects the execution contexts for a given DomainEvent using reflection and a mirror-based approach.
 *
 * @author Mario Herb
 */
public class MirrorBasedExecutionContextDetector implements ExecutionContextDetector {

    private static final Logger log = LoggerFactory.getLogger(MirrorBasedExecutionContextDetector.class);

    private final ServiceProvider serviceProvider;

    public MirrorBasedExecutionContextDetector(ServiceProvider serviceProvider) {
        this.serviceProvider = Objects.requireNonNull(serviceProvider,
            "A ServiceProvider is needed to detect DomainEvent execution contexts!");
    }

    /**
     * Detects the execution contexts for a given domain event.
     *
     * @param domainEvent The domain event to detect execution contexts for.
     * @return A list of execution contexts detected for the domain event.
     */
    @Override
    public List<ExecutionContext> detectExecutionContexts(DomainEvent domainEvent) {
        var detectedContexts = new ArrayList<ExecutionContext>();
        var dem = Domain.domainEventMirrorFor(domainEvent);
        if (domainEvent instanceof AggregateDomainEvent<?, ?>) {
            detectedContexts.addAll(
                dem.getListeningAggregates()
                    .stream()
                    .flatMap(arm -> detectAggregateRootExecutionContexts(arm, dem, domainEvent).stream())
                    .toList()
            );
        } else {
            detectedContexts.addAll(
                dem.getListeningServiceKinds()
                    .stream()
                    .flatMap(dsm -> detectServiceExecutionContexts(dsm, dem, domainEvent).stream())
                    .toList()
            );
        }
        log.debug("ExecutionContexts detected for {}", domainEvent);
        return detectedContexts;
    }

    private List<ServiceExecutionContext> detectServiceExecutionContexts(ServiceKindMirror skm,
                                                                         DomainEventMirror dem, DomainEvent de) {
        log.debug("Detecting ExecutionContext for DomainEvent {} on ServiceKind {}", de, skm.getTypeName());
        var ds = serviceProvider.getServiceKindInstance(skm.getTypeName());
        if (ds != null) {
            return skm.getMethods()
                .stream()
                .filter(m -> m.listensTo(dem))
                .map(m -> new ServiceExecutionContext(ds, m.getName(), de))
                .toList();
        } else {
            var msg = String.format("No ServiceKind instance found for %s", skm.getTypeName());
            log.error(msg);
            throw DLCEventsException.fail(msg);
        }
    }

    private List<AggregateExecutionContext<Identity<?>, AggregateRoot<Identity<?>>>> detectAggregateRootExecutionContexts(AggregateRootMirror arm, DomainEventMirror dem, DomainEvent de) {
        log.debug("Detecting ExecutionContext for DomainEvent {} on AggregateRoot {}", de, arm.getTypeName());
        var rm = Domain.repositoryMirrorFor(arm);
        var ade = (AggregateDomainEvent<Identity<?>, AggregateRoot<Identity<?>>>) de;
        Repository<Identity<?>,AggregateRoot<Identity<?>>> r = serviceProvider.getServiceKindInstance(rm.getTypeName());
        if (r != null) {
            return arm.getMethods()
                .stream()
                .filter(m -> m.listensTo(dem))
                .map(m -> new AggregateExecutionContext<>(r, m.getName(), ade))
                .toList();
        } else {
            var msg = String.format("No Repository instance found for %s", rm.getTypeName());
            log.error(msg);
            throw DLCEventsException.fail(msg);
        }

    }
}
