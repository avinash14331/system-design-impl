package org.avi.javaairflowlite.service;

import org.avi.javaairflowlite.entity.DagEntity;
import org.avi.javaairflowlite.repository.DagRepository;
import org.quartz.*;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Set;

@Service
public class SchedulerService {
    private final Scheduler quartz;
    private final DagRepository dagRepo;

    public SchedulerService(Scheduler quartz, DagRepository dagRepo) { this.quartz = quartz; this.dagRepo = dagRepo; }

    @PostConstruct
    public void scheduleAll() throws Exception {
        for (DagEntity d : dagRepo.findAll()) scheduleDag(d);
    }

    public void scheduleDag(DagEntity dag) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(QuartzDagJob.class)
                .withIdentity(dag.getId(), "dag-jobs")
                .usingJobData("dagId", dag.getId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(dag.getId() + "-trigger", "dag-triggers")
                .withSchedule(CronScheduleBuilder.cronSchedule(dag.getSchedule()))
                .build();

        quartz.scheduleJob(job, trigger);
    }

    public static class QuartzDagJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            try {
                String dagId = context.getJobDetail().getKey().getName();
                var appCtx = (org.springframework.context.ApplicationContext) context.getScheduler().getContext().get("applicationContext");
                ExecutionService exec = appCtx.getBean(ExecutionService.class);
                exec.trigger(dagId);
            } catch (Exception e) {
                throw new JobExecutionException(e);
            }
        }
    }
}
