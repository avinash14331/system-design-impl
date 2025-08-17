package org.avi.javaairflowlite.runner;

//import org.avi.javaairflowlite.dag.DAG;
//import org.avi.javaairflowlite.dag.DagLoader;
//import org.avi.javaairflowlite.service.DagSchedulerService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class AirflowLiteRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Starting java-airflow-lite...");
//commenting to use spring persistence version
//        DagLoader loader = new DagLoader();
//        DAG dag = loader.loadFromResource("/dags/sample-dag.yaml");
//
//        DagSchedulerService sched = new DagSchedulerService();
//        sched.scheduleDag(dag);
//
//        // keep main alive (in real app, run as service)
//        Thread.currentThread().join();
    }
}
