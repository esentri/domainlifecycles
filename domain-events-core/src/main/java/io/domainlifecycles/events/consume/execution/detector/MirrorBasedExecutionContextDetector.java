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

package io.domainlifecycles.events.consume.execution.detector;

import io.domainlifecycles.domain.types.AggregateDomainEvent;
import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryClientMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
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
public final class MirrorBasedExecutionContextDetector implements ExecutionContextDetector {

    private static final Logger log = LoggerFactory.getLogger(MirrorBasedExecutionContextDetector.class);

    private final ServiceProvider serviceProvider;

    /**
     * Initializes a new instance of MirrorBasedExecutionContextDetector with the provided ServiceProvider.
     *
     * @param serviceProvider The ServiceProvider instance to be used for detecting DomainEvent execution contexts.
     */
    public MirrorBasedExecutionContextDetector(ServiceProvider serviceProvider) {
        this.serviceProvider = Objects.requireNonNull(serviceProvider, "A ServiceProvider is needed to detect DomainEvent execution contexts!");
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
        if(domainEvent instanceof AggregateDomainEvent<?,?>) {
            detectedContexts.addAll(
                dem.getListeningAggregates()
                    .stream()
                    .flatMap(arm -> detectAggregateRootExecutionContexts(arm, dem, domainEvent).stream())
                    .toList()
            );
        }else{
            detectedContexts.addAll(
                dem.getListeningDomainServices()
                    .stream()
                    .flatMap(dsm -> detectDomainServiceExecutionContexts(dsm, dem, domainEvent).stream())
                    .toList()
            );

            detectedContexts.addAll(
                dem.getListeningRepositories()
                    .stream()
                    .flatMap(rm -> detectRepositoryExecutionContexts(rm, dem, domainEvent).stream())
                    .toList()
            );

            detectedContexts.addAll(
                dem.getListeningApplicationServices()
                    .stream()
                    .flatMap(am -> detectApplicationServiceExecutionContexts(am, dem, domainEvent).stream())
                    .toList()
            );

            detectedContexts.addAll(
                dem.getListeningOutboundServices()
                    .stream()
                    .flatMap(os -> detectOutboundServiceExecutionContexts(os, dem, domainEvent).stream())
                    .toList()
            );

            detectedContexts.addAll(
                dem.getListeningQueryClients()
                    .stream()
                    .flatMap(rmp -> detectQueryClientExecutionContexts(rmp, dem, domainEvent).stream())
                    .toList()
            );
        }
        log.debug("ExecutionContexts detected for {}", domainEvent);
        return detectedContexts;
    }

    private List<ServiceExecutionContext> detectDomainServiceExecutionContexts(DomainServiceMirror dsm, DomainEventMirror dem, DomainEvent de){
        log.debug("Detecting ExecutionContext for DomainEvent {} on DomainService {}", de, dsm.getTypeName());
        var ds = serviceProvider.getDomainServiceInstance(dsm.getTypeName());
        if(ds != null) {
            return dsm.getMethods()
                .stream()
                .filter(m -> m.listensTo(dem))
                .map(m -> new ServiceExecutionContext(ds, m.getName(), de))
                .toList();
        }else{
            var msg = String.format("No DomainService instance found for %s", dsm.getTypeName());
            log.error(msg);
            throw DLCEventsException.fail(msg);
        }
    }

    private List<ServiceExecutionContext> detectRepositoryExecutionContexts(RepositoryMirror rm, DomainEventMirror dem, DomainEvent de){
        log.debug("Detecting ExecutionContext for DomainEvent {} on Repository {}", de, rm.getTypeName());
        var r = serviceProvider.getRepositoryInstance(rm.getTypeName());
        if(r != null) {
            return rm.getMethods()
                .stream()
                .filter(m -> m.listensTo(dem))
                .map(m -> new ServiceExecutionContext(r, m.getName(), de))
                .toList();
        }else{
            var msg = String.format("No Repository instance found for %s", rm.getTypeName());
            log.error(msg);
            throw DLCEventsException.fail(msg);
        }
    }

    private List<ServiceExecutionContext> detectApplicationServiceExecutionContexts(ApplicationServiceMirror am, DomainEventMirror dem, DomainEvent de){
        log.debug("Detecting ExecutionContext for DomainEvent {} on ApplicationService {}", de, am.getTypeName());
        var as = serviceProvider.getApplicationServiceInstance(am.getTypeName());
        if(as != null) {
            return am.getMethods()
                .stream()
                .filter(m -> m.listensTo(dem))
                .map(m -> new ServiceExecutionContext(as, m.getName(), de))
                .toList();
        }else{
            var msg = String.format("No ApplicationService instance found for %s", am.getTypeName());
            log.error(msg);
            throw DLCEventsException.fail(msg);
        }

    }

    private List<ServiceExecutionContext> detectQueryClientExecutionContexts(QueryClientMirror qcm, DomainEventMirror dem, DomainEvent de){
        log.debug("Detecting ExecutionContext for DomainEvent {} on QueryClient {}", de, qcm.getTypeName());
        var rmp = serviceProvider.getQueryClientInstance(qcm.getTypeName());
        if(rmp != null) {
            return qcm.getMethods()
                .stream()
                .filter(m -> m.listensTo(dem))
                .map(m -> new ServiceExecutionContext(rmp, m.getName(), de))
                .toList();
        }else{
            var msg = String.format("No QueryClient instance found for %s", qcm.getTypeName());
            log.error(msg);
            throw DLCEventsException.fail(msg);
        }
    }

    private List<ServiceExecutionContext> detectOutboundServiceExecutionContexts(OutboundServiceMirror om, DomainEventMirror dem, DomainEvent de){
        log.debug("Detecting ExecutionContext for DomainEvent {} on OutboundService {}", de, om.getTypeName());
        var os = serviceProvider.getOutboundServiceInstance(om.getTypeName());
        if(os != null) {
            return om.getMethods()
                .stream()
                .filter(m -> m.listensTo(dem))
                .map(m -> new ServiceExecutionContext(os, m.getName(), de))
                .toList();
        }else{
            var msg = String.format("No OutboundService instance found for %s", om.getTypeName());
            log.error(msg);
            throw DLCEventsException.fail(msg);
        }
    }

    private List<AggregateExecutionContext<Identity<?>, AggregateRoot<Identity<?>>>> detectAggregateRootExecutionContexts(AggregateRootMirror arm, DomainEventMirror dem, DomainEvent de){
        log.debug("Detecting ExecutionContext for DomainEvent {} on AggregateRoot {}", de, arm.getTypeName());
        var rm = Domain.repositoryMirrorFor(arm);
        var ade = (AggregateDomainEvent<Identity<?>, AggregateRoot<Identity<?>>>) de;
        var r = serviceProvider.getRepositoryInstance(rm.getTypeName());
        if(r != null){
            return arm.getMethods()
                .stream()
                .filter(m -> m.listensTo(dem))
                .map(m -> new AggregateExecutionContext<>(r, m.getName(), ade))
                .toList();
        }else{
            var msg = String.format("No Repository instance found for %s", rm.getTypeName());
            log.error(msg);
            throw DLCEventsException.fail(msg);
        }

    }
}
