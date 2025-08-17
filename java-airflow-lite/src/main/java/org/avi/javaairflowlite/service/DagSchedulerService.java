package org.avi.javaairflowlite.service;

import org.avi.javaairflowlite.dag.DAG;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Service
public class DagSchedulerService {
    private final Scheduler scheduler;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public DagSchedulerService() throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
    }

    public void scheduleDag(DAG dag) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(DagRunnerJob.class)
                .withIdentity(dag.getId())
                .usingJobData("dagId", dag.getId())
                .build();

        // pass DAG instance via SchedulerContext
        scheduler.getContext().put(dag.getId(), dag);

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(dag.getId() + "-trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(dag.getSchedule()))
                .startNow()
                .build();

        scheduler.scheduleJob(job, trigger);
    }

    public void shutdown() throws SchedulerException {
        scheduler.shutdown();
        executor.shutdown();
    }

    public static class DagRunnerJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            try {
                SchedulerContext ctx = context.getScheduler().getContext();
                String dagId = context.getJobDetail().getKey().getName();
                DAG dag = (DAG) ctx.get(dagId);
                if (dag == null) {
                    System.err.println("DAG not found in scheduler context: " + dagId);
                    return;
                }

                // create a dedicated executor for the run (or reuse shared one)
                ExecutorService runExecutor = Executors.newFixedThreadPool(4);
                dag.run(runExecutor);
                runExecutor.shutdown();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }
}

