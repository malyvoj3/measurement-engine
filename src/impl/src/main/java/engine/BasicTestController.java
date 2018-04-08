package engine;

import engine.test.GraphGenerator;
import engine.test.TestAggregateJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Preparation for Spring 5.0 with Webflux
 * RestController
 */
@RestController
public class BasicTestController {

    private final JobQueue jobQueue;

    @Autowired
    public BasicTestController(JobQueue jobQueue) {
        Assert.notNull(jobQueue, "Job Queue must not be null");
        this.jobQueue = jobQueue;
    }

    /**
     * Generate test graph and push it to the queue
     * @param number Large of graph
     * @return
     */
    @GetMapping("/{number}")
    public Mono<TestAggregateJob> runTest(@PathVariable("number") int number) {
        TestAggregateJob testAggregateJob = GraphGenerator.generateJob(number);
        jobQueue.push(testAggregateJob);
        return Mono.just(testAggregateJob);
    }

    /**
     * Generate test graph and return it
     * @return
     */
    @GetMapping("/graph")
    public Mono<TestAggregateJob> getJob() {
        return Mono.just(GraphGenerator.generateJob(1));
    }

}
