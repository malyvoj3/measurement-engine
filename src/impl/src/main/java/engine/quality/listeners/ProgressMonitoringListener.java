package engine.quality.listeners;

import engine.api.Job;
import engine.api.JobListener;

/**
 * Listener printing actual progress of job execution
 */
public class ProgressMonitoringListener implements JobListener {

    private int started = 0;

    private int finished = 0;

    private int failed = 0;

    @Override
    public synchronized void jobStarted(Job<?> job) {
        started++;
        progress();
    }

    @Override
    public synchronized void jobFinished(Job<?> job) {
        finished++;
        progress();
    }

    @Override
    public synchronized void jobFailed(Job<?> job, Throwable t) {
        failed++;
        progress();
    }

    private void progress() {
        System.out.println(String.format("%d started / %d finished / %d failed", started, finished, failed));
    }
}
