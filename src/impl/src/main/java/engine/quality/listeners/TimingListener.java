package engine.quality.listeners;

import engine.api.Job;
import engine.api.JobListener;

import java.util.Date;

/**
 * Listener for printing duration of job execution
 */
public class TimingListener implements JobListener {

    private final Job<?> job;

    private Date started;

    private Date finished;

    private long duration;

    public TimingListener(Job<?> job) {
        this.job = job;
    }

    public Date getStarted() {
        return started;
    }

    public Date getFinished() {
        return finished;
    }

    public long getDuration() {
        return duration;
    }

    @Override
    public void jobStarted(Job<?> job) {
        if (this.job == job) {
            started = new Date();
        }
    }

    @Override
    public void jobFinished(Job<?> job) {
        if (this.job == job) {
            finished = new Date();
            duration = (started == null || finished == null) ? 0L : finished.getTime() - started.getTime();
            System.out.println(String.format("%s finished in %d millis", job.getUuid(), duration));
        }
    }

    @Override
    public void jobFailed(Job<?> job, Throwable t) {
        jobFinished(job);
    }

}
