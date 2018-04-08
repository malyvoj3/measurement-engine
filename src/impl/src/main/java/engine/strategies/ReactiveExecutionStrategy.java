package engine.strategies;

import engine.api.ExecutionStrategy;
import engine.api.Job;
import engine.graph.ReactiveGraph;
import engine.graph.ReactiveNode;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Strategy using Reactor {@link Flux} for parallel traversal of graph
 */
public class ReactiveExecutionStrategy implements ExecutionStrategy {

    @Override
    public void execute(Job job) {
        ReactiveGraph graph = new ReactiveGraph(job);

        Flux.fromIterable(graph.getRoots())
                .flatMap(value ->
                                Mono.just(value)
                                        .subscribeOn(Schedulers.elastic())
                                        .doOnNext(ReactiveNode::execute),
                        64)
                .subscribe();
    }
}
