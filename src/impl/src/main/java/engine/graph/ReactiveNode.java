package engine.graph;

import engine.api.Job;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * Graph node for {@link ReactiveGraph} using reactive traversal
 */
public class ReactiveNode extends Node {

    private int finishedPredecessors;
    private final List<ReactiveNode> successors = new ArrayList<>();

    public ReactiveNode(Job job) {
        super(job);
    }

    public void execute() {
        synchronized (this) {
            if (++finishedPredecessors < inDegree || job.getResult() != null) {
                return;
            }
        }
        job.execute();
        Flux.fromIterable(successors)
                .flatMap(value ->
                                Mono.just(value)
                                        .subscribeOn(Schedulers.elastic())
                                        .doOnNext(ReactiveNode::execute),
                        64)
                .subscribe();
    }

    public int getFinishedPredecessors() {
        return finishedPredecessors;
    }

    public void setFinishedPredecessors(int finishedPredecessors) {
        this.finishedPredecessors = finishedPredecessors;
    }

    public void addSuccessor(ReactiveNode node) {
        successors.add(node);
    }

}
