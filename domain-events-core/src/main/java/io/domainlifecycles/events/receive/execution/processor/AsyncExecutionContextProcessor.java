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

package io.domainlifecycles.events.receive.execution.processor;

import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.receive.execution.detector.ExecutionContext;
import io.domainlifecycles.events.receive.execution.handler.HandlerExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * AsyncExecutionContextProcessor is a subclass of SimpleExecutionContextProcessor that processes
 * execution contexts asynchronously using a fixed thread pool. It executes a HandlerExecutor on
 * each context in separate threads and returns the results.
 *
 * @author Mario Herb
 */
public class AsyncExecutionContextProcessor extends SimpleExecutionContextProcessor {
    private static final Logger log = LoggerFactory.getLogger(AsyncExecutionContextProcessor.class);
    private int threadPoolSize = 10;
    private final ExecutorService executorService;

    public AsyncExecutionContextProcessor(HandlerExecutor handlerExecutor) {
        super(handlerExecutor);
        executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    public AsyncExecutionContextProcessor(HandlerExecutor handlerExecutor, int threadPoolSize) {
        super(handlerExecutor);
        this.threadPoolSize = threadPoolSize;
        executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    /**
     * Processes a list of execution contexts by executing a HandlerExecutor on each context asynchronously using a fixed thread pool.
     *
     * @param contextList The list of execution contexts to be processed.
     * @return A list of ExecutionResult objects, representing the results of the processing.
     * @throws DLCEventsException if invoking the handler executor asynchronously fails or getting the results fails.
     */
    @Override
    public List<ExecutionResult> process(List<ExecutionContext> contextList) {
        log.debug("Going to process {}", contextList.stream().map(c-> c.toString()).collect(Collectors.joining(",")));
        List<Future<ExecutionResult>> futures;
        try {
            futures = executorService.invokeAll(contextList.stream().map(c -> new Callable<ExecutionResult>(){
                    @Override
                    public ExecutionResult call() throws Exception {
                        return new ExecutionResult(c, handlerExecutor.execute(c));
                    }
                }).toList());

        } catch (InterruptedException e ) {
            throw DLCEventsException.fail("Invoking the handler executor asynchronously failed!", e);
        }
        var results = new ArrayList<ExecutionResult>();
        for(var f : futures){
            ExecutionResult result = null;
            try {
                result = f.get();
            } catch (InterruptedException | ExecutionException e) {
                throw DLCEventsException.fail("Getting result of the handler executor asynchronously failed!", e);
            }
            results.add(result);
        }
        log.debug("Returning to process results {}", results.stream().map(c-> c.toString()).collect(Collectors.joining(",")));
        return results;

    }

    /**
     * Returns the size of the thread pool used by the AsyncExecutionContextProcessor.
     *
     * @return The size of the thread pool.
     */
    public int getThreadPoolSize() {
        return threadPoolSize;
    }
}
