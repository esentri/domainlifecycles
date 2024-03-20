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

package nitrox.dlc.persistence.fetcher;

import nitrox.dlc.access.DlcAccess;
import nitrox.dlc.builder.DomainObjectBuilder;
import nitrox.dlc.domain.types.AggregateRoot;
import nitrox.dlc.domain.types.Entity;
import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.domain.types.internal.DomainObject;
import nitrox.dlc.mirror.api.AggregateRootReferenceMirror;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.api.EntityReferenceMirror;
import nitrox.dlc.mirror.api.FieldMirror;
import nitrox.dlc.mirror.api.ValueReferenceMirror;
import nitrox.dlc.mirror.visitor.ContextDomainTypeVisitor;
import nitrox.dlc.persistence.exception.DLCPersistenceException;
import nitrox.dlc.persistence.fetcher.simple.FetchedRecord;
import nitrox.dlc.persistence.mapping.RecordMapper;
import nitrox.dlc.persistence.mirror.api.EntityRecordMirror;
import nitrox.dlc.persistence.mirror.api.ValueObjectRecordMirror;
import nitrox.dlc.persistence.provider.DomainPersistenceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * InternalAggregateFetcher is the base class for all internal aggregate fetchers.
 *
 * @param <A>                the aggregate root entity type
 * @param <I>                the identity type
 * @param <BASE_RECORD_TYPE> the base record type
 *
 * @author Mario Herb
 */
public abstract class InternalAggregateFetcher<A extends AggregateRoot<I>, I extends Identity<?>, BASE_RECORD_TYPE> implements AggregateFetcher<A, I, BASE_RECORD_TYPE> {

    private final Class<A> aggregateRootEntityClass;

    private final Map<PropertyProviderKey, RecordProvider<? extends BASE_RECORD_TYPE, ? extends BASE_RECORD_TYPE>> recordProviderMap = new HashMap<>();

    private final DomainPersistenceProvider domainPersistenceProvider;


    /**
     * Creates a new InternalAggregateFetcher.
     */
    public InternalAggregateFetcher(Class<A> aggregateRootEntityClass,
                                    DomainPersistenceProvider domainPersistenceProvider
    ) {
        this.aggregateRootEntityClass = aggregateRootEntityClass;
        this.domainPersistenceProvider = domainPersistenceProvider;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AggregateFetcher<A, I, BASE_RECORD_TYPE> withRecordProvider(RecordProvider<? extends BASE_RECORD_TYPE, ? extends BASE_RECORD_TYPE> recordProvider,
                                                                       Class<? extends Entity<?>> containingEntityClass,
                                                                       Class<? extends DomainObject> propertyClass,
                                                                       List<String> propertyPath) {
        checkValidRecordProviderAssignment(containingEntityClass, propertyClass, propertyPath);
        recordProviderMap.put(new PropertyProviderKey(containingEntityClass.getName(), propertyClass.getName(), propertyPath), recordProvider);
        return this;
    }

    private void checkValidRecordProviderAssignment(Class<? extends Entity<?>> containingEntityClass,
                                                    Class<? extends DomainObject> propertyClass,
                                                    List<String> propertyPath) {
        if (propertyPath == null || propertyPath.size() == 0) {
            throw DLCPersistenceException.fail("A property path of a RecordProvider cannot be null or empty!");
        }

        var correctPropertyCoordinates = new AtomicBoolean(false);
        var visitor = new ContextDomainTypeVisitor(containingEntityClass.getName()){

            @Override
            public void visitAggregateRootReference(AggregateRootReferenceMirror aggregateRootReferenceMirror) {
                var context = getVisitorContext();
                if (context.getCurrentPath().stream().map(FieldMirror::getName).toList().equals(propertyPath)) {
                    if (aggregateRootReferenceMirror.getType().getTypeName().equals(propertyClass.getName())) {
                        correctPropertyCoordinates.set(true);
                    }
                }
            }

            @Override
            public void visitEntityReference(EntityReferenceMirror entityReferenceMirror) {
                var context = getVisitorContext();
                if (context.getCurrentPath().stream().map(FieldMirror::getName).toList().equals(propertyPath)) {
                    if (entityReferenceMirror.getType().getTypeName().equals(propertyClass.getName())) {
                        correctPropertyCoordinates.set(true);
                    }
                }
            }

            @Override
            public void visitValueReference(ValueReferenceMirror valueReferenceMirror) {
                var context = getVisitorContext();
                if (context.getCurrentPath().stream().map(FieldMirror::getName).toList().equals(propertyPath)) {
                    if (valueReferenceMirror.getType().getTypeName().equals(propertyClass.getName())) {
                        correctPropertyCoordinates.set(true);
                    }
                }
            }
        };
        visitor.start();

        if (!correctPropertyCoordinates.get()) {
            throw DLCPersistenceException.fail("%s does not contain a %s at the given path [%s]"
                    , containingEntityClass.getName()
                    , propertyClass.getName()
                    , String.join(".", propertyPath));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public FetcherResult<A, BASE_RECORD_TYPE> fetchDeep(BASE_RECORD_TYPE aggregateRecord) {

        final InternalFetcherContext<BASE_RECORD_TYPE> fetcherContext = new InternalFetcherContext<>();

        if (aggregateRecord == null) {
            return new FetcherResult<>(null, fetcherContext);
        }
        fetcherContext.recordFetched(aggregateRecord);
        A domainObjectDeepFetched = (A) fetchEntityReferencesAndValues(
                aggregateRecord,
                this.aggregateRootEntityClass.getName(),
                fetcherContext
        );

        //set potential back references
        //to prevent infinite loops, if there are any kind of back references
        fetcherContext.getBackReferences().forEach(
                br -> {
                    Optional<DomainObject> propertyDomainObject = fetcherContext.getDomainObjectFor(br.getReferencedRecord());
                    Optional<DomainObject> ownerDomainObject = fetcherContext.getDomainObjectFor(br.getOwnerRecord());
                    FieldMirror fm = br.getBackReferenceMirror();

                    if (propertyDomainObject.isPresent() && ownerDomainObject.isPresent()) {
                        var accessor = DlcAccess.accessorFor(ownerDomainObject.get());
                        if (fm.getType().hasCollectionContainer()) {
                            Collection<DomainObject> c = accessor.peek(fm.getName());
                            c.add(propertyDomainObject.get());
                        } else {
                            accessor.poke(fm.getName(), propertyDomainObject.get());
                        }
                    }
                }
        );

        return new FetcherResult<>(domainObjectDeepFetched, fetcherContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FetcherResult<A, BASE_RECORD_TYPE> fetchDeep(I id) {
        BASE_RECORD_TYPE record = null;
        if (id == null) {
            return fetchDeep(record);
        }
        record = getEntityRecordById(id);
        return fetchDeep(record);
    }

    protected DomainObject buildPropertyDomainObjectByPropertyRecord(BASE_RECORD_TYPE parentRecord,
                                                                     BASE_RECORD_TYPE propertyRecord,
                                                                     FieldMirror entityReference,
                                                                     InternalFetcherContext<BASE_RECORD_TYPE> fetcherContext
    ) {
        if (propertyRecord != null && fetcherContext.isFetched(propertyRecord)) {
            fetcherContext.addBackRef(entityReference, parentRecord, propertyRecord);
            return null;
        } else if (propertyRecord != null) {
            fetcherContext.recordFetched(propertyRecord);
        }
        return fetchEntityReferencesAndValues(
            propertyRecord,
            entityReference.getType().getTypeName(),
            fetcherContext
        );
    }

    @SuppressWarnings("unchecked")
    private DomainObject fetchEntityReferencesAndValues(BASE_RECORD_TYPE baseEntityRecord,
                                                        String baseEntityClassName,
                                                        InternalFetcherContext<BASE_RECORD_TYPE> fetcherContext) {
        if(baseEntityRecord==null){
            return null;
        }
        var rm = (RecordMapper<BASE_RECORD_TYPE, ?, ?>) domainPersistenceProvider.persistenceMirror.getEntityRecordMapper(baseEntityClassName);
        var b = rm.recordToDomainObjectBuilder(baseEntityRecord);
        if (b == null) {
            throw DLCPersistenceException.fail(String.format("Was not able to get DomainObjectBuilder for record: %s", baseEntityRecord));
        }

        var em = Domain.entityMirrorFor(b.instanceType().getName());
        for (EntityReferenceMirror entityReferenceMirror : em.getEntityReferences()) {
            fetchEntityReference(entityReferenceMirror, b, baseEntityRecord, fetcherContext);
        }
        for (AggregateRootReferenceMirror aggregateRootReferenceMirror : em.getAggregateRootReferences()) {
            fetchEntityReference(aggregateRootReferenceMirror, b, baseEntityRecord, fetcherContext);
        }
        var erm = (EntityRecordMirror<BASE_RECORD_TYPE>)domainPersistenceProvider
                .persistenceMirror
                .getEntityRecordMirror(b.instanceType().getName());
        fetchValueObjects(erm.valueObjectRecords(), b, baseEntityRecord, fetcherContext);

        DomainObject domainObjectBuilt = b.build();
        fetcherContext.assignRecordToDomainObject(domainObjectBuilt, baseEntityRecord);
        return domainObjectBuilt;
    }

    @SuppressWarnings("unchecked")
    private void fetchEntityReference(FieldMirror entityReferenceMirror,
                                      DomainObjectBuilder<? extends DomainObject> baseRecordBuilder,
                                      BASE_RECORD_TYPE baseEntityRecord,
                                      InternalFetcherContext<BASE_RECORD_TYPE> fetcherContext
    ) {
        var ppk = new PropertyProviderKey(entityReferenceMirror.getDeclaredByTypeName(), entityReferenceMirror.getType().getTypeName(), List.of(entityReferenceMirror.getName()));
        RecordProvider<? extends BASE_RECORD_TYPE, BASE_RECORD_TYPE> prp = (RecordProvider<? extends BASE_RECORD_TYPE, BASE_RECORD_TYPE>) recordProviderMap.get(ppk);
        var referencedEntityClassName = entityReferenceMirror.getType().getTypeName();
        DomainObject childDomainObject;
        if (prp != null) {
            //here we deal with an "injected" custom record provider
            //to be able to work with the results of custom queries
            if (entityReferenceMirror.getType().hasCollectionContainer()) {
                var c = prp.provideCollection(baseEntityRecord);
                for (BASE_RECORD_TYPE childRecord : c) {
                    childDomainObject = buildPropertyDomainObjectByPropertyRecord(
                            baseEntityRecord,
                            childRecord,
                            entityReferenceMirror,
                            fetcherContext
                    );
                    baseRecordBuilder.addValueToCollection(childDomainObject, entityReferenceMirror.getName());
                }
            } else {
                BASE_RECORD_TYPE propertyChildRecord = prp.provide(baseEntityRecord);
                childDomainObject = buildPropertyDomainObjectByPropertyRecord(
                        baseEntityRecord,
                        propertyChildRecord,
                        entityReferenceMirror,
                        fetcherContext);
                baseRecordBuilder.setFieldValue(childDomainObject, entityReferenceMirror.getName());
            }
        } else {
            //if there is no custom record provider we resolve association records in the way they are "naturally" provided
            // e.g. by foreign key associations
            if (entityReferenceMirror.getType().hasCollectionContainer()) {
                Collection<BASE_RECORD_TYPE> c = getEntityReferenceRecordCollectionByParentRecord(baseEntityRecord, referencedEntityClassName);
                for (BASE_RECORD_TYPE childRecord : c) {
                    childDomainObject = buildPropertyDomainObjectByPropertyRecord(
                            baseEntityRecord,
                            childRecord,
                            entityReferenceMirror,
                            fetcherContext
                    );
                    baseRecordBuilder.addValueToCollection(childDomainObject, entityReferenceMirror.getName());
                }
            } else {
                childDomainObject = fetchEntityForReference(
                        baseEntityRecord,
                        entityReferenceMirror,
                        fetcherContext);
                baseRecordBuilder.setFieldValue(childDomainObject, entityReferenceMirror.getName());
            }
        }
    }

    private void fetchValueObjects(List<? extends ValueObjectRecordMirror<BASE_RECORD_TYPE>> vorms,
                                   DomainObjectBuilder<?> baseRecordBuilder,
                                   BASE_RECORD_TYPE baseRecord,
                                   InternalFetcherContext<BASE_RECORD_TYPE> fetcherContext
    ) {
        //fetch record mapped value objects
        //phew, thats weird stuff
        if (vorms != null) {
            var vormsSorted = (List<? extends ValueObjectRecordMirror<BASE_RECORD_TYPE>>) vorms.stream().sorted(Comparator.comparingInt((ValueObjectRecordMirror o) -> o.pathSegments().size())).collect(Collectors.toList());

            final Map<FetchedRecord<BASE_RECORD_TYPE>, BuilderAndBuilt> builderMap = new HashMap<>();
            final List<ValueObjectRecordComposition> compositions = new ArrayList<>();
            final List<FetchedRecord<BASE_RECORD_TYPE>> currentContainers = new ArrayList<>();
            FetchedRecord<BASE_RECORD_TYPE> parent = FetchedRecord.of(baseRecord);

            builderMap.put(parent, new BuilderAndBuilt(baseRecordBuilder));

            Collection<BASE_RECORD_TYPE> children;
            for (ValueObjectRecordMirror<BASE_RECORD_TYPE> vorm : vormsSorted) {
                resetCurrentContainers(currentContainers, vorm, compositions, parent);
                for (FetchedRecord<? extends BASE_RECORD_TYPE> container : currentContainers) {
                    ValueObjectRecordComposition composition = new ValueObjectRecordComposition(vorm, container.record);
                    var ppk = new PropertyProviderKey(vorm.containingEntityTypeName(), vorm.domainObjectTypeName(), vorm.pathSegments());
                    RecordProvider<? extends BASE_RECORD_TYPE, BASE_RECORD_TYPE> prp = (RecordProvider<? extends BASE_RECORD_TYPE, BASE_RECORD_TYPE>) recordProviderMap.get(ppk);
                    if (prp != null) {
                        if (toManyReferenceOnPath(vorm)) {
                            children = (Collection<BASE_RECORD_TYPE>) prp.provideCollection(container.record);
                        } else {
                            children = new ArrayList<>();
                            children.add(prp.provide(container.record));
                        }
                    } else {
                        children = getChildValueObjectRecordCollectionByParentRecord(container.record, vorm);
                    }
                    for (BASE_RECORD_TYPE childRecord : children) {
                        composition.addChildRecord(childRecord);
                        builderMap.put(FetchedRecord.of(childRecord),
                                new BuilderAndBuilt(
                                    vorm.recordMapper().recordToDomainObjectBuilder(childRecord)
                                )
                        );
                    }
                    //the compositions are now ordered from leafs to root
                    //if we take a look at the value object tree
                    compositions.add(composition);
                }
            }

            //concrete instances must be built in reverse order of the compositions
            Collections.reverse(compositions);
            for (ValueObjectRecordComposition comp : compositions) {
                for (BASE_RECORD_TYPE child : comp.childRecords) {
                    BuilderAndBuilt childBuilderAndBuilt = builderMap.get(FetchedRecord.of(child));
                    childBuilderAndBuilt.build();
                    //its important to provide all fetched records to the fetcher context
                    fetcherContext.assignRecordToDomainObject(childBuilderAndBuilt.getBuilt(), child);
                    DomainObject childInstance = childBuilderAndBuilt.getBuilt();
                    BuilderAndBuilt parentBuilderAndBuilt = builderMap.get(FetchedRecord.of(comp.parentRecord));
                    String fieldName = comp.valueObjectRecordMirror.pathSegments().get(comp.valueObjectRecordMirror.pathSegments().size() - 1);
                    var parentTypeName = parentBuilderAndBuilt.getBuilder().instanceType().getName();
                    var dtm = Domain.typeMirror(parentTypeName)
                            .orElseThrow(() -> DLCPersistenceException.fail("DomainTypeMirror not found for '%s'", parentTypeName));
                    var fm = dtm.fieldByName(fieldName);
                    if(fm.getType().hasCollectionContainer()){
                        parentBuilderAndBuilt.getBuilder().addValueToCollection(childInstance, fieldName);
                    }else{
                        parentBuilderAndBuilt.getBuilder().setFieldValue(childInstance, fieldName);
                    }

                }
            }

        }
    }

    private boolean toManyReferenceOnPath(ValueObjectRecordMirror<BASE_RECORD_TYPE> vorm) {
        //use visitor
        var em = Domain.entityMirrorFor(vorm.containingEntityTypeName());
        var vom = em.valueReferenceByName(vorm.pathSegments().get(0));

        if (vom.getType().hasCollectionContainer()) {
            return true;
        }
        if (vorm.pathSegments().size() > 1) {
            var currentVm = Domain.valueObjectMirrorFor(vom.getType().getTypeName());
            var currentVom = vom;
            for (int i = 1; i < vorm.pathSegments().size(); i++) {
                var pathSegment = vorm.pathSegments().get(i);

                currentVom = currentVm.valueReferenceByName(pathSegment);

                if (currentVom.getType().hasCollectionContainer()) {
                    return true;
                }
                var vomTargetClassName = currentVom.getType().getTypeName();
                currentVm = Domain.valueObjectMirrorFor(vomTargetClassName);
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private void resetCurrentContainers(List<FetchedRecord<BASE_RECORD_TYPE>> currentContainers,
                                        ValueObjectRecordMirror<BASE_RECORD_TYPE> vorm,
                                        List<ValueObjectRecordComposition> compositions,
                                        FetchedRecord<BASE_RECORD_TYPE> parent) {
        currentContainers.clear();
        if (compositions.size() > 0) {
            List<ValueObjectRecordComposition> compositionsReversed = new ArrayList<>(compositions);
            Collections.reverse(compositionsReversed);
            ValueObjectRecordComposition predecessorComp = null;
            //this is very complex:
            //we search for the predecessor valueobject record composition
            //they are ordered descending by their path length
            //the first hit is the direct predecessor
            //the child record of the predecessor composition are the parents for the following one
            //aka the current containers
            for (ValueObjectRecordComposition comp : compositionsReversed) {
                //the direct predecessor is the first hit with the same path prefix
                //and a shorter path length
                String compPath = comp.valueObjectRecordMirror.completePath();
                String vormPath = vorm.completePath();
                if (vorm.pathSegments().size() > (comp.valueObjectRecordMirror.pathSegments().size())
                        && vormPath.startsWith(compPath)) {
                    predecessorComp = comp;
                    break;
                }
            }
            if (predecessorComp != null) {
                for (BASE_RECORD_TYPE child : predecessorComp.childRecords) {
                    currentContainers.add(FetchedRecord.of(child));
                }
            } else {
                currentContainers.add(parent);
            }

        } else {
            currentContainers.add(parent);
        }
    }

    protected abstract BASE_RECORD_TYPE getEntityRecordById(I id);

    protected abstract BASE_RECORD_TYPE getEntityReferenceRecordByParentRecord(BASE_RECORD_TYPE parentRecord, String referencedEntityClassName);

    protected abstract Collection<BASE_RECORD_TYPE> getChildValueObjectRecordCollectionByParentRecord(BASE_RECORD_TYPE parentRecord, ValueObjectRecordMirror<BASE_RECORD_TYPE> vorm);

    protected abstract Collection<BASE_RECORD_TYPE> getEntityReferenceRecordCollectionByParentRecord(BASE_RECORD_TYPE parentRecord, String referencedEntityClassName);

    protected DomainObject fetchEntityForReference(BASE_RECORD_TYPE parentRecord,
                                                   FieldMirror entityReferenceMirror,
                                                   InternalFetcherContext<BASE_RECORD_TYPE> fetcherContext
    ) {
        BASE_RECORD_TYPE record = getEntityReferenceRecordByParentRecord(parentRecord, entityReferenceMirror.getType().getTypeName());
        return buildPropertyDomainObjectByPropertyRecord(
                parentRecord,
                record,
                entityReferenceMirror,
                fetcherContext
        );
    }

    private record PropertyProviderKey(String containingEntityClassName, String propertyClassName,
                                       List<String> propertyPath) {
            private PropertyProviderKey(final String containingEntityClassName,
                                        final String propertyClassName,
                                        final List<String> propertyPath) {
                this.containingEntityClassName = Objects.requireNonNull(containingEntityClassName);
                this.propertyClassName = Objects.requireNonNull(propertyClassName);
                this.propertyPath = Objects.requireNonNull(propertyPath);
            }
    }

    private static class BuilderAndBuilt {
        private final DomainObjectBuilder<?> builder;
        private DomainObject built;

        public BuilderAndBuilt(DomainObjectBuilder<?> builder) {
            this.builder = builder;
        }

        public void build() {
            this.built = this.builder.build();
        }

        public DomainObjectBuilder<?> getBuilder() {
            return builder;
        }

        public DomainObject getBuilt() {
            return built;
        }
    }

    private class ValueObjectRecordComposition {
        private final ValueObjectRecordMirror<BASE_RECORD_TYPE> valueObjectRecordMirror;
        private final BASE_RECORD_TYPE parentRecord;
        private final List<BASE_RECORD_TYPE> childRecords = new ArrayList<>();

        public ValueObjectRecordComposition(ValueObjectRecordMirror<BASE_RECORD_TYPE> valueObjectRecordMirror, BASE_RECORD_TYPE parentRecord) {
            this.valueObjectRecordMirror = valueObjectRecordMirror;
            this.parentRecord = parentRecord;
        }

        public void addChildRecord(BASE_RECORD_TYPE childRecord) {
            this.childRecords.add(childRecord);
        }

        public ValueObjectRecordMirror<BASE_RECORD_TYPE> getValueObjectRecordMirror() {
            return valueObjectRecordMirror;
        }

        public BASE_RECORD_TYPE getParentRecord() {
            return parentRecord;
        }

        public List<BASE_RECORD_TYPE> getChildRecords() {
            return childRecords;
        }
    }


}
